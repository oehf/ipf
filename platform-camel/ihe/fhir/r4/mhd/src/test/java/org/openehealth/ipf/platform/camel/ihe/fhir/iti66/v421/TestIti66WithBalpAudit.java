/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66.v421;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti66WithBalpAudit extends AbstractTestIti66 {

    private static final String CONTEXT_DESCRIPTOR = "v421/iti-66-balp.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testSendManualIti66() {
        var result = sendManually(listPatientIdentifierParameter(), statusParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (ListResource) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());

        assertEquals(1, FhirAuditRepository.getAuditEvents().size());
        AuditEvent auditEvent = FhirAuditRepository.getAuditEvents().get(0);
        assertEquals("110112", auditEvent.getType().getCode());
        assertEquals("Query", auditEvent.getType().getDisplay());
        assertEquals("ITI-66", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        Optional<AuditEvent.AuditEventAgentComponent> sourceRole = findRoleAgentWithCode(auditEvent, "110153");
        assertTrue(sourceRole.isPresent());
        assertTrue(sourceRole.get().getRequestor());
        Optional<AuditEvent.AuditEventAgentComponent> destinationRole = findRoleAgentWithCode(auditEvent, "110152");
        assertTrue(destinationRole.isPresent());
        assertFalse(destinationRole.get().getRequestor());
        assertEquals(1, auditEvent.getEntity().stream()
            .filter(event -> event.getType().getCode().equals("2") && event.getRole().getCode().equals("24"))
            .map(AuditEvent.AuditEventEntityComponent::getQuery)
            .filter(Objects::nonNull)
            .count());
    }

    @Test
    public void testSendIti66WithPatientReference() {
        var result = sendViaProducer(listPatientReferenceParameter(), statusParameter());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (ListResource) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());
        assertEquals(2, FhirAuditRepository.getAuditEvents().size());
    }

    private Optional<AuditEvent.AuditEventAgentComponent> findRoleAgentWithCode(AuditEvent auditEvent, String code) {
        return auditEvent.getAgent().stream()
            .filter(p -> p.getType().getCodingFirstRep().getCode().equals(code))
            .findFirst();
    }
}