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

import org.apache.camel.Exchange;
import org.apache.camel.processor.DelegateProcessor;

/**
 * An interceptor that delegates to a {@link Closure}.
 * 
 * @author Martin Krasser
 */
public class DelegatingInterceptor extends DelegateProcessor {

    private final Closure closure;
    
    public DelegatingInterceptor(Closure closure) {
        this.closure = closure;
    }
    
    public Closure getClosure() {
        return closure;
    }
    
    @Override
    public void proceed(Exchange exchange) throws Exception {
        super.processNext(exchange);
    }
    
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        ClosureAdapter.callClosure(closure, exchange, this);
    }

}