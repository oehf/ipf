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

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various XML utilities.
 *
 * @author Dmytro Rud
 */
@Slf4j
abstract public class XmlUtils {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:<\\?xml.+?\\?>)?"   +                              // optional prolog
        "(?:\\s*<\\!--.*?-->)*" +                              // optional comments
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))",   // open tag of the root element
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
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(s);
        return (matcher.find() && (matcher.start() == 0)) ? matcher.group(1) : null;
    }


    /**
     * Searches for the first sub-element of the given XML element, which has
     * the given local name and whose namespace belongs to the given set.
     *
     * @param root            an XML element whose children will be iterated, null values are allowed
     * @param nsUris          a set of namespace URIs the wanted element can belong to
     * @param wantedLocalName local name of the wanted element
     * @return corresponding child element or <code>null</code> when none found
     */
    public static Element getElementNS(
            Element root,
            Set<String> nsUris,
            String wantedLocalName) {
        if (root == null) {
            return null;
        }

        Node node = root.getFirstChild();
        while (node != null) {
            if ((node instanceof Element) &&
                    nsUris.contains(node.getNamespaceURI()) &&
                    node.getLocalName().equals(wantedLocalName)) {
                return (Element) node;
            }

            node = node.getNextSibling();
        }

        return null;
    }


    /**
     * Extracts the given XML element from the given XML document.
     * <p>
     * Notes:
     * <ul>
     *     <li>Only non-empty elements can be found, the form
     *          <tt>&lt;prefix:elementName (attr="value")* /&gt;</tt> is not supported.</li>
     *     <li>When multiple elements with the given local name are present,
     *          the last one will be returned.</li>
     * </ul>
     *
     * @param document
     *      XML document as String.
     * @param elementName
     *      XML local element name.
     * @return
     *      XML element as String, or <code>null</code> when no element could be extracted.
     */
    public static String extractNonEmptyElement(String document, String elementName) {

        // ... <prefix:elementName attr1="abcd"> ... </prefix:elementName> ...
        //     3                                     2        1

        try {
            int pos1 = document.lastIndexOf(elementName + '>');
            if (pos1 < 0) {
                log.warn("Cannot find end of the closing tag of {}", elementName);
                return null;
            }

            int pos2 = document.lastIndexOf('<', pos1 - 1);
            if (pos2 < 0) {
                log.warn("Cannot find start of the closing tag of {}", elementName);
                return null;
            }

            StringBuilder sb = new StringBuilder().append('<');
            if (pos1 - pos2 > 2) {
                sb.append(document, pos2 + 2, pos1 - 1).append(':');
            }
            int pos3 = document.indexOf(sb.append(elementName).toString());
            if (pos3 < 0) {
                log.warn("Cannot find start of the opening tag of {}", elementName);
                return null;
            }

            return document.substring(pos3, pos1 + elementName.length() + 1);

        } catch (Exception e) {
            log.error("Could not extract element" + elementName, e);
            return null;
        }
    }

}
