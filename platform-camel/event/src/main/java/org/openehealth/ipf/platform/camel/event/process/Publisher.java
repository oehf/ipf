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
package org.openehealth.ipf.platform.camel.event.process;

import static org.apache.commons.lang.Validate.notNull;

import groovy.lang.Closure;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.DelegateProcessor;
import org.openehealth.ipf.commons.event.EventEngine;
import org.openehealth.ipf.commons.event.EventObject;

/**
 * The Camel processor to use within routes to create and publish events
 * @author Jens Riemschneider
 */
public class Publisher extends DelegateProcessor {
    private Closure closure;
    private String topic;
    private boolean sync = true;
    private EventEngine eventEngine;

    /**
     * Configures the topic that an event is publish with
     * <p>
     * If this method is not called, the event is published to the topic "default".
     * @param topic
     *          the topic, <code>null</code> to use the default topic
     * @return this instance to allow chaining
     */
    public Publisher topic(String topic) {
        this.topic = topic;
        return this;
    }  
    
    /**
     * Configures synchronous event object delivery
     * <p>
     * Note: The default behavior is synchronous delivery. Calling this method is
     *       usually not necessary.
     * @return this instance to allow chaining
     */
    public Publisher synchronous() {
        this.sync = true;
        return this;
    }

    /**
     * Configures asynchronous event object delivery
     * @return this instance to allow chaining
     */
    public Publisher asynchronous() {
        this.sync = false;
        return this;
    }

    /**
     * Sets a closure that is used to create and configure the event
     * @param closure
     *          the closure to use
     * @return this instance to allow chaining
     */
    public Publisher eventFactoryClosure(Closure closure) {
        notNull(closure, "closure cannot be null");
        this.closure = closure;
        return this;
    }
    
    /**
     * Configures the event engine that the event object is published with
     * @param eventEngine
     *          the event engine
     * @return this instance to allow chaining
     */
    public Publisher with(EventEngine eventEngine) {
        notNull(eventEngine, "eventEngine cannot be null");
        this.eventEngine = eventEngine;
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.processor.DelegateProcessor#processNext(org.apache.camel.Exchange)
     */
    @Override
    protected void processNext(Exchange exchange) throws Exception {
        publish(exchange);
        
        super.processNext(exchange);
    }
    
    private void publish(Exchange exchange) throws Exception {
        Class[] parameterTypes = closure.getParameterTypes();
        if (parameterTypes.length > 1) {
            throw new IllegalArgumentException("Closure defined in the publish() processor must have no argument or a single exchange parameter");
        }
        
        EventObject event = callClosure(exchange, parameterTypes);
        if (event != null) { 
            eventEngine.publish(topic, event, sync);
        }
    }

    private EventObject callClosure(Exchange exchange, Class[] parameterTypes) {
        if (parameterTypes.length == 0) {
            return (EventObject) closure.call();
        }
        
        Class type = parameterTypes[0];
        EventObject event = callClosureWithExchange(exchange, type);
        if (event != null) {
            return event;
        }
        
        event = callClosureWithMessage(exchange, type);
        if (event != null) {
            return event;
        }

        return callClosureWithBody(exchange, type);
    }

    private EventObject callClosureWithBody(Exchange exchange, Class type) {
        Message in = exchange.getIn();
        if (in != null) {
            Object body = in.getBody();
            if (body != null && type.isAssignableFrom(body.getClass())) {
                return (EventObject) closure.call(body);
            }
        }
        return null;
    }

    private EventObject callClosureWithMessage(Exchange exchange, Class type) {
        Message in = exchange.getIn();
        if (in != null && type.isAssignableFrom(in.getClass())) {
            return (EventObject) closure.call(in);
        }
        return null;
    }

    private EventObject callClosureWithExchange(Exchange exchange, Class type) {
        if (type.isAssignableFrom(exchange.getClass())) {
            return (EventObject) closure.call(exchange);
        }
        return null;
    }
}