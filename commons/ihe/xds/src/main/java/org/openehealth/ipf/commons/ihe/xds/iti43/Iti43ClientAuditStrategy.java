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

import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * Client audit strategy for ITI-43.
 * @author Dmytro Rud
 */
public class Iti43ClientAuditStrategy extends Iti43AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ServiceEndpointUrl",
        "DocumentUuids",
        "RepositoryUuids",
        "HomeCommunityUuids"};

    
    public Iti43ClientAuditStrategy(boolean allowIncompleteAudit) {
        super(false, allowIncompleteAudit);
    }

    @Override
    public void doAudit(WsAuditDataset auditDataset) {
        Iti43AuditDataset xdsAuditDataset = (Iti43AuditDataset) auditDataset;
        AuditorManager.getConsumerAuditor().auditRetrieveDocumentSetEvent(
                xdsAuditDataset.getEventOutcomeCode(),
                xdsAuditDataset.getServiceEndpointUrl(),
                xdsAuditDataset.getDocumentUuids(),
                xdsAuditDataset.getRepositoryUuids(),
                xdsAuditDataset.getHomeCommunityUuids(),
                xdsAuditDataset.getPatientId());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
