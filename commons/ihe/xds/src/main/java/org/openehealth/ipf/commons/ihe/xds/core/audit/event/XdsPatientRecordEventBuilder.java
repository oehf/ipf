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

package org.openehealth.ipf.commons.ihe.xds.core.audit.event;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.event.PatientRecordBuilder;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.event.PatientRecordEventBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class XdsPatientRecordEventBuilder extends PatientRecordEventBuilder<XdsPatientRecordEventBuilder> {


    public XdsPatientRecordEventBuilder(AuditContext auditContext,
                                        XdsAuditDataset auditDataset,
                                        EventActionCode action,
                                        EventType eventType,
                                        PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, action, eventType, purposesOfUse);
    }

    public XdsPatientRecordEventBuilder(AuditContext auditContext,
                                        XdsAuditDataset auditDataset,
                                        EventOutcomeIndicator eventOutcomeIndicator,
                                        String eventOutcomeDescription,
                                        EventActionCode action,
                                        EventType eventType,
                                        PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventOutcomeDescription,
                action, eventType, purposesOfUse);
    }



    public XdsPatientRecordEventBuilder addPatients(List<String> patientIds) {
        return addPatients(null, null,
                patientIds.toArray(new String[patientIds.size()]));
    }

    public XdsPatientRecordEventBuilder addObjectIds(List<String> objectIds, ParticipantObjectDataLifeCycle lifeCycle) {
        if (objectIds != null) {
            objectIds.forEach(objectId ->
                    delegate.addParticipantObjectIdentification(
                            ParticipantObjectIdType.of(
                                    "urn:ihe:iti:2017:ObjectRef",
                                    "IHE XDS Metadata",
                                    "registry object reference"),
                            null,
                            null,
                            Collections.emptyList(),
                            objectId,
                            ParticipantObjectTypeCode.System,
                            ParticipantObjectTypeCodeRole.Report,
                            lifeCycle,
                            null));
        }
        return self();
    }

    public XdsPatientRecordEventBuilder addDocumentIds(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
                                                       XdsNonconstructiveDocumentSetRequestAuditDataset.Status status,
                                                       ParticipantObjectIdType participantObjectIdType,
                                                       ParticipantObjectDataLifeCycle lifeCycle) {
        var documentIds = auditDataset.getDocumentIds(status);
        var repositoryIds = auditDataset.getRepositoryIds(status);
        IntStream.range(0, documentIds.length).forEach(i ->
                delegate.addParticipantObjectIdentification(
                        participantObjectIdType,
                        null,
                        null,
                        makeDetail(delegate, repositoryIds[i]),
                        documentIds[i],
                        ParticipantObjectTypeCode.System,
                        ParticipantObjectTypeCodeRole.Report,
                        lifeCycle,
                        null));
        return self();
    }

    private List<TypeValuePairType> makeDetail(PatientRecordBuilder builder, String repositoryId) {
        return repositoryId != null && !repositoryId.isEmpty() ?
                Collections.singletonList(delegate.getTypeValuePair(REPOSITORY_UNIQUE_ID, repositoryId)) :
                Collections.emptyList();
    }
}