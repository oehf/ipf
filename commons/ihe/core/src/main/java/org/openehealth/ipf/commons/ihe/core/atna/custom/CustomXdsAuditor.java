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
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
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

    /**
     * Audits an ITI-51 Multi-Patient Query event for XDS.a and XDS.b Document Consumer actors.
     *
     * @param eventOutcome The event outcome indicator
     * @param registryEndpointUri The endpoint of the registry in this transaction
     * @param consumerUserName The Active Participant UserName for the consumer (if using WS-Security / XUA)
     * @param storedQueryUUID The UUID of the stored query
     * @param adhocQueryRequestPayload The payload of the adhoc query request element
     * @param homeCommunityId The home community id of the transaction (if present)
     * @param patientId The patient ID queried (if query pertained to a patient id)
     */
    public void auditIti51(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String registryEndpointUri,
            String consumerUserName,
            String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
            String patientId)
    {
        if (!isAuditorEnabled()) {
            return;
        }

        /*
           * FIXME:  Overriding endpoint URI with "anonymous", for now
           */
        String replyToUri = "http://www.w3.org/2005/08/addressing/anonymous";
        //String replyToUri = getSystemUserId();

        auditQueryEvent(true,
                new CustomIHETransactionEventTypeCodes.MultiPatientQuery(), eventOutcome,
                getAuditSourceId(), getAuditEnterpriseSiteId(),
                replyToUri, getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(),
                consumerUserName, consumerUserName, false,
                registryEndpointUri, null,
                storedQueryUUID, adhocQueryRequestPayload, homeCommunityId,
                patientId);
    }

    /**
     * Audits an ITI-51 Multi-Patient Query event for XDS.a and XDS.b Document Registry actors.
     *
     * @param eventOutcome The event outcome indicator
     * @param consumerUserId The Active Participant UserID for the consumer (if using WS-Addressing)
     * @param consumerUserName The Active Participant UserName for the consumer (if using WS-Security / XUA)
     * @param consumerIpAddress The IP Address of the consumer that initiated the transaction
     * @param registryEndpointUri The URI of this registry's endpoint that received the transaction
     * @param storedQueryUUID The UUID of the stored query
     * @param adhocQueryRequestPayload The payload of the adhoc query request element
     * @param homeCommunityId The home community id of the transaction (if present)
     * @param patientId The patient ID queried (if query pertained to a patient id)
     */
    public void auditRegistryMultiPatientQueryEvent(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String consumerUserId, String consumerUserName, String consumerIpAddress,
            String registryEndpointUri,
            String storedQueryUUID, String adhocQueryRequestPayload, String homeCommunityId,
            String patientId)
    {
        if (!isAuditorEnabled()) {
            return;
        }
        auditQueryEvent(false,
                new CustomIHETransactionEventTypeCodes.MultiPatientQuery(), eventOutcome,
                getAuditSourceId(), getAuditEnterpriseSiteId(),
                consumerUserId, null, consumerUserName, consumerIpAddress,
                consumerUserName, consumerUserName, false,
                registryEndpointUri, getSystemAltUserId(),
                storedQueryUUID, adhocQueryRequestPayload, homeCommunityId,
                patientId);
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
