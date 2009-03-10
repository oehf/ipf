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
package org.openehealth.ipf.platform.camel.http;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;

/**
 * An endpoint implementation that delegates all calls to another endpoint
 * <p>
 * This class can be used as a base class to plug in custom functionality 
 * into an existing endpoint
 * @author Jens Riemschneider
 */
public class DelegatingEndpoint implements Endpoint {
    private Endpoint originalEndpoint;

    /**
     * Constructs the new endpoint
     * @param originalEndpoint
     *          the endpoint that this endpoint delegates to
     */
    public DelegatingEndpoint(Endpoint originalEndpoint) {
        notNull(originalEndpoint, "originalEndpoint cannot be null");
        this.originalEndpoint = originalEndpoint;
    }

    @Override
    public void configureProperties(Map options) {
        originalEndpoint.configureProperties(options);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return originalEndpoint.createConsumer(processor);
    }

    @Override
    public Exchange createExchange() {
        return originalEndpoint.createExchange();
    }

    @Override
    public Exchange createExchange(ExchangePattern pattern) {
        return originalEndpoint.createExchange(pattern);
    }

    @Override
    public Exchange createExchange(Exchange exchange) {
        return originalEndpoint.createExchange(exchange);
    }

    @Override
    public PollingConsumer createPollingConsumer() throws Exception {
        return originalEndpoint.createPollingConsumer();
    }

    @Override
    public Producer createProducer() throws Exception {
        return originalEndpoint.createProducer();
    }

    @Override
    public CamelContext getCamelContext() {
        return originalEndpoint.getCamelContext();
    }

    @Override
    @Deprecated
    public CamelContext getContext() {
        return originalEndpoint.getContext();
    }

    @Override
    public String getEndpointUri() {
        return originalEndpoint.getEndpointUri();
    }

    @Override
    public boolean isLenientProperties() {
        return originalEndpoint.isLenientProperties();
    }

    @Override
    public boolean isSingleton() {
        return originalEndpoint.isSingleton();
    }

    @Override
    public void setCamelContext(CamelContext context) {
        originalEndpoint.setCamelContext(context);
    }

    @Override
    @Deprecated
    public void setContext(CamelContext context) {
        originalEndpoint.setContext(context);
    }
}
