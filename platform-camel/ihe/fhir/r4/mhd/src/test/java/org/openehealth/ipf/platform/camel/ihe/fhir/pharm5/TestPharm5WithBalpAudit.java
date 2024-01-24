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
package org.openehealth.ipf.platform.camel.ihe.fhir.pharm5;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.ihe.fhir.extension.FhirAuditRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
@ExtendWith(FhirAuditRepository.class)
public class TestPharm5WithBalpAudit extends AbstractTestPharm5 {
    private static final String CONTEXT_DESCRIPTOR = "pharm-5-balp.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void beforeEach() {
        FhirAuditRepository.getAuditEvents().clear();
    }

    @Test
    void testPharm5FindMedicationTreatmentPlans() {
        var result = sendManually(findMedicationTreatmentPlanParameters());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());
        assertEquals("$find-medication-treatment-plans", p.getDescription());

        // Check ATNA Audit
        assertEquals(1, FhirAuditRepository.getAuditEvents().size());
        var auditEvent = FhirAuditRepository.getAuditEvents().get(0);
        assertEquals("110112", auditEvent.getType().getCode());
        assertEquals("PHARM-5", auditEvent.getSubtypeFirstRep().getCode());
        assertEquals("E", auditEvent.getAction().toCode());
        assertEquals("Query", auditEvent.getType().getDisplay());
        assertEquals("0", auditEvent.getOutcome().toCode());
    }

    @Test
    void testSendPharm5FindMedicationList() {
        var result = sendManually(findMedicationListParameters());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        var p = (DocumentReference) result.getEntry().get(0).getResource();
        assertEquals("63ab1c29-4225-11e6-9b33-0050569b0094", p.getIdElement().getIdPart());
        assertEquals("$find-medication-list", p.getDescription());

        assertEquals(1, FhirAuditRepository.getAuditEvents().size());
    }
}
