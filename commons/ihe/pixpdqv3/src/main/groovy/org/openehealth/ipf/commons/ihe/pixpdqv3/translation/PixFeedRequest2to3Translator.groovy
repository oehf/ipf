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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation

import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.SelectorClosure
import groovy.xml.MarkupBuilder

import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * Translator for PIX Feed v2 to v3.
 * @author Boris Stanojevic
 */
class PixFeedRequest2to3Translator implements Hl7TranslatorV2toV3 {

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
     * Root for values from PID-19.
     */
    String nationalIdentifierRoot = '2.16.840.1.113883.4.1'

    static final String CREATE_MSG = 'PRPA_IN201301UV02'
    static final String UPDATE_MSG = 'PRPA_IN201302UV02'
    static final String MERGE_MSG  = 'PRPA_IN201304UV02'


    private def translators = [
        A01: { MessageAdapter adt -> translate(adt, CREATE_MSG) },
        A04: { MessageAdapter adt -> translate(adt, CREATE_MSG) },
        A08: { MessageAdapter adt -> translate(adt, UPDATE_MSG) },
        A40: { MessageAdapter adt -> translate(adt, MERGE_MSG) }
    ]

    String translateV2toV3(MessageAdapter adt, String dummy = null) {
        def msh = adt.MSH[9][2].value
        if (translators.containsKey(msh)){
            translators[msh](adt)
        } else {
            throw new Exception('Not supported HL7 message event')
        }
    }


    /**
     * Translates an HL7 v2 <tt>ADT^Axx</tt> message
     * into HL7 v3 <tt>PRPA_IN201301UV02</tt> or <tt>PRPA_IN201302UV02</tt>
     * or <tt>PRPA_IN201304UV02</tt> message.
     */
    String translate(MessageAdapter adt, String interactId) {
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output)

        builder."$interactId" (
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema')
        {
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, adt.MSH[10].value)
            creationTime(value: (adt.MSH[7][1] ? dropTimeZone(adt.MSH[7][1].value) : MessageUtils.hl7Now()))
            interactionId(root: '2.16.840.1.113883.1.6', extension: interactId)
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'AL')

            createAgent(builder, false)
            createAgent(builder, true)

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
                                    buildInstanceIdentifier(builder, 'id', false, pid3[4][2].value, pid3[1].value)
                                }
                                statusCode(code: 'active')
                                patientPerson(classCode: 'PSN', determinerCode: 'INSTANCE') {
                                    for (pid5 in PIDSegment[5]()) {
                                        createName(builder, pid5)
                                    }

                                    translateTelecom(builder, PIDSegment[13], 'H')
                                    translateTelecom(builder, PIDSegment[14], 'WP')

                                    def gender = (PIDSegment[8].value ?: '').mapReverse('hl7v2v3-bidi-administrativeGender-administrativeGender')
                                    administrativeGenderCode(code: gender)
                                    birthTime(value: PIDSegment[7][1].value ?: '')
                                    addr {
                                        def pid11 = PIDSegment[11]
                                        conditional(builder, 'country',           pid11[6].value)
                                        conditional(builder, 'state',             pid11[4].value)
                                        conditional(builder, 'postalCode',        pid11[5].value)
                                        conditional(builder, 'city',              pid11[3].value)
                                        conditional(builder, 'streetAddressLine', pid11[1].value)
                                    }

                                    def pid4collection = PIDSegment[4]()
                                    if (pid4collection) {
                                        asOtherIDs(classCode: 'PAT') {
                                            for(pid4 in pid4collection) {
                                                buildInstanceIdentifier(builder, 'id', false,
                                                    pid4[4][2].value, pid4[1].value, pid4[4][1].value)
                                            }
                                            scopingOrganization(classCode: 'ORG', determinerCode: 'INSTANCE') {
                                                id(nullFlavor: 'UNK')
                                            }
                                        }
                                    }

                                    if (PIDSegment[19].value) {
                                        asOtherIDs(classCode: 'PAT') {
                                            id(root: nationalIdentifierRoot, extension: PIDSegment[19].value)
                                            scopingOrganization(classCode: 'ORG', determinerCode: 'INSTANCE') {
                                                id(root: nationalIdentifierRoot)
                                            }
                                        }
                                    }
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
                        if (interactId.equals(MERGE_MSG)){
                            createReplacementOf(builder, adt)
                        }
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

    void translateTelecom(MarkupBuilder builder, SelectorClosure repeatableXTN, String defaultUse) {
        repeatableXTN().each { telecom ->
          String number = telecom[1].value ?: telecom[4].value
          if (number) {
              String use = defaultUse
              String schema = 'tel'

              switch (telecom[2].value) {
              case 'PRN':
                  use = 'H'
                  break
              case 'WPN':
                  use = 'WP'
                  break
              }

              switch (telecom[3].value) {
              case 'PH':
                  // take the defaults
                  break
              case 'CP':
                  use = 'MC'
                  break
              case 'FX':
                  schema = 'fax'
                  break
              case 'Internet':
              case 'X.400':
                  schema = 'mailto'
                  break
              }
              builder.telecom(value: "${schema}:${number}", use: use)
          }
        }
    }

    void createReplacementOf(MarkupBuilder builder, MessageAdapter adt){
        builder.replacementOf(typeCode: 'RPLC'){
            priorRegistration(classCode: 'REG', moodCode: 'EVN'){
                statusCode(code: 'obsolete')
                subject1(typeCode: 'SBJ'){
                    priorRegisteredRole(classCode: 'PAT'){
                      buildInstanceIdentifier(builder, 'id', false,
                          adt.PIDPD1MRGPV1.MRG[1][4][2].value,
                          adt.PIDPD1MRGPV1.MRG[1][1].value,
                          adt.PIDPD1MRGPV1.MRG[1][4][1].value)
                    }
                }
            }
        }
    }


     /**
     * Returns a PID that is either directly in the PID segment
     * or within a PATIENT group.
     */
    static def getPID(MessageAdapter msg) {
        def names = msg.names
        if (names.find { it == 'PID' } != null) {
            return msg.PID
        }
        if (names.find { it == 'PATIENT' } != null) {
            return msg.PATIENT.PID
        }
        if (names.find { it == 'PIDPD1MRGPV1' } != null) {
            return msg.PIDPD1MRGPV1.PID
        }
        throw new IllegalArgumentException('Cannot find PID segment in: ' + msg)
    }

}
