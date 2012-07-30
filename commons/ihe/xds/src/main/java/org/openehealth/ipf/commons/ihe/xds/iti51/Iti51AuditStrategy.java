/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.iti51;

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditStrategy30;

/**
 * Base audit strategy for ITI-51.
 * @author Dmytro Rud
 * @author Michael Ottati
 */
public class Iti51AuditStrategy extends XdsQueryAuditStrategy30 {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "EventOutcomeCode",
        "ServiceEndpointUrl",
        "QueryUuid",
        "RequestPayload"
        /*"PatientId"*/};


    /**
     * Constructs the audit strategy.
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public Iti51AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }


    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }


    private void doAudit(XdsQueryAuditDataset auditDataset, String patientId) {
        AuditorManager.getCustomXdsAuditor().auditIti51(
                isServerSide(),
                auditDataset.getEventOutcomeCode(),
                auditDataset.getUserId(),
                auditDataset.getUserName(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getQueryUuid(),
                auditDataset.getRequestPayload(),
                auditDataset.getHomeCommunityId(),
                patientId);
    }


    @Override
    public void doAudit(XdsQueryAuditDataset auditDataset) throws Exception {
        if (auditDataset.getPatientIds().isEmpty()) {
            // when no patient IDs were present in the request,
            // then we create one ATNA audit record without ID
            doAudit(auditDataset, null);
        } else {
            for (String patientId : auditDataset.getPatientIds()) {
                doAudit(auditDataset, patientId);
            }
        }
    }
}
