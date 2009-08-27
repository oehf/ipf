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
package org.openehealth.ipf.platform.camel.core.model;

import groovy.lang.Closure;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.StaticParams;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

/**
 * @author Martin Krasser
 */
public abstract class ProcessorAdapterDefinition extends DelegateDefinition {

    private Expression inputExpression;
    private Expression paramsExpression;
    
    public ProcessorAdapterDefinition input(Expression inputExpression) {
        this.inputExpression = inputExpression;
        return this;
    }
    
    public ProcessorAdapterDefinition input(Closure inputExpression) {
        this.inputExpression = new DelegatingExpression(inputExpression);
        return this;
    }
    
    public ProcessorAdapterDefinition params(Expression paramsExpression) {
        this.paramsExpression = paramsExpression;
        return this;
    }
    
    public ProcessorAdapterDefinition params(Closure paramsExpression) {
        this.paramsExpression = new DelegatingExpression(paramsExpression);
        return this;
    }
    
    public ProcessorAdapterDefinition staticParams(Object... params) {
        this.paramsExpression = new StaticParams(params);
        return this;
    }
    
    public ParamsDefinition params() {
        return new ParamsDefinition(this);
    }
    
    @Override
    protected Processor doCreateDelegate(RouteContext routeContext) {
        ProcessorAdapter adapter = doCreateProcessor(routeContext);
        if (inputExpression != null) {
            adapter.input(inputExpression);
        }
        if (paramsExpression != null) {
            adapter.params(paramsExpression);
        }
        return adapter;
    }

    protected abstract ProcessorAdapter doCreateProcessor(RouteContext routeContext);
    
}
