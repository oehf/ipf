/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditEntityType;
import org.hl7.fhir.r4.model.codesystems.AuditSourceType;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;

import java.util.Date;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.AUDIT_ENTITY_TYPE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DATA_ABSENT_REASON_EXTENSION;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DATA_ABSENT_REASON_UNKNOWN;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_OCLIENT_CODE;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.DCM_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_ROLE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_TYPE_CODE;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.USER_OAUTH_AGENT_CODE;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.USER_AGENT_TYPES_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.OUSER_AGENT_TYPE_SYSTEM_NAME;

/**
 * Base class of the AuditEvents built along the conventions of
 * <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a>: the agent slices for the client
 * and the server of a transaction, the user agent of the access token it carried, the audit source, and
 * the entities for the patient, the data and the query. Each of those is a concept the BALP
 * StructureDefinitions share; what differs between the patterns is which code a slice is fixed to, and
 * that is what a subclass declares.
 * <p>
 * A subclass exposes the concepts under the names its own profile uses -- {@code setClient} /
 * {@code setServer} for the RESTful patterns, {@code setSource} / {@code setRecipient} for the privacy
 * disclosure ones -- and builds them with the {@code add…} methods here, so that a record populated by
 * hand and one populated from an {@link AuditMessage} come out the same.
 * <p>
 * Every pattern is {@link SelfInitializing}: it can convert an ATNA audit record of its own shape into
 * itself. That conversion runs the same way for all of them, so {@link #initialize(AuditMessage)} is
 * final here and a pattern contributes what is specific about it by overriding
 * {@link #initializeFrom(AuditMessage, ActiveParticipantRoleIdCode)} -- which is not defaulted to a
 * no-op by accident: {@code initialize} is called <em>instead of</em> the generic
 * AuditMessage-to-AuditEvent translation, so a pattern that added nothing of its own would emit a record
 * without the interaction subtype, the data entity or the patient its profile requires.
 *
 * @author Christian Ohr
 * @since 5.3
 */
public abstract class BalpAuditEvent extends AuditEvent implements SelfInitializing {

    /**
     * The client of a transaction is the ATNA source, and its server the ATNA destination. That holds
     * whichever end records the event, so it is not something a profile gets to fix -- only the code of
     * the agent slice the participant goes into is, see {@link #clientAgentType()}.
     */
    private static final ActiveParticipantRoleIdCode CLIENT_PARTICIPANT = ActiveParticipantRoleIdCode.Source;
    private static final ActiveParticipantRoleIdCode SERVER_PARTICIPANT = ActiveParticipantRoleIdCode.Destination;

    /**
     * Converts an ATNA audit record into this pattern: the transaction subtype the record names, and then
     * everything else through {@link #initializeFrom(AuditMessage, ActiveParticipantRoleIdCode)}.
     * <p>
     * This is the same for every pattern, which is why it is fixed here. A pattern says what is specific
     * about it by overriding {@code initializeFrom} -- and, if, which end of the transaction it records
     * does not follow from the audit message, {@link #localRole(AuditMessage)}.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    @Override
    public final void initialize(AuditMessage auditMessage) {
        addTransactionSubtype(auditMessage);
        initializeFrom(auditMessage, localRole(auditMessage));
    }

    /**
     * Which end of the transaction recorded this event, and therefore which agent the audit source has to
     * mirror -- BALP states that as the {@code val-audit-source} invariant on the local actor's slice.
     * <p>
     * For the RESTful patterns it follows from the audit message: the end that recorded it is the client
     * or the server of the transaction. The privacy disclosure patterns fix it instead, being by
     * definition the record of one particular side of the disclosure.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @return the participant standing for the actor that recorded the event
     */
    protected ActiveParticipantRoleIdCode localRole(AuditMessage auditMessage) {
        return auditMessage.isServerSide() ? SERVER_PARTICIPANT : CLIENT_PARTICIPANT;
    }

    /**
     * Fills in everything every BALP pattern takes from the audit record the same way: when the event
     * happened, the client and server agents, the user agents, the audit source, and the entity carrying
     * the request id that correlates the two ends of the transaction.
     * <p>
     * What it deliberately leaves to the pattern is the part the profile fixes differently: the RESTful
     * interaction subtype next to the transaction subtype, the entity for the data the transaction acted
     * on, and the patient. A pattern overrides this, calls {@code super}, and adds those around it.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @param localRole    which end of the transaction recorded it, see {@link #localRole(AuditMessage)}
     */
    protected void initializeFrom(AuditMessage auditMessage, ActiveParticipantRoleIdCode localRole) {
        // the time the event happened, as opposed to the time this resource was built
        setRecorded(Date.from(auditMessage.getEventIdentification().getEventDateTime()));

        setClientAndServer(auditMessage);
        addUserAgents(auditMessage);
        setAuditSource(auditMessage, localRole);

        BalpAuditEventHelper.requestId(auditMessage).ifPresent(this::addTransactionEntity);
    }

    /**
     * @return the type this profile fixes for the agent slice of the transaction's client. The BALP
     * query, create and update patterns use DICOM 110153, the read patterns 110152.
     */
    protected abstract Coding clientAgentType();

    /**
     * @return the type this profile fixes for the agent slice of the transaction's server. Usually the
     * counterpart of {@link #clientAgentType()}, but not necessarily a DICOM code -- the delete
     * pattern names the server a custodian instead.
     */
    protected abstract Coding serverAgentType();

    // ------------------------------------------------------------------------------------- agents

    /**
     * The display matters: the BALP patterns leave it open, but a transaction profile may fix the agent
     * slice as a pattern of system, code <em>and</em> display, and a coding without one does not match
     * such a slice. It comes from the original text of the coded value, which is where IPF keeps the
     * human readable name of a code -- {@code getDisplayName()} is null for these codes.
     *
     * @param roleCode DICOM role of an active participant
     * @return the agent slice type for it, as the BALP patterns spell it out
     */
    protected static Coding dicomAgentType(ActiveParticipantRoleIdCode roleCode) {
        return new Coding()
            .setCode(roleCode.getCode())
            .setSystem(DCM_SYSTEM_NAME)
            .setDisplay(roleCode.getOriginalText());
    }

    /**
     * Adds an agent built from an active participant of an ATNA audit record. In contrast to
     * {@link #addAgent(Coding, Reference, String, AuditEventAgentNetworkType)} this keeps the
     * participant's {@code userIsRequestor} flag, its alternative user id and its user name rather than
     * assuming values for them.
     *
     * @param agentType         type of the agent slice, as the profile fixes it
     * @param activeParticipant active participant to build the agent from
     * @return the new agent
     */
    protected AuditEventAgentComponent addAgent(Coding agentType, ActiveParticipantType activeParticipant) {
        return addAgent(agentType,
            new Reference().setDisplay(activeParticipant.getUserID()),
            activeParticipant.getNetworkAccessPointID(),
            BalpAuditEventHelper.networkType(activeParticipant))
            .setRequestor(activeParticipant.isUserIsRequestor())
            .setAltId(activeParticipant.getAlternativeUserID())
            .setName(activeParticipant.getUserName());
    }

    /**
     * Adds an agent from what a caller knows about it. The requestor flag is left at false; use
     * {@link #addAgent(Coding, ActiveParticipantType)} whenever an ATNA record says otherwise.
     *
     * @param agentType      type of the agent slice, as the profile fixes it
     * @param who            who the agent is (can be display only)
     * @param networkAddress network address, may be null
     * @param networkType    network type, may be null
     * @return the new agent
     */
    protected AuditEventAgentComponent addAgent(Coding agentType,
                                                Reference who,
                                                String networkAddress,
                                                AuditEventAgentNetworkType networkType) {
        return addAgent()
            .setType(new CodeableConcept().addCoding(agentType))
            .setWho(who)
            .setRequestor(false)
            .setNetwork(new AuditEventAgentNetworkComponent()
                .setType(networkType)
                .setAddress(networkAddress));
    }

    /**
     * Adds the client and the server agent, taken from the active participants of an ATNA audit record.
     * Which type each slice carries is this profile's business ({@link #clientAgentType()},
     * {@link #serverAgentType()}); where to find the participants is not, because the client of a
     * transaction is always recorded as the ATNA source and the server as the ATNA destination.
     * <p>
     * Either participant may be missing: an audit dataset is not guaranteed to know both ends of the
     * transaction, and an audit record without one of the agents is still better than none at all.
     *
     * @param auditMessage audit message to take the participants from
     * @return this instance
     */
    public BalpAuditEvent setClientAndServer(AuditMessage auditMessage) {
        BalpAuditEventHelper.findActiveParticipant(auditMessage, CLIENT_PARTICIPANT)
            .ifPresent(client -> addAgent(clientAgentType(), client));
        BalpAuditEventHelper.findActiveParticipant(auditMessage, SERVER_PARTICIPANT)
            .ifPresent(server -> addAgent(serverAgentType(), server));
        return this;
    }

    /**
     * Adds the user agent, which every BALP pattern allows next to the client and the server.
     *
     * @param typeCode      participation type of the user
     * @param userReference who the user is (can be display only)
     * @return the new agent
     */
    protected AuditEventAgentComponent addUserAgent(V3ParticipationType typeCode, Reference userReference) {
        return addAgent()
            .setType(new CodeableConcept()
                .addCoding(new Coding()
                    .setCode(typeCode.toCode())
                    .setSystem(typeCode.getSystem())))
            .setWho(userReference)
            .setRequestor(true);
    }

    /**
     * Adds the user agent, and where applicable the OAuth client agent, of the access token the audited
     * request carried. The transaction profiles allow both next to the client and the server.
     * <p>
     * When the audit message carries the token's claims, they are mapped directly. That is the whole
     * point of keeping them: the ATNA participants the claims were encoded into cannot be decoded
     * unambiguously, because the subject organization and the client id are tagged with the same code
     * system and are told apart only by a piece of original text. Audit messages built elsewhere carry
     * no claims, and fall back to reading the participants.
     *
     * @param auditMessage audit message of the transaction being audited
     * @return this instance
     */
    public BalpAuditEvent addUserAgents(AuditMessage auditMessage) {
        var claims = auditMessage.getJwtDataSet();
        if (claims == null) {
            auditMessage.getActiveParticipants().forEach(activeParticipant ->
                BalpAuditEventHelper.oAuthActiveParticipantToAgent(activeParticipant)
                    .ifPresent(agent -> getAgent().add(agent)));
            return this;
        }
        if (isNotBlank(claims.getJwtId())) {
            var agent = addAgent()
                .setType(codeableConcept(OUSER_AGENT_TYPE_SYSTEM_NAME, OUSER_AGENT_TYPE_CODE, "information recipient"))
                .addPolicy(claims.getJwtId())
                .setName(claims.getIheIuaSubjectName())
                .setWho(new Reference()
                    .setIdentifier(new Identifier()
                        .setSystem(claims.getIssuer())
                        .setValue(claims.getSubject()))
                    .setDisplay(claims.getIheIuaSubjectName()))
                .setRequestor(true);
            if (claims.getIheIuaPurposeOfUse() != null) {
                claims.getIheIuaPurposeOfUse().forEach(purpose -> agent.getPurposeOfUse().add(
                    codeableConcept(OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME, purpose, purpose)));
            }
            if (claims.getIheIuaSubjectRole() != null) {
                claims.getIheIuaSubjectRole().forEach(role -> agent.getRole().add(
                    codeableConcept(OUSER_AGENT_ROLE_SYSTEM_NAME, role, role)));
            }
        } else if (claims.isOpaque()) {
            // IHE.BasicAudit.OAUTHaccessTokenUse.Opaque: a token was presented, its contents were not
            // visible to the audit source. There is no jti to put into agent.policy and no subject to
            // name, and the token itself is a credential that does not belong in an audit record.
            addAgent()
                .setType(codeableConcept(USER_AGENT_TYPES_SYSTEM_NAME, USER_OAUTH_AGENT_CODE,
                    "User OAuth Agent participant"))
                .setRequestor(true);
        }
        if (isNotBlank(claims.getClientId())) {
            addAgent()
                .setType(codeableConcept(DCM_SYSTEM_NAME, DCM_OCLIENT_CODE, "Application"))
                .setWho(new Reference().setIdentifier(new Identifier().setValue(claims.getClientId())))
                .setRequestor(!auditMessage.isServerSide());
        }
        return this;
    }

    // ------------------------------------------------------------------------------- audit source

    /**
     * Sets the audit source, i.e. what BALP calls the identity of the source detecting the event.
     * <p>
     * The transaction profiles do not leave the observer free: an invariant on the agent slice of the
     * end that wrote the record -- {@code val-audit-source}, "the Audit Source is this agent too" --
     * requires {@code source.observer} to be that very agent. So the observer is named after the local
     * participant, exactly as {@link #setClientAndServer(AuditMessage)} names the agent, and the two
     * references come out identical.
     * <p>
     * The audit source id of the ATNA record is the fallback for an audit message that does not name the
     * local participant, since the profiles require an observer either way. It cannot be the primary:
     * it identifies the application writing the record rather than its end of the transaction, so
     * using it would break the invariant for every deployment that configures one.
     *
     * @param auditMessage audit message of the transaction being audited
     * @param localRole    which end of the transaction the audit source is
     * @return this instance
     */
    public BalpAuditEvent setAuditSource(AuditMessage auditMessage, ActiveParticipantRoleIdCode localRole) {
        var auditSourceIdentification = auditMessage.getAuditSourceIdentification();
        var source = getSource()
            .setSite(auditSourceIdentification.getAuditEnterpriseSiteID());
        source.addType()
            .setCode(AuditSourceType._4.toCode())
            .setSystem(AuditSourceType._4.getSystem())
            .setDisplay(AuditSourceType._4.getDisplay());
        BalpAuditEventHelper.findActiveParticipant(auditMessage, localRole)
            .map(ActiveParticipantType::getUserID)
            .filter(StringUtils::isNotBlank)
            .or(() -> Optional.of(auditSourceIdentification.getAuditSourceID())
                .filter(StringUtils::isNotBlank))
            .ifPresent(display -> source.setObserver(new Reference().setDisplay(display)));
        return this;
    }

    // ----------------------------------------------------------------------------------- entities

    /**
     * Adds the patient entity, which the Patient* variant of every BALP pattern requires.
     *
     * @param patientReference who the patient is (can be display only)
     * @return the new entity
     */
    protected AuditEventEntityComponent addPatientEntity(Reference patientReference) {
        return addEntity(AuditEntityType._1, ObjectRole._1)
            .setWhat(patientReference);
    }

    /**
     * Adds the patient entity if the audit message names a patient, and does nothing otherwise.
     * <p>
     * This is for the profiles whose patient entity is optional -- both PDQm transactions, for instance,
     * derive from the plain query pattern but say the patient entity "SHOULD be used when one patient is
     * explicitly identified in the query parameters". Where the entity is mandatory instead, use
     * {@link #addRequiredPatientEntity(AuditMessage)}.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    protected void addPatientEntityIfPresent(AuditMessage auditMessage) {
        BalpAuditEventHelper.patientReference(auditMessage).ifPresent(this::addPatientEntity);
    }

    /**
     * Adds the patient entity that the Patient* variant of every BALP pattern requires, recording the
     * patient as explicitly unknown when the audit message names none.
     * <p>
     * Some transactions cannot always name a patient even though their profile insists on one -- Retrieve
     * Document [ITI-68] never can, as it downloads a binary from a URL. Rather than drop such a record to
     * a weaker profile, it keeps the profile of its transaction and marks the reference absent with the
     * FHIR {@code data-absent-reason} extension, which is the standard way of saying that a required
     * value is expected to exist but is not known. The record then still validates against the profile
     * the transaction prescribes, and says plainly that the patient could not be determined.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    protected void addRequiredPatientEntity(AuditMessage auditMessage) {
        addPatientEntity(BalpAuditEventHelper.patientReference(auditMessage)
            .orElseGet(BalpAuditEvent::unknownPatient));
    }

    /**
     * @return a reference to a patient that is expected to exist but could not be determined
     */
    private static Reference unknownPatient() {
        var reference = new Reference();
        reference.addExtension(DATA_ABSENT_REASON_EXTENSION, new CodeType(DATA_ABSENT_REASON_UNKNOWN));
        return reference;
    }

    /**
     * Adds the entity for the data the transaction acted on.
     *
     * @param entity     what the data is (can be display only)
     * @param entityRole role of the data: report, domain resource or job
     * @return the new entity
     * @throws IllegalArgumentException if the role is none of those three
     */
    protected AuditEventEntityComponent addDataEntity(Reference entity, ObjectRole entityRole) {
        if (entityRole != ObjectRole._3 &&
            entityRole != ObjectRole._4 &&
            entityRole != ObjectRole._20) {
            throw new IllegalArgumentException("Must be object role report, domain resource or job");
        }
        return addEntity(AuditEntityType._2, entityRole)
            .setWhat(entity);
    }

    /**
     * Adds the query entity, which every BALP query pattern requires.
     *
     * @param query        raw query. {@code entity.query} is a base64Binary element, so FHIR encodes it
     *                     on serialization -- encoding it here as well would leave the recipient with
     *                     base64 text instead of the query.
     * @param cleanedQuery optional cleaned query, kept as readable text in {@code entity.description}
     * @return the new entity
     */
    protected AuditEventEntityComponent addQueryEntity(byte[] query, String cleanedQuery) {
        return addEntity(AuditEntityType._2, ObjectRole._24)
            .setQuery(query)
            .setDescription(cleanedQuery);
    }

    /**
     * Adds the entity carrying the X-Request-Id that correlates the audit records of the two ends of a
     * transaction.
     *
     * @param xRequestId value of the X-Request-Id header
     * @return the new entity
     */
    protected AuditEventEntityComponent addTransactionEntity(String xRequestId) {
        return addEntity()
            .setWhat(new Reference()
                .setIdentifier(new Identifier()
                    .setValue(xRequestId)))
            .setType(new Coding()
                .setCode("XrequestId")
                .setSystem(AUDIT_ENTITY_TYPE_SYSTEM_NAME));
    }

    /**
     * @param type entity type
     * @param role entity role
     * @return a new entity with both codes spelled out as the profiles require
     */
    protected AuditEventEntityComponent addEntity(AuditEntityType type, ObjectRole role) {
        return addEntity()
            .setType(new Coding()
                .setCode(type.toCode())
                .setSystem(type.getSystem())
                .setDisplay(type.getDisplay()))
            .setRole(new Coding()
                .setCode(role.toCode())
                .setSystem(role.getSystem())
                .setDisplay(role.getDisplay()));
    }

    // ------------------------------------------------------------------------------------ subtype

    /**
     * Adds the subtype naming the IHE transaction, which the transaction profiles fix as a pattern of
     * system, code <em>and</em> display -- a subtype without a display does not match it. The display
     * comes from the original text of the coded value, which is where IPF keeps the human readable name
     * of a code; {@code getDisplayName()} is null for these codes and must not be used.
     *
     * @param transaction the IHE transaction this audit event is about
     */
    protected final void addTransactionSubtype(EventType transaction) {
        addSubtype()
            .setCode(transaction.getCode())
            .setSystem(transaction.getCodeSystemName())
            .setDisplay(transaction.getOriginalText());
    }

    /**
     * Adds the subtype naming the IHE transaction the audit message is about, unless it is already
     * there. A transaction-specific subclass fixes it in its constructor, because that is what its
     * profile fixes; a record converted through a bare BALP pattern has no such subclass and takes it
     * from the message instead.
     *
     * @param auditMessage the audit message of the transaction being audited
     */
    protected final void addTransactionSubtype(AuditMessage auditMessage) {
        transactionOf(auditMessage)
            .filter(transaction -> getSubtype().stream()
                .noneMatch(subtype -> transaction.getCode().equals(subtype.getCode())))
            .ifPresent(this::addTransactionSubtype);
    }

    /**
     * @param auditMessage the audit message of the transaction being audited
     * @return the IHE transaction it is about, if it names one
     */
    protected static Optional<FhirEventTypeCode> transactionOf(AuditMessage auditMessage) {
        return auditMessage.getEventIdentification().getEventTypeCode().stream()
            .filter(FhirEventTypeCode.class::isInstance)
            .map(FhirEventTypeCode.class::cast)
            .findFirst();
    }


    private static CodeableConcept codeableConcept(String system, String code, String display) {
        return new CodeableConcept().addCoding(new Coding(system, code, display));
    }

}
