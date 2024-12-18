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
import java.util.Base64;

import static org.openehealth.ipf.commons.audit.XMLNames.*;

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
        var element = new Element(AUDIT_MESSAGE);
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
        var element = new Element(ACTIVE_PARTICIPANT);
        element.setAttribute(USER_ID, activeParticipant.getUserID());
        conditionallyAddAttribute(element, ALTERNATIVE_USER_ID, activeParticipant.getAlternativeUserID());
        conditionallyAddAttribute(element, USER_NAME, activeParticipant.getUserName());
        element.setAttribute(USER_IS_REQUESTOR, Boolean.toString(activeParticipant.isUserIsRequestor()));
        conditionallyAddAttribute(element, NETWORK_ACCESS_POINT_ID, activeParticipant.getNetworkAccessPointID());
        conditionallyAddAttribute(element, NETWORK_ACCESS_POINT_TYPE_CODE, activeParticipant.getNetworkAccessPointTypeCode());
        // as per https://docs.oracle.com/cd/E63053_01/doc.30/e61289/xmlschema.htm
        // RoleIDCode and MediaIdentifier are in a sequence so they should be written
        // in exactly this order
        if (activeParticipant.getRoleIDCodes() != null) {
            activeParticipant.getRoleIDCodes().stream()
                    .map(roleIdCode -> codedValueType(ROLE_ID_CODE, roleIdCode))
                    .forEach(element::addContent);
        }
        if (activeParticipant.getMediaType() != null) {
            element.addContent(
                    new Element(MEDIA_IDENTIFIER)
                            .addContent(codedValueType(MEDIA_TYPE, activeParticipant.getMediaType())));
        }
        return element;
    }

    protected Element eventIdentification(EventIdentificationType eventIdentification) {
        var element = new Element(EVENT_IDENTIFICATION);
        if (eventIdentification != null) {
            element.setAttribute(EVENT_ACTION_CODE, eventIdentification.getEventActionCode().getValue());
            element.setAttribute(EVENT_DATE_TIME, eventIdentification.getEventDateTime().toString());
            element.setAttribute(EVENT_OUTCOME_INDICATOR, eventIdentification.getEventOutcomeIndicator().getValue().toString());
            element.addContent(codedValueType(EVENT_ID, eventIdentification.getEventID()));
            eventIdentification.getEventTypeCode().stream()
                    .map(eventTypeCode -> codedValueType(EVENT_TYPE_CODE, eventTypeCode))
                    .forEach(element::addContent);
            if (eventIdentification.getEventOutcomeDescription() != null) {
                element.addContent(
                        new Element(EVENT_OUTCOME_DESCRIPTION)
                                .addContent(eventIdentification.getEventOutcomeDescription()));
            }
            eventIdentification.getPurposesOfUse().stream()
                    .map(purposeOfUse -> codedValueType(PURPOSE_OF_USE, purposeOfUse))
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element participantObjectIdentification(ParticipantObjectIdentificationType poi) {
        var element = new Element(PARTICIPANT_OBJECT_IDENTIFICATION);
        if (poi != null) {
            conditionallyAddAttribute(element, PARTICIPANT_OBJECT_ID, poi.getParticipantObjectID());
            if (poi.getParticipantObjectTypeCode() != null) {
                conditionallyAddAttribute(element, PARTICIPANT_OBJECT_TYPE_CODE, poi.getParticipantObjectTypeCode().getValue().toString());
            }
            conditionallyAddAttribute(element, PARTICIPANT_OBJECT_TYPE_CODE_ROLE, poi.getParticipantObjectTypeCodeRole());
            conditionallyAddAttribute(element, PARTICIPANT_OBJECT_DATA_LIFE_CYCLE, poi.getParticipantObjectDataLifeCycle());
            conditionallyAddAttribute(element, PARTICIPANT_OBJECT_SENSITIVITY, poi.getParticipantObjectSensitivity());
            element.addContent(codedValueType(PARTICIPANT_OBJECT_ID_TYPE_CODE, poi.getParticipantObjectIDTypeCode()));
            if (poi.getParticipantObjectName() != null) {
                element.addContent(new Element(PARTICIPANT_OBJECT_NAME)
                        .addContent(poi.getParticipantObjectName()));
            }
            if (poi.getParticipantObjectQuery() != null) {
                element.addContent(new Element(PARTICIPANT_OBJECT_QUERY)
                        .addContent(new String(
                                Base64.getEncoder().encode(poi.getParticipantObjectQuery()),
                                StandardCharsets.UTF_8)));
            }
            poi.getParticipantObjectDetails().stream()
                    .map(participantObjectDetail -> typeValuePairType(PARTICIPANT_OBJECT_DETAIL, participantObjectDetail))
                    .forEach(element::addContent);

            poi.getParticipantObjectDescriptions().stream()
                    .map(this::dicomObjectDescription)
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element auditSourceIdentification(AuditSourceIdentificationType auditSourceIdentification) {
        var element = new Element(AUDIT_SOURCE_IDENTIFICATION);
        if (auditSourceIdentification != null) {
            conditionallyAddAttribute(element, AUDIT_ENTERPRISE_SITE_ID, auditSourceIdentification.getAuditEnterpriseSiteID());
            conditionallyAddAttribute(element, AUDIT_SOURCE_ID, auditSourceIdentification.getAuditSourceID());

            auditSourceIdentification.getAuditSourceType().stream()
                    .map(this::auditSourceType)
                    .forEach(element::addContent);
        }
        return element;
    }

    protected Element auditSourceType(AuditSource auditSourceType) {
        return new Element(AUDIT_SOURCE_TYPE_CODE).addContent(auditSourceType.getCode());
    }

    protected Element codedValueType(String tagName, CodedValueType codedValue) {
        var element = new Element(tagName);
        element.setAttribute(CSD_CODE, codedValue.getCode());
        conditionallyAddAttribute(element, CODE_SYSTEM_NAME, codedValue.getCodeSystemName());
        conditionallyAddAttribute(element, DISPLAY_NAME, codedValue.getDisplayName());
        conditionallyAddAttribute(element, ORIGINAL_TEXT, codedValue.getOriginalText());
        return element;
    }

    protected Element typeValuePairType(String tagName, TypeValuePairType typeValuePair) {
        var element = new Element(tagName);
        element.setAttribute(TYPE, typeValuePair.getType());
        element.setAttribute(VALUE, new String(
                Base64.getEncoder().encode(typeValuePair.getValue()),
                StandardCharsets.UTF_8));
        return element;
    }

    protected Element dicomObjectDescription(DicomObjectDescriptionType dicomObjectDescription) {
        var pod = new Element(PARTICIPANT_OBJECT_DESCRIPTION);
        dicomObjectDescription.getMPPS().forEach(mpps ->
                pod.addContent(new Element(MPPS).setAttribute(UID, mpps)));
        dicomObjectDescription.getAccession().forEach(accession ->
                pod.addContent(new Element(ACCESSION).setAttribute(NUMBER, accession)));
        dicomObjectDescription.getSOPClasses().forEach(sop -> {
            var sopClass = new Element(SOP_CLASS)
                    .setAttribute(NUMBER_OF_INSTANCES, String.valueOf(sop.getNumberOfInstances()));
            conditionallyAddAttribute(sopClass, UID, sop.getUid());
            sop.getInstanceUids().forEach(uid ->
                    sopClass.addContent(new Element(INSTANCE).setAttribute(UID, uid)));
            pod.addContent(sopClass);
        });
        if (!dicomObjectDescription.getStudyIDs().isEmpty()) {
            var participantObjectContainsStudy = new Element(PARTICIPANT_OBJECT_CONTAINS_STUDY);
            dicomObjectDescription.getStudyIDs().forEach(studyID ->
                    participantObjectContainsStudy.addContent(
                            new Element(STUDY_IDS)
                                    .setAttribute(UID, studyID)));
            pod.addContent(participantObjectContainsStudy);
        }
        if (dicomObjectDescription.getEncrypted() != null) {
            pod.addContent(
                    new Element(ENCRYPTED)
                            .addContent(String.valueOf(dicomObjectDescription.getEncrypted())));
        }
        if (dicomObjectDescription.getAnonymized() != null) {
            pod.addContent(
                    new Element(ANONYMIZED)
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
