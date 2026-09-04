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
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.activation.DataHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Signature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the document content of a large upload is streamed past the FHIR parser and can be recovered from
 * the staged content registry while the request is being processed.
 * <p>
 * Note that the registry only lives for the duration of the request, so all assertions about staged content
 * have to be made from inside the filter chain.
 */
class Base64SplittingFilterTest {

    private static final String CONTEXT_PATH = "/ipf";
    private static final String FHIR_JSON = "application/fhir+json";
    private static final String FHIR_XML = "application/fhir+xml";
    private static final String CONTENT_TYPE = "application/pdf";
    private static final String BINARY_FULL_URL = "urn:uuid:11111111-1111-1111-1111-111111111111";
    private static final int DOCUMENT_SIZE = 1024 * 1024;

    private static FhirContext fhirContext;

    @TempDir
    Path tempDirectory;

    private String fhirBasePath;
    private int engageAboveBytes;
    private int memoryThresholdBytes;
    private long maxDocumentSizeBytes;
    private Set<String> inlineDocumentResources;
    private UnaryOperator<String> diagnosticsPolicy;
    private final List<Long> streamedSizes = new ArrayList<>();

    @BeforeAll
    static void setupClass() {
        fhirContext = FhirContext.forR4Cached();
    }

    @BeforeEach
    void setup() {
        fhirBasePath = "/fhir";
        diagnosticsPolicy = UnaryOperator.identity();
        engageAboveBytes = StreamingUploadOptions.DEFAULT_ENGAGE_ABOVE_BYTES;
        maxDocumentSizeBytes = StreamingUploadOptions.DEFAULT_MAX_DOCUMENT_SIZE_BYTES;
        inlineDocumentResources = StreamingUploadOptions.MHD_INLINE_DOCUMENT_RESOURCES;
        // Low enough that a 1 MiB document is spilled to a temporary file
        memoryThresholdBytes = 64 * 1024;
    }

    private Base64SplittingFilter filter() {
        var options = new StreamingUploadOptions(engageAboveBytes, memoryThresholdBytes, maxDocumentSizeBytes,
                tempDirectory, inlineDocumentResources);
        return new Base64SplittingFilter(options, fhirContext, fhirBasePath, diagnosticsPolicy, streamedSizes::add);
    }

    @Test
    void iti65BinaryContentIsStreamedOutOfTheRequestBody() throws Exception {
        var document = randomBytes(DOCUMENT_SIZE);

        filter(request("/fhir", json(provideDocumentBundle(document))), forwarded -> {
            var registry = registryOf(forwarded);
            assertNotNull(registry, "Document content should have been split off");
            assertEquals(1, registry.size());

            var staged = registry.contents().iterator().next();
            assertEquals(DOCUMENT_SIZE, staged.getSize());
            assertEquals(sha1Hex(document), staged.getSha1Hex(), "SHA-1 is computed while streaming");
            assertTrue(staged.isSpilled(), "1 MiB should have been spilled to a temporary file");

            // The body handed on to the FHIR parser carries the metadata only
            var slimBody = bodyOf(forwarded);
            assertTrue(slimBody.length < 4096, "Expected metadata only, but got " + slimBody.length + " bytes");
            assertEquals(slimBody.length, forwarded.getContentLength());

            // ... and the content is recovered from the marker that took its place
            var parsed = (Bundle) parse(slimBody);
            var marker = binaryOf(parsed).getData();
            assertTrue(registry.hasUnresolvedContent(), "Nothing resolved yet");
            var resolved = registry.resolve(marker);
            assertTrue(resolved.isPresent(), "Marker should resolve to the staged content");
            assertFalse(registry.hasUnresolvedContent(), "All staged content resolved");
            assertArrayEquals(document, readFully(resolved.get().dataHandler(CONTENT_TYPE)));
            assertEquals(CONTENT_TYPE, resolved.get().dataHandler(CONTENT_TYPE).getContentType());
        });
    }

    @Test
    void iti105InlineAttachmentIsStreamedOutOfTheRequestBody() throws Exception {
        var document = randomBytes(DOCUMENT_SIZE);
        var documentReference = new DocumentReference();
        documentReference.addContent().setAttachment(new Attachment()
                .setContentType(CONTENT_TYPE)
                .setData(document));

        filter(request("/fhir/DocumentReference", json(documentReference)), forwarded -> {
            var registry = registryOf(forwarded);
            assertNotNull(registry);
            assertEquals(1, registry.size());

            var parsed = (DocumentReference) parse(bodyOf(forwarded));
            var marker = parsed.getContentFirstRep().getAttachment().getData();
            var resolved = registry.resolve(marker);
            assertTrue(resolved.isPresent());
            assertArrayEquals(document, readFully(resolved.get().dataHandler(CONTENT_TYPE)));
        });
    }

    /**
     * An ITI-65 bundle may carry several documents. Each gets its own marker, so the pairing back to the right
     * {@code DocumentReference} does not depend on the order they appear in.
     */
    @Test
    void severalBinariesInOneBundleAreStreamedOutIndependently() throws Exception {
        var first = randomBytes(DOCUMENT_SIZE);
        var second = randomBytes(DOCUMENT_SIZE / 2);
        var bundle = provideDocumentBundle(first);
        bundle.addEntry().setFullUrl("urn:uuid:44444444-4444-4444-4444-444444444444")
                .setResource(new Binary().setContentType(CONTENT_TYPE).setData(second));

        filter(request("/fhir", json(bundle)), forwarded -> {
            var registry = registryOf(forwarded);
            assertEquals(2, registry.size());

            var parsed = (Bundle) parse(bodyOf(forwarded));
            var binaries = parsed.getEntry().stream()
                    .map(Bundle.BundleEntryComponent::getResource)
                    .filter(Binary.class::isInstance)
                    .map(Binary.class::cast)
                    .toList();
            assertEquals(2, binaries.size());

            // Each marker resolves to its own content, and the markers differ
            var firstResolved = registry.resolve(binaries.get(0).getData()).orElseThrow();
            var secondResolved = registry.resolve(binaries.get(1).getData()).orElseThrow();
            assertArrayEquals(first, readFully(firstResolved.dataHandler(CONTENT_TYPE)));
            assertArrayEquals(second, readFully(secondResolved.dataHandler(CONTENT_TYPE)));
            assertFalse(registry.hasUnresolvedContent());
        });
    }

    @Test
    void iti65XmlBinaryContentIsStreamedOutOfTheRequestBody() throws Exception {
        var document = randomBytes(DOCUMENT_SIZE);
        var request = request("/fhir", xml(provideDocumentBundle(document)));
        request.setContentType(FHIR_XML);

        filter(request, forwarded -> {
            var registry = registryOf(forwarded);
            assertNotNull(registry, "Document content should have been split off");
            assertEquals(1, registry.size());
            assertEquals(sha1Hex(document), registry.contents().iterator().next().getSha1Hex());

            var slimBody = bodyOf(forwarded);
            assertTrue(slimBody.length < 4096, "Expected metadata only, but got " + slimBody.length + " bytes");

            // The slimmed body is still valid FHIR XML, and the marker resolves to the streamed content
            var parsed = (Bundle) fhirContext.newXmlParser()
                    .parseResource(new String(slimBody, StandardCharsets.UTF_8));
            var resolved = registry.resolve(binaryOf(parsed).getData());
            assertTrue(resolved.isPresent(), "Marker should resolve to the staged content");
            assertArrayEquals(document, readFully(resolved.get().dataHandler(CONTENT_TYPE)));
        });
    }

    @Test
    void nonUtf8XmlIsRejected() throws Exception {
        var body = ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                + new String(xml(provideDocumentBundle(randomBytes(DOCUMENT_SIZE))), StandardCharsets.UTF_8))
                .getBytes(StandardCharsets.UTF_8);
        var request = request("/fhir", body);
        request.setContentType(FHIR_XML);

        var response = new MockHttpServletResponse();
        filter().doFilter(request, response,
                (req, res) -> fail("The request must not have been forwarded"));

        assertEquals(400, response.getStatus());
        assertTrue(response.getContentAsString().contains("UTF-8"));
    }

    /**
     * The filter answers some requests itself, bypassing the FHIR servlet and whatever diagnostics policy the
     * application applies there, so it has to apply the one it was given on its own.
     */
    @Test
    void theDiagnosticsPolicyIsAppliedToRejections() throws Exception {
        diagnosticsPolicy = diagnostics -> "Error diagnostics removed";
        maxDocumentSizeBytes = DOCUMENT_SIZE / 2L;
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        var response = new MockHttpServletResponse();
        filter()
                .doFilter(request("/fhir", body), response, (req, res) -> fail("Must not have been forwarded"));

        assertEquals(413, response.getStatus());
        var outcome = (OperationOutcome) parse(response.getContentAsByteArray());
        assertEquals("Error diagnostics removed", outcome.getIssueFirstRep().getDiagnostics());
        assertEquals(OperationOutcome.IssueSeverity.ERROR, outcome.getIssueFirstRep().getSeverity());
        // The filter assembles the outcome version-independently, from issue code strings rather than from the
        // enum constants of one FHIR version, so pin the code that ends up on the wire
        assertEquals(OperationOutcome.IssueType.TOOCOSTLY, outcome.getIssueFirstRep().getCode());
    }

    /**
     * The filter answers before the servlet gets a chance to negotiate, so it has to negotiate itself. A client
     * that posted XML must not be answered in JSON.
     */
    @Test
    void rejectionsAreAnsweredInTheFormatTheRequestArrivedIn() throws Exception {
        maxDocumentSizeBytes = DOCUMENT_SIZE / 2L;

        assertRejectedWith(FHIR_XML, null, null, FHIR_XML);
        assertRejectedWith(FHIR_JSON, null, null, FHIR_JSON);
    }

    @Test
    void rejectionsHonourAcceptAndFormatOverTheRequestFormat() throws Exception {
        maxDocumentSizeBytes = DOCUMENT_SIZE / 2L;

        // Accept wins over the request format ...
        assertRejectedWith(FHIR_JSON, FHIR_XML, null, FHIR_XML);
        assertRejectedWith(FHIR_XML, FHIR_JSON, null, FHIR_JSON);
        // ... q values are respected ...
        assertRejectedWith(FHIR_JSON, FHIR_XML + ";q=0.9, " + FHIR_JSON + ";q=0.1", null, FHIR_XML);
        // ... _format wins over Accept ...
        assertRejectedWith(FHIR_JSON, FHIR_XML, "json", FHIR_JSON);
        assertRejectedWith(FHIR_JSON, FHIR_JSON, "xml", FHIR_XML);
        // ... and an equally ranked tie falls back to the request format
        assertRejectedWith(FHIR_XML, FHIR_JSON + ", " + FHIR_XML, null, FHIR_XML);
        // An Accept the server cannot honour is ignored rather than obeyed
        assertRejectedWith(FHIR_XML, "text/html", null, FHIR_XML);
    }

    private void assertRejectedWith(String contentType, String accept, String format, String expectedContentType)
            throws Exception {
        var body = contentType.contains("xml")
                ? xml(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)))
                : json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));
        var request = request("/fhir", body);
        request.setContentType(contentType);
        if (accept != null) {
            request.addHeader("Accept", accept);
        }
        if (format != null) {
            request.setQueryString("_format=" + format);
            request.setParameter("_format", format);
        }

        var response = new MockHttpServletResponse();
        filter()
                .doFilter(request, response, (req, res) -> fail("Must not have been forwarded"));

        assertEquals(413, response.getStatus());
        var described = "content-type=" + contentType + " accept=" + accept + " _format=" + format;
        assertTrue(response.getContentType().startsWith(expectedContentType),
                described + " should be answered as " + expectedContentType + " but was " + response.getContentType());
        // ... and the body really is that format, i.e. parseable by the matching parser
        var parser = expectedContentType.contains("xml") ? fhirContext.newXmlParser() : fhirContext.newJsonParser();
        var outcome = (OperationOutcome) parser.parseResource(response.getContentAsString());
        assertTrue(outcome.hasIssue(), described);
    }

    /**
     * A body declaring some other charset stays on the ordinary parsing path, where HAPI honours the declaration,
     * rather than being re-encoded to UTF-8 behind a Content-Type that says otherwise.
     */
    @Test
    void aNonUtf8CharsetDeclarationDeclinesTheSplit() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));
        var request = request("/fhir", body);
        request.setContentType(FHIR_JSON + "; charset=ISO-8859-1");

        filter(request, forwarded -> {
            assertNull(registryOf(forwarded), "Must not have been split");
            assertArrayEquals(body, bodyOf(forwarded));
        });
    }

    @Test
    void theForwardedInputStreamIsConsumedProgressively() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        filter(request("/fhir", body), forwarded -> {
            // The servlet contract requires the same, progressively consumed stream on repeated calls
            var first = bodyOf(forwarded);
            assertTrue(first.length > 0);
            assertEquals(0, bodyOf(forwarded).length, "The body must not be replayed");
            assertSame(forwarded.getInputStream(), forwarded.getInputStream());
        });
    }

    @Test
    void gzippedRequestBodyIsSplitAsWell() throws Exception {
        var document = randomBytes(DOCUMENT_SIZE);
        var request = request("/fhir", gzip(json(provideDocumentBundle(document))));
        request.addHeader("Content-Encoding", "gzip");

        filter(request, forwarded -> {
            var registry = registryOf(forwarded);
            assertNotNull(registry);
            assertEquals(DOCUMENT_SIZE, registry.contents().iterator().next().getSize());
            // The forwarded body is plain JSON, so the encoding header must be gone
            assertNull(forwarded.getHeader("Content-Encoding"));
        });
    }

    @Test
    void smallRequestIsForwardedByteForByte() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(1024)));

        filter(request("/fhir", body), forwarded -> {
            assertNull(registryOf(forwarded), "A small request must not be touched at all");
            assertArrayEquals(body, bodyOf(forwarded));
        });
    }

    @Test
    void base64ElementsThatAreNotDocumentContentAreLeftAlone() throws Exception {
        // A large Bundle.signature must not be replaced by a marker, because no translator would resolve it
        var bundle = provideDocumentBundle(randomBytes(1024));
        bundle.setSignature(new Signature().setData(randomBytes(DOCUMENT_SIZE)));

        filter(request("/fhir", json(bundle)), forwarded -> {
            var parsed = (Bundle) parse(bodyOf(forwarded));
            assertEquals(DOCUMENT_SIZE, parsed.getSignature().getData().length,
                    "Bundle.signature must have been copied through verbatim");
            // Only the Binary content was staged, and it is still the small document
            assertEquals(1, registryOf(forwarded).size());
            assertEquals(1024, registryOf(forwarded).contents().iterator().next().getSize());
        });
    }

    @Test
    void decimalPrecisionSurvivesTheCopy() throws Exception {
        // FHIR treats the number of decimal places as significant, so the re-serialized body must not
        // round-trip decimals through a double
        var bundle = provideDocumentBundle(randomBytes(DOCUMENT_SIZE));
        var observation = new Observation().setValue(new Quantity().setValue(new BigDecimal("1.10")));
        bundle.addEntry().setFullUrl("urn:uuid:33333333-3333-3333-3333-333333333333").setResource(observation);
        var body = json(bundle);
        assertTrue(new String(body, StandardCharsets.UTF_8).contains("1.10"), "Precondition");

        filter(request("/fhir", body), forwarded ->
                assertTrue(new String(bodyOf(forwarded), StandardCharsets.UTF_8).contains("1.10"),
                        "Trailing zero of the decimal must be preserved"));
    }

    @Test
    void documentLargerThanTheConfiguredMaximumIsRejected() throws Exception {
        maxDocumentSizeBytes = DOCUMENT_SIZE / 2L;
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        var response = new MockHttpServletResponse();
        filter().doFilter(request("/fhir", body), response,
                (req, res) -> fail("The request must not have been forwarded"));

        assertEquals(413, response.getStatus());
        assertTrue(response.getContentAsString().contains("OperationOutcome"));
    }

    @Test
    void malformedJsonIsRejected() throws Exception {
        var body = new byte[engageAboveBytes + 1024];
        java.util.Arrays.fill(body, (byte) '{');

        var response = new MockHttpServletResponse();
        filter().doFilter(request("/fhir", body), response,
                (req, res) -> fail("The request must not have been forwarded"));

        assertEquals(400, response.getStatus());
        var outcome = (OperationOutcome) parse(response.getContentAsByteArray());
        assertEquals(OperationOutcome.IssueType.STRUCTURE, outcome.getIssueFirstRep().getCode());
    }

    /**
     * Eligibility is derived from the method and the path below the FHIR base, not from a configured list.
     */
    @Test
    void onlyInteractionsThatCanCarryADocumentAreTouched() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        // The transaction endpoint and the ITI-105 create qualify
        assertSplit("POST", "/fhir", body);
        assertSplit("POST", "/fhir/", body);
        assertSplit("POST", "/fhir/DocumentReference", body);

        // A create of some other resource type does not
        assertNotSplit("POST", "/fhir/Patient", body);
        assertNotSplit("POST", "/fhir/List", body);
        // Neither does any update: the MHD update transactions are metadata-only
        assertNotSplit("PUT", "/fhir/DocumentReference/1", body);
        assertNotSplit("PUT", "/fhir/DocumentReference", body);
        // ... nor a read, a search or a conditional delete
        assertNotSplit("GET", "/fhir/DocumentReference", body);
        assertNotSplit("DELETE", "/fhir/DocumentReference/1", body);
        // ... nor an endpoint that merely starts with the same characters as the FHIR base
        assertNotSplit("POST", "/fhirsomethingelse", body);
        assertNotSplit("POST", "/binary", body);
    }

    /**
     * Which resource types carry a document inline is transaction specific and therefore configured rather than
     * built in, so that the split is not tied to MHD.
     */
    @Test
    void aDifferentlyConfiguredSetOfInlineDocumentResourcesIsHonoured() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));
        inlineDocumentResources = Set.of("Patient");

        assertSplit("POST", "/fhir/Patient", body);
        // The transaction endpoint always qualifies, whatever the set says
        assertSplit("POST", "/fhir", body);
        assertNotSplit("POST", "/fhir/DocumentReference", body);
    }

    @Test
    void aDifferentlyConfiguredFhirBasePathIsHonoured() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));
        fhirBasePath = "/r4";

        assertSplit("POST", "/r4", body);
        assertSplit("POST", "/r4/DocumentReference", body);
        assertNotSplit("POST", "/fhir", body);
    }

    private void assertSplit(String method, String path, byte[] body) throws Exception {
        var request = request(path, body);
        request.setMethod(method);
        filter(request, forwarded -> assertNotNull(registryOf(forwarded),
                method + " " + path + " should have been split"));
    }

    private void assertNotSplit(String method, String path, byte[] body) throws Exception {
        var request = request(path, body);
        request.setMethod(method);
        filter(request, forwarded -> {
            assertNull(registryOf(forwarded), method + " " + path + " should not have been touched");
            assertArrayEquals(body, bodyOf(forwarded), method + " " + path + " should be forwarded verbatim");
        });
    }

    @Test
    void temporaryFilesAreDeletedWhenTheRequestIsDone() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        filter(request("/fhir", body), forwarded ->
                assertEquals(1, temporaryFileCount(), "Content is spilled while the request is in flight"));

        assertEquals(0, temporaryFileCount(), "Temporary files must be gone once the request is done");
    }

    /**
     * The listener is how an application meters the split, so it has to see every document, not just the request.
     */
    @Test
    void everyStreamedDocumentIsReportedToTheListener() throws Exception {
        var bundle = provideDocumentBundle(randomBytes(DOCUMENT_SIZE));
        bundle.addEntry().setFullUrl("urn:uuid:44444444-4444-4444-4444-444444444444")
                .setResource(new Binary().setContentType(CONTENT_TYPE).setData(randomBytes(DOCUMENT_SIZE / 2)));

        filter(request("/fhir", json(bundle)), forwarded -> {
        });

        assertEquals(List.of((long) DOCUMENT_SIZE, DOCUMENT_SIZE / 2L), streamedSizes);
    }

    // Fixtures and helpers

    private Bundle provideDocumentBundle(byte[] document) {
        var documentReference = new DocumentReference();
        documentReference.addContent().setAttachment(new Attachment()
                .setContentType(CONTENT_TYPE)
                .setUrl(BINARY_FULL_URL));
        var binary = new Binary()
                .setContentType(CONTENT_TYPE)
                .setData(document);

        var bundle = new Bundle().setType(Bundle.BundleType.TRANSACTION);
        bundle.addEntry()
                .setFullUrl("urn:uuid:22222222-2222-2222-2222-222222222222")
                .setResource(documentReference)
                .getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("DocumentReference");
        bundle.addEntry()
                .setFullUrl(BINARY_FULL_URL)
                .setResource(binary)
                .getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Binary");
        return bundle;
    }

    private static Binary binaryOf(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(Binary.class::isInstance)
                .map(Binary.class::cast)
                .findFirst()
                .orElseThrow();
    }

    private byte[] xml(IBaseResource resource) {
        return fhirContext.newXmlParser().encodeResourceToString(resource).getBytes(StandardCharsets.UTF_8);
    }

    private byte[] json(IBaseResource resource) {
        return fhirContext.newJsonParser().encodeResourceToString(resource).getBytes(StandardCharsets.UTF_8);
    }

    private IBaseResource parse(byte[] body) {
        return fhirContext.newJsonParser().parseResource(new String(body, StandardCharsets.UTF_8));
    }

    private MockHttpServletRequest request(String path, byte[] body) {
        var request = new MockHttpServletRequest("POST", CONTEXT_PATH + path);
        request.setContextPath(CONTEXT_PATH);
        request.setContentType(FHIR_JSON);
        request.setContent(body);
        return request;
    }

    /**
     * Runs the filter and hands the forwarded request to the inspector, which runs while the staged content
     * registry is still alive.
     */
    private void filter(MockHttpServletRequest request, Inspector inspector) throws Exception {
        filter().doFilter(request, new MockHttpServletResponse(), (req, res) -> {
            try {
                inspector.inspect((HttpServletRequest) req);
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new ServletException(e);
            }
        });
    }

    /**
     * The way a consumer of the parsed resource actually reaches the staged content: from the parameter map that
     * a resource provider hands on, rather than from the servlet request directly.
     */
    @Test
    void theRegistryIsReachableFromTheFhirRequestParameters() throws Exception {
        var body = json(provideDocumentBundle(randomBytes(DOCUMENT_SIZE)));

        filter(request("/fhir", body), forwarded -> {
            var requestDetails = new ServletRequestDetails();
            requestDetails.setServletRequest(forwarded);
            var parameters = Map.<String, Object>of(Constants.FHIR_REQUEST_DETAILS, requestDetails);

            var registry = StagedContentRegistry.forParameters(parameters);
            assertTrue(registry.isPresent(), "The registry should be reachable from the request parameters");
            assertSame(registryOf(forwarded), registry.get());
        });

        assertTrue(StagedContentRegistry.forParameters(null).isEmpty());
        assertTrue(StagedContentRegistry.forParameters(Map.of()).isEmpty());
    }

    @FunctionalInterface
    private interface Inspector {
        void inspect(HttpServletRequest forwarded) throws Exception;
    }

    private static StagedContentRegistry registryOf(HttpServletRequest request) {
        return (StagedContentRegistry) request.getAttribute(StagedContentRegistry.REQUEST_ATTRIBUTE);
    }

    private static byte[] bodyOf(HttpServletRequest request) throws IOException {
        return request.getInputStream().readAllBytes();
    }

    private static byte[] readFully(DataHandler dataHandler) throws IOException {
        try (var in = dataHandler.getInputStream()) {
            return in.readAllBytes();
        }
    }

    private long temporaryFileCount() throws IOException {
        try (var files = Files.list(tempDirectory)) {
            return files.count();
        }
    }

    private static byte[] gzip(byte[] content) throws IOException {
        var compressed = new ByteArrayOutputStream();
        try (var out = new GZIPOutputStream(compressed)) {
            out.write(content);
        }
        return compressed.toByteArray();
    }

    private static byte[] randomBytes(int size) {
        var bytes = new byte[size];
        new Random(size).nextBytes(bytes);
        return bytes;
    }

    private static String sha1Hex(byte[] content) throws Exception {
        return Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(content));
    }

    private static void fail(String message) {
        throw new AssertionError(message);
    }
}
