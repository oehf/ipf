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

import org.hl7.fhir.dstu3.model.*
import org.hl7.fhir.dstu3.model.codesystems.AuditSourceTypeEnumFactory
import org.hl7.fhir.dstu3.model.codesystems.ObjectRoleEnumFactory
import org.hl7.fhir.dstu3.model.codesystems.ObjectTypeEnumFactory
import org.openehealth.ipf.commons.audit.model.*
import org.openehealth.ipf.commons.audit.types.CodedValueType

import java.time.Instant

/**
 * Translates ATNA audit records into FHIR AuditEvent resources.
 *
 * @author Dmytro Rud
 * @since 3.4
 */
class AuditRecordTranslator implements ToFhirTranslator<AuditMessage> {

    static Coding coding(CodedValueType codedValueType) {
        return new Coding(
                code: codedValueType.code,
                display: codedValueType.originalText,
                system: codedValueType.codeSystemName.mapAtnaCodingSystem())
    }

    /**
     * For codes returned from FHIR *EnumFactory instances.
     */
    static Coding codingEnum(Enum type) {
        return new Coding(
                code: type.toCode(),
                display: type.display,
                system: type.system)
    }

    static CodeableConcept codeableConcept(CodedValueType codedValueType) {
        return new CodeableConcept().addCoding(coding(codedValueType))
    }

    static InstantType timestamp(Instant instant) {
        return new InstantType(Date.from(instant))
    }

    static AuditEvent auditEvent(EventIdentificationType eventIdentificationType, AuditSourceIdentificationType auditSourceIdentificationType) {
        AuditEvent fhir = new AuditEvent(
                coding(eventIdentificationType.eventID),
                timestamp(eventIdentificationType.eventDateTime),
                auditSourceIdentification(auditSourceIdentificationType))
        eventIdentificationType.eventTypeCode.each { fhir.addSubtype(coding(it)) }
        fhir.action = new AuditEvent.AuditEventActionEnumFactory().fromCode(eventIdentificationType.eventActionCode.value)
        fhir.outcome = new AuditEvent.AuditEventOutcomeEnumFactory().fromCode(Integer.toString(eventIdentificationType.eventOutcomeIndicator.value))
        eventIdentificationType.purposesOfUse.each { fhir.addPurposeOfEvent(codeableConcept(it)) }
        return fhir
    }

    static AuditEvent.AuditEventSourceComponent auditSourceIdentification(AuditSourceIdentificationType atna) {
        AuditEvent.AuditEventSourceComponent fhir = new AuditEvent.AuditEventSourceComponent(
                site: atna.auditEnterpriseSiteID,
                identifier: new Identifier(value: atna.auditSourceID))
        atna.auditSourceType.each {
            fhir.addType(codingEnum(new AuditSourceTypeEnumFactory().fromCode(it.code)))
        }
        return fhir
    }

    static AuditEvent.AuditEventAgentComponent participant(ActiveParticipantType atna) {
        AuditEvent.AuditEventAgentComponent fhir = new AuditEvent.AuditEventAgentComponent(new BooleanType(atna.userIsRequestor))
        atna.roleIDCodes.each { fhir.addRole(codeableConcept(it)) }
        fhir.userId = new Identifier(value: atna.userID)
        fhir.altId = atna.alternativeUserID
        fhir.name = atna.userName
        if (atna.networkAccessPointID) {
            fhir.network = new AuditEvent.AuditEventAgentNetworkComponent()
                    .setAddress(atna.networkAccessPointID)
                    .setType(new AuditEvent.AuditEventAgentNetworkTypeEnumFactory().fromCode(Short.toString(atna.networkAccessPointTypeCode.value)))
        }
        fhir
    }

    static AuditEvent.AuditEventEntityComponent participantObject(ParticipantObjectIdentificationType atna) {
        AuditEvent.AuditEventEntityComponent fhir = new AuditEvent.AuditEventEntityComponent()
        fhir.identifier = new Identifier(
                value: atna.participantObjectID,
                /*type: codeableConcept(atna.participantObjectIDTypeCode)*/)
        fhir.type = codingEnum(new ObjectTypeEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCode.value)))
        fhir.role = codingEnum(new ObjectRoleEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCodeRole.value)))
        atna.participantObjectDetail.each {
            fhir.addDetail(new AuditEvent.AuditEventEntityDetailComponent(type: it.type, value: it.value))
        }
        fhir
    }

    AuditEvent translate(AuditMessage atna) {
        translateToFhir(atna, null)
    }

    @Override
    AuditEvent translateToFhir(AuditMessage auditMessage, Map<String, Object> parameters) {
        AuditEvent fhir = auditEvent(
                auditMessage.eventIdentification,
                auditMessage.auditSourceIdentification)
        auditMessage.activeParticipants.each { fhir.addAgent(participant(it)) }
        auditMessage.participantObjectIdentifications.each { fhir.addEntity(participantObject(it)) }
        fhir
    }
}
