package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * An expression that delegates to a {@link Closure}
 */
public class DelegatingExpression extends ClosureAdapter implements Expression {

    public DelegatingExpression(Closure closure) {
        super(closure);
    }

    public Object evaluate(Exchange exchange) {
        return call(exchange);
    }
    
}