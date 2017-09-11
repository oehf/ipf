/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.translation

import org.hl7.fhir.dstu3.model.AuditEvent
import org.hl7.fhir.dstu3.model.BooleanType
import org.hl7.fhir.dstu3.model.CodeableConcept
import org.hl7.fhir.dstu3.model.Coding
import org.hl7.fhir.dstu3.model.Identifier
import org.hl7.fhir.dstu3.model.InstantType
import org.hl7.fhir.dstu3.model.codesystems.AuditSourceTypeEnumFactory
import org.hl7.fhir.dstu3.model.codesystems.ObjectRoleEnumFactory
import org.joda.time.format.ISODateTimeFormat
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ActiveParticipantType
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.AuditSourceIdentificationType
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.CodedValueType
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.EventIdentificationType
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ParticipantObjectIdentificationType


/**
 * Translates ATNA audit records into FHIR AuditEvent resources.
 *
 * @author Dmytro Rud
 * @since 3.4
 */
class AuditRecordTranslator implements ToFhirTranslator<AuditEventMessage> {

    static Coding coding(CodedValueType atna) {
        return new Coding(
                code: atna.code,
                display: atna.originalText,
                system: atna.codeSystemName.mapAtnaCodingSystem())
    }

    /**
     * For codes returned from FHIR *EnumFactory instances.
     */
    static Coding coding(Enum type) {
        return new Coding(
                code: type.toCode(),
                display: type.display,
                system: type.system)
    }

    static CodeableConcept codeableConcept(CodedValueType atna) {
        return new CodeableConcept().addCoding(coding(atna))
    }

    static InstantType timestamp(String s) {
        return new InstantType(ISODateTimeFormat.dateTime().parseDateTime(s).toDate())
    }

    static AuditEvent auditEvent(EventIdentificationType eventIdentificationType, AuditSourceIdentificationType auditSourceIdentificationType) {
        AuditEvent fhir = new AuditEvent(
                coding(eventIdentificationType.eventID),
                timestamp(eventIdentificationType.eventDateTime),
                auditSourceIdentification(auditSourceIdentificationType))
        eventIdentificationType.eventTypeCode.each { fhir.addSubtype(coding(it)) }
        fhir.action = new AuditEvent.AuditEventActionEnumFactory().fromCode(eventIdentificationType.eventActionCode)
        fhir.outcome = new AuditEvent.AuditEventOutcomeEnumFactory().fromCode(Integer.toString(eventIdentificationType.eventOutcomeIndicator))
        eventIdentificationType.purposesOfUse.each { fhir.addPurposeOfEvent(codeableConcept(it)) }
        return fhir
    }

    static AuditEvent.AuditEventSourceComponent auditSourceIdentification(AuditSourceIdentificationType atna) {
        AuditEvent.AuditEventSourceComponent fhir = new AuditEvent.AuditEventSourceComponent(
                site: atna.auditEnterpriseSiteID,
                identifier: new Identifier(value: atna.auditSourceID))
            if (atna.auditSourceTypeCode?.code) {
            fhir.addType(coding(new AuditSourceTypeEnumFactory().fromCode(atna.auditSourceTypeCode.code)))
        }
        return fhir
    }

    static AuditEvent.AuditEventAgentComponent participant(ActiveParticipantType atna) {
        AuditEvent.AuditEventAgentComponent fhir = new AuditEvent.AuditEventAgentComponent(new BooleanType(atna.userIsRequestor))
        atna.roleIDCode.each { fhir.addRole(codeableConcept(it)) }
        fhir.userId = new Identifier(value: atna.userID)
        fhir.altId = atna.alternativeUserID
        fhir.name = atna.userName
        if (atna.networkAccessPointID) {
            fhir.network = new AuditEvent.AuditEventAgentNetworkComponent()
                .setAddress(atna.networkAccessPointID)
                .setType(new AuditEvent.AuditEventAgentNetworkTypeEnumFactory().fromCode(Short.toString(atna.networkAccessPointTypeCode)))
        }
        fhir
    }

    static AuditEvent.AuditEventEntityComponent participantObject(ParticipantObjectIdentificationType atna) {
        AuditEvent.AuditEventEntityComponent fhir = new AuditEvent.AuditEventEntityComponent()
        fhir.identifier = new Identifier(
                value: atna.participantObjectID,
                /*type: codeableConcept(atna.participantObjectIDTypeCode)*/)
        fhir.type = coding(new ObjectTypeEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCode)))
        fhir.role = coding(new ObjectRoleEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCodeRole)))
        atna.participantObjectDetail.each {
            fhir.addDetail(new AuditEvent.AuditEventEntityDetailComponent(type: it.type, value: it.value))
        }
        fhir
    }

    AuditEvent translate(AuditEventMessage atna) {
        translateToFhir(atna, null)
    }

    @Override
    AuditEvent translateToFhir(AuditEventMessage atna, Map<String, Object> parameters) {
        AuditEvent fhir = auditEvent(
                atna.auditMessage.eventIdentification,
                atna.auditMessage.auditSourceIdentification[0])
        atna.auditMessage.activeParticipant.each { fhir.addAgent(participant(it)) }
        atna.auditMessage.participantObjectIdentification.each { fhir.addEntity(participantObject(it)) }
        fhir
    }
}
