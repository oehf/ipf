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
package org.openehealth.ipf.commons.ihe.xcpd;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55AuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils

/**
 * Server-side audit strategy for ITI-55 (XCPD).
 * @author Dmytro Rud
 */
class Iti55ServerAuditStrategy extends Iti55AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
        'UserId', 'ServiceEndpointUrl', 'Payload', 
        'QueryId', 'HomeCommunityId', 'ClientIpAddress'            
    ]
    
    
    Iti55ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit)
    }

    
    @Override
    void doAudit(
            RFC3881EventOutcomeCodes eventOutcomeCode, 
            WsAuditDataset auditDataset) throws Exception 
    {
        AuditorManager.getXCPDRespondingGatewayAuditor().auditXCPDPatientDiscoveryEvent(
                auditDataset.outcomeCode,
                auditDataset.userId,
                auditDataset.clientIpAddress,
                auditDataset.serviceEndpointUrl,
                auditDataset.payload,
                auditDataset.queryId,
                auditDataset.homeCommunityId,
                auditDataset.patientIds)
    }

    
    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }
}
