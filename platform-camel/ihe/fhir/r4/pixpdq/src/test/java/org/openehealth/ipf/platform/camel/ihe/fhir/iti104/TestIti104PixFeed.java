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
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ITI-104 proxied to a PIX Feed (ITI-8), whose messages are validated against the ITI-8 conformance profile
 */
public class TestIti104PixFeed extends AbstractTestIti104 {

    private static final String CONTEXT_DESCRIPTOR = "iti-104-pixfeed.xml";

    @BeforeAll
    public static void setUpClass() {
        startServer(CONTEXT_DESCRIPTOR);
    }

    /**
     * Revised as A08, which does not tell whether the PIX Manager has created the patient
     */
    @Test
    public void testAddRevisePatient() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.addIdentifier().setSystem("urn:oid:1.2.3.4").setValue("4711");
        var outcome = sendConditionalUpdate(patient, EXISTING_PATIENT);
        assertNotEquals(Boolean.TRUE, outcome.getCreated());

        var events = getAuditSender().getMessages();
        assertEquals(1, events.size());
        assertEquals(EventActionCode.Update, events.get(0).getEventIdentification().getEventActionCode());
        assertEquals(EventOutcomeIndicator.Success, events.get(0).getEventIdentification().getEventOutcomeIndicator());
    }

    @Test
    public void testResolveDuplicatePatient() {
        var patient = pixmPatient(EXISTING_PATIENT).addReplacedByLink(identifier("IHERED-1000"));
        var outcome = sendConditionalUpdate(patient, EXISTING_PATIENT);
        assertNotEquals(Boolean.TRUE, outcome.getCreated());
    }

    @Test
    public void testRemovePatientIsNotSupported() {
        assertThrows(NotImplementedOperationException.class, () -> sendConditionalDelete(EXISTING_PATIENT));
    }

    @Test
    public void testPatientUnknownToPixManager() {
        assertThrows(InvalidRequestException.class, () ->
            sendConditionalUpdate(pixmPatient(Iti104PixFeedTestRouteBuilder.REJECTED_PATIENT), Iti104PixFeedTestRouteBuilder.REJECTED_PATIENT));
    }

    @Test
    public void testUnmappableIdentifierSystem() {
        var patient = pixmPatient(EXISTING_PATIENT);
        patient.getIdentifier().clear();
        patient.addIdentifier().setSystem("http://example.org/patients").setValue(EXISTING_PATIENT);
        assertThrows(InvalidRequestException.class, () -> client.update()
            .resource(patient)
            .conditional()
            .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("http://example.org/patients", EXISTING_PATIENT))
            .execute());
    }
}
