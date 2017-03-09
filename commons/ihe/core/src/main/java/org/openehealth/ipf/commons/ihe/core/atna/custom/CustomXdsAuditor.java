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
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionParticipantObjectIDTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of ATNA Auditors for the following XDS-based transactions:
 * <ul>
 *     <li>ITI-51 -- XDS.b Multi-Patient Stored Query</li>
 *     <li>ITI-57 -- XDS.b Update Document Set</li>
 *     <li>ITI-61 -- XDS.b Register On-Demand Document Entry</li>
 *     <li>ITI-62 -- XDS.b Delete Document Set</li>
 *     <li>ITI-63 -- XCF Cross-Community Fetch</li>
 *     <li>RAD-69 -- XDS-I.b Retrieve Imaging Document Set</li>
 *     <li>RAD-75 -- XCA-I Cross-Gateway Retrieve Imaging Document Set</li>
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
     * Audits an ITI-51 Multi-Patient Query event.
     *
     * @param serverSide
     *      <code>true</code> for the Document Registry actor,
     *      <code>false</code> for the Document Consumer actor.
     * @param eventOutcome
     *      event outcome code.
     * @param userId
     *      user ID (contents of the WS-Addressing &lt;ReplyTo&gt; header).
     * @param userName
     *      user name on the Document Consumer side (for XUA).
     * @param serviceEndpointUri
     *      network endpoint URI of the Document Registry actor.
     * @param clientIpAddress
     *      IP address of the Document Consumer actor.
     * @param queryUuid
     *      UUID of the XDS query.
     * @param requestPayload
     *      the whole XDS request as an XML String.
     * @param homeCommunityId
     *      home community ID (optional).
     * @param patientId
     *      patient ID as an HL7 v2 CX string.
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditIti51(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String queryUuid,
            String requestPayload,
            String homeCommunityId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (!isAuditorEnabled()) {
            return;
        }

        doAuditQueryEvent(
                serverSide,
                new CustomIHETransactionEventTypeCodes.MultiPatientQuery(),
                eventOutcome,
                userId,
                userName,
                serviceEndpointUri,
                clientIpAddress,
                queryUuid,
                requestPayload,
                homeCommunityId,
                patientId,
                purposesOfUse);
    }

    /**
     * Generically sends audit messages for XDS Document Administrator Update Document Set events
     *
     * @param eventOutcome The event outcome indicator
     * @param repositoryUserId The Active Participant UserID for the document repository (if using WS-Addressing)
     * @param registryEndpointUri  The Web service endpoint URI for the document registry
     * @param submissionSetUniqueId The UniqueID of the Submission Set registered
     * @param patientId The Patient Id that this submission pertains to
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditClientIti57(
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String repositoryUserId,
            String userName,
            String registryEndpointUri,
            String submissionSetUniqueId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        GenericIHEAuditEventMessage iti57ExportEvent = new GenericIHEAuditEventMessage(
                true,
                eventOutcome,
                RFC3881EventCodes.RFC3881EventActionCodes.UPDATE,
                new DICOMEventIdCodes.Export(),
                new CustomIHETransactionEventTypeCodes.UpdateDocumentSet(),
                purposesOfUse);

        iti57ExportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        iti57ExportEvent.addSourceActiveParticipant(
                repositoryUserId, getSystemAltUserId(), null, getSystemNetworkId(), true);

        if (!EventUtils.isEmptyOrNull(userName)) {
            iti57ExportEvent.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        iti57ExportEvent.addDestinationActiveParticipant(
                registryEndpointUri, null, null, EventUtils.getAddressForUrl(registryEndpointUri, false), false);
        if (!EventUtils.isEmptyOrNull(patientId)) {
            iti57ExportEvent.addPatientParticipantObject(patientId);
        }
        iti57ExportEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);
        audit(iti57ExportEvent);
    }

    /**
     * Generically sends audit messages for XDS Update Document Set events
     *
     * @param eventOutcome The event outcome indicator
     * @param sourceUserId The Active Participant UserID for the document consumer (if using WS-Addressing)
     * @param sourceIpAddress The IP address of the document source that initiated the transaction
     * @param repositoryEndpointUri The Web service endpoint URI for this document repository
     * @param submissionSetUniqueId The UniqueID of the Submission Set registered
     * @param patientId The Patient Id that this submission pertains to
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditServerIti57 (
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String sourceUserId,
            String sourceIpAddress,
            String userName,
            String repositoryEndpointUri,
            String submissionSetUniqueId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        GenericIHEAuditEventMessage iti57ImportEvent = new GenericIHEAuditEventMessage(
                false,
                eventOutcome,
                RFC3881EventCodes.RFC3881EventActionCodes.UPDATE,
                new DICOMEventIdCodes.Import(),
                new CustomIHETransactionEventTypeCodes.UpdateDocumentSet(),
                purposesOfUse);

        iti57ImportEvent.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());
        iti57ImportEvent.addSourceActiveParticipant(sourceUserId, null, null, sourceIpAddress, true);
        if (!EventUtils.isEmptyOrNull(userName)) {
            iti57ImportEvent.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        iti57ImportEvent.addDestinationActiveParticipant(repositoryEndpointUri, getSystemAltUserId(), null,
                EventUtils.getAddressForUrl(repositoryEndpointUri, false), false);
        if (!EventUtils.isEmptyOrNull(patientId)) {
            iti57ImportEvent.addPatientParticipantObject(patientId);
        }

        iti57ImportEvent.addSubmissionSetParticipantObject(submissionSetUniqueId);
        audit(iti57ImportEvent);
    }


    /**
     * Audits an ITI-61 Register On-Demand Document Entry event.
     *
     * @param serverSide
     *      <code>true</code> for the Document Registry actor,
     *      <code>false</code> for the On-Demand Document Source actor.
     * @param eventOutcome
     *      event outcome code.
     * @param userId
     *      user ID (contents of the WS-Addressing &lt;ReplyTo&gt; header).
     * @param userName
     *      user name on the Document Consumer side (for XUA).
     * @param serviceEndpointUri
     *      network endpoint URI of the Document Registry actor.
     * @param clientIpAddress
     *      IP address of the Document On-Demand Document Source actor.
     * @param submissionSetUniqueId
     *      unique ID of the XDS submission set.
     * @param patientId
     *      patient ID as an HL7 v2 CX string.
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditIti61(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String submissionSetUniqueId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        GenericIHEAuditEventMessage event = new GenericIHEAuditEventMessage(
                ! serverSide,
                eventOutcome,
                serverSide ? RFC3881EventCodes.RFC3881EventActionCodes.CREATE : RFC3881EventCodes.RFC3881EventActionCodes.READ,
                serverSide ? new DICOMEventIdCodes.Import() : new DICOMEventIdCodes.Export(),
                new CustomIHETransactionEventTypeCodes.RegisterOnDemandDocumentEntry(),
                purposesOfUse);

        configureEvent(this, serverSide, event, userId, userName, serviceEndpointUri, serviceEndpointUri, clientIpAddress);
        if (!EventUtils.isEmptyOrNull(patientId)) {
            event.addPatientParticipantObject(patientId);
        }
        event.addSubmissionSetParticipantObject(submissionSetUniqueId);
        audit(event);
    }

    /**
     * Sends an audit message for the XDS Delete Document Set event.
     *
     * @param serverSide <code>true</code> for XDS Registry, <code>false</code> for Document Administrator
     * @param eventOutcome The event outcome indicator
     * @param userId ID of the user at the actor performing audit trail
     * @param userName name of the user at the actor performing audit trail
     * @param serviceEndpointUri Web Service endpoint URI of the Document Registry
     * @param clientIpAddress IP address of the Document Administrator
     * @param patientId ID of the patient related to the deleted registry objects
     * @param objectUuids UUIDs of the registry objects being deleted
     * @param purposesOfUse &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditIti62(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String patientId,
            String[] objectUuids,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        GenericIHEAuditEventMessage event = new GenericIHEAuditEventMessage(
                ! serverSide,
                eventOutcome,
                RFC3881EventCodes.RFC3881EventActionCodes.DELETE,
                new DICOMEventIdCodes.Export(),
                new CustomIHETransactionEventTypeCodes.DeleteDocumentSet(),
                purposesOfUse);

        event.addSourceActiveParticipant(
                serverSide ? null : userId,
                serverSide ? null : getSystemAltUserId(),
                null,
                serverSide ? clientIpAddress : getSystemNetworkId(),
                true);

        if (!EventUtils.isEmptyOrNull(userName)) {
            event.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        event.addDestinationActiveParticipant(
                serviceEndpointUri,
                serverSide ? getSystemAltUserId() : null,
                null,
                serverSide ? getSystemNetworkId() : EventUtils.getAddressForUrl(serviceEndpointUri, false),
                false);

        event.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());

        if (!EventUtils.isEmptyOrNull(patientId)) {
            event.addPatientParticipantObject(patientId);
        }

        if (objectUuids != null) {
            for (String uuid : objectUuids) {
                event.addDocumentEntryObject(new IHETransactionParticipantObjectIDTypeCodes.StableXdsDocumentEntry(), uuid);
            }
        }

        audit(event);
    }

    /**
     * Audits an ITI-63 XCF Cross-Community Fetch event.
     *
     * @param serverSide
     *      <code>true</code> for the Responding Gateway actor,
     *      <code>false</code> for the Initiating Gateway actor.
     * @param eventOutcome
     *      event outcome code.
     * @param userId
     *      user ID (contents of the WS-Addressing &lt;ReplyTo&gt; header).
     * @param userName
     *      user name on the Document Consumer side (for XUA).
     * @param serviceEndpointUri
     *      network endpoint URI of the XCF Responding Gateway actor.
     * @param clientIpAddress
     *      IP address of the XCF Initiating Gateway actor.
     * @param queryUuid
     *      UUID of the XCF query.
     * @param requestPayload
     *      the whole XCF request as an XML String.
     * @param homeCommunityId
     *      home community ID.
     * @param patientId
     *      patient ID as an HL7 v2 CX string.
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditIti63(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String queryUuid,
            String requestPayload,
            String homeCommunityId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        doAuditQueryEvent(
                serverSide,
                new CustomIHETransactionEventTypeCodes.CrossCommunityFetch(),
                eventOutcome,
                userId,
                userName,
                serviceEndpointUri,
                clientIpAddress,
                queryUuid,
                requestPayload,
                homeCommunityId,
                patientId,
                purposesOfUse);
    }


    /**
     * Audits an RAD-69 Retrieve Imaging Document Set event.
     *
     * @param serverSide
     *      <code>true</code> for the Imaging Document source actor,
     *      <code>false</code> for the Imaging Document Consumer actor.
     * @param eventOutcome
     *      event outcome code.
     * @param userId
     *      user ID (contents of the WS-Addressing &lt;ReplyTo&gt; header).
     * @param userName
     *      user name on the Document Consumer side (for XUA).
     * @param serviceEndpointUri
     *      network endpoint URI of the Imaging Document Source actor.
     * @param clientIpAddress
     *      IP address of the Imaging Document Consumer actor.
     * @param studyInstanceUniqueIds,
     *      list of study instance unique IDs.
     * @param seriesInstanceUniqueIds,
     *      list of series instance unique IDs.
     * @param documentUniqueIds,
     *      list of document unique IDs.
     * @param repositoryUniqueIds,
     *      list of unique IDs of document repositories.
     * @param homeCommunityIds,
     *      list of home community IDs.
     * @param patientId
     *      patient ID as an HL7 v2 CX string (if known).
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditRad69(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String[] studyInstanceUniqueIds,
            String[] seriesInstanceUniqueIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        doAuditImagingEvent(
                serverSide,
                new CustomIHETransactionEventTypeCodes.RetrieveImagingDocumentSet(),
                eventOutcome,
                userId,
                userName,
                serviceEndpointUri,
                clientIpAddress,
                studyInstanceUniqueIds,
                seriesInstanceUniqueIds,
                documentUniqueIds,
                repositoryUniqueIds,
                homeCommunityIds,
                patientId,
                purposesOfUse);
    }


    /**
     * Audits an RAD-75 Cross-Gateway Retrieve Imaging Document Set event.
     *
     * @param serverSide
     *      <code>true</code> for the Document Registry actor,
     *      <code>false</code> for the Document Consumer actor.
     * @param eventOutcome
     *      event outcome code.
     * @param userId
     *      user ID (contents of the WS-Addressing &lt;ReplyTo&gt; header).
     * @param userName
     *      user name on the Document Consumer side (for XUA).
     * @param serviceEndpointUri
     *      network endpoint URI of the Responding Gateway actor.
     * @param clientIpAddress
     *      IP address of the Initiating Gateway actor.
     * @param studyInstanceUniqueIds
     *      list of study instance unique IDs.
     * @param seriesInstanceUniqueIds
     *      list of series instance unique IDs.
     * @param documentUniqueIds
     *      list of document unique IDs.
     * @param repositoryUniqueIds
     *      list of unique IDs of document repositories.
     * @param homeCommunityIds
     *      list of home community IDs.
     * @param patientId
     *      patient ID as an HL7 v2 CX string (if known).
     * @param purposesOfUse
     *      &lt;PurposeOfUse&gt; attributes from XUA SAML assertion.
     */
    public void auditRad75(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String[] studyInstanceUniqueIds,
            String[] seriesInstanceUniqueIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        doAuditImagingEvent(
                serverSide,
                new CustomIHETransactionEventTypeCodes.CrossGatewayRetrieveImagingDocumentSet(),
                eventOutcome,
                userId,
                userName,
                serviceEndpointUri,
                clientIpAddress,
                studyInstanceUniqueIds,
                seriesInstanceUniqueIds,
                documentUniqueIds,
                repositoryUniqueIds,
                homeCommunityIds,
                patientId,
                purposesOfUse);
    }


    private void doAuditImagingEvent(
            boolean serverSide,
            IHETransactionEventTypeCodes transactionEventTypeCodes,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serviceEndpointUri,
            String clientIpAddress,
            String[] studyInstanceUniqueIds,
            String[] seriesInstanceUniqueIds,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        ImagingRetrieveEvent event = new ImagingRetrieveEvent(
                ! serverSide,
                eventOutcome,
                transactionEventTypeCodes,
                purposesOfUse);

        event.addSourceActiveParticipant(
                serviceEndpointUri,
                serverSide ? getSystemAltUserId() : null,
                null,
                serverSide ? getSystemNetworkId() : EventUtils.getAddressForUrl(serviceEndpointUri, false),
                false);

        event.addDestinationActiveParticipant(
                userId,
                serverSide ? null : getSystemAltUserId(),
                null,
                serverSide ? clientIpAddress : getSystemNetworkId(),
                true);

        if (! EventUtils.isEmptyOrNull(userName)) {
            event.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        event.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());

        if (! EventUtils.isEmptyOrNull(patientId)) {
            event.addPatientParticipantObject(patientId);
        }

        if (! EventUtils.isEmptyOrNull(documentUniqueIds)) {
            for (int i = 0; i < documentUniqueIds.length; i++) {
                event.addDocumentParticipantObject(
                        studyInstanceUniqueIds[i],
                        seriesInstanceUniqueIds[i],
                        documentUniqueIds[i],
                        repositoryUniqueIds[i],
                        homeCommunityIds[i]);
            }
        }

        audit(event);
    }


    private void doAuditQueryEvent(
            boolean serverSide,
            IHETransactionEventTypeCodes transactionEventTypeCode,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String serverEndpointUri,
            String clientIpAddress,
            String queryUuid,
            String requestPayload,
            String homeCommunityId,
            String patientId,
            List<CodedValueType> purposesOfUse)
    {
        auditQueryEvent(
                ! serverSide,
                transactionEventTypeCode,
                eventOutcome,
                getAuditSourceId(),
                getAuditEnterpriseSiteId(),
                userId,
                serverSide ? null : getSystemAltUserId(),
                null,
                serverSide ? clientIpAddress : getSystemNetworkId(),
                userName,
                userName,
                false,
                serverEndpointUri,
                serverSide ? getSystemAltUserId() : null,
                queryUuid,
                requestPayload,
                homeCommunityId,
                patientId,
                purposesOfUse);
    }

    public void auditItiY1(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String userId,
            String userName,
            String clientIpAddress,
            String serviceEndpointUri,
            String patientId,
            String[] documentUniqueIds,
            String[] repositoryUniqueIds,
            String[] homeCommunityIds,
            List<CodedValueType> purposesOfUse)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        RemoveDocumentsEvent event = new RemoveDocumentsEvent(!serverSide, eventOutcome, purposesOfUse);

        event.addSourceActiveParticipant(
                serverSide ? null : userId,
                serverSide ? null : getSystemAltUserId(),
                null,
                serverSide ? clientIpAddress : getSystemNetworkId(),
                true);

        if (!EventUtils.isEmptyOrNull(userName)) {
            event.addHumanRequestorActiveParticipant(userName, null, userName, null);
        }

        event.addDestinationActiveParticipant(
                serviceEndpointUri,
                serverSide ? getSystemAltUserId() : null,
                null,
                serverSide ? getSystemNetworkId() : EventUtils.getAddressForUrl(serviceEndpointUri, false),
                false);

        event.setAuditSourceId(getAuditSourceId(), getAuditEnterpriseSiteId());

        if (!EventUtils.isEmptyOrNull(patientId)) {
            event.addPatientParticipantObject(patientId);
        }

        for (int i = 0; i < documentUniqueIds.length; ++i) {
            event.addRemovedDocumentParticipantObject(documentUniqueIds[i], repositoryUniqueIds[i]);
        }

        audit(event);
    }
}
