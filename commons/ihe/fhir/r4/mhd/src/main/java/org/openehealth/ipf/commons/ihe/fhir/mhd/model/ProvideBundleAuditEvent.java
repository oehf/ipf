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
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEvent;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEventHelper;

import java.util.Date;

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
abstract class ProvideBundleAuditEvent extends BalpAuditEvent implements SelfInitializing {

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
     * @return the role the audit source plays in the transaction, used to name an observer when the
     *      audit context does not.
     */
    protected abstract ActiveParticipantRoleIdCode localRole();

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
     * Fills in everything the profile requires and the audit message provides: the time the event was
     * recorded, the outcome, the agents, the audit source, the patient and the SubmissionSet.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    @Override
    public void initialize(AuditMessage auditMessage) {
        var eventIdentification = auditMessage.getEventIdentification();
        setRecorded(Date.from(eventIdentification.getEventDateTime()));
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

        setClientAndServer(auditMessage);
        addUserAgents(auditMessage);
        setAuditSource(auditMessage, localRole());
        BalpAuditEventHelper.patientReference(auditMessage).ifPresent(this::addPatientEntity);
        BalpAuditEventHelper.requestId(auditMessage).ifPresent(this::addTransactionEntity);

        // the submission set, which the ATNA record carries as the job the documents belong to
        auditMessage.findParticipantObjectIdentifications(
                poi -> ParticipantObjectTypeCodeRole.Job == poi.getParticipantObjectTypeCodeRole())
            .stream()
            .findFirst()
            .ifPresent(poi -> addEntity(AuditEntityType._2, ObjectRole._20)
                .setWhat(BalpAuditEventHelper.reference(poi.getParticipantObjectID())));
    }
}
