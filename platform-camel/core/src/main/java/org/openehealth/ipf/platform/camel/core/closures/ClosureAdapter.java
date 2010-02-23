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

import org.codehaus.groovy.runtime.InvokerInvocationException;

/**
 * Base class for concrete closure adapters.
 * 
 * @author Martin Krasser
 */
public abstract class ClosureAdapter {
    
    private final Closure closure;
    
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