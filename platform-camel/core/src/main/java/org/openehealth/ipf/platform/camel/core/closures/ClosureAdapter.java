package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.codehaus.groovy.runtime.InvokerInvocationException;

/**
 * Base class for concrete closure adapters.
 */
public abstract class ClosureAdapter {
    
    private Closure closure;
    
    public ClosureAdapter(Closure closure) {
        this.closure = closure;
    }
    
    public Closure getClosure() {
        return closure;
    }
    
    public Object call(Object... args) {
        return callClosure(closure, args);
    }

    public static Object callClosure(Closure closure, Object... args) {
        try {
            return closure.call(args);
        } catch (InvokerInvocationException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e.getCause();
            } else {
                throw e;
            }
        }
    }
    
}