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
package org.openehealth.ipf.platform.camel.ihe.core;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.telemetry.Op;
import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.decorators.AbstractSpanDecorator;
import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;

/**
 * Base class for the span decorators of IPF's eHealth transaction components.
 * <p>
 * Adds the metadata of the eHealth transaction the endpoint stands for to the span created by
 * {@code camel-telemetry}, on top of the endpoint tags that {@link AbstractSpanDecorator} already
 * contributes. The metadata is obtained generically through {@link InteractionAwareComponent}, so
 * this class needs no knowledge of the individual transaction families.
 * <p>
 * Trace context propagation is deliberately <em>not</em> implemented here. For SOAP and HTTP based
 * transactions it is performed by the underlying libraries (CXF and the HTTP clients respectively);
 * their instrumentation establishes the context before the exchange reaches the Camel route, and the
 * span created for the route joins it. Overriding {@link #getExtractor} or {@link #getInjector}
 * would compete with that. HL7v2 over MLLP, which has no such mechanism, is the exception and is
 * handled by its own decorator.
 * <p>
 * A concrete subclass exists per endpoint scheme and does nothing but name it, because
 * {@code camel-telemetry} registers decorators in a map keyed on {@link #getComponent()}:
 * <pre>
 * public class XdsIti18SpanDecorator extends WsSpanDecorator {
 *     &#64;Override
 *     public String getComponent() {
 *         return "xds-iti18";
 *     }
 * }
 * </pre>
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class IpfSpanDecorator extends AbstractSpanDecorator {

    /** Code of the eHealth transaction, e.g. {@code xds-iti18}. */
    public static final String TAG_TRANSACTION = "ihe.transaction";

    /** Human readable name of the eHealth transaction, e.g. {@code Registry Stored Query}. */
    public static final String TAG_TRANSACTION_NAME = "ihe.transaction.name";

    /** Whether the transaction is a query, as opposed to a feed. */
    public static final String TAG_QUERY = "ihe.transaction.query";

    /**
     * IPF component classes are one per transaction, so matching by class name would be as
     * fine-grained as matching by scheme and is therefore not supported.
     *
     * @return {@code null}, always.
     */
    @Override
    public String getComponentClassName() {
        return null;
    }

    /**
     * Names the span after the eHealth transaction rather than after the endpoint, so that spans of
     * the same transaction are aggregated regardless of the address they were sent to or received on.
     */
    @Override
    public String getOperationName(Exchange exchange, Endpoint endpoint) {
        var configuration = transactionConfiguration(endpoint);
        return configuration != null ? configuration.getName() : super.getOperationName(exchange, endpoint);
    }

    @Override
    public void beforeTracingEvent(Span span, Exchange exchange, Endpoint endpoint) {
        super.beforeTracingEvent(span, exchange, endpoint);
        var configuration = transactionConfiguration(endpoint);
        if (configuration != null) {
            span.setTag(TAG_TRANSACTION, configuration.getName());
            span.setTag(TAG_TRANSACTION_NAME, configuration.getDescription());
            span.setTag(TAG_QUERY, Boolean.toString(configuration.isQuery()));
        }
    }

    /**
     * A transaction is either served by this endpoint or called by it, never merely passed through.
     */
    @Override
    public String getSpanKind(String op) {
        if (Op.EVENT_RECEIVED.name().equals(op)) {
            return "SERVER";
        }
        if (Op.EVENT_SENT.name().equals(op)) {
            return "CLIENT";
        }
        return super.getSpanKind(op);
    }

    /**
     * @param endpoint an endpoint, of any kind.
     * @return the configuration of the eHealth transaction the endpoint stands for, or {@code null}
     *      if the endpoint does not stand for one.
     */
    protected static TransactionConfiguration transactionConfiguration(Endpoint endpoint) {
        if (endpoint instanceof DefaultEndpoint defaultEndpoint
                && defaultEndpoint.getComponent() instanceof InteractionAwareComponent component) {
            var interactionId = component.getInteractionId();
            return interactionId != null ? interactionId.getTransactionConfiguration() : null;
        }
        return null;
    }
}
