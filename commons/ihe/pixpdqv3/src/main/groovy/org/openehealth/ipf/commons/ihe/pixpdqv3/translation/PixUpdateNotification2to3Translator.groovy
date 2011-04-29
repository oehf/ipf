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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation

import groovy.xml.MarkupBuilder;

import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * Translator for PIX Update notification v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PixUpdateNotification2to3Translator implements Hl7TranslatorV2toV3 {

	/**
	 * <tt>root</tt> attribute of message's <tt>id</tt> element.
	 */
	String messageIdRoot = '1.2.3'

    // 4*3 parameters for generation of elements "sender" and "receiver"
    String senderIdRoot = '1.2.3.4'
    String senderIdExtension = 'SE'
    String senderIdAssigningAuthority = 'SA'
        
    String senderAgentIdRoot = '1.2.3.5'
    String senderAgentIdExtension = 'SAE'
    String senderAgentIdAssigningAuthority = 'SAA'
    
    String receiverIdRoot = '1.2.3.6'
    String receiverIdExtension = 'RE'
    String receiverIdAssigningAuthority = 'RA'
        
    String receiverAgentIdRoot = '1.2.3.7'
    String receiverAgentIdExtension = 'RAE'
    String receiverAgentIdAssigningAuthority = 'RAA'

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
     * Translates an HL7 v2 <tt>ADT^A31</tt> message 
     * into HL7 v3 <tt>PRPA_IN201302UV02</tt> message.
     */
    String translateV2toV3(MessageAdapter adt, String dummy = null) {    
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output)

        builder.PRPA_IN201302UV02(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema') 
        {         
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, adt.MSH[10].value)    
            //creationTime(value: (adt.MSH[7][1] ? dropTimeZone(adt.MSH[7][1].value) : MessageUtils.hl7Now()))      
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
                                    buildInstanceIdentifier(builder, 'id', false, pid3[4][2].value, pid3[1].value)
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

        return output.toString()
    }
	
	
	
	/**
	 * Creates sender or receiver element.
	 */
    private void createAgent(MarkupBuilder builder, boolean sender) {
        String name = (sender ? 'sender' : 'receiver')
        
        String idRoot = this."${name}IdRoot"
        String idExtension = this."${name}IdExtension"
        String idAssigningAuthority = this."${name}IdAssigningAuthority"
        
        String agentIdRoot = this."${name}AgentIdRoot"
        String agentIdExtension = this."${name}AgentIdExtension"
        String agentIdAssigningAuthority = this."${name}AgentIdAssigningAuthority"
        
        builder."${name}"(typeCode: (sender ? 'SND' : 'RCV')) {
            device(determinerCode: 'INSTANCE', classCode: 'DEV') {
                buildInstanceIdentifier(builder, 'id', true, idRoot, idExtension, idAssigningAuthority)
                if (agentIdRoot || agentIdExtension || agentIdAssigningAuthority) {
                    asAgent(classCode: 'AGNT') {
                        representedOrganization(determinerCode: 'INSTANCE', classCode: 'ORG') {
                            buildInstanceIdentifier(builder, 'id', false, 
                                    agentIdRoot, agentIdExtension, agentIdAssigningAuthority)
                        }
                    }
                }
            }
        }
    }


}
