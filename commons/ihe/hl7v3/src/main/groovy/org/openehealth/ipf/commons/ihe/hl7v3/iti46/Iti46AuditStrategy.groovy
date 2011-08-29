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

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp
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
    void enrichDatasetFromRequest(Object request, WsAuditDataset auditDataset0) {
        Hl7v3AuditDataset auditDataset = (Hl7v3AuditDataset) auditDataset0
        GPathResult xml = slurp((String) request)

        // message ID
        auditDataset.messageId = idString(xml.id)

        // patient IDs
        def patientIds = [] as Set<String>
        addPatientIds(xml.controlActProcess.subject[0].registrationEvent.subject1.patient.id, patientIds)
        auditDataset.patientIds = patientIds.toArray()
    }


    @Override
    void doAudit(WsAuditDataset auditDataset) {
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
