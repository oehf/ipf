package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;

/**
 * A transmogrifier that delegates to a {@link Closure}.
 */
public class DelegatingTransmogrifier extends ClosureAdapter implements Transmogrifier<Object, Object> {

    public DelegatingTransmogrifier(Closure closure) {
        super(closure);
    }

    public Object zap(Object object, Object... params) {
        if (getClosure().getParameterTypes().length == 2) {
            return call(new Object[] {object, prepare(params)});
        } else {
            return call(object);
        }
    }

    protected static Object prepare(Object... params) {
        if (params == null) {
            return null;
        }
        if (params.length == 1) {
            return params[0];
        }
        return params;
    }
}