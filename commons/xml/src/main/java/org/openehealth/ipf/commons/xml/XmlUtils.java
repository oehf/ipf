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

import groovy.xml.XmlUtil;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Various XML utilities.
 * @author Dmytro Rud
 */
abstract public class XmlUtils {

    private XmlUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }


    /**
     * Creates a byte array from the given object.
     * @param obj
     *      one of the following:
     *      <ul>
     *          <li>{@link String}</li>
     *          <li>byte[]</li>
     *          <li>{@link InputStream}</li>
     *          <li>DOM {@link Document}</li>
     *      </ul>
     * @param charsetName
     *      character set name for using in data transformation.
     *      When <code>null</code>, the system default will be used.
     * @return
     *      byte array.
     */
    public static byte[] bytes(Object obj, String charsetName) {
        try {
            if (obj instanceof Document) {
                obj = XmlUtil.serialize(((Document) obj).getDocumentElement());
            }

            if (obj instanceof String) {
                String s = (String) obj;
                return (charsetName != null) ? s.getBytes(charsetName) : s.getBytes();
            }

            if (obj instanceof byte[]) {
                return (byte[]) obj;
            }

            if (obj instanceof InputStream) {
                return IOUtils.toByteArray((InputStream) obj);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException("Cannot create XML Source from " +
                ((obj == null) ? "<null>" : obj.getClass().getName()));
    }


    /**
     * Creates an XML Source from the given object.
     * @param obj
     *      one of the following:
     *      <ul>
     *          <li>{@link Source}</li>
     *          <li>{@link String}</li>
     *          <li>byte[]</li>
     *          <li>{@link InputStream}</li>
     *          <li>DOM {@link Document}</li>
     *      </ul>
     * @param charsetName
     *      character set name for using in data transformation.
     *      When <code>null</code>, the system default will be used.
     * @return
     *      XML Source.
     */
    public static Source source(Object obj, String charsetName) {
        try {
            if (obj instanceof Source) {
                return (Source) obj;
            }

            if (obj instanceof String) {
                String s = (String) obj;
                obj = (charsetName != null) ? s.getBytes(charsetName) : s.getBytes();
            }

            if (obj instanceof byte[]) {
                obj = new ByteArrayInputStream((byte[]) obj);
            }

            if (obj instanceof InputStream) {
                return new StreamSource((InputStream) obj);
            }

            if (obj instanceof Document) {
                return new DOMSource((Document) obj);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException("Cannot create XML Source from " +
                ((obj == null) ? "<null>" : obj.getClass().getName()));
    }


    /**
     * Creates an XML String from the given object.
     * @param obj
     *      one of the following:
     *      <ul>
     *          <li>{@link String}</li>
     *          <li>byte[]</li>
     *          <li>{@link InputStream}</li>
     *          <li>DOM {@link Document}</li>
     *      </ul>
     * @param charsetName
     *      character set name for using in data transformation.
     *      When <code>null</code>, the system default will be used.
     * @return
     *      XML string.
     */
    public static String toString(Object obj, String charsetName) {
        try {
            if (obj instanceof String) {
                return (String) obj;
            }

            if (obj instanceof InputStream) {
                return IOUtils.toString((InputStream) obj, charsetName);
            }

            if (obj instanceof byte[]) {
                byte[] bytes = (byte[]) obj;
                return (charsetName != null) ? new String(bytes, charsetName) : new String(bytes);
            }

            if (obj instanceof Document) {
                return XmlUtil.serialize(((Document) obj).getDocumentElement());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException("Cannot create XML String from " +
                ((obj == null) ? "<null>" : obj.getClass().getName()));
    }


    /**
     * Determines the name of the root element of the XML document contained in the given object.
     * @param obj
     *      an object whose type is supported by the method {@link #source}.
     * @return
     *      element name.
     */
    public static QName rootElementName(Object obj) {
        if (obj instanceof Document) {
            Element element = ((Document) obj).getDocumentElement();
            return new QName(element.getNamespaceURI(), element.getTagName());
        }

        try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(source(obj, null));
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    return reader.getName();
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Cannot determine root element name");
    }

}
