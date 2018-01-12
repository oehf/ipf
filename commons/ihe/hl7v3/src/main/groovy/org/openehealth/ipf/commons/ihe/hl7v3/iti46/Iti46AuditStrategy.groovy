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

import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.PatientRecordEventBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3EventTypeCode

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString

/**
 * @author Dmytro Rud
 */
class Iti46AuditStrategy extends Hl7v3AuditStrategy {

    Iti46AuditStrategy(boolean serverSide) {
        super(serverSide)
    }


    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)

        // message ID
        auditDataset.messageId = idString(request.id)

        // patient IDs
        Set<String> patientIds = [] as Set<String>
        addPatientIds(request.controlActProcess.subject[0].registrationEvent.subject1.patient.id, patientIds)
        auditDataset.setPatientIds(patientIds as String[])
        auditDataset
    }

    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        new PatientRecordEventBuilder(
                auditContext,
                auditDataset,
                isServerSide() ? EventActionCode.Update : EventActionCode.Read,
                Hl7v3EventTypeCode.PIXUpdateNotification,
                auditDataset.purposesOfUse)
        // Type=II (the literal string), Value=the value of MSH-10 (from the message content, base64 encoded)
                .addPatients('II', auditDataset.messageId, auditDataset.getPatientIds())
                .getMessages()
    }


}
