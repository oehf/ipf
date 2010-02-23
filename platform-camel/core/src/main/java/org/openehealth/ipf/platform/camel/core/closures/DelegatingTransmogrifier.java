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

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;

/**
 * A transmogrifier that delegates to a {@link Closure}.
 * 
 * @author Martin Krasser
 */
public class DelegatingTransmogrifier extends ClosureAdapter implements Transmogrifier<Object, Object> {

    public DelegatingTransmogrifier(Closure closure) {
        super(closure);
    }

    @Override
    public Object zap(Object object, Object... params) {
        if (getClosure().getParameterTypes().length == 2) {
            return call(object, prepare(params));
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