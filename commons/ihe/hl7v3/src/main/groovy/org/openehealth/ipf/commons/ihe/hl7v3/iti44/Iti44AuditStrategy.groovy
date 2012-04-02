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
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.iiToCx

/**
 * @author Dmytro Rud
 */
class Iti44AuditStrategy extends Hl7v3AuditStrategy {

    private static final String[] NECESSARY_FIELD_NAMES = [
            'EventOutcomeCode',
            'UserId',
            'ServiceEndpointUrl',
            'PatientIds',
    ]


    Iti44AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }


    @Override
    void enrichDatasetFromRequest(Object request, Hl7v3AuditDataset auditDataset) {
        request = slurp(request)
        GPathResult regEvent = request.controlActProcess.subject[0].registrationEvent

        // request type
        auditDataset.requestType = request.name()

        // message ID
        auditDataset.messageId = idString(request.id)

        // (actual) patient IDs --
        // potentially multiple for Add/Revise, single for Merge
        def patientIds = [] as Set<String>
        addPatientIds(regEvent.subject1.patient.id, patientIds)
        auditDataset.patientIds = patientIds.toArray()

        // obsolete (merged) patient ID
        auditDataset.oldPatientId = iiToCx(regEvent.replacementOf[0].priorRegistration.subject1.priorRegisteredRole.id[0]) ?: null
    }


    // TODO consider oldPatientId for Merge operations --> requires API change
    @Override
    String[] getNecessaryAuditFieldNames() {
        return NECESSARY_FIELD_NAMES
    }


    @Override
    void doAudit(Hl7v3AuditDataset auditDataset) {
        switch (auditDataset.requestType) {
            case 'PRPA_IN201301UV02':
                AuditorManager.hl7v3Auditor.auditIti44Add(
                        serverSide,
                        auditDataset.eventOutcomeCode,
                        auditDataset.userId,
                        auditDataset.userName,
                        auditDataset.serviceEndpointUrl,
                        auditDataset.clientIpAddress,
                        auditDataset.patientIds,
                        auditDataset.messageId)
                break

            case 'PRPA_IN201302UV02':
                AuditorManager.hl7v3Auditor.auditIti44Revise(
                        serverSide,
                        auditDataset.eventOutcomeCode,
                        auditDataset.userId,
                        auditDataset.userName,
                        auditDataset.serviceEndpointUrl,
                        auditDataset.clientIpAddress,
                        auditDataset.patientIds,
                        auditDataset.messageId)
                break

            case 'PRPA_IN201304UV02':
                AuditorManager.hl7v3Auditor.auditIti44Delete(
                        serverSide,
                        auditDataset.eventOutcomeCode,
                        auditDataset.userId,
                        auditDataset.userName,
                        auditDataset.serviceEndpointUrl,
                        auditDataset.clientIpAddress,
                        auditDataset.oldPatientId,
                        auditDataset.messageId)

                AuditorManager.hl7v3Auditor.auditIti44Revise(
                        serverSide,
                        auditDataset.eventOutcomeCode,
                        auditDataset.userId,
                        auditDataset.userName,
                        auditDataset.serviceEndpointUrl,
                        auditDataset.clientIpAddress,
                        auditDataset.patientIds,
                        auditDataset.messageId)
                break

            default:
                LOG.error("Don't know how to audit message ${auditDataset.requestType}")
        }
    }


}
