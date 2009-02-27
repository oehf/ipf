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
package org.openehealth.ipf.commons.event;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Subscription information for a single subscription of a handler
 * @author Jens Riemschneider
 */
public class Subscription {
    private EventFilter filter;
    private EventHandler handler;
    private String topic;

    /**
     * Default constructor 
     */
    public Subscription() {
    }

    /**
     * Constructs a subscription for a handler to the default topic
     * @param handler
     *          the handler
     */
    public Subscription(EventHandler handler) {
        notNull(handler, "handler cannot be null");
        this.handler = handler;
    }
    
    /**
     * Constructs a subscription for a handler to a specific topic
     * @param handler
     *          the handler
     * @param topic
     *          the topic
     */
    public Subscription(EventHandler handler, String topic) {
        notNull(handler, "handler cannot be null");
        notNull(topic, "topic cannot be null");
        this.handler = handler;
        this.topic = topic;
    }
    
    /**
     * Constructs a subscription for a handler to a topic with filtering 
     * @param handler
     *          the handler
     * @param topic
     *          the topic
     * @param filter
     *          the filter
     */
    public Subscription(EventHandler handler, String topic, EventFilter filter) {
        notNull(handler, "handler cannot be null");
        notNull(topic, "topic cannot be null");
        notNull(filter, "filter cannot be null");
        this.handler = handler;
        this.topic = topic;
        this.filter = filter;
    }
    
    /**
     * @return the filter or <code>null</code> if no filtering is configured
     */
    public EventFilter getFilter() {
        return filter;
    }    
    
    /**
     * @param filter
     *          the filter to call before handing an event to a handler, 
     *          <code>null</code> if filtering is not used
     */
    public void setFilter(EventFilter filter) {
        this.filter = filter;
    }

    /**
     * @return the handler that is informed about events
     */
    public EventHandler getHandler() {
        return handler;
    }
    
    /**
     * @param handler the handler to set
     */
    public void setHandler(EventHandler handler) {
        notNull(handler, "handler cannot be null");
        this.handler = handler;
    }

    /**
     * @return the topic that this subscription is for, <code>null</code> to use the
     *         default topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the topic of this subscription
     * @param topic
     *          the topic that this subscription is for, <code>null</code> to use
     *          the default topic
     */
    public void setTopic(String topic) {
        notNull(topic, "topic cannot be null");
        this.topic = topic;
    }
}
