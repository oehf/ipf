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
package org.openehealth.ipf.platform.camel.event.model;

import static org.apache.commons.lang3.Validate.notNull;

import groovy.lang.Closure;

import org.apache.camel.Processor;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.platform.camel.event.process.Publisher;

/**
 * Processor definition for {@link Publisher}
 * <p>
 * This processor definition provides the fluent API configuration of the processor.
 * @author Jens Riemschneider
 */
public class PublishProcessorDefinition extends OutputDefinition<PublishProcessorDefinition> {
    /** The default name of the event engine bean */
    public static final String DEFAULT_EVENT_ENGINE_BEAN = "eventEngine";
    
    private Closure closure;
    private String topic;
    private boolean sync = true;
    private String eventEngineBean = DEFAULT_EVENT_ENGINE_BEAN;

    /**
     * Configures the topic that an event is publish with. If this method is 
     * not called, the event is published to the topic "default".
     * @param topic
     *          the topic
     */
    public PublishProcessorDefinition toTopic(String topic) {
        notNull(topic, "topic cannot be null");
        this.topic = topic;
        return this;
    }
    
    /**
     * Sets the closure that is used to create the event
     * @param closure
     *          the closure to use
     */
    public PublishProcessorDefinition eventFactoryClosure(Closure closure) {
        notNull(closure, "closure cannot be null");
        this.closure = closure;
        return this;
    }
    
    /**
     * Configures synchronous event object delivery (which is the default)
     */
    public PublishProcessorDefinition synchronous() {
        sync = true;
        return this;
    }

    /**
     * Configures asynchronous event object delivery
     */
    public PublishProcessorDefinition asynchronous() {
        sync = false;
        return this;
    }
    
    /**
     * Configures the event engine that the event object is published with
     * @param eventEngineBean
     *          the name of a bean in the application context that represents the
     *          event engine
     */
    public PublishProcessorDefinition with(String eventEngineBean) {
        notNull(eventEngineBean, "eventEngineBean cannot be null");
        this.eventEngineBean = eventEngineBean;
        return this;
    }

    @Override
    public Processor createProcessor(final RouteContext routeContext) throws Exception {
        notNull(routeContext, "routeContext cannot be null");
        notNull(closure, "closure cannot be null");   
        
        EventEngine eventEngine = routeContext.lookup(eventEngineBean, EventEngine.class);
        
        Publisher publisher = new Publisher();        
        publisher.eventFactoryClosure(closure).topic(topic).with(eventEngine);
        if (sync) {
            publisher.synchronous();
        }
        else {
            publisher.asynchronous();
        }
        
        publisher.setProcessor(createChildProcessor(routeContext, false));
        return publisher;
    }
}
