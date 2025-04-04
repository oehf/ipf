/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55

import groovy.util.logging.Slf4j
import org.openehealth.ipf.commons.audit.AuditContext
import org.openehealth.ipf.commons.audit.model.AuditMessage
import org.openehealth.ipf.commons.ihe.core.atna.event.DefaultQueryInformationBuilder
import org.openehealth.ipf.commons.ihe.core.atna.event.IHEAuditMessageBuilder
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset
import org.openehealth.ipf.commons.ihe.hl7v3.audit.codes.Hl7v3EventTypeCode
import org.openehealth.ipf.commons.ihe.hl7v3.audit.codes.Hl7v3ParticipantObjectIdTypeCode
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47AuditStrategy

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString

/**
 * Generic audit strategy for ITI-55 (XCPD).
 * @author Dmytro Rud
 */
@Slf4j
class Iti55AuditStrategy extends Iti47AuditStrategy {

    Iti55AuditStrategy(boolean serverSide) {
        super(serverSide)
    }

    @Override
    Hl7v3AuditDataset enrichAuditDatasetFromRequest(Hl7v3AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        try {
            request = slurpIfNecessary(request)
            super.enrichAuditDatasetFromRequest(auditDataset, request, parameters)

            // query ID
            auditDataset.messageId = idString(request.id)
            auditDataset.queryId = idString(request.controlActProcess.queryByParameter.queryId)

            // home community ID
            auditDataset.homeCommunityId = request.sender.device.asAgent.representedOrganization.id.@root.text() ?: null
        } catch (Exception e) {
            log.warn('Missing or malformed request', e)
        }
        return auditDataset
    }

    @Override
    AuditMessage[] makeAuditMessage(AuditContext auditContext, Hl7v3AuditDataset auditDataset) {
        def builder = new DefaultQueryInformationBuilder(auditContext, auditDataset, Hl7v3EventTypeCode.CrossGatewayPatientDiscovery, auditDataset.purposesOfUse)
        // No patient identifiers are included for the Initiating Gateway
        if (serverSide) {
            builder.addPatients(auditDataset.patientIds)
        }
        builder.setQueryParameters(
                auditDataset.messageId,
                Hl7v3ParticipantObjectIdTypeCode.CrossGatewayPatientDiscovery,
                auditDataset.requestPayload,
                IHEAuditMessageBuilder.IHE_HOME_COMMUNITY_ID,
                auditDataset.homeCommunityId)

        return builder.messages
    }

    /**
     * MCCI ACKs are of no interest for ITI-55 ATNA auditing.
     */
    @Override
    boolean isAuditableResponse(Object response) {
        return (!Iti55Utils.isMcciAck(response.toString()))
    }
}
