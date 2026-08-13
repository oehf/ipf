/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.streaming;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.servlet.ServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Holds the document content that has been split out of a FHIR request body before the body was handed to the
 * FHIR parser, for the duration of one request.
 * <p>
 * For every extracted document the registry mints a short <em>marker</em>: a few dozen bytes that take the place
 * of the original content in the slimmed-down request body. The marker is valid base64 content, so the FHIR
 * parser accepts it as an ordinary {@code base64Binary} value and it survives resource copying and reordering
 * unchanged &mdash; it simply travels with the resource as its {@code data}. When the MHD-to-XDS translator
 * finally needs the document it hands the parsed {@code data} back here via {@link #resolve(byte[])} and gets
 * the streamed content instead.
 * <p>
 * The registry is put on the servlet request under {@link #REQUEST_ATTRIBUTE} and owns temporary files, so it
 * must be {@link #close() closed} once the request has been processed.
 *
 * @see StagedContent
 */
public final class StagedContentRegistry implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(StagedContentRegistry.class);

    /**
     * Name of the servlet request attribute under which the registry of the current request is published.
     */
    public static final String REQUEST_ATTRIBUTE = StagedContentRegistry.class.getName();

    /**
     * Marker prefix. Starts with a NUL byte so that it cannot collide with the beginning of any real document,
     * and is followed by a per-request nonce so that a client cannot craft a marker itself.
     */
    private static final byte[] MARKER_PREFIX = {0x00, 'S', 'T', 'A', 'G', 'E', 'D', 0x00};

    private static final SecureRandom RANDOM = new SecureRandom();

    private final StreamingUploadOptions options;
    private final String nonce;

    private final Map<String, StagedContent> staged = new LinkedHashMap<>();
    private final Set<String> resolved = new HashSet<>();
    private int counter;

    public StagedContentRegistry(StreamingUploadOptions options) {
        this.options = options;
        var bytes = new byte[8];
        RANDOM.nextBytes(bytes);
        this.nonce = Hex.encodeHexString(bytes);
    }

    /**
     * @param request the request being processed, may be {@code null}
     * @return the registry of that request, or empty if its body was not split
     */
    public static Optional<StagedContentRegistry> forRequest(ServletRequest request) {
        return request == null
                ? Optional.empty()
                : Optional.ofNullable((StagedContentRegistry) request.getAttribute(REQUEST_ATTRIBUTE));
    }

    /**
     * Looks up the registry from the parameter map that a FHIR resource provider hands to a translator or route,
     * which is where a consumer of the parsed resource normally starts from. Equivalent to
     * {@link #forRequest(ServletRequest)} on the servlet request behind
     * {@link Constants#FHIR_REQUEST_DETAILS}.
     *
     * @param parameters parameter map of the FHIR request, may be {@code null}
     * @return the registry of that request, or empty if its body was not split, if the request did not arrive
     *         through a servlet, or if the parameters do not carry any request details at all
     */
    public static Optional<StagedContentRegistry> forParameters(Map<String, Object> parameters) {
        if (parameters == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(parameters.get(Constants.FHIR_REQUEST_DETAILS))
                .filter(ServletRequestDetails.class::isInstance)
                .map(ServletRequestDetails.class::cast)
                .flatMap(requestDetails -> forRequest(requestDetails.getServletRequest()));
    }

    /**
     * Creates and registers a new sink for document content.
     *
     * @return the staged content, to be filled through {@link StagedContent#sink()}
     */
    public StagedContent stage() {
        var token = nonce + '-' + counter++;
        var content = new StagedContent(token, options.memoryThresholdBytes(), options.maxDocumentSizeBytes(),
                options.tempDirectory());
        staged.put(token, content);
        return content;
    }

    /**
     * @param token token of a previously staged content
     * @return the marker bytes to write into the slimmed-down request body in place of the content
     */
    public byte[] marker(String token) {
        var tokenBytes = token.getBytes(StandardCharsets.US_ASCII);
        var marker = new byte[MARKER_PREFIX.length + tokenBytes.length];
        System.arraycopy(MARKER_PREFIX, 0, marker, 0, MARKER_PREFIX.length);
        System.arraycopy(tokenBytes, 0, marker, MARKER_PREFIX.length, tokenBytes.length);
        return marker;
    }

    /**
     * Resolves the {@code base64Binary} value of a parsed {@code Binary.data} or {@code Attachment.data} back
     * to the content that was streamed out of the request body.
     *
     * @param data value as parsed by the FHIR parser
     * @return the staged content, or empty if {@code data} is not a marker of this registry, in which case it
     *         is the document content itself
     */
    public Optional<StagedContent> resolve(byte[] data) {
        if (data == null
                || data.length <= MARKER_PREFIX.length
                || !Arrays.equals(MARKER_PREFIX, 0, MARKER_PREFIX.length, data, 0, MARKER_PREFIX.length)) {
            return Optional.empty();
        }
        var token = new String(data, MARKER_PREFIX.length, data.length - MARKER_PREFIX.length, StandardCharsets.US_ASCII);
        var content = staged.get(token);
        if (content == null) {
            // Cannot happen for a marker minted by this registry; treat as ordinary content.
            log.warn("Request body contained an unknown staged content marker");
            return Optional.empty();
        }
        resolved.add(token);
        return Optional.of(content);
    }

    /**
     * @return true if content was staged but never resolved by a translator, which means it silently did not
     *         make it into the backend request
     */
    public boolean hasUnresolvedContent() {
        return resolved.size() != staged.size();
    }

    /**
     * @return the number of documents that were streamed out of the request body
     */
    public int size() {
        return staged.size();
    }

    /**
     * @return all staged content, in the order in which it appeared in the request body
     */
    public Collection<StagedContent> contents() {
        return staged.values();
    }

    /**
     * Removes all state connected with an instance of this class.
     * As this class is {@link Closeable}, this is designed to be called at the end of
     * a try-with-resources statement.
     */
    @Override
    public void close() {
        staged.values().forEach(StagedContent::close);
        staged.clear();
        resolved.clear();
    }
}
