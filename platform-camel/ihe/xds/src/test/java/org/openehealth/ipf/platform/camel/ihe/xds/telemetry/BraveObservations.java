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
package org.openehealth.ipf.platform.camel.ihe.xds.telemetry;

import brave.Tracing;
import brave.propagation.ThreadLocalCurrentTraceContext;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BravePropagator;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A Brave backed {@link ObservationRegistry} for the tests, so that CXF's observation features really
 * create and propagate trace context. Referenced from the Spring test context by factory method, and
 * from the test itself to open a span of known trace ID and to read the ambient context.
 *
 * @author Christian Ohr
 */
public final class BraveObservations {

    private static Tracer tracer;
    private static ObservationRegistry observationRegistry;

    private BraveObservations() {
    }

    private static synchronized void initialize() {
        if (observationRegistry != null) {
            return;
        }
        var currentTraceContext = ThreadLocalCurrentTraceContext.newBuilder().build();
        var tracing = Tracing.newBuilder().currentTraceContext(currentTraceContext).build();
        tracer = new BraveTracer(tracing.tracer(),
                new BraveCurrentTraceContext(currentTraceContext),
                new BraveBaggageManager());
        var propagator = new BravePropagator(tracing);

        observationRegistry = ObservationRegistry.create();
        observationRegistry.observationConfig().observationHandler(
                new ObservationHandler.FirstMatchingCompositeObservationHandler(
                        new PropagatingSenderTracingObservationHandler<>(tracer, propagator),
                        new PropagatingReceiverTracingObservationHandler<>(tracer, propagator),
                        new DefaultTracingObservationHandler(tracer)));
    }

    /** Referenced from the Spring test context. */
    @SuppressWarnings("unused")
    public static synchronized ObservationRegistry observationRegistry() {
        initialize();
        return observationRegistry;
    }

    public static synchronized Tracer tracer() {
        initialize();
        return tracer;
    }

    /**
     * The trace ID observed inside the consumer route. The consumer runs on its own exchange, so the
     * observation cannot be handed back with the response; both sides share the JVM, so a field can.
     */
    private static final AtomicReference<String> CONSUMER_TRACE_ID =
            new AtomicReference<>();

    public static void recordConsumerTraceId(String traceId) {
        CONSUMER_TRACE_ID.set(traceId);
    }

    public static String consumerTraceId() {
        return CONSUMER_TRACE_ID.get();
    }

    public static void forgetConsumerTraceId() {
        CONSUMER_TRACE_ID.set(null);
    }

    /**
     * @return the trace ID of the span that is currently in scope, or {@code null} if there is none.
     *      This is what tells whether CXF's instrumentation made its context ambient on the thread
     *      that Camel routes on.
     */
    public static String currentTraceId() {
        var current = tracer().currentSpan();
        return current != null ? current.context().traceId() : null;
    }
}
