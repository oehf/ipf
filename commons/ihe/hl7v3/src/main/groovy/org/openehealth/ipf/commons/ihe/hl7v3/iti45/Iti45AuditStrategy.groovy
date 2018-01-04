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
package org.openehealth.ipf.commons.ihe.hl7v3.iti45

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3EventTypeCode

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ParticipantObjectIdTypeCode.PIXQuery
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.iiToCx
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.render

/**
 * @author Dmytro Rud
 */
class Iti45AuditStrategy extends Hl7v3AuditStrategy {

    Iti45AuditStrategy(boolean serverSide) {
        super(serverSide)
    }


    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)
        GPathResult qbp = request.controlActProcess.queryByParameter

        // patient ID from request
        auditDataset.setPatientIds([iiToCx(qbp.parameterList.patientIdentifier[0].value)] as String[])

        // dump of queryByParameter
        auditDataset.requestPayload = render(qbp)
        auditDataset
    }


    @Override
    boolean enrichAuditDatasetFromResponse(Hl7v3AuditDataset auditDataset, Object response) {
        response = slurp(response)
        boolean result = super.enrichAuditDatasetFromResponse(auditDataset, response)

        // patient IDs from response
        Set<String> patientIds = [] as Set<String>
        addPatientIds(response.controlActProcess.subject[0].registrationEvent.subject1.patient.id, patientIds)
        if (auditDataset.patientIds) {
            patientIds << auditDataset.patientIds[0]
        }
        auditDataset.patientIds = patientIds as String[]
        result
    }

    @Override
    AuditMessage[] makeAuditMessage(Hl7v3AuditDataset auditDataset) {
        new IHEQueryBuilder(auditDataset, Hl7v3EventTypeCode.PIXQuery, auditDataset.getPurposesOfUse())
                .addPatients(auditDataset.patientIds)
                .setQueryParameters(auditDataset.messageId, PIXQuery, auditDataset.requestPayload)
                .getMessages()
    }

}
