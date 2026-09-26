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

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ITI-104 Manager without the Remove Patient Option, using the update-only resource provider
 */
public class TestIti104UpdateOnly extends AbstractTestIti104 {

    private static final String CONTEXT_DESCRIPTOR = "iti-104-update-only.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testDeleteIsNotAdvertised() {
        var conf = client.capabilities().ofType(CapabilityStatement.class).execute();
        var patient = conf.getRestFirstRep().getResource().stream()
            .filter(resource -> "Patient".equals(resource.getType()))
            .findFirst()
            .orElseThrow();
        assertTrue(patient.getConditionalUpdate());
        var interactions = patient.getInteraction().stream()
            .map(interaction -> interaction.getCode().toCode())
            .toList();
        assertTrue(interactions.contains("update"), interactions.toString());
        assertFalse(interactions.contains("delete"), interactions.toString());
    }

    @Test
    public void testUpdate() {
        sendConditionalUpdate(pixmPatient(EXISTING_PATIENT), EXISTING_PATIENT);
    }

    @Test
    public void testDeleteIsRejected() {
        assertThrows(BaseServerResponseException.class, () -> sendConditionalDelete(EXISTING_PATIENT));
    }
}
