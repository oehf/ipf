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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer;

import ca.uhn.hl7v2.model.Message;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TraceContext;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2SpanDecorator;

/**
 * Removes the trace context segment ({@link Hl7v2TraceContext}) from incoming messages once they are parsed;
 * the nonstandard segment would otherwise fail validation or confuse the application, which is why IPF 5
 * removed it as well. The consumer span is only created after the interceptors have run, so the trace context
 * is kept in the exchange property {@link Hl7v2SpanDecorator#RECEIVED_TRACE_CONTEXT_PROPERTY}, from where
 * {@link Hl7v2SpanDecorator} takes it.
 * <p>
 * Belongs in the consumer chain directly inside the marshalling interceptor, i.e. before request validation.
 * Part of the chain unless the endpoint opts out with {@code removeTraceContext=false}.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class ConsumerTraceContextRemovalInterceptor extends InterceptorSupport {

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn().getBody() instanceof Message message) {
            var traceContext = Hl7v2TraceContext.read(message.encode());
            if (!traceContext.isEmpty()) {
                exchange.setProperty(Hl7v2SpanDecorator.RECEIVED_TRACE_CONTEXT_PROPERTY, traceContext);
                Hl7v2TraceContext.remove(message);
            }
        }
        getWrappedProcessor().process(exchange);
    }

    public static class Factory implements InterceptorFactory {
        @Override
        public ConsumerTraceContextRemovalInterceptor getNewInstance() {
            return new ConsumerTraceContextRemovalInterceptor();
        }
    }
}
