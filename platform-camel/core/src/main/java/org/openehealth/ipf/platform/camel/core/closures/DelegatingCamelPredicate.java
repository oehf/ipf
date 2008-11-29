package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

/**
 * An Camel predicate that delegates to a {@link Closure}.
 */
public class DelegatingCamelPredicate extends ClosureAdapter implements org.apache.camel.Predicate {

    public DelegatingCamelPredicate(Closure closure) {
        super(closure);
    }

    public void assertMatches(String text, Object exchange) throws AssertionError {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean matches(Object exchange) {
        return (Boolean)call(exchange);
    }
    
}