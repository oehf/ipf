/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.map.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.glassfish.jaxb.core.marshaller.CharacterEscapeHandler;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingWriter;
import org.openehealth.ipf.commons.map.MappingWriters;
import org.openehealth.ipf.commons.map.Unmatched;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Writes {@link Mapping}s as a {@code *.mapping.xml} document.
 * <p>
 * The output declares the schema with {@code xsi:noNamespaceSchemaLocation}, so an IDE validates
 * it while it is being edited. Attributes that only restate a default - {@code equivalence="equal"},
 * {@code reversible="true"}, {@code override="false"} - are left out.
 *
 * @since 6.0
 */
public class XmlMappingWriter implements MappingWriter {

    private static final JAXBContext CONTEXT = context();

    @Override
    public String format() {
        return XmlMappingLoader.FORMAT;
    }

    @Override
    public String extension() {
        return XmlMappingLoader.EXTENSION;
    }

    /**
     * @param mappings the mappings to write
     * @return the document as a string, always with a {@code <mappings>} root so that further
     * mappings can be added to the file by hand
     */
    public String toXml(List<? extends Mapping> mappings) {
        var writer = new StringWriter();
        write(mappings, writer);
        return writer.toString();
    }

    public void write(List<? extends Mapping> mappings, OutputStream out) {
        write(mappings, new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    @Override
    public void write(List<? extends Mapping> mappings, Writer out) {
        MappingWriters.requireKeysAndValues(mappings);
        mappings.forEach(XmlMappingWriter::requireXmlCharacters);
        var document = new MappingXml.Mappings();
        mappings.forEach(mapping -> document.mappings.add(toXml(mapping)));
        try {
            var marshaller = CONTEXT.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, MappingXml.SCHEMA_LOCATION);
            marshaller.setProperty(CharacterEscapeHandler.class.getName(), ESCAPE);
            marshaller.marshal(document, out);
            out.flush();
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException("Could not write mappings as XML", e);
        }
    }

    /**
     * Escapes what the default escaping leaves as it is. Every value is written as an attribute,
     * and a parser normalizes a literal tab, line feed or carriage return in an attribute value to
     * a space - only a character reference survives - so these are written as references.
     */
    private static final CharacterEscapeHandler ESCAPE = (buffer, start, length, attribute, out) -> {
        for (var i = start; i < start + length; i++) {
            var c = buffer[i];
            switch (c) {
                case '&' -> out.write("&amp;");
                case '<' -> out.write("&lt;");
                case '>' -> out.write("&gt;");
                case '"' -> out.write(attribute ? "&quot;" : "\"");
                case '\t' -> out.write(attribute ? "&#9;" : "\t");
                case '\n' -> out.write(attribute ? "&#10;" : "\n");
                case '\r' -> out.write("&#13;");
                default -> out.write(c);
            }
        }
    };

    /**
     * XML 1.0 has no representation at all for most control characters, not even as a reference,
     * so a mapping holding one is rejected rather than written into a file that cannot be read.
     */
    private static void requireXmlCharacters(Mapping mapping) {
        var texts = new ArrayList<>(List.of(mapping.name()));
        texts.add(mapping.keySystem());
        texts.add(mapping.valueSystem());
        for (var entry : mapping.entries()) {
            texts.addAll(Arrays.asList(entry.key(), entry.value(), entry.keyDisplay(), entry.valueDisplay()));
        }
        for (var unmatched : List.of(mapping.unmatched(), mapping.reverseUnmatched())) {
            if (unmatched instanceof Unmatched.Fixed fixed) {
                texts.add(fixed.value());
            } else if (unmatched instanceof Unmatched.Computed computed) {
                texts.add(computed.ref());
            } else if (unmatched instanceof Unmatched.Delegate delegate) {
                texts.add(delegate.mapping());
            }
        }
        for (var text : texts) {
            if (text != null && !text.codePoints().allMatch(XmlMappingWriter::isXmlCharacter)) {
                throw new IllegalArgumentException("Mapping '" + mapping.name() + "' holds a character XML"
                        + " cannot represent, in '" + text.replaceAll("\\p{Cntrl}", "?") + "'");
            }
        }
    }

    private static boolean isXmlCharacter(int c) {
        return c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF)
                || (c >= 0xE000 && c <= 0xFFFD) || (c >= 0x10000 && c <= 0x10FFFF);
    }

    private static MappingXml.Mapping toXml(Mapping mapping) {
        var xml = new MappingXml.Mapping();
        xml.name = mapping.name();
        xml.keySystem = mapping.keySystem();
        xml.valueSystem = mapping.valueSystem();
        xml.reversible = mapping.reversible() ? null : Boolean.FALSE;
        xml.override = mapping.override() ? Boolean.TRUE : null;
        mapping.entries().forEach(entry -> {
            var e = new MappingXml.Entry();
            e.key = entry.key();
            e.value = entry.value();
            e.equivalence = entry.equivalence() == Equivalence.EQUAL
                    ? null
                    : MappingXml.XmlEquivalence.valueOf(entry.equivalence().name());
            e.keyDisplay = entry.keyDisplay();
            e.valueDisplay = entry.valueDisplay();
            xml.entries.add(e);
        });
        xml.unmatched = toXml(mapping.unmatched());
        var reverseUnmatched = toXml(mapping.reverseUnmatched());
        if (reverseUnmatched != null) {
            xml.reverse = new MappingXml.Reverse();
            xml.reverse.unmatched = reverseUnmatched;
        }
        return xml;
    }

    /**
     * @return the {@code <unmatched>} element, or {@code null} for {@link Unmatched.Absent},
     * which is what an omitted element means
     */
    private static MappingXml.Unmatched toXml(Unmatched unmatched) {
        if (unmatched instanceof Unmatched.Absent) {
            return null;
        }
        var xml = new MappingXml.Unmatched();
        if (unmatched instanceof Unmatched.Identity) {
            xml.mode = MappingXml.XmlUnmatchedMode.PROVIDED;
        } else if (unmatched instanceof Unmatched.Fixed fixed) {
            xml.mode = MappingXml.XmlUnmatchedMode.FIXED;
            xml.value = fixed.value();
        } else if (unmatched instanceof Unmatched.Computed computed) {
            xml.mode = MappingXml.XmlUnmatchedMode.FUNCTION;
            xml.ref = computed.ref();
        } else if (unmatched instanceof Unmatched.Delegate delegate) {
            xml.mode = MappingXml.XmlUnmatchedMode.DELEGATE;
            xml.ref = delegate.mapping();
        } else if (unmatched instanceof Unmatched.Fail) {
            xml.mode = MappingXml.XmlUnmatchedMode.FAIL;
        } else {
            throw new IllegalStateException("Unsupported unmatched behavior " + unmatched);
        }
        return xml;
    }

    private static JAXBContext context() {
        try {
            return JAXBContext.newInstance(MappingXml.Mappings.class, MappingXml.Mapping.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Could not initialize the mapping XML binding", e);
        }
    }
}
