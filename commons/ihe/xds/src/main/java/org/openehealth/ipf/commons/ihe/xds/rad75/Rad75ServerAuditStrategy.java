/*
 * Copyright 2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.rad75;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69AuditDataset;
import org.openehealth.ipf.commons.ihe.xds.rad69.Rad69AuditStrategy;

/**
 * Server audit strategy for RAD-75.
 * @author Clay Sebourn
 */
public class Rad75ServerAuditStrategy extends Rad69AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ClientIpAddress",
        "ServiceEndpointUrl",
        "DocumentUuids",
        "RepositoryUuids",
        "HomeCommunityUuids"};

    public Rad75ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit);
    }

    @Override
    public void doAudit(Rad69AuditDataset auditDataset) {
        String[] homeCommunityIds = auditDataset.getHomeCommunityUuids();
        AuditorManager.getCustomRadAuditor().auditCrossGatewayRetrieveImagingDocumentSetEvent(
            auditDataset.getEventOutcomeCode(),
            auditDataset.getUserId(),
            auditDataset.getUserName(),
            auditDataset.getClientIpAddress(),
            auditDataset.getServiceEndpointUrl(),
            auditDataset.getStudyInstanceUuids(),
            auditDataset.getSeriesInstanceUuids(),
            auditDataset.getDocumentUuids(),
            auditDataset.getRepositoryUuids(),
            auditDataset.getHomeCommunityUuids() );
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
