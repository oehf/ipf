/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.SneakyThrows;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.SelfInitializing;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpAuditEventHelper;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpQueryAuditEvent;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.PatientQueryAuditEvent;

import java.io.IOException;
import java.io.Writer;
import java.sql.Date;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.audit.types.CodedValueType.CODE_SYSTEM_NAME_DCM;
import static org.openehealth.ipf.commons.audit.types.CodedValueType.CODE_SYSTEM_NAME_EHS;
import static org.openehealth.ipf.commons.audit.types.CodedValueType.CODE_SYSTEM_NAME_IHE_TRANSACTIONS;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.*;

/**
 * {@link SerializationStrategy} that renders an ATNA {@link AuditMessage} as a FHIR R4
 * {@link AuditEvent} resource rather than as a DICOM audit record. This is what an audit record
 * repository speaking FHIR expects, and the prerequisite for the audit records of a transaction to
 * conform to <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a> and to the AuditEvent
 * profiles the IHE transactions build on it. Configure an instance of a subclass as the
 * {@code serializationStrategy} of the audit context; the transactions themselves are unaffected, as
 * they keep building the same audit messages either way.
 * <p>
 * Two kinds of AuditEvent come out of here: a transaction whose IHE profile defines an AuditEvent of
 * its own has it converted by that AuditEvent (see {@link SelfInitializing}), everything else by the
 * generic element-by-element translation of {@link #translate(AuditMessage)}. Note that neither is
 * validated against the profile it claims -- what the translation produces is only as conformant as
 * the audit message it was given.
 * <p>
 * A subclass decides nothing but the encoding, by choosing the {@link IParser}.
 *
 * @author Christian Ohr
 * @since 3.6
 * @see BalpXmlSerializationStrategy
 * @see BalpJsonSerializationStrategy
 */
abstract class AbstractFhirAuditSerializationStrategy implements SerializationStrategy {

    private final FhirContext fhirContext;

    /**
     * Uses a newly created R4 {@link FhirContext}. Prefer {@link #AbstractFhirAuditSerializationStrategy(FhirContext)}
     * whenever the application has one already: a FhirContext is expensive to create and meant to be shared.
     */
    public AbstractFhirAuditSerializationStrategy() {
        this(FhirContext.forR4());
    }

    /**
     * @param fhirContext the FhirContext whose parser serializes the AuditEvents. Must be an R4 context.
     */
    public AbstractFhirAuditSerializationStrategy(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    /**
     * Translates the audit message into an {@link AuditEvent} and writes it out in this strategy's encoding.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @param writer       where to write the serialized AuditEvent to
     * @param pretty       whether to pretty-print the output
     * @throws IOException if writing fails
     */
    @Override
    public void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) throws IOException {
        getParser(fhirContext)
            .setPrettyPrint(pretty)
            .encodeResourceToWriter(translate(auditMessage), writer);
    }

    /**
     * @param fhirContext the FhirContext to obtain the parser from
     * @return the parser determining the encoding of the serialized AuditEvent
     */
    protected abstract IParser getParser(FhirContext fhirContext);

    /**
     * Converts an ATNA audit message into the AuditEvent resource representing it, either through the
     * AuditEvent the audited transaction's profile defines or, failing that, generically.
     *
     * @param auditMessage the audit message of the transaction being audited
     * @return the AuditEvent representing it
     */
    public AuditEvent translate(AuditMessage auditMessage) {
        var eit = auditMessage.getEventIdentification();

        // A transaction that is profiled in FHIR gets its own AuditEvent, which fills itself in from the
        // audit message -- but only if that AuditEvent can represent the message conformantly. What it
        // cannot represent is stepped down rather than mislabelled: to the BALP pattern the transaction
        // profile derives from where there is one, and to the generic translation below otherwise.
        var profiledAuditEvent = makeAuditEventInstance(eit, auditMessage.isServerSide());
        var selfInitializing = selfInitializingFor(profiledAuditEvent, auditMessage);
        if (selfInitializing != null) {
            selfInitializing.initialize(auditMessage);
            return (AuditEvent) selfInitializing;
        }

        var auditEvent = new AuditEvent();
        // Otherwise we do some generic transformation
        auditEvent
            .setAction(getAuditEventAction(eit.getEventActionCode()))
            .setRecorded(Date.from(eit.getEventDateTime()))
            .setOutcome(getAuditEventOutcome(eit.getEventOutcomeIndicator()))
            .setOutcomeDesc(eit.getEventOutcomeDescription());
        if (CODE_SYSTEM_NAME_DCM.equals(eit.getEventID().getCodeSystemName())) {
            auditEvent.setType(codedValueTypeToCoding(eit.getEventID(), DCM_SYSTEM_NAME));
        } else {
            auditEvent.setType(codedValueTypeToCoding(eit.getEventID()));
        }
        eit.getEventTypeCode().forEach(etc -> {
            if (CODE_SYSTEM_NAME_IHE_TRANSACTIONS.equals(etc.getCodeSystemName())) {
                auditEvent.addSubtype(codedValueTypeToCoding(etc, IHE_SYSTEM_NAME));
            } else if (CODE_SYSTEM_NAME_EHS.equals(etc.getCodeSystemName())) {
                auditEvent.addSubtype(codedValueTypeToCoding(etc, EHS_SYSTEM_NAME));
            } else {
                auditEvent.addSubtype(codedValueTypeToCoding(etc));
            }
        });
        eit.getPurposesOfUse().forEach(pou ->
            auditEvent.addPurposeOfEvent(codedValueTypeToCodeableConcept(pou)));

        auditMessage.getActiveParticipants().forEach(ap ->
            auditEvent.addAgent(activeParticipantToAgent(ap)));

        auditEvent.setSource(auditSourceIdentificationToEventSource(auditMessage.getAuditSourceIdentification()));

        auditMessage.getParticipantObjectIdentifications().forEach(poit ->
            auditEvent.addEntity(participantObjectIdentificationToEntity(poit)));
        return auditEvent;
    }

    /**
     * Picks the AuditEvent that is to convert the audit message itself: the one the transaction's profile
     * defines, or, when that one cannot represent this particular message, the BALP pattern it derives
     * from without the constraint it fails.
     * <p>
     * Today the only such step-down is the patient: the query patterns the IHE transactions build on
     * require a patient entity, and a query that identifies none is audited as the plain BALP query
     * pattern instead. A message that not even that can represent -- a failed transaction, say -- returns
     * null and stays on the generic translation.
     *
     * @param profiledAuditEvent the AuditEvent the transaction's profile defines
     * @param auditMessage       the audit message of the transaction being audited
     * @return the AuditEvent to convert the message, or null to translate it generically
     */
    private SelfInitializing selfInitializingFor(AuditEvent profiledAuditEvent, AuditMessage auditMessage) {
        if (profiledAuditEvent instanceof SelfInitializing selfInitializing) {
            if (selfInitializing.supports(auditMessage)) {
                return selfInitializing;
            }
            if (profiledAuditEvent instanceof PatientQueryAuditEvent) {
                var withoutPatient = new BalpQueryAuditEvent();
                if (withoutPatient.supports(auditMessage)) {
                    return withoutPatient;
                }
            }
        }
        return null;
    }

    /**
     * Instantiates the AuditEvent that the audited transaction's profile defines for the end of the
     * transaction the audit source is on. Which transaction that is is taken from the first
     * {@link FhirEventTypeCode} among the event type codes; a transaction without one, or one whose
     * profile does not define an AuditEvent, falls back to a plain {@link AuditEvent}.
     *
     * @param eventIdentification event identification of the audit message
     * @param serverSide          whether the audit message was recorded by the server of the transaction
     * @return a new AuditEvent instance, never null
     */
    private AuditEvent makeAuditEventInstance(EventIdentificationType eventIdentification, boolean serverSide) {
        return eventIdentification.getEventTypeCode().stream()
            .filter(eventType -> eventType instanceof FhirEventTypeCode)
            .findFirst()
            .map(FhirEventTypeCode.class::cast)
            .map(fetc -> serverSide ?
                fetc.getServerEventClassName() :
                fetc.getClientEventClassName())
            .map(this::auditEventInstance)
            .orElseGet(AuditEvent::new);
    }

    /**
     * Returns a new instance of {@link AuditEvent}. The class is named rather than referenced because
     * the transaction-specific AuditEvents live in the modules of their transactions, which this module
     * is a dependency of and not the other way round.
     *
     * @param auditEventClassName fully qualified name of an {@link AuditEvent} subclass with a public
     *                            no-arg constructor, or null
     * @return AuditEvent instance matching the FHIR transaction, or a plain {@link AuditEvent} if no
     *      class name was given
     * @throws ClassCastException on misconfiguration
     */
    @SneakyThrows
    public AuditEvent auditEventInstance(String auditEventClassName) {
        if (auditEventClassName == null) {
            return new AuditEvent();
        }
        var auditEventClass = (Class<? extends AuditEvent>)Class.forName(auditEventClassName);
        return auditEventClass
            .getConstructor()
            .newInstance();
    }

    /**
     * Converts a participant object of the audit message into an entity of the AuditEvent. The
     * participant object ID becomes {@code entity.what}, as a literal reference if it reads like one
     * and as an identifier otherwise.
     *
     * @param poit participant object identification
     * @return the corresponding entity
     */
    protected AuditEvent.AuditEventEntityComponent participantObjectIdentificationToEntity(ParticipantObjectIdentificationType poit) {
        var entity = new AuditEvent.AuditEventEntityComponent()
            // poit.getParticipantObjectIDTypeCode())) not used here
            .setType(codeToCoding(AUDIT_ENTITY_SYSTEM_NAME, poit.getParticipantObjectTypeCode(), ParticipantObjectTypeCode::getValue))
            .setRole(codeToCoding(OBJECT_ROLE_SYSTEM_NAME, poit.getParticipantObjectTypeCodeRole(), ParticipantObjectTypeCodeRole::getValue))
            .setLifecycle(codeToCoding(AUDIT_LIFECYCLE_SYSTEM_NAME, poit.getParticipantObjectDataLifeCycle(), ParticipantObjectDataLifeCycle::getValue))
            .addSecurityLabel(codeToCoding(null, poit.getParticipantObjectSensitivity(), Function.identity()))
            .setName(poit.getParticipantObjectName())
            // .setDescription(poit.getParticipantObjectDescriptions().isEmpty() ? null : poit.getParticipantObjectDescriptions().get(0).toString())
            .setQuery(poit.getParticipantObjectQuery());

        poit.getParticipantObjectDetails().forEach(tvp ->
            entity.addDetail(new AuditEvent.AuditEventEntityDetailComponent()
                .setType(tvp.getType())
                .setValue(new Base64BinaryType(tvp.getValue()))));
        if (isNotBlank(poit.getParticipantObjectID())) {
            var whatReference = new Reference(poit.getParticipantObjectID());
            if (whatReference.getReferenceElement().hasResourceType()) {
                // participantObjectID is a FHIR resource reference, let's use it
                entity.setWhat(whatReference);
            } else {
                entity.setWhat(new Reference().setIdentifier(
                    new Identifier().setValue(poit.getParticipantObjectID())));
            }
        }
        return entity;
    }

    /**
     * Converts the audit source identification of the audit message into {@code AuditEvent.source}.
     *
     * @param asit audit source identification
     * @return the corresponding source component
     */
    protected AuditEvent.AuditEventSourceComponent auditSourceIdentificationToEventSource(AuditSourceIdentificationType asit) {
        var source = new AuditEvent.AuditEventSourceComponent()
            .setSite(asit.getAuditEnterpriseSiteID())
            .setObserver(new Reference().setDisplay(asit.getAuditSourceID()));
        asit.getAuditSourceType().forEach(ast ->
            source.addType(codedValueTypeToCoding(ast, SECURITY_SOURCE_SYSTEM_NAME)));
        return source;
    }

    /**
     * Converts an active participant of the audit message into an agent of the AuditEvent. An active
     * participant whose role ID codes encode the claims of an access token becomes the OAuth agent BALP
     * defines for it; everything else is mapped element by element.
     *
     * @param ap active participant
     * @return the corresponding agent
     */
    protected AuditEvent.AuditEventAgentComponent activeParticipantToAgent(ActiveParticipantType ap) {
        var oAuthEventAgent = BalpAuditEventHelper.oAuthActiveParticipantToAgent(ap);
        return oAuthEventAgent.orElseGet(() -> new AuditEvent.AuditEventAgentComponent()
            .setType(ap.getRoleIDCodes().isEmpty() ? null : codedValueTypeToCodeableConcept(ap.getRoleIDCodes().get(0), DCM_SYSTEM_NAME))
            .setWho(new Reference().setDisplay(ap.getUserID()))
            .setAltId(ap.getAlternativeUserID())
            .setName(ap.getUserName())
            .setRequestor(ap.isUserIsRequestor())
            .setMedia(codedValueTypeToCoding(ap.getMediaType()))
            .setNetwork(new AuditEvent.AuditEventAgentNetworkComponent()
                .setAddress(ap.getNetworkAccessPointID())
                .setType(auditEventNetworkType(ap.getNetworkAccessPointTypeCode()))));
    }

    /**
     * @param naptc network access point type code, may be null
     * @return the corresponding {@code AuditEvent.agent.network.type}, or null
     */
    protected AuditEvent.AuditEventAgentNetworkType auditEventNetworkType(NetworkAccessPointTypeCode naptc) {
        try {
            return naptc != null?
                AuditEvent.AuditEventAgentNetworkType.fromCode(String.valueOf(naptc.getValue())) : null;
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    /**
     * @param eventOutcomeIndicator outcome of the audited event
     * @return the corresponding {@code AuditEvent.outcome}
     */
    protected AuditEvent.AuditEventOutcome getAuditEventOutcome(EventOutcomeIndicator eventOutcomeIndicator) {
        try {
            return AuditEvent.AuditEventOutcome.fromCode(String.valueOf(eventOutcomeIndicator.getValue()));
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    /**
     * @param eventActionCode action code of the audited event
     * @return the corresponding {@code AuditEvent.action}
     */
    protected AuditEvent.AuditEventAction getAuditEventAction(EventActionCode eventActionCode) {
        try {
            return AuditEvent.AuditEventAction.fromCode(eventActionCode.getValue());
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    /**
     * Builds a Coding from an arbitrary code object by extracting its value.
     *
     * @param codeSystem    system of the resulting Coding
     * @param code          the code object, may be null
     * @param valueSupplier extracts the code value from it
     * @param <T>           type of the code object
     * @param <V>           type of the extracted value
     * @return the resulting Coding, or null if no code was given
     */
    protected <T, V> Coding codeToCoding(String codeSystem, T code, Function<T, V> valueSupplier) {
        return (code != null) ?
            new Coding()
                .setCode(String.valueOf(valueSupplier.apply(code)))
                .setSystem(codeSystem) :
            null;
    }

    /**
     * @param cvt coded value, may be null
     * @return the corresponding Coding using the coded value's own code system name, or null
     */
    protected Coding codedValueTypeToCoding(CodedValueType cvt) {
        return cvt != null ?
            codedValueTypeToCoding(cvt, cvt.getCodeSystemName()) :
            null;
    }

    /**
     * The display of the resulting Coding is the coded value's original text, which is where IPF keeps
     * the human readable name of a code.
     *
     * @param cvt        coded value, may be null
     * @param codeSystem system of the resulting Coding, typically the URI the code system name maps to
     * @return the corresponding Coding, or null
     */
    protected Coding codedValueTypeToCoding(CodedValueType cvt, String codeSystem) {
        return cvt != null ?
            new Coding(codeSystem,
                cvt.getCode(),
                cvt.getOriginalText()) :
            null;
    }

    /**
     * @param cvt coded value, may be null
     * @return a CodeableConcept with the coded value as its only Coding, or null
     */
    protected CodeableConcept codedValueTypeToCodeableConcept(CodedValueType cvt) {
        return cvt != null ?
            codedValueTypeToCodeableConcept(cvt, cvt.getCodeSystemName()) :
            null;
    }

    /**
     * @param cvt        coded value, may be null
     * @param codeSystem system of the resulting Coding
     * @return a CodeableConcept with the coded value as its only Coding, or null
     */
    protected CodeableConcept codedValueTypeToCodeableConcept(CodedValueType cvt, String codeSystem) {
        return cvt != null ?
            new CodeableConcept().addCoding(codedValueTypeToCoding(cvt, codeSystem)) :
            null;
    }

}
