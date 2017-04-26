/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v3

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.HL7V3_NSURI
import static org.openehealth.ipf.commons.xml.XmlUtils.getElementNS

/**
 * @author Dmytro Rud
 */
abstract class Hl7v3ContinuationUtils {

    static final Set<String> HL7V3_NSURI_SET = Collections.singleton(HL7V3_NSURI)


    static String getAttribute(Element root, String subElementLocalName, String attributeName) {
        NodeList list = root?.getElementsByTagNameNS(HL7V3_NSURI, subElementLocalName)
        return list?.length ?
            ((Element) list.item(0))?.getAttributeNode(attributeName)?.value :
            null
    }


    static int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim())
        } catch (Exception e) {  // NullPointerException, NumberFormatException
            return -1
        }
    }


    static int getIntFromValueAttribute(Element root, String subElementLocalName) {
        return parseInt(getAttribute(root, subElementLocalName, 'value').trim())
    }


    static void addAcknowledgementDetail(Document doc, String message) {
        Element text = doc.createElementNS(HL7V3_NSURI, 'text')
        text.setTextContent(message)

        Element acknowledgementDetail = doc.createElementNS(HL7V3_NSURI, 'acknowledgementDetail')
        acknowledgementDetail.setAttribute('typeCode', 'I')
        acknowledgementDetail.appendChild(text)

        Element acknowledgement = getElementNS(doc.documentElement, HL7V3_NSURI_SET, 'acknowledgement')
        acknowledgement.appendChild(acknowledgementDetail)
    }


    static void deleteIpfRelatedAcknowledgementDetails(Document doc) {
        Element acknowledgement = getElementNS(
                doc.documentElement, HL7V3_NSURI_SET, 'acknowledgement')
        NodeList nl = acknowledgement.getElementsByTagNameNS(HL7V3_NSURI, 'acknowledgementDetail')
        int i = 0
        while (i < nl.length) {
            Element detail = (Element) nl.item(i)
            if (detail.getAttribute('typeCode') == 'I') {
                Element text = getElementNS(detail, HL7V3_NSURI_SET, 'text')
                if (text?.textContent?.contains('by the IPF')) {
                    acknowledgement.removeChild(detail)
                    continue
                }
            }
            ++i
        }
    }


    static void setValueAttribute(Node node, int value) {
        Element element = (Element) node
        element.removeAttribute('nullFlavor')
        element.setAttribute('value', Integer.toString(value))
    }


    static void setQueryAckContinuationNumbers(Element queryAck, int remaining, int current, int total) {
        NodeList queryAckChildren = queryAck.getElementsByTagNameNS(HL7V3_NSURI, '*')
        int len = queryAckChildren.length
        setValueAttribute(queryAckChildren.item(len - 1), remaining)
        setValueAttribute(queryAckChildren.item(len - 2), current)
        setValueAttribute(queryAckChildren.item(len - 3), total)
    }

}

