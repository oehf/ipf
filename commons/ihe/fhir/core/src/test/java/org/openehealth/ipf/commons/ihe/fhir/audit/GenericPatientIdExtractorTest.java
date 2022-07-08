/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericPatientIdExtractorTest {

    private final FhirContext fhirContext = FhirContext.forR4Cached();
    private final PatientIdExtractor extractor = new GenericPatientIdExtractor(fhirContext);

    @Test
    public void extractPatientReference() {
        var dr = new DocumentReference()
                .setSubject(new Reference("Patient/0815"));
        var extracted = extractor.patientReferenceFromResource(dr)
                .orElseThrow(() -> new AssertionError("Expected patient reference"));
        assertEquals(dr.getSubject().getReferenceElement().getValue(), extracted.getReferenceElement().getValue());
    }

    @Test
    public void extractPatientId() {
        var p = new Patient();
        p.setId("Patient/0815");
        var extracted = extractor.patientReferenceFromResource(p)
                .orElseThrow(() -> new AssertionError("Expected patient reference"));
        assertEquals(p.getId(), extracted.getReferenceElement().getValue());
    }
}
