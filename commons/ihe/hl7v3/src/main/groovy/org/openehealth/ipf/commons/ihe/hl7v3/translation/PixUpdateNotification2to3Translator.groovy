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
import groovy.xml.MarkupBuilder
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.buildInstanceIdentifier

/**
 * Translator for PIX Update notification v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PixUpdateNotification2to3Translator extends AbstractHl7TranslatorV2toV3 {

    /**
     * Translates an HL7 v2 <tt>ADT^A31</tt> message 
     * into HL7 v3 <tt>PRPA_IN201302UV02</tt> message.
     */
    String translateV2toV3(Message adt, String dummy, String charset) {
        OutputStream output = new ByteArrayOutputStream()
        MarkupBuilder builder = getBuilder(output, charset)

        builder.PRPA_IN201302UV02(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema') 
        {         
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, adt.MSH[10].value)    
            creationTime(value: MessageUtils.hl7Now())
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'PRPA_IN201302UV02')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'AL')

            createAgent(builder, "receiver")
            createAgent(builder, "sender")

            controlActProcess(classCode: 'CACT', moodCode: 'EVN') { 
                code(code: 'PRPA_TE201302UV02', codeSystem: '2.16.840.1.113883.1.6')
                subject(typeCode: 'SUBJ') {
                    registrationEvent(classCode: 'REG', moodCode: 'EVN') {
                        id(nullFlavor: 'NA')
                        statusCode(code: 'active')
                        subject1(typeCode: 'SBJ') {
                            patient(classCode: 'PAT') {
                                for (pid3 in adt.PID[3]()) {
                                    buildInstanceIdentifier(builder, 'id', false, pid3)
                                }
                                statusCode(code: 'active')
                                addPerson(builder, adt)
                            }
                        }
                        createCustodian(builder, this.mpiSystemIdRoot, this.mpiSystemIdExtension)  
                    }
                }
            }
        }

        return output.toString(charset)
    }

    /**
     * Adds a person element to the patient. By default a person with empty name is added, like this:
     * <pre>
     *     builder.patientPerson(classCode: 'PSN', determinerCode: 'INSTANCE') { name(nullFlavor: 'NA') }
     * </pre>
     * @param builder
     */
    void addPerson(MarkupBuilder builder, Message adt) {
        fakePatientPerson(builder)
    }
}
