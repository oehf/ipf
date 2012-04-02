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
package org.openehealth.ipf.commons.ihe.hl7v3.pcc1;

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.render
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString

/**
 * @author Dmytro Rud
 */
class Pcc1AuditStrategy extends Hl7v3AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'PatientIds',
            'RequestPayload',
            'QueryId',
    ]


    Pcc1AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }
    

    @Override
    void enrichDatasetFromRequest(Object request, Hl7v3AuditDataset auditDataset) {
        request = slurp(request)
        GPathResult qbp = request.controlActProcess.queryByParameter

        // query ID
        auditDataset.queryId = idString(request.controlActProcess.queryByParameter.queryId)

        // patient IDs from request
        def patientIds = [] as Set<String>
        addPatientIds(qbp.parameterList.patientId.value, patientIds)
        auditDataset.patientIds = patientIds.toArray() ?: null

        // dump of the "queryByParameter" element
        auditDataset.requestPayload = render(qbp)
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        AuditorManager.hl7v3Auditor.auditPcc1(
                serverSide,
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.clientIpAddress,
                auditDataset.requestPayload,
                auditDataset.queryId,
                auditDataset.patientIds
        )
    }


    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }

}
