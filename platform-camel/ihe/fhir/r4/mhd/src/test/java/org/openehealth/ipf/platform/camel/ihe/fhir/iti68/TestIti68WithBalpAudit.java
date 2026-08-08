/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti68;

import ca.uhn.fhir.rest.gclient.ICriterion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestIti68WithBalpAudit extends AbstractTestIti68 {

    private static final String CONTEXT_DESCRIPTOR = "iti-68-balp.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.clearAuditEvents();
    }

    @Test
    public void testRetrieveDocument() {
        var response = sendViaProducer((ICriterion<?>) null);
        assertArrayEquals(Iti68TestRouteBuilder.DATA, response);

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        // ITI-68 is audited with its BALP profiled AuditEvent, built on the PatientRead pattern
        assertEquals("rest", auditEvent.getType().getCode());
        assertEquals("RESTful Operation", auditEvent.getType().getDisplay());
        assertEquals("R", auditEvent.getAction().toCode());
        assertEquals("0", auditEvent.getOutcome().toCode());

        assertTrue(auditEvent.getSubtype().stream().anyMatch(subtype ->
            "urn:ihe:event-type-code".equals(subtype.getSystem())
                && "ITI-68".equals(subtype.getCode())
                && "Retrieve Document".equals(subtype.getDisplay())),
            "the ITI-68 subtype with its fixed display is missing");
        assertTrue(auditEvent.getSubtype().stream().anyMatch(subtype ->
            "read".equals(subtype.getCode())), "the read subtype the pattern requires is missing");

        // the read patterns give the client 110152 and the server 110153, unlike the query patterns
        assertTrue(auditEvent.getAgent().stream().anyMatch(agent ->
            "110152".equals(agent.getType().getCodingFirstRep().getCode())), "no client agent");
        assertTrue(auditEvent.getAgent().stream().anyMatch(agent ->
            "110153".equals(agent.getType().getCodingFirstRep().getCode())), "no server agent");

        // only the responder audits this exchange, so the record claims the responder's profile
        assertEquals("https://profiles.ihe.net/ITI/MHD/StructureDefinition/IHE.MHD.RetrieveDocument.Audit.Responder",
            auditEvent.getMeta().getProfile().get(0).getValue());
    }


}
