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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.camel.*;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.component.mina2.Mina2Configuration;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.apache.camel.component.mina2.Mina2Endpoint;
import org.apache.camel.component.mina2.Mina2Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.lang3.Validate;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IoSession;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.openehealth.ipf.commons.ihe.core.chain.ChainUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2InterceptorUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for standard camel-mina endpoint 
 * which provides support for IHE PIX/PDQ-related extensions.
 * @author Dmytro Rud
 */
@ManagedResource(description = "Managed IPF MLLP ITI Endpoint")
public abstract class MllpEndpoint
        <
            ConfigType extends MllpEndpointConfiguration,
            ComponentType extends MllpComponent<ConfigType>
        >
        extends DefaultEndpoint implements Hl7v2ConfigurationHolder
{

    @Getter(AccessLevel.PROTECTED) private final ConfigType config;
    @Getter(AccessLevel.PROTECTED) private final ComponentType mllpComponent;
    @Getter(AccessLevel.PROTECTED) private final Mina2Endpoint wrappedEndpoint;


    /**
     * Constructor.
     * @param mllpComponent
     *      MLLP Component instance which is creating this endpoint.
     * @param wrappedEndpoint
     *      The original camel-mina endpoint instance.
     * @param config
     *      Configuration parameters.
     */
    public MllpEndpoint(
            ComponentType mllpComponent,
            Mina2Endpoint wrappedEndpoint,
            ConfigType config)
    {
        this.mllpComponent = Validate.notNull(mllpComponent);
        this.wrappedEndpoint = Validate.notNull(wrappedEndpoint);
        this.config = Validate.notNull(config);
    }


    private synchronized List<Hl7v2Interceptor> getCustomInterceptors() {
        List<Hl7v2Interceptor> result = new ArrayList<>();
        for (String beanName : config.getCustomInterceptorBeans()) {
            result.add(getCamelContext().getRegistry().lookupByNameAndType(beanName, Hl7v2Interceptor.class));
        }
        for (Hl7v2InterceptorFactory customInterceptorFactory : config.getCustomInterceptorFactories()) {
            result.add(customInterceptorFactory.getNewInstance());
        }
        return result;
    }


    protected abstract List<Hl7v2Interceptor> createInitialConsumerInterceptorChain();
    protected abstract List<Hl7v2Interceptor> createInitialProducerInterceptorChain();


    private List<Hl7v2Interceptor> getConsumerInterceptorChain() {
        // set up initial interceptor chain
        List<Hl7v2Interceptor> initialChain = createInitialConsumerInterceptorChain();

        // add interceptors provided by the user
        List<Hl7v2Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(mllpComponent.getAdditionalConsumerInterceptors());
        additionalInterceptors.addAll(getCustomInterceptors());

        return ChainUtils.createChain(initialChain, additionalInterceptors);
    }


    private List<Hl7v2Interceptor> getProducerInterceptorChain() {
        // set up initial interceptor chain
        List<Hl7v2Interceptor> initialChain = createInitialProducerInterceptorChain();

        // add interceptors provided by the user
        List<Hl7v2Interceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(mllpComponent.getAdditionalProducerInterceptors());
        additionalInterceptors.addAll(getCustomInterceptors());

        return ChainUtils.createChain(initialChain, additionalInterceptors);
    }


    /**
     * Wraps the original starting point of the consumer route 
     * into a set of PIX/PDQ-specific interceptors.
     * @param originalProcessor
     *      The original consumer processor.
     */
    @Override
    public Consumer createConsumer(Processor originalProcessor) throws Exception {
        // configure interceptor chain
        List<Hl7v2Interceptor> chain = getConsumerInterceptorChain();
        Processor processor = originalProcessor;
        for (int i = chain.size() - 1; i >= 0; --i) {
            Hl7v2Interceptor interceptor = chain.get(i);
            interceptor.setConfigurationHolder(this);
            interceptor.setWrappedProcessor(processor);
            processor = interceptor;
        }

        Mina2Consumer consumer = (Mina2Consumer) wrappedEndpoint.createConsumer(processor);
        if (config.getSslContext() != null) {
            DefaultIoFilterChainBuilder filterChain = consumer.getAcceptor().getFilterChain();
            if (! filterChain.contains("ssl")) {
                HandshakeCallbackSSLFilter filter = new HandshakeCallbackSSLFilter(config.getSslContext());
                filter.setNeedClientAuth(config.getClientAuthType() == ClientAuthType.MUST);
                filter.setWantClientAuth(config.getClientAuthType() == ClientAuthType.WANT);
                filter.setHandshakeExceptionCallback(new HandshakeFailureCallback());
                filter.setEnabledProtocols(config.getSslProtocols());
                filter.setEnabledCipherSuites(config.getSslCiphers());
                filterChain.addFirst("ssl", filter);
            }
        }

        return consumer;
    }


    /**
     * Wraps the original camel-mina producer  
     * into a set of PIX/PDQ-specific ones.
     */
    @Override
    public Producer createProducer() throws Exception {
        Mina2Producer producer = (Mina2Producer)wrappedEndpoint.createProducer();

        if (config.getSslContext() != null) {
            DefaultIoFilterChainBuilder filterChain = producer.getFilterChain();
            if (!filterChain.contains("ssl")) {
                HandshakeCallbackSSLFilter filter = new HandshakeCallbackSSLFilter(config.getSslContext());
                filter.setUseClientMode(true);
                filter.setHandshakeExceptionCallback(new HandshakeFailureCallback());
                filter.setEnabledProtocols(config.getSslProtocols());
                filter.setEnabledCipherSuites(config.getSslCiphers());
                filterChain.addFirst("ssl", filter);
            }
        }
        return Hl7v2InterceptorUtils.adaptProducerChain(
                getProducerInterceptorChain(),
                this,
                producer);
    }


    private class HandshakeFailureCallback implements HandshakeCallbackSSLFilter.Callback {
        @Override
        public void run(IoSession session) {
            if (config.isAudit()) {
                String hostAddress = session.getRemoteAddress().toString();
                MllpAuditStrategy.auditAuthenticationNodeFailure(hostAddress);
            }
        }
    }

// ----- getters -----

    /**
     * Returns transaction configuration.
     */
    @Override
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return mllpComponent.getHl7v2TransactionConfiguration();
    }

    /**
     * Returns transaction-specific ACK and NAK factory.
     */
    @Override
    public NakFactory getNakFactory() {
        return mllpComponent.getNakFactory();
    }

    /**
     * Returns <code>true</code> if this endpoint supports segment fragmentation.
     */
    @ManagedAttribute(description = "Support Segment Fragmentation Enabled")
    public boolean isSupportSegmentFragmentation() {
        return config.isSupportSegmentFragmentation();
    }

    /**
     * Returns threshold for segment fragmentation. 
     */
    @ManagedAttribute(description = "Segment Fragmentation Threshold")
    public int getSegmentFragmentationThreshold() {
        return config.getSegmentFragmentationThreshold();
    }

    /**
     * @return the sslContext
     */
    public SSLContext getSslContext() {
        return config.getSslContext();
    }

    /**
     * @return the sslProtocols
     */
    @ManagedAttribute(description = "Defined SSL Protocols")
    public String[] getSslProtocols() {
        return config.getSslProtocols();
    }

    /**
     * @return the sslCiphers
     */
    @ManagedAttribute(description = "Defined SSL Ciphers")
    public String[] getSslCiphers() {
        return config.getSslCiphers();
    }

    @ManagedAttribute(description = "Component Type Name")
    public String getComponentType() {
        return getComponent().getClass().getName();
    }

    @ManagedAttribute(description = "Mina Host")
    public String getHost() {
        return getConfiguration().getHost();
    }

    @ManagedAttribute(description = "Mina Port")
    public int getPort() {
        return getConfiguration().getPort();
    }

    @ManagedAttribute(description = "Mina Character Set")
    public String getCharsetName() {
        return getConfiguration().getCharsetName();
    }

    @ManagedAttribute(description = "Mina Timeout")
    public long getTimeout() {
        return getConfiguration().getTimeout();
    }

    @ManagedAttribute(description = "Mina Filters")
    public String[] getIoFilters() {
        List<IoFilter> filters = getConfiguration().getFilters();
        return toStringArray(filters);
    }

    @ManagedAttribute(description = "SSL Secure Enabled")
    public boolean isSslSecure() {
        return getSslContext() != null;
    }

    /**
     * @return the client authentication type.
     */
    public ClientAuthType getClientAuthType() {
        return config.getClientAuthType();
    }

    @ManagedAttribute(description = "Client Authentication Type")
    public String getClientAuthTypeClass() {
        return getClientAuthType().toString();
    }

    /**
     * @return the customInterceptors as array of bean names.
     */
    @ManagedAttribute(description = "Custom Interceptor Beans")
    public String[] getCustomInterceptorBeans() {
        return config.getCustomInterceptorBeans();
    }

    /**
     * @return the customInterceptorFactories
     */
    public List<Hl7v2InterceptorFactory> getCustomInterceptorFactories() {
        return config.getCustomInterceptorFactories();
    }

    public ConsumerDispatchingInterceptor getDispatcher() {
        return config.getDispatcher();
    }


    /**
     * @return the customInterceptors as array of string names
     */
    @ManagedAttribute(description = "Custom Interceptor Factories")
    public String[] getCustomInterceptorFactoryList() {
        return toStringArray(getCustomInterceptorFactories());
    }

    private String[] toStringArray(List<?> list) {
        final String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getClass().getCanonicalName();
        }
        return result;
    }


    /* ----- dumb delegation, nothing interesting below ----- */
    @SuppressWarnings({ "unchecked", "rawtypes" })
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

    @Override
    public PollingConsumer createPollingConsumer() throws Exception {
        return wrappedEndpoint.createPollingConsumer();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MllpEndpoint) {
            MllpEndpoint<?, ?> that = (MllpEndpoint<?, ?>) object;
            return wrappedEndpoint.equals(that.getWrappedEndpoint());
        }
        return false;
    }

    @Override
    public CamelContext getCamelContext() {
        return wrappedEndpoint.getCamelContext();
    }

    @Override
    public Component getComponent() {
        return wrappedEndpoint.getComponent();
    }

    public Mina2Configuration getConfiguration() {
        return wrappedEndpoint.getConfiguration();
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

    @Override
    public void setCamelContext(CamelContext camelContext) {
        wrappedEndpoint.setCamelContext(camelContext);
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
    public String toString() {
        return wrappedEndpoint.toString();
    }

}
