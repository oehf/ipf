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
package org.openehealth.ipf.platform.camel.ihe.xds.telemetry;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Answers the question the rest of the telemetry work left open: does the trace context that CXF's
 * observation features establish survive as far as the Camel route on the consumer side?
 * <p>
 * The test opens a span of known trace ID, sends an ITI-18 request through a producer endpoint with
 * CXF's client observation feature attached, and lets the consumer route record the trace ID that is
 * ambient while it runs. If the two match, the context crossed the SOAP call and was in scope on the
 * thread that Camel routes on -- which is what allows the spans of {@code camel-telemetry} to join the
 * trace without IPF propagating anything itself.
 *
 * @author Christian Ohr
 */
public class Iti18BraveObservationTest extends XdsStandardTestContainer {

    private static String serviceUri;

    @BeforeAll
    public static void setUp() {
        startServer(new CXFServlet(), "iti-18-observation.xml");
        serviceUri = "xds-iti18://localhost:" + getPort() + "/" + Iti18BraveObservationRouteBuilder.SERVICE
                + "?features=#observationClientFeature";
    }

    /**
     * @return the trace ID that was in scope inside the consumer route.
     */
    private String sendAndReportConsumerTraceId() {
        BraveObservations.forgetConsumerTraceId();
        var exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(SampleData.createFindDocumentsQuery());
        Exchange result = producerTemplate.send(serviceUri, exchange);
        if (result.getException() != null) {
            throw new AssertionError("the transaction failed", result.getException());
        }
        assertNotNull(result.getMessage().getBody(QueryResponse.class), "no response came back");
        return BraveObservations.consumerTraceId();
    }

    @Test
    public void testTraceContextReachesTheConsumerRoute() {
        var root = BraveObservations.tracer()
            .nextSpan()
            .name("test-root")
            .start();
        String ambientOnConsumerSide;
        try (var ignored = BraveObservations.tracer().withSpan(root)) {
            assertEquals(root.context().traceId(), BraveObservations.currentTraceId(),
                    "the test's own span is not in scope");
            ambientOnConsumerSide = sendAndReportConsumerTraceId();
        } finally {
            root.end();
        }

        assertNotNull(ambientOnConsumerSide,
                "no trace context was in scope in the consumer route -- CXF's context did not survive "
                        + "the handoff to Camel, so IPF would have to propagate it after all");
        assertEquals(root.context().traceId(), ambientOnConsumerSide,
                "the consumer route ran in a different trace than the caller");
    }

    /**
     * Without a caller side trace there is nothing to join, so the consumer must still be observed --
     * in a trace of its own rather than in none at all.
     */
    @Test
    public void testConsumerIsObservedWithoutACallerTrace() {
        var ambientOnConsumerSide = sendAndReportConsumerTraceId();
        assertNotNull(ambientOnConsumerSide, "the consumer route ran outside any trace");
    }
}
