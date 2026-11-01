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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.iti44;

import net.ihe.gazelle.hl7v3.datatypes.II;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.hl7v3.core.Hl7v3MessageUtils;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientRecordTransformersTest;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertDeviceEquals;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest.assertIiEquals;

/**
 * ITI-44 round trip using the simplified model on both sides: the client sends {@link PatientRecordRequest}s
 * (record added, record revised, duplicates resolved) and receives an {@link AcceptAcknowledgement}; the server route
 * ({@link Iti44ModelRouteBuilder}) receives the request as the matching {@link PatientRecordRequest} subclass and
 * answers with an acknowledgement created by {@link Hl7v3MessageUtils#ack(PatientRecordRequest)}. The conversion from
 * and to XML is done by the type converters of this module, and the server route validates the XML in both
 * directions.
 */
public class TestIti44Model extends StandardTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-44-model.xml";

    private static final II PATIENT_ID = new II("1.2.840.114350.1.13.99997.2.3412", "38273N237");
    private static final II DUPLICATE_PATIENT_ID = new II("1.2.840.114350.1.13.99997.2.3412", "38273N238");

    private String endpoint() {
        return "pixv3-iti44://localhost:" + getPort() + "/pixv3-iti44-model-service";
    }

    private static Iti44ModelRouteBuilder registry() {
        return appContext.getBean(Iti44ModelRouteBuilder.class);
    }

    @BeforeAll
    public static void setUpClass() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
    }

    @BeforeEach
    public void clearRegistry() {
        registry().clear();
    }

    @Test
    public void testAddReviseMerge() {
        // Add a patient
        final var added = request(new PatientRecordAddedRequest(), PATIENT_ID);
        assertAccepted(added, send(endpoint(), added, AcceptAcknowledgement.class), 2);
        assertEquals("19630804", registry().getPatient(PATIENT_ID).getBirthTime());

        // Revise the patient
        final var revised = request(new PatientRecordRevisedRequest(), PATIENT_ID);
        revised.getPatient().setBirthTime("19630805");
        assertAccepted(revised, send(endpoint(), revised, AcceptAcknowledgement.class), 2);
        assertEquals("19630805", registry().getPatient(PATIENT_ID).getBirthTime());

        // Add a duplicate of the patient and merge it into the original one
        final var duplicate = request(new PatientRecordAddedRequest(), DUPLICATE_PATIENT_ID);
        assertAccepted(duplicate, send(endpoint(), duplicate, AcceptAcknowledgement.class), 2);
        assertNotNull(registry().getPatient(DUPLICATE_PATIENT_ID));

        final var merge = request(new PatientDuplicatesResolvedRequest(), PATIENT_ID);
        merge.getObsoletePatientIds().add(DUPLICATE_PATIENT_ID);
        // a merge is audited as update of the surviving and deletion of the obsolete patient, on both sides
        assertAccepted(merge, send(endpoint(), merge, AcceptAcknowledgement.class), 4);
        assertNotNull(registry().getPatient(PATIENT_ID));
        assertNull(registry().getPatient(DUPLICATE_PATIENT_ID));
    }

    @Test
    public void testReviseUnknownPatient() {
        final var revised = request(new PatientRecordRevisedRequest(), PATIENT_ID);

        final var ack = send(endpoint(), revised, AcceptAcknowledgement.class);

        assertEquals("AE", ack.getAcknowledgementTypeCode());
        assertIiEquals(revised.getMessageId(), ack.getTargetMessageId());
        assertEquals(1, ack.getAcknowledgementDetails().size());
        assertEquals("204", ack.getAcknowledgementDetails().get(0).getCode().getCode());
        assertNull(registry().getPatient(PATIENT_ID));

        final var messages = getAuditSender().getMessages();
        assertEquals(2, messages.size());
        messages.forEach(message -> assertNotEquals(EventOutcomeIndicator.Success,
                message.getEventIdentification().getEventOutcomeIndicator()));
    }

    private static <T extends PatientRecordRequest> T request(final T request, final II patientId) {
        PatientRecordTransformersTest.fill(request);
        request.setMessageId(Hl7v3MessageUtils.newMessageId());
        request.getPatient().getPatientIds().clear();
        request.getPatient().getPatientIds().add(patientId);
        return request;
    }

    /**
     * Checks a positive acknowledgement, and that client and server have written the expected number of successful
     * audit records.
     */
    private static void assertAccepted(final PatientRecordRequest request, final AcceptAcknowledgement ack,
                                       final int auditRecords) {
        assertEquals("AA", ack.getAcknowledgementTypeCode());
        assertIiEquals(request.getMessageId(), ack.getTargetMessageId());
        assertDeviceEquals(request.getSender(), ack.getReceiver());
        assertDeviceEquals(request.getReceiver(), ack.getSender());
        assertTrue(ack.getAcknowledgementDetails().isEmpty());

        final var messages = getAuditSender().getMessages();
        assertEquals(auditRecords, messages.size());
        messages.forEach(message -> assertEquals(EventOutcomeIndicator.Success,
                message.getEventIdentification().getEventOutcomeIndicator()));
        getAuditSender().clear();
    }
}
