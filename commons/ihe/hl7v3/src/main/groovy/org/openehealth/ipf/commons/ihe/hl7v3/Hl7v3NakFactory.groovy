/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3

import groovy.util.slurpersupport.GPathResult;
import groovy.xml.MarkupBuilder;

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.xml.XmlYielder.*

/**
 * Factory class for HL7 v3 NAK messages.
 * @author Dmytro Rud
 */
class Hl7v3NakFactory {

    static String nakMessageIdRoot = '1.2.3'

    /**
     * Creates a HL7 v3 NAK message as XML String.
     * 
     * @param originalMessageString
     *      original request message as XML String.
     * @param t
     *      the occurred exception.
     * @param nakRootElementName
     *      root element name of the target NAK message, for example, 
     *      'MCCI_IN000002UV01' or 'PRPA_IN201306UV02'.
     * @param needControlActProcess
     *      whether a <tt>controlAckProcess</tt> element should be created in the NAK.
     * @return
     */
    static String createNak(
            String originalMessageString,
            Throwable t,
            String nakRootElementName,
            boolean needControlActProcess)
    {
        GPathResult xml
        try {
            xml = slurp(originalMessageString)
        } catch (Exception e) {
            xml = slurp('<dummy/>')
        }

        return createNak(xml, t, nakMessageIdRoot, needControlActProcess)
    }


    /**
     * Creates a HL7 v3 NAK message as XML String.
     *
     * @param originalMessage
     *      original request message as parsed XML.
     * @param t
     *      the occurred exception.
     * @param nakRootElementName
     *      root element name of the target NAK message, for example,
     *      'MCCI_IN000002UV01' or 'PRPA_IN201306UV02'.
     * @param needControlActProcess
     *      whether a <tt>controlAckProcess</tt> element should be created in the NAK.
     * @return
     */
    static String createNak(
        GPathResult originalMessage,
        Throwable t,
        String nakRootElementName,
        boolean needControlActProcess)
    {
        OutputStream output = new ByteArrayOutputStream()
        MarkupBuilder builder = getBuilder(output)

        builder."${nakRootElementName}"(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema')
        {
            buildInstanceIdentifier(builder, 'id', false, nakMessageIdRoot, UUID.randomUUID().toString())
            creationTime(value: hl7timestamp())
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'MCCI_IN000002UV01')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, originalMessage, HL7V3_NSURI)

            acknowledgement {
                typeCode(code: 'AE')
                targetMessage {
                    def quid = originalMessage.id
                    buildInstanceIdentifier(builder, 'id', false, quid.@root.text(), quid.@extension.text())
                }
                acknowledgementDetail(typeCode: 'E') {
                    code(code: 'INTERR', codeSystem: '2.16.840.1.113883.5.1100')
                    text(t.getMessage())
                }
            }

            if (needControlActProcess) {
                controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                    def qbp = originalMessage.controlActProcess.queryByParameter
                    queryAck {
                        yieldElement(qbp.queryId, builder, HL7V3_NSURI)
                        queryResponseCode(code: 'AE')
                    }
                    yieldElement(qbp, builder, HL7V3_NSURI)
                }
            }
        }

        return output.toString()
    }
}
