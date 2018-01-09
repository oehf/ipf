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
package org.openehealth.ipf.commons.ihe.hl7v3.pcc1

import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEQueryBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditStrategy
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3EventTypeCode

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ParticipantObjectIdTypeCode.QueryExistingData
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.render

/**
 * @author Dmytro Rud
 */
class Pcc1AuditStrategy extends Hl7v3AuditStrategy {

    Pcc1AuditStrategy(boolean serverSide) {
        super(serverSide)
    }


    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        request = slurp(request)
        GPathResult qbp = request.controlActProcess.queryByParameter

        // query ID
        auditDataset.messageId = idString(request.id)
        auditDataset.queryId = idString(request.controlActProcess.queryByParameter.queryId)

        // patient IDs from request
        def patientIds = [] as Set<String>
        addPatientIds(qbp.parameterList.patientId.value, patientIds)
        auditDataset.setPatientIds(patientIds.toArray(new String[patientIds.size()]) ?: null)

        // dump of the "queryByParameter" element
        auditDataset.requestPayload = render(qbp)
        auditDataset
    }

    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        new IHEQueryBuilder<>(auditContext, auditDataset, Hl7v3EventTypeCode.QueryExistingData)
                .setQueryParameters(auditDataset.messageId, QueryExistingData, auditDataset.requestPayload)
                .addPatients(auditDataset.patientIds)
                .getMessages()
    }

}
