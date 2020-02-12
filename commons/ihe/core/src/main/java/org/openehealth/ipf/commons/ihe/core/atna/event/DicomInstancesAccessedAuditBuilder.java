/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.core.atna.event;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.DicomInstancesAccessedBuilder;
import org.openehealth.ipf.commons.audit.model.DicomObjectDescriptionType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

/**
 * Builder for building IHE-specific event if SOP Instances from a specific study are created, modified or accessed. It
 * automatically sets the AuditSource, local and remote ActiveParticipant and a Human Requestor and provides methods for
 * adding patient IDs.
 *
 * @author Christian Ohr
 * @author Eugen Fischer
 * @since 3.5
 */
public class DicomInstancesAccessedAuditBuilder<T extends DicomInstancesAccessedAuditBuilder<T>>
        extends IHEAuditMessageBuilder<T, DicomInstancesAccessedBuilder> {

    public DicomInstancesAccessedAuditBuilder(final AuditContext auditContext,
            final AuditDataset auditDataset,
            final EventOutcomeIndicator eventOutcomeIndicator,
            final String eventOutcomeDescription,
            final EventActionCode eventActionCode,
            final EventType eventType,
            final PurposeOfUse... purposesOfUse) {
        super(auditContext, new DicomInstancesAccessedBuilder(
                eventOutcomeIndicator,
                eventOutcomeDescription,
                eventActionCode,
                eventType,
                purposesOfUse));

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

    public T setPatient(final String patientId) {
        if (patientId != null) {
            delegate.addPatientParticipantObject(
                    patientId,
                    null,
                    Collections.emptyList(),
                    null);
        }
        return self();
    }

    public T addExportedEntity(
            final String objectId,
            final ParticipantObjectIdType participantObjectIdType,
            final ParticipantObjectTypeCodeRole participantObjectTypeCodeRole,
            final List<TypeValuePairType> details) {
        return addExportedEntity(
                objectId,
                participantObjectIdType,
                ParticipantObjectTypeCode.System,
                participantObjectTypeCodeRole,
                null,
                details);
    }

    public T addExportedEntity(
            final String objectId,
            final ParticipantObjectIdType participantObjectIdType,
            final ParticipantObjectTypeCode participantObjectTypeCode,
            final ParticipantObjectTypeCodeRole participantObjectTypeCodeRole,
            final ParticipantObjectDataLifeCycle participantObjectDataLifeCycle,
            final List<TypeValuePairType> details) {
        delegate.addParticipantObjectIdentification(
                requireNonNull(participantObjectIdType, "Exported entity ID type must not be null"),
                null,
                null,
                details,
                objectId != null ? objectId : getAuditContext().getAuditValueIfMissing(),
                participantObjectTypeCode,
                participantObjectTypeCodeRole,
                participantObjectDataLifeCycle,
                null);
        return self();
    }

    public T addTransferredStudyParticipantObject(final String studyId, final List<TypeValuePairType> objectDetails) {
        delegate.addStudyParticipantObject(studyId, objectDetails);
        return self();
    }

    @Override
    public void validate() {
        super.validate();
    }
}
