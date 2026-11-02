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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFhirRegistryTest {

    @Test
    public void testResolveProviderPerClassAndRegistry() {
        var registryA = new DefaultFhirRegistry();
        var registryB = new DefaultFhirRegistry();

        var plain = registryA.resolveProvider(new TestPlainProvider());
        assertSame(plain, registryA.resolveProvider(new TestPlainProvider()));
        assertNotSame(plain, registryB.resolveProvider(new TestPlainProvider()));

        var shared = registryA.resolveProvider(new TestSharedProvider());
        assertInstanceOf(TestSharedProvider.class, shared);
        assertSame(shared, registryA.resolveProvider(new TestSharedProvider()));
    }

    @Test
    public void testServletAndProvidersInAnyOrder() {
        var registry = new DefaultFhirRegistry();
        var provider1 = connectedPlainProvider();
        var provider2 = connectedPlainProvider();

        registry.register(provider1);
        var servlet = new RecordingServlet("servlet");
        registry.register(servlet);
        assertEquals(Set.of(provider1), servlet.providers);
        assertTrue(registry.hasServlet("servlet"));

        registry.register(provider2);
        assertEquals(Set.of(provider1, provider2), servlet.providers);

        registry.unregister(provider1);
        assertEquals(Set.of(provider2), servlet.providers);

        registry.unregister(servlet);
        assertTrue(servlet.providers.isEmpty());
        assertFalse(registry.hasServlet("servlet"));
    }

    @Test
    public void testDuplicateServletName() {
        var registry = new DefaultFhirRegistry();
        var servlet = new RecordingServlet("servlet");
        registry.register(servlet);
        assertThrows(IllegalStateException.class, () -> registry.register(new RecordingServlet("servlet")));
        registry.register(new RecordingServlet("other"));
        assertTrue(registry.hasServlet("other"));
    }

    @Test
    public void testConcurrentlyConnectingConsumersToSharedProvider() throws Exception {
        var registry = new DefaultFhirRegistry();
        var servlet = new RecordingServlet("servlet");
        registry.register(servlet);
        var provider = (SharedFhirProvider) registry.resolveProvider(new TestSharedProvider());

        var threads = 16;
        var rounds = 200;
        var executor = Executors.newFixedThreadPool(threads + 1);
        var stop = new AtomicBoolean();
        var start = new CountDownLatch(1);
        try {
            // Simulates request threads dispatching to consumers while these are being connected and disconnected
            var reader = executor.submit(() -> {
                while (!stop.get()) {
                    provider.getRequestConsumer(null);
                }
                return null;
            });
            var workers = new ArrayList<Future<?>>();
            for (var i = 0; i < threads; i++) {
                workers.add(executor.submit(() -> {
                    start.await();
                    for (var j = 0; j < rounds; j++) {
                        RequestConsumer consumer = EasyMock.niceMock(RequestConsumer.class);
                        EasyMock.replay(consumer);
                        provider.connect(consumer, registry);
                        assertTrue(servlet.providers.contains(provider));
                        provider.disconnect(consumer, registry);
                    }
                    return null;
                }));
            }
            start.countDown();
            for (var worker : workers) {
                worker.get(30, TimeUnit.SECONDS);
            }
            stop.set(true);
            reader.get(5, TimeUnit.SECONDS);
        } finally {
            executor.shutdownNow();
        }
        assertTrue(provider.getRequestConsumer(null).isEmpty());
        assertTrue(servlet.providers.isEmpty());
    }

    private static TestPlainProvider connectedPlainProvider() {
        var provider = new TestPlainProvider();
        RequestConsumer consumer = EasyMock.niceMock(RequestConsumer.class);
        EasyMock.replay(consumer);
        provider.setConsumer(consumer);
        return provider;
    }

    static class TestPlainProvider extends AbstractPlainProvider {
    }

    static class TestSharedProvider extends SharedFhirProvider {
    }

    /**
     * Records the registered providers instead of setting up the HAPI server.
     */
    static class RecordingServlet extends RestfulServer {

        private final String name;
        final Set<Object> providers = ConcurrentHashMap.newKeySet();

        RecordingServlet(String name) {
            this.name = name;
        }

        @Override
        public String getServletName() {
            return name;
        }

        @Override
        public void registerProvider(Object provider) {
            providers.add(provider);
        }

        @Override
        public void registerProviders(Collection<?> providers) {
            providers.forEach(this::registerProvider);
        }

        @Override
        public void unregisterProvider(Object provider) {
            providers.remove(provider);
        }

        @Override
        public void unregisterProviders(Collection<?> providers) {
            providers.forEach(this::unregisterProvider);
        }
    }
}
