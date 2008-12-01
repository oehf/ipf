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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.StaticParams;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

/**
 * @author Martin Krasser
 */
public abstract class ProcessorAdapterType extends ProcessorType {

    private List outputs = new ArrayList();
    
    private Expression inputExpression;
    private Expression paramsExpression;
    
    public ProcessorAdapterType input(Expression inputExpression) {
        this.inputExpression = inputExpression;
        return this;
    }
    
    public ProcessorAdapterType input(Closure inputExpression) {
        this.inputExpression = new DelegatingExpression(inputExpression);
        return this;
    }
    
    public ProcessorAdapterType params(Expression paramsExpression) {
        this.paramsExpression = paramsExpression;
        return this;
    }
    
    public ProcessorAdapterType params(Closure paramsExpression) {
        this.paramsExpression = new DelegatingExpression(paramsExpression);
        return this;
    }
    
    public ProcessorAdapterType staticParams(Object... params) {
        this.paramsExpression = new StaticParams(params);
        return this;
    }
    
    public ParamsType params() {
        return new ParamsType(this);
    }
    
    @Override
    public List getOutputs() {
        return outputs;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        ProcessorAdapter adapter = doCreateProcessor(routeContext);
        if (inputExpression != null) {
            adapter.input(inputExpression);
        }
        if (paramsExpression != null) {
            adapter.params(paramsExpression);
        }
        return new AdaptingProcessor(adapter, routeContext.createProcessor(this));
    }
    
    protected abstract ProcessorAdapter doCreateProcessor(RouteContext routeContext);
    
    private static class AdaptingProcessor extends DelegateProcessor {
        
        private Processor adaptee;

        public AdaptingProcessor(Processor adaptee, Processor next) {
            super(next);
            this.adaptee = adaptee;
        }
        
        @Override
        public String toString() {
            String adapteeName = adaptee.getClass().getSimpleName();
            return adapteeName + "[" + getProcessor() + "]";
        }

        @Override
        protected void processNext(Exchange exchange) throws Exception {
            adaptee.process(exchange);
            
            if (exchange.getPattern().isOutCapable()) {
                exchange.getIn().copyFrom(exchange.getOut());
            }
            
            if (getProcessor() != null) {
                exchange.setOut(null);
            }
            
            if (!exchange.isFailed()) {
                super.processNext(exchange);
            }
        }

    }
    
}
