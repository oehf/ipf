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

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CanonicalType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmValidator;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmValidator;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-104 audited with the AuditEvents profiled in PIXm: IHE.PIXm.Feed.Create/Update/Delete.Audit.Manager
 * for the Manager, IHE.PIXm.Feed.Update/Delete.Audit.Source for the Source.
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti104WithBalpAudit extends AbstractTestIti104 {

    private static final String CONTEXT_DESCRIPTOR = "iti-104-balp.xml";

    private static final String PROFILE_PREFIX = "https://profiles.ihe.net/ITI/PIXm/StructureDefinition/";
    private static final String IHE_TRANSACTION_SYSTEM = "urn:ihe:event-type-code";
    private static final String RESTFUL_INTERACTION_SYSTEM = "http://hl7.org/fhir/restful-interaction";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testAddPatient() {
        sendConditionalUpdate(pixmPatient(NEW_PATIENT), NEW_PATIENT);
        var auditEvent = singleAuditEvent();
        assertEquals(List.of(PROFILE_PREFIX + "IHE.PIXm.Feed.Create.Audit.Manager"), profiles(auditEvent));
        assertEquals("C", auditEvent.getAction().toCode());
        assertSubtypes(auditEvent, "create");
        assertPatientAndData(auditEvent, NEW_PATIENT);
    }

    @Test
    public void testRevisePatient() {
        sendConditionalUpdate(pixmPatient(EXISTING_PATIENT), EXISTING_PATIENT);
        var auditEvent = singleAuditEvent();
        assertEquals(List.of(PROFILE_PREFIX + "IHE.PIXm.Feed.Update.Audit.Manager"), profiles(auditEvent));
        assertEquals("U", auditEvent.getAction().toCode());
        assertSubtypes(auditEvent, "update");
        assertPatientAndData(auditEvent, EXISTING_PATIENT);
    }

    @Test
    public void testRemovePatient() {
        sendConditionalDelete(EXISTING_PATIENT);
        var auditEvent = singleAuditEvent();
        assertEquals(List.of(PROFILE_PREFIX + "IHE.PIXm.Feed.Delete.Audit.Manager"), profiles(auditEvent));
        assertEquals("D", auditEvent.getAction().toCode());
        assertSubtypes(auditEvent, "delete");
        assertPatientAndData(auditEvent, EXISTING_PATIENT);
    }

    @Test
    public void testAddPatientViaProducerAuditsBothEnds() {
        sendViaProducer(pixmPatient(NEW_PATIENT), Map.of());
        assertEquals(List.of(
                PROFILE_PREFIX + "IHE.PIXm.Feed.Create.Audit.Manager",
                PROFILE_PREFIX + "IHE.PIXm.Feed.Update.Audit.Source"),
            allProfiles());
    }

    @Test
    public void testRemovePatientViaProducerAuditsBothEnds() {
        sendViaProducer(identifier(EXISTING_PATIENT), Map.of());
        assertEquals(List.of(
                PROFILE_PREFIX + "IHE.PIXm.Feed.Delete.Audit.Manager",
                PROFILE_PREFIX + "IHE.PIXm.Feed.Delete.Audit.Source"),
            allProfiles());
    }

    private static AuditEvent singleAuditEvent() {
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        return auditEvents.get(0);
    }

    private static List<String> profiles(AuditEvent auditEvent) {
        return auditEvent.getMeta().getProfile().stream()
            .map(CanonicalType::getValue)
            .toList();
    }

    private static List<String> allProfiles() {
        return FhirAuditRepository.getAuditEvents().stream()
            .flatMap(auditEvent -> profiles(auditEvent).stream())
            .sorted()
            .toList();
    }

    private static void assertSubtypes(AuditEvent auditEvent, String interaction) {
        assertTrue(auditEvent.getSubtype().stream()
            .anyMatch(subtype -> RESTFUL_INTERACTION_SYSTEM.equals(subtype.getSystem()) && interaction.equals(subtype.getCode())));
        var transaction = auditEvent.getSubtype().stream()
            .filter(subtype -> IHE_TRANSACTION_SYSTEM.equals(subtype.getSystem()))
            .findFirst()
            .orElseThrow();
        assertEquals("ITI-104", transaction.getCode());
        assertEquals("Patient Identity Feed FHIR", transaction.getDisplay());
    }

    private static void assertPatientAndData(AuditEvent auditEvent, String patientValue) {
        var patient = auditEvent.getEntity().stream()
            .filter(entity -> "1".equals(entity.getType().getCode()) && "1".equals(entity.getRole().getCode()))
            .findFirst()
            .orElseThrow();
        assertEquals(SYSTEM, patient.getWhat().getIdentifier().getSystem());
        assertEquals(patientValue, patient.getWhat().getIdentifier().getValue());

        var data = auditEvent.getEntity().stream()
            .filter(entity -> "2".equals(entity.getType().getCode()))
            .findFirst()
            .orElseThrow();
        assertEquals("Patient/" + patientValue, data.getWhat().getReference());
    }

    @AfterEach
    public void validateRecordedAuditEvents() {
        BalpAuditEventValidator.sharedInstance(PixmValidator.PIXM_PACKAGE_PATH, PdqmValidator.PDQM_PACKAGE_PATH)
            .assertAllConformant(FhirAuditRepository.getAuditEvents());
    }
}
