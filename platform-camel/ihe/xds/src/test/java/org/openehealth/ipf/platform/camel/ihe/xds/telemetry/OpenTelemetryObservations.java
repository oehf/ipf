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

import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;
import io.micrometer.tracing.otel.bridge.ArrayListSpanProcessor;
import io.micrometer.tracing.otel.bridge.OtelBaggageManager;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelPropagator;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.trace.SdkTracerProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An OpenTelemetry backed {@link ObservationRegistry} for the tests, the counterpart of
 * {@link BraveObservations}. Both are set up the same way and differ only in the micrometer tracing
 * bridge, which is the point: neither IPF nor CXF cares which one is used.
 *
 * @author Christian Ohr
 */
public final class OpenTelemetryObservations {

    private static Tracer tracer;
    private static ObservationRegistry observationRegistry;

    /**
     * The trace ID observed inside the consumer route. The consumer runs on its own exchange, so the
     * observation cannot be handed back with the response; both sides share the JVM, so a field can.
     */
    private static final AtomicReference<String> CONSUMER_TRACE_ID = new AtomicReference<>();

    private OpenTelemetryObservations() {
    }

    private static synchronized void initialize() {
        if (observationRegistry != null) {
            return;
        }
        var tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(new ArrayListSpanProcessor())
            .build();
        var otelTracer = tracerProvider.get("ipf-xds-test");
        var currentTraceContext = new OtelCurrentTraceContext();
        tracer = new OtelTracer(otelTracer, currentTraceContext,
            event -> {},
            new OtelBaggageManager(currentTraceContext, List.of(), List.of()));
        var propagator = new OtelPropagator(
            ContextPropagators.create(W3CTraceContextPropagator.getInstance()),
            otelTracer);

        observationRegistry = ObservationRegistry.create();
        observationRegistry.observationConfig().observationHandler(
            new ObservationHandler.FirstMatchingCompositeObservationHandler(
                new PropagatingSenderTracingObservationHandler<>(tracer, propagator),
                new PropagatingReceiverTracingObservationHandler<>(tracer, propagator),
                new DefaultTracingObservationHandler(tracer)));
    }

    /**
     * Referenced from the Spring test context.
     */
    public static synchronized ObservationRegistry observationRegistry() {
        initialize();
        return observationRegistry;
    }

    public static synchronized Tracer tracer() {
        initialize();
        return tracer;
    }

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
     */
    public static String currentTraceId() {
        var current = tracer().currentSpan();
        return current != null ? current.context().traceId() : null;
    }
}
