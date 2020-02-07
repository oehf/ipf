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

import java.util.Collections;
import java.util.stream.IntStream;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.DicomInstancesTransferredAuditBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

/**
 * @author Christian Ohr
 * @quthor Eugen Fischer
 * @since 3.5
 */
public class DicomInstancesTransferredEventBuilder extends
        DicomInstancesTransferredAuditBuilder<DicomInstancesTransferredEventBuilder> {

    public DicomInstancesTransferredEventBuilder(final AuditContext auditContext,
            final AuditDataset auditDataset,
            final EventOutcomeIndicator eventOutcomeIndicator,
            final String eventOutcomeDescription,
            final EventActionCode eventActionCode,
            final EventType eventType,
            final PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventOutcomeDescription, eventActionCode, eventType,
                purposesOfUse);
    }

    public DicomInstancesTransferredEventBuilder setSubmissionSet(final XdsSubmitAuditDataset auditDataset) {
        return addExportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                Collections.emptyList());
    }

    public DicomInstancesTransferredEventBuilder setSubmissionSetWithHomeCommunityId(final XdsSubmitAuditDataset auditDataset,
            final boolean xcaHomeCommunityId) {
        return addExportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                documentDetails(null, auditDataset.getHomeCommunityId(), null, null, xcaHomeCommunityId));
    }

    public DicomInstancesTransferredEventBuilder addDocumentIds(final XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
            final XdsNonconstructiveDocumentSetRequestAuditDataset.Status status,
            final boolean xcaHomeCommunityId) {
        final String[] documentIds = auditDataset.getDocumentIds(status);
        final String[] homeCommunityIds = auditDataset.getHomeCommunityIds(status);
        final String[] repositoryIds = auditDataset.getRepositoryIds(status);
        final String[] seriesInstanceIds = auditDataset.getSeriesInstanceIds(status);
        final String[] studyInstanceIds = auditDataset.getStudyInstanceIds(status);
        IntStream.range(0, documentIds.length).forEach(i ->
                addExportedEntity(
                        documentIds[i],
                        ParticipantObjectIdTypeCode.ReportNumber,
                        ParticipantObjectTypeCodeRole.Report,
                        documentDetails(repositoryIds[i], homeCommunityIds[i], seriesInstanceIds[i],
                                studyInstanceIds[i], xcaHomeCommunityId)));
        return self();
    }

}
