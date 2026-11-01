/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.iti47;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformerTest;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

/**
 * ITI-47 round trip using the simplified model on both sides: the client sends a {@link PdqV3QueryRequest} and
 * receives a {@link PdqV3QueryResponse}; the server route ({@link Iti47ModelRouteBuilder}) receives the request as
 * {@link PdqV3QueryRequest} and answers with a {@link PdqV3QueryResponse}. The conversion from and to XML is done by
 * the type converters of this module, and the server route validates the XML in both directions.
 */
public class TestIti47Model extends StandardTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-47-model.xml";

    private String endpoint() {
        return "pdqv3-iti47://localhost:" + getPort() + "/pdqv3-iti47-model-service";
    }

    @BeforeAll
    public static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testDataFound() {
        final var request = PdqV3QueryRequestTransformerTest.getSampleQuery();

        final var response = send(endpoint(), request, PdqV3QueryResponse.class);

        // Acknowledgement
        assertEquals("AA", response.getAcknowledgementTypeCode());
        assertEquals("OK", response.getQueryResponseCode());
        assertIiEquals(request.getMessageId(), response.getTargetMessageId());
        assertIiEquals(request.getQueryId(), response.getQueryId());
        assertDeviceEquals(request.getSender(), response.getReceiver());
        assertDeviceEquals(request.getReceiver(), response.getSender());

        // Echoed query parameters
        assertEquals(request.getLivingSubjectIds().size(), response.getLivingSubjectIds().size());
        assertEquals(request.getOtherIDsScopingOrganizationOids(), response.getOtherIDsScopingOrganizationOids());
        assertEquals(request.getInitialQuantity(), response.getInitialQuantity());

        // Matched patients, built by the server route from the query parameters
        assertEquals(2, response.getPatients().size());
        for (var i = 0; i < 2; i++) {
            final var patient = response.getPatients().get(i);
            assertIiEquals(request.getLivingSubjectIds().get(i), patient.getPatientIds().get(0));
            assertEquals("Jones", patient.getNames().get(0).getFamily().get(0).getListStringValues().get(0));
            assertEquals("M", patient.getAdministrativeGenderCode());
            assertEquals("19630804", patient.getBirthTime());
            assertEquals(100, patient.getMatchDegree());
            assertEquals(Iti47ModelRouteBuilder.PROVIDER_ORGANIZATION_OID, patient.getCustodianOid());
            assertEquals(Iti47ModelRouteBuilder.PROVIDER_ORGANIZATION_OID,
                    patient.getProviderOrganization().getId().get(0).getRoot());
        }

        assertAudit();
    }

    @Test
    public void testNoDataFound() {
        final var request = PdqV3QueryRequestTransformerTest.getSampleQuery();
        request.getLivingSubjectIds().clear();

        final var response = send(endpoint(), request, PdqV3QueryResponse.class);

        assertEquals("AA", response.getAcknowledgementTypeCode());
        assertEquals("NF", response.getQueryResponseCode());
        assertIiEquals(request.getMessageId(), response.getTargetMessageId());
        assertTrue(response.getPatients().isEmpty());

        assertAudit();
    }

    @Test
    public void testInvalidRequest() {
        final var request = PdqV3QueryRequestTransformerTest.getSampleQuery();
        // not a valid OID, rejected by the XML schema
        request.getOtherIDsScopingOrganizationOids().add("4.5.6");

        final var response = send(endpoint(), request, PdqV3QueryResponse.class);

        // The validation failure is turned into a NAK ("Query Parameter Error") by the HL7v3 consumer,
        // which arrives as simplified model, too
        assertEquals("AE", response.getAcknowledgementTypeCode());
        assertEquals("QE", response.getQueryResponseCode());
        assertIiEquals(request.getMessageId(), response.getTargetMessageId());
        assertTrue(response.getPatients().isEmpty());
        assertFalse(response.getAcknowledgementDetails().isEmpty());
    }

    /**
     * Client and server have both written a successful audit record.
     */
    private static void assertAudit() {
        final var messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());
        messages.forEach(message -> assertEquals(EventOutcomeIndicator.Success,
                message.getEventIdentification().getEventOutcomeIndicator()));
    }
}
