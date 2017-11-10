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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55

import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager

import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47AuditStrategy
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset

/**
 * Generic audit strategy for ITI-55 (XCPD).
 * @author Dmytro Rud
 */
class Iti55AuditStrategy extends Iti47AuditStrategy {

    Iti55AuditStrategy(boolean serverSide) {
        super(serverSide)
    }
    
    
    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)
        super.enrichAuditDatasetFromRequest(auditDataset, request, parameters)

        // query ID
        auditDataset.queryId = idString(request.controlActProcess.queryByParameter.queryId)

        // home community ID
        auditDataset.homeCommunityId = 
            request.sender.device.asAgent.representedOrganization.id.@root.text() ?: null
        auditDataset
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        AuditorManager.hl7v3Auditor.auditIti55(
                serverSide,
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.clientIpAddress,
                auditDataset.requestPayload,
                auditDataset.queryId,
                auditDataset.homeCommunityId,
                auditDataset.patientIds,
                auditDataset.purposesOfUse,
                auditDataset.userRoles)
    }


    /**
     * MCCI ACKs are of no interest for ITI-55 ATNA auditing.
     */
    @Override
    boolean isAuditableResponse(Object response) {
        return (! Iti55Utils.isMcciAck(response))
    }
}
