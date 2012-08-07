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
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ExportEvent;
import org.openhealthtools.ihe.atna.auditor.events.ihe.ImportEvent;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Custom Implementation of RAD event auditors to send audit messages for
 * <ul>
 *     <li>RAD-69 Retrieve Imaging Document Set event for XDS.b Document Consumer actors.</li>
 *     <li>RAD-75 Cross Gateway Retrieve Imaging Document Set event for XCA-I.b Initiating Gateway actors</li>
 * </ul>
 *
 * @author Clay Sebourn
 */
public class CustomRadAuditor extends XDSAuditor {

    public static CustomRadAuditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (CustomRadAuditor) ctx.getAuditor(CustomRadAuditor.class);
    }

    /**
     * Audits an RAD-69 Retrieve Imaging Document Set event for XDS-I.b Document Consumer actors.
     * Sends audit messages for situations when more than one repository and more than one community are specified in the transaction.
     *
     * @param eventOutcome          The event outcome indicator
     * @param userId                The Active Participant UserId for the document repository (if using WS-Security / XUA)
     * @param userName              The Active Participant UserName for the document repository (if using WS-Security / XUA)
     * @param clientIpAddress       The IP address of the document consumer that initiated the transaction
     * @param repositoryEndpointUri The Web service endpoint URI for the document repository
     * @param studyInstanceIds      The list of Study UniqueId(s) for the document(s) retrieved
     * @param seriesInstanceIds     The list of Series UniqueId(s) for the document(s) retrieved
     * @param documentUniqueIds     The list of Document Entry UniqueId(s) for the document(s) retrieved
     * @param repositoryUniqueIds   The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
     * @param homeCommunityIds      The list of XCA Home Community Ids involved in this transaction (aligned with Document Unique Ids array)
     * @param patientId             The patient ID the document(s) relate to (if known)
     */
    public void auditRetrieveImagingDocumentSetEvent(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String clientIpAddress,
            String repositoryEndpointUri,
            String[] studyInstanceIds,
            String[] seriesInstanceIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds,
            String patientId)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        ImportEvent importEvent = new ImportEvent(false, eventOutcome, new CustomIHERadTransactionEventTypeCodes.RetrieveImagingDocumentSet());
        importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        importEvent.addSourceActiveParticipant(repositoryEndpointUri, null, null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);

        /*
         * FIXME:  Overriding endpoint URI with "anonymous", for now
         */
        String replyToUri = "http://www.w3.org/2005/08/addressing/anonymous";
        importEvent.addDestinationActiveParticipant(replyToUri, getSystemAltUserId(), getSystemUserName(), getSystemNetworkId(), true);
        if (!EventUtils.isEmptyOrNull(userName)) {
            importEvent.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }
        if (!EventUtils.isEmptyOrNull(patientId)) {
            importEvent.addPatientParticipantObject(patientId);
        }
        if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
            for (int i=0; i<documentUniqueIds.length; i++) {
                importEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
            }
        }
        audit(importEvent);
    }
    /**
     * Audits an RAD-69 Retrieve Imaging Document Set XDS-I.b event for XDS.b Document Repository actors.
     * Sends audit messages for situations when more than one repository and more than one community are specified in the transaction.
     *
     * @param eventOutcome          The event outcome indicator
     * @param consumerUserId        The Active Participant UserID for the document consumer (if using WS-Addressing)
     * @param consumerUserName      The Active Participant UserName for the document consumer (if using WS-Security / XUA)
     * @param consumerIpAddress     The IP address of the document consumer that initiated the transaction
     * @param repositoryEndpointUri The Web service endpoint URI for this document repository
     * @param studyInstanceIds      The list of Study UniqueId(s) for the document(s) retrieved
     * @param seriesInstanceIds     The list of Series UniqueId(s) for the document(s) retrieved
     * @param documentUniqueIds     The list of Document Entry UniqueId(s) for the document(s) retrieved
     * @param repositoryUniqueIds   The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
     * @param homeCommunityIds      The list of XCA Home Community Ids involved in this transaction (aligned with Document Unique Ids array)
     */
    public void auditRetrieveImagingDocumentSetEvent(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String consumerUserId,
            String consumerUserName,
            String consumerIpAddress,
            String repositoryEndpointUri,
            String[] studyInstanceIds,
            String[] seriesInstanceIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds)
    {
        if (!isAuditorEnabled()) {
            return;
        }
        ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new CustomIHERadTransactionEventTypeCodes.RetrieveImagingDocumentSet());
        exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        exportEvent.addSourceActiveParticipant(repositoryEndpointUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
        exportEvent.addDestinationActiveParticipant(consumerUserId, null, consumerUserName, consumerIpAddress, true);
        if (! EventUtils.isEmptyOrNull(consumerUserName)) {
            exportEvent.addHumanRequestorActiveParticipant(consumerUserName, null, consumerUserName, null);
        }

        //exportEvent.addPatientParticipantObject(patientId);
        if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
            for (int i=0; i<documentUniqueIds.length; i++) {
                //todo add study/series ids
                exportEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
            }
        }

        audit(exportEvent);
    }

    /**
     * Audits an RAD-75 Cross Gateway Retrieve Imaging Document Set event for XCA Initiating Gateway actors.
     *
     * @param eventOutcome                  The event outcome indicator
     * @param initiatingGatewayUserId       The Active Participant UserID for the initiating gateway (if using WS-Addressing)
     * @param initiatingGatewayUserName     The Active Participant UserName for the intiating gateway (if using WS-Security / XUA)
     * @param consumerIpAddress             The IP address of the document consumer that initiated the transaction
     * @param respondingGatewayEndpointUri  The Web service endpoint URI for the document repository
     * @param studyInstanceIds              The list of Study UniqueId(s) for the document(s) retrieved
     * @param seriesInstanceIds             The list of Series UniqueId(s) for the document(s) retrieved
     * @param documentUniqueIds             The list of Document Entry UniqueId(s) for the document(s) retrieved
     * @param repositoryUniqueIds           The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
     * @param homeCommunityIds              The home community id used in the transaction
     */
    public void auditCrossGatewayRetrieveImagingDocumentSetEvent(
        RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
        String initiatingGatewayUserId,
        String initiatingGatewayUserName,
        String consumerIpAddress,
        String respondingGatewayEndpointUri,
        String[] studyInstanceIds,
        String[] seriesInstanceIds,
        String[] documentUniqueIds,
        String[] repositoryUniqueIds,
        String[] homeCommunityIds,
        String patientId)
    {
        if (!isAuditorEnabled()) {
            return;
        }

        ImportEvent importEvent = new ImportEvent(false, eventOutcome, new CustomIHERadTransactionEventTypeCodes.CrossGatewayRetrieveImagingDocumentSet());
        importEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        importEvent.addSourceActiveParticipant(respondingGatewayEndpointUri, null, null, EventUtils.getAddressForUrl(respondingGatewayEndpointUri, false), false);
        importEvent.addDestinationActiveParticipant(initiatingGatewayUserId, getSystemAltUserId(), initiatingGatewayUserName, getSystemNetworkId(), true);

        if (!EventUtils.isEmptyOrNull(initiatingGatewayUserId)) {
            importEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserId, null, initiatingGatewayUserName, null);
        } else if(!EventUtils.isEmptyOrNull(initiatingGatewayUserName)) {
            importEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserName, null, initiatingGatewayUserName, null);
        }

        if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
            for (int i=0; i<documentUniqueIds.length; i++) {
                importEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
            }
        }
        audit(importEvent);
    }

    /**
     * Audits an RAD-75 Cross Gateway Retrieve Imaging Document Setevent for XCA Responding Gateway actors.
     *
     * @param eventOutcome                  The event outcome indicator
     * @param initiatingGatewayUserId       The Active Participant UserID for the document consumer (if using WS-Addressing)
     * @param initiatingGatewayUserName     The Active Participant UserName for the document consumer (if using WS-Security / XUA)
     * @param initiatingGatewayIpAddress    The IP address of the document consumer that initiated the transaction
     * @param respondingGatewayEndpointUri  The Web service endpoint URI for this document repository
     * @param studyInstanceIds              The list of Study UniqueId(s) for the document(s) retrieved
     * @param seriesInstanceIds             The list of Series UniqueId(s) for the document(s) retrieved
     * @param documentUniqueIds             The list of Document Entry UniqueId(s) for the document(s) retrieved
     * @param repositoryUniqueIds           The list of XDS.b Repository Unique Ids involved in this transaction (aligned with Document Unique Ids array)
     * @param homeCommunityIds              The home community ids used in the transaction
     */
    public void auditCrossGatewayRetrieveImagingDocumentSetEvent(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String initiatingGatewayUserId,
            String initiatingGatewayUserName,
            String initiatingGatewayIpAddress,
            String respondingGatewayEndpointUri,
            String[] studyInstanceIds,
            String[] seriesInstanceIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds)
    {
        if (!isAuditorEnabled()) {
            return;
        }
        ExportEvent exportEvent = new ExportEvent(true, eventOutcome, new CustomIHERadTransactionEventTypeCodes.CrossGatewayRetrieveImagingDocumentSet());
        exportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        exportEvent.addSourceActiveParticipant(respondingGatewayEndpointUri, getSystemAltUserId(), null, EventUtils.getAddressForUrl(respondingGatewayEndpointUri, false), false);

        if (!EventUtils.isEmptyOrNull(initiatingGatewayUserId)) {
            exportEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserId, null, initiatingGatewayUserName, null);
        } else if(!EventUtils.isEmptyOrNull(initiatingGatewayUserName)) {
            exportEvent.addHumanRequestorActiveParticipant(initiatingGatewayUserName, null, initiatingGatewayUserName, null);
        }

        exportEvent.addDestinationActiveParticipant(initiatingGatewayUserId, null, null, initiatingGatewayIpAddress, true);
        //exportEvent.addPatientParticipantObject(patientId);
        if (!EventUtils.isEmptyOrNull(documentUniqueIds)) {
            for (int i=0; i<documentUniqueIds.length; i++) {
                exportEvent.addDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i], homeCommunityIds[i]);
            }
        }
        audit(exportEvent);
    }
}
