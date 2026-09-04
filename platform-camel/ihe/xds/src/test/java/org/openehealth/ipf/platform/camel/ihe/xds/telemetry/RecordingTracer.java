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

import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.SpanContextPropagationExtractor;
import org.apache.camel.telemetry.SpanContextPropagationInjector;
import org.apache.camel.telemetry.SpanLifecycleManager;
import org.apache.camel.telemetry.Tracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A {@link Tracer} that records the spans it is asked to create, so that a test can assert which span
 * camel-telemetry produced for an endpoint, which decorator it selected, and what the decorator
 * contributed. Deliberately minimal: no tracing library is involved, and no context is propagated
 * beyond what the extractor offers.
 *
 * @author Christian Ohr
 */
public class RecordingTracer extends Tracer {

    private final List<RecordedSpan> spans = new CopyOnWriteArrayList<>();

    /** A span as camel-telemetry created it, plus everything the decorator put on it. */
    public static class RecordedSpan implements Span {
        private final String name;
        private final String kind;
        private final RecordedSpan parent;
        private final Map<String, String> tags = Collections.synchronizedMap(new HashMap<>());
        private volatile String component;
        private volatile boolean error;

        RecordedSpan(String name, String kind, RecordedSpan parent) {
            this.name = name;
            this.kind = kind;
            this.parent = parent;
        }

        @Override public void log(Map<String, String> fields) { }
        @Override public void setTag(String key, String value) { tags.put(key, value); }
        @Override public void setComponent(String component) { this.component = component; }
        @Override public void setError(boolean error) { this.error = error; }

        public String getName() { return name; }
        public String getKind() { return kind; }
        public RecordedSpan getParent() { return parent; }
        public String getComponent() { return component; }
        public boolean isError() { return error; }
        public String getTag(String key) { return tags.get(key); }
        public Map<String, String> getTags() { return Map.copyOf(tags); }

        @Override
        public String toString() {
            return kind + " " + name + " " + new java.util.TreeMap<>(tags);
        }
    }

    public List<RecordedSpan> getSpans() {
        return new ArrayList<>(spans);
    }

    public void clear() {
        spans.clear();
    }

    @Override
    protected void initTracer() {
        setSpanLifecycleManager(new SpanLifecycleManager() {
            @Override
            public Span create(String name, String kind, Span parent, SpanContextPropagationExtractor extractor) {
                var span = new RecordedSpan(name, kind, (RecordedSpan) parent);
                spans.add(span);
                return span;
            }

            @Override
            public void activate(Span span) { }

            @Override
            public void deactivate(Span span) { }

            @Override
            public void close(Span span) { }

            @Override
            public void inject(Span span, SpanContextPropagationInjector injector, boolean force) { }
        });
    }
}
