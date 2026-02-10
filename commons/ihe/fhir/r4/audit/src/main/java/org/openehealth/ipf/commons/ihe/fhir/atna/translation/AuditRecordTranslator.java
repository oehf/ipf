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
package org.openehealth.ipf.commons.ihe.fhir.atna.translation;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.EnumFactory;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditEntityTypeEnumFactory;
import org.hl7.fhir.r4.model.codesystems.AuditSourceTypeEnumFactory;
import org.hl7.fhir.r4.model.codesystems.ObjectRoleEnumFactory;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.DicomObjectDescriptionType;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator;
import org.openehealth.ipf.commons.map.MappingService;

import java.util.Date;
import java.util.Map;

/**
 * Translates DICOM/ATNA audit records into FHIR R4 AuditEvent resources.
 * <p>
 * Mapping based on:
 * - FHIR R4 <a href="https://hl7.org/fhir/R4/auditevent-mappings.html">AuditEvent mappings</a>
 * - DICOM PS3.15 Audit Trail Message Format
 * <p>
 * Note that the preferred method of auditing in FHIR is to use one of the FhirAuditSerializationStrategy subclasses
 *
 * @author Dmytro Rud
 * @since 3.6
 */
public class AuditRecordTranslator implements ToFhirTranslator<AuditMessage> {

    private final MappingService mappingService;

    public AuditRecordTranslator(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    public AuditEvent translate(AuditMessage atna) {
        return translateToFhir(atna, null);
    }

    /**
     * Translates a DICOM/ATNA audit message to a FHIR AuditEvent resource.
     *
     * @param auditMessage the ATNA audit message to translate
     * @param parameters additional parameters (currently unused)
     * @return the translated FHIR AuditEvent resource
     */
    @Override
    public AuditEvent translateToFhir(AuditMessage auditMessage, Map<String, Object> parameters) {
        var fhir = auditEvent(
                auditMessage.getEventIdentification(),
                auditMessage.getAuditSourceIdentification());

        auditMessage.getActiveParticipants().stream()
            .map(this::participant)
            .forEach(fhir::addAgent);

        auditMessage.getParticipantObjectIdentifications().stream()
            .map(this::participantObject)
            .forEach(fhir::addEntity);
        
        return fhir;
    }

    /**
     * Creates a FHIR AuditEvent from event identification and audit source.
     * <p>
     * DICOM to FHIR Mapping:
     * <ul>
     *   <li>EventID -> type (Coding, required)</li>
     *   <li>EventTypeCode -> subtype (List&lt;Coding&gt;, 0..*)</li>
     *   <li>EventActionCode -> action (code, 0..1)</li>
     *   <li>EventDateTime -> recorded (instant, required)</li>
     *   <li>EventOutcomeIndicator -> outcome (code, required)</li>
     *   <li>EventOutcomeDescription -> outcomeDesc (string, 0..1)</li>
     *   <li>PurposeOfUse -> purposeOfEvent (List&lt;CodeableConcept&gt;, 0..*)</li>
     * </ul>
     *
     * @param eventIdentification the event identification
     * @param auditSourceIdentification the audit source identification
     * @return the created AuditEvent
     */
    private AuditEvent auditEvent(
            EventIdentificationType eventIdentification,
            AuditSourceIdentificationType auditSourceIdentification) {
        
        var auditEvent = new AuditEvent();
        
        // EventID -> type (required)
        auditEvent.setType(coding(eventIdentification.getEventID()));
        
        // EventTypeCode -> subtype (0..*)
        eventIdentification.getEventTypeCode().stream()
            .map(this::coding)
            .forEach(auditEvent::addSubtype);
        
        // EventActionCode -> action (0..1)
        if (eventIdentification.getEventActionCode() != null) {
            auditEvent.setAction(AuditEvent.AuditEventAction.fromCode(
                    eventIdentification.getEventActionCode().getValue()));
        }
        
        // EventDateTime -> recorded (required)
        auditEvent.setRecordedElement(new InstantType(Date.from(eventIdentification.getEventDateTime())));

        // EventOutcomeIndicator -> outcome (required)
        auditEvent.setOutcome(AuditEvent.AuditEventOutcome.fromCode(
            eventIdentification.getEventOutcomeIndicator().getValue().toString()));

        // EventOutcomeDescription -> outcomeDesc (0..1)
        if (eventIdentification.getEventOutcomeDescription() != null) {
            auditEvent.setOutcomeDesc(eventIdentification.getEventOutcomeDescription());
        }
        
        // PurposeOfUse -> purposeOfEvent (0..*)
        eventIdentification.getPurposesOfUse().stream()
            .map(this::codeableConcept)
            .forEach(auditEvent::addPurposeOfEvent);
        
        // AuditSourceIdentification -> source (required)
        auditEvent.setSource(auditSourceIdentification(auditSourceIdentification));
        
        return auditEvent;
    }

    /**
     * Creates a FHIR AuditEvent source component from audit source identification.
     * <p>
     * DICOM to FHIR Mapping:
     * <ul>
     *   <li>AuditSourceID -> observer.identifier.value (required)</li>
     *   <li>AuditEnterpriseSiteID -> site (string, 0..1)</li>
     *   <li>AuditSourceTypeCode -> type (List&lt;Coding&gt;, 0..*)</li>
     * </ul>
     *
     * @param auditSourceIdentification the audit source identification
     * @return the created source component
     */
    private AuditEvent.AuditEventSourceComponent auditSourceIdentification(
            AuditSourceIdentificationType auditSourceIdentification) {
        
        var source = new AuditEvent.AuditEventSourceComponent();
        
        // AuditEnterpriseSiteID -> site
        source.setSite(auditSourceIdentification.getAuditEnterpriseSiteID());

        // AuditSourceID -> observer.identifier (required)
        var identifier = new Identifier().setValue(auditSourceIdentification.getAuditSourceID());
        source.setObserver(new Reference().setIdentifier(identifier));

        // AuditSourceTypeCode -> type (0..*)
        auditSourceIdentification.getAuditSourceType().forEach(sourceType -> 
            source.addType(createCodingFromEnum(
                new AuditSourceTypeEnumFactory(), 
                sourceType.getCode(), 
                sourceType.getOriginalText()
            ))
        );
        
        return source;
    }

    /**
     * Creates a FHIR AuditEvent agent from an active participant.
     * <p>
     * DICOM to FHIR Mapping:
     * <ul>
     *   <li>UserID -> who.identifier.value (required)</li>
     *   <li>AlternativeUserID -> altId (string, 0..1)</li>
     *   <li>UserName -> name (string, 0..1)</li>
     *   <li>UserIsRequestor -> requestor (boolean, required)</li>
     *   <li>RoleIDCode[0] -> type (CodeableConcept, 0..1) - first role</li>
     *   <li>RoleIDCode[1..n] -> role (List&lt;CodeableConcept&gt;, 0..*) - remaining roles</li>
     *   <li>NetworkAccessPointID -> network.address (string, 0..1)</li>
     *   <li>NetworkAccessPointTypeCode -> network.type (code, 0..1)</li>
     *   <li>MediaType -> media (Coding, 0..1)</li>
     * </ul>
     *
     * @param activeParticipant the active participant
     * @return the created agent component
     */
    private AuditEvent.AuditEventAgentComponent participant(ActiveParticipantType activeParticipant) {
        // UserIsRequestor -> requestor (required)
        var agent = new AuditEvent.AuditEventAgentComponent(new BooleanType(activeParticipant.isUserIsRequestor()));
        
        // UserID -> who.identifier (required)
        var identifier = new Identifier().setValue(activeParticipant.getUserID());
        agent.setWho(new Reference().setIdentifier(identifier));
        
        // AlternativeUserID -> altId
        if (activeParticipant.getAlternativeUserID() != null) {
            agent.setAltId(activeParticipant.getAlternativeUserID());
        }
        
        // UserName -> name
        if (activeParticipant.getUserName() != null) {
            agent.setName(activeParticipant.getUserName());
        }
        
        // NetworkAccessPointID + NetworkAccessPointTypeCode -> network
        if (activeParticipant.getNetworkAccessPointID() != null) {
            agent.setNetwork(createNetworkComponent(activeParticipant));
        }
        
        // MediaType -> media (Coding)
        if (activeParticipant.getMediaType() != null) {
            agent.setMedia(coding(activeParticipant.getMediaType()));
        }
        
        // RoleIDCode mapping: first -> type, rest -> role
        var roleIdCodes = activeParticipant.getRoleIDCodes();
        if (!roleIdCodes.isEmpty()) {
            // First role -> type (CodeableConcept)
            agent.setType(codeableConcept(roleIdCodes.get(0)));
            
            // Remaining roles -> role (CodeableConcept list)
            if (roleIdCodes.size() > 1) {
                roleIdCodes.stream()
                    .skip(1)
                    .map(this::codeableConcept)
                    .forEach(agent::addRole);
            }
        }
        
        return agent;
    }

    /**
     * Creates a network component from active participant network information.
     *
     * @param activeParticipant the active participant
     * @return the created network component
     */
    private AuditEvent.AuditEventAgentNetworkComponent createNetworkComponent(
            ActiveParticipantType activeParticipant) {
        
        var network = new AuditEvent.AuditEventAgentNetworkComponent();
        network.setAddress(activeParticipant.getNetworkAccessPointID());
        
        if (activeParticipant.getNetworkAccessPointTypeCode() != null) {
            network.setType(AuditEvent.AuditEventAgentNetworkType.fromCode(
                    activeParticipant.getNetworkAccessPointTypeCode().getValue().toString()));
        }
        
        return network;
    }

    /**
     * Creates a FHIR AuditEvent entity from a participant object.
     * <p> 
     * DICOM to FHIR Mapping:
     * <ul>
     *   <li>ParticipantObjectID -> what.identifier.value (required)</li>
     *   <li>ParticipantObjectIDTypeCode -> what.identifier.type (CodeableConcept, 0..1)</li>
     *   <li>ParticipantObjectTypeCode -> type (Coding, 0..1)</li>
     *   <li>ParticipantObjectTypeCodeRole -> role (Coding, 0..1)</li>
     *   <li>ParticipantObjectDataLifeCycle -> lifecycle (Coding, 0..1)</li>
     *   <li>ParticipantObjectSensitivity -> securityLabel (List&lt;Coding&gt;, 0..*)</li>
     *   <li>ParticipantObjectName -> name (string, 0..1)</li>
     *   <li>ParticipantObjectQuery -> query (base64Binary, 0..1)</li>
     *   <li>ParticipantObjectDetail -> detail (List&lt;detail&gt;, 0..*)</li>
     *   <li>ParticipantObjectDescription -> description (string, 0..1)</li>
     * </ul>
     *
     * @param participantObject the participant object
     * @return the created entity component
     */
    private AuditEvent.AuditEventEntityComponent participantObject(
            ParticipantObjectIdentificationType participantObject) {
        
        var entity = new AuditEvent.AuditEventEntityComponent();
        
        // ParticipantObjectID -> what.identifier.value (required)
        // ParticipantObjectIDTypeCode -> what.identifier.type (CodeableConcept)
        var identifier = new Identifier()
            .setValue(participantObject.getParticipantObjectID())
            .setType(codeableConcept(participantObject.getParticipantObjectIDTypeCode()));
        entity.setWhat(new Reference().setIdentifier(identifier));
        
        // ParticipantObjectTypeCode -> type (Coding)
        if (participantObject.getParticipantObjectTypeCode() != null) {
            entity.setType(createCodingFromEnum(
                new AuditEntityTypeEnumFactory(),
                participantObject.getParticipantObjectTypeCode().getValue().toString(),
                null
            ));
        }
        
        // ParticipantObjectTypeCodeRole -> role (Coding)
        if (participantObject.getParticipantObjectTypeCodeRole() != null) {
            entity.setRole(createCodingFromEnum(
                new ObjectRoleEnumFactory(),
                participantObject.getParticipantObjectTypeCodeRole().getValue().toString(),
                null
            ));
        }
        
        // ParticipantObjectDataLifeCycle -> lifecycle (Coding)
        if (participantObject.getParticipantObjectDataLifeCycle() != null) {
            entity.setLifecycle(new Coding()
                    .setCode(participantObject.getParticipantObjectDataLifeCycle().getValue().toString()));
        }
        
        // ParticipantObjectSensitivity -> securityLabel (List<Coding>)
        if (participantObject.getParticipantObjectSensitivity() != null) {
            entity.addSecurityLabel(new Coding()
                .setCode(participantObject.getParticipantObjectSensitivity()));
        }
        
        // ParticipantObjectName -> name
        if (participantObject.getParticipantObjectName() != null) {
            entity.setName(participantObject.getParticipantObjectName());
        }
        
        // ParticipantObjectQuery -> query (base64Binary)
        if (participantObject.getParticipantObjectQuery() != null) {
            entity.setQuery(participantObject.getParticipantObjectQuery());
        }
        
        // ParticipantObjectDetail -> detail (type + value as base64Binary)
        participantObject.getParticipantObjectDetails().forEach(detail ->
            entity.addDetail(new AuditEvent.AuditEventEntityDetailComponent()
                    .setType(detail.getType())
                    .setValue(new Base64BinaryType(detail.getValue())))
        );
        
        // ParticipantObjectDescription -> description (string)
        if (!participantObject.getParticipantObjectDescriptions().isEmpty()) {
            entity.setDescription(buildDicomDescription(participantObject.getParticipantObjectDescriptions()));
        }
        
        return entity;
    }

    /**
     * Builds a string description from DICOM object descriptions.
     *
     * @param dicomDescriptions the DICOM object descriptions
     * @return the formatted description string
     */
    private String buildDicomDescription(java.util.List<DicomObjectDescriptionType> dicomDescriptions) {
        var descriptionBuilder = new StringBuilder();
        
        for (DicomObjectDescriptionType dicomDesc : dicomDescriptions) {
            if (!descriptionBuilder.isEmpty()) {
                descriptionBuilder.append("; ");
            }
            
            appendIfNotEmpty(descriptionBuilder, "MPPS: ", dicomDesc.getMPPS());
            appendIfNotEmpty(descriptionBuilder, "Accession: ", dicomDesc.getAccession());
            
            if (!dicomDesc.getSOPClasses().isEmpty()) {
                if (!descriptionBuilder.isEmpty()) {
                    descriptionBuilder.append(", ");
                }
                descriptionBuilder.append("SOPClasses: ");
                dicomDesc.getSOPClasses().forEach(sopClass ->
                    descriptionBuilder.append(sopClass.getUid())
                            .append("(")
                            .append(sopClass.getNumberOfInstances())
                            .append(") ")
                );
            }
        }
        
        return descriptionBuilder.toString();
    }

    /**
     * Appends a list of values to a StringBuilder with a prefix if the list is not empty.
     *
     * @param builder the StringBuilder to append to
     * @param prefix the prefix to add before the values
     * @param values the list of values to append
     */
    private void appendIfNotEmpty(StringBuilder builder, String prefix, java.util.List<String> values) {
        if (!values.isEmpty()) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder
                .append(prefix)
                .append(String.join(", ", values));
        }
    }

    /**
     * Creates a Coding from an enum factory, handling exceptions gracefully.
     *
     * @param factory the enum factory
     * @param code the code value
     * @param display the display text (optional)
     * @param <T> the enum type
     * @return the created Coding
     */
    private <T extends Enum<?>> Coding createCodingFromEnum(
            EnumFactory<T> factory,
            String code, 
            String display) {
        
        try {
            var enumValue = factory.fromCode(code);
            return new Coding()
                .setSystem(factory.toSystem(enumValue))
                .setCode(code)
                .setDisplay(display);
        } catch (Exception e) {
            // If code is not in the enum, create a coding without system
            return new Coding()
                .setCode(code)
                .setDisplay(display);
        }
    }

    /**
     * Creates a FHIR Coding from a coded value type.
     *
     * @param codedValue the coded value
     * @return the created Coding, or null if codedValue is null
     */
    private Coding coding(CodedValueType codedValue) {
        if (codedValue == null) {
            return null;
        }
        return new Coding()
            .setSystem((String)mappingService.get("atnaCodingSystem", codedValue.getCodeSystemName()))
            .setCode(codedValue.getCode())
            .setDisplay(codedValue.getOriginalText());
    }

    /**
     * Creates a FHIR CodeableConcept from a coded value type.
     *
     * @param codedValue the coded value
     * @return the created CodeableConcept, or null if codedValue is null
     */
    private CodeableConcept codeableConcept(CodedValueType codedValue) {
        if (codedValue == null) {
            return null;
        }
        
        var concept = new CodeableConcept().addCoding(coding(codedValue));
        if (codedValue.getOriginalText() != null) {
            concept.setText(codedValue.getOriginalText());
        }
        return concept;
    }
}