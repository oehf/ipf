/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditEntityType;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.AUDIT_ENTITY_TYPE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;

public abstract class BalpAuditEventHelper {

    public static <T extends AuditEvent> T addAgent(T auditEvent,
                                                     ActiveParticipantRoleIdCode roleCode,
                                                     Reference who,
                                                     String networkAddress,
                                                     AuditEvent.AuditEventAgentNetworkType networkType) {
        auditEvent.addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode(roleCode.getCode())
                    .setSystem(DCM_SYSTEM_NAME)
                    .setDisplay(roleCode.getDisplayName())
                ))
            .setWho(who)
            .setRequestor(false)
            .setNetwork(new AuditEvent.AuditEventAgentNetworkComponent()
                .setType(networkType)
                .setAddress(networkAddress));
        return auditEvent;
    }

    /**
     * Sets the user agent (optional)
     *
     * @param userReference user reference (can be display only)
     * @return this instance
     */
    public static <T extends AuditEvent> T addUserAgent(T auditEvent,
                                                        V3ParticipationType typeCode,
                                                        Reference userReference) {
        auditEvent.addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode(typeCode.toCode())
                    .setSystem(typeCode.getSystem())
                ))
            .setWho(userReference)
            .setRequestor(true);
        return auditEvent;
    }


    public static <T extends AuditEvent> T addPatient(T auditEvent, Reference patientReference) {
        auditEvent.addEntity()
            .setWhat(patientReference)
            .setType(new Coding()
                .setCode(AuditEntityType._1.toCode())
                .setSystem(AuditEntityType._1.getSystem())
                .setDisplay(AuditEntityType._1.getDisplay()))
            .setRole(new Coding()
                .setCode(ObjectRole._1.toCode())
                .setSystem(ObjectRole._1.getSystem())
                .setDisplay(ObjectRole._1.getDisplay()));
        return auditEvent;
    }

    public static <T extends AuditEvent> T addTransactionEntity(T auditEvent, String xRequestId) {
        auditEvent.addEntity()
            .setWhat(new Reference()
                .setIdentifier(new Identifier()
                    .setValue(xRequestId)))
            .setType(new Coding()
                .setCode("XrequestId")
                .setSystem(AUDIT_ENTITY_TYPE_SYSTEM_NAME));
        return auditEvent;
    }

}
