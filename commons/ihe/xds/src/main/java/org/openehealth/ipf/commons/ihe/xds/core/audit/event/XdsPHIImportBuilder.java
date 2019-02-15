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
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.core.atna.event.PHIImportBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class XdsPHIImportBuilder extends PHIImportBuilder<XdsPHIImportBuilder> {


    public XdsPHIImportBuilder(AuditContext auditContext,
                               XdsAuditDataset auditDataset,
                               EventType eventType,
                               PurposeOfUse... purposesOfUse) {
        this(auditContext, auditDataset, EventActionCode.Create, eventType, purposesOfUse);
    }

    public XdsPHIImportBuilder(AuditContext auditContext,
                               XdsAuditDataset auditDataset,
                               EventActionCode eventActionCode,
                               EventType eventType,
                               PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventActionCode, eventType, purposesOfUse);
    }

    public XdsPHIImportBuilder(AuditContext auditContext,
                               AuditDataset auditDataset,
                               EventOutcomeIndicator eventOutcomeIndicator,
                               String eventOutcomeDescription,
                               EventActionCode eventActionCode,
                               EventType eventType,
                               PurposeOfUse... purposesOfUse) {
        super(auditContext, auditDataset, eventOutcomeIndicator, eventOutcomeDescription,
                eventActionCode, eventType, purposesOfUse);
    }



    public XdsPHIImportBuilder setSubmissionSet(XdsSubmitAuditDataset auditDataset) {
        return addImportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                Collections.emptyList());
    }

    public XdsPHIImportBuilder setSubmissionSetWithHomeCommunityId(XdsSubmitAuditDataset auditDataset, boolean xcaHomeCommunityId) {
        return addImportedEntity(auditDataset.getSubmissionSetUuid(),
                ParticipantObjectIdTypeCode.XdsMetadata,
                ParticipantObjectTypeCodeRole.Job,
                documentDetails(null, auditDataset.getHomeCommunityId(), null, null, xcaHomeCommunityId));
    }

    public XdsPHIImportBuilder addDocumentIds(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset,
                                              XdsNonconstructiveDocumentSetRequestAuditDataset.Status status,
                                              boolean xcaHomeCommunityId) {
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
                        documentDetails(repositoryIds[i], homeCommunityIds[i], seriesInstanceIds[i],
                                studyInstanceIds[i], xcaHomeCommunityId)));
        return self();
    }

    /**
     * @return "missing". ITI-43 makes the remote alt user ID mandatory and so does the EVS validator
     */
    @Override
    protected String getRemoteAltUserId() {
        return getAuditContext().getAuditValueIfMissing();
    }
}
