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
package org.openehealth.ipf.commons.ihe.hl7v3.iti46

import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager

/**
 * @author Dmytro Rud
 */
class Iti46AuditStrategy extends Hl7v3AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'PatientIds',
            'MessageId',
    ]


    Iti46AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }


    @Override
    void enrichDatasetFromRequest(Object request, Hl7v3AuditDataset auditDataset) {
        request = slurp(request)

        // message ID
        auditDataset.messageId = idString(request.id)

        // patient IDs
        def patientIds = [] as Set<String>
        addPatientIds(request.controlActProcess.subject[0].registrationEvent.subject1.patient.id, patientIds)
        auditDataset.patientIds = patientIds.toArray()
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        AuditorManager.hl7v3Auditor.auditIti46(
                serverSide,
                auditDataset.eventOutcomeCode,
                auditDataset.userId,
                auditDataset.userName,
                auditDataset.serviceEndpointUrl,
                auditDataset.clientIpAddress,
                auditDataset.patientIds,
                auditDataset.messageId)
    }


    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }

}
