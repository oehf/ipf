/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir

import org.hl7.fhir.instance.model.*
import org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent
import org.hl7.fhir.instance.model.valuesets.AuditSourceTypeEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectRoleEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectTypeEnumFactory
import org.joda.time.format.ISODateTimeFormat
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.*

/**
 * Translates ATNA audit records into FHIR AuditEvent resources.
 * @author Dmytro Rud
 */
class AuditRecordTranslator {

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
        def fhir = new CodeableConcept()
        fhir.addCoding(coding(atna))
        return fhir
    }

    static InstantType timestamp(String s) {
        return new InstantType(ISODateTimeFormat.dateTime().parseDateTime(s).toDate())
    }

    static AuditEvent.AuditEventEventComponent eventIdentification(EventIdentificationType atna) {
        def fhir = new AuditEvent.AuditEventEventComponent(coding(atna.eventID), timestamp(atna.eventDateTime))
        atna.eventTypeCode.each { fhir.addSubtype(coding(it)) }
        fhir.action = new AuditEvent.AuditEventActionEnumFactory().fromCode(atna.eventActionCode)
        fhir.outcome = new AuditEvent.AuditEventOutcomeEnumFactory().fromCode(Integer.toString(atna.eventOutcomeIndicator))
        atna.purposesOfUse.each { fhir.addPurposeOfEvent(coding(it)) }
        return fhir
    }

    static AuditEventSourceComponent auditSourceIdentification(AuditSourceIdentificationType atna) {
        def fhir = new AuditEventSourceComponent(
                site: atna.auditEnterpriseSiteID,
                identifier: new Identifier(value: atna.auditSourceID))
        if (atna.auditSourceTypeCode?.code) {
            fhir.addType(coding(new AuditSourceTypeEnumFactory().fromCode(atna.auditSourceTypeCode.code)))
        }
        return fhir
    }

    static AuditEvent.AuditEventParticipantComponent participant(ActiveParticipantType atna) {
        def fhir = new AuditEvent.AuditEventParticipantComponent(new BooleanType(atna.userIsRequestor))
        atna.roleIDCode.each { fhir.addRole(codeableConcept(it)) }
        fhir.userId = new Identifier(value: atna.userID)
        fhir.altId = atna.alternativeUserID
        fhir.name = atna.userName
        if (atna.networkAccessPointID) {
            fhir.network = new AuditEvent.AuditEventParticipantNetworkComponent(
                    address: atna.networkAccessPointID,
                    type: new AuditEvent.AuditEventParticipantNetworkTypeEnumFactory().fromCode(Short.toString(atna.networkAccessPointTypeCode)))
        }
        return fhir
    }

    static AuditEvent.AuditEventObjectComponent participantObject(ParticipantObjectIdentificationType atna) {
        def fhir = new AuditEvent.AuditEventObjectComponent()
        fhir.identifier = new Identifier(
                value: atna.participantObjectID,
                type: codeableConcept(atna.participantObjectIDTypeCode))
        fhir.type = coding(new ObjectTypeEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCode)))
        fhir.role = coding(new ObjectRoleEnumFactory().fromCode(Short.toString(atna.participantObjectTypeCodeRole)))
        atna.participantObjectDetail.each {
            fhir.addDetail(new AuditEvent.AuditEventObjectDetailComponent(type: it.type, value: it.value))
        }
        return fhir
    }

    AuditEvent translate(AuditEventMessage atna) {
        AuditEvent fhir = new AuditEvent(
                eventIdentification(atna.auditMessage.eventIdentification),
                auditSourceIdentification(atna.auditMessage.auditSourceIdentification[0]))
        atna.auditMessage.activeParticipant.each { fhir.addParticipant(participant(it)) }
        atna.auditMessage.participantObjectIdentification.each { fhir.addObject(participantObject(it)) }
        return fhir
    }
}
