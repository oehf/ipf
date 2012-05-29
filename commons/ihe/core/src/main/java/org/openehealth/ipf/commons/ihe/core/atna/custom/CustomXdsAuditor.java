/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.XDSAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of XDS Auditors to send audit messages for
 * <ul>
 *     <li>ITI-61 (XDS.b Register On-Demand Document Entry)</li>
 *     <li>ITI-63 (XCF Cross-Community Fetch)</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
public class CustomXdsAuditor extends XDSAuditor {

    public static CustomXdsAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (CustomXdsAuditor) ctx.getAuditor(CustomXdsAuditor.class);
    }


    public void auditIti61(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String registryEndpointUri,
            String clientIpAddress,
            String submissionSetUniqueId,
            String patientId)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        GenericIHEAuditEventMessage event = new GenericIHEAuditEventMessage(
                true,
                eventOutcome,
                serverSide ? RFC3881EventCodes.RFC3881EventActionCodes.CREATE : RFC3881EventCodes.RFC3881EventActionCodes.READ,
                serverSide ? new DICOMEventIdCodes.Import() : new DICOMEventIdCodes.Export(),
                new CustomIHETransactionEventTypeCodes.RegisterOnDemandDocumentEntry());

        configureEvent(this, serverSide, event, userId, userName, registryEndpointUri, registryEndpointUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientId)) {
            event.addPatientParticipantObject(patientId);
        }
        event.addSubmissionSetParticipantObject(submissionSetUniqueId);
        audit(event);
    }


    public void auditIti63(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String respondingGatewayUri,
            String clientIpAddress,
            String queryUuid,
            String requestPayload,
            String homeCommunityId,
            String patientId)
    {
        auditQueryEvent(
                ! serverSide,
                new CustomIHETransactionEventTypeCodes.CrossCommunityFetch(),
                eventOutcome,
                getAuditSourceId(),
                getAuditEnterpriseSiteId(),
                userId,
                serverSide ? null : getSystemAltUserId(),
                null,
                clientIpAddress,
                userName,
                userName,
                false,
                respondingGatewayUri,
                serverSide ? getSystemAltUserId() : null,
                queryUuid,
                requestPayload,
                homeCommunityId,
                patientId);
    }
}
