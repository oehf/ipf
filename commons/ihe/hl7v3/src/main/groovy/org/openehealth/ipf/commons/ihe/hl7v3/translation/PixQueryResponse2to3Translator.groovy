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
package org.openehealth.ipf.commons.ihe.hl7v3.translation

import ca.uhn.hl7v2.model.Message
import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.openehealth.ipf.commons.xml.XmlYielder
import org.openehealth.ipf.modules.hl7.ErrorLocation
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*

/**
 * PIX Query Response translator HL7 v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PixQueryResponse2to3Translator implements Hl7TranslatorV2toV3 {

    /**
     * Predefined fix value of
     * <code>//registrationEvent/custodian/assignedEntity/id/@root</code>.
     * In productive environments to be set to the <code>id/@root</code>
     * of the device representing the MPI.
     */
    String mpiSystemIdRoot = '1.2.3'

    /**
     * Predefined fix value of
     * <code>//registrationEvent/custodian/assignedEntity/id/@extension</code>.
     * In productive environments to be set to the <code>id/@extension</code>
     * of the device representing the MPI.
     */
    String mpiSystemIdExtension = ''

    /**
     * Predefined fix value of <code>//acknowledgementDetail/code/@codeSystem</code>.
     */
    String errorCodeSystem = '2.16.840.1.113883.12.357'

    /**
     * <tt>root</tt> attribute of message's <tt>id</tt> element.
     */
    String messageIdRoot = '1.2.3'

    /**
     * First character of the acknowledgment code.  Possible values are 'A' and 'C'.
     * <p>
     * Declared as String from technical reasons.
     */
    String ackCodeFirstCharacter = 'A'


    /**
     * Translates HL7 v2 response messages <tt>RSP^K23</tt> and <tt>ACK</tt> 
     * into HL7 v3 message <tt>PRPA_IN201310UV02</tt>.
     */
    String translateV2toV3(Message rsp, String origMessage, String charset) {
        OutputStream output = new ByteArrayOutputStream()
        MarkupBuilder builder = getBuilder(output, charset)

        def xml = slurp(origMessage)

        def messageTimestamp = dropTimeZone(rsp.MSH[7][1].value) ?: MessageUtils.hl7Now()
        def status = getStatusInformation(rsp, xml)
        
        builder.PRPA_IN201310UV02(
                'ITSVersion': 'XML_1.0',
                'xmlns':      HL7V3_NSURI,
                'xmlns:xsi':  'http://www.w3.org/2001/XMLSchema-instance',
                'xmlns:xsd':  'http://www.w3.org/2001/XMLSchema') 
        {
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, rsp.MSH[10].value)
            creationTime(value: MessageUtils.hl7Now())
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'PRPA_IN201310UV02')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, xml, HL7V3_NSURI)
            createQueryAcknowledgementElement(builder, xml, status, this.errorCodeSystem, this.ackCodeFirstCharacter) 

            controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                code(code: 'PRPA_TE201310UV02', codeSystem: '2.16.840.1.113883.1.6')
                effectiveTime(value: messageTimestamp)
                
                if (rsp.MSH[9][1].value == 'RSP') {
					def pid3collection = rsp.QUERY_RESPONSE.PID[3]()
                    if (pid3collection) {
                        subject(typeCode: 'SUBJ') {
                            registrationEvent(classCode: 'REG', moodCode: 'EVN') {
                                statusCode(code: 'active')
                                subject1(typeCode: 'SBJ') {
                                    patient(classCode: 'PAT') {
                                        for(pid3 in pid3collection) {   
                                            buildInstanceIdentifier(builder, 'id', false, pid3)
                                        }
										statusCode(code: 'active')
                                        addPerson(builder, rsp)
									}
								}
								createCustodian(builder, this.mpiSystemIdRoot, this.mpiSystemIdExtension)
                            }                            
                        }
				    }
				}
                
                queryAck {
                    def queryId = xml.controlActProcess.queryByParameter.queryId
                    buildInstanceIdentifier(builder, 'queryId', false, 
                            queryId.@root.text(), queryId.@extension.text())

                    String responseStatus = (rsp.MSH[9][1].value == 'RSP') ? rsp.QAK[2].value : 'AE'
                    queryResponseCode(code: responseStatus)
                }
                XmlYielder.yieldElement(xml.controlActProcess.queryByParameter, builder, HL7V3_NSURI)
            }
        }
        postprocess(rsp, builder)
        return output.toString(charset)
    }

    /**
     * Adds a person element to the patient. By default a person with empty name is added, like this:
     * <pre>
     *     builder.patientPerson(classCode: 'PSN', determinerCode: 'INSTANCE') { name(nullFlavor: 'NA') }
     * </pre>
     * @param builder
     */
    void addPerson(MarkupBuilder builder, Message rsp) {
        fakePatientPerson(builder)
    }
     
    private Map getStatusInformation(Message rsp, GPathResult xml) {
        def ackCode   = rsp.MSA[1].value.endsWith('A') ? 'AA' : 'AE'
        def errorCode = rsp.ERR[3][1].value ?: ''
        def errorText = "PIXv2 Interface Reported [${collectErrorInfo(rsp)}]"

        // collect error locations
        def errorLocations = rsp.ERR[2]()?.collect { err ->
            if (err[1].value == 'QPD') {
                String rootPath = '/' + xml.interactionId.@extension.text() +
                    '/controlActProcess/queryByParameter/parameterList/'

                switch (err[3].value) {
                case '3':
                    return rootPath + 'patientIdentifier/value'
                case '4':
                    String elementIndexString = ''
                    if (err[4].value) {
                        int index = Math.max(0, Integer.parseInt(err[4].value) - ErrorLocation.fieldRepetitionIndexingBase)
                        elementIndexString += "[${index}]"
                    }
                    return rootPath + 'dataSource' + elementIndexString + '/value'
                }
            }
            return '/'
        }
        
        return [ackCode:        ackCode, 
                errorText:      errorText, 
                errorCode:      errorCode, 
                errorLocations: errorLocations]
        
    }

    @Override
    void postprocess(Message msg, MarkupBuilder builder) {
    }
}
