/*
 * Copyright 2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.beans.factory.FactoryBean;

public class OpenTelemetryTracerFactoryBean implements FactoryBean<Tracer> {

    private final SpanProcessor spanProcessor;

    public OpenTelemetryTracerFactoryBean(SpanProcessor spanProcessor) {
        this.spanProcessor = spanProcessor;
    }

    @Override
    public Tracer getObject() {
        var sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(spanProcessor)
            .setResource(Resource.getDefault())
            .setSampler(Sampler.alwaysOn())
            .build();
        var openTelemetrySdkBuilder = OpenTelemetrySdk.builder()
            .setPropagators(ContextPropagators.create(B3Propagator.injectingMultiHeaders()))
            .setTracerProvider(sdkTracerProvider);
        return openTelemetrySdkBuilder.build().getTracer("io.micrometer.micrometer-tracing");
    }

    @Override
    public Class<?> getObjectType() {
        return Tracer.class;
    }
}
