package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A processor that delegates to a {@link Closure}
 */
public class DelegatingProcessor extends ClosureAdapter implements Processor {

    public DelegatingProcessor(Closure closure) {
        super(closure);
    }

    public void process(Exchange exchange) throws Exception {
        call(exchange);
    }
    
}