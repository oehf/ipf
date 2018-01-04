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

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.DataImportBuilder;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.Collections;
import java.util.List;

/**
 * @author Christian Ohr
 */
public class IHEDataImportBuilder<T extends IHEDataImportBuilder<T>> extends IHEAuditMessageBuilder<T, DataImportBuilder> {

    public IHEDataImportBuilder(AuditDataset auditDataset, EventType eventType) {
        this(auditDataset, eventType, Collections.emptyList());
    }

    public IHEDataImportBuilder(AuditDataset auditDataset, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        this(auditDataset, EventActionCode.Create, eventType, purposesOfUse);
    }

    public IHEDataImportBuilder(AuditDataset auditDataset, EventActionCode eventActionCode, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(new DataImportBuilder(
                auditDataset.getEventOutcomeIndicator(),
                eventActionCode,
                eventType,
                purposesOfUse.toArray(new PurposeOfUse[purposesOfUse.size()])));
        setAuditSource(auditDataset);

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

    public T setPatient(String patientId) {
        if (patientId != null) {
            delegate.addPatientParticipantObject(patientId, null, null, null);
        }
        return self();
    }

    public T addImportedEntity(
            String objectId,
            ParticipantObjectIdType participantObjectIdType,
            ParticipantObjectTypeCodeRole participantObjectTypeCodeRole,
            List<TypeValuePairType> details) {
        delegate.addParticipantObjectIdentification(
                participantObjectIdType,
                null,
                null,
                details,
                objectId,
                ParticipantObjectTypeCode.System,
                participantObjectTypeCodeRole,
                null,
                null);
        return self();
    }

    @Override
    public void validate() {
        super.validate();
    }
}
