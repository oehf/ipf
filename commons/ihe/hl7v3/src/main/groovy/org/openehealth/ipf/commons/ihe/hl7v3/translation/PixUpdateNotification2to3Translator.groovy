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

import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * Translator for PIX Update notification v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PixUpdateNotification2to3Translator extends AbstractHl7TranslatorV2toV3 {

    /**
     * Translates an HL7 v2 <tt>ADT^A31</tt> message 
     * into HL7 v3 <tt>PRPA_IN201302UV02</tt> message.
     */
    String translateV2toV3(MessageAdapter adt, String dummy, String charset) {
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output, charset)

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

            createAgent(builder, false)
            createAgent(builder, true)

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
                                fakePatientPerson(builder)
                            }
                        }
                        createCustodian(builder, this.mpiSystemIdRoot, this.mpiSystemIdExtension)  
                    }
                }
            }
        }

        return output.toString(charset)
    }
	
}
