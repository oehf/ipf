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
package org.openehealth.ipf.platform.camel.ihe.mllp.telemetry;

import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.SpanContextPropagationExtractor;
import org.apache.camel.telemetry.SpanContextPropagationInjector;
import org.apache.camel.telemetry.SpanLifecycleManager;
import org.apache.camel.telemetry.Tracer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link Tracer} that injects a known trace context on the way out and records what the extractor
 * offered on the way in. That is all it takes to tell whether the context really travelled: no tracing
 * library is involved, so what the test observes is IPF's own propagation and nothing else.
 *
 * @author Christian Ohr
 */
public class TraceContextRecordingTracer extends Tracer {

    public static final String KEY = "traceparent";
    public static final String VALUE = "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01";

    /** What the extractors saw, keyed by the span name they were used for. */
    private final Map<String, Map<String, String>> extracted = new ConcurrentHashMap<>();

    public Map<String, Map<String, String>> getExtracted() {
        return Map.copyOf(extracted);
    }

    public void clear() {
        extracted.clear();
    }

    @Override
    protected void initTracer() {
        setSpanLifecycleManager(new SpanLifecycleManager() {
            @Override
            public Span create(String name, String kind, Span parent, SpanContextPropagationExtractor extractor) {
                var seen = new LinkedHashMap<String, String>();
                if (extractor != null) {
                    for (var key : extractor.keys()) {
                        var value = extractor.get(key);
                        seen.put(key, value == null ? null : value.toString());
                    }
                }
                extracted.put(name + "/" + kind, seen);
                return new Span() {
                    @Override public void log(Map<String, String> fields) { }
                    @Override public void setTag(String key, String value) { }
                    @Override public void setComponent(String component) { }
                    @Override public void setError(boolean error) { }
                };
            }

            @Override
            public void activate(Span span) { }

            @Override
            public void deactivate(Span span) { }

            @Override
            public void close(Span span) { }

            @Override
            public void inject(Span span, SpanContextPropagationInjector injector, boolean force) {
                if (injector != null) {
                    injector.put(KEY, VALUE);
                }
            }
        });
    }
}
