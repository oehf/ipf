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
import tools.jackson.core.Base64Variants;
import tools.jackson.core.JsonEncoding;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.json.JsonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Splits the document content out of a FHIR JSON body.
 * <p>
 * The body is copied token by token through a Jackson {@link JsonParser}/{@code JsonGenerator} pair. Whenever a
 * {@code data} field belonging to an {@code Attachment} or to a bundle entry {@code resource} is reached, the
 * base64 is decoded incrementally into the registry via
 * {@link JsonParser#readBinaryValue(tools.jackson.core.Base64Variant, java.io.OutputStream)}, so neither the
 * base64 text nor the decoded array is ever materialized.
 *
 * @author Christian Ohr
 */
class JsonBodySplitter implements BodySplitter {

    private static final Logger log = LoggerFactory.getLogger(JsonBodySplitter.class);

    private static final String DATA_FIELD = "data";

    /**
     * Field names of the objects whose {@code data} element may carry a document: {@code Attachment.data} and the
     * {@code data} of a {@code Binary} sitting in {@code Bundle.entry.resource} &mdash; in JSON the
     * {@code resourceType} is a sibling of {@code data}, so the enclosing field is {@code resource}. Restricting
     * the split this way keeps unrelated base64 elements such as {@code Signature.data} on the ordinary parsing
     * path.
     */
    private static final Set<String> DOCUMENT_HOLDERS = Set.of("attachment", "resource");

    private final JsonFactory jsonFactory = new JsonFactory();

    @Override
    public EncodingEnum encoding() {
        return EncodingEnum.JSON;
    }

    @Override
    public byte[] split(InputStream body, StagedContentRegistry registry) throws IOException {
        var slimBody = new ByteArrayOutputStream(64 * 1024);
        try (var parser = jsonFactory.createParser(body);
             var generator = jsonFactory.createGenerator(slimBody, JsonEncoding.UTF8)) {

            String field = null;
            while (parser.nextToken() != null) {
                if (parser.currentToken() == JsonToken.PROPERTY_NAME) {
                    field = parser.currentName();
                    generator.writeName(field);
                    continue;
                }
                if (parser.currentToken() == JsonToken.VALUE_STRING
                        && DATA_FIELD.equals(field)
                        && holdsDocument(parser)) {
                    // writeBinary() base64-encodes the marker again, so the FHIR parser sees a perfectly
                    // ordinary (and very short) base64Binary value.
                    generator.writeBinary(stage(parser, registry));
                } else if (parser.currentToken() == JsonToken.VALUE_NUMBER_FLOAT) {
                    // copyCurrentEvent() would round-trip through a double and drop trailing zeros. In FHIR the
                    // precision of a decimal is significant, so copy the lexical form verbatim instead.
                    generator.writeNumber(parser.getText());
                } else {
                    generator.copyCurrentEvent(parser);
                }
                field = null;
            }
        } catch (StreamReadException e) {
            throw new MalformedBodyException("Request body is not well-formed JSON", e);
        } catch (JacksonIOException e) {
            // Jackson wraps every IOException it encounters, including the ones the staging sink raises when a
            // document exceeds the configured maximum. Unwrap so that the caller sees
            // StagedContent.DocumentTooLargeException and genuine read errors as themselves.
            throw e.getCause();
        }
        return slimBody.toByteArray();
    }

    /**
     * Decides whether the {@code data} element the parser is positioned on belongs to an {@code Attachment} or to
     * a {@code Binary} inside a bundle entry, by looking at the field name of the enclosing object. This only
     * inspects field names, never the value, so the base64 text stays untouched.
     */
    private boolean holdsDocument(JsonParser parser) {
        var enclosing = parser.streamReadContext().getParent();
        return enclosing != null && DOCUMENT_HOLDERS.contains(enclosing.currentName());
    }

    private byte[] stage(JsonParser parser, StagedContentRegistry registry) throws IOException {
        var content = registry.stage();
        try (var sink = content.sink()) {
            parser.readBinaryValue(Base64Variants.MIME, sink);
        }
        log.debug("Streamed {} bytes of document content {} into {}",
                content.getSize(), content.getToken(), content.isSpilled() ? "a temporary file" : "memory");
        return registry.marker(content.getToken());
    }
}
