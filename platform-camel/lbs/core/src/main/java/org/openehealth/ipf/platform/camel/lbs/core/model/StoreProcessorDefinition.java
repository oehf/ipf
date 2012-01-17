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
package org.openehealth.ipf.platform.camel.lbs.core.model;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.lbs.core.process.StoreProcessor;

/**
 * Processor definition for {@link StoreProcessor}
 * <p>
 * This processor definition provides the fluent API configuration of the processor.
 * @author Jens Riemschneider
 */
public class StoreProcessorDefinition extends OutputDefinition<StoreProcessorDefinition> {

    private final List<ProcessorDefinition<?>> outputs = new ArrayList<ProcessorDefinition<?>>();
    
    private String resourceHandlersBeanName;
    
    /**
     * Configures the resource handlers via a bean
     * @param resourceHandlersBeanName
     *          the bean name of the handler for integrating resources
     */
    public StoreProcessorDefinition with(String resourceHandlersBeanName) {
        notNull(resourceHandlersBeanName, "resourceHandlersBeanName cannot be null");
        this.resourceHandlersBeanName = resourceHandlersBeanName;
        return this;
    }
    
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        StoreProcessor storer = new StoreProcessor();

        storer.setProcessor(createChildProcessor(routeContext, false));

        if (resourceHandlersBeanName == null) {
            throw new IllegalStateException("resource handlers must be set via with()");
        }
        
        storer.with(routeContext.lookup(resourceHandlersBeanName, List.class));
        
        return storer;
    }
    
    @Override
    public List getOutputs() {
        return outputs;
    }
    
    @Override
    public String toString() {
        return String.format("{%1$s: resourceHandlersBeanName=%2$s}",
                getClass().getSimpleName(), resourceHandlersBeanName);
    }
}
