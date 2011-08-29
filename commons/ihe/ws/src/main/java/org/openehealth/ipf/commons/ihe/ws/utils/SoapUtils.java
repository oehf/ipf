/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.addressing.Names;
import org.apache.cxf.ws.addressing.VersionTransformer.Names200403;
import org.apache.cxf.ws.addressing.VersionTransformer.Names200408;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic constants and subroutines for SOAP/XML processing.
 * @author Dmytro Rud
 */
public abstract class SoapUtils {
    private static final transient Log LOG = LogFactory.getLog(SoapUtils.class);

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:\\s*<\\!--.*?-->)*"                             +  // optional comments before prolog (are they allowed?)
        "(?:\\s*<\\?xml.+?\\?>(?:\\s*<\\!--.*?-->)*)?"      +  // optional prolog and comments after it
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))",   // open tag of the root element
        Pattern.DOTALL
    );


    private SoapUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /* --------------------------------------- */
    /*      XML/SOAP processing constants      */
    /* --------------------------------------- */

    /**
     * Set of URIs corresponding to supported WS-Addressing specification versions.
     */
    public static final Set<String> WS_ADDRESSING_NS_URIS;

    /**
     * Set of URIs corresponding to supported WS-Security specification versions.
     */
    public static final Set<String> WS_SECURITY_NS_URIS;

    /**
     * Set of URIs corresponding to supported SOAP versions.
     */
    public static final Set<String> SOAP_NS_URIS;

    static {
        WS_ADDRESSING_NS_URIS = new HashSet<String>();
        WS_ADDRESSING_NS_URIS.add(Names.WSA_NAMESPACE_NAME);
        WS_ADDRESSING_NS_URIS.add(Names200403.WSA_NAMESPACE_NAME);
        WS_ADDRESSING_NS_URIS.add(Names200408.WSA_NAMESPACE_NAME);

        WS_SECURITY_NS_URIS = new HashSet<String>();
        WS_SECURITY_NS_URIS.add("http://schemas.xmlsoap.org/ws/2002/07/secext");
        WS_SECURITY_NS_URIS.add("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

        SOAP_NS_URIS = new HashSet<String>();
        SOAP_NS_URIS.add(Soap11.SOAP_NAMESPACE);
        SOAP_NS_URIS.add(Soap12.SOAP_NAMESPACE);
    }


    /* ----------------------- */
    /*      XML utilities      */
    /* ----------------------- */

    /**
     * Searches for the first sub-element of the given XML element, which has
     * the given local name and whose namespace belongs to the given set.
     *
     * @param root
     *      an XML element whose children will be iterated, null values are allowed
     * @param nsUris
     *      a set of namespace URIs the wanted element can belong to
     * @param wantedLocalName
     *      local name of the wanted element
     * @return
     *      corresponding child element or <code>null</code> when none found
     */
    public static Element getElementNS(
            Element root,
            Set<String> nsUris,
            String wantedLocalName)
    {
        if(root == null) {
            return null;
        }

        Node node = root.getFirstChild();
        while(node != null) {
            if((node instanceof Element) &&
               nsUris.contains(node.getNamespaceURI()) &&
               node.getLocalName().equals(wantedLocalName))
            {
                return (Element)node;
            }

            node = node.getNextSibling();
        }

        return null;
    }


    /**
     * Recursively searches for an XML element.
     * <p>
     * In the first step of the recursion, any namespace URI from the given
     * set will be accepted.  After the first (the topmost) element has been
     * found, its namespace URI is the only acceptable one.
     *
     * @param root
     *      an XML element whose children will be iterated, null values are allowed
     * @param nsUris
     *      a set of namespace URIs the wanted elements can belong to
     * @param wantedLocalNamesChain
     *      a chain of local element names, e.g. <code>{"outer", "middle", "inner"}</code>
     * @return the XML element or {@code null} if none was found.
     */
    public static Element getDeepElementNS(
            Element root,
            Set<String> nsUris,
            String[] wantedLocalNamesChain)
    {
        Element element = root;

        for(String name : wantedLocalNamesChain) {
            element = getElementNS(element, nsUris, name);
            if(element == null) {
                return null;
            }
            if(nsUris.size() > 1) {
                nsUris = Collections.singleton(element.getNamespaceURI());
            }
        }

        return element;
    }
    

    /**
     * Extracts the proper body (for example, a Query) from the
     * SOAP envelope, both represented as Strings.
     * <p>
     * Does really suppose that the given String contains
     * a SOAP envelope and not check it thoroughly.
     *
     * @param soapEnvelope
     *      The SOAP Envelope (XML document) as String.
     * @return
     *      Extracted SOAP Body contents as String, or the original
     *      parameter when it does not seem to represent a valid
     *      SOAP envelope.
     */
    public static String extractSoapBody(String soapEnvelope) {
        try {
            /*
             * We search for following positions (variables posXX):
             *
             *    <S:Envelope><S:Body>the required information</S:Body><S:Envelope>
             *                3      4                        1  2    5
             *
             *
             *    <Envelope><Body>the required information</Body><Envelope>
             *   2                                        1     5
             *
             */
            int pos1, pos2, pos3, pos4, pos5;
            pos1 = soapEnvelope.lastIndexOf("<");
            pos1 = soapEnvelope.lastIndexOf("<", pos1 - 1);
            pos5 = soapEnvelope.indexOf(">", pos1);
            if (soapEnvelope.charAt(pos5 - 1) == '/') {
                return "";
            }
            pos2 = soapEnvelope.indexOf(":", pos1);
            String soapPrefix = ((pos2 == -1) || (pos5 < pos2)) ?
                    "" : soapEnvelope.substring(pos1 + 2, pos2 + 1);
            String bodyElementStart = new StringBuilder()
                .append('<')
                .append(soapPrefix)
                .append("Body")
                .toString();
            pos3 = soapEnvelope.indexOf(bodyElementStart);
            pos4 = soapEnvelope.indexOf('>', pos3 + bodyElementStart.length());
            return soapEnvelope.substring(pos4 + 1, pos1);

        } catch(Exception e) {
            LOG.error("Invalid contents, probably not a SOAP Envelope in the parameter", e);
            return soapEnvelope;
        }
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
                LOG.warn("Cannot find end of the closing tag of " + elementName);
                return null;
            }

            int pos2 = document.lastIndexOf('<', pos1 - 1);
            if (pos2 < 0) {
                LOG.warn("Cannot find start of the closing tag of " + elementName);
                return null;
            }

            StringBuilder sb = new StringBuilder().append('<');
            if (pos1 - pos2 > 2) {
                sb.append(document, pos2 + 2, pos1 - 1).append(':');
            }
            int pos3 = document.indexOf(sb.append(elementName).toString());
            if (pos3 < 0) {
                LOG.warn("Cannot find start of the opening tag of " + elementName);
                return null;
            }

            return document.substring(pos3, pos1 + elementName.length() + 1);

        } catch (Exception e) {
            LOG.error("Could not extract element" + elementName, e);
            return null;
        }
    }


    /**
     * Returns local name of the root element of the XML document represented
     * by the given string, or <code>null</code>, when the given string does
     * not contain valid XML.
     */
    public static String getRootElementLocalName(String xml) {
        if (xml == null) {
            return null;
        }
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(xml);
        return (matcher.find() && (matcher.start() == 0)) ? matcher.group(1) : null;
    }


    /**
     * Returns Exception object from the outgoing fault message contained in the given
     * CXF exchange, or <code>null</code>, when no exception could be extracted.
     */
    public static Exception extractOutgoingException(Exchange exchange) {
        Message outFaultMessage = exchange.getOutFaultMessage();
        return (outFaultMessage != null) ? outFaultMessage.getContent(Exception.class) : null;
    }


    /**
     * Returns String payload of the outgoing message contained in the given
     * CXF exchange, or <code>null</code>, when no String payload could be extracted.
     */
    public static String extractOutgoingPayload(Exchange exchange) {
        try {
            return (String) exchange.getOutMessage().getContent(List.class).get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
