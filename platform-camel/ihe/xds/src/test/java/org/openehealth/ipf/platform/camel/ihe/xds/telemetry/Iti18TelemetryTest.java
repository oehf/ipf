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

import org.apache.camel.builder.RouteBuilder;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.core.IpfSpanDecorator;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsSpanDecorator;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.iti18.XdsIti18SpanDecorator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Verifies the telemetry of a real ITI-18 transaction carried over CXF: that camel-telemetry selects
 * the {@link XdsIti18SpanDecorator} by the endpoint scheme at runtime, on both the producer and the
 * consumer side, and that the decorator's metadata ends up on the resulting spans.
 * <p>
 * The tracer is installed after the server has started and the traced route is added afterwards,
 * because the route policy that produces the consumer span is attached when a route is created.
 *
 * @author Christian Ohr
 */
public class Iti18TelemetryTest extends XdsStandardTestContainer {

    private static final String CONTEXT_DESCRIPTOR = "iti-18.xml";
    private static final String SERVICE_PATH = "telemetry-iti18-service";

    private static RecordingTracer tracer;
    private static String serviceUri;

    @BeforeAll
    public static void setUpTelemetry() throws Exception {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);

        tracer = new RecordingTracer();
        tracer.init(camelContext);

        serviceUri = "xds-iti18://localhost:" + getPort() + "/" + SERVICE_PATH;
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("xds-iti18:" + SERVICE_PATH)
                        .process(exchange -> exchange.getMessage()
                                .setBody(SampleData.createQueryResponseWithLeafClass()));
            }
        });
    }

    @BeforeEach
    public void clearSpans() {
        tracer.clear();
    }

    private List<RecordingTracer.RecordedSpan> spansOfKind(String kind) {
        return tracer.getSpans().stream()
            .filter(span -> kind.equals(span.getKind()))
            .toList();
    }

    @Test
    public void testTransactionProducesAClientAndAServerSpan() {
        var response = send(serviceUri, SampleData.createFindDocumentsQuery(), QueryResponse.class);
        // the sample response carries PARTIAL_SUCCESS; all that matters here is that the
        // transaction really went over the wire rather than failing
        assertNotNull(response, "no response came back");
        assertNotEquals(Status.FAILURE, response.getStatus(), "the transaction itself failed");

        var client = spansOfKind("CLIENT");
        var server = spansOfKind("SERVER");
        assertEquals(1, client.size(), "expected one producer span, got " + tracer.getSpans());
        assertEquals(1, server.size(), "expected one consumer span, got " + tracer.getSpans());
    }

    /**
     * The decorator is selected by the endpoint scheme, so both sides of the transaction must carry
     * the metadata of ITI-18 -- this is the mechanism of {@code SpanDecoratorManagerImpl.get(Endpoint)}
     * at work in a running transaction.
     */
    @Test
    public void testBothSpansCarryTheTransactionMetadata() {
        send(serviceUri, SampleData.createFindDocumentsQuery(), QueryResponse.class);

        for (var span : tracer.getSpans()) {
            assertEquals("xds-iti18", span.getName(), "span not named after the transaction: " + span);
            assertEquals("xds-iti18", span.getTag(IpfSpanDecorator.TAG_TRANSACTION), "on " + span);
            assertEquals("Registry Stored Query", span.getTag(IpfSpanDecorator.TAG_TRANSACTION_NAME), "on " + span);
            assertEquals("true", span.getTag(IpfSpanDecorator.TAG_QUERY), "on " + span);
            assertEquals("XDS", span.getTag(XdsSpanDecorator.TAG_PROFILE), "on " + span);
            assertEquals("xds-iti18", span.getTag("url.scheme"), "on " + span);
            assertFalse(span.isError(), "unexpected error flag on " + span);
        }
    }

    /**
     * The very decorator instance registered for the scheme is the one Camel uses -- not a copy and
     * not the default decorator.
     */
    @Test
    public void testTheRegisteredDecoratorIsTheOneUsed() {
        var endpoint = camelContext.getEndpoint(serviceUri);
        var resolved = new org.apache.camel.telemetry.SpanDecoratorManagerImpl().get(endpoint);
        assertSame(XdsIti18SpanDecorator.class, resolved.getClass());
        assertEquals("xds-iti18", resolved.getComponent());
    }

    /**
     * Nothing in IPF injects trace context across the SOAP call: that is the job of the CXF
     * instrumentation, which this test deliberately does not install. Without it the consumer span is
     * a root span rather than a child of the producer span.
     * <p>
     * {@link Iti18BraveObservationTest} covers the other half, namely that CXF's observation features do
     * carry the context across and keep it in scope while the consumer route runs, which is what lets
     * the spans of camel-telemetry join the caller's trace.
     */
    @Test
    public void testTraceContextIsNotPropagatedByIpfItself() {
        send(serviceUri, SampleData.createFindDocumentsQuery(), QueryResponse.class);

        var server = spansOfKind("SERVER").get(0);
        assertNull(server.getParent(),
                "the consumer span has a parent, so something did propagate the context: " + server);
    }

    @Test
    public void testAnUnrelatedEndpointIsNotDecoratedAsAnIheTransaction() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:telemetry-plain").to("log:telemetry-plain");
            }
        });
        tracer.clear();

        producerTemplate.sendBody("direct:telemetry-plain", "irrelevant");

        var spans = tracer.getSpans();
        assertFalse(spans.isEmpty(), "expected a span for the plain endpoint");
        for (var span : spans) {
            assertNull(span.getTag(IpfSpanDecorator.TAG_TRANSACTION), "unexpected IHE metadata on " + span);
            assertNotNull(span.getName());
        }
    }
}
