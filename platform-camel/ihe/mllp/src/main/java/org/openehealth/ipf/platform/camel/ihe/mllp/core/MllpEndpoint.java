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
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.component.netty.NettyConfiguration;
import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.NettyEndpoint;
import org.apache.camel.component.netty.NettyProducer;
import org.apache.camel.support.DefaultEndpoint;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A wrapper for standard camel-netty endpoint
 * which provides support for IHE PIX/PDQ-related extensions.
 *
 * @author Dmytro Rud
 */
@ManagedResource(description = "Managed IPF MLLP ITI Endpoint")
public abstract class MllpEndpoint<
        ConfigType extends MllpEndpointConfiguration,
        AuditDatasetType extends MllpAuditDataset,
        ComponentType extends MllpComponent<ConfigType, AuditDatasetType>>
        extends DefaultEndpoint
        implements InterceptableEndpoint<ConfigType, ComponentType>, HL7v2Endpoint<AuditDatasetType> {

    @Getter(AccessLevel.PROTECTED)
    private final ConfigType config;
    @Getter(AccessLevel.PROTECTED)
    private final ComponentType mllpComponent;
    @Getter(AccessLevel.PROTECTED)
    private final NettyEndpoint wrappedEndpoint;

    /**
     * Constructor.
     *
     * @param mllpComponent   MLLP Component instance which is creating this endpoint.
     * @param wrappedEndpoint The original camel-netty endpoint instance.
     * @param config          Configuration parameters.
     */
    public MllpEndpoint(
            ComponentType mllpComponent,
            NettyEndpoint wrappedEndpoint,
            ConfigType config) {
        super(wrappedEndpoint.getEndpointUri(), mllpComponent);
        this.mllpComponent = requireNonNull(mllpComponent);
        this.wrappedEndpoint = requireNonNull(wrappedEndpoint);
        this.config = requireNonNull(config);
    }

    @Override
    public ComponentType getInterceptableComponent() {
        return mllpComponent;
    }

    @Override
    public ConfigType getInterceptableConfiguration() {
        return config;
    }

    /**
     * Returns the original camel-netty producer which will be wrapped
     * into a set of PIX/PDQ-specific interceptors in {@link #createProducer()}.
     */
    @Override
    public Producer doCreateProducer() throws Exception {
        var producer = (NettyProducer) wrappedEndpoint.createProducer();
        return producer;
    }

    /**
     * Returns the original starting point of the camel-mina route which will be wrapped
     * into a set of PIX/PDQ-specific interceptors in {@link #createConsumer(Processor)}.
     *
     * @param processor The original consumer processor.
     */
    @Override
    public Consumer doCreateConsumer(Processor processor) throws Exception {
        var consumer = (NettyConsumer) wrappedEndpoint.createConsumer(processor);
        return new MllpConsumer(consumer);
    }

/*
    private class HandshakeFailureCallback implements HandshakeCallbackSSLFilter.Callback {

        @Override
        public void run(IoSession session, String message) {
            if (config.isAudit()) {
                var hostAddress = session.getRemoteAddress().toString();
                var auditMessage = MllpAuditUtils.auditAuthenticationNodeFailure(
                        config.getAuditContext(), message, hostAddress);
                config.getAuditContext().audit(auditMessage);
            }
        }
    }

 */

// ----- getters -----

    /**
     * Returns transaction configuration.
     */
    @Override
    public Hl7v2TransactionConfiguration<AuditDatasetType> getHl7v2TransactionConfiguration() {
        return mllpComponent.getHl7v2TransactionConfiguration();
    }

    /**
     * Returns transaction-specific ACK and NAK factory.
     */
    @Override
    public NakFactory<AuditDatasetType> getNakFactory() {
        return mllpComponent.getNakFactory();
    }

    @Override
    public Hl7v2InteractionId<AuditDatasetType> getInteractionId() {
        return mllpComponent.getInteractionId();
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

    @ManagedAttribute(description = "Component Type Name")
    public String getComponentType() {
        return getComponent().getClass().getName();
    }

    @ManagedAttribute(description = "Netty Host")
    public String getHost() {
        return getConfiguration().getHost();
    }

    @ManagedAttribute(description = "Netty Port")
    public int getPort() {
        return getConfiguration().getPort();
    }

    @ManagedAttribute(description = "Netty Character Set")
    public String getCharsetName() {
        return getConfiguration().getCharsetName();
    }

    @ManagedAttribute(description = "Netty Request Timeout")
    public long getRequestTimeout() {
        return getConfiguration().getRequestTimeout();
    }

    @ManagedAttribute(description = "Netty Request Timeout")
    public long getTimeout() {
        return getConfiguration().getRequestTimeout();
    }

    /**
     * @return the customInterceptorFactories
     */
    public List<InterceptorFactory> getCustomInterceptorFactories() {
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
        final var result = new String[list.size()];
        for (var i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getClass().getCanonicalName();
        }
        return result;
    }


    /* ----- dumb delegation, nothing interesting below ----- */
    @SuppressWarnings({"unchecked"})
    @Override
    public void configureProperties(Map options) {
        wrappedEndpoint.configureProperties(options);
    }

    @Override
    public Exchange createExchange() {
        return wrappedEndpoint.createExchange();
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
            var that = (MllpEndpoint<?, ?, ?>) object;
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

    public NettyConfiguration getConfiguration() {
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
