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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation;

import java.util.Map

import groovy.xml.MarkupBuilder
import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter

import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*

/**
 * PDQ Response translator HL7 v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PdqResponse2to3Translator implements Hl7TranslatorV2toV3 {

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
     * If true, <code>&lt;resultTotalQuantity nullFlavor="UNK"/&gt;</code>
     * will appear on the output. Otherwise the element will not be present.
     */
    boolean outputResultTotalQuantity = true

    /**
     * If true, <code>&lt;resultRemainingQuantity nullFlavor="UNK"/&gt;</code>
     * will appear on the output. Otherwise the element will not be present.
     */
    boolean outputResultRemainingQuantity = true

    /**
     * Predefined fix value of <code>//patient/queryMatchObservation/value/@value</code>.
     */
    String defaultMatchQuality = '50'

    /**
     * Predefined fix value of <code>//acknowledgementDetail/code/@codeSystem</code>.
     */
    String errorCodeSystem = '2.16.840.1.113883.12.357'

    /**
     * If true, the translation will handle DSC segments from the PDQv2 interface. 
     * Otherwise the presence of the DSC segment will cause errors.
     */
    boolean supportQueryContinuation = false

    /**
     * <tt>root</tt> attribute of message's <tt>id</tt> element.
     */
    String messageIdRoot = '1.2.3'


    /**
     * Translates HL7 v2 response messages <tt>RSP^K22</tt> and <tt>ACK</tt> 
     * into HL7 v3 message <tt>PRPA_IN201306UV02</tt>.
     */
    String translateV2toV3(MessageAdapter rsp, String origMessage) {
        def writer  = new StringWriter()
        def builder = getBuilder(writer)

        def xml = slurp(origMessage)

        def messageTimestamp = dropTimeZone(rsp.MSH[7][1].value) ?: MessageUtils.hl7Now()
        def status = getStatusInformation(rsp, xml)
        
        builder.PRPA_IN201306UV02(
                'ITSVersion': 'XML_1.0',
                'xmlns':      'urn:hl7-org:v3',
                'xmlns:xsi':  'http://www.w3.org/2001/XMLSchema-instance',
                'xmlns:xsd':  'http://www.w3.org/2001/XMLSchema') 
        {
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, rsp.MSH[10].value)
            creationTime(value: messageTimestamp)
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'PRPA_IN201306UV02')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, xml, 'urn:hl7-org:v3')
            createQueryAcknowledgementElement(builder, xml, status, this.errorCodeSystem) 
            controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                code(code: 'PRPA_TE201306UV02', codeSystem: '2.16.840.1.113883.1.6')
                effectiveTime(value: messageTimestamp)
                
                if (rsp.MSH[9][1].value == 'RSP') {
                    rsp.QUERY_RESPONSE().each { qr ->
                        def pid3collection = qr.PID[3]() 
                        if (pid3collection) {
                            subject(typeCode: 'SUBJ') {
                                registrationEvent(classCode: 'REG', moodCode: 'EVN') {
                                    statusCode(code: 'active')
                                    subject1(typeCode: 'SBJ') {
                                        patient(classCode: 'PAT') {
                                            for(pid3 in pid3collection) {   
                                                buildInstanceIdentifier(builder, 'id', false, 
                                                        pid3[4][2].value, pid3[1].value, pid3[4][1].value) 
                                            }
                                            statusCode(code: 'active')
                                            patientPerson {
                                                createName(builder, qr.PID[5])

                                                qr.PID[13]().collect { it[1].value }.each { tel ->
                                                    telecom(value: "tel: ${tel}")
                                                }

                                                def gender = (qr.PID[8].value ?: '').mapReverse('bidi-administrativeGender-administrativeGender')
                                                administrativeGenderCode(code: gender)
                                                birthTime(value: qr.PID[7][1].value ?: '')
                                                addr {
                                                    def pid11 = qr.PID[11]
                                                    conditional(builder, 'country',           pid11[6].value)
                                                    conditional(builder, 'state',             pid11[4].value)
                                                    conditional(builder, 'postalCode',        pid11[5].value)
                                                    conditional(builder, 'city',              pid11[3].value)
                                                    conditional(builder, 'streetAddressLine', pid11[1][1].value)
                                                }
                                            }
                                            /*
                                            providerOrganization {
                                                buildInstanceIdentifier(builder, 'id', false, 
                                                        qr.PID[3][4][2].value, '', '') 
                                            }
                                            */
                                            subjectOf1 {
                                                queryMatchObservation(classCode: 'COND', moodCode: 'EVN') {
                                                    code(code: 'IHE_PDQ')
                                                    value('xsi:type': 'INT', value: this.defaultMatchQuality)
                                                }
                                            }
                                        }
                                    }
                                    createCustodian(builder, this.mpiSystemIdRoot, this.mpiSystemIdExtension)
                                }
                            }                            
                        }
                    }
                }
                
                queryAck {
                    int patientCount = (rsp.MSH[9][1].value == 'RSP') ? rsp.QUERY_RESPONSE().size() : 0
                    
                    buildInstanceIdentifier(builder, 'queryId', false, 
                            xml.id.@root.text(), xml.id.@extension.text())

                    queryResponseCode(code: status.responseStatus)
                    if (this.outputResultTotalQuantity) {
                        resultTotalQuantity(value: patientCount)
                    }
                    resultCurrentQuantity(value: patientCount)
                    if (this.outputResultRemainingQuantity) {
                        resultRemainingQuantity(value: '0')
                    }
                }
                
                XmlYielder.yieldElement(xml.controlActProcess.queryByParameter, builder, 'urn:hl7-org:v3')
            }
        }

        return writer.toString()
    }

     
    private Map getStatusInformation(MessageAdapter rsp, GPathResult xml) {
        def responseStatus = 'OK'
        def errorText      = ''
        def errorCode      = ''
        def errorLocations = []
        def ackCode        = rsp.MSA[1].value
        
        if (ackCode[1] == 'A') {
            if (rsp.MSH[9][1].value == 'RSP') {
                // continuations are not supported
                if ((!this.supportQueryContinuation) && rsp.DSC[1].value) {
                    ackCode = 'AE'
                    responseStatus = 'AE'
                    errorText = 'resultTotalQuantity > initialQuantity, but query continuation not supported'
                } else {
                    responseStatus = rsp.QAK[2].value ?: ''                    
                }
            }
        } else {
            responseStatus = ackCode
            errorCode = rsp.ERR[3][1].value ?: ''
            errorText = "PDQv2 Interface Reported [${rsp.ERR[6].value ?: ''} ${rsp.ERR[7].value ?: ''}]"

            // collect error locations
            errorLocations = rsp.ERR[2]()?.collect { err ->
                if ((err[1].value == 'QPD') && (err[2].value == '1') && (err[3].value == '8')) {
                    return '/' + xml.interactionId.@extension.text() +
                           '/controlActProcess/queryByParameter/parameterList/otherIDsScopingOrganization' +
                           (err[4].value ? ('[' + (err[4].value + 1) + ']') : '')
                }
                return '/'
            }
        } 
        
        return [ackCode:        ackCode, 
                errorText:      errorText, 
                errorCode:      errorCode, 
                errorLocations: errorLocations, 
                responseStatus: responseStatus]
        
    }
}
