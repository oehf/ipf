/*
 * Copyright 2009 the original author or authors.
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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.process.Interceptor;

/**
 * @author Martin Krasser
 */
public class InterceptDefinition extends OutputDefinition<RouteDefinition> {

    private DelegateProcessor delegateProcessor;
    private String interceptorBean;
    
    public InterceptDefinition(DelegateProcessor delegateProcessor) {
        this.delegateProcessor = delegateProcessor;
    }

    public InterceptDefinition(String interceptorBean) {
        this.interceptorBean = interceptorBean;
    }
    
    @Override
    public Processor createProcessor(final RouteContext routeContext) throws Exception {
        if (interceptorBean != null) {
            final Interceptor interceptor = routeContext.lookup(interceptorBean, Interceptor.class);
            delegateProcessor = new DelegateProcessor() {
                @Override
                protected void processNext(Exchange exchange) throws Exception {
                    interceptor.process(exchange, getProcessor());
                }
            };
        }
        delegateProcessor.setProcessor(createChildProcessor(routeContext, false));
    	return delegateProcessor;
    }
    
}
