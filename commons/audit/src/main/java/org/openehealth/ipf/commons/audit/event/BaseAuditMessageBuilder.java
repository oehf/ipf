/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.event;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.model.*;
import org.openehealth.ipf.commons.audit.types.*;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * AuditMessage builder with some protected helper methods that are called by subclasses in order to add
 * e.g. participants and participating objects of the audit event.
 *
 * @param <T>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public abstract class BaseAuditMessageBuilder<T extends BaseAuditMessageBuilder<T>> implements AuditMessageBuilder<T> {

    private static final Pattern IPV4 = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    private static final Pattern IPV6 = Pattern.compile("^(?:[A-F0-9]{1,4}:){7}[A-F0-9]{1,4}$", Pattern.CASE_INSENSITIVE);
    private AuditMessage auditMessage;

    public BaseAuditMessageBuilder() {
        this.auditMessage = new AuditMessage();
    }

    @Override
    public void validate() {
        auditMessage.validate();
    }

    /**
     * @return the AuditMessage built
     */
    public AuditMessage getMessage() {
        return auditMessage;
    }

    /**
     * Sets a {@link org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType} for a given Audit Source ID
     *
     * @param sourceId The Audit Source ID to use
     * @return this
     */
    public T setAuditSource(String sourceId) {
        return setAuditSource(sourceId, null);
    }

    /**
     * Sets a {@link org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType} for a given Audit Source ID
     * and Audit Source Enterprise Site ID
     *
     * @param sourceId         The Audit Source ID to use
     * @param enterpriseSiteId The Audit Enterprise Site ID to use
     * @return this
     */
    public T setAuditSource(String sourceId, String enterpriseSiteId) {
        return setAuditSource(sourceId, enterpriseSiteId, (AuditSource[]) null);
    }

    /**
     * Sets a {@link org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType} for a given Audit Source ID,
     * Audit Source Enterprise Site ID, and a list of audit source type codes
     *
     * @param sourceId         The Audit Source ID to use
     * @param enterpriseSiteId The Audit Enterprise Site ID to use
     * @param typeCodes        The RFC 3881 Audit Source Type codes to use
     * @return this
     */
    public T setAuditSource(String sourceId,
                            String enterpriseSiteId,
                            AuditSource... typeCodes) {
        return setAuditSourceIdentification(sourceId, enterpriseSiteId, typeCodes);
    }

    /**
     * Sets the audit source from the audit context
     *
     * @param auditContext audit context
     * @return this
     */
    public T setAuditSource(AuditContext auditContext) {
        return setAuditSourceIdentification(
                auditContext.getAuditSourceId(),
                auditContext.getAuditEnterpriseSiteId(),
                auditContext.getAuditSource());
    }

    /**
     * Create and set an Event Identification block for this audit event message
     *
     * @param outcome The Event Outcome Indicator
     * @param action  The Event Action Code
     * @param id      The Event ID
     * @param type    The Event Type Code
     * @return this
     */
    public T setEventIdentification(EventOutcomeIndicator outcome,
                                    String eventOutcomeDescription,
                                    EventActionCode action,
                                    EventId id,
                                    EventType type,
                                    PurposeOfUse... purposesOfUse) {
        return setEventIdentification(outcome, eventOutcomeDescription, action, id, type,
                purposesOfUse != null ? Arrays.asList(purposesOfUse) : Collections.emptyList());
    }

    /**
     * Create and set an Event Identification block for this audit event message
     *
     * @param outcome The Event Outcome Indicator
     * @param action  The Event Action Code
     * @param id      The Event ID
     * @param type    The Event Type Code
     * @return this
     */
    public T setEventIdentification(EventOutcomeIndicator outcome,
                                    String eventOutcomeDescription,
                                    EventActionCode action,
                                    EventId id,
                                    EventType type,
                                    Collection<PurposeOfUse> purposesOfUse) {
        EventIdentificationType eventIdentification = new EventIdentificationType(id, Instant.now(), outcome);
        eventIdentification.setEventActionCode(action);
        eventIdentification.setEventOutcomeDescription(eventOutcomeDescription);
        if (type != null) {
            eventIdentification.getEventTypeCode().add(type);
        }
        if (purposesOfUse != null) {
            purposesOfUse.stream()
                    .filter(Objects::nonNull)
                    .forEach(pou -> eventIdentification.getPurposesOfUse().add(pou));
        }
        auditMessage.setEventIdentification(eventIdentification);
        return self();
    }

    /**
     * Create and add an Audit Source Identification to this audit event message
     *
     * @param sourceID         The Audit Source ID
     * @param enterpriseSiteID The Audit Enterprise Site ID
     * @param typeCodes        The Audit Source Type Codes
     * @return this
     */
    public T setAuditSourceIdentification(String sourceID,
                                          String enterpriseSiteID,
                                          AuditSource... typeCodes) {
        return setAuditSourceIdentification(sourceID, enterpriseSiteID,
                typeCodes != null ? Arrays.asList(typeCodes) : Collections.emptyList());
    }

    /**
     * Create and add an Audit Source Identification to this audit event message
     *
     * @param sourceID         The Audit Source ID
     * @param enterpriseSiteID The Audit Enterprise Site ID
     * @param typeCodes        The Audit Source Type Codes
     * @return this
     */
    public T setAuditSourceIdentification(String sourceID,
                                          String enterpriseSiteID,
                                          Collection<AuditSource> typeCodes) {
        AuditSourceIdentificationType asi = new AuditSourceIdentificationType(sourceID);
        if (typeCodes != null) {
            typeCodes.stream()
                    .filter(Objects::nonNull)
                    .forEach(typeCode -> asi.getAuditSourceType().add(typeCode));
        }
        asi.setAuditEnterpriseSiteID(enterpriseSiteID);
        auditMessage.setAuditSourceIdentification(asi);
        return self();
    }

    /**
     * Adds an Active Participant representing the source participant
     *
     * @param userId      The identity of the local user or process exporting the data. If both are known, then two active participants shall be included (both the person and the process).
     * @param altUserId   The Active Participant's Alternate UserID
     * @param userName    The Active Participant's UserName
     * @param networkId   The Active Participant's Network Access Point ID
     * @param isRequestor Whether the participant represents the requestor (i.e. push request)
     * @return this
     */
    public T addSourceActiveParticipant(String userId,
                                        String altUserId,
                                        String userName,
                                        String networkId,
                                        boolean isRequestor) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                isRequestor,
                Collections.singletonList(ActiveParticipantRoleIdCode.Source),
                networkId);
    }

    /**
     * Adds an Active Participant representing destination participant
     *
     * @param userId               The identity of the remote user or process receiving the data
     * @param altUserId            The Active Participant's Alternate UserID
     * @param userName             The Active Participant's UserName
     * @param networkAccessPointId The Active Participant's Network Access Point ID
     * @param userIsRequestor      Whether the destination participant represents the requestor (i.e. pull request)
     * @return this
     */
    public T addDestinationActiveParticipant(String userId,
                                             String altUserId,
                                             String userName,
                                             String networkAccessPointId,
                                             boolean userIsRequestor) {
        return addActiveParticipant(
                userId,
                altUserId,
                userName,
                userIsRequestor,
                Collections.singletonList(ActiveParticipantRoleIdCode.Destination),
                networkAccessPointId);
    }


    /**
     * Create and add an Active Participant to this audit event message but automatically
     * determine the Network Access Point ID Type Code
     *
     * @param userID               The Active Participant's UserID
     * @param altUserID            The Active Participant's Alternate UserID
     * @param userName             The Active Participant's UserName
     * @param userIsRequestor      Whether this Active Participant is a requestor
     * @param roleIdCodes          The Active Participant's Role Codes
     * @param networkAccessPointID The Active Participant's Network Access Point ID (IP / Hostname)
     * @return this
     */
    public T addActiveParticipant(String userID,
                                  String altUserID,
                                  String userName,
                                  Boolean userIsRequestor,
                                  List<ActiveParticipantRoleId> roleIdCodes,
                                  String networkAccessPointID) {
        // Does lookup to see if using IP Address or hostname in Network Access Point ID
        return addActiveParticipant(
                userID,
                altUserID,
                userName,
                userIsRequestor,
                roleIdCodes,
                networkAccessPointID,
                getNetworkAccessPointCodeFromAddress(networkAccessPointID),
                null,
                null);
    }

    /**
     * Create and add an Active Participant block to this audit event message
     *
     * @param userID                     The Active Participant's UserID
     * @param altUserID                  The Active Participant's Alternate UserID
     * @param userName                   The Active Participant's UserName
     * @param userIsRequestor            Whether this Active Participant is a requestor
     * @param roleIdCodes                The Active Participant's Role Codes
     * @param networkAccessPointID       The Active Participant's Network Access Point ID (IP / Hostname)
     * @param networkAccessPointTypeCode The type code for the Network Access Point ID
     * @return this
     */
    public T addActiveParticipant(String userID,
                                  String altUserID,
                                  String userName,
                                  Boolean userIsRequestor,
                                  List<ActiveParticipantRoleId> roleIdCodes,
                                  String networkAccessPointID,
                                  NetworkAccessPointTypeCode networkAccessPointTypeCode,
                                  String mediaIdentifier,
                                  MediaType mediaType) {
        ActiveParticipantType ap = new ActiveParticipantType(userID, userIsRequestor);
        ap.setAlternativeUserID(altUserID);
        ap.setUserName(userName);
        if (roleIdCodes != null) {
            roleIdCodes.stream()
                    .filter(Objects::nonNull)
                    .forEach(roleId -> ap.getRoleIDCodes().add(roleId));
        }
        ap.setNetworkAccessPointID(networkAccessPointID);
        ap.setNetworkAccessPointTypeCode(networkAccessPointTypeCode);
        ap.setMediaIdentifier(mediaIdentifier);
        ap.setMediaType(mediaType);
        auditMessage.getActiveParticipants().add(ap);
        return self();
    }

    /**
     * Adds a Participant Object representing a patient involved in the event
     *
     * @param patientId   Identifier of the involved patient
     * @param patientName name of the involved patient
     * @return this
     */
    public T addPatientParticipantObject(String patientId, String patientName,
                                         List<TypeValuePairType> details,
                                         ParticipantObjectDataLifeCycle lifecycle) {
        return addParticipantObjectIdentification(
                ParticipantObjectIdTypeCode.PatientNumber,
                patientName,
                null,
                details,
                requireNonNull(patientId),
                ParticipantObjectTypeCode.Person,
                ParticipantObjectTypeCodeRole.Patient,
                lifecycle,
                null);
    }

    /**
     * Adds a Participant Object representing a studies involved in the event
     *
     * @param studyId       Identifier of the involved study
     * @param objectDetails objectDetails
     * @return this
     */
    public T addStudyParticipantObject(String studyId, List<TypeValuePairType> objectDetails) {
        return addParticipantObjectIdentification(
                ParticipantObjectIdTypeCode.StudyInstanceUID,
                studyId,
                null,
                objectDetails,
                requireNonNull(studyId),
                ParticipantObjectTypeCode.System,
                ParticipantObjectTypeCodeRole.Report,
                null,
                null);
    }


    /**
     * Create and add an Participant Object Identification block to this audit event message
     *
     * @param objectIDTypeCode    The Participant Object ID Type code
     * @param objectName          The Participant Object Name
     * @param objectQuery         The Participant Object Query data
     * @param objectDetails       The Participant Object detail
     * @param objectID            The Participant Object ID
     * @param objectTypeCode      The Participant Object Type Code
     * @param objectTypeCodeRole  The Participant Object Type Code's ROle
     * @param objectDataLifeCycle The Participant Object Data Life Cycle
     * @param objectSensitivity   The Participant Object sensitivity
     * @return this
     */
    public T addParticipantObjectIdentification(ParticipantObjectIdType objectIDTypeCode,
                                                String objectName,
                                                byte[] objectQuery,
                                                List<TypeValuePairType> objectDetails,
                                                String objectID,
                                                ParticipantObjectTypeCode objectTypeCode,
                                                ParticipantObjectTypeCodeRole objectTypeCodeRole,
                                                ParticipantObjectDataLifeCycle objectDataLifeCycle,
                                                String objectSensitivity) {
        ParticipantObjectIdentificationType poi = new ParticipantObjectIdentificationType(objectID, objectIDTypeCode);

        poi.setParticipantObjectName(objectName);
        poi.setParticipantObjectQuery(objectQuery);
        if (objectDetails != null) {
            objectDetails.stream()
                    .filter(Objects::nonNull)
                    .forEach(objectDetail -> poi.getParticipantObjectDetails().add(objectDetail));
        }
        poi.setParticipantObjectTypeCode(objectTypeCode);
        poi.setParticipantObjectTypeCodeRole(objectTypeCodeRole);
        poi.setParticipantObjectDataLifeCycle(objectDataLifeCycle);
        poi.setParticipantObjectSensitivity(objectSensitivity);
        auditMessage.getParticipantObjectIdentifications().add(poi);
        return self();
    }


    protected NetworkAccessPointTypeCode getNetworkAccessPointCodeFromAddress(String address) {
        if (address == null) {
            return null;
        }
        return IPV4.matcher(address).matches() || IPV6.matcher(address).matches() ?
                NetworkAccessPointTypeCode.IPAddress :
                NetworkAccessPointTypeCode.MachineName;
    }

    /**
     * Create and set a Type Value Pair instance for a given type and value
     *
     * @param type  The type to set
     * @param value The value to set
     * @return The Type Value Pair instance
     */
    public TypeValuePairType getTypeValuePair(String type, Object value) {
        return new TypeValuePairType(requireNonNull(type), requireNonNull(value).toString());
    }



}
