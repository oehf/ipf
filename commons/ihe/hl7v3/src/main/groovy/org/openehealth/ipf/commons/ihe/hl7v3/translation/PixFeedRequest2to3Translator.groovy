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
package org.openehealth.ipf.commons.ihe.hl7v3.translation

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Segment
import groovy.xml.MarkupBuilder
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.buildInstanceIdentifier

/**
 * Translator for PIX Feed requests v2 to v3.
 * @author Boris Stanojevic
 */
class PixFeedRequest2to3Translator extends AbstractHl7TranslatorV2toV3 {

    static final String CREATE_MSG = 'PRPA_IN201301UV02'
    static final String UPDATE_MSG = 'PRPA_IN201302UV02'
    static final String MERGE_MSG  = 'PRPA_IN201304UV02'


    private def translators = [
        A01: { Message adt, String charset -> translate(adt, CREATE_MSG, charset) },
        A04: { Message adt, String charset -> translate(adt, CREATE_MSG, charset) },
        A08: { Message adt, String charset -> translate(adt, UPDATE_MSG, charset) },
        A40: { Message adt, String charset -> translate(adt, MERGE_MSG, charset) }
    ]


    String translateV2toV3(Message adt, String dummy, String charset) {
        def triggerEvent = adt.MSH[9][2].value
        if (translators.containsKey(triggerEvent)){
            return translators[triggerEvent](adt, charset)
        } else {
            throw new Hl7TranslationException('Not supported HL7 message event')
        }
    }


    /**
     * Translates an HL7 v2 <tt>ADT^Axx</tt> message
     * into HL7 v3 <tt>PRPA_IN201301UV02</tt> or <tt>PRPA_IN201302UV02</tt>
     * or <tt>PRPA_IN201304UV02</tt> message.
     */
    String translate(Message adt, String interactId, String charset) {
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output, charset)

        builder."$interactId" (
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema') {
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, adt.MSH[10].value)
            creationTime(value: MessageUtils.hl7Now())
            interactionId(root: '2.16.840.1.113883.1.6', extension: interactId)
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'AL')

            createAgent(builder, "receiver")
            createAgent(builder, "sender")

            controlActProcess(classCode: 'CACT', moodCode: 'EVN') {
                code(code: interactId.replace('IN', 'TE'))
                subject(typeCode: 'SUBJ') {
                    registrationEvent(classCode: 'REG', moodCode: 'EVN') {
                        id(nullFlavor: 'NA')
                        statusCode(code: 'active')
                        subject1(typeCode: 'SBJ') {
                            def PIDSegment = getPID(adt)
                            patient(classCode: 'PAT') {
                                for (pid3 in PIDSegment[3]()) {
                                    buildInstanceIdentifier(builder, 'id', false, pid3)
                                }
                                statusCode(code: 'active')
                                patientPerson(classCode: 'PSN', determinerCode: 'INSTANCE') {
                                    createPatientPersonElements(builder, PIDSegment)
                                    createBirthPlaceElement(builder, PIDSegment)
                                }
                                providerOrganization(classCode: 'ORG', determinerCode: 'INSTANCE') {
                                    buildInstanceIdentifier(builder, 'id', false,
                                                      this.mpiSystemIdRoot, this.mpiSystemIdExtension)
                                    contactParty(classCode: 'CON'){
                                        contactPerson(classCode: 'PSN', determinerCode: 'INSTANCE'){
                                            name(nullFlavor: 'UNK')
                                        }
                                    }
                                }
                            }
                        }
                        createCustodian(builder, this.mpiSystemIdRoot, this.mpiSystemIdExtension)
                        if (interactId == MERGE_MSG){
                            createReplacementOf(builder, adt)
                        }
                    }
                }
            }
        }
        postprocess(adt, builder)
        return output.toString(charset)
    }


    void createReplacementOf(MarkupBuilder builder, Message adt){
        builder.replacementOf(typeCode: 'RPLC'){
            priorRegistration(classCode: 'REG', moodCode: 'EVN'){
                statusCode(code: 'obsolete')
                subject1(typeCode: 'SBJ'){
                    priorRegisteredRole(classCode: 'PAT'){
                        buildInstanceIdentifier(builder, 'id', false, adt.PATIENT.MRG[1](0))
                    }
                }
            }
        }
    }


    /**
     * Returns a PID that is either directly in the PID segment
     * or within a PATIENT group.
     *
     * FIXME replace with msg.findPID
     */
    static Segment getPID(Message msg) {
        def names = msg.names
        if ('PID' in names) {
            return msg.PID
        }
        if ('PATIENT' in names) {
            return msg.PATIENT.PID
        }
        if ('PIDPD1MRGPV1' in names) {
            return msg.PIDPD1MRGPV1.PID
        }
        throw new Hl7TranslationException('Cannot find PID segment in: ' + msg)
    }

}
