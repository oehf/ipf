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
        FhirAuditRepository.getAuditEvents().clear();
    }

    @Test
    public void testRetrieveDocument() {
        var response = sendViaProducer((ICriterion<?>) null);
        assertArrayEquals(Iti68TestRouteBuilder.DATA, response);

        // Check ATNA Audit
        var auditEvents = FhirAuditRepository.getAuditEvents();
        assertEquals(1, auditEvents.size());
        var auditEvent = auditEvents.get(0);

        assertEquals("110106", auditEvent.getType().getCode());
        assertEquals("ITI-68", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("C", auditEvent.getAction().toCode());
        assertEquals("Export", auditEvent.getType().getDisplay());
        assertEquals("0", auditEvent.getOutcome().toCode());
    }


}
