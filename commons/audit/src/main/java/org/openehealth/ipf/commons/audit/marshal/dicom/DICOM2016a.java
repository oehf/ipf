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
package org.openehealth.ipf.commons.audit.marshal.dicom;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.model.*;
import org.openehealth.ipf.commons.audit.types.AuditSource;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.audit.types.EnumeratedValueSet;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class DICOM2016a implements SerializationStrategy {

    // Omit XML declaration, because this is done as part of the RFC5424Protocol
    private static final XMLOutputter PRETTY = new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true));
    private static final XMLOutputter COMPACT = new XMLOutputter(Format.getCompactFormat().setOmitDeclaration(true));


    @Override
    public void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) throws IOException {
        serialize(auditMessage, writer, pretty ? PRETTY : COMPACT);
    }

    private void serialize(AuditMessage auditMessage, Writer writer, XMLOutputter outputter) throws IOException {
        Element element = new Element("AuditMessage");
        element.addContent(eventIdentification(auditMessage.getEventIdentification()));

        auditMessage.getActiveParticipants().stream()
                .map(this::activeParticipant)
                .forEach(element::addContent);

        element.addContent(auditSourceIdentification(auditMessage.getAuditSourceIdentification()));

        auditMessage.getParticipantObjectIdentifications().stream()
                .map(this::participantObjectIdentification)
                .forEach(element::addContent);

        outputter.output(new Document(element), writer);
    }

    protected Content activeParticipant(ActiveParticipantType activeParticipant) {
        Element element = new Element("ActiveParticipant");
        element.setAttribute("UserID", activeParticipant.getUserID());
        conditionallyAddAttribute(element, "AlternativeUserID", activeParticipant.getAlternativeUserID());
        conditionallyAddAttribute(element, "UserName", activeParticipant.getUserName());
        element.setAttribute("UserIsRequestor", Boolean.toString(activeParticipant.isUserIsRequestor()));
        conditionallyAddAttribute(element, "NetworkAccessPointID", activeParticipant.getNetworkAccessPointID());
        conditionallyAddAttribute(element, "NetworkAccessPointTypeCode", activeParticipant.getNetworkAccessPointTypeCode());
// TODO mediaidentifier/mediatype
        if (activeParticipant.getRoleIDCodes() != null) {
            activeParticipant.getRoleIDCodes().stream()
                    .map(roleIdCode -> codedValueType("RoleIDCode", roleIdCode))
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element eventIdentification(EventIdentificationType eventIdentification) {
        Element element = new Element("EventIdentification");
        if (eventIdentification != null) {
            element.setAttribute("EventActionCode", eventIdentification.getEventActionCode().getValue());
            element.setAttribute("EventDateTime", eventIdentification.getEventDateTime().toString());
            element.setAttribute("EventOutcomeIndicator", eventIdentification.getEventOutcomeIndicator().getValue().toString());
            if (eventIdentification.getEventID() != null) {
                element.addContent(codedValueType("EventID", eventIdentification.getEventID()));
            }
            eventIdentification.getEventTypeCode().stream()
                    .map(eventTypeCode -> codedValueType("EventTypeCode", eventTypeCode))
                    .forEach(element::addContent);
            if (eventIdentification.getEventOutcomeDescription() != null) {
                element.addContent(
                        new Element("EventOutcomeDescription")
                                .addContent(eventIdentification.getEventOutcomeDescription()));
            }
            eventIdentification.getPurposesOfUse().stream()
                    .map(purposeOfUse -> codedValueType("PurposeOfUse", purposeOfUse))
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element participantObjectIdentification(ParticipantObjectIdentificationType poi) {
        Element element = new Element("ParticipantObjectIdentification");
        if (poi != null) {
            conditionallyAddAttribute(element, "ParticipantObjectID", poi.getParticipantObjectID());
            conditionallyAddAttribute(element, "ParticipantObjectTypeCode", poi.getParticipantObjectTypeCode().getValue().toString());
            conditionallyAddAttribute(element, "ParticipantObjectTypeCodeRole", poi.getParticipantObjectTypeCodeRole());
            conditionallyAddAttribute(element, "ParticipantObjectDataLifeCycle", poi.getParticipantObjectDataLifeCycle());
            conditionallyAddAttribute(element, "ParticipantObjectSensitivity", poi.getParticipantObjectSensitivity());
            if (poi.getParticipantObjectIDTypeCode() != null) {
                element.addContent(codedValueType("ParticipantObjectIDTypeCode", poi.getParticipantObjectIDTypeCode()));
            }
            if (poi.getParticipantObjectName() != null) {
                element.addContent(new Element("ParticipantObjectName")
                        .addContent(poi.getParticipantObjectName()));
            }
            if (poi.getParticipantObjectQuery() != null) {
                element.addContent(new Element("ParticipantObjectQuery")
                        .addContent(new String(poi.getParticipantObjectQuery(), StandardCharsets.UTF_8)));
            }
            poi.getParticipantObjectDetails().stream()
                    .map(participantObjectDetail -> typeValuePairType("ParticipantObjectDetail", participantObjectDetail))
                    .forEach(element::addContent);

            poi.getParticipantObjectDescriptions().stream()
                    .map(this::dicomObjectDescription)
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element auditSourceIdentification(AuditSourceIdentificationType auditSourceIdentification) {
        Element element = new Element("AuditSourceIdentification");
        if (auditSourceIdentification != null) {
            conditionallyAddAttribute(element, "AuditEnterpriseSiteID", auditSourceIdentification.getAuditEnterpriseSiteID());
            conditionallyAddAttribute(element, "AuditSourceID", auditSourceIdentification.getAuditSourceID());

            auditSourceIdentification.getAuditSourceType().stream()
                    .map(this::auditSourceType)
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element auditSourceType(AuditSource auditSourceType) {
        return new Element("AuditSourceTypeCode").addContent(auditSourceType.getCode());
    }

    protected Element codedValueType(String tagName, CodedValueType codedValue) {
        Element element = new Element(tagName);
        element.setAttribute("csd-code", codedValue.getCode());
        conditionallyAddAttribute(element, "codeSystemName", codedValue.getCodeSystemName());
        conditionallyAddAttribute(element, "displayName", codedValue.getDisplayName());
        conditionallyAddAttribute(element, "originalText", codedValue.getOriginalText());
        return element;
    }

    protected Element typeValuePairType(String tagName, TypeValuePairType typeValuePair) {
        Element element = new Element(tagName);
        element.setAttribute("type", typeValuePair.getType());
        element.setAttribute("value", new String(typeValuePair.getValue(), StandardCharsets.UTF_8));
        return element;
    }

    protected Element dicomObjectDescription(DicomObjectDescriptionType dicomObjectDescription) {
        Element pod = new Element("ParticipantObjectDescription");
        dicomObjectDescription.getMPPS().forEach(mpps ->
                pod.addContent(new Element("MPPS").setAttribute("UID", mpps)));
        dicomObjectDescription.getAccession().forEach(accession ->
                pod.addContent(new Element("Accession").setAttribute("Number", accession)));
        dicomObjectDescription.getSOPClasses().forEach(sop -> {
            Element sopClass = new Element("SOPClass")
                    .setAttribute("NumberOfInstances", String.valueOf(sop.getNumberOfInstances()));
            conditionallyAddAttribute(sopClass, "UID", sop.getUid());
            sop.getInstanceUids().forEach(uid ->
                    sopClass.addContent(new Element("Instance").setAttribute("UID", uid)));
            pod.addContent(sopClass);
        });
        if (!dicomObjectDescription.getStudyIDs().isEmpty()) {
            Element participantObjectContainsStudy = new Element("ParticipantObjectContainsStudy ");
            dicomObjectDescription.getStudyIDs().forEach(studyID ->
                    participantObjectContainsStudy.addContent(
                            new Element("StudyIDs ")
                                    .setAttribute("UID", studyID)));
            pod.addContent(participantObjectContainsStudy);
        }
        if (dicomObjectDescription.getEncrypted() != null) {
            pod.addContent(
                    new Element("Encrypted")
                            .addContent(String.valueOf(dicomObjectDescription.getEncrypted())));
        }
        if (dicomObjectDescription.getAnonymized() != null) {
            pod.addContent(
                    new Element("Anonymized")
                            .addContent(String.valueOf(dicomObjectDescription.getAnonymized())));
        }
        return pod;
    }

    protected void conditionallyAddAttribute(Element element, String attributeName, String value) {
        if (value != null) {
            element.setAttribute(attributeName, value);
        }
    }

    protected void conditionallyAddAttribute(Element element, String attributeName, EnumeratedValueSet<?> value) {
        if (value != null) {
            element.setAttribute(attributeName, value.getValue().toString());
        }
    }

}
