/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti104;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ITI-104 requests that are not conditional on the patient identifier, or carry an invalid Patient
 */
public class TestIti104Rejections extends AbstractTestIti104 {

    private static final String CONTEXT_DESCRIPTOR = "iti-104.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testUpdateByLogicalId() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.setId("123");
        assertThrows(InvalidRequestException.class, () -> client.update()
            .resource(patient)
            .withId("123")
            .execute());
    }

    @Test
    public void testDeleteByLogicalId() {
        assertThrows(InvalidRequestException.class, () -> client.delete()
            .resourceById("Patient", "123")
            .execute());
    }

    @Test
    public void testDeleteConditionalOnName() {
        assertThrows(InvalidRequestException.class, () -> client.delete()
            .resourceConditionalByType(Patient.class)
            .where(Patient.NAME.matches().value("Moore"))
            .execute());
    }

    @Test
    public void testUpdateConditionalOnIdentifierWithoutSystem() {
        assertThrows(InvalidRequestException.class, () -> client.update()
            .resource(pixmPatient(EXISTING_PATIENT))
            .conditional()
            .where(Patient.IDENTIFIER.exactly().code(EXISTING_PATIENT))
            .execute());
    }

    /**
     * The PIXm Patient profile requires a name
     */
    @Test
    public void testInvalidPixmPatient() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.getName().clear();
        assertThrows(UnprocessableEntityException.class, () -> sendConditionalUpdate(patient, EXISTING_PATIENT));
    }
}
