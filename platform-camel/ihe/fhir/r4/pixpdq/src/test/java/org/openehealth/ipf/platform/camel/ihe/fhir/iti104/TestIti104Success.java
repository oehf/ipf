/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti104;

import org.hl7.fhir.r4.model.CapabilityStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventIdCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.iti104.Iti104Constants;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-104 conditional update and delete, sent directly and through the producer, and their ATNA
 * audit records.
 */
public class TestIti104Success extends AbstractTestIti104 {

    private static final String CONTEXT_DESCRIPTOR = "iti-104.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    /**
     * The Manager shall indicate that conditional update is available
     */
    @Test
    public void testGetConformance() {
        var conf = client.capabilities().ofType(CapabilityStatement.class).execute();
        var patient = conf.getRestFirstRep().getResource().stream()
            .filter(resource -> "Patient".equals(resource.getType()))
            .findFirst()
            .orElseThrow();
        assertTrue(patient.getConditionalUpdate());
        assertEquals("single", patient.getConditionalDelete().toCode());
        var interactions = patient.getInteraction().stream()
            .map(interaction -> interaction.getCode().toCode())
            .toList();
        assertTrue(interactions.contains("update"), interactions.toString());
        assertTrue(interactions.contains("delete"), interactions.toString());
    }

    @Test
    public void testAddPatient() {
        var outcome = sendConditionalUpdate(pixmPatient(NEW_PATIENT), NEW_PATIENT);
        assertEquals(Boolean.TRUE, outcome.getCreated());
        assertEquals(NEW_PATIENT, outcome.getId().getIdPart());

        var event = singleAuditMessage();
        assertAuditMessage(event, EventActionCode.Create, NEW_PATIENT);
        assertDataEntity(event, "Patient/" + NEW_PATIENT, ParticipantObjectTypeCodeRole.Job);
    }

    @Test
    public void testRevisePatient() {
        var outcome = sendConditionalUpdate(pixmPatient(EXISTING_PATIENT), EXISTING_PATIENT);
        assertNotEquals(Boolean.TRUE, outcome.getCreated());

        var event = singleAuditMessage();
        assertAuditMessage(event, EventActionCode.Update, EXISTING_PATIENT);
        assertDataEntity(event, "Patient/" + EXISTING_PATIENT, ParticipantObjectTypeCodeRole.Report);
    }

    /**
     * A Patient in the body of a conditional update may carry a logical id, as the PIXm examples do
     */
    @Test
    public void testRevisePatientWithIdInBody() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.setId("Patient-Red");
        sendConditionalUpdate(patient, EXISTING_PATIENT);
        assertAuditMessage(singleAuditMessage(), EventActionCode.Update, EXISTING_PATIENT);
    }

    @Test
    public void testResolveDuplicatePatient() {
        var patient = pixmPatient(EXISTING_PATIENT)
            .addReplacedByLink(identifier("IHERED-1000"));
        sendConditionalUpdate(patient, EXISTING_PATIENT);

        assertAuditMessage(singleAuditMessage(), EventActionCode.Update, EXISTING_PATIENT);
    }

    @Test
    public void testRemovePatient() {
        sendConditionalDelete(EXISTING_PATIENT);

        var event = singleAuditMessage();
        assertAuditMessage(event, EventActionCode.Delete, EXISTING_PATIENT);
        assertDataEntity(event, "Patient/" + EXISTING_PATIENT, ParticipantObjectTypeCodeRole.Report);
    }

    /**
     * The Source records an update even when the Manager reports that it has created the patient.
     */
    @Test
    public void testAddPatientViaProducer() {
        var outcome = sendViaProducer(pixmPatient(NEW_PATIENT), Map.of());
        assertEquals(Boolean.TRUE, outcome.getCreated());

        var events = getAuditSender().getMessages();
        assertEquals(2, events.size());
        assertAuditMessage(serverMessage(events), EventActionCode.Create, NEW_PATIENT);
        assertAuditMessage(clientMessage(events), EventActionCode.Update, NEW_PATIENT);
    }

    /**
     * A Patient with several identifiers needs the identifier of the condition to be named
     */
    @Test
    public void testRevisePatientViaProducerWithIdentifierHeader() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.addIdentifier().setSystem("urn:oid:1.2.3.4").setValue("4711");
        sendViaProducer(patient, Map.of(Iti104Constants.ITI104_PATIENT_IDENTIFIER_HEADER, SYSTEM + "|" + EXISTING_PATIENT));

        var events = getAuditSender().getMessages();
        assertEquals(2, events.size());
        assertAuditMessage(serverMessage(events), EventActionCode.Update, EXISTING_PATIENT);
        assertAuditMessage(clientMessage(events), EventActionCode.Update, EXISTING_PATIENT);
    }

    @Test
    public void testRemovePatientViaProducer() {
        sendViaProducer(identifier(EXISTING_PATIENT), Map.of());

        var events = getAuditSender().getMessages();
        assertEquals(2, events.size());
        assertAuditMessage(serverMessage(events), EventActionCode.Delete, EXISTING_PATIENT);
        assertAuditMessage(clientMessage(events), EventActionCode.Delete, EXISTING_PATIENT);
    }

    private static AuditMessage singleAuditMessage() {
        var events = getAuditSender().getMessages();
        assertEquals(1, events.size());
        return events.get(0);
    }

    private static AuditMessage serverMessage(java.util.List<AuditMessage> events) {
        return events.stream().filter(AuditMessage::isServerSide).findFirst().orElseThrow();
    }

    private static AuditMessage clientMessage(java.util.List<AuditMessage> events) {
        return events.stream().filter(event -> !event.isServerSide()).findFirst().orElseThrow();
    }

    private static void assertAuditMessage(AuditMessage event, EventActionCode action, String patientValue) {
        var eit = event.getEventIdentification();
        assertEquals(EventOutcomeIndicator.Success, eit.getEventOutcomeIndicator());
        assertEquals(action, eit.getEventActionCode());
        assertEquals(EventIdCode.PatientRecord, eit.getEventID());
        assertEquals(FhirEventTypeCode.MobilePatientIdentityFeed, eit.getEventTypeCode().get(0));

        var patients = event.findParticipantObjectIdentifications(
            poi -> poi.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.PatientNumber);
        assertEquals(1, patients.size());
        assertEquals(SYSTEM + "|" + patientValue, patients.get(0).getParticipantObjectID());
    }

    private static void assertDataEntity(AuditMessage event, String reference, ParticipantObjectTypeCodeRole role) {
        var data = event.findParticipantObjectIdentifications(
            poi -> poi.getParticipantObjectIDTypeCode() == ParticipantObjectIdTypeCode.URI);
        assertEquals(1, data.size());
        assertEquals(reference, data.get(0).getParticipantObjectID());
        assertEquals(role, data.get(0).getParticipantObjectTypeCodeRole());
    }
}
