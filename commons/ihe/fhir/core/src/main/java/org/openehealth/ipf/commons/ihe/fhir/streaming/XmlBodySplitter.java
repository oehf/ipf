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

import ca.uhn.fhir.rest.api.EncodingEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Deque;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Splits the document content out of a FHIR XML body.
 * <p>
 * Unlike JSON, FHIR XML carries primitive values in <em>attributes</em>
 * ({@code <data value="JVBERi0..."/>}). No Java XML API can read an attribute value incrementally &mdash;
 * StAX, SAX and DOM all hand it over as a fully materialized {@link String}, which is precisely the allocation
 * that has to be avoided here. This class therefore scans the raw bytes itself.
 * <p>
 * The scan is deliberately narrow: it copies the input <em>byte for byte</em> and only ever substitutes the
 * value of a {@code data} attribute whose element sits inside an {@code Attachment} or a {@code Binary}.
 * Everything else &mdash; whitespace, attribute order, quoting style, comments, processing instructions
 * &mdash; is reproduced exactly, so unlike the JSON splitter there is no re-serialization to be faithful about.
 * <p>
 * Recognizing markup only requires finding {@code <}, which in well-formed XML cannot occur inside attribute
 * values or character data, so comments, CDATA sections and processing instructions are the only constructs that
 * need to be skipped explicitly.
 *
 * @author Christian Ohr
 * @see JsonBodySplitter
 */
class XmlBodySplitter implements BodySplitter {

    private static final Logger log = LoggerFactory.getLogger(XmlBodySplitter.class);

    private static final String DATA_ELEMENT = "data";
    private static final String VALUE_ATTRIBUTE = "value";

    /**
     * Local names of the elements whose {@code data} child may carry a document. Note that this differs from the
     * JSON case: in XML a bundle entry nests the resource in an extra element, so the parent of the document is
     * {@code Binary} rather than {@code resource}. Restricting the split this way keeps unrelated base64 elements
     * such as {@code Signature.data} on the ordinary parsing path.
     */
    private static final Set<String> DOCUMENT_HOLDERS = Set.of("attachment", "Binary");

    /**
     * Number of leading bytes inspected to establish the encoding.
     */
    private static final int ENCODING_PROBE_LENGTH = 200;

    private static final Pattern ENCODING_DECLARATION =
            Pattern.compile("<\\?xml[^>]*?encoding\\s*=\\s*[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);

    private static final Set<String> SUPPORTED_ENCODINGS = Set.of("utf-8", "us-ascii", "ascii");

    @Override
    public EncodingEnum encoding() {
        return EncodingEnum.XML;
    }

    @Override
    public byte[] split(InputStream body, StagedContentRegistry registry) throws IOException {
        return new Scan(body, registry).run();
    }

    /**
     * Scanning raw bytes is only sound for an ASCII-compatible encoding, so anything else has to be refused
     * rather than misread. FHIR mandates UTF-8 for request bodies, which makes this a theoretical concern, but a
     * silently mis-scanned body would be far worse than a rejected one.
     *
     * @param head the leading bytes of the request body
     * @return true if the body can be scanned byte by byte
     */
    static boolean isSupportedEncoding(byte[] head) {
        var probe = new String(head, 0, Math.min(head.length, ENCODING_PROBE_LENGTH), StandardCharsets.ISO_8859_1);
        // A NUL byte this early means a UTF-16 or UTF-32 body, with or without a byte order mark
        if (probe.indexOf(0) >= 0) {
            return false;
        }
        var declaration = ENCODING_DECLARATION.matcher(probe);
        return !declaration.find()
                || SUPPORTED_ENCODINGS.contains(declaration.group(1).toLowerCase(Locale.ROOT));
    }

    /**
     * The state of one scan. Every byte read is echoed to the output unless it belongs to a document value.
     */
    private static final class Scan {

        private final PushbackInputStream in;
        private final ByteArrayOutputStream out = new ByteArrayOutputStream(64 * 1024);
        private final StagedContentRegistry registry;
        private final Deque<String> path = new ArrayDeque<>();

        Scan(InputStream in, StagedContentRegistry registry) {
            this.in = new PushbackInputStream(in, 1);
            this.registry = registry;
        }

        byte[] run() throws IOException {
            int c;
            while ((c = next()) >= 0) {
                if (c == '<') {
                    markup();
                }
            }
            return out.toByteArray();
        }

        private void markup() throws IOException {
            switch (peek()) {
                case '!' -> declaration();
                case '?' -> skipThrough("?>");
                case '/' -> endTag();
                default -> startTag();
            }
        }

        /**
         * A comment, a CDATA section or a document type declaration. The latter is rejected: FHIR payloads never
         * carry one, its internal subset would complicate scanning, and refusing it keeps entity expansion out of
         * the picture entirely.
         */
        private void declaration() throws IOException {
            next(); // '!'
            if (peek() == '-') {
                skipThrough("-->");
            } else if (peek() == '[') {
                skipThrough("]]>");
            } else {
                throw new MalformedBodyException("Document type declarations are not supported");
            }
        }

        private void endTag() throws IOException {
            next(); // '/'
            readName();
            skipThrough(">");
            if (path.isEmpty()) {
                throw new MalformedBodyException("Unbalanced end tag in request body");
            }
            path.pop();
        }

        private void startTag() throws IOException {
            var name = readName();
            // The parent is known before the attributes are read, so the decision can be made up front and the
            // document value never has to be buffered.
            var holdsDocument = DATA_ELEMENT.equals(localName(name))
                    && !path.isEmpty()
                    && DOCUMENT_HOLDERS.contains(localName(path.peek()));

            while (true) {
                var c = next();
                if (c < 0) {
                    throw new MalformedBodyException("Unexpected end of request body inside a start tag");
                }
                if (c == '>') {
                    path.push(name);
                    return;
                }
                if (c == '/') {
                    if (next() != '>') {
                        throw new MalformedBodyException("Malformed empty element tag in request body");
                    }
                    return;
                }
                if (isWhitespace(c)) {
                    continue;
                }
                attribute(c, holdsDocument);
            }
        }

        private void attribute(int firstChar, boolean holdsDocument) throws IOException {
            var attributeName = readName(firstChar);
            skipWhitespace();
            if (next() != '=') {
                throw new MalformedBodyException("Malformed attribute in request body");
            }
            skipWhitespace();
            var quote = next();
            if (quote != '"' && quote != '\'') {
                throw new MalformedBodyException("Unquoted attribute value in request body");
            }
            if (holdsDocument && VALUE_ATTRIBUTE.equals(localName(attributeName))) {
                stageValue(quote);
            } else {
                copyValue(quote);
            }
        }

        /**
         * Streams the base64 attribute value into the registry and writes the marker in its place. The marker is
         * itself base64, so the FHIR parser sees a perfectly ordinary (and very short) base64Binary value.
         */
        private void stageValue(int quote) throws IOException {
            var content = registry.stage();
            var value = new AttributeValueInputStream(in, quote);
            try (var sink = content.sink()) {
                // The MIME decoder tolerates the line breaks that clients like to put into large base64 values
                Base64.getMimeDecoder().wrap(value).transferTo(sink);
            }
            value.finish();
            out.write(Base64.getEncoder().encode(registry.marker(content.getToken())));
            out.write(quote);
            log.debug("Streamed {} bytes of document content {} into {}",
                    content.getSize(), content.getToken(), content.isSpilled() ? "a temporary file" : "memory");
        }

        private void copyValue(int quote) throws IOException {
            int c;
            while ((c = next()) != quote) {
                if (c < 0) {
                    throw new MalformedBodyException("Unterminated attribute value in request body");
                }
            }
        }

        private String readName() throws IOException {
            var c = next();
            if (c < 0) {
                throw new MalformedBodyException("Unexpected end of request body inside a tag name");
            }
            return readName(c);
        }

        private String readName(int firstChar) throws IOException {
            var name = new StringBuilder().append((char) firstChar);
            int c;
            while ((c = in.read()) >= 0 && !isNameEnd(c)) {
                out.write(c);
                name.append((char) c);
            }
            if (c >= 0) {
                in.unread(c);
            }
            return name.toString();
        }

        private void skipWhitespace() throws IOException {
            while (isWhitespace(peek())) {
                next();
            }
        }

        /**
         * Copies bytes until the given terminator has been consumed. Matching keeps a sliding window rather than
         * a match counter, so that overlapping candidates such as the {@code ]]]>} that legitimately ends a CDATA
         * section containing a {@code ]} are recognized.
         */
        private void skipThrough(String terminator) throws IOException {
            var tail = new StringBuilder(terminator.length());
            while (true) {
                var c = next();
                if (c < 0) {
                    throw new MalformedBodyException("Unexpected end of request body, expected " + terminator);
                }
                tail.append((char) c);
                if (tail.length() > terminator.length()) {
                    tail.deleteCharAt(0);
                }
                if (tail.length() == terminator.length() && tail.indexOf(terminator) == 0) {
                    return;
                }
            }
        }

        /**
         * Reads the next byte and echoes it to the output.
         */
        private int next() throws IOException {
            var c = in.read();
            if (c >= 0) {
                out.write(c);
            }
            return c;
        }

        private int peek() throws IOException {
            var c = in.read();
            if (c >= 0) {
                in.unread(c);
            }
            return c;
        }

        private static String localName(String qualifiedName) {
            var colon = qualifiedName.indexOf(':');
            return colon < 0 ? qualifiedName : qualifiedName.substring(colon + 1);
        }

        private static boolean isNameEnd(int c) {
            return isWhitespace(c) || c == '=' || c == '>' || c == '/';
        }

        private static boolean isWhitespace(int c) {
            return c == ' ' || c == '\t' || c == '\r' || c == '\n';
        }
    }

    /**
     * The bytes of a single XML attribute value, ending at its closing quote. Character references are expanded,
     * because an entity name such as the {@code amp} of {@code &amp;} would otherwise be mistaken for base64
     * content.
     */
    private static final class AttributeValueInputStream extends InputStream {

        private static final int MAX_REFERENCE_LENGTH = 16;

        private final PushbackInputStream in;
        private final int quote;
        private int pending = -1;
        private boolean atEnd;

        AttributeValueInputStream(PushbackInputStream in, int quote) {
            this.in = in;
            this.quote = quote;
        }

        @Override
        public int read() throws IOException {
            if (pending >= 0) {
                var c = pending;
                pending = -1;
                return c;
            }
            if (atEnd) {
                return -1;
            }
            var c = in.read();
            if (c < 0) {
                throw new MalformedBodyException("Unterminated attribute value in request body");
            }
            if (c == quote) {
                atEnd = true;
                return -1;
            }
            return c == '&' ? reference() : c;
        }

        /**
         * Expands a character or entity reference. Only references that expand to a single ASCII character are
         * accepted; anything else cannot be part of a base64 value anyway.
         */
        private int reference() throws IOException {
            var reference = new StringBuilder();
            int c;
            while ((c = in.read()) >= 0 && c != ';') {
                if (reference.length() == MAX_REFERENCE_LENGTH) {
                    throw new MalformedBodyException("Unsupported reference in attribute value");
                }
                reference.append((char) c);
            }
            if (c < 0) {
                throw new MalformedBodyException("Unterminated reference in attribute value");
            }
            return expand(reference.toString());
        }

        private static int expand(String reference) throws IOException {
            try {
                if (reference.startsWith("#x") || reference.startsWith("#X")) {
                    return ascii(Integer.parseInt(reference.substring(2), 16));
                }
                if (reference.startsWith("#")) {
                    return ascii(Integer.parseInt(reference.substring(1)));
                }
            } catch (NumberFormatException e) {
                throw new MalformedBodyException("Malformed character reference in attribute value", e);
            }
            return switch (reference) {
                case "amp" -> '&';
                case "lt" -> '<';
                case "gt" -> '>';
                case "quot" -> '"';
                case "apos" -> '\'';
                default -> throw new MalformedBodyException("Unsupported entity reference in attribute value");
            };
        }

        private static int ascii(int codePoint) throws IOException {
            if (codePoint < 0 || codePoint > 0x7F) {
                throw new MalformedBodyException("Non-ASCII character reference in attribute value");
            }
            return codePoint;
        }

        /**
         * Consumes whatever is left of the value, so that the closing quote has been read even if the decoder
         * stopped early.
         */
        void finish() throws IOException {
            while (!atEnd) {
                if (read() < 0) {
                    return;
                }
            }
        }
    }
}
