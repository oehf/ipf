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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditEntityType;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.RestfulInteraction;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;

import java.util.Date;

import static org.hl7.fhir.r4.model.codesystems.AuditEventType.REST;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Application;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.PROVENANCE_PARTICIPANT_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_DELETE_AUDIT_PROFILE;

@ResourceDef(name = "AuditEvent", id = "DeleteAuditEvent", profile = BALP_DELETE_AUDIT_PROFILE)
public class DeleteAuditEvent extends AuditEvent {

    public DeleteAuditEvent() {
        super();
        setAction(AuditEventAction.D);
        setOutcome(AuditEventOutcome._0);
        setRecorded(new Date());
        setType(new Coding()
            .setCode(REST.toCode())
            .setSystem(REST.getSystem())
            .setDisplay(REST.getDisplay()));
        addSubtype()
            .setCode(RestfulInteraction.DELETE.toCode())
            .setSystem(RestfulInteraction.DELETE.getSystem());
    }

    /**
     * Sets the client agent (mandatory)
     *
     * @param clientReference client reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public DeleteAuditEvent setClient(Reference clientReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        return BalpAuditEventHelper.addAgent(this,
            Application, clientReference,
            networkAddress, networkType);
    }

    /**
     * Sets the server agent (mandatory)
     *
     * @param serverReference server reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public DeleteAuditEvent setServer(Reference serverReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode("custodian")
                    .setSystem(PROVENANCE_PARTICIPANT_SYSTEM_NAME)
                ))
            .setWho(serverReference)
            .setRequestor(false)
            .setNetwork(new AuditEventAgentNetworkComponent()
                .setType(networkType)
                .setAddress(networkAddress));
        return this;
    }

    /**
     * Sets the user agent (optional)
     *
     * @param userReference user reference (can be display only)
     * @return this instance
     */
    public DeleteAuditEvent setUser(V3ParticipationType typeCode, Reference userReference) {
        return BalpAuditEventHelper.addUserAgent(this, typeCode, userReference);
    }

    public DeleteAuditEvent setTransaction(String xRequestId) {
        return BalpAuditEventHelper.addTransactionEntity(this, xRequestId);
    }

    /**
     * Sets the data entity (mandatory)
     *
     * @param entity     entity data
     * @param entityRole entity role
     * @return this instance
     */
    public DeleteAuditEvent setData(Reference entity, ObjectRole entityRole) {
        if (entityRole != ObjectRole._3 &&
            entityRole != ObjectRole._4 &&
            entityRole != ObjectRole._20) {
            throw new IllegalArgumentException("Must be object role report, domain resource or job");
        }
        addEntity()
            .setWhat(entity)
            .setType(new Coding()
                .setCode(AuditEntityType._2.toCode())
                .setSystem(AuditEntityType._2.getSystem())
                .setDisplay(AuditEntityType._2.getDisplay()))
            .setRole(new Coding()
                .setCode(entityRole.toCode())
                .setSystem(entityRole.getSystem())
                .setDisplay(entityRole.getDisplay()));
        return this;
    }
}
