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
import org.hl7.fhir.r4.model.codesystems.ObjectRole;

import java.util.Date;

import static org.hl7.fhir.r4.model.codesystems.Iso21089Lifecycle.DISCLOSE;
import static org.hl7.fhir.r4.model.codesystems.RestfulInteraction.READ;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;
import static org.openehealth.ipf.commons.audit.codes.EventIdCode.Import;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_IMPORT_AUDIT_PROFILE;

@ResourceDef(name = "AuditEvent", id = "ImportAuditEvent", profile = BALP_IMPORT_AUDIT_PROFILE)
public class ImportAuditEvent extends AuditEvent {

    public ImportAuditEvent() {
        super();
        setAction(AuditEventAction.C);
        setOutcome(AuditEventOutcome._0);
        setRecorded(new Date());
        setType(new Coding()
            .setCode(Import.getCode())
            .setSystem(DCM_SYSTEM_NAME)
            .setDisplay(Import.getDisplayName()));
        addSubtype()
            .setCode(DISCLOSE.toCode())
            .setSystem(DISCLOSE.getSystem())
            .setDisplay(DISCLOSE.getDisplay());
        addSubtype()
            .setCode(READ.toCode())
            .setSystem(READ.getSystem())
            .setDisplay(READ.getDisplay());
    }

    /**
     * Sets the source agent (mandatory)
     *
     * @param sourceReference source reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public ImportAuditEvent setSource(Reference sourceReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        return BalpAuditEventHelper.addAgent(this,
            Source, sourceReference,
            networkAddress, networkType);
    }

    /**
     * Sets the recipient agent (mandatory)
     *
     * @param recipientReference recipient reference (can be display only)
     * @param networkAddress     network address
     * @param networkType        network type
     * @return this instance
     */
    public ImportAuditEvent setRecipient(Reference recipientReference,
                                         String networkAddress,
                                         AuditEventAgentNetworkType networkType) {
        return BalpAuditEventHelper.addAgent(this,
            Destination, recipientReference,
            networkAddress, networkType);
    }

    /**
     * Sets the custodian agent (optional)
     *
     * @param custodianReference custodian reference that released the data (can be display only) (optional)
     * @return this instance
     */
    public ImportAuditEvent setCustodian(Reference custodianReference) {
        addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode("159541003")
                    .setSystem("http://snomed.info/sct")
                ))
            .setWho(custodianReference);
        return this;
    }

    /**
     * Sets the authorizer agent that represented the patient (may be the patient) (optional)
     *
     * @param authorizerReference authorizer reference (can be display only)
     * @return this instance
     */
    public ImportAuditEvent setAuthorizer(Reference authorizerReference) {
        addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode("429577009")
                    .setSystem("http://snomed.info/sct")
                ))
            .setRequestor(true)
            .setWho(authorizerReference);
        return this;
    }

    public ImportAuditEvent setData(Reference entity, Coding type) {
        addEntity()
            .setWhat(entity)
            .setType(type)
            .setRole(new Coding()
                .setCode(ObjectRole._3.toCode())
                .setSystem(ObjectRole._3.getSystem())
                .setDisplay(ObjectRole._3.getDisplay()));
        return this;
    }

    /**
     * Sets the patient entity (mandatory)
     *
     * @param patientReference patient reference
     * @return this instance
     */
    public ImportAuditEvent setPatient(Reference patientReference) {
        return BalpAuditEventHelper.addPatient(this, patientReference);
    }

}
