package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.apache.camel.Exchange;
import org.apache.camel.processor.DelegateProcessor;

/**
 * An interceptor that delegates to a {@link Closure}.
 */
public class DelegatingInterceptor extends DelegateProcessor {

    private Closure closure;
    
    public DelegatingInterceptor(Closure closure) {
        this.closure = closure;
    }
    
    public Closure getClosure() {
        return closure;
    }
    
    public void proceed(Exchange exchange) throws Exception {
        super.processNext(exchange);
    }
    
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        ClosureAdapter.callClosure(closure, exchange, this);
    }

}