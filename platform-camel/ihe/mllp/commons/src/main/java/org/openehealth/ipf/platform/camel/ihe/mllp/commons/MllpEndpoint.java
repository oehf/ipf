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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.mina.MinaEndpoint;
import org.apache.camel.component.mina.MinaExchange;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.lang.Validate;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoSession;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer.MllpConsumerAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer.MllpConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.consumer.MllpConsumerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer.MllpProducerAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer.MllpProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer.MllpProducerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.producer.MllpProducerMarshalInterceptor;


/**
 * A wrapper for standard camel-mina endpoint 
 * which provides support for IHE PIX/PDQ-related extensions.
 *  
 * @author Dmytro Rud
 */
public class MllpEndpoint extends DefaultEndpoint<MinaExchange> {

    private final MinaEndpoint wrappedEndpoint;
    private final boolean audit;
    private final boolean allowIncompleteAudit;
    private final MllpAuditStrategy serverStrategy;
    private final MllpAuditStrategy clientStrategy;
    private final MllpEndpointConfiguration endpointConfiguration;
    
    
    /**
     * Constructor.
     * @param wrappedEndpoint
     *      The original camel-mina endpoint instance.
     * @param audit
     *      Whether ATNA auditing should be performed.
     * @param allowIncompleteAudit
     *      Whether incomplete ATNA auditing are allowed as well.
     * @param serverStrategy
     *      Server-side audit strategy.
     * @param clientStrategy
     *      Client-side audit strategy.
     */
    public MllpEndpoint(
            MinaEndpoint wrappedEndpoint,
            boolean audit,
            boolean allowIncompleteAudit,
            MllpAuditStrategy serverStrategy,
            MllpAuditStrategy clientStrategy,
            MllpEndpointConfiguration endpointConfiguration) 
    {
        Validate.notNull(wrappedEndpoint);
        Validate.notNull(serverStrategy);
        Validate.notNull(clientStrategy);
        Validate.notNull(endpointConfiguration);
        
        this.wrappedEndpoint = wrappedEndpoint;
        this.audit = audit;
        this.allowIncompleteAudit = allowIncompleteAudit;
        this.serverStrategy = serverStrategy;
        this.clientStrategy = clientStrategy;
        this.endpointConfiguration = endpointConfiguration;
    }


    /**
     * Wraps the original starting point of the consumer route 
     * into a set of PIX/PDQ-specific interceptors.
     * @param processor
     *      The original consumer processor.  
     */
    public Consumer<MinaExchange> createConsumer(Processor processor) throws Exception {
        Processor x = processor;
        if(isAudit()) {
            x = new MllpConsumerAuditInterceptor(this, x);
        }
        x = new MllpConsumerAcceptanceInterceptor(this, x);
        x = new MllpConsumerMarshalInterceptor(this, x);
        return this.wrappedEndpoint.createConsumer(x);
    }


    /**
     * Wraps the original camel-mina producer  
     * into a set of PIX/PDQ-specific ones.
     */
    public Producer<MinaExchange> createProducer() throws Exception {
        Producer<MinaExchange> x = this.wrappedEndpoint.createProducer();
        x = new MllpProducerMarshalInterceptor(this, x);
        if(isAudit()) {
            x = new MllpProducerAuditInterceptor(this, x);
        }
        x = new MllpProducerAcceptanceInterceptor(this, x);
        x = new MllpProducerAdaptingInterceptor(this, x);
        return x;
    }

    
    // ----- getters -----
    
    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    public boolean isAudit() { 
        return audit;
    }
    
    /**
     * Returns <tt>true</tt> when incomplete ATNA auditing records are allowed as well.
     */
    public boolean isAllowIncompleteAudit() { 
        return allowIncompleteAudit;
    }
    
    /**
     * Returns client-side audit strategy instance.
     */
    public MllpAuditStrategy getClientAuditStrategy() {
        return clientStrategy;
    }

    /**
     * Returns server-side audit strategy instance.
     */
    public MllpAuditStrategy getServerAuditStrategy() {
        return serverStrategy;
    }
    
    public MllpEndpointConfiguration getEndpointConfiguration() {
        return endpointConfiguration;
    }
    
    // ----- dumb delegation, nothing interesting below -----

    @Override
    public MinaExchange createExchange(ExchangePattern pattern) {
        return wrappedEndpoint.createExchange(pattern);
    }

    public MinaExchange createExchange(IoSession session, Object payload) {
        return wrappedEndpoint.createExchange(session, payload);
    }

    public IoAcceptor getAcceptor() {
        return wrappedEndpoint.getAcceptor();
    }

    public SocketAddress getAddress() {
        return wrappedEndpoint.getAddress();
    }

    public IoConnector getConnector() {
        return wrappedEndpoint.getConnector();
    }

    public boolean isLazySessionCreation() {
        return wrappedEndpoint.isLazySessionCreation();
    }

    public IoAcceptorConfig getAcceptorConfig() {
        return wrappedEndpoint.getAcceptorConfig();
    }

    public IoConnectorConfig getConnectorConfig() {
        return wrappedEndpoint.getConnectorConfig();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configureProperties(Map options) {
        wrappedEndpoint.configureProperties(options);
    }

    @Override
    public MinaExchange convertTo(Class<MinaExchange> type, Exchange exchange) {
        return wrappedEndpoint.convertTo(type, exchange);
    }

    @Override
    public MinaExchange createExchange() {
        return wrappedEndpoint.createExchange();
    }

    @Override
    public MinaExchange createExchange(Exchange exchange) {
        return wrappedEndpoint.createExchange(exchange);
    }

    @Override
    public PollingConsumer<MinaExchange> createPollingConsumer() throws Exception {
        return wrappedEndpoint.createPollingConsumer();
    }

    @Override
    public CamelContext getCamelContext() {
        return wrappedEndpoint.getCamelContext();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getComponent() {
        return wrappedEndpoint.getComponent();
    }

    @SuppressWarnings("deprecation")
    @Override
    public CamelContext getContext() {
        return wrappedEndpoint.getContext();
    }

    @Override
    public String getEndpointUri() {
        return wrappedEndpoint.getEndpointUri();
    }

    @Override
    public ExchangePattern getExchangePattern() {
        return wrappedEndpoint.getExchangePattern();
    }

    @Override
    public Class<MinaExchange> getExchangeType() {
        return wrappedEndpoint.getExchangeType();
    }

    @Override
    public synchronized ScheduledExecutorService getExecutorService() {
        return wrappedEndpoint.getExecutorService();
    }

    @Override
    public int hashCode() {
        return wrappedEndpoint.hashCode();
    }

    @Override
    public boolean isLenientProperties() {
        return wrappedEndpoint.isLenientProperties();
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        wrappedEndpoint.setCamelContext(camelContext);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setContext(CamelContext context) {
        wrappedEndpoint.setContext(context);
    }

    @Override
    public void setExchangePattern(ExchangePattern exchangePattern) {
        wrappedEndpoint.setExchangePattern(exchangePattern);
    }

    @Override
    public synchronized void setExecutorService(ScheduledExecutorService executorService) {
        wrappedEndpoint.setExecutorService(executorService);
    }

    @Override
    public String toString() {
        return wrappedEndpoint.toString();
    }

    public boolean isSingleton() {
        return wrappedEndpoint.isSingleton();
    }

    public long getTimeout() {
        return wrappedEndpoint.getTimeout();
    }

    public boolean isTransferExchange() {
        return wrappedEndpoint.isTransferExchange();
    }

    public boolean isSync() {
        return wrappedEndpoint.isSync();
    }

    public void setCharsetName(String charset) {
        wrappedEndpoint.setCharsetName(charset);
    }

    public String getCharsetName() {
        return wrappedEndpoint.getCharsetName();
    }
}
