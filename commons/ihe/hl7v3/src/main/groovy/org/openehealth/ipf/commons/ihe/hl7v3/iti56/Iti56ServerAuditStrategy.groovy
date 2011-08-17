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
package org.openehealth.ipf.commons.ihe.hl7v3.iti56;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * Server-side audit strategy for ITI-56 (XCPD).
 * @author Dmytro Rud
 */
class Iti56ServerAuditStrategy extends Iti56AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'RequestPayload',
            'PatientId',
            'ClientIpAddress'
    ]
    
    
    Iti56ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit)
    }

    
    @Override
    void doAudit(WsAuditDataset auditDataset) throws Exception {
        AuditorManager.getXCPDRespondingGatewayAuditor().auditXCPDPatientLocationQueryEvent(
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.clientIpAddress,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.requestPayload,
                auditDataset.patientId)
    }

    
    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }
}
