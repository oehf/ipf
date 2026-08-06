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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer;

import ca.uhn.hl7v2.model.Message;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TraceContext;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2SpanDecorator;

import java.util.Map;

/**
 * Adds the trace context that the span decorator collected to the outgoing message, so that the
 * receiver can join the trace. HL7v2 has no transport level mechanism for this, hence the nonstandard
 * segment described in {@link Hl7v2TraceContext}.
 * <p>
 * Belongs in the producer chain directly outside the marshalling interceptor, so that it still has the
 * parsed message: leaving the encoding to HAPI is what escapes values containing delimiters, and
 * marshalling afterwards puts the segment on the wire as part of the message.
 * <p>
 * Does nothing at all unless a trace context was collected, which only happens when
 * {@code camel-telemetry} is active. The interceptor itself does not depend on it: the context reaches
 * it as a plain map in an exchange property, so it can be part of the default producer chain without
 * making telemetry a hard requirement.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class ProducerTraceContextInterceptor extends InterceptorSupport {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerTraceContextInterceptor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        var traceContext = (Map<String, String>) exchange.getProperty(Hl7v2SpanDecorator.TRACE_CONTEXT_PROPERTY);
        if (traceContext != null && !traceContext.isEmpty()) {
            // deliberately not converting: a converted copy would be thrown away together with the
            // trace context written into it, so rather say that the chain order is wrong
            if (exchange.getIn().getBody() instanceof Message message) {
                Hl7v2TraceContext.write(message, traceContext);
            } else {
                LOG.warn("Cannot propagate trace context: this interceptor must run before marshalling");
            }
        }
        getWrappedProcessor().process(exchange);
    }

    public static class Factory implements InterceptorFactory {
        @Override
        public ProducerTraceContextInterceptor getNewInstance() {
            return new ProducerTraceContextInterceptor();
        }
    }
}
