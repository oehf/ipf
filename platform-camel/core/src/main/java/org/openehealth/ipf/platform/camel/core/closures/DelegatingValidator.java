package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

/**
 * A validator that delegates to a {@link Closure}.
 */
public class DelegatingValidator extends ClosureAdapter implements Validator<Object, Object> {

    public DelegatingValidator(Closure closure) {
        super(closure);
    }

    public void validate(Object message, Object profile) {
        Object result = null;
        result = validateInternal(message, profile);
        if (result instanceof Boolean == false) {
            return;
        }
        if (!(Boolean)result) {
            throw new ValidationException("validation closure returned false");
        }
    }
    
    private Object validateInternal(Object message, Object profile) {
        if (getClosure().getParameterTypes().length == 2) {
            return call(new Object[] {message, profile});
        } else {
            return call(message);
        }
    }
    
}