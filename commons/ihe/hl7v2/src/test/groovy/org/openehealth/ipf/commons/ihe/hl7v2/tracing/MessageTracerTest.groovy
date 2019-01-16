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

import brave.SpanCustomizer
import brave.Tracing
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import zipkin2.Span
import zipkin2.reporter.Reporter

import static org.junit.Assert.*

/**
 * @author Christian Ohr
 */
class MessageTracerTest {

    private static final HapiContext CONTEXT = HapiContextFactory.createHapiContext()

    @Test
    void traceMessage() {
        MockReporter reporter = new MockReporter();
        Tracing tracing = Tracing.newBuilder()
                .localServiceName('MessageTracerTest')
                .spanReporter(reporter)
                .build()
        MessageTracer messageTracer = new MessageTracer(tracing)
        Message sending = MessageUtils.makeMessage(CONTEXT, 'ORU', 'R01', '2.5')

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
        List<zipkin2.Span> spans = reporter.getSpans()
        assertEquals(2, spans.size())

        Span clientSpan = reporter.spans.find { span -> span.kind() == Span.Kind.CLIENT}
        Span serverSpan = reporter.spans.find { span -> span.kind() == Span.Kind.SERVER}
        assertFalse(clientSpan.tags().isEmpty())
        assertEquals(clientSpan.tags(), serverSpan.tags())
        assertNotEquals(clientSpan.id(), serverSpan.id())
        assertEquals(clientSpan.id(), serverSpan.parentId())
        assertTrue(clientSpan.durationAsLong() > serverSpan.durationAsLong())
    }

    private static final class MockReporter implements Reporter<zipkin2.Span> {

        private List<zipkin2.Span> spans = new ArrayList<>();

        @Override
        void report(zipkin2.Span span) {
            spans.add(span);
        }

        List<zipkin2.Span> getSpans() {
            return spans
        }
    }
}
