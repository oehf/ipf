/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2.tracing


import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import io.micrometer.tracing.SpanCustomizer
import io.micrometer.tracing.otel.bridge.ArrayListSpanProcessor
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext
import io.micrometer.tracing.otel.bridge.OtelPropagator
import io.micrometer.tracing.otel.bridge.OtelTracer
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.extension.trace.propagation.B3Propagator
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.samplers.Sampler
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.junit.jupiter.api.Assertions.*

/**
 * @author Christian Ohr
 */
class MessageOtelTracerTest {

    private static final HapiContext CONTEXT = HapiContextFactory.createHapiContext()

    @Test
    void traceMessage() {
        ArrayListSpanProcessor reporter = new ArrayListSpanProcessor()

        // Otel setup
        var sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(reporter)
            .setResource(Resource.getDefault())
            .setSampler(Sampler.alwaysOn())
            .build()
        var otelPropagator = ContextPropagators.create(B3Propagator.injectingMultiHeaders())
        var openTelemetrySdkBuilder = OpenTelemetrySdk.builder()
            .setPropagators(otelPropagator)
            .setTracerProvider(sdkTracerProvider)
        var otelTracer = openTelemetrySdkBuilder.build().getTracer("io.micrometer.micrometer-tracing")

        // Micrometer Otel Bridge
        def propagator = new OtelPropagator(otelPropagator, otelTracer)
        def tracer = new OtelTracer(otelTracer, new OtelCurrentTraceContext(), new OtelTracer.EventPublisher() {
            @Override
            void publishEvent(Object event) {
            }
        })

        def messageTracer = new MessageTracer(tracer, propagator)
        def sending = MessageUtils.makeMessage(CONTEXT, 'ORU', 'R01', '2.5')

        messageTracer.sendMessage(sending, "producer", new Handler() {
            @Override
            void accept(Message receiving, SpanCustomizer sc1) {
                Thread.sleep(100)
                messageTracer.receiveMessage(receiving, "consumer", new Handler() {
                    @Override
                    void accept(Message received, SpanCustomizer sc2) {
                        assertTrue(received.get('ZTR').empty)
                    }
                })
            }
        })

        // Check a few things
        assertEquals(2, reporter.spans().size())

        def clientSpan = reporter.spans().find { span -> span.kind == SpanKind.CLIENT }
        def serverSpan = reporter.spans().find { span -> span.kind == SpanKind.SERVER }

        assertFalse(clientSpan.attributes.isEmpty())
        assertEquals(clientSpan.attributes.asMap(), serverSpan.attributes.asMap())
        assertNotEquals(clientSpan.spanId, serverSpan.spanId)
        assertEquals(clientSpan.traceId, serverSpan.traceId)
        assertEquals(clientSpan.spanId, serverSpan.parentSpanId)
    }

}
