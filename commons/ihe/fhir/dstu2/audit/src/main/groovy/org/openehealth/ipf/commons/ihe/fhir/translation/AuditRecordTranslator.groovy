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

import org.hl7.fhir.instance.model.*
import org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent
import org.hl7.fhir.instance.model.valuesets.AuditSourceTypeEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectRoleEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectTypeEnumFactory
import org.openehealth.ipf.commons.audit.model.*
import org.openehealth.ipf.commons.audit.types.CodedValueType

import java.time.Instant

/**
 * Translates ATNA audit records into FHIR AuditEvent resources.
 *
 * @author Dmytro Rud
 * @since 3.1
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

    static AuditEvent.AuditEventEventComponent eventIdentification(EventIdentificationType event) {
        AuditEvent.AuditEventEventComponent fhir = new AuditEvent.AuditEventEventComponent(coding(event.eventID), timestamp(event.eventDateTime))
        event.eventTypeCode.each { fhir.addSubtype(coding(it)) }
        fhir.action = new AuditEvent.AuditEventActionEnumFactory().fromCode(event.eventActionCode.value)
        fhir.outcome = new AuditEvent.AuditEventOutcomeEnumFactory().fromCode(Integer.toString(event.eventOutcomeIndicator.value))
        event.purposesOfUse.each { fhir.addPurposeOfEvent(coding(it)) }
        return fhir
    }

    static AuditEventSourceComponent auditSourceIdentification(AuditSourceIdentificationType auditSource) {
        AuditEventSourceComponent fhir = new AuditEventSourceComponent(
                site: auditSource.auditEnterpriseSiteID,
                identifier: new Identifier(value: auditSource.auditSourceID))
        auditSource.auditSourceType.each {
            fhir.addType(codingEnum(new AuditSourceTypeEnumFactory().fromCode(it.code)))
        }
        return fhir
    }

    static AuditEvent.AuditEventParticipantComponent participant(ActiveParticipantType ap) {
        AuditEvent.AuditEventParticipantComponent fhir = new AuditEvent.AuditEventParticipantComponent(new BooleanType(ap.userIsRequestor))
        ap.roleIDCodes.each { fhir.addRole(codeableConcept(it)) }
        fhir.userId = new Identifier(value: ap.userID)
        fhir.altId = ap.alternativeUserID
        fhir.name = ap.userName
        if (ap.networkAccessPointID) {
            fhir.network = new AuditEvent.AuditEventParticipantNetworkComponent(
                    address: ap.networkAccessPointID,
                    type: new AuditEvent.AuditEventParticipantNetworkTypeEnumFactory().fromCode(Short.toString(ap.networkAccessPointTypeCode.value)))
        }
        return fhir
    }

    static AuditEvent.AuditEventObjectComponent participantObject(ParticipantObjectIdentificationType poi) {
        AuditEvent.AuditEventObjectComponent fhir = new AuditEvent.AuditEventObjectComponent()
        fhir.identifier = new Identifier(
                value: poi.participantObjectID,
                /*type: codeableConcept(atna.participantObjectIDTypeCode)*/)
        fhir.type = codingEnum(new ObjectTypeEnumFactory().fromCode(Short.toString(poi.participantObjectTypeCode.value)))
        fhir.role = codingEnum(new ObjectRoleEnumFactory().fromCode(Short.toString(poi.participantObjectTypeCodeRole.value)))
        poi.participantObjectDetails.each {
            fhir.addDetail(new AuditEvent.AuditEventObjectDetailComponent(type: it.type, value: it.value))
        }
        fhir
    }

    AuditEvent translate(AuditMessage atna) {
        translateToFhir(atna, null)
    }

    @Override
    AuditEvent translateToFhir(AuditMessage auditMessage, Map<String, Object> parameters) {
        AuditEvent fhir = new AuditEvent(
                eventIdentification(auditMessage.eventIdentification),
                auditSourceIdentification(auditMessage.auditSourceIdentification))
        auditMessage.activeParticipants.each { fhir.addParticipant(participant(it)) }
        auditMessage.participantObjectIdentifications.each { fhir.addObject(participantObject(it)) }
        fhir
    }
}
