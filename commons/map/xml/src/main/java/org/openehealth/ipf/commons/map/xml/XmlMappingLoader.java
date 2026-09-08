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
import jakarta.xml.bind.ValidationEvent;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingFunctionRegistry;
import org.openehealth.ipf.commons.map.MappingLoader;
import org.openehealth.ipf.commons.map.Unmatched;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MappingLoader} for {@code *.mapping.xml} files, the recommended format for IPF's own
 * mappings.
 * <p>
 * Every file is validated against {@code META-INF/ipf/mapping.xsd} as it is read, so an unknown element,
 * an unknown attribute or a misspelt one is an error at startup with a line and a column.
 * The two constraints the schema language cannot express, that {@code mode="fixed"} needs a value and {@code mode="function"} a
 * registered function, are checked here.
 *
 * @since 6.0
 */
public class XmlMappingLoader implements MappingLoader {

    /**
     * File extension this loader claims. Two-part on purpose: {@code .xml} alone says nothing
     * about which of the many XML formats in healthcare integration a file holds.
     */
    public static final String EXTENSION = ".mapping.xml";

    /**
     * Format id under which this loader can be selected explicitly.
     */
    public static final String FORMAT = "xml";

    private static final JAXBContext CONTEXT = context();
    private static final Schema SCHEMA = schema();

    @Override
    public String format() {
        return FORMAT;
    }

    @Override
    public boolean supports(URI source) {
        return MappingLoader.hasExtension(source, EXTENSION);
    }

    @Override
    public List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions) {
        var events = new ArrayList<ValidationEvent>();
        Object root;
        try {
            var unmarshaller = CONTEXT.createUnmarshaller();
            unmarshaller.setSchema(SCHEMA);
            unmarshaller.setEventHandler(event -> {
                events.add(event);
                return event.getSeverity() != ValidationEvent.FATAL_ERROR;
            });
            root = unmarshaller.unmarshal(new SAXSource(reader(), new InputSource(in)));
        } catch (JAXBException e) {
            throw new MappingException(source, events.isEmpty()
                    ? "Could not parse mapping file: " + rootCauseMessage(e)
                    : describe(events, "Invalid mapping file"), e);
        }
        if (!events.isEmpty()) {
            throw new MappingException(source, describe(events, "Invalid mapping file"));
        }

        var declared = root instanceof MappingXml.Mappings mappings
                ? mappings.mappings
                : List.of((MappingXml.Mapping) root);
        return declared.stream()
                .map(mapping -> toMapping(mapping, source, functions))
                .toList();
    }

    // ------------------------------------------------------------------ binding to model

    private static Mapping toMapping(MappingXml.Mapping xml, URI source, MappingFunctionRegistry functions) {
        var builder = Mapping.builder(xml.name)
                .keySystem(xml.keySystem)
                .valueSystem(xml.valueSystem)
                .reversible(xml.reversible == null || xml.reversible)
                .override(Boolean.TRUE.equals(xml.override));
        xml.entries.forEach(entry -> builder.entry(new Entry(entry.key, entry.value,
                equivalence(entry.equivalence), entry.keyDisplay, entry.valueDisplay)));
        builder.unmatched(unmatched(xml.unmatched, xml.name, "unmatched", source, functions));
        if (xml.reverse != null) {
            builder.reverseUnmatched(unmatched(xml.reverse.unmatched, xml.name, "reverse/unmatched",
                    source, functions));
        }
        return builder.build();
    }

    private static Equivalence equivalence(MappingXml.XmlEquivalence equivalence) {
        return equivalence == null ? Equivalence.EQUAL : Equivalence.valueOf(equivalence.name());
    }

    private static Unmatched unmatched(MappingXml.Unmatched xml, String mapping, String element,
                                       URI source, MappingFunctionRegistry functions) {
        if (xml == null) {
            return Unmatched.ABSENT;
        }
        return switch (xml.mode) {
            case ABSENT -> {
                reject(xml.value, "value", xml, mapping, element, source);
                reject(xml.ref, "ref", xml, mapping, element, source);
                yield Unmatched.ABSENT;
            }
            case PROVIDED -> {
                reject(xml.value, "value", xml, mapping, element, source);
                reject(xml.ref, "ref", xml, mapping, element, source);
                yield Unmatched.IDENTITY;
            }
            case FAIL -> {
                reject(xml.value, "value", xml, mapping, element, source);
                reject(xml.ref, "ref", xml, mapping, element, source);
                yield Unmatched.FAIL;
            }
            case FIXED -> {
                require(xml.value, "value", xml, mapping, element, source);
                reject(xml.ref, "ref", xml, mapping, element, source);
                yield Unmatched.fixed(xml.value);
            }
            case FUNCTION -> {
                require(xml.ref, "ref", xml, mapping, element, source);
                reject(xml.value, "value", xml, mapping, element, source);
                if (!functions.contains(xml.ref)) {
                    throw new MappingException(source, "Mapping '" + mapping + "' refers to the mapping"
                            + " function '" + xml.ref + "', which is not registered. Register it with"
                            + " Mappings.builder().function(..) before loading this file");
                }
                yield Unmatched.computed(xml.ref);
            }
            case DELEGATE -> {
                require(xml.ref, "ref", xml, mapping, element, source);
                reject(xml.value, "value", xml, mapping, element, source);
                yield Unmatched.delegate(xml.ref);
            }
        };
    }

    private static void require(String attribute, String name, MappingXml.Unmatched xml,
                                String mapping, String element, URI source) {
        if (attribute == null) {
            throw new MappingException(source, "Mapping '" + mapping + "': <" + element + " mode=\""
                    + mode(xml) + "\"> requires the '" + name + "' attribute");
        }
    }

    private static void reject(String attribute, String name, MappingXml.Unmatched xml,
                               String mapping, String element, URI source) {
        if (attribute != null) {
            throw new MappingException(source, "Mapping '" + mapping + "': <" + element + " mode=\""
                    + mode(xml) + "\"> does not take a '" + name + "' attribute");
        }
    }

    private static String mode(MappingXml.Unmatched xml) {
        return xml.mode.name().toLowerCase();
    }

    // ------------------------------------------------------------------ parser plumbing

    private static String describe(List<ValidationEvent> events, String prefix) {
        if (events.isEmpty()) {
            return prefix;
        }
        return prefix + ": " + events.stream()
                .map(event -> {
                    var locator = event.getLocator();
                    var at = locator == null || locator.getLineNumber() < 0
                            ? ""
                            : " at line " + locator.getLineNumber() + ", column " + locator.getColumnNumber();
                    return event.getMessage() + at;
                })
                .collect(Collectors.joining("; "));
    }

    private static String rootCauseMessage(Throwable e) {
        var cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? cause.toString() : cause.getMessage();
    }

    private static JAXBContext context() {
        try {
            return JAXBContext.newInstance(MappingXml.Mappings.class, MappingXml.Mapping.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Could not initialize the mapping XML binding", e);
        }
    }

    private static Schema schema() {
        var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        setProperty(factory, XMLConstants.ACCESS_EXTERNAL_DTD);
        setProperty(factory, XMLConstants.ACCESS_EXTERNAL_SCHEMA);
        try (var xsd = XmlMappingLoader.class.getResourceAsStream(MappingXml.SCHEMA_RESOURCE)) {
            if (xsd == null) {
                throw new IllegalStateException(MappingXml.SCHEMA_RESOURCE + " is missing from the classpath");
            }
            return factory.newSchema(new StreamSource(xsd));
        } catch (SAXException | IOException e) {
            throw new IllegalStateException("Could not read " + MappingXml.SCHEMA_RESOURCE, e);
        }
    }

    private static void setProperty(SchemaFactory factory, String property) {
        try {
            factory.setProperty(property, "");
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            // an implementation that does not know the property does not resolve anything either
        }
    }

    /**
     * A parser that resolves nothing outside the document itself: mapping files are data, and a
     * data file has no business pulling in a DTD or an external entity.
     */
    private static XMLReader reader() {
        var factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            return factory.newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Could not configure the mapping XML parser", e);
        }
    }
}
