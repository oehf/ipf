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

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultComponent;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.telemetry.Op;
import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.SpanDecorator;
import org.apache.camel.telemetry.SpanDecoratorManagerImpl;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.TransactionConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Christian Ohr
 */
public class IpfSpanDecoratorTest {

    private static final String SCHEME = "test-iti99";

    // ------------------------------------------------------------------
    // Test doubles standing in for a transaction component
    // ------------------------------------------------------------------

    private static class TestTransactionConfiguration extends TransactionConfiguration {
        TestTransactionConfiguration() {
            super(SCHEME, "Test Stored Query", true, null, null);
        }
    }

    private enum TestInteractions implements InteractionId {
        ITI_99;

        private static final TransactionConfiguration CONFIGURATION = new TestTransactionConfiguration();

        @Override
        public TransactionConfiguration getTransactionConfiguration() {
            return CONFIGURATION;
        }
    }

    private static class TestComponent extends DefaultComponent implements InteractionAwareComponent {
        @Override
        public InteractionId getInteractionId() {
            return TestInteractions.ITI_99;
        }

        @Override
        protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
            return new DefaultEndpoint(uri, this) {
                @Override
                public org.apache.camel.Producer createProducer() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public org.apache.camel.Consumer createConsumer(org.apache.camel.Processor processor) {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /** A per-scheme decorator, exactly as the transaction modules will declare them. */
    public static class TestSpanDecorator extends IpfSpanDecorator {
        @Override
        public String getComponent() {
            return SCHEME;
        }
    }

    /** Collects what the decorator sets, so the tags can be asserted. */
    private static class RecordingSpan implements Span {
        private final Map<String, String> tags = new HashMap<>();
        private String component;

        @Override public void log(Map<String, String> fields) { }
        @Override public void setTag(String key, String value) { tags.put(key, value); }
        @Override public void setComponent(String component) { this.component = component; }
        @Override public void setError(boolean error) { }
    }

    // ------------------------------------------------------------------

    private Endpoint endpoint(CamelContext context) throws Exception {
        var component = new TestComponent();
        component.setCamelContext(context);
        context.addComponent(SCHEME, component);
        return component.createEndpoint(SCHEME + "://localhost:8080/service");
    }

    @Test
    public void testTransactionMetadataIsAddedToTheSpan() throws Exception {
        try (var context = new DefaultCamelContext()) {
            var endpoint = endpoint(context);
            var exchange = new DefaultExchange(context);
            var span = new RecordingSpan();

            new TestSpanDecorator().beforeTracingEvent(span, exchange, endpoint);

            assertEquals(SCHEME, span.tags.get(IpfSpanDecorator.TAG_TRANSACTION));
            assertEquals("Test Stored Query", span.tags.get(IpfSpanDecorator.TAG_TRANSACTION_NAME));
            assertEquals("true", span.tags.get(IpfSpanDecorator.TAG_QUERY));
            // the endpoint tags of AbstractSpanDecorator are still contributed
            assertEquals(SCHEME, span.tags.get("url.scheme"));
        }
    }

    @Test
    public void testSpanIsNamedAfterTheTransaction() throws Exception {
        try (var context = new DefaultCamelContext()) {
            var endpoint = endpoint(context);
            var exchange = new DefaultExchange(context);
            assertEquals(SCHEME, new TestSpanDecorator().getOperationName(exchange, endpoint));
        }
    }

    @Test
    public void testSpanKind() {
        var decorator = new TestSpanDecorator();
        assertEquals("SERVER", decorator.getSpanKind(Op.EVENT_RECEIVED.name()));
        assertEquals("CLIENT", decorator.getSpanKind(Op.EVENT_SENT.name()));
        assertEquals("INTERNAL", decorator.getSpanKind(Op.EVENT_PROCESS.name()));
    }

    /**
     * Endpoints of components that do not stand for an eHealth transaction must not break the
     * decorator -- it falls back to the behaviour of {@code AbstractSpanDecorator}.
     */
    @Test
    public void testForeignEndpointIsTolerated() throws Exception {
        try (var context = new DefaultCamelContext()) {
            var endpoint = context.getEndpoint("direct:foo");
            var exchange = new DefaultExchange(context);
            var span = new RecordingSpan();

            var decorator = new TestSpanDecorator();
            decorator.beforeTracingEvent(span, exchange, endpoint);

            assertNull(span.tags.get(IpfSpanDecorator.TAG_TRANSACTION));
            assertEquals("direct", decorator.getOperationName(exchange, endpoint));
        }
    }

    /**
     * Verifies the registration contract that the whole design rests on: camel-telemetry discovers
     * decorators via {@link java.util.ServiceLoader} and resolves them for an endpoint by the scheme
     * its {@link SpanDecorator#getComponent()} names.
     */
    @Test
    public void testDecoratorIsDiscoveredByItsScheme() throws Exception {
        try (var context = new DefaultCamelContext()) {
            var resolved = new SpanDecoratorManagerImpl().get(endpoint(context));
            assertInstanceOf(TestSpanDecorator.class, resolved, "expected the service-loaded TestSpanDecorator, got " + resolved.getClass());
        }
    }

    /** An endpoint of a component nobody registered a decorator for resolves to the default. */
    @Test
    public void testUnregisteredSchemeFallsBackToTheDefault() throws Exception {
        try (var context = new DefaultCamelContext()) {
            var resolved = new SpanDecoratorManagerImpl().get(context.getEndpoint("direct:foo"));
            assertEquals("direct", resolved.getComponent());
        }
    }

    /** Matching by component class name is not supported, see {@link IpfSpanDecorator}. */
    @Test
    public void testNoComponentClassNameMatching() {
        assertNull(new TestSpanDecorator().getComponentClassName());
    }
}
