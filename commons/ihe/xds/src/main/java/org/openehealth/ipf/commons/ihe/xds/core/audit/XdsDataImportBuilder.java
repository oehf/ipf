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
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEDataImportBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsBuilderUtils.makeDocumentDetail;

/**
 * @author Christian Ohr
 */
public class XdsDataImportBuilder extends IHEDataImportBuilder<XdsDataImportBuilder> {


    public XdsDataImportBuilder(AuditContext auditContext, XdsAuditDataset auditDataset, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        this(auditContext, auditDataset, EventActionCode.Create, eventType, purposesOfUse);
    }

    public XdsDataImportBuilder(AuditContext auditContext, XdsAuditDataset auditDataset, EventActionCode eventActionCode, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(auditContext, auditDataset, eventActionCode, eventType, purposesOfUse);
    }

    public XdsDataImportBuilder(AuditContext auditContext, AuditDataset auditDataset, EventOutcomeIndicator eventOutcomeIndicator, EventActionCode eventActionCode, EventType eventType, List<PurposeOfUse> purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventActionCode, eventType, purposesOfUse);
    }

    public XdsDataImportBuilder setSubmissionSet(XdsSubmitAuditDataset auditDataset) {
        return addImportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                Collections.emptyList());
    }

    public XdsDataImportBuilder setSubmissionSetWithHomeCommunityId(XdsSubmitAuditDataset auditDataset) {
        return addImportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                makeDocumentDetail(null, auditDataset.getHomeCommunityId(), null, null));
    }

    public XdsDataImportBuilder addDocumentIds(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
                                               XdsNonconstructiveDocumentSetRequestAuditDataset.Status status) {
        String[] documentIds = auditDataset.getDocumentIds(status);
        String[] homeCommunityIds = auditDataset.getHomeCommunityIds(status);
        String[] repositoryIds = auditDataset.getRepositoryIds(status);
        String[] seriesInstanceIds = auditDataset.getSeriesInstanceIds(status);
        String[] studyInstanceIds = auditDataset.getStudyInstanceIds(status);
        IntStream.range(0, documentIds.length).forEach(i ->
                addImportedEntity(
                        documentIds[i],
                        ParticipantObjectIdTypeCode.ReportNumber,
                        ParticipantObjectTypeCodeRole.Report,
                        makeDocumentDetail(repositoryIds[i], homeCommunityIds[i], seriesInstanceIds[i], studyInstanceIds[i])));
        return self();
    }

}
