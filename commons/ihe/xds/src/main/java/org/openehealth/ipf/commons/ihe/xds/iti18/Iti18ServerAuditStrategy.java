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
package org.openehealth.ipf.commons.ihe.xds.iti18;

import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Server audit strategy for ITI-18.
 * @author Dmytro Rud
 */
public class Iti18ServerAuditStrategy extends Iti18AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "ClientIpAddress",
        "ServiceEndpointUrl",
        "QueryUuid",
        "Payload"
        /*"PatientId"*/};

    
    /**
     * Constructs the audit strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public Iti18ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit);
    }

    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, WsAuditDataset auditDataset) {
        Iti18AuditDataset xdsAuditDataset = (Iti18AuditDataset) auditDataset;
        AuditorManager.getRegistryAuditor().auditRegistryStoredQueryEvent(
                eventOutcome,
                xdsAuditDataset.getUserId(), 
                xdsAuditDataset.getUserName(),
                xdsAuditDataset.getClientIpAddress(), 
                xdsAuditDataset.getServiceEndpointUrl(),
                xdsAuditDataset.getQueryUuid(), 
                xdsAuditDataset.getPayload(), 
                homeCommunityId,
                xdsAuditDataset.getPatientId());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
