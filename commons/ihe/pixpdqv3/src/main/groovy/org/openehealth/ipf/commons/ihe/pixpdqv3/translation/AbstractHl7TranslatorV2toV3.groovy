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

import groovy.xml.MarkupBuilder
import org.openehealth.ipf.modules.hl7dsl.SegmentAdapter

import static Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

import org.openehealth.ipf.modules.hl7dsl.SelectorClosure

/**
 * @author Dmytro Rud
 */
abstract class AbstractHl7TranslatorV2toV3 implements Hl7TranslatorV2toV3 {

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


    /**
     * Creates sub-elements of the element "patientPerson", which
     * are common for PIX Feed requests and PDQ responses.
     * @param builder
     *      target HL7v3 builder.
     * @param pid
     *      HL7v2 PID segment.
     */
    void createPatientPersonElements(MarkupBuilder builder, SegmentAdapter pid) {
        for (pid5 in pid[5]()) {
            createName(builder, pid5)
        }

        translateTelecom(builder, pid[13], 'H')
        translateTelecom(builder, pid[14], 'WP')

        def gender = (pid[8].value ?: '').mapReverse('hl7v2v3-bidi-administrativeGender-administrativeGender')
        builder.administrativeGenderCode(code: gender)
        builder.birthTime(value: pid[7][1].value ?: '')
        builder.addr {
            def pid11 = pid[11]
            conditional(builder, 'country',           pid11[6].value)
            conditional(builder, 'state',             pid11[4].value)
            conditional(builder, 'postalCode',        pid11[5].value)
            conditional(builder, 'city',              pid11[3].value)
            conditional(builder, 'streetAddressLine', pid11[1].value)
        }

        def pid4collection = pid[4]()
        if (pid4collection) {
            builder.asOtherIDs(classCode: 'PAT') {
                for(pid4 in pid4collection) {
                    buildInstanceIdentifier(builder, 'id', false, pid4)
                }
                scopingOrganization(classCode: 'ORG', determinerCode: 'INSTANCE') {
                    id(nullFlavor: 'UNK')
                }
            }
        }

        if (pid[19].value) {
            builder.asOtherIDs(classCode: 'PAT') {
                id(root: nationalIdentifierRoot, extension: pid[19].value)
                scopingOrganization(classCode: 'ORG', determinerCode: 'INSTANCE') {
                    id(root: nationalIdentifierRoot)
                }
            }
        }

    }

    /**
     * Translates telecommunication items from HL7v2 to HL7v3.
     * @param builder
     *      target HL7v3 builder.
     * @param repeatableXTN
     *      source set of HL7v2 XTN elements.
     * @param defaultUse
     *      default value of HL7v3 attribute "use".
     */
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


    /**
     * Creates sender or receiver element.
     */
    void createAgent(MarkupBuilder builder, boolean sender) {
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
