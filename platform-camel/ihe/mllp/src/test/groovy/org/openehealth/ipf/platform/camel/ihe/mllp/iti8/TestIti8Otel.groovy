/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8


import io.micrometer.tracing.otel.bridge.ArrayListSpanProcessor
import io.opentelemetry.api.trace.SpanKind
import org.junit.jupiter.api.Test
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AbstractMllpTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

import static org.junit.jupiter.api.Assertions.*

/**
 * Unit tests for the PIX Feed transaction a.k.a. ITI-8 with OpenTelemtry trace propagation
 */
@ContextConfiguration('/iti8/iti-8-otel.xml')
class TestIti8Otel extends AbstractMllpTest {

    @Autowired
    private ArrayListSpanProcessor reporter

    @Test
    void testHappyCaseAndTrace() {
        doTestHappyCaseAndAudit("pix-iti8://localhost:18083?interceptorFactories=#producerTracingInterceptor,#clientInLogger,#clientOutLogger&timeout=${TIMEOUT}", 2)
        assertEquals(2, reporter.spans().size())

        def clientSpan = reporter.spans().find { span -> span.kind == SpanKind.CLIENT }
        def serverSpan = reporter.spans().find { span -> span.kind == SpanKind.SERVER }

        assertFalse(clientSpan.attributes.isEmpty())
        assertEquals(clientSpan.attributes.asMap(), serverSpan.attributes.asMap())
        assertNotEquals(clientSpan.spanId, serverSpan.spanId)
        assertEquals(clientSpan.traceId, serverSpan.traceId)
        assertEquals(clientSpan.spanId, serverSpan.parentSpanId)
    }

    def doTestHappyCaseAndAudit(String endpointUri, int expectedAuditItemsCount) {
        final String body = getMessageString('ADT^A01', '2.3.1')
        def msg = send(endpointUri, body)
        assertACK(msg)
        assertAuditEvents { it.messages.size() == expectedAuditItemsCount }
    }

}
