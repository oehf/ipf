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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Settings of the streaming upload split, see {@link Base64SplittingFilter}.
 * <p>
 * These are the only settings the split needs; whether it is active at all, and where the values come from, is up
 * to whoever registers the filter. The defaults are meant to be usable as they are &mdash; see
 * {@link #defaults()}.
 *
 * @param engageAboveBytes     only request bodies larger than this are split; smaller ones are passed through byte
 *                             for byte, so that ordinary requests behave exactly as they do without the filter.
 *                             This is measured on the <em>whole request body</em>, i.e. metadata plus the base64
 *                             of all documents in it.
 * @param memoryThresholdBytes extracted document content up to this size is kept in memory instead of being
 *                             written to a temporary file. This is measured on a <em>single decoded document</em>
 *                             and is therefore deliberately independent of {@code engageAboveBytes}; it is quite
 *                             normal for it to be the larger of the two. Splitting the body is what avoids the
 *                             expensive part &mdash; the base64 text and the decoded array that the FHIR parser
 *                             would otherwise materialize &mdash; whereas a temporary file is only needed to also
 *                             avoid a large contiguous heap allocation. Small documents therefore stay in memory
 *                             and cause no disk I/O at all.
 * @param maxDocumentSizeBytes maximum size of a single extracted document. Bounds how much temporary disk space
 *                             one request can use.
 * @param tempDirectory        directory for temporary document files
 * @param inlineDocumentResources
 *                             types of the resources whose {@code create} interaction carries a document inline,
 *                             i.e. the resource types below the FHIR base for which a {@code POST} is a candidate
 *                             for the split. A {@code POST} to the FHIR base itself always is one, because a
 *                             transaction bundle may contain anything. See
 *                             {@link #MHD_INLINE_DOCUMENT_RESOURCES} for the value that covers MHD.
 *
 * @author Christian Ohr
 */
public record StreamingUploadOptions(
        int engageAboveBytes,
        int memoryThresholdBytes,
        long maxDocumentSizeBytes,
        Path tempDirectory,
        Set<String> inlineDocumentResources) {

    public static final int DEFAULT_ENGAGE_ABOVE_BYTES = 256 * 1024;

    public static final int DEFAULT_MEMORY_THRESHOLD_BYTES = 512 * 1024;

    public static final long DEFAULT_MAX_DOCUMENT_SIZE_BYTES = 128L * 1024 * 1024;

    /**
     * The resource types that carry a document inline in MHD: only ITI-105 does, as a {@code DocumentReference}
     * with the document in {@code content.attachment.data}. ITI-65 arrives as a transaction bundle posted to the
     * FHIR base instead and is therefore covered without being named here. The remaining MHD transactions that
     * accept a {@code DocumentReference} body &mdash; the ITI-57 based updates &mdash; are metadata-only, so they
     * are of no interest here even though they are uploads.
     */
    public static final Set<String> MHD_INLINE_DOCUMENT_RESOURCES = Set.of("DocumentReference");

    public StreamingUploadOptions {
        if (engageAboveBytes < 0) {
            throw new IllegalArgumentException("engageAboveBytes must not be negative");
        }
        if (memoryThresholdBytes < 0) {
            throw new IllegalArgumentException("memoryThresholdBytes must not be negative");
        }
        if (maxDocumentSizeBytes <= 0) {
            throw new IllegalArgumentException("maxDocumentSizeBytes must be positive");
        }
        if (tempDirectory == null) {
            throw new IllegalArgumentException("tempDirectory must not be null");
        }
        if (inlineDocumentResources == null) {
            throw new IllegalArgumentException("inlineDocumentResources must not be null");
        }
        inlineDocumentResources = Set.copyOf(inlineDocumentResources);
    }

    /**
     * @return options that split MHD uploads into the JVM temporary directory, with the thresholds the
     *         measurements this implementation was built on found reasonable
     */
    public static StreamingUploadOptions defaults() {
        return new StreamingUploadOptions(DEFAULT_ENGAGE_ABOVE_BYTES, DEFAULT_MEMORY_THRESHOLD_BYTES,
                DEFAULT_MAX_DOCUMENT_SIZE_BYTES, defaultTempDirectory(), MHD_INLINE_DOCUMENT_RESOURCES);
    }

    /**
     * @return the JVM temporary directory
     */
    public static Path defaultTempDirectory() {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }
}
