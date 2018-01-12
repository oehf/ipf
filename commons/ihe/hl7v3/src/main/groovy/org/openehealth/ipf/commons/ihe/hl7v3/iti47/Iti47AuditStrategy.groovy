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
package org.openehealth.ipf.commons.ihe.hl7v3.iti47

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3EventTypeCode

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ParticipantObjectIdTypeCode.PatientDemographicsQuery
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
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

        // query IDs from request
        auditDataset.messageId = idString(request.id)
        auditDataset.queryId = idString(request.controlActProcess.queryByParameter.queryId)

        // patient IDs from request
        Set<String> patientIds = [] as Set<String>
        addPatientIds(qbp.parameterList.livingSubjectId.value, patientIds)
        auditDataset.setPatientIds(patientIds.toArray(new String[patientIds.size()]) ?: null)

        // dump of the "queryByParameter" element
        auditDataset.requestPayload = render(qbp)
        auditDataset
    }


    @Override
    boolean enrichAuditDatasetFromResponse(Hl7v3AuditDataset auditDataset, Object response) {
        response = slurp(response)
        boolean result = super.enrichAuditDatasetFromResponse(auditDataset, response)

        // patient IDs from response FIXME really?
        Set<String> patientIds = [] as Set<String>
        addPatientIds(response.controlActProcess.subject.registrationEvent.subject1.patient.id, patientIds)
        if (auditDataset.patientIds) {
            patientIds.addAll(auditDataset.patientIds)
        }
        auditDataset.patientIds = patientIds as String[] ?: null
        result
    }

    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        new QueryInformationBuilder(auditContext, auditDataset, Hl7v3EventTypeCode.PatientDemographicsQuery, auditDataset.getPurposesOfUse())
                .setQueryParameters(auditDataset.messageId, PatientDemographicsQuery, auditDataset.requestPayload)
                .addPatients(auditDataset.patientIds)
                .getMessages()
    }

}
