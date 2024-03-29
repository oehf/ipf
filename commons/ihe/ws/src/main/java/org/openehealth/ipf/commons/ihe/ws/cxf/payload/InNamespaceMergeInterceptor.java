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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;

import org.apache.cxf.wsdl.interceptors.DocLiteralInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * CXF interceptor which inserts XML namespace declarations from incoming 
 * SOAP Envelope and SOAP Body elements into the String payload.
 * 
 * @author Dmytro Rud
 */
public class InNamespaceMergeInterceptor extends AbstractPhaseInterceptor<Message> {

    private final static Pattern XMLNS_PATTERN = Pattern.compile("xmlns:(\\w+)=\".+?\"");

    public InNamespaceMergeInterceptor() {
        super(Phase.UNMARSHAL);
        addAfter(DocLiteralInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        if (isGET(message)) {
            return;
        }

        var payloadHolder = message.getContent(StringPayloadHolder.class);
        if (payloadHolder != null) {
            var payload = payloadHolder.get(SOAP_BODY);
            if (isXmlContent(payload)) {
                var document = (Document) message.getContent(Node.class);
                if (document != null) {
                    // The Node representation of the message is usually produced
                    // by CXF's ReadHeadersInterceptor, but this does not happen when the message
                    // does not contain SOAP headers (i.e. when the request is invalid, because
                    // it does not fulfill the IHE requirement of using WS-Addressing).
                    // Let us hope that in this unhappy case all relevant XML namespaces
                    // are defined directly in the SOAP body and not in the SOAP envelope.
                    payloadHolder.put(SOAP_BODY, enrichNamespaces(document, payload));
                }
            }
        }
    }

    
    /**
     * Returns <code>true</code> iff the given payload string seems to contain XML.
     */
    private static boolean isXmlContent(String payload) {
        if (payload != null) {
            for (var i = 0; i < payload.length(); ++i) {
                var c = payload.charAt(i);
                if (!Character.isWhitespace(c)) {
                    return (c == '<');
                }
            }
        }
        return false;
    }
    
    
    /**
     * Copies namespace definitions from SOAP Envelope and SOAP Body elements 
     * of the given XML Document into the top-level element of the XML document
     * represented by the given String.
     * <p>
     * Caller is supposed to take care of the parameters' correctness,
     * so there is no sophisticated error handling. 
     * 
     * @param source
     *      source SOAP document as a DOM object.
     * @param target
     *      target XML Document as String.
     * @return target 
     *      XML document enriched with namespace declarations from the
     *      source XML document.
     */
    protected static String enrichNamespaces(Document source, String target) {
        var namespaces = new HashMap<String, String>();

        // collect namespace definitions from <soap:Envelope>
        var envelope = source.getDocumentElement();
        addNamespacesFromElement(envelope, namespaces);

        // collect namespace definitions from <soap:Body>
        var node = envelope.getFirstChild();
        while ((node != null) && !((node instanceof Element) && "Body".equals(node.getLocalName()))) {
            node = node.getNextSibling();
        }
        var body = (Element) node;
        addNamespacesFromElement(body, namespaces);

        if (!namespaces.isEmpty()) {
            // determine boundaries of the body's root element, ignore leading comments
            var startPos = 0;
            var endPos = target.indexOf('>');
            while ((endPos != -1) && "--".equals(target.substring(endPos - 2, endPos))) {
                startPos = endPos + 1;
                endPos = target.indexOf('>', startPos);
            }
            if (endPos == -1) {
                return target;
            }
            if (target.charAt(endPos - 1) == '/') {
                --endPos;
            }

            // ignore namespaces which are (re)defined in the payload
            var startTag = target.substring(startPos, endPos);
            var matcher = XMLNS_PATTERN.matcher(startTag);
            while (matcher.find()) {
                namespaces.remove(matcher.group(1));
            }

            // insert remained definitions (if any)
            if (!namespaces.isEmpty()) {
                var sb = new StringBuilder(startTag);
                for (var ns : namespaces.entrySet()) {
                    sb.append(" xmlns:").append(ns.getKey()).append("=\"")
                      .append(ns.getValue()).append('"');
                }
                sb.append(target.substring(endPos));
                return sb.toString();
            }
        }

        return target;
    }

    
    /**
     * Adds NS defined in the given XML element to the map.
     * <p>
     * Existing map items will be overwritten, so this method should be called
     * in the tree-descending order.
     */
    protected static void addNamespacesFromElement(Element elem, Map<String, String> map) {
        var attributes = elem.getAttributes();
        for (var i = 0; i < attributes.getLength(); ++i) {
            var attribute = attributes.item(i);
            if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
                map.put(attribute.getLocalName(), attribute.getTextContent());
            }
        }
    }

}
