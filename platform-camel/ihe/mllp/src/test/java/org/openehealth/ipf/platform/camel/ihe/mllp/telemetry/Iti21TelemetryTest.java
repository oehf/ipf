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
package org.openehealth.ipf.platform.camel.ihe.mllp.telemetry;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HL7v2 has no transport that could carry trace context, so this is the one family where IPF propagates
 * it itself, in a nonstandard segment. The test drives a real ITI-21 transaction over MLLP and checks
 * both halves of that mechanism: that the producer writes the context the span decorator handed it into
 * the outgoing message, and that the consumer offers it back to camel-telemetry when it creates the span
 * for the incoming message.
 * <p>
 * No tracing library takes part -- the tracer here only injects a known value and records what was
 * extracted -- so what is observed is IPF's propagation and nothing else.
 *
 * @author Christian Ohr
 */
@CamelSpringTest
@ContextConfiguration("/telemetry/iti-21-telemetry.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class Iti21TelemetryTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    // JUnit creates one test instance per method, so the tracer and its route are installed once
    private static TraceContextRecordingTracer tracer;

    @BeforeEach
    public void setUp() throws Exception {
        Iti21TelemetryRouteBuilder.receivedMessage = null;
        if (tracer == null) {
            tracer = new TraceContextRecordingTracer();
            tracer.init(camelContext);
            camelContext.addRoutes(Iti21TelemetryRouteBuilder.consumerRoute());
        }
        tracer.clear();
    }

    private void sendQuery() {
        var request = "MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|"
                + "20261231235959||QBP^Q22|123456|P|2.5\r"
                + "QPD|IHE PDQ Query|Q0001|@PID.5.1^SMITH\r"
                + "RCP|I\r";
        producerTemplate.requestBody(
                "pdq-iti21://localhost:" + Iti21TelemetryRouteBuilder.PORT + "?audit=false", request);
    }

    @Test
    public void testProducerWritesTheTraceContextIntoTheMessage() {
        sendQuery();

        var received = Iti21TelemetryRouteBuilder.receivedMessage;
        assertNotNull(received, "the consumer did not record the message it received");
        assertTrue(received.contains(Hl7v2TraceContext.SEGMENT_NAME + "|"),
                "the message that went over the wire carries no trace context segment:\n" + received);
        assertEquals(TraceContextRecordingTracer.VALUE,
                Iti21TelemetryRouteBuilder.receivedTraceContext().get(TraceContextRecordingTracer.KEY),
                "the trace context in the message is not the one the tracer injected:\n" + received);
    }

    @Test
    public void testConsumerOffersTheTraceContextToTheTracer() {
        sendQuery();

        var extractedOnConsumerSide = tracer.getExtracted().entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("/SERVER"))
                .map(java.util.Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "no consumer span was created, only " + tracer.getExtracted().keySet()));

        assertEquals(TraceContextRecordingTracer.VALUE,
                extractedOnConsumerSide.get(TraceContextRecordingTracer.KEY),
                "the consumer did not offer the sender's trace context, so the span would start a new "
                        + "trace instead of joining: " + extractedOnConsumerSide);
    }

    /** The segment must not leak into the response that goes back to the caller. */
    @Test
    public void testTheTraceContextSegmentIsPartOfTheRequestOnly() {
        sendQuery();
        assertFalse(Iti21TelemetryRouteBuilder.receivedMessage.split("[\\r\\n]+").length > 5,
                "unexpectedly many segments, the trace context may have been added more than once:\n"
                        + Iti21TelemetryRouteBuilder.receivedMessage);
    }
}
