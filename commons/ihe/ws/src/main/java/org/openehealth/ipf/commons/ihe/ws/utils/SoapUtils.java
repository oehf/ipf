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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.addressing.Names;
import org.apache.cxf.ws.addressing.VersionTransformer.Names200403;
import org.apache.cxf.ws.addressing.VersionTransformer.Names200408;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generic constants and subroutines for SOAP/XML processing.
 * @author Dmytro Rud
 */
public abstract class SoapUtils {
    private static final transient Logger LOG = LoggerFactory.getLogger(SoapUtils.class);

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
     * Set of URIs corresponding to supported SOAP versions.
     */
    public static final Set<String> SOAP_NS_URIS;

    static {
        WS_ADDRESSING_NS_URIS = new HashSet<>();
        WS_ADDRESSING_NS_URIS.add(Names.WSA_NAMESPACE_NAME);
        WS_ADDRESSING_NS_URIS.add(Names200403.WSA_NAMESPACE_NAME);
        WS_ADDRESSING_NS_URIS.add(Names200408.WSA_NAMESPACE_NAME);

        SOAP_NS_URIS = new HashSet<>();
        SOAP_NS_URIS.add(Soap11.SOAP_NAMESPACE);
        SOAP_NS_URIS.add(Soap12.SOAP_NAMESPACE);
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
            String bodyElementStart = "<" + soapPrefix + "Body";
            pos3 = soapEnvelope.indexOf(bodyElementStart);
            pos4 = soapEnvelope.indexOf('>', pos3 + bodyElementStart.length());
            return soapEnvelope.substring(pos4 + 1, pos1);

        } catch(Exception e) {
            LOG.error("Invalid contents, probably not a SOAP Envelope in the parameter", e);
            return soapEnvelope;
        }
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
            String wantedLocalName)
    {
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
}
