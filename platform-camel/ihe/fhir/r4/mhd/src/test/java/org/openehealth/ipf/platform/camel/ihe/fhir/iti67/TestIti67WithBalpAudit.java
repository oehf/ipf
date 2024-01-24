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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti67;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti67WithBalpAudit extends AbstractTestIti67 {

    private static final String CONTEXT_DESCRIPTOR = "iti-67-balp.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.getAuditEvents().clear();
    }

    @Test
    public void testSendManualIti67() {
        var result = sendManually(referencePatientIdentifierParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        assertEquals("110112", auditEvent.getType().getCode());
        assertEquals("ITI-67", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("Query", auditEvent.getType().getDisplay());
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
    public void testSendEndpointIti67() {
        sendViaProducer(referencePatientIdentifierParameter());
        assertEquals(2, FhirAuditRepository.getAuditEvents().size());

        List<byte[]> queries = FhirAuditRepository.getAuditEvents().stream().flatMap(event -> event.getEntity().stream()
            .filter(entity -> entity.getType().getCode().equals("2") && entity.getRole().getCode().equals("24"))
            .map(AuditEvent.AuditEventEntityComponent::getQuery)
            .filter(Objects::nonNull)).collect(Collectors.toList());

        String query = new String(queries.get(0), StandardCharsets.UTF_8);
        assertTrue(query.startsWith("patient.identifier=urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1"));
    }

    private Optional<AuditEvent.AuditEventAgentComponent> findRoleAgentWithCode(AuditEvent auditEvent, String code) {
        return auditEvent.getAgent().stream()
            .filter(p -> p.getType().getCodingFirstRep().getCode().equals(code))
            .findFirst();
    }

}
