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
package org.openehealth.ipf.commons.ihe.hl7v3.iti47;

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.render

/**
 * @author Dmytro Rud
 */
class Iti47AuditStrategy extends Hl7v3AuditStrategy {

    Iti47AuditStrategy(boolean serverSide) {
        super(serverSide)
    }
    

    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)
        GPathResult qbp = request.controlActProcess.queryByParameter

        // patient IDs from request
        def patientIds = [] as Set<String>
        addPatientIds(qbp.parameterList.livingSubjectId.value, patientIds)
        auditDataset.patientIds = patientIds.toArray() ?: null

        // dump of the "queryByParameter" element
        auditDataset.requestPayload = render(qbp)
        auditDataset
    }


    @Override
    boolean enrichAuditDatasetFromResponse(Hl7v3AuditDataset auditDataset, Object response) {
        response = slurp(response)
        boolean result = super.enrichAuditDatasetFromResponse(auditDataset, response)
        
        // patient IDs from response
        def patientIds = [] as Set<String>
        addPatientIds(response.controlActProcess.subject.registrationEvent.subject1.patient.id, patientIds)
        if (auditDataset.patientIds) {
            patientIds.addAll(auditDataset.patientIds)
        }
        auditDataset.patientIds = patientIds.toArray() ?: null
        result
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        AuditorManager.hl7v3Auditor.auditIti47(
                serverSide,
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.clientIpAddress,
                auditDataset.requestPayload,
                auditDataset.patientIds,
                auditDataset.purposesOfUse,
                auditDataset.userRoles)
    }

}
