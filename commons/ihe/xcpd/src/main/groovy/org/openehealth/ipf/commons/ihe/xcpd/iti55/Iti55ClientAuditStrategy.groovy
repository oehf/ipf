/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xcpd.iti55;

import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * Client-side audit strategy for ITI-55 (XCPD).
 * @author Dmytro Rud
 */
class Iti55ClientAuditStrategy extends Iti55AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'RequestPayload',
            'QueryId',
            'HomeCommunityId',
    ]

    
    Iti55ClientAuditStrategy(boolean allowIncompleteAudit) {
        super(false, allowIncompleteAudit)
    }
    
    
    @Override
    void doAudit(WsAuditDataset auditDataset) throws Exception {
        AuditorManager.getXCPDInitiatingGatewayAuditor().auditXCPDPatientDiscoveryQueryEvent(
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.serviceEndpointUrl,
                auditDataset.requestPayload,
                auditDataset.queryId,
                auditDataset.homeCommunityId,
                auditDataset.patientIds)
    }

    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }
}
