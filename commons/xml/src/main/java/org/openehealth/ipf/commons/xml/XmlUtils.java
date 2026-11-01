/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import org.w3c.dom.Node;

import org.xml.sax.InputSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Various XML utilities.
 *
 * @author Dmytro Rud
 */
abstract public class XmlUtils {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
            "(?:<\\?xml.+?\\?>)?" +                              // optional prolog
            "(?:\\s*<!--.*?-->)*" +                              // optional comments
            "\\s*<(?:[\\w.-]+?:)?([\\w.-]+)(?:\\s|(?:/?>))",   // open tag of the root element
            Pattern.DOTALL
    );

    private XmlUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }

    /**
     * Creates an XML Source from the given XML String.
     *
     * @param s XML String.
     * @return XML Source.
     */
    public static Source source(String s) {
        return new StreamSource(new StringReader(s));
    }

    /**
     * Returns local name of the root element of the XML document represented
     * by the given string, or <code>null</code>, when the given string does
     * not contain valid XML.
     *
     * @param s XML string.
     * @return root element local name, or <code>null</code> when it could not be determined.
     */
    public static String rootElementName(String s) {
        if (s == null) {
            return null;
        }
        var matcher = ROOT_ELEMENT_PATTERN.matcher(s);
        return (matcher.find() && (matcher.start() == 0)) ? matcher.group(1) : null;
    }

    /**
     * Marshals a JAXB object into its XML string representation, without XML declaration.
     *
     * @param jaxbContext JAXB context corresponding to the object's type
     * @param object      the JAXB object to be marshalled
     * @param prettyPrint whether the XML shall be pretty printed
     * @return XML string representation of the given JAXB object
     * @throws RuntimeException wrapping the {@link jakarta.xml.bind.JAXBException} if marshalling fails
     * @since 6.1
     */
    public static String marshal(JAXBContext jaxbContext, Object object, boolean prettyPrint) {
        try {
            var marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            var writer = new StringWriter();
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marshals a DOM node into indented XML bytes, without XML declaration.
     *
     * @param node DOM node to marshal
     * @return XML bytes
     * @throws TransformerException if serialization fails
     * @since 6.1
     */
    public static byte[] marshal(Node node) throws TransformerException {
        var output = new ByteArrayOutputStream();
        var serializer = TransformerFactory.newInstance().newTransformer();
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        serializer.transform(new DOMSource(node), new StreamResult(output));
        return output.toByteArray();
    }

    /**
     * Unmarshals an untrusted XML string with JAXB. The document is parsed by a reader from
     * {@link XmlSecurity#newXmlReader()}, so any {@code DOCTYPE} declaration is rejected.
     *
     * @param jaxbContext JAXB context corresponding to the expected content
     * @param xml         XML string
     * @return the unmarshalled object, which may be a {@link jakarta.xml.bind.JAXBElement} for types
     * without root element declaration
     * @throws JAXBException if the document cannot be parsed or unmarshalled, including when it contains
     *                       a {@code DOCTYPE} declaration
     * @since 6.1
     */
    public static Object unmarshal(JAXBContext jaxbContext, String xml) throws JAXBException {
        var source = new SAXSource(XmlSecurity.newXmlReader(), new InputSource(new StringReader(xml)));
        return jaxbContext.createUnmarshaller().unmarshal(source);
    }

    /**
     * Unmarshals an untrusted XML string with JAXB into an object of the given type, see
     * {@link #unmarshal(JAXBContext, String)}.
     *
     * @param jaxbContext JAXB context corresponding to the expected content
     * @param xml         XML string
     * @param type        expected type of the unmarshalled object
     * @param <T>         expected type of the unmarshalled object
     * @return the unmarshalled object
     * @throws JAXBException      if the document cannot be parsed or unmarshalled
     * @throws ClassCastException if the unmarshalled object is not of the expected type
     * @since 6.1
     */
    public static <T> T unmarshal(JAXBContext jaxbContext, String xml, Class<T> type) throws JAXBException {
        return type.cast(unmarshal(jaxbContext, xml));
    }

    /**
     * @deprecated use {@link #marshal(JAXBContext, Object, boolean)}
     */
    @Deprecated(since = "6.1", forRemoval = true)
    public static String renderJaxb(JAXBContext jaxbContext, Object object, Boolean prettyPrint) {
        return marshal(jaxbContext, object, prettyPrint);
    }

    /**
     * @deprecated use {@link #marshal(Node)}
     */
    @Deprecated(since = "6.1", forRemoval = true)
    public static byte[] serialize(Node inputNode) throws Exception {
        return marshal(inputNode);
    }
}
