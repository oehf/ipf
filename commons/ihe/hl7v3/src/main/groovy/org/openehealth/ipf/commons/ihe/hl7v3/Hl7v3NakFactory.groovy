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
 * Generator for HL7v3 ACK and NAK response messages.
 * <p>
 * This class has been tested in the context of PDQv3, XCPD, and QED transactions.
 * @author Dmytro Rud
 */
class Hl7v3NakFactory {

    static String messageIdRoot = '1.2.3'

    /**
     * Parses the given String into a {@link GPathResult GPath object}
     * and calls the other variant of the <code>response()</code> method.
     * See javadocs there.
     */
    static String response(
            String originalMessageString,
            Throwable throwable,
            String rootElementName,
            String controlActProcessCode,
            boolean useCAckTypeCodes,
            boolean includeQuantities)
    {
        GPathResult xml
        try {
            xml = slurp(originalMessageString)
        } catch (Exception ignored) {
            xml = slurp('<dummy/>')
        }

        return response(xml, throwable, rootElementName, controlActProcessCode, useCAckTypeCodes, includeQuantities)
    }


    /**
     * Creates an HL7v3 ACK/NAK response message as XML String.
     *
     * @param originalMessage
     *      original request message as parsed Groovy GPath instance.
     * @param throwable
     *      the occurred exception, may be <code>null</code>.
     * @param rootElementName
     *      root element name of the target NAK message, for example,
     *      'MCCI_IN000002UV01' or 'PRPA_IN201306UV02'.
     * @param controlActProcessCode
     *      whether a <tt>controlAckProcess</tt> element with the given code ID
     *      shall be created in the NAK.
     *      Note that setting this parameter to <code>true</code> is only allowed when
     *      <code>throwable!=null</code>.
     * @param useCAckTypeCodes
     *      if <code>true</code>, HL7v2 acknowledgement codes
     *      <tt>CA</tt>, <tt>CE</tt>, <tt>CR</tt> will be used instead of the default
     *      <tt>AA</tt>, <tt>AE</tt>, <tt>AR</tt>.  Ignored when the parameter
     *      <code>throwable</code> contanis an instance of {@link Hl7v3Exception}.
     * @param includeQuantities
     *      if <code>true</code>, the attributes <tt>QueryAck.resultTotalQuantity</tt>,
     *      <tt>QueryAck.resultCurrentQuantity</tt>, <tt>QueryAck.resultRemainingQuantity</tt>
     *      will be included in NAK.
     * @return
     *      HL7v3 response as XML String.
     */
    static String response(
        GPathResult originalMessage,
        Throwable throwable,
        String rootElementName,
        String controlActProcessCode,
        boolean useCAckTypeCodes,
        boolean includeQuantities)
    {
        if (controlActProcessCode && ! throwable) {
            throw new IllegalArgumentException("Cannot generate positive ACKs with <controlActProcess>")
        }

        String typeCode0 = (useCAckTypeCodes ? 'C' : 'A') + (throwable ? 'E' : 'A')
        String queryResponseCode0 = 'OK'

        // variables below will be used only when the parameter "throwable" is not null
        String statusCode0
        String acknowledgementDetailCode0
        String detectedIssueEventCode0
        String detectedIssueEventCodeSystem0
        String detectedIssueManagementCode0
        String detectedIssueManagementCodeSystem0

        // special mode for some exception types
        if (throwable) {
            acknowledgementDetailCode0         = 'INTERR'
            queryResponseCode0                 = 'AE'
            detectedIssueEventCodeSystem0      = '2.16.840.1.113883.5.4'
            detectedIssueManagementCodeSystem0 = '2.16.840.1.113883.5.4'
        }
        if (throwable instanceof Hl7v3Exception) {
            Hl7v3Exception hl7v3exception = (Hl7v3Exception) throwable
            typeCode0                          = hl7v3exception.typeCode
            acknowledgementDetailCode0         = hl7v3exception.acknowledgementDetailCode
            queryResponseCode0                 = hl7v3exception.queryResponseCode
            statusCode0                        = hl7v3exception.statusCode
            detectedIssueEventCode0            = hl7v3exception.detectedIssueEventCode
            detectedIssueEventCodeSystem0      = hl7v3exception.detectedIssueEventCodeSystem
            detectedIssueManagementCode0       = hl7v3exception.detectedIssueManagementCode
            detectedIssueManagementCodeSystem0 = hl7v3exception.detectedIssueManagementCodeSystem
        }
        if (throwable instanceof ValidationException) {
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

        builder."${rootElementName}"(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema')
        {
            buildInstanceIdentifier(builder, 'id', false, messageIdRoot, UUID.randomUUID().toString())
            creationTime(value: hl7timestamp())
            interactionId(root: originalMessage.interactionId.@root.text(), extension: rootElementName)
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
                if (throwable) {
                    acknowledgementDetail(typeCode: 'E') {
                        code(code: acknowledgementDetailCode0, codeSystem: '2.16.840.1.113883.5.1100')
                        text(throwable.getMessage())
                    }
                }
            }

            if (controlActProcessCode) {
                controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                    code(code: controlActProcessCode, codeSystem: '2.16.840.1.113883.1.6')

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
                        if (includeQuantities) {
                            resultTotalQuantity(value: '0')
                            resultCurrentQuantity(value: '0')
                            resultRemainingQuantity(value: '0')
                        }
                    }

                    yieldElement(qbp, builder, HL7V3_NSURI)
                }
            }
        }

        return output.toString()
    }
}
