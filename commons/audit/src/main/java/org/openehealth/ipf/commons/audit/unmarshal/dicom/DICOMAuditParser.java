/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.audit.unmarshal.dicom;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.AuditSourceType;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.AuditSourceIdentificationType;
import org.openehealth.ipf.commons.audit.model.DicomObjectDescriptionType;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.model.TypeValuePairType;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.CodedValueType;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.MediaType;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.audit.unmarshal.AuditParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openehealth.ipf.commons.audit.XMLNames.*;

/**
 * Parses XML-formatted Audit messages as written by
 * {@link org.openehealth.ipf.commons.audit.marshal.dicom.DICOM2017c}
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class DICOMAuditParser implements AuditParser {

    private static XMLReaderJDOMFactory XSD_FACTORY;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart().appendOffsetId()
            .toFormatter();

    static {
        try {
            XSD_FACTORY = new XMLReaderXSDFactory(DICOMAuditParser.class.getResource("/dicom2017c.xsd"));
        } catch (JDOMException ignored) {
        }
    }

    @Override
    public AuditMessage parse(String s, boolean validate) {
        try {
            var document = useSAXParser(new StringReader(s), validate);
            var root = document.getRootElement();
            var auditMessage = new AuditMessage();
            auditMessage.setEventIdentification(eventIdentificationType(root.getChild(EVENT_IDENTIFICATION)));
            mapInto(auditMessage.getActiveParticipants(), root, ACTIVE_PARTICIPANT, this::activeParticipantType);
            auditMessage.setAuditSourceIdentification(auditSourceIdentificationType(root.getChild(AUDIT_SOURCE_IDENTIFICATION)));
            mapInto(auditMessage.getParticipantObjectIdentifications(), root, PARTICIPANT_OBJECT_IDENTIFICATION, this::participantObjectIdentificationType);
            return auditMessage;
        } catch (AuditException e) {
            throw e;
        } catch (Exception e) {
            throw new AuditException(e);
        }
    }

    private EventIdentificationType eventIdentificationType(Element element) {
        var ei = new EventIdentificationType(
                eventId(element.getChild(EVENT_ID)),
                dateTime(element.getAttributeValue(EVENT_DATE_TIME)),
                EventOutcomeIndicator.enumForCode(Integer.parseInt(element.getAttributeValue(EVENT_OUTCOME_INDICATOR))));
        mapInto(ei.getEventTypeCode(), element, EVENT_TYPE_CODE, this::eventType);
        ei.setEventOutcomeDescription(element.getChildText(EVENT_OUTCOME_DESCRIPTION));
        if (element.getAttribute(EVENT_ACTION_CODE) != null) {
            ei.setEventActionCode(
                    EventActionCode.enumForCode(element.getAttributeValue(EVENT_ACTION_CODE)));
        }
        mapInto(ei.getPurposesOfUse(), element, PURPOSE_OF_USE, this::purposeOfUse);
        return ei;
    }


    private ActiveParticipantType activeParticipantType(Element element) {
        var ap = new ActiveParticipantType(
                element.getAttributeValue(USER_ID),
                Boolean.parseBoolean(element.getAttributeValue(USER_IS_REQUESTOR)));
        ap.setAlternativeUserID(element.getAttributeValue(ALTERNATIVE_USER_ID));
        ap.setUserName(element.getAttributeValue(USER_NAME));
        ap.setNetworkAccessPointID(element.getAttributeValue(NETWORK_ACCESS_POINT_ID));
        if (element.getAttribute(NETWORK_ACCESS_POINT_TYPE_CODE) != null) {
            ap.setNetworkAccessPointTypeCode(NetworkAccessPointTypeCode.enumForCode(
                    Short.parseShort(element.getAttributeValue(NETWORK_ACCESS_POINT_TYPE_CODE))));
        }
        mapInto(ap.getRoleIDCodes(), element, ROLE_ID_CODE, this::activeParticipantRoleId);
        var mediaIdentifier = element.getChild(MEDIA_IDENTIFIER);
        if (mediaIdentifier != null) {
            ap.setMediaIdentifier(mediaIdentifier.getText());
            if (element.getChild(MEDIA_TYPE) != null) {
                ap.setMediaType(mediaType(element.getChild(MEDIA_TYPE)));
            }
        }
        return ap;
    }

    private AuditSourceIdentificationType auditSourceIdentificationType(Element element) {
        var asi = new AuditSourceIdentificationType(
                element.getAttributeValue(AUDIT_SOURCE_ID));
        asi.setAuditEnterpriseSiteID(element.getAttributeValue(AUDIT_ENTERPRISE_SITE_ID));
        mapInto(asi.getAuditSourceType(), element, AUDIT_SOURCE_TYPE_CODE, e ->
                AuditSourceType.enumForCode(e.getAttributeValue(CSD_CODE)));
        return asi;
    }

    private ParticipantObjectIdentificationType participantObjectIdentificationType(Element element) {
        var poi = new ParticipantObjectIdentificationType(
                element.getAttributeValue(PARTICIPANT_OBJECT_ID),
                participantObjectIdType(element.getChild(PARTICIPANT_OBJECT_ID_TYPE_CODE)));
        if (element.getAttributeValue(PARTICIPANT_OBJECT_TYPE_CODE) != null) {
            poi.setParticipantObjectTypeCode(ParticipantObjectTypeCode.enumForCode(
                    Short.parseShort(element.getAttributeValue(PARTICIPANT_OBJECT_TYPE_CODE))));
        }
        if (element.getAttributeValue(PARTICIPANT_OBJECT_TYPE_CODE_ROLE) != null) {
            poi.setParticipantObjectTypeCodeRole(ParticipantObjectTypeCodeRole.enumForCode(
                    Short.parseShort(element.getAttributeValue(PARTICIPANT_OBJECT_TYPE_CODE_ROLE))));
        }
        if (element.getAttributeValue(PARTICIPANT_OBJECT_DATA_LIFE_CYCLE) != null) {
            poi.setParticipantObjectDataLifeCycle(ParticipantObjectDataLifeCycle.enumForCode(
                    Short.parseShort(element.getAttributeValue(PARTICIPANT_OBJECT_DATA_LIFE_CYCLE))));
        }
        poi.setParticipantObjectSensitivity(element.getAttributeValue(PARTICIPANT_OBJECT_SENSITIVITY));
        poi.setParticipantObjectName(element.getChildText(PARTICIPANT_OBJECT_NAME));
        if (element.getChild(PARTICIPANT_OBJECT_QUERY) != null) {
            poi.setParticipantObjectQuery(Base64.getDecoder().decode(element.getChildText(PARTICIPANT_OBJECT_QUERY)));
        }
        mapInto(poi.getParticipantObjectDetails(), element, PARTICIPANT_OBJECT_DETAIL, this::valuePair);
        mapInto(poi.getParticipantObjectDescriptions(), element, PARTICIPANT_OBJECT_DESCRIPTION, this::partipantObjectDescription);
        return poi;
    }

    private DicomObjectDescriptionType partipantObjectDescription(Element element) {
        var dicom = new DicomObjectDescriptionType();
        mapInto(dicom.getMPPS(), element, MPPS, e -> e.getAttributeValue(UID));
        mapInto(dicom.getAccession(), element, ACCESSION, e -> e.getAttributeValue(NUMBER));
        mapInto(dicom.getSOPClasses(), element, SOP_CLASS, this::sopClass);
        var studies = element.getChild(PARTICIPANT_OBJECT_CONTAINS_STUDY);
        mapInto(dicom.getStudyIDs(), studies, STUDY_IDS, e -> e.getAttributeValue(UID));
        if (element.getChild(ENCRYPTED) != null) {
            dicom.setEncrypted(Boolean.parseBoolean(element.getChildText(ENCRYPTED)));
        }
        if (element.getChild(ANONYMIZED) != null) {
            dicom.setEncrypted(Boolean.parseBoolean(element.getChildText(ANONYMIZED)));
        }
        return dicom;
    }

    private DicomObjectDescriptionType.SOPClass sopClass(Element element) {
        var sopClass = new DicomObjectDescriptionType.SOPClass(
                Integer.parseInt(element.getAttributeValue(NUMBER_OF_INSTANCES)));
        sopClass.setUid(element.getAttributeValue(UID));
        mapInto(sopClass.getInstanceUids(), element, INSTANCE, e -> e.getAttributeValue(UID));
        return sopClass;
    }

    private <T> void mapInto(List<T> container, Element element, String name, Function<Element, T> mapper) {
        if (element == null) return;
        container.addAll(
                element.getChildren(name).stream()
                        .map(mapper)
                        .collect(Collectors.toList()));
    }

    private EventId eventId(Element codedValueElement) {
        return codedValue(codedValueElement, EventId::of);
    }

    private EventType eventType(Element codedValueElement) {
        return codedValue(codedValueElement, EventType::of);
    }

    private PurposeOfUse purposeOfUse(Element codedValueElement) {
        return codedValue(codedValueElement, PurposeOfUse::of);
    }

    private ActiveParticipantRoleId activeParticipantRoleId(Element codedValueElement) {
        return codedValue(codedValueElement, ActiveParticipantRoleId::of);
    }

    private MediaType mediaType(Element codedValueElement) {
        return codedValue(codedValueElement, MediaType::of);
    }

    private ParticipantObjectIdType participantObjectIdType(Element codedValueElement) {
        return codedValue(codedValueElement, ParticipantObjectIdType::of);
    }

    private TypeValuePairType valuePair(Element element) {
        return new TypeValuePairType(
                element.getAttributeValue(TYPE),
                Base64.getDecoder().decode(element.getAttributeValue(VALUE)));
    }

    private <T> T codedValue(Element element, Function<CodedValueType, T> f) {
        return f.apply(CodedValueType.of(
                element.getAttributeValue(CSD_CODE),
                element.getAttributeValue(CODE_SYSTEM_NAME),
                element.getAttributeValue(ORIGINAL_TEXT),
                element.getAttributeValue(DISPLAY_NAME)));
    }

    private Instant dateTime(String s) {
        var parsed = DATE_TIME_FORMATTER.parseBest(s, Instant::from, LocalDateTime::from);
        if (parsed instanceof Instant) {
            return (Instant) parsed;
        } else if (parsed instanceof LocalDateTime) {
            return ((LocalDateTime) parsed).atOffset(ZoneOffset.UTC).toInstant();
        } else {
            throw new AuditException("Could not parse " + s + " to Instant");
        }
    }

    private static Document useSAXParser(Reader reader, boolean validate) throws JDOMException, IOException {
        var saxBuilder = validate ? new SAXBuilder(XSD_FACTORY) : new SAXBuilder();
        return saxBuilder.build(reader);
    }
}
