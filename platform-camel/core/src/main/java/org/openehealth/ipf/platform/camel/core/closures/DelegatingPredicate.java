package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.openehealth.ipf.commons.core.modules.api.Predicate;

/**
 * A predicate that delegates to a {@link Closure}.
 */
public class DelegatingPredicate extends ClosureAdapter implements Predicate {

    public DelegatingPredicate(Closure closure) {
        super(closure);
    }

    public boolean matches(Object source, Object... params) {
        if (getClosure().getParameterTypes().length == 2) {
            return (Boolean)call(new Object[] {source, params});
        } else {
            return (Boolean)call(source);
        }
    }
    
}