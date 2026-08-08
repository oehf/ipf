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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import java.util.List;
import java.util.Optional;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-83 audited with the AuditEvents profiled in PIXm, i.e. through
 * {@code PixmConsumerAuditEvent} / {@code PixmManagerAuditEvent} rather than through the generic
 * AuditMessage-to-AuditEvent translation.
 * <p>
 * IHE.PIXm.Query.Audit.Consumer/.Manager build on the BALP PatientQuery pattern, so on top of what the
 * pattern fixes, both the query and the patient entity are required -- the patient being the one the
 * source identifier of the query names.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti83WithBalpAudit extends AbstractTestIti83 {

    private static final String CONTEXT_DESCRIPTOR = "iti-83-balp.xml";

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
    public void testSendManualIti83() {
        var result = sendManuallyOnType(validQueryParameters());
        assertNotNull(result);

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        // the BALP query pattern fixes these
        assertEquals("rest", auditEvent.getType().getCode());
        assertEquals("RESTful Operation", auditEvent.getType().getDisplay());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        assertNotNull(auditEvent.getRecorded());

        // ... and PIXm adds the transaction on top of the search subtype the pattern requires
        assertTrue(hasSubtype(auditEvent, RESTFUL_INTERACTION_SYSTEM, "search"),
            "the search subtype the BALP query pattern requires is missing: " + subtypes(auditEvent));
        assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-83"),
            "the ITI-83 subtype is missing: " + subtypes(auditEvent));

        // client is the Cross-reference Consumer, server the Cross-reference Manager
        assertTrue(agentWithTypeCode(auditEvent, "110153").isPresent(), "no client agent");
        var server = agentWithTypeCode(auditEvent, "110152");
        assertTrue(server.isPresent(), "no server agent");

        // val-audit-source: the observer is the agent of the end that wrote the record, here the server
        assertEquals(server.get().getWho().getDisplay(), auditEvent.getSource().getObserver().getDisplay());
        assertEquals("IPF", auditEvent.getSource().getSite());
    }

    /**
     * PIXm requires both entities: the query, and the patient the source identifier names.
     */
    @Test
    public void testTheQueryAndPatientEntitiesArePresent() {
        sendManuallyOnType(validQueryParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);

        assertEquals(1, entitiesWithTypeAndRole(auditEvent, "2", "24").size(),
            "the query entity is missing: " + entityDescription(auditEvent));

        var patients = entitiesWithTypeAndRole(auditEvent, "1", "1");
        assertEquals(1, patients.size(), "the patient entity is missing: " + entityDescription(auditEvent));
        // PIXm requires the patient entity to identify the patient rather than merely display it, so the
        // FHIR token of the ATNA record is taken apart into a system and a value
        var patient = patients.get(0).getWhat().getIdentifier();
        assertEquals("urn:oid:1.2.3.4", patient.getSystem());
        assertEquals("0815", patient.getValue());
    }

    /**
     * Going through the producer audits both ends, exercising the consumer's and the manager's
     * AuditEvent.
     */
    @Test
    public void testSendViaProducerAuditsBothEnds() {
        assertNotNull(sendViaProducer(validQueryParameters()));

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(2, auditEvents.size());

        for (var auditEvent : auditEvents) {
            assertEquals("rest", auditEvent.getType().getCode());
            assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-83"));
            assertTrue(agentWithTypeCode(auditEvent, "110153").isPresent(), "no client agent");
            assertTrue(agentWithTypeCode(auditEvent, "110152").isPresent(), "no server agent");
            assertEquals(1, entitiesWithTypeAndRole(auditEvent, "2", "24").size(), "query entity missing");
            assertEquals(1, entitiesWithTypeAndRole(auditEvent, "1", "1").size(), "patient entity missing");
        }

        // val-audit-source: each end names itself as the observer, so the two records differ in it
        for (var auditEvent : auditEvents) {
            var localAgentCode = isConsumerRecord(auditEvent) ? "110153" : "110152";
            assertEquals(
                agentWithTypeCode(auditEvent, localAgentCode).orElseThrow().getWho().getDisplay(),
                auditEvent.getSource().getObserver().getDisplay(),
                "the observer is not the agent of the end that wrote the record");
        }
    }

    @Test
    public void testTheRequestorFlagComesFromTheAuditRecord() {
        sendManuallyOnType(validQueryParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);

        assertTrue(agentWithTypeCode(auditEvent, "110153").orElseThrow().getRequestor());
        assertFalse(agentWithTypeCode(auditEvent, "110152").orElseThrow().getRequestor());
    }



    /**
     * The profile each AuditEvent claims is the transaction's own, not the BALP pattern its class
     * inherits from: HAPI writes the {@code @ResourceDef} profile of the concrete class into
     * meta.profile, which is what marks the record as PIXm conformant on the wire.
     */
    @Test
    public void testEachEndClaimsItsOwnProfile() {
        sendViaProducer(validQueryParameters());

        var profiles = FhirAuditRepository.getAuditEvents().stream()
            .flatMap(auditEvent -> auditEvent.getMeta().getProfile().stream())
            .map(CanonicalType::getValue)
            .sorted()
            .toList();
        assertEquals(List.of(
            "https://profiles.ihe.net/ITI/PIXm/StructureDefinition/IHE.PIXm.Query.Audit.Consumer",
            "https://profiles.ihe.net/ITI/PIXm/StructureDefinition/IHE.PIXm.Query.Audit.Manager"),
            profiles);
    }



    /**
     * PIXm fixes the transaction subtype as a pattern of system, code <em>and</em> display, so a
     * subtype without a display does not match the profile.
     */
    @Test
    public void testTheTransactionSubtypeCarriesItsDisplay() {
        sendManuallyOnType(validQueryParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);

        var transactionSubtype = auditEvent.getSubtype().stream()
            .filter(subtype -> IHE_TRANSACTION_SYSTEM.equals(subtype.getSystem()))
            .findFirst()
            .orElseThrow();
        assertEquals("ITI-83", transactionSubtype.getCode());
        assertEquals("Mobile Patient Identifier Cross-reference Query", transactionSubtype.getDisplay());
    }

    private static List<AuditEvent.AuditEventEntityComponent> entitiesWithTypeAndRole(AuditEvent auditEvent,
                                                                                      String type,
                                                                                      String role) {
        return auditEvent.getEntity().stream()
            .filter(entity -> type.equals(entity.getType().getCode()) && role.equals(entity.getRole().getCode()))
            .toList();
    }

    private static String entityDescription(AuditEvent auditEvent) {
        return auditEvent.getEntity().stream()
            .map(entity -> entity.getType().getCode() + "/" + entity.getRole().getCode())
            .toList()
            .toString();
    }

    private static boolean isConsumerRecord(AuditEvent auditEvent) {
        return auditEvent.getMeta().getProfile().stream()
            .anyMatch(profile -> profile.getValueAsString().endsWith("Consumer"));
    }

    private static Optional<AuditEvent.AuditEventAgentComponent> agentWithTypeCode(AuditEvent auditEvent, String code) {
        return auditEvent.getAgent().stream()
            .filter(agent -> code.equals(agent.getType().getCodingFirstRep().getCode()))
            .findFirst();
    }

    private static boolean hasSubtype(AuditEvent auditEvent, String system, String code) {
        return auditEvent.getSubtype().stream()
            .anyMatch(subtype -> system.equals(subtype.getSystem()) && code.equals(subtype.getCode()));
    }

    private static List<String> subtypes(AuditEvent auditEvent) {
        return auditEvent.getSubtype().stream()
            .map(subtype -> subtype.getSystem() + "|" + subtype.getCode())
            .toList();
    }

    /**
     * Whatever a test in this class did, the AuditEvents it caused have to conform to the profiles they
     * claim -- checked here rather than per test, so that a new test is covered without having to say so.
     */
    @AfterEach
    public void validateRecordedAuditEvents() {
        BalpAuditEventValidator.sharedInstance(PixmValidator.PIXM_PACKAGE_PATH, PdqmValidator.PDQM_PACKAGE_PATH)
            .assertAllConformant(FhirAuditRepository.getAuditEvents());
    }

}
