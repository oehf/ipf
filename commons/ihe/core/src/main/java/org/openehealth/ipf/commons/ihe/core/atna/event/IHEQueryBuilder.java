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

package org.openehealth.ipf.commons.ihe.core.atna.event;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.QueryBuilder;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Christian Ohr
 */
public class IHEQueryBuilder<T extends IHEQueryBuilder<T>> extends IHEAuditMessageBuilder<T, QueryBuilder> {

    public IHEQueryBuilder(AuditContext auditContext, AuditDataset auditDataset, EventType eventType) {
        this(auditContext, auditDataset, eventType, Collections.emptyList());
    }

    public IHEQueryBuilder(AuditContext auditContext, AuditDataset auditDataset, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(auditContext, new QueryBuilder(
                auditDataset.getEventOutcomeIndicator(),
                eventType,
                purposesOfUse.toArray(new PurposeOfUse[purposesOfUse.size()])));

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

    public T addPatients(String... patientIds) {
        if (patientIds != null) {
            Arrays.stream(patientIds)
                    .filter(Objects::nonNull)
                    .forEach(patientId -> delegate.addPatientParticipantObject(patientId, null, null, null));
        }
        return self();
    }

    public T addPatients(Collection<String> patientIds) {
        if (patientIds != null) {
            patientIds.stream()
                    .filter(Objects::nonNull)
                    .forEach(patientId ->
                            delegate.addPatientParticipantObject(patientId, null, null, null));
        }
        return self();
    }

    public T setQueryParameters(
            String queryMessageIdentifier,
            ParticipantObjectIdType participantObjectIdType,
            String queryMessage) {
        return setQueryParameters(
                queryMessageIdentifier,
                participantObjectIdType,
                queryMessage,
                null,
                null);
    }

    public T setQueryParameters(
            String queryMessageIdentifier,
            ParticipantObjectIdType participantObjectIdType,
            String queryMessage,
            String messageIdDesignator,
            String messageId) {
        return setQueryParameters(
                queryMessageIdentifier,
                participantObjectIdType,
                queryMessage,
                messageIdDesignator != null ?
                        Collections.singletonList(getTypeValuePair(messageIdDesignator, messageId)) :
                        Collections.emptyList());
    }

    public T setQueryParameters(
            String queryMessageIdentifier,
            ParticipantObjectIdType participantObjectIdType,
            String queryMessage,
            List<TypeValuePairType> details) {
        delegate.addParticipantObjectIdentification(
                participantObjectIdType,
                null,
                queryMessage.getBytes(StandardCharsets.UTF_8),
                details,
                queryMessageIdentifier,
                ParticipantObjectTypeCode.System,
                ParticipantObjectTypeCodeRole.Query,
                null,
                null);
        return self();
    }

    @Override
    public void validate() {
        super.validate();
    }
}
