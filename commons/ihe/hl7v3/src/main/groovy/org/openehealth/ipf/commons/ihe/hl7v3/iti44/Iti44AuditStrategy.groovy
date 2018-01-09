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
package org.openehealth.ipf.commons.ihe.hl7v3.iti44

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.AuditException
import org.openehealth.ipf.commons.audit.codes.EventActionCode
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEPatientRecordBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3EventTypeCode

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.iiToCx

/**
 * @author Dmytro Rud
 */
class Iti44AuditStrategy extends Hl7v3AuditStrategy {

    Iti44AuditStrategy(boolean serverSide) {
        super(serverSide)
    }


    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)
        GPathResult regEvent = request.controlActProcess.subject[0].registrationEvent

        // request type
        auditDataset.requestType = request.name()

        // message ID
        auditDataset.messageId = idString(request.id)

        // (actual) patient IDs --
        // potentially multiple for Add/Revise, single for Merge
        Set<String> patientIds = [] as Set<String>
        addPatientIds(regEvent.subject1.patient.id, patientIds)
        auditDataset.patientIds = patientIds as String[]

        // obsolete (merged) patient ID
        auditDataset.oldPatientId = iiToCx(regEvent.replacementOf[0].priorRegistration.subject1.priorRegisteredRole.id[0]) ?: null
        auditDataset
    }


    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        switch (auditDataset.requestType) {
            case 'PRPA_IN201301UV02':
                return [
                        patientRecordAuditMessage(auditContext,auditDataset, EventActionCode.Create, true)
                ] as AuditMessage[]
            case 'PRPA_IN201302UV02':
                return [
                        patientRecordAuditMessage(auditContext,auditDataset, EventActionCode.Update, true)
                ] as AuditMessage[]
            case 'PRPA_IN201304UV02':
                return [
                        patientRecordAuditMessage(auditContext,auditDataset, EventActionCode.Delete, false),
                        patientRecordAuditMessage(auditContext,auditDataset, EventActionCode.Update, true)
                ] as AuditMessage[]
            default:
                throw new AuditException("Cannot create audit message for event " + auditDataset.requestType)
        }
    }

    protected AuditMessage patientRecordAuditMessage(AuditContext auditContext,
                                                     final Hl7v3AuditDataset auditDataset,
                                                     EventActionCode eventActionCode,
                                                     boolean newPatientId) {
        String[] patientIds = newPatientId ? auditDataset.patientIds : [ auditDataset.oldPatientId ] as String[]
        return new IHEPatientRecordBuilder<>(auditContext, auditDataset, eventActionCode, Hl7v3EventTypeCode.PatientIdentityFeed, auditDataset.purposesOfUse)

        // Type=II (the literal string), Value=the value of the message ID (from the message content, base64 encoded)
                .addPatients("II", auditDataset.messageId, patientIds)
                .getMessage()
    }


}
