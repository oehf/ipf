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
package org.openehealth.ipf.commons.ihe.xds;

import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Server audit strategy for ITI-16.
 * @author Dmytro Rud
 */
class Iti16ServerAuditStrategy extends Iti16AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "ClientIpAddress",
        "ServiceEndpointUrl",
        "Payload"
        /*"PatientId"*/};
    
    Iti16ServerAuditStrategy(boolean allowIncompleteAudit) {
        super(true, allowIncompleteAudit);
    }
    
    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, XdsAuditDataset auditDataset) {
        AuditorManager.getRegistryAuditor().auditRegistryQueryEvent(
                eventOutcome,
                auditDataset.getClientIpAddress(), // Must be set to something, otherwise schema is broken
                auditDataset.getUserName(),
                auditDataset.getClientIpAddress(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getPayload(),
                /*auditDataset.getPatientId()*/ null);
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
