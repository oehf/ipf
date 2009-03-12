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

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.http.HttpBinding;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpConsumer;
import org.apache.camel.component.http.HttpExchange;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * An endpoint implementation that delegates all calls to another endpoint
 * <p>
 * This class can be used as a base class to plug in custom functionality 
 * into an existing endpoint
 * @author Jens Riemschneider
 */
public class HttpEndpoint extends org.apache.camel.component.http.HttpEndpoint {
    private org.apache.camel.component.http.HttpEndpoint originalEndpoint;

    /**
     * Constructs the new endpoint
     * @param originalEndpoint
     *          the endpoint that this endpoint delegates to
     * @throws Exception 
     */
    public HttpEndpoint(org.apache.camel.component.http.HttpEndpoint originalEndpoint) throws Exception {
        super(originalEndpoint.getEndpointUri(), 
                (HttpComponent)originalEndpoint.getComponent(), 
                originalEndpoint.getHttpUri(), null);
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
    public HttpExchange createExchange() {
        return originalEndpoint.createExchange();
    }

    @Override
    public HttpExchange createExchange(ExchangePattern pattern) {
        return originalEndpoint.createExchange(pattern);
    }

    @Override
    public HttpExchange createExchange(Exchange exchange) {
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

    @Override
    public HttpExchange createExchange(HttpServletRequest request, HttpServletResponse response) {
        return originalEndpoint.createExchange(request, response);
    }

    @Override
    public HttpClient createHttpClient() {
        return originalEndpoint.createHttpClient();
    }

    @Override
    public void connect(HttpConsumer consumer) throws Exception {
        originalEndpoint.connect(consumer);
    }

    @Override
    public void disconnect(HttpConsumer consumer) throws Exception {
        originalEndpoint.disconnect(consumer);
    }

    @Override
    public HttpClientParams getClientParams() {
        return originalEndpoint.getClientParams();
    }

    @Override
    public void setClientParams(HttpClientParams clientParams) {
        originalEndpoint.setClientParams(clientParams);
    }

    @Override
    public HttpClientConfigurer getHttpClientConfigurer() {
        return originalEndpoint.getHttpClientConfigurer();
    }

    @Override
    public void setHttpClientConfigurer(HttpClientConfigurer httpClientConfigurer) {
        originalEndpoint.setHttpClientConfigurer(httpClientConfigurer);
    }

    @Override
    public HttpBinding getBinding() {
        return originalEndpoint.getBinding();
    }

    @Override
    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return originalEndpoint.getHeaderFilterStrategy();
    }

    @Override
    public void setBinding(HttpBinding binding) {
        originalEndpoint.setBinding(binding);
    }

    @Override
    public String getPath() {
        return originalEndpoint.getPath();
    }

    @Override
    public int getPort() {
        return originalEndpoint.getPort();
    }

    @Override
    public String getProtocol() {
        return originalEndpoint.getProtocol();
    }

    @Override
    public URI getHttpUri() {
        return originalEndpoint.getHttpUri();
    }
}

