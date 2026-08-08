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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-78 audited with the AuditEvents profiled in PDQm, i.e. through
 * {@code PdqmConsumerAuditEvent} / {@code PdqmSupplierAuditEvent} rather than through the generic
 * AuditMessage-to-AuditEvent translation.
 * <p>
 * The assertions follow what IHE.PDQm.Query.Audit.Consumer/.Supplier constrain on top of the BALP
 * Query pattern: the RESTful event type, the search subtype next to the ITI-78 subtype, a client and
 * a server agent, and the query entity -- which is required, and which carries the raw search request
 * rather than a base64 rendering of it.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti78WithBalpAudit extends AbstractTestIti78 {

    private static final String CONTEXT_DESCRIPTOR = "iti-78-balp.xml";

    private static final String IHE_TRANSACTION_SYSTEM = "urn:ihe:event-type-code";
    private static final String RESTFUL_INTERACTION_SYSTEM = "http://hl7.org/fhir/restful-interaction";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR, false);
        startClient();
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testSendManualIti78() {
        var result = sendManually(familyParameters());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertTrue(result.hasEntry());

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        // the BALP Query pattern fixes these
        assertEquals("rest", auditEvent.getType().getCode());
        assertEquals("RESTful Operation", auditEvent.getType().getDisplay());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        assertNotNull(auditEvent.getRecorded());

        // ... and PDQm adds the transaction on top of the search subtype the pattern requires
        assertTrue(hasSubtype(auditEvent, RESTFUL_INTERACTION_SYSTEM, "search"),
            "the search subtype the BALP Query pattern requires is missing: " + subtypes(auditEvent));
        assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-78"),
            "the ITI-78 subtype is missing: " + subtypes(auditEvent));

        // client is the Patient Demographics Consumer, server the Supplier
        var client = agentWithTypeCode(auditEvent, "110153");
        assertTrue(client.isPresent(), "no client agent");
        assertTrue(client.get().hasNetwork());
        var server = agentWithTypeCode(auditEvent, "110152");
        assertTrue(server.isPresent(), "no server agent");
        assertTrue(server.get().hasNetwork());

        // val-audit-source: the observer is the agent of the end that wrote the record, here the server
        assertEquals(server.get().getWho().getDisplay(), auditEvent.getSource().getObserver().getDisplay());
        assertEquals("IPF", auditEvent.getSource().getSite());
    }

    /**
     * The query entity is required by the profile, and used to be missing entirely for PDQm. It holds
     * the raw search request: {@code entity.query} is a base64Binary element, so encoding it before
     * handing it to FHIR would leave the recipient with base64 text instead of the query.
     */
    @Test
    public void testTheQueryEntityCarriesTheRawQuery() {
        sendManually(familyParameters());

        var queries = queryEntities(FhirAuditRepository.getAuditEvents().get(0));
        assertEquals(1, queries.size(), "expected exactly one query entity");
        assertEquals("family=Test&_format=xml",
            new String(queries.get(0).getQuery(), StandardCharsets.UTF_8));
    }

    /**
     * Going through the producer audits both ends, so both profiled AuditEvents are exercised: the
     * consumer's and the supplier's.
     */
    @Test
    public void testSendViaProducerAuditsBothEnds() {
        var result = sendViaProducer(familyParameters());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(2, auditEvents.size());

        for (var auditEvent : auditEvents) {
            assertEquals("rest", auditEvent.getType().getCode());
            assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-78"));
            assertTrue(agentWithTypeCode(auditEvent, "110153").isPresent(), "no client agent");
            assertTrue(agentWithTypeCode(auditEvent, "110152").isPresent(), "no server agent");
            assertEquals(1, queryEntities(auditEvent).size(), "query entity missing");
        }

        // val-audit-source: the observer is one of the agents of the record -- the end that wrote it --
        // rather than the audit source id of the audit context
        for (var auditEvent : auditEvents) {
            var agents = auditEvent.getAgent().stream()
                .map(agent -> agent.getWho().getDisplay())
                .toList();
            var observer = auditEvent.getSource().getObserver().getDisplay();
            assertTrue(agents.contains(observer), "the observer is not one of the agents: " + agents);
            // the consumer used to name itself "UNKNOWN", which the invariant then propagated to the
            // observer of every client side record
            assertNotEquals("UNKNOWN", observer, "the client does not know its own identity");
        }
    }

    /**
     * The requestor flag is taken from the ATNA record rather than assumed: the consumer is the
     * requestor of an ITI-78, the supplier is not.
     */
    @Test
    public void testTheRequestorFlagComesFromTheAuditRecord() {
        sendManually(familyParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);

        assertTrue(agentWithTypeCode(auditEvent, "110153").orElseThrow().getRequestor());
        assertFalse(agentWithTypeCode(auditEvent, "110152").orElseThrow().getRequestor());
    }


    /**
     * A query by identifier names the patient it is about, unlike one by demographics, so the record
     * satisfies the BALP PatientQuery pattern the PDQm profiles derive from and claims one of them.
     * The profile it claims is the transaction's own, not the pattern its class inherits from: HAPI
     * writes the {@code @ResourceDef} profile of the concrete class into meta.profile, which is what
     * marks the record as PDQm conformant on the wire.
     */
    @Test
    public void testAQueryByIdentifierClaimsThePdqmProfile() {
        sendManually(identifierParameters());

        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);
        assertEquals(1, entitiesWithTypeAndRole(auditEvent, "1", "1").size(), "patient entity missing");
        assertEquals(List.of("https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Query.Audit.Supplier"),
            auditEvent.getMeta().getProfile().stream()
                .map(CanonicalType::getValue)
                .toList());
    }

    /**
     * Both ends record the patient of a query by identifier, so both claim their PDQm profile. The
     * consumer has to work harder for it: the patient comes from the parsed search parameters, which
     * only the server side has, so the client side reads it back out of the query string it sent.
     */
    @Test
    public void testBothEndsRecordThePatientOfAQueryByIdentifier() {
        sendViaProducer(identifierParameters());

        var auditEvents = FhirAuditRepository.getAuditEvents();
        auditEvents.forEach(auditEvent ->
            assertEquals(1, entitiesWithTypeAndRole(auditEvent, "1", "1").size(),
                "patient entity missing from " + auditEvent.getMeta().getProfile()));

        var profiles = auditEvents.stream()
            .flatMap(auditEvent -> auditEvent.getMeta().getProfile().stream())
            .map(CanonicalType::getValue)
            .sorted()
            .toList();
        assertEquals(List.of(
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Query.Audit.Consumer",
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Query.Audit.Supplier"),
            profiles);
    }

    @Test
    public void testADemographicsQueryKeepsTheProfileWithoutAPatient() {
        sendViaProducer(familyParameters());

        // A query by family name identifies no patient -- the matches only come back in the response.
        // The PDQm profiles derive from the plain BALP Query pattern, whose patient entity is optional
        // and used only "when one patient is explicitly identified in the query parameters", so both
        // ends stay PDQm conformant and simply leave the entity out.
        var profiles = FhirAuditRepository.getAuditEvents().stream()
            .flatMap(auditEvent -> auditEvent.getMeta().getProfile().stream())
            .map(CanonicalType::getValue)
            .sorted()
            .toList();
        assertEquals(List.of(
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Query.Audit.Consumer",
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Query.Audit.Supplier"),
            profiles);

        FhirAuditRepository.getAuditEvents().forEach(auditEvent ->
            assertTrue(entitiesWithTypeAndRole(auditEvent, "1", "1").isEmpty(),
                "there is no patient, so no patient entity may be recorded"));

        // the transaction is still named, so the records remain recognisable as ITI-78
        FhirAuditRepository.getAuditEvents().forEach(auditEvent ->
            assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-78")));
    }


    /**
     * PDQm fixes the transaction subtype as a pattern of system, code <em>and</em> display, so a
     * subtype without a display does not match the profile.
     */
    @Test
    public void testTheTransactionSubtypeCarriesItsDisplay() {
        sendManually(familyParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);

        var transactionSubtype = auditEvent.getSubtype().stream()
            .filter(subtype -> IHE_TRANSACTION_SYSTEM.equals(subtype.getSystem()))
            .findFirst()
            .orElseThrow();
        assertEquals("ITI-78", transactionSubtype.getCode());
        assertEquals("Mobile Patient Demographics Query", transactionSubtype.getDisplay());
    }

    private static List<AuditEvent.AuditEventEntityComponent> entitiesWithTypeAndRole(AuditEvent auditEvent,
                                                                                      String type,
                                                                                      String role) {
        return auditEvent.getEntity().stream()
            .filter(entity -> type.equals(entity.getType().getCode()) && role.equals(entity.getRole().getCode()))
            .toList();
    }

    private static List<AuditEvent.AuditEventEntityComponent> queryEntities(AuditEvent auditEvent) {
        return auditEvent.getEntity().stream()
            .filter(entity -> "2".equals(entity.getType().getCode()) && "24".equals(entity.getRole().getCode()))
            .filter(AuditEvent.AuditEventEntityComponent::hasQuery)
            .toList();
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
