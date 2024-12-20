/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2.audit.iti64;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.PatientRecordBuilder;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder;
import org.openehealth.ipf.commons.ihe.core.atna.event.PatientRecordEventBuilder;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.codes.MllpEventTypeCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle.LogicalDeletion;
import static org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle.Origination;

/**
 * ITI-64 Auditing is somewhat special, so we have a dedicated builder
 *
 * @author Christian Ohr
 */
class IHEPatientRecordChangeLinkBuilder<T extends PatientRecordEventBuilder<T>> extends IHEAuditMessageBuilder<T, PatientRecordBuilder> {

    private static final String URN_IHE_ITI_XPID_2017_PATIENT_IDENTIFIER_TYPE = "urn:ihe:iti:xpid:2017:patientIdentifierType";

    IHEPatientRecordChangeLinkBuilder(AuditContext auditContext, AuditDataset auditDataset) {
        super(auditContext, auditDataset, new PatientRecordBuilder(auditDataset.getEventOutcomeIndicator(), EventActionCode.Update, MllpEventTypeCode.XadPidLinkChange));

        // First the source, then the destination
        if (auditDataset.isServerSide()) {
            setRemoteParticipant(auditDataset);
            addHumanRequestor(auditDataset);
            setLocalParticipant(auditDataset);
        } else {
            setLocalParticipant(auditDataset);
            addHumanRequestor(auditDataset);
            setRemoteParticipant(auditDataset);
        }
    }

    public IHEPatientRecordChangeLinkBuilder setLocalPatientId(Iti64AuditDataset auditDataset) {
        var patientId = auditDataset.getLocalPatientId() != null ?
                auditDataset.getLocalPatientId() : getAuditContext().getAuditValueIfMissing();
        delegate.addPatient(patientId, null,
                Arrays.asList(
                        getTypeValuePair("MSH-10", auditDataset.getMessageControlId(), getAuditContext().getAuditValueIfMissing()),
                        getTypeValuePair(URN_IHE_ITI_XPID_2017_PATIENT_IDENTIFIER_TYPE, "localPatientId")
                ),
                // If subsumedLocalPatientId is present, then this value shall
                // equal "1" (Origination / Creation). Otherwise, this value is not
                // specialized.
                auditDataset.getSubsumedLocalPatientId() == null ? null : Origination);
        return this;
    }

    public IHEPatientRecordChangeLinkBuilder setSubsumedLocalPatientId(Iti64AuditDataset auditDataset) {
        var patientId = auditDataset.getSubsumedLocalPatientId() != null ?
                auditDataset.getSubsumedLocalPatientId() : getAuditContext().getAuditValueIfMissing();
        delegate.addPatient(patientId, null,
                Arrays.asList(
                        getTypeValuePair("MSH-10", auditDataset.getMessageControlId(), getAuditContext().getAuditValueIfMissing()),
                        getTypeValuePair(URN_IHE_ITI_XPID_2017_PATIENT_IDENTIFIER_TYPE, "subsumedPatientId")
                ),
                LogicalDeletion);
        return this;
    }

    public IHEPatientRecordChangeLinkBuilder setNewPatientId(Iti64AuditDataset auditDataset) {
        var patientId = auditDataset.getNewPatientId() != null ?
                auditDataset.getNewPatientId() : getAuditContext().getAuditValueIfMissing();
        delegate.addPatient(patientId, null,
                Arrays.asList(
                        getTypeValuePair("MSH-10", auditDataset.getMessageControlId(), getAuditContext().getAuditValueIfMissing()),
                        getTypeValuePair(URN_IHE_ITI_XPID_2017_PATIENT_IDENTIFIER_TYPE, "newPatientId")
                ),
                // If newPatientId and previousPatientId are not equal, then this
                // value shall equal "14" (Logical deletion). Otherwise, this value
                // is not specialized.
                Objects.equals(auditDataset.getNewPatientId(), auditDataset.getPreviousPatientId()) ? null : Origination);
        return this;
    }

    public IHEPatientRecordChangeLinkBuilder setPreviousPatientId(Iti64AuditDataset auditDataset) {
        var patientId = auditDataset.getPreviousPatientId() != null ?
                auditDataset.getPreviousPatientId() : getAuditContext().getAuditValueIfMissing();
        delegate.addPatient(patientId, null,
                Arrays.asList(
                        getTypeValuePair("MSH-10", auditDataset.getMessageControlId(), getAuditContext().getAuditValueIfMissing()),
                        getTypeValuePair(URN_IHE_ITI_XPID_2017_PATIENT_IDENTIFIER_TYPE, "previousPatientId")
                ),
                // If newPatientId and previousPatientId are not equal, then this
                // value shall equal "14" (Logical deletion). Otherwise, this value
                // is not specialized.
                Objects.equals(auditDataset.getNewPatientId(), auditDataset.getPreviousPatientId()) ? null : LogicalDeletion);
        return this;
    }

    public IHEPatientRecordChangeLinkBuilder setSubmissionSet(Iti64AuditDataset auditDataset) {
        var submissionSetUuid = auditDataset.getSubmissionSetUuid() != null ?
                auditDataset.getSubmissionSetUuid() : getAuditContext().getAuditValueIfMissing();
        if (auditDataset.getSubmissionSetUuid() != null) {
            delegate.addParticipantObjectIdentification(
                    ParticipantObjectIdTypeCode.XdsMetadata,
                    null,
                    null,
                    Collections.emptyList(),
                    submissionSetUuid,
                    ParticipantObjectTypeCode.System,
                    ParticipantObjectTypeCodeRole.Job,
                    null,
                    null);
        }
        return this;
    }

}
