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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti65.v421;

import org.hl7.fhir.r4.model.AuditEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti65WithBalpAudit extends AbstractTestIti65 {

    private static final String CONTEXT_DESCRIPTOR = "iti-65-balp.xml";

    @BeforeAll
    public static void setUpClass() {
        setup(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.getAuditEvents().clear();
    }

    @Test
    public void testSendManualMhd() throws Exception {
        sendManually(provideAndRegister());

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        AuditEvent auditEvent = auditEvents.get(0);
        assertEquals("110107", auditEvent.getType().getCode());
        assertEquals("Import", auditEvent.getType().getDisplay());
        assertEquals("ITI-65", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("C", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());
        Optional<AuditEvent.AuditEventAgentComponent> sourceRole = findRoleAgentWithCode(auditEvent, "110153");
        assertTrue(sourceRole.isPresent());
        assertTrue(sourceRole.get().getRequestor());
        Optional<AuditEvent.AuditEventAgentComponent> destinationRole = findRoleAgentWithCode(auditEvent, "110152");
        assertTrue(destinationRole.isPresent());
        assertFalse(destinationRole.get().getRequestor());
        assertEquals(1, auditEvent.getEntity().stream()
            .filter(event -> event.getType().getCode().equals("2") && event.getRole().getCode().equals("20"))
            .count());
    }

    @Test
    public void testSendEndpointMhd() throws Exception {
        var bundle = provideAndRegister();
        sendViaProducer(bundle);

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(2, auditEvents.size());
    }

    private Optional<AuditEvent.AuditEventAgentComponent> findRoleAgentWithCode(AuditEvent auditEvent, String code) {
        return auditEvent.getAgent().stream()
            .filter(p -> p.getType().getCodingFirstRep().getCode().equals(code))
            .findFirst();
    }
}
