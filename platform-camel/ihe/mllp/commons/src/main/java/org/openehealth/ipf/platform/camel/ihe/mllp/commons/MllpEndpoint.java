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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.mina.MinaConfiguration;
import org.apache.camel.component.mina.MinaEndpoint;
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

import ca.uhn.hl7v2.parser.Parser;


/**
 * A wrapper for standard camel-mina endpoint 
 * which provides support for IHE PIX/PDQ-related extensions.
 *  
 * @author Dmytro Rud
 */
public class MllpEndpoint extends DefaultEndpoint {

    private final MinaEndpoint wrappedEndpoint;
    private final boolean audit;
    private final boolean allowIncompleteAudit;
    private final MllpAuditStrategy serverStrategy;
    private final MllpAuditStrategy clientStrategy;
    private final MllpTransactionConfiguration transactionConfiguration;
    private final Parser parser;
    
    
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
            MllpTransactionConfiguration transactionConfiguration,
            Parser parser) 
    {
        Validate.notNull(wrappedEndpoint);
        Validate.notNull(serverStrategy);
        Validate.notNull(clientStrategy);
        Validate.notNull(transactionConfiguration);
        Validate.notNull(parser);
        
        this.wrappedEndpoint = wrappedEndpoint;
        this.audit = audit;
        this.allowIncompleteAudit = allowIncompleteAudit;
        this.serverStrategy = serverStrategy;
        this.clientStrategy = clientStrategy;
        this.transactionConfiguration = transactionConfiguration;
        this.parser = parser;
    }


    /**
     * Wraps the original starting point of the consumer route 
     * into a set of PIX/PDQ-specific interceptors.
     * @param processor
     *      The original consumer processor.  
     */
    public Consumer createConsumer(Processor processor) throws Exception {
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
    public Producer createProducer() throws Exception {
        Producer x = this.wrappedEndpoint.createProducer();
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
    
    /**
     * Returns transaction configuration.
     */
    public MllpTransactionConfiguration getTransactionConfiguration() {
        return transactionConfiguration;
    }

    /**
     * Returns HL7 parser instance.
     */
    public Parser getParser() {
        return parser;
    }

    
    // ----- dumb delegation, nothing interesting below -----

    @SuppressWarnings("unchecked")
    @Override
    public void configureProperties(Map options) {
        wrappedEndpoint.configureProperties(options);
    }

    @Override
    public Exchange createExchange() {
        return wrappedEndpoint.createExchange();
    }

    @Override
    public Exchange createExchange(Exchange exchange) {
        return wrappedEndpoint.createExchange(exchange);
    }

    @Override
    public Exchange createExchange(ExchangePattern pattern) {
        return wrappedEndpoint.createExchange(pattern);
    }

    public Exchange createExchange(IoSession session, Object payload) {
        return wrappedEndpoint.createExchange(session, payload);
    }

    @Override
    public PollingConsumer createPollingConsumer() throws Exception {
        return wrappedEndpoint.createPollingConsumer();
    }

    @Override
    public boolean equals(Object object) {
        return wrappedEndpoint.equals(object);
    }

    public IoAcceptor getAcceptor() {
        return wrappedEndpoint.getAcceptor();
    }

    public IoAcceptorConfig getAcceptorConfig() {
        return wrappedEndpoint.getAcceptorConfig();
    }

    public SocketAddress getAddress() {
        return wrappedEndpoint.getAddress();
    }

    @Override
    public CamelContext getCamelContext() {
        return wrappedEndpoint.getCamelContext();
    }

    @Override
    public Component getComponent() {
        return wrappedEndpoint.getComponent();
    }

    public MinaConfiguration getConfiguration() {
        return wrappedEndpoint.getConfiguration();
    }

    public IoConnector getConnector() {
        return wrappedEndpoint.getConnector();
    }

    public IoConnectorConfig getConnectorConfig() {
        return wrappedEndpoint.getConnectorConfig();
    }

    @Override
    public String getEndpointKey() {
        return wrappedEndpoint.getEndpointKey();
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
    public Class<Exchange> getExchangeType() {
        return wrappedEndpoint.getExchangeType();
    }

    @Override
    public ExecutorService getExecutorService() {
        return wrappedEndpoint.getExecutorService();
    }

    @Override
    public ScheduledExecutorService getScheduledExecutorService() {
        return wrappedEndpoint.getScheduledExecutorService();
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
    public boolean isSingleton() {
        return wrappedEndpoint.isSingleton();
    }

    public void setAcceptor(IoAcceptor acceptor) {
        wrappedEndpoint.setAcceptor(acceptor);
    }

    public void setAcceptorConfig(IoAcceptorConfig acceptorConfig) {
        wrappedEndpoint.setAcceptorConfig(acceptorConfig);
    }

    public void setAddress(SocketAddress address) {
        wrappedEndpoint.setAddress(address);
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        wrappedEndpoint.setCamelContext(camelContext);
    }

    public void setConfiguration(MinaConfiguration configuration) {
        wrappedEndpoint.setConfiguration(configuration);
    }

    public void setConnector(IoConnector connector) {
        wrappedEndpoint.setConnector(connector);
    }

    public void setConnectorConfig(IoConnectorConfig connectorConfig) {
        wrappedEndpoint.setConnectorConfig(connectorConfig);
    }

    @Override
    public void setEndpointUriIfNotSpecified(String value) {
        wrappedEndpoint.setEndpointUriIfNotSpecified(value);
    }

    @Override
    public void setExchangePattern(ExchangePattern exchangePattern) {
        wrappedEndpoint.setExchangePattern(exchangePattern);
    }

    @Override
    public void setExecutorService(ExecutorService executorService) {
        wrappedEndpoint.setExecutorService(executorService);
    }

    @Override
    public String toString() {
        return wrappedEndpoint.toString();
    }
}
