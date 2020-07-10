/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

/**
 * A validator that delegates to a {@link Closure}.
 * 
 * @author Martin Krasser
 */
public class DelegatingValidator extends ClosureAdapter<Boolean> implements Validator<Object, Object> {

    public DelegatingValidator(Closure<Boolean> closure) {
        super(closure);
    }

    @Override
    public void validate(Object message, Object profile) {
        var result = validateInternal(message, profile);
        if (!result) {
            throw new ValidationException("validation closure returned false");
        }
    }
    
    private Boolean validateInternal(Object message, Object profile) {
        if (getClosure().getParameterTypes().length == 2) {
            return call(message, profile);
        } else {
            return call(message);
        }
    }
    
}