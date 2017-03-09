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
package org.openehealth.ipf.commons.ihe.xds.iti39;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsRetrieveAuditStrategy30;

/**
 * Client audit strategy for ITI-39.
 * @author Dmytro Rud
 */
public class Iti39ClientAuditStrategy extends XdsRetrieveAuditStrategy30 {

    public Iti39ClientAuditStrategy() {
        super(false);
    }

    @Override
    public void doAudit(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset) {
        for (Status status : Status.values()) {
            if (auditDataset.hasDocuments(status)) {
                AuditorManager.getXCAInitiatingGatewayAuditor().auditCrossGatewayRetrieveEvent(
                        auditDataset.getEventOutcomeCode(status),
                        auditDataset.getServiceEndpointUrl(),
                        auditDataset.getUserId(),
                        auditDataset.getUserName(),
                        auditDataset.getDocumentIds(status),
                        auditDataset.getRepositoryIds(status),
                        auditDataset.getHomeCommunityIds(status),
                        auditDataset.getPurposesOfUse());
            }
        }
    }

}
