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
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.CodedValueType;

import java.io.IOException;
import java.io.Writer;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.*;

/**
 * @author Christian Ohr
 * @since 3.6
 */
abstract class AbstractFhirAuditSerializationStrategy implements SerializationStrategy {

    private final FhirContext fhirContext;

    public AbstractFhirAuditSerializationStrategy() {
        this(FhirContext.forR4());
    }

    public AbstractFhirAuditSerializationStrategy(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    public void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) throws IOException {
        getParser(fhirContext)
            .setPrettyPrint(pretty)
            .encodeResourceToWriter(translate(auditMessage), writer);
    }

    protected abstract IParser getParser(FhirContext fhirContext);

    public AuditEvent translate(AuditMessage auditMessage) {
        var eit = auditMessage.getEventIdentification();
        var auditEvent = new AuditEvent()
            .setType(codedValueTypeToCoding(eit.getEventID(), DCM_SYSTEM_NAME))
            .setAction(getAuditEventAction(eit.getEventActionCode()))
            .setRecorded(Date.from(eit.getEventDateTime()))
            .setOutcome(getAuditEventOutcome(eit.getEventOutcomeIndicator()))
            .setOutcomeDesc(eit.getEventOutcomeDescription());
        eit.getEventTypeCode().forEach(etc -> {
            if ("IHE Transactions".equals(etc.getCodeSystemName())) {
                auditEvent.addSubtype(codedValueTypeToCoding(etc, IHE_SYSTEM_NAME));
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
            if (poit.getParticipantObjectTypeCodeRole() == ParticipantObjectTypeCodeRole.Patient) {
                var patReference = new Reference(poit.getParticipantObjectID());
                if (patReference.getReferenceElement().hasResourceType()) {
                    entity.setWhat(new Reference(poit.getParticipantObjectID()));
                } else {
                    entity.setWhat(new Reference().setIdentifier(
                        new Identifier().setValue(poit.getParticipantObjectID())));
                }
            } else {
                final var reference = new Reference().setDisplay(poit.getParticipantObjectID());
                entity.setWhat(reference);
            }
        }
        return entity;
    }

    protected AuditEvent.AuditEventSourceComponent auditSourceIdentificationToEventSource(AuditSourceIdentificationType asit) {
        var source = new AuditEvent.AuditEventSourceComponent()
            .setSite(asit.getAuditEnterpriseSiteID())
            .setObserver(new Reference().setDisplay(asit.getAuditSourceID()));
        asit.getAuditSourceType().forEach(ast ->
            source.addType(codedValueTypeToCoding(ast, SECURITY_SOURCE_SYSTEM_NAME)));
        return source;
    }

    protected AuditEvent.AuditEventAgentComponent activeParticipantToAgent(ActiveParticipantType ap) {
        var oAuthEventAgent = oAuthActiveParticipantToAgent(ap);
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

    private Optional<AuditEvent.AuditEventAgentComponent> oAuthActiveParticipantToAgent(ActiveParticipantType ap) {
        var oUser = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_TYPE_SYSTEM_NAME);
        if (oUser.isPresent()) {
            var agent = new AuditEvent.AuditEventAgentComponent()
                .setType(systemAndCodeToCodeableConcept(OUSER_AGENT_TYPE_SYSTEM_NAME, OUSER_AGENT_TYPE_CODE, "information recipient"))
                .addPolicy(oUser.get())
                .setName(ap.getUserName())
                .setWho(
                    new Reference(ap.getUserID())
                        .setIdentifier(new Identifier().setSystem(ap.getAlternativeUserID()).setValue(ap.getUserID()))
                        .setDisplay(ap.getUserName()))
                .setRequestor(ap.isUserIsRequestor());
            getOAuthListAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME)
                .forEach(purpose -> agent.getPurposeOfUse().add(
                    systemAndCodeToCodeableConcept(OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME, purpose, "")));
            getOAuthListAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), OUSER_AGENT_ROLE_SYSTEM_NAME)
                .forEach(purpose -> agent.getRole().add(
                    systemAndCodeToCodeableConcept(OUSER_AGENT_ROLE_SYSTEM_NAME, purpose, "")));
            return Optional.of(agent);
        }
        var oClient = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(), DCM_SYSTEM_NAME);
        if (oClient.isPresent()) {
            return Optional.of(new AuditEvent.AuditEventAgentComponent()
                .setType(systemAndCodeToCodeableConcept(DCM_SYSTEM_NAME, DCM_OCLIENT_CODE, "Application"))
                .setRequestor(ap.isUserIsRequestor())
                .setWho(new Reference().setIdentifier(
                    new Identifier().setValue(oClient.get())).setDisplay(ap.getUserName())));
        }
        var opaqueToken = getOAuthAttrFromKnownRoleIdCode(ap.getRoleIDCodes(),
            OUSER_AGENT_TYPE_OPAQUE_SYSTEM_NAME);
        if (opaqueToken.isPresent()) {
            return Optional.of(new AuditEvent.AuditEventAgentComponent()
                .setType(new CodeableConcept(
                    new Coding(OUSER_AGENT_TYPE_OPAQUE_SYSTEM_NAME, OUSER_AGENT_TYPE_OPAQUE_CODE, "")))
                .setRequestor(true));
        }
        return Optional.empty();
    }

    private Optional<String> getOAuthAttrFromKnownRoleIdCode(List<ActiveParticipantRoleId> roleCodes,
                                                             String knownCodeSystem) {
        return roleCodes.stream().filter(p -> p.getCodeSystemName().equals(knownCodeSystem))
            .findFirst()
            .map(CodedValueType::getCode);
    }

    private List<String> getOAuthListAttrFromKnownRoleIdCode(List<ActiveParticipantRoleId> roleCodes,
                                                             String knownCodeSystem) {
        return roleCodes.stream().filter(p -> p.getCodeSystemName().equals(knownCodeSystem))
            .map(CodedValueType::getCode).collect(Collectors.toList());
    }

    protected AuditEvent.AuditEventAgentNetworkType auditEventNetworkType(NetworkAccessPointTypeCode naptc) {
        try {
            return naptc != null?
                AuditEvent.AuditEventAgentNetworkType.fromCode(String.valueOf(naptc.getValue())) : null;
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    protected AuditEvent.AuditEventOutcome getAuditEventOutcome(EventOutcomeIndicator eventOutcomeIndicator) {
        try {
            return AuditEvent.AuditEventOutcome.fromCode(String.valueOf(eventOutcomeIndicator.getValue()));
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    protected AuditEvent.AuditEventAction getAuditEventAction(EventActionCode eventActionCode) {
        try {
            return AuditEvent.AuditEventAction.fromCode(eventActionCode.getValue());
        } catch (FHIRException e) {
            // should never happen
            throw new AuditException(e);
        }
    }

    protected <T, V> Coding codeToCoding(String codeSystem, T code, Function<T, V> valueSupplier) {
        return (code != null) ?
            new Coding()
                .setCode(String.valueOf(valueSupplier.apply(code)))
                .setSystem(codeSystem) :
            null;
    }

    protected Coding codedValueTypeToCoding(CodedValueType cvt) {
        return cvt != null ?
            codedValueTypeToCoding(cvt, cvt.getCodeSystemName()) :
            null;
    }

    protected Coding codedValueTypeToCoding(CodedValueType cvt, String codeSystem) {
        return cvt != null ?
            new Coding(codeSystem,
                cvt.getCode(),
                cvt.getOriginalText()) :
            null;
    }

    protected CodeableConcept codedValueTypeToCodeableConcept(CodedValueType cvt) {
        return cvt != null ?
            codedValueTypeToCodeableConcept(cvt, cvt.getCodeSystemName()) :
            null;
    }

    protected CodeableConcept codedValueTypeToCodeableConcept(CodedValueType cvt, String codeSystem) {
        return cvt != null ?
            new CodeableConcept().addCoding(codedValueTypeToCoding(cvt, codeSystem)) :
            null;
    }

    protected CodeableConcept systemAndCodeToCodeableConcept(String codeSystem, String code, String displayName) {
        return new CodeableConcept().addCoding(new Coding(codeSystem, code, displayName));
    }
}
