/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.boot.atna;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.model.ParticipantObjectIdentificationType;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * AuditMessageQueue implementation that publishes IPF audit messages as Spring Boot AuditApplicationEvents.
 * This allows integration with Spring Boot's audit event infrastructure.
 * It is meant to be included into a {@link org.openehealth.ipf.commons.audit.queue.CompositeAuditMessageQueue}
 * in addition to the actual audit queue (e.g. synchronous or asynchronous).
 *
 * @author Christian Ohr
 * @since 5.2
 */
public class AuditApplicationEventMessageQueue implements AuditMessageQueue, ApplicationEventPublisherAware {

    private static final String EVENT_TYPE_SEPARATOR = "^";

    private ApplicationEventPublisher publisher;
    
    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        if (publisher != null && auditMessages != null) {
            Arrays.stream(auditMessages)
                .filter(Objects::nonNull)
                .forEach(auditMessage -> publishAuditEvent(auditContext, auditMessage));
        }
    }

    private void publishAuditEvent(AuditContext auditContext, AuditMessage auditMessage) {
        var eventId = auditMessage.getEventIdentification();
        var principal = extractPrincipal(auditMessage, auditContext.getAuditValueIfMissing());
        var eventType = buildEventType(eventId);
        var data = buildEventData(auditMessage);
        var applicationAuditEvent = new AuditApplicationEvent(eventId.getEventDateTime(), principal, eventType, data);
        publisher.publishEvent(applicationAuditEvent);
    }

    private String extractPrincipal(AuditMessage auditMessage, String missingValue) {
        return auditMessage.getActiveParticipants().stream()
            .filter(ActiveParticipantType::isUserIsRequestor)
            .findFirst()
            .map(ActiveParticipantType::getUserID)
            .orElse(missingValue);
    }

    private String buildEventType(EventIdentificationType eventId) {
        var eventIdCode = eventId.getEventID();
        return String.join(EVENT_TYPE_SEPARATOR,
            eventIdCode.getCode(),
            eventIdCode.getCodeSystemName(),
            eventIdCode.getOriginalText());
    }

    private Map<String, Object> buildEventData(AuditMessage auditMessage) {
        var data = new TreeMap<String, Object>();
        addEventIdentificationData(data, auditMessage.getEventIdentification());
        addAuditSourceData(data, auditMessage);
        addActiveParticipantsData(data, auditMessage);
        addParticipantObjectsData(data, auditMessage);
        return data;
    }

    private void addEventIdentificationData(Map<String, Object> data, EventIdentificationType eventId) {
        data.put("eventActionCode", eventId.getEventActionCode());
        data.put("eventOutcome", eventId.getEventOutcomeIndicator());
        if (!eventId.getEventTypeCode().isEmpty()) {
            data.put("eventTypeCode", eventId.getEventTypeCode());
        }
        if (eventId.getEventOutcomeDescription() != null) {
            data.put("eventOutcomeDescription", eventId.getEventOutcomeDescription());
        }
    }

    private void addAuditSourceData(Map<String, Object> data, AuditMessage auditMessage) {
        var auditSource = auditMessage.getAuditSourceIdentification();
        if (auditSource != null) {
            data.put("auditSourceId", auditSource.getAuditSourceID());
            if (!auditSource.getAuditSourceType().isEmpty()) {
                data.put("auditSourceType", auditSource.getAuditSourceType());
            }
        }
    }

    private void addActiveParticipantsData(Map<String, Object> data, AuditMessage auditMessage) {
        if (!auditMessage.getActiveParticipants().isEmpty()) {
            var participants = auditMessage.getActiveParticipants().stream()
                .map(this::mapActiveParticipant)
                .toList();
            data.put("activeParticipants", participants);
        }
    }

    private Map<String, Object> mapActiveParticipant(ActiveParticipantType ap) {
        var participantData = new HashMap<String, Object>();
        participantData.put("userId", ap.getUserID());
        participantData.put("userIsRequestor", ap.isUserIsRequestor());
        
        if (ap.getUserName() != null) {
            participantData.put("userName", ap.getUserName());
        }
        if (ap.getNetworkAccessPointID() != null) {
            participantData.put("networkAccessPointId", ap.getNetworkAccessPointID());
        }
        if (ap.getNetworkAccessPointTypeCode() != null) {
            participantData.put("networkAccessPointTypeCode", ap.getNetworkAccessPointTypeCode());
        }
        if (!ap.getRoleIDCodes().isEmpty()) {
            participantData.put("roleIdCodes", ap.getRoleIDCodes());
        }
        
        return participantData;
    }

    private void addParticipantObjectsData(Map<String, Object> data, AuditMessage auditMessage) {
        if (!auditMessage.getParticipantObjectIdentifications().isEmpty()) {
            var participantObjects = auditMessage.getParticipantObjectIdentifications().stream()
                .map(this::mapParticipantObject)
                .toList();
            data.put("participantObjects", participantObjects);
        }
    }

    private Map<String, Object> mapParticipantObject(ParticipantObjectIdentificationType poi) {
        var objectData = new HashMap<String, Object>();
        objectData.put("participantObjectId", poi.getParticipantObjectID());
        objectData.put("participantObjectIdTypeCode", poi.getParticipantObjectIDTypeCode());
        
        if (poi.getParticipantObjectTypeCode() != null) {
            objectData.put("participantObjectTypeCode", poi.getParticipantObjectTypeCode());
        }
        if (poi.getParticipantObjectTypeCodeRole() != null) {
            objectData.put("participantObjectTypeCodeRole", poi.getParticipantObjectTypeCodeRole());
        }
        if (poi.getParticipantObjectDataLifeCycle() != null) {
            objectData.put("participantObjectDataLifeCycle", poi.getParticipantObjectDataLifeCycle());
        }
        if (poi.getParticipantObjectSensitivity() != null) {
            objectData.put("participantObjectSensitivity", poi.getParticipantObjectSensitivity());
        }
        if (poi.getParticipantObjectName() != null) {
            objectData.put("participantObjectName", poi.getParticipantObjectName());
        }
        if (poi.getParticipantObjectQuery() != null) {
            objectData.put("participantObjectQuery", poi.getParticipantObjectQuery());
        }
        if (!poi.getParticipantObjectDetails().isEmpty()) {
            objectData.put("participantObjectDetails", poi.getParticipantObjectDetails());
        }
        return objectData;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = Objects.requireNonNull(applicationEventPublisher,
            "applicationEventPublisher must not be null");
    }
}
