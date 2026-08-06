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
 * {@link Iti18ObservationTest} with OpenTelemetry instead of Brave: the trace context that CXF's
 * observation features establish must reach the Camel route on the consumer side either way, since
 * neither IPF nor CXF depends on which micrometer tracing bridge is in use.
 * <p>
 * Kept as a separate test rather than folded into a shared base class, because the test container
 * keeps its server, Camel context and producer template in static fields, which a shared base would
 * have to hand back and forth between the two variants.
 *
 * @author Christian Ohr
 */
public class Iti18OtelObservationTest extends XdsStandardTestContainer {

    private static String serviceUri;

    @BeforeAll
    public static void setUp() {
        startServer(new CXFServlet(), "iti-18-otel-observation.xml");
        serviceUri = "xds-iti18://localhost:" + getPort() + "/" + Iti18OtelObservationRouteBuilder.SERVICE
                + "?features=#observationClientFeature";
    }

    /**
     * @return the trace ID that was in scope inside the consumer route.
     */
    private String sendAndReportConsumerTraceId() {
        OpenTelemetryObservations.forgetConsumerTraceId();
        var exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(SampleData.createFindDocumentsQuery());
        Exchange result = producerTemplate.send(serviceUri, exchange);
        if (result.getException() != null) {
            throw new AssertionError("the transaction failed", result.getException());
        }
        assertNotNull(result.getMessage().getBody(QueryResponse.class), "no response came back");
        return OpenTelemetryObservations.consumerTraceId();
    }

    @Test
    public void testTraceContextReachesTheConsumerRoute() {
        var root = OpenTelemetryObservations.tracer().nextSpan().name("test-root").start();
        String ambientOnConsumerSide;
        try (var ignored = OpenTelemetryObservations.tracer().withSpan(root)) {
            assertEquals(root.context().traceId(), OpenTelemetryObservations.currentTraceId(),
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
        assertNotNull(sendAndReportConsumerTraceId(), "the consumer route ran outside any trace");
    }
}
