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

package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEPatientRecordBuilder;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Christian Ohr
 */
public class XdsPatientRecordBuilder extends IHEPatientRecordBuilder<XdsPatientRecordBuilder> {

    public XdsPatientRecordBuilder(AuditContext auditContext, XdsAuditDataset auditDataset, EventActionCode action, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(auditContext, auditDataset, action, eventType, purposesOfUse);
    }

    public XdsPatientRecordBuilder addPatients(List<String> patientIds) {
        return addPatients(null, null,
                patientIds.toArray(new String[patientIds.size()]));
    }

    public XdsPatientRecordBuilder addObjectIds(String... objectIds) {
        if (objectIds != null) {
            Stream.of(objectIds).forEach(objectId ->
                    delegate.addParticipantObjectIdentification(
                            ParticipantObjectIdType.of(
                                    "urn:ihe:iti:2017:ObjectRef",
                                    "IHE XDS Metadata",
                                    "registry object reference"),
                            null,
                            null,
                            null,
                            objectId,
                            ParticipantObjectTypeCode.System,
                            ParticipantObjectTypeCodeRole.Report,
                            null,
                            null));
        }
        return self();
    }

    public XdsPatientRecordBuilder addDocumentIds(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
                                                  XdsNonconstructiveDocumentSetRequestAuditDataset.Status status,
                                                  ParticipantObjectIdType participantObjectIdType,
                                                  ParticipantObjectDataLifeCycle lifeCycle) {
        String[] documentIds = auditDataset.getDocumentIds(status);
        String[] repositoryIds = auditDataset.getRepositoryIds(status);
        IntStream.range(0, documentIds.length).forEach(i ->
                delegate.addParticipantObjectIdentification(
                        participantObjectIdType,
                        null,
                        null,
                        makeDetail(repositoryIds[i]),
                        documentIds[i],
                        ParticipantObjectTypeCode.System,
                        ParticipantObjectTypeCodeRole.Report,
                        lifeCycle,
                        null));
        return self();
    }

    private List<TypeValuePairType> makeDetail(String repositoryId) {
        List<TypeValuePairType> tvp = new LinkedList<>();
        tvp.add(new TypeValuePairType(XdsAuditStrategy.REPOSITORY_UNIQUE_ID, repositoryId.getBytes(StandardCharsets.UTF_8)));
        return tvp;
    }
}