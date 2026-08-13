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

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the byte level scan of FHIR XML bodies. FHIR XML carries primitive values in attributes, and no Java XML
 * API can read an attribute value incrementally, so the scan works on raw bytes; these tests pin down that it
 * reproduces everything it is not supposed to touch exactly as it found it.
 */
class XmlBodySplitterTest {

    private static final String DOCUMENT = "Hello World";

    @TempDir
    Path tempDirectory;

    private XmlBodySplitter splitter;
    private StagedContentRegistry registry;

    @BeforeEach
    void setup() {
        splitter = new XmlBodySplitter();
        registry = new StagedContentRegistry(options(64, 1024 * 1024));
    }

    @Test
    void binaryContentIsStreamedOutOfABundle() throws Exception {
        var body = """
                <Bundle xmlns="http://hl7.org/fhir">
                  <entry>
                    <fullUrl value="urn:uuid:1"/>
                    <resource>
                      <Binary>
                        <contentType value="application/pdf"/>
                        <data value="%s"/>
                      </Binary>
                    </resource>
                  </entry>
                </Bundle>""".formatted(base64(DOCUMENT));

        var slim = split(body);

        assertEquals(1, registry.size());
        assertEquals(DOCUMENT, stagedContent());
        assertFalse(slim.contains(base64(DOCUMENT)), "The document must be gone from the forwarded body");
        // Everything but the value of the data element is reproduced verbatim
        assertEquals(body.replace(base64(DOCUMENT), marker()), slim);
    }

    @Test
    void inlineAttachmentContentIsStreamedOut() throws Exception {
        var body = """
                <DocumentReference xmlns="http://hl7.org/fhir">
                  <content>
                    <attachment>
                      <contentType value="application/pdf"/>
                      <data value="%s"/>
                    </attachment>
                  </content>
                </DocumentReference>""".formatted(base64(DOCUMENT));

        var slim = split(body);

        assertEquals(1, registry.size());
        assertEquals(DOCUMENT, stagedContent());
        assertEquals(body.replace(base64(DOCUMENT), marker()), slim);
    }

    @Test
    void severalDocumentsAreStreamedOutIndependently() throws Exception {
        var body = """
                <Bundle xmlns="http://hl7.org/fhir">
                  <entry><resource><Binary><data value="%s"/></Binary></resource></entry>
                  <entry><resource><Binary><data value="%s"/></Binary></resource></entry>
                </Bundle>""".formatted(base64("first"), base64("second"));

        split(body);

        assertEquals(2, registry.size());
        var contents = registry.contents().iterator();
        assertEquals("first", read(contents.next()));
        assertEquals("second", read(contents.next()));
    }

    @Test
    void base64ElementsThatAreNotDocumentContentAreLeftAlone() throws Exception {
        // Signature.data is base64 too, but no translator would resolve a marker there. The XHTML narrative may
        // even contain an HTML <data> element, which must not be mistaken for document content either.
        var body = """
                <Bundle xmlns="http://hl7.org/fhir">
                  <signature><data value="%s"/></signature>
                  <text><div xmlns="http://www.w3.org/1999/xhtml"><data value="%s">x</data></div></text>
                </Bundle>""".formatted(base64("signature"), base64("narrative"));

        var slim = split(body);

        assertEquals(0, registry.size());
        assertEquals(body, slim, "Nothing should have been touched");
    }

    @Test
    void namespacePrefixesAreHandled() throws Exception {
        var body = """
                <f:Bundle xmlns:f="http://hl7.org/fhir">
                  <f:entry><f:resource><f:Binary><f:data value="%s"/></f:Binary></f:resource></f:entry>
                </f:Bundle>""".formatted(base64(DOCUMENT));

        split(body);

        assertEquals(1, registry.size());
        assertEquals(DOCUMENT, stagedContent());
    }

    @Test
    void quotingStyleAndAttributeOrderAreReproduced() throws Exception {
        var body = "<Binary xmlns='http://hl7.org/fhir'>"
                + "<data id = 'x'   value='" + base64(DOCUMENT) + "'  extra=\"y\" />"
                + "</Binary>";

        var slim = split(body);

        assertEquals(DOCUMENT, stagedContent());
        assertEquals(body.replace(base64(DOCUMENT), marker()), slim);
    }

    @Test
    void commentsProcessingInstructionsAndCdataAreReproduced() throws Exception {
        var body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<!-- a comment with <data value=\"ignore\"/> and -- dashes -->"
                + "<Bundle xmlns=\"http://hl7.org/fhir\">"
                + "<?some instruction?>"
                + "<text><div xmlns=\"http://www.w3.org/1999/xhtml\"><![CDATA[a] and ]]]></div></text>"
                + "<entry><resource><Binary><data value=\"" + base64(DOCUMENT) + "\"/></Binary></resource></entry>"
                + "</Bundle>";

        var slim = split(body);

        assertEquals(1, registry.size());
        assertEquals(DOCUMENT, stagedContent());
        assertEquals(body.replace(base64(DOCUMENT), marker()), slim);
    }

    @Test
    void lineBreaksInsideTheBase64ValueAreTolerated() throws Exception {
        var encoded = base64(DOCUMENT);
        var wrapped = encoded.substring(0, 4) + "\n   " + encoded.substring(4);
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\"" + wrapped + "\"/></Binary>";

        split(body);

        assertEquals(DOCUMENT, stagedContent());
    }

    @Test
    void characterReferencesInsideTheBase64ValueAreExpanded() throws Exception {
        // Legal, if perverse: the first base64 character written as a character reference
        var encoded = base64(DOCUMENT);
        var referenced = "&#x%02X;".formatted((int) encoded.charAt(0)) + encoded.substring(1);
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\"" + referenced + "\"/></Binary>";

        split(body);

        assertEquals(DOCUMENT, stagedContent(), "A character reference must not be read as base64 content");
    }

    @Test
    void sizeAndDigestAreComputedWhileStreaming() throws Exception {
        var document = new byte[256 * 1024];
        new Random(1).nextBytes(document);
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\""
                + Base64.getEncoder().encodeToString(document) + "\"/></Binary>";

        split(body);

        var content = registry.contents().iterator().next();
        assertEquals(document.length, content.getSize());
        assertEquals(sha1Hex(document), content.getSha1Hex());
        assertTrue(content.isSpilled(), "256 KiB should have been spilled to a temporary file");
        assertArrayEquals(document, read(content).getBytes(StandardCharsets.ISO_8859_1));
    }

    /**
     * The in-memory buffer is only allocated on the first write, so zero-length content leaves it null. That must
     * still hand out an empty stream rather than looking like released content.
     */
    @Test
    void emptyContentIsStagedAsAnEmptyDocument() throws Exception {
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\"\"/></Binary>";

        split(body);

        assertEquals(1, registry.size());
        var content = registry.contents().iterator().next();
        assertEquals(0, content.getSize());
        assertEquals("", read(content));
    }

    @Test
    void contentExactlyAtTheMemoryThresholdStaysInMemory() throws Exception {
        // The buffer is pre-sized to the threshold, so the boundary is where an off-by-one would show up
        var registryWithThreshold = new StagedContentRegistry(options(16, 1024));
        var document = "x".repeat(16);
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\""
                + Base64.getEncoder().encodeToString(document.getBytes(StandardCharsets.UTF_8)) + "\"/></Binary>";

        splitter.split(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)), registryWithThreshold);

        var content = registryWithThreshold.contents().iterator().next();
        assertEquals(16, content.getSize());
        assertFalse(content.isSpilled(), "Exactly at the threshold must not spill");
        assertEquals(document, read(content));
    }

    @Test
    void contentOneByteOverTheMemoryThresholdSpills() throws Exception {
        var registryWithThreshold = new StagedContentRegistry(options(16, 1024));
        var document = "x".repeat(17);
        var body = "<Binary xmlns=\"http://hl7.org/fhir\"><data value=\""
                + Base64.getEncoder().encodeToString(document.getBytes(StandardCharsets.UTF_8)) + "\"/></Binary>";

        splitter.split(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)), registryWithThreshold);

        var content = registryWithThreshold.contents().iterator().next();
        assertEquals(17, content.getSize());
        assertTrue(content.isSpilled());
        assertEquals(document, read(content));
    }

    @Test
    void documentTypeDeclarationsAreRejected() {
        var body = "<!DOCTYPE Bundle SYSTEM \"evil.dtd\"><Bundle xmlns=\"http://hl7.org/fhir\"/>";

        assertThrows(BodySplitter.MalformedBodyException.class, () -> split(body));
    }

    @Test
    void malformedBodiesAreRejected() {
        assertThrows(BodySplitter.MalformedBodyException.class,
                () -> split("<Binary><data value=\"unterminated"));
        assertThrows(BodySplitter.MalformedBodyException.class,
                () -> split("<Binary><data value=unquoted/></Binary>"));
        assertThrows(BodySplitter.MalformedBodyException.class,
                () -> split("<Binary></Binary></Bundle>"));
    }

    @Test
    void nonUtf8BodiesAreNotScanned() {
        assertTrue(XmlBodySplitter.isSupportedEncoding(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Bundle/>".getBytes(StandardCharsets.UTF_8)));
        assertTrue(XmlBodySplitter.isSupportedEncoding("<Bundle/>".getBytes(StandardCharsets.UTF_8)));
        assertFalse(XmlBodySplitter.isSupportedEncoding(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><Bundle/>".getBytes(StandardCharsets.UTF_8)));
        assertFalse(XmlBodySplitter.isSupportedEncoding("<Bundle/>".getBytes(StandardCharsets.UTF_16)));
    }

    // Helpers

    private String split(String body) throws IOException {
        var slim = splitter.split(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)), registry);
        return new String(slim, StandardCharsets.UTF_8);
    }

    private String marker() {
        var token = registry.contents().iterator().next().getToken();
        return Base64.getEncoder().encodeToString(registry.marker(token));
    }

    private StreamingUploadOptions options(int memoryThresholdBytes, long maxDocumentSizeBytes) {
        return new StreamingUploadOptions(StreamingUploadOptions.DEFAULT_ENGAGE_ABOVE_BYTES,
                memoryThresholdBytes, maxDocumentSizeBytes, tempDirectory,
                StreamingUploadOptions.MHD_INLINE_DOCUMENT_RESOURCES);
    }

    private String stagedContent() throws IOException {
        return read(registry.contents().iterator().next());
    }

    private static String read(StagedContent content) throws IOException {
        try (var in = content.dataHandler("application/octet-stream").getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.ISO_8859_1);
        }
    }

    private static String base64(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    private static String sha1Hex(byte[] content) throws Exception {
        return Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(content));
    }
}
