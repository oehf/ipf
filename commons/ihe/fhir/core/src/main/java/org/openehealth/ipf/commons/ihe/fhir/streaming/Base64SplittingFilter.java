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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;
import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;

/**
 * Splits the document content out of a large FHIR upload before the body reaches the FHIR parser.
 * <p>
 * For {@code Binary.data} (ITI-65) and {@code DocumentReference.content.attachment.data} (ITI-105), HAPI
 * materializes the base64 text as a {@link String} <em>and</em> the decoded array in
 * {@code Base64BinaryType}, on top of the raw request bytes &mdash; several times the wire size of the document,
 * allocated in one burst per concurrent upload. That is the one respect in which a FHIR upload is worse than an
 * MTOM/XOP SOAP attachment, where CXF hands the application a file-backed
 * {@link jakarta.activation.DataHandler} and nothing large ever touches the heap.
 * <p>
 * This filter restores the MTOM behavior without changing the wire format. The body is copied by a
 * {@link BodySplitter}, which decodes the document content incrementally into a {@link StagedContent} and leaves
 * a short marker in its place. The FHIR parser then only sees a few kilobytes of metadata, and whoever consumes
 * the parsed resource exchanges the marker for the streamed content again &mdash; see
 * {@link StagedContentRegistry#forParameters(Map)} and {@link StagedContentRegistry#resolve(byte[])}.
 * <p>
 * Requests below {@link StreamingUploadOptions#engageAboveBytes()} are passed through unchanged, so only large
 * uploads take this path.
 * <p>
 * The filter must run after authentication &mdash; otherwise unauthenticated requests would be spooled to
 * disk &mdash; and before any request logging, which then logs the slimmed-down body instead of buffering the
 * full one.
 *
 * <h2>Getting hold of the document content</h2>
 *
 * Everything downstream of this filter &mdash; a HAPI resource provider, an IPF FHIR translator, a Camel
 * route &mdash; sees a parsed resource whose {@code Binary.data} or {@code Attachment.data} is the
 * <em>marker</em>, not the document. The content itself is fetched from the
 * {@link StagedContentRegistry} of the current request, which the filter publishes as the servlet request
 * attribute {@link StagedContentRegistry#REQUEST_ATTRIBUTE}. There are two ways in, depending on what the
 * consumer has at hand:
 * <ul>
 *     <li>{@link StagedContentRegistry#forParameters(Map)} &mdash; from the parameter map that a FHIR resource
 *     provider passes to a translator or route, which is the usual starting point</li>
 *     <li>{@link StagedContentRegistry#forRequest(jakarta.servlet.ServletRequest)} &mdash; from the servlet
 *     request itself</li>
 * </ul>
 * Handing the parsed {@code data} to {@link StagedContentRegistry#resolve(byte[])} then exchanges the marker
 * for the {@link StagedContent}, from which the document is read as a stream:
 * <pre>{@code
 * var content = StagedContentRegistry.forParameters(parameters)
 *         .flatMap(registry -> registry.resolve(binary.getData()));
 * if (content.isPresent()) {
 *     // Streams from memory or from the temporary file, whichever this document ended up in.
 *     DataHandler document = content.get().dataHandler(binary.getContentType());
 *     ...
 * } else {
 *     // Not split: binary.getData() is the document content itself.
 * }
 * }</pre>
 * Both branches have to be there. A request below {@link StreamingUploadOptions#engageAboveBytes()}, or one
 * that arrived on a non-servlet transport, never passes this filter, so an empty {@code Optional} is the normal
 * case rather than an error &mdash; it simply means the value in hand is already the document.
 * <p>
 * Besides the content, {@link StagedContent} also carries what was measured while the bytes streamed past:
 * {@code getSize()} and {@code getSha1Hex()}, the latter being exactly the digest XDS expects, so a
 * client-supplied one can be verified without a second pass over the document.
 * <p>
 * <strong>The content lives no longer than the request.</strong> The registry is closed, and any temporary file
 * deleted, as soon as this filter returns, i.e. once the response has been produced. A consumer must therefore
 * read the stream (or copy it somewhere it owns) while the request is being processed on the calling thread;
 * a {@link jakarta.activation.DataHandler} handed to another thread or kept beyond the response reads from
 * content that is already gone. Every staged document is expected to be resolved exactly once by whoever
 * handles the request; content that is never picked up is reported as a warning, because it means the document
 * silently did not make it into the backend request.
 *
 * @author Christian Ohr
 * @see JsonBodySplitter
 * @see XmlBodySplitter
 * @see StagedContentRegistry
 * @see StagedContent
 */
public class Base64SplittingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(Base64SplittingFilter.class);

    private static final Set<String> GZIP_ENCODINGS = Set.of("gzip", "x-gzip");

    /**
     * {@code OperationOutcome.issue.code} values of the rejections this filter answers itself, as codes rather
     * than as enum constants of a particular FHIR version. Both are defined identically in every version this
     * filter can be used with.
     */
    private static final String ISSUE_CODE_STRUCTURE = "structure";
    private static final String ISSUE_CODE_TOO_COSTLY = "too-costly";

    private final StreamingUploadOptions options;
    private final FhirContext fhirContext;
    private final String fhirBasePath;
    private final UnaryOperator<String> diagnosticsPolicy;
    private final StreamedDocumentListener listener;

    private final BodySplitter jsonSplitter = new JsonBodySplitter();
    private final BodySplitter xmlSplitter = new XmlBodySplitter();

    private final Map<String, BodySplitter> splitters = Map.of(
            "application/fhir+json", jsonSplitter,
            "application/json+fhir", jsonSplitter,
            "application/json", jsonSplitter,
            "text/json", jsonSplitter,
            "application/fhir+xml", xmlSplitter,
            "application/xml+fhir", xmlSplitter,
            "application/xml", xmlSplitter,
            "text/xml", xmlSplitter);

    /**
     * @param options      tuning parameters of the split
     * @param fhirContext  FHIR context, used to serialize the {@code OperationOutcome} of a rejected request
     * @param fhirBasePath path of the FHIR endpoint below the context path, e.g. {@code /fhir}
     */
    public Base64SplittingFilter(StreamingUploadOptions options, FhirContext fhirContext, String fhirBasePath) {
        this(options, fhirContext, fhirBasePath, UnaryOperator.identity(), StreamedDocumentListener.NOOP);
    }

    /**
     * @param options           tuning parameters of the split
     * @param fhirContext       FHIR context, used to serialize the {@code OperationOutcome} of a rejected request
     * @param fhirBasePath      path of the FHIR endpoint below the context path, e.g. {@code /fhir}
     * @param diagnosticsPolicy applied to the diagnostics of a rejection before it is sent, so that an application
     *                          which does not disclose error details to its clients can redact them here as well.
     *                          The filter answers such requests itself and therefore bypasses whatever the FHIR
     *                          servlet would have applied.
     * @param listener          notified about every document that was streamed out of a request body
     */
    public Base64SplittingFilter(StreamingUploadOptions options,
                                 FhirContext fhirContext,
                                 String fhirBasePath,
                                 UnaryOperator<String> diagnosticsPolicy,
                                 StreamedDocumentListener listener) {
        this.options = options;
        this.fhirContext = fhirContext;
        this.fhirBasePath = stripTrailingSlash(fhirBasePath);
        this.diagnosticsPolicy = diagnosticsPolicy;
        this.listener = listener;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !carriesInlineDocument(request)
                || splitterFor(request) == null
                || !isUtf8(request);
    }

    /**
     * Both splitters produce UTF-8, and the XML one scans raw bytes, so a body declaring some other charset would
     * be re-encoded or misread while its {@code Content-Type} still says otherwise. FHIR mandates UTF-8, but
     * since the charset is known from the header alone such a request can simply be declined before anything has
     * been read, leaving it on the ordinary parsing path where HAPI honours the declared charset.
     */
    private static boolean isUtf8(HttpServletRequest request) {
        var charset = request.getCharacterEncoding();
        return charset == null || StandardCharsets.UTF_8.name().equalsIgnoreCase(charset.trim());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Resolve the splitter before touching the body: once bytes have been read they can no longer be handed
        // on untouched, so anything that makes us decline has to be decided up front.
        var splitter = splitterFor(request);
        if (splitter == null) {
            log.warn("Unrecognized content-type: {}. Don't know how to split this request", request.getContentType());
            chain.doFilter(request, response);
            return;
        }

        var body = decodedBody(request);

        // Read the first chunk to find out whether this request is large enough to bother. If the whole body
        // fits, it is forwarded byte for byte, which keeps the behaviour (including error handling for
        // malformed bodies) of ordinary requests completely unchanged.
        var head = read(body, options.engageAboveBytes() + 1);
        if (head.length <= options.engageAboveBytes()) {
            chain.doFilter(new ReplacedBodyRequest(request, head), response);
            return;
        }

        if (splitter.encoding() == EncodingEnum.XML && !XmlBodySplitter.isSupportedEncoding(head)) {
            log.warn("Rejecting upload: XML request body is not UTF-8 encoded");
            sendOperationOutcome(request, response, HttpStatus.BAD_REQUEST, splitter.encoding(),
                    ISSUE_CODE_STRUCTURE, "XML request bodies must be UTF-8 encoded");
            return;
        }

        splitAndForward(request, response, chain, splitter, head, body);
    }

    /**
     * @return the request body, transparently decompressed if it arrived gzipped
     */
    private static InputStream decodedBody(HttpServletRequest request) throws IOException {
        var body = request.getInputStream();
        return isGzipped(request) ? new GZIPInputStream(body) : body;
    }

    /**
     * Splits the body and passes the remaining metadata down the chain, for the lifetime of one
     * {@link StagedContentRegistry} &mdash; which owns the extracted content and therefore has to outlive the
     * downstream processing of the request.
     *
     * @param head the leading bytes that have already been read to size the request up
     * @param rest the remainder of the body, still unread
     */
    private void splitAndForward(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                 BodySplitter splitter, byte[] head, InputStream rest)
            throws ServletException, IOException {

        try (var registry = new StagedContentRegistry(options)) {

            byte[] slimBody;
            try (var in = new SequenceInputStream(new ByteArrayInputStream(head), rest)) {
                slimBody = splitter.split(in, registry);
            } catch (StagedContent.DocumentTooLargeException e) {
                log.warn("Rejecting upload: {}", e.getMessage());
                sendOperationOutcome(request, response, HttpStatus.CONTENT_TOO_LARGE, splitter.encoding(),
                        ISSUE_CODE_TOO_COSTLY, e.getMessage());
                return;
            } catch (BodySplitter.MalformedBodyException e) {
                // The body would have been rejected by the FHIR parser as well. It cannot be forwarded verbatim
                // because it has already been consumed.
                log.debug("Rejecting malformed upload", e);
                sendOperationOutcome(request, response, HttpStatus.BAD_REQUEST, splitter.encoding(),
                        ISSUE_CODE_STRUCTURE, e.getMessage());
                return;
            }

            if (registry.size() == 0) {
                log.debug("Large upload contained no document content to split off");
                chain.doFilter(new ReplacedBodyRequest(request, slimBody), response);
                return;
            }

            forwardWithStagedContent(request, response, chain, registry, slimBody);
        }
    }

    /**
     * Publishes the registry on the request so that the consumer of the parsed resource can exchange the markers
     * for the extracted content again, and forwards the slimmed-down body.
     */
    private void forwardWithStagedContent(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, StagedContentRegistry registry, byte[] slimBody)
            throws ServletException, IOException {

        registry.contents().forEach(content -> listener.documentStreamed(content.getSize()));
        log.debug("Split {} document(s) out of the request body, {} bytes of metadata remain",
                registry.size(), slimBody.length);
        request.setAttribute(StagedContentRegistry.REQUEST_ATTRIBUTE, registry);
        chain.doFilter(new ReplacedBodyRequest(request, slimBody), response);

        if (registry.hasUnresolvedContent() && !response.isCommitted()) {
            log.warn("Not all document content that was split off the request body was picked up by the " +
                    "translator; the backend request may be incomplete");
        }
    }

    /**
     * Reads at most {@code limit} bytes. A shorter result means the stream was exhausted.
     * <p>
     * The buffer is allocated at its full size once instead of being grown into: geometric growth would allocate
     * roughly twice the limit in intermediate arrays and then copy once more to trim, on every request that gets
     * this far. The price is that a small eligible request also allocates the full limit, which is bounded, short
     * lived and never escapes the young generation.
     */
    private static byte[] read(InputStream in, int limit) throws IOException {
        var buffer = new byte[limit];
        var filled = 0;
        while (filled < limit) {
            var read = in.read(buffer, filled, limit - filled);
            if (read < 0) {
                break;
            }
            filled += read;
        }
        return filled == limit ? buffer : Arrays.copyOf(buffer, filled);
    }

    /**
     * Answers the request directly, without involving the FHIR servlet. Two things the servlet would have done
     * therefore have to be done here:
     * <ul>
     *     <li>content negotiation, so that a client which posted XML is not answered in JSON</li>
     *     <li>the application's diagnostics policy, which is bypassed along with the servlet and is therefore
     *     supplied to the constructor</li>
     * </ul>
     * The outcome is assembled through {@link OperationOutcomeUtil} rather than from the model classes of one
     * FHIR version, so that this filter works for every version the {@link FhirContext} may be configured for.
     */
    private void sendOperationOutcome(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
                                      EncodingEnum requestEncoding, String code,
                                      String diagnostics) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        IBaseOperationOutcome outcome = OperationOutcomeUtil.newInstance(fhirContext);
        OperationOutcomeUtil.addIssue(fhirContext, outcome,
                OperationOutcomeUtil.OO_SEVERITY_ERROR,
                escapeHtml4(diagnosticsPolicy.apply(diagnostics)),
                null,
                code);

        var encoding = responseEncoding(request, requestEncoding);
        response.reset();
        response.setStatus(status.value());
        response.setContentType(encoding.getResourceContentTypeNonLegacy());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(encoding.newParser(fhirContext).encodeResourceToString(outcome));
    }

    /**
     * Picks the response format the way the FHIR servlet would: the {@code _format} parameter first, then the
     * highest ranked {@code Accept} option - q values and all, courtesy of
     * {@link RestfulServerUtils#parseAcceptHeaderAndReturnHighestRankedOptions(HttpServletRequest)} - and finally
     * the format the request itself arrived in.
     *
     * @param request         the request being answered
     * @param requestEncoding encoding of the request body, used when nothing else settles the question
     * @return the encoding to answer in, never {@code null}
     */
    private static EncodingEnum responseEncoding(HttpServletRequest request, EncodingEnum requestEncoding) {
        var format = EncodingEnum.forContentType(request.getParameter(Constants.PARAM_FORMAT));
        if (isJsonOrXml(format)) {
            return format;
        }
        var accepted = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(request).stream()
                .map(EncodingEnum::forContentType)
                .filter(Base64SplittingFilter::isJsonOrXml)
                .collect(Collectors.toSet());
        // The ranked options are unordered, so a tie between the two is settled by the request's own format
        return accepted.size() == 1 ? accepted.iterator().next() : requestEncoding;
    }

    private static boolean isJsonOrXml(EncodingEnum encoding) {
        return encoding == EncodingEnum.JSON || encoding == EncodingEnum.XML;
    }

    /**
     * Decides from the request method and the path below the FHIR base whether this interaction can carry a
     * document inline, rather than from a configured list of paths. Two kinds can:
     * <ul>
     *     <li>{@code POST} to the FHIR base itself - a transaction bundle, which for ITI-65 contains the
     *     {@code Binary} resources</li>
     *     <li>{@code POST} to one of {@link StreamingUploadOptions#inlineDocumentResources()} - for MHD that is
     *     the ITI-105 create of a {@code DocumentReference}, with the document in
     *     {@code content.attachment.data}</li>
     * </ul>
     * Everything else, including the metadata-only updates and every read or search, is left alone. Note that a
     * {@code PUT} never qualifies: the MHD update transactions do not transport content.
     */
    private boolean carriesInlineDocument(HttpServletRequest request) {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            return false;
        }
        var resourcePath = resourcePath(request);
        return resourcePath != null
                && (resourcePath.isEmpty() || options.inlineDocumentResources().contains(resourcePath));
    }

    /**
     * @return the request path below the FHIR base with any surrounding slashes removed - empty for the base
     *         itself - or {@code null} if the request does not address the FHIR endpoint at all
     */
    private String resourcePath(HttpServletRequest request) {
        var path = request.getRequestURI().substring(request.getContextPath().length());
        if (!path.startsWith(fhirBasePath)) {
            return null;
        }
        var relative = path.substring(fhirBasePath.length());
        if (!relative.isEmpty() && relative.charAt(0) != '/') {
            // A different endpoint that merely starts with the same characters
            return null;
        }
        return stripTrailingSlash(relative.isEmpty() ? relative : relative.substring(1));
    }

    private static String stripTrailingSlash(String path) {
        return path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    /**
     * @return the splitter for the request's content type, or {@code null} if the format is not handled
     */
    private BodySplitter splitterFor(HttpServletRequest request) {
        var contentType = request.getContentType();
        if (contentType == null) {
            return null;
        }
        return splitters.get(contentType.split(";")[0].trim().toLowerCase(Locale.ROOT));
    }

    private static boolean isGzipped(HttpServletRequest request) {
        var encoding = request.getHeader(CONTENT_ENCODING);
        return encoding != null && GZIP_ENCODINGS.contains(encoding.trim().toLowerCase(Locale.ROOT));
    }

}
