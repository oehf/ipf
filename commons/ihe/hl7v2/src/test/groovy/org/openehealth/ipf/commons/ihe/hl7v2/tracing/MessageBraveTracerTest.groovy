/*
 * Copyright 2019 the original author or authors.
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

import brave.Span
import brave.Tracing
import brave.context.slf4j.MDCScopeDecorator
import brave.handler.MutableSpan
import brave.handler.SpanHandler
import brave.propagation.ThreadLocalCurrentTraceContext
import brave.propagation.TraceContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import io.micrometer.tracing.SpanCustomizer
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext
import io.micrometer.tracing.brave.bridge.BravePropagator
import io.micrometer.tracing.brave.bridge.BraveTracer
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.junit.jupiter.api.Assertions.*

/**
 * @author Christian Ohr
 */
class MessageBraveTracerTest {

    private static final HapiContext CONTEXT = HapiContextFactory.createHapiContext()

    @Test
    void traceMessage() {
        MockReporter reporter = new MockReporter()

        // Brave setup
        def braveCurrentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
            .addScopeDecorator(MDCScopeDecorator.get()) // Example of Brave's automatic MDC setup
            .build();
        def tracing = Tracing.newBuilder()
                .localServiceName('MessageTracerTest')
                .addSpanHandler(reporter)
                .build()
        def braveTracer = tracing.tracer();

        // Micrometer Brave Bridge
        def propagator = new BravePropagator(tracing)
        def bridgeContext = new BraveCurrentTraceContext(braveCurrentTraceContext);
        def tracer = new BraveTracer(braveTracer, bridgeContext);

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
        def spans = reporter.getSpans()
        assertEquals(2, spans.size())

        def clientSpan = reporter.spans.find { span -> span.kind() == Span.Kind.CLIENT}
        def serverSpan = reporter.spans.find { span -> span.kind() == Span.Kind.SERVER}
        assertFalse(clientSpan.tags().isEmpty())
        assertEquals(new HashMap<>(clientSpan.tags()), new HashMap<>(serverSpan.tags()))
        assertNotEquals(clientSpan.id(), serverSpan.id())
        assertEquals(clientSpan.id(), serverSpan.parentId())
        assertEquals(clientSpan.traceId(), serverSpan.traceId())
    }

    class MockReporter extends SpanHandler {

        private List<MutableSpan> spans = new ArrayList<>();

        @Override
        boolean end(TraceContext context, MutableSpan span, Cause cause) {
            spans.add(span);
            return super.end(context, span, cause)
        }

        List<MutableSpan> getSpans() {
            return spans
        }
    }

}
