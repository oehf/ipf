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
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;

import java.util.Date;

import static org.hl7.fhir.r4.model.codesystems.Iso21089Lifecycle.DISCLOSE;
import static org.hl7.fhir.r4.model.codesystems.RestfulInteraction.READ;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;
import static org.openehealth.ipf.commons.audit.codes.EventIdCode.*;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_EXPORT_AUDIT_PROFILE;

/**
 * The AuditEvent the disclosing side of a privacy disclosure records. The two ends of the disclosure are
 * called source and recipient here rather than client and server, but they are the same agent slices,
 * carrying the same DICOM codes.
 *
 * @author Christian Ohr
 */
@ResourceDef(name = "AuditEvent", id = "ExportAuditEvent", profile = BALP_EXPORT_AUDIT_PROFILE)
public class ExportAuditEvent extends BalpAuditEvent {

    /** SNOMED CT code of the custodian of the disclosed data. */
    private static final String CUSTODIAN_CODE = "159541003";

    /** SNOMED CT code of the party that authorized the disclosure on the patient's behalf. */
    private static final String AUTHORIZER_CODE = "429577009";

    private static final String SNOMED_SYSTEM_NAME = "http://snomed.info/sct";

    public ExportAuditEvent() {
        super();
        setAction(AuditEventAction.R);
        setOutcome(AuditEventOutcome._0);
        setRecorded(new Date());
        setType(new Coding()
            .setCode(Export.getCode())
            .setSystem(DCM_SYSTEM_NAME)
            .setDisplay(Export.getOriginalText()));
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
     * @return DICOM 110153, which this profile fixes for the source of the disclosure
     */
    @Override
    protected Coding clientAgentType() {
        return dicomAgentType(Source);
    }

    /**
     * @return DICOM 110152, which this profile fixes for the recipient of the disclosure
     */
    @Override
    protected Coding serverAgentType() {
        return dicomAgentType(Destination);
    }

    /**
     * Sets the source agent (mandatory)
     *
     * @param sourceReference source reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public ExportAuditEvent setSource(Reference sourceReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        addAgent(clientAgentType(), sourceReference, networkAddress, networkType);
        return this;
    }

    /**
     * Sets the source agent (mandatory) from the active participant an ATNA audit record holds it in.
     *
     * @param source active participant standing for the source of the disclosure
     * @return this instance
     */
    public ExportAuditEvent setSource(ActiveParticipantType source) {
        addAgent(clientAgentType(), source);
        return this;
    }

    /**
     * Sets the recipient agent (mandatory)
     *
     * @param recipientReference recipient reference (can be display only)
     * @param networkAddress     network address
     * @param networkType        network type
     * @return this instance
     */
    public ExportAuditEvent setRecipient(Reference recipientReference,
                                         String networkAddress,
                                         AuditEventAgentNetworkType networkType) {
        addAgent(serverAgentType(), recipientReference, networkAddress, networkType);
        return this;
    }

    /**
     * Sets the recipient agent (mandatory) from the active participant an ATNA audit record holds it in.
     *
     * @param recipient active participant standing for the recipient of the disclosure
     * @return this instance
     */
    public ExportAuditEvent setRecipient(ActiveParticipantType recipient) {
        addAgent(serverAgentType(), recipient);
        return this;
    }

    /**
     * Sets the custodian agent (optional)
     *
     * @param custodianReference custodian reference that released the data (can be display only) (optional)
     * @return this instance
     */
    public ExportAuditEvent setCustodian(Reference custodianReference) {
        addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode(CUSTODIAN_CODE)
                    .setSystem(SNOMED_SYSTEM_NAME)
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
    public ExportAuditEvent setAuthorizer(Reference authorizerReference) {
        addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode(AUTHORIZER_CODE)
                    .setSystem(SNOMED_SYSTEM_NAME)
                ))
            .setRequestor(true)
            .setWho(authorizerReference);
        return this;
    }

    /**
     * Adds an entity for a piece of the disclosed data
     *
     * @param entity what was disclosed (can be display only)
     * @param type   entity type
     * @return this instance
     */
    public ExportAuditEvent addData(Reference entity, Coding type) {
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
    public ExportAuditEvent setPatient(Reference patientReference) {
        addPatientEntity(patientReference);
        return this;
    }

}
