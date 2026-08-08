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

package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.codesystems.AuditEntityType;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEvent;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEventHelper;

import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.ProvideDocumentBundle;

/**
 * The AuditEvent of the Provide Document Bundle [ITI-65] transaction. Unlike the other MHD transactions,
 * this one is not profiled on one of the BALP patterns but constrains AuditEvent directly: its event type
 * is a DICOM PHI transfer code rather than the RESTful one, and it carries the SubmissionSet next to the
 * patient. It is still built from the same slices as the patterns are, which is what it takes
 * {@link BalpAuditEvent} for.
 * <p>
 * Its outcome is required but not fixed, so unlike the BALP patterns this AuditEvent can report a failed
 * transaction as well -- see {@link #supports(AuditMessage)}.
 * <p>
 * Which of the two profiles the record claims is decided by the concrete subclass.
 *
 * @author Christian Ohr
 * @since 5.3
 * @see ProvideBundleSourceAuditEvent
 * @see ProvideBundleRecipientAuditEvent
 */
abstract class ProvideBundleAuditEvent extends BalpAuditEvent {

    protected ProvideBundleAuditEvent() {
        super();
        addTransactionSubtype(ProvideDocumentBundle);
    }

    /**
     * @return DICOM 110153, which this profile fixes for the Document Source agent
     */
    @Override
    protected Coding clientAgentType() {
        return dicomAgentType(Source);
    }

    /**
     * @return DICOM 110152, which this profile fixes for the Document Recipient agent
     */
    @Override
    protected Coding serverAgentType() {
        return dicomAgentType(Destination);
    }


    /**
     * The profile constrains the outcome but does not fix it, and its patient entity is optional, so
     * every Provide Document Bundle is reported with this AuditEvent rather than being stepped down.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @return always true
     */
    @Override
    public boolean supports(AuditMessage auditMessage) {
        return true;
    }

    /**
     * Fills in what this profile takes from the audit record beyond the common part: the outcome and the
     * event itself, the patient, and the SubmissionSet.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @param localRole    which end of the transaction recorded it
     */
    @Override
    protected void initializeFrom(AuditMessage auditMessage, ActiveParticipantRoleIdCode localRole) {
        super.initializeFrom(auditMessage, localRole);

        var eventIdentification = auditMessage.getEventIdentification();
        setOutcome(AuditEventOutcome.fromCode(String.valueOf(eventIdentification.getEventOutcomeIndicator().getValue())));
        setOutcomeDesc(eventIdentification.getEventOutcomeDescription());

        // the two ends do not record the same event -- the Document Source exports the PHI, the Document
        // Recipient imports it -- and the ATNA record already says which, so it is taken from there
        // rather than fixed per subclass
        var eventId = eventIdentification.getEventID();
        setType(new Coding()
            .setCode(eventId.getCode())
            .setSystem(DCM_SYSTEM_NAME)
            .setDisplay(eventId.getOriginalText()));
        setAction(AuditEventAction.fromCode(eventIdentification.getEventActionCode().getValue()));

        // the patient entity is optional in this profile
        addPatientEntityIfPresent(auditMessage);

        // the submission set, which the ATNA record carries as the job the documents belong to
        auditMessage.findParticipantObjectIdentifications(
                poi -> ParticipantObjectTypeCodeRole.Job == poi.getParticipantObjectTypeCodeRole())
            .stream()
            .findFirst()
            .ifPresent(poi -> addEntity(AuditEntityType._2, ObjectRole._20)
                .setWhat(BalpAuditEventHelper.reference(poi.getParticipantObjectID())));
    }
}
