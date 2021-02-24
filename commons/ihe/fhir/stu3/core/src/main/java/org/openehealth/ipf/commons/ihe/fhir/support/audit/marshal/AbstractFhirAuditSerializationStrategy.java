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
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.exceptions.FHIRException;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.NetworkAccessPointTypeCode;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.model.*;
import org.openehealth.ipf.commons.audit.types.CodedValueType;

import java.io.IOException;
import java.io.Writer;
import java.sql.Date;

/**
 * @author Christian Ohr
 * @since 4.1
 */
abstract class AbstractFhirAuditSerializationStrategy implements SerializationStrategy {

    private final FhirContext fhirContext;

    public AbstractFhirAuditSerializationStrategy() {
        this(FhirContext.forDstu3());
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

    protected AuditEvent translate(AuditMessage auditMessage) {
        var eit = auditMessage.getEventIdentification();
        var auditEvent = new AuditEvent()
                .setType(codedValueTypeToCoding(eit.getEventID()))
                .setAction(getAuditEventAction(eit.getEventActionCode()))
                .setRecorded(Date.from(eit.getEventDateTime()))
                .setOutcome(getAuditEventOutcome(eit.getEventOutcomeIndicator()))
                .setOutcomeDesc(eit.getEventOutcomeDescription());
        eit.getEventTypeCode().forEach(etc ->
                auditEvent.addSubtype(codedValueTypeToCoding(etc)));
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
                .setIdentifier(new Identifier()
                        .setValue(poit.getParticipantObjectID()))
                // poit.getParticipantObjectIDTypeCode())) not used here
                .setType(new Coding()
                        .setCode(String.valueOf(poit.getParticipantObjectTypeCode().getValue()))
                        .setSystem("http://hl7.org/fhir/audit-entity-type"))
                .setRole(new Coding()
                        .setCode(String.valueOf(poit.getParticipantObjectTypeCodeRole().getValue()))
                        .setSystem("http://hl7.org/fhir/object-role"))
                .setLifecycle(new Coding()
                        .setCode(String.valueOf(poit.getParticipantObjectDataLifeCycle().getValue()))
                        .setSystem("http://hl7.org/fhir/dicom-audit-lifecycle"))
                .addSecurityLabel(new Coding()
                        .setCode(poit.getParticipantObjectSensitivity()))
                .setName(poit.getParticipantObjectName())
                // poit.getParticipantObjectDescription) not mappable here
                .setQuery(poit.getParticipantObjectQuery());

        poit.getParticipantObjectDetails().forEach(tvp ->
                entity.addDetail(new AuditEvent.AuditEventEntityDetailComponent()
                        .setType(tvp.getType())
                        .setValue(tvp.getValue())));

        return entity;

    }

    protected AuditEvent.AuditEventSourceComponent auditSourceIdentificationToEventSource(AuditSourceIdentificationType asit) {
        var source = new AuditEvent.AuditEventSourceComponent()
                .setSite(asit.getAuditEnterpriseSiteID())
                .setIdentifier(new Identifier().setValue(asit.getAuditSourceID()));
        asit.getAuditSourceType().forEach(ast ->
                source.addType(codedValueTypeToCoding(ast)));
        return source;
    }

    protected AuditEvent.AuditEventAgentComponent activeParticipantToAgent(ActiveParticipantType ap) {
        var agent = new AuditEvent.AuditEventAgentComponent()
                .setUserId(new Identifier().setValue(ap.getUserID()))
                .setAltId(ap.getAlternativeUserID())
                .setName(ap.getUserName())
                .setRequestor(ap.isUserIsRequestor())
                .setMedia(codedValueTypeToCoding(ap.getMediaType()))
                .setNetwork(new AuditEvent.AuditEventAgentNetworkComponent()
                        .setAddress(ap.getNetworkAccessPointID())
                        .setType(auditEventNetworkType(ap.getNetworkAccessPointTypeCode())));
        ap.getRoleIDCodes().forEach(roleID ->
                agent.addPolicy(roleID.getCode()));
        return agent;
    }

    protected AuditEvent.AuditEventAgentNetworkType auditEventNetworkType(NetworkAccessPointTypeCode naptc) {
        try {
            return AuditEvent.AuditEventAgentNetworkType.fromCode(String.valueOf(naptc.getValue()));
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


    protected Coding codedValueTypeToCoding(CodedValueType cvt) {
        return cvt != null ?
                new Coding(cvt.getCodeSystemName(),
                        cvt.getCode(),
                        cvt.getOriginalText()) :
                null;
    }

    protected CodeableConcept codedValueTypeToCodeableConcept(CodedValueType cvt) {
        return cvt != null ?
                new CodeableConcept().addCoding(codedValueTypeToCoding(cvt)) :
                null;
    }
}
