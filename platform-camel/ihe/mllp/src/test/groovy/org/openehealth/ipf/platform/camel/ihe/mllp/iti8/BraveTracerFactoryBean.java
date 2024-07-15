package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.beans.factory.FactoryBean;

public class BraveTracerFactoryBean implements FactoryBean<BraveTracer> {

    private final Tracing tracing;

    public BraveTracerFactoryBean(Tracing tracing) {
        this.tracing = tracing;
    }

    @Override
    public BraveTracer getObject() throws Exception {
        var braveCurrentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
            .addScopeDecorator(MDCScopeDecorator.get()) // Example of Brave's automatic MDC setup
            .build();
        var bridgeContext = new BraveCurrentTraceContext(braveCurrentTraceContext);
        return new BraveTracer(tracing.tracer(), bridgeContext);
    }

    @Override
    public Class<?> getObjectType() {
        return BraveTracer.class;
    }
}
