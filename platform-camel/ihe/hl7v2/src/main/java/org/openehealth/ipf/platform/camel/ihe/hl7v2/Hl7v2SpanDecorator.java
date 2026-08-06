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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.openehealth.ipf.commons.ihe.hl7v2.Constants;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TraceContext;
import org.apache.camel.Exchange;
import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.SpanContextPropagationExtractor;
import org.apache.camel.telemetry.SpanContextPropagationInjector;
import org.openehealth.ipf.platform.camel.ihe.core.IpfSpanDecorator;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerTraceContextInterceptor;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Base class for the span decorators of the HL7v2 transactions.
 * <p>
 * Unlike the SOAP and HTTP based transactions, HL7v2 has no transport that could propagate trace
 * context, which is why this is the one family where IPF has to do it: the context travels in a
 * nonstandard segment, see {@link Hl7v2TraceContext}.
 * <ul>
 * <li>Reading happens here, in {@link #getExtractor}: when {@code camel-telemetry} creates the span for
 *     an incoming message the body is still the encoded message, which is exactly what can be read
 *     without parsing.</li>
 * <li>Writing cannot happen here, because at that moment the outgoing message does not exist yet. The
 *     context is therefore handed to {@link ProducerTraceContextInterceptor} through an exchange
 *     property, and that interceptor writes it into the message it has by then.</li>
 * </ul>
 * Details of the message itself are added once it has been parsed, see {@link #afterTracingEvent}.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class Hl7v2SpanDecorator extends IpfSpanDecorator {

    /**
     * Exchange property under which the trace context to be sent is collected. Written here when
     * the span is created, read by {@code ProducerTraceContextInterceptor} once the message exists.
     */
    public static final String TRACE_CONTEXT_PROPERTY = Hl7v2TraceContext.class.getName();

    /** Sending application, i.e. MSH-3. */
    public static final String TAG_SENDING_APPLICATION = "hl7v2.sending.application";
    /** Sending facility, i.e. MSH-4. */
    public static final String TAG_SENDING_FACILITY = "hl7v2.sending.facility";
    /** Message type, i.e. MSH-9-1. */
    public static final String TAG_MESSAGE_TYPE = "hl7v2.message.type";
    /** Trigger event, i.e. MSH-9-2. */
    public static final String TAG_TRIGGER_EVENT = "hl7v2.trigger.event";
    /** Message control ID, i.e. MSH-10. Identifies the message, not the patient. */
    public static final String TAG_MESSAGE_CONTROL_ID = "hl7v2.message.control.id";
    /** Processing ID, i.e. MSH-11, telling production apart from test traffic. */
    public static final String TAG_PROCESSING_ID = "hl7v2.processing.id";

    /**
     * Reads the trace context out of the encoded message, so that the span of an incoming message joins
     * the trace of the sender.
     */
    @Override
    public SpanContextPropagationExtractor getExtractor(Exchange exchange) {
        var traceContext = Hl7v2TraceContext.read(exchange.getIn().getBody(String.class));
        return new SpanContextPropagationExtractor() {
            @Override
            public Iterator<Map.Entry<String, Object>> iterator() {
                return traceContext.entrySet().stream()
                        .map(entry -> Map.<String, Object>entry(entry.getKey(), entry.getValue()))
                        .iterator();
            }

            @Override
            public Object get(String key) {
                return traceContext.get(key);
            }

            @Override
            public Set<String> keys() {
                return traceContext.keySet();
            }
        };
    }

    /**
     * Collects the trace context to be sent in an exchange property, from where
     * {@link ProducerTraceContextInterceptor} takes it once the outgoing message exists.
     */
    @Override
    public SpanContextPropagationInjector getInjector(Exchange exchange) {
        return (key, value) -> {
            @SuppressWarnings("unchecked")
            var traceContext = (Map<String, String>) exchange.getProperty(TRACE_CONTEXT_PROPERTY);
            if (traceContext == null) {
                traceContext = new LinkedHashMap<>();
                exchange.setProperty(TRACE_CONTEXT_PROPERTY, traceContext);
            }
            traceContext.put(key, value);
        };
    }

    /**
     * Adds details of the message itself. This happens at the end of the exchange rather than at the
     * start, because only by then has the consumer interceptor chain parsed the message.
     */
    @Override
    public void afterTracingEvent(Span span, Exchange exchange) {
        super.afterTracingEvent(span, exchange);
        var message = parsedMessage(exchange);
        if (message == null) {
            return;
        }
        try {
            var terser = new Terser(message);
            tag(span, TAG_SENDING_APPLICATION, terser.get("MSH-3"));
            tag(span, TAG_SENDING_FACILITY, terser.get("MSH-4"));
            tag(span, TAG_MESSAGE_TYPE, terser.get("MSH-9-1"));
            tag(span, TAG_TRIGGER_EVENT, terser.get("MSH-9-2"));
            tag(span, TAG_MESSAGE_CONTROL_ID, terser.get("MSH-10"));
            tag(span, TAG_PROCESSING_ID, terser.get("MSH-11"));
        } catch (Exception ignored) {
            // a message that cannot be tersed has already been rejected elsewhere; a span is no place
            // to report that
        }
    }

    private static void tag(Span span, String key, String value) {
        if (value != null && !value.isEmpty()) {
            span.setTag(key, value);
        }
    }

    /**
     * @return the parsed message of this exchange, or {@code null} if it never got parsed. The consumer
     *      interceptor chain keeps it in a header, which outlives the body being replaced by the
     *      response.
     */
    private static Message parsedMessage(Exchange exchange) {
        var fromHeader = exchange.getIn().getHeader(Constants.ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME);
        if (fromHeader instanceof Message message) {
            return message;
        }
        var body = exchange.getIn().getBody();
        return body instanceof Message message ? message : null;
    }
}
