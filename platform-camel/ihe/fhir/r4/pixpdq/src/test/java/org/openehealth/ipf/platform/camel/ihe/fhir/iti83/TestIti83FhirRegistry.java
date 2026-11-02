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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.DefaultFhirRegistry;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.iti83.Iti83ResourceProvider;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests how FHIR consumers are connected to FHIR servlets via a {@link org.openehealth.ipf.commons.ihe.fhir.FhirRegistry}.
 */
public class TestIti83FhirRegistry {

    private static final String SERVLET_A = "Iti83ServletA";
    private static final String SERVLET_B = "Iti83ServletB";

    @AfterEach
    public void cleanup() {
        DefaultFhirRegistry.removeFhirRegistry(SERVLET_A);
        DefaultFhirRegistry.removeFhirRegistry(SERVLET_B);
    }

    /**
     * Consumers of the same transaction can be served by different FHIR servlets, each of them
     * using its own instance of the transaction's default resource provider.
     */
    @Test
    public void testConsumersOfSameTransactionOnDifferentServlets() throws Exception {
        try (var camelContext = new DefaultCamelContext()) {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("pixm-iti83:a?audit=false&servletName=" + SERVLET_A).routeId("a").log("a");
                    from("pixm-iti83:b?audit=false&servletName=" + SERVLET_B).routeId("b").log("b");
                }
            });
            camelContext.start();
            assertTrue(camelContext.getRouteController().getRouteStatus("a").isStarted());
            assertTrue(camelContext.getRouteController().getRouteStatus("b").isStarted());

            var registryA = DefaultFhirRegistry.getFhirRegistry(SERVLET_A);
            var registryB = DefaultFhirRegistry.getFhirRegistry(SERVLET_B);
            var providerA = registryA.resolveProvider(new Iti83ResourceProvider());
            var providerB = registryB.resolveProvider(new Iti83ResourceProvider());
            assertInstanceOf(Iti83ResourceProvider.class, providerA);
            assertNotSame(providerA, providerB);
            // resolving again yields the instance the consumer is connected to
            assertSame(providerA, registryA.resolveProvider(new Iti83ResourceProvider()));
        }
    }

    @Test
    public void testExplicitlyConfiguredRegistry() throws Exception {
        var registry = new RecordingFhirRegistry();
        try (var camelContext = new DefaultCamelContext()) {
            camelContext.getRegistry().bind("myRegistry", registry);
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("pixm-iti83:a?audit=false&fhirRegistry=#myRegistry").log("a");
                }
            });
            camelContext.start();
            assertRegisteredProvider(registry);
            assertSame(registry, camelContext.getEndpoint("pixm-iti83:a?audit=false&fhirRegistry=#myRegistry", FhirEndpoint.class).getFhirRegistry());
        }
        assertTrue(registry.registered.isEmpty());
    }

    @Test
    public void testRegistryOfServletBean() throws Exception {
        var registry = new RecordingFhirRegistry();
        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        servlet.setServletName(SERVLET_A);
        servlet.setFhirRegistry(registry);
        var otherServlet = new IpfFhirServlet(FhirVersionEnum.R4);
        otherServlet.setServletName(SERVLET_B);
        otherServlet.setFhirRegistry(new RecordingFhirRegistry());

        try (var camelContext = new DefaultCamelContext()) {
            camelContext.getRegistry().bind("servletA", servlet);
            camelContext.getRegistry().bind("servletB", otherServlet);
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("pixm-iti83:a?audit=false&servletName=" + SERVLET_A).log("a");
                }
            });
            camelContext.start();
            assertRegisteredProvider(registry);
            assertTrue(((RecordingFhirRegistry) otherServlet.getFhirRegistry()).registered.isEmpty());
        }
        assertTrue(registry.registered.isEmpty());
    }

    private static void assertRegisteredProvider(RecordingFhirRegistry registry) {
        assertEquals(1, registry.registered.size());
        assertInstanceOf(Iti83ResourceProvider.class, registry.registered.get(0));
        assertSame(registry.registered.get(0), registry.resolveProvider(new Iti83ResourceProvider()));
    }

    static class RecordingFhirRegistry extends DefaultFhirRegistry {
        final List<Object> registered = new CopyOnWriteArrayList<>();

        @Override
        public synchronized void register(Object resourceProvider) {
            super.register(resourceProvider);
            registered.add(resourceProvider);
        }

        @Override
        public synchronized void unregister(Object resourceProvider) {
            super.unregister(resourceProvider);
            registered.remove(resourceProvider);
        }
    }
}
