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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import java.util.List;
import java.util.Optional;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.HumanName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmValidator;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmValidator;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchInputParameters;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchInputPatient;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.validate.BalpAuditEventValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-119 audited with the AuditEvents profiled in PDQm, i.e. through
 * {@code PdqmMatchConsumerAuditEvent} / {@code PdqmMatchSupplierAuditEvent} rather than through the
 * generic AuditMessage-to-AuditEvent translation.
 * <p>
 * IHE.PDQm.Match.Audit.Consumer/.Supplier build on the BALP Query pattern, like the PDQm query itself:
 * the query entity is required, and a patient entity is used when the match names one.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti119WithBalpAudit extends AbstractTestIti119 {

    private static final String CONTEXT_DESCRIPTOR = "iti-119-balp.xml";

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

    private PdqmMatchInputParameters matchParameters() {
        var resource = new PdqmMatchInputPatient();
        resource.addName(new HumanName().setFamily("Test"));
        return new PdqmMatchInputParameters()
            .setResourceParameter(resource)
            .setCount(1);
    }

    @Test
    public void testSendManualIti119() {
        assertNotNull(sendManually(matchParameters()));

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        assertEquals("rest", auditEvent.getType().getCode());
        assertEquals("RESTful Operation", auditEvent.getType().getDisplay());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        assertNotNull(auditEvent.getRecorded());

        assertTrue(hasSubtype(auditEvent, RESTFUL_INTERACTION_SYSTEM, "search"),
            "the search subtype the BALP Query pattern requires is missing: " + subtypes(auditEvent));
        assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-119"),
            "the ITI-119 subtype is missing: " + subtypes(auditEvent));

        assertTrue(agentWithTypeCode(auditEvent, "110153").isPresent(), "no client agent");
        var server = agentWithTypeCode(auditEvent, "110152");
        assertTrue(server.isPresent(), "no server agent");
        // val-audit-source: the observer is the agent of the end that wrote the record, here the server
        assertEquals(server.get().getWho().getDisplay(), auditEvent.getSource().getObserver().getDisplay());
    }

    /**
     * PDQm fixes the transaction subtype as a pattern of system, code <em>and</em> display.
     */
    @Test
    public void testTheTransactionSubtypeCarriesItsDisplay() {
        sendManually(matchParameters());
        var transactionSubtype = FhirAuditRepository.getAuditEvents().get(0).getSubtype().stream()
            .filter(subtype -> IHE_TRANSACTION_SYSTEM.equals(subtype.getSystem()))
            .findFirst()
            .orElseThrow();
        assertEquals("ITI-119", transactionSubtype.getCode());
        assertEquals("Patient Demographics Match", transactionSubtype.getDisplay());
    }

    @Test
    public void testTheQueryEntityIsPresent() {
        sendManually(matchParameters());
        assertEquals(1, queryEntities(FhirAuditRepository.getAuditEvents().get(0)).size(),
            "the query entity the profile requires is missing");
    }

    @Test
    public void testSendViaProducerAuditsBothEnds() {
        assertNotNull(sendViaProducer(matchParameters()));

        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(2, auditEvents.size());
        for (var auditEvent : auditEvents) {
            assertTrue(hasSubtype(auditEvent, IHE_TRANSACTION_SYSTEM, "ITI-119"));
            assertTrue(agentWithTypeCode(auditEvent, "110153").isPresent(), "no client agent");
            assertTrue(agentWithTypeCode(auditEvent, "110152").isPresent(), "no server agent");
        }

        // ITI-119 is a POST $match whose parameters live in the body rather than in a query string, so
        // both ends record that body as the query entity the profile requires.
        var withQuery = auditEvents.stream().filter(auditEvent -> !queryEntities(auditEvent).isEmpty()).toList();
        assertEquals(2, withQuery.size(), "both ends must record the query");

        // A $match by candidate demographics identifies no patient -- the candidates come back in the
        // response. The PDQm profiles allow that, so both records keep the transaction profile.
        var profiles = auditEvents.stream()
            .flatMap(auditEvent -> auditEvent.getMeta().getProfile().stream())
            .map(CanonicalType::getValue)
            .sorted()
            .toList();
        assertEquals(List.of(
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Match.Audit.Consumer",
            "https://profiles.ihe.net/ITI/PDQm/StructureDefinition/IHE.PDQm.Match.Audit.Supplier"),
            profiles);
    }

    @Test
    public void testTheRequestorFlagComesFromTheAuditRecord() {
        sendManually(matchParameters());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);
        assertTrue(agentWithTypeCode(auditEvent, "110153").orElseThrow().getRequestor());
        assertFalse(agentWithTypeCode(auditEvent, "110152").orElseThrow().getRequestor());
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
