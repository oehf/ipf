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

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import static org.openehealth.ipf.commons.xml.XmlYielder.yieldElement

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

        return createNak(xml, t, nakRootElementName, needControlActProcess)
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
        String typeCode0 = 'AE'
        String acknowledgementDetailCode0 = 'INTERR'
        String queryResponseCode0 = 'AE'
        String statusCode0

        String detectedIssueEventCode0
        String detectedIssueEventCodeSystem0 = '2.16.840.1.113883.5.4'
        String detectedIssueManagementCode0
        String detectedIssueManagementCodeSystem0 = '2.16.840.1.113883.5.4'

        // special mode for some exception types
        if (t instanceof Hl7v3Exception) {
            Hl7v3Exception hl7v3exception = (Hl7v3Exception) t
            typeCode0                          = hl7v3exception.typeCode
            acknowledgementDetailCode0         = hl7v3exception.acknowledgementDetailCode
            queryResponseCode0                 = hl7v3exception.queryResponseCode
            statusCode0                        = hl7v3exception.statusCode
            detectedIssueEventCode0            = hl7v3exception.detectedIssueEventCode
            detectedIssueEventCodeSystem0      = hl7v3exception.detectedIssueEventCodeSystem
            detectedIssueManagementCode0       = hl7v3exception.detectedIssueManagementCode
            detectedIssueManagementCodeSystem0 = hl7v3exception.detectedIssueManagementCodeSystem
        }
        if (t instanceof ValidationException) {
            typeCode0                    = 'AE'
            acknowledgementDetailCode0   = 'SYN113'
            queryResponseCode0           = 'QE'
            statusCode0                  = 'aborted'
            detectedIssueEventCode0      = 'ActAdministrativeDetectedIssueCode'
            detectedIssueManagementCode0 = 'VALIDAT'
        }

        // build the NAK
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
            interactionId(root: originalMessage.interactionId.@root.text(), extension: nakRootElementName)
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, originalMessage, HL7V3_NSURI)

            acknowledgement {
                typeCode(code: typeCode0)
                targetMessage {
                    def quid = originalMessage.id
                    buildInstanceIdentifier(builder, 'id', false, quid.@root.text(), quid.@extension.text())
                }
                acknowledgementDetail(typeCode: 'E') {
                    code(code: acknowledgementDetailCode0, codeSystem: '2.16.840.1.113883.5.1100')
                    text(t.getMessage())
                }
            }

            if (needControlActProcess) {
                controlActProcess(classCode: 'CACT', moodCode: 'EVN') {

                    // (optionally) create reasonOf element
                    if (detectedIssueEventCode0) {
                        reasonOf(typeCode: 'RSON') {
                            detectedIssueEvent(classCode: 'ALRT', moodCode: 'EVN') {
                                code(code: detectedIssueEventCode0, codeSystem: detectedIssueEventCodeSystem0)

                                if (detectedIssueManagementCode0) {
                                    mitigatedBy(typeCode: 'MITGT') {
                                        detectedIssueManagement(classCode: 'ACT', moodCode: 'EVN') {
                                            code(code: detectedIssueManagementCode0, codeSystem: detectedIssueManagementCodeSystem0)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // create queryAck element
                    def qbp = originalMessage.controlActProcess.queryByParameter
                    queryAck {
                        yieldElement(qbp.queryId, builder, HL7V3_NSURI)
                        if (statusCode0) {
                            statusCode(code: statusCode0)
                        }
                        queryResponseCode(code: queryResponseCode0)
                    }
                    yieldElement(qbp, builder, HL7V3_NSURI)
                }
            }
        }

        return output.toString()
    }
}
