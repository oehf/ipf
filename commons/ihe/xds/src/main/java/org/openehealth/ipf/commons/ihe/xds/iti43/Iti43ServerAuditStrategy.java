/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti43;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditStrategy30;

/**
 * Server audit strategy for ITI-43.
 * @author Dmytro Rud
 */
public class Iti43ServerAuditStrategy extends XdsRetrieveAuditStrategy30 {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ClientIpAddress",
        "ServiceEndpointUrl",
        "DocumentUniqueIds",
        "RepositoryUniqueIds"};

    
    public Iti43ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit);
    }

    @Override
    public void doAudit(XdsRetrieveAuditDataset auditDataset) {
        AuditorManager.getRepositoryAuditor().auditRetrieveDocumentSetEvent(
                auditDataset.getEventOutcomeCode(),
                auditDataset.getUserId(),
                auditDataset.getUserName(),
                auditDataset.getClientIpAddress(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getDocumentUniqueIds(),
                auditDataset.getRepositoryUniqueIds(),
                auditDataset.getHomeCommunityIds());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
