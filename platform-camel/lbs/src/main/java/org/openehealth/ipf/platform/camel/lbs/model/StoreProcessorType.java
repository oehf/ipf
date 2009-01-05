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
package org.openehealth.ipf.platform.camel.lbs.model;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.model.ProcessorTypeSupport;
import org.openehealth.ipf.platform.camel.lbs.process.AttachmentHandler;
import org.openehealth.ipf.platform.camel.lbs.process.StoreProcessor;

/**
 * Processor type for {@link StoreProcessor}
 * <p>
 * This processor type provides the fluent API configuration of the processor.
 * @author Jens Riemschneider
 */
public class StoreProcessorType extends ProcessorTypeSupport {

    private List<ProcessorType<?>> outputs = new ArrayList<ProcessorType<?>>();
    
    private AttachmentHandler handler;
    private String handlerBean;
    
    /**
     * Sets the {@link AttachmentHandler} of the processor.
     * <p>
     * An attachment handler enables the processor to handle a specific type of
     * endpoint exchange. The handler contains the actual strategy for 
     * extracting attachments from a specific message type (e.g. an HTTP message).
     * <p>
     * When calling this method or {@link #with(String)} multiple times the 
     * result is undefined.
     * <p>
     * This method or {@link #with(String)} must be called at least once.
     * Otherwise an {@link IllegalArgumentException} is thrown when creating
     * the route.
     *  
     * @param handler
     *          the handler for extracting attachments
     * @return this type instance for usage with a fluent API
     */
    public StoreProcessorType with(AttachmentHandler handler) {
        notNull(handler, "handler cannot be null");
        this.handler = handler;
        return this;
    }
    
    /**
     * Sets the {@link AttachmentHandler} of the processor.
     * <p>
     * An attachment handler enables the processor to handle a specific type of
     * endpoint exchange. The handler contains the actual strategy for 
     * extracting attachments from a specific message type (e.g. an HTTP message).
     * <p>
     * When calling this method or {@link #with(AttachmentHandler)} multiple  
     * times the result is undefined.  
     * <p>
     * This method or {@link #with(AttachmentHandler)} must be called at 
     * least once. Otherwise an {@link IllegalArgumentException} is thrown when 
     * creating the route.
     *
     * @param handlerBeanName
     *          the bean name of the handler for extracting attachments
     * @return this type instance for usage with a fluent API
     */
    public StoreProcessorType with(String handlerBeanName) {
        notNull(handlerBeanName, "handlerBeanName cannot be null");
        this.handlerBean = handlerBeanName;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.model.ProcessorType#createProcessor(org.apache.camel.spi.RouteContext)
     */
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        StoreProcessor storer = new StoreProcessor();

        storer.setProcessor(routeContext.createProcessor(this));
        
        if (handler == null) {
            handler = routeContext.lookup(handlerBean, AttachmentHandler.class);
        }
        
        if (handler == null) {
            throw new IllegalStateException("attachment handler must be set via handleBy()");
        }

        storer.with(handler);        
        return storer;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.model.ProcessorType#getOutputs()
     */
    @Override
    public List getOutputs() {
        return outputs;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: handler=%2$s, handlerBean=%3$s}",
                getClass().getSimpleName(), handler, handlerBean);
    }
}
