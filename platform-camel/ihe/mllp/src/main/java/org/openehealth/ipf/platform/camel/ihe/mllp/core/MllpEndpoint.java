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

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

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
import org.apache.camel.component.mina.MinaConfiguration;
import org.apache.camel.component.mina.MinaEndpoint;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.lang3.Validate;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoSession;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerInputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerOutputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerInputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerOutputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.CustomInterceptorWrapper;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpCustomInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerAuthenticationFailureInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerInteractiveResponseSenderInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerRequestDefragmenterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerStringProcessingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerMarshalAndInteractiveResponseReceiverInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerRequestFragmenterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerStringProcessingInterceptor;

/**
 * A wrapper for standard camel-mina endpoint 
 * which provides support for IHE PIX/PDQ-related extensions.
 * @author Dmytro Rud
 */
@ManagedResource(description = "Managed IPF MLLP ITI Endpoint")
public class MllpEndpoint extends DefaultEndpoint implements Hl7v2ConfigurationHolder {

    private final MllpComponent mllpComponent;
    private final MinaEndpoint wrappedEndpoint;
    private final boolean audit;
    private final boolean allowIncompleteAudit;

    private final SSLContext sslContext;
    private final List<MllpCustomInterceptor> customInterceptors;
    private final ClientAuthType clientAuthType;
    private final String[] sslProtocols;
    private final String[] sslCiphers;
    
    private final boolean supportInteractiveContinuation;
    private final boolean supportUnsolicitedFragmentation;
    private final boolean supportSegmentFragmentation;
    private final int interactiveContinuationDefaultThreshold;
    private final int unsolicitedFragmentationThreshold;
    private final int segmentFragmentationThreshold;
    private final InteractiveContinuationStorage interactiveContinuationStorage;
    private final UnsolicitedFragmentationStorage unsolicitedFragmentationStorage;
    private final boolean autoCancel;

    /**
     * Constructor.
     * @param mllpComponent
     *      MLLP Component instance which is creating this endpoint.
     * @param wrappedEndpoint
     *      The original camel-mina endpoint instance.
     * @param audit
     *      Whether ATNA auditing should be performed.
     * @param allowIncompleteAudit
     *      Whether incomplete ATNA auditing are allowed as well.
     * @param sslContext
     *      the SSL context to use; {@code null} if secure communication is not used.
     * @param clientAuthType
     *      type of desired client authentication (NONE/WANT/MUST).
     * @param customInterceptors
     *      the interceptors defined in the endpoint URI.
     * @param sslProtocols
     *      the protocols defined in the endpoint URI or {@code null} if none were specified.
     * @param sslCiphers
     *      the ciphers defined in the endpoint URI or {@code null} if none were specified.
     * @param supportInteractiveContinuation
     *      {@code true} when this endpoint should support interactive message continuation.
     * @param supportUnsolicitedFragmentation
     *      {@code true} when this endpoint should support segment fragmentation.
     * @param supportSegmentFragmentation
     *      {@code true} when this endpoint should support segment fragmentation.
     * @param interactiveContinuationDefaultThreshold
     *      default consumer-side threshold for interactive response continuation. 
     * @param unsolicitedFragmentationThreshold
     *      producer-side threshold for unsolicited message fragmentation. 
     * @param segmentFragmentationThreshold
     *      threshold for segment fragmentation.
     * @param interactiveContinuationStorage
     *      consumer-side storage for interactive message continuation.
     * @param unsolicitedFragmentationStorage
     *      consumer-side storage for unsolicited message fragmentation.
     * @param autoCancel
     *      whether the producer should automatically send a cancel message
     *      after it has collected all interactive continuation pieces.
     */
    public MllpEndpoint(
            MllpComponent mllpComponent,
            MinaEndpoint wrappedEndpoint, 
            boolean audit, 
            boolean allowIncompleteAudit, 
            SSLContext sslContext,
            ClientAuthType clientAuthType,
            List<MllpCustomInterceptor> customInterceptors, 
            String[] sslProtocols, 
            String[] sslCiphers,
            boolean supportInteractiveContinuation,
            boolean supportUnsolicitedFragmentation,
            boolean supportSegmentFragmentation,
            int interactiveContinuationDefaultThreshold,
            int unsolicitedFragmentationThreshold,
            int segmentFragmentationThreshold,
            InteractiveContinuationStorage interactiveContinuationStorage,
            UnsolicitedFragmentationStorage unsolicitedFragmentationStorage,
            boolean autoCancel)
    {
        Validate.notNull(mllpComponent);
        Validate.notNull(wrappedEndpoint);
        Validate.noNullElements(customInterceptors);
        Validate.notNull(clientAuthType);

        this.mllpComponent = mllpComponent;
        this.wrappedEndpoint = wrappedEndpoint;
        this.audit = audit;
        this.allowIncompleteAudit = allowIncompleteAudit;
        this.sslContext = sslContext;
        this.clientAuthType = clientAuthType;
        this.customInterceptors = customInterceptors;
        this.sslProtocols = sslProtocols;
        this.sslCiphers = sslCiphers;
        
        this.supportInteractiveContinuation = supportInteractiveContinuation;
        this.supportUnsolicitedFragmentation = supportUnsolicitedFragmentation;
        this.supportSegmentFragmentation = supportSegmentFragmentation;
        this.interactiveContinuationDefaultThreshold = interactiveContinuationDefaultThreshold;
        this.unsolicitedFragmentationThreshold = unsolicitedFragmentationThreshold;
        this.segmentFragmentationThreshold = segmentFragmentationThreshold;
        this.interactiveContinuationStorage = interactiveContinuationStorage;
        this.unsolicitedFragmentationStorage = unsolicitedFragmentationStorage;
        this.autoCancel = autoCancel;
    }


    /**
     * Wraps the original starting point of the consumer route 
     * into a set of PIX/PDQ-specific interceptors.
     * @param processor
     *      The original consumer processor.  
     */
    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        final String charsetName = getConfiguration().getCharsetName();

        if (sslContext != null) {
            DefaultIoFilterChainBuilder filterChain = wrappedEndpoint.getAcceptorConfig().getFilterChain();
            if (!filterChain.contains("ssl")) {
                HandshakeCallbackSSLFilter filter = new HandshakeCallbackSSLFilter(sslContext);
                filter.setNeedClientAuth(clientAuthType == ClientAuthType.MUST);
                filter.setWantClientAuth(clientAuthType == ClientAuthType.WANT);
                filter.setHandshakeExceptionCallback(new HandshakeFailureCallback());
                filter.setEnabledProtocols(sslProtocols);
                filter.setEnabledCipherSuites(sslCiphers);
                filterChain.addFirst("ssl", filter);
            }
        }

        Processor x = processor;
        for (MllpCustomInterceptor interceptor : customInterceptors) {
            x = new CustomInterceptorWrapper(interceptor, this, x);
        }
        if (isAudit()) {
            x = new ConsumerAuthenticationFailureInterceptor(this, x);
        }
        x = new ConsumerAdaptingInterceptor(this, x, charsetName);
        x = new ConsumerOutputAcceptanceInterceptor(this, x);
        if (isAudit()) {
            x = new ConsumerAuditInterceptor(this, x);
        }
        if (isSupportInteractiveContinuation()) {
            x = new ConsumerInteractiveResponseSenderInterceptor(this, x);
        }
        x = new ConsumerInputAcceptanceInterceptor(this, x);
        x = new ConsumerMarshalInterceptor(this, x);
        if (isSupportUnsolicitedFragmentation()) {
            x = new ConsumerRequestDefragmenterInterceptor(this, x);
        }
        x = new ConsumerStringProcessingInterceptor(this, x);
        return wrappedEndpoint.createConsumer(x);
    }


    /**
     * Wraps the original camel-mina producer  
     * into a set of PIX/PDQ-specific ones.
     */
    @Override
    public Producer createProducer() throws Exception {
        final String charsetName = getConfiguration().getCharsetName();

        if (sslContext != null) {
            DefaultIoFilterChainBuilder filterChain = wrappedEndpoint.getConnectorConfig().getFilterChain();
            if (!filterChain.contains("ssl")) {
                HandshakeCallbackSSLFilter filter = new HandshakeCallbackSSLFilter(sslContext);
                filter.setUseClientMode(true);
                filter.setHandshakeExceptionCallback(new HandshakeFailureCallback());
                filter.setEnabledProtocols(sslProtocols);
                filter.setEnabledCipherSuites(sslCiphers);
                filterChain.addFirst("ssl", filter);
            }
        }

        Producer x = wrappedEndpoint.createProducer();
        x = new ProducerStringProcessingInterceptor(this, x);
        if (isSupportUnsolicitedFragmentation()) {
            x = new ProducerRequestFragmenterInterceptor(this, x);
        }
        x = isSupportInteractiveContinuation() 
                ? new ProducerMarshalAndInteractiveResponseReceiverInterceptor(this, x)
                : new ProducerMarshalInterceptor(this, x);
        x = new ProducerOutputAcceptanceInterceptor(this, x);
        if (isAudit()) {
            x = new ProducerAuditInterceptor(this, x);
        }
        x = new ProducerInputAcceptanceInterceptor(this, x);
        x = new ProducerAdaptingInterceptor(this, x, charsetName);
        return x;
    }


    private class HandshakeFailureCallback implements HandshakeCallbackSSLFilter.Callback {
        @Override
        public void run(IoSession session) {
            if (isAudit()) {
                String hostAddress = session.getRemoteAddress().toString();
                getServerAuditStrategy().auditAuthenticationNodeFailure(hostAddress);
            }
        }
    }

    // ----- getters -----
    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() { 
        return audit;
    }
    
    /**
     * Returns <tt>true</tt> when incomplete ATNA auditing records are allowed as well.
     */
    @ManagedAttribute(description = "Incomplete Audit Allowed")
    public boolean isAllowIncompleteAudit() { 
        return allowIncompleteAudit;
    }
    
    /**
     * Returns client-side audit strategy instance.
     */
    public MllpAuditStrategy getClientAuditStrategy() {
        return mllpComponent.getClientAuditStrategy();
    }

    /**
     * Returns server-side audit strategy instance.
     */
    public MllpAuditStrategy getServerAuditStrategy() {
        return mllpComponent.getServerAuditStrategy();
    }

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
     * Returns <code>true</code> if this endpoint supports interactive continuation.
     */
    @ManagedAttribute(description = "Support Interactive Continuation Enabled")
    public boolean isSupportInteractiveContinuation() {
        return supportInteractiveContinuation;
    }

    /**
     * Returns <code>true</code> if this endpoint supports unsolicited message fragmentation.
     */
    @ManagedAttribute(description = "Support Unsolicited Fragmentation Enabled")
    public boolean isSupportUnsolicitedFragmentation() {
        return supportUnsolicitedFragmentation;
    }

    /**
     * Returns <code>true</code> if this endpoint supports segment fragmentation.
     */
    @ManagedAttribute(description = "Support Segment Fragmentation Enabled")
    public boolean isSupportSegmentFragmentation() {
        return supportSegmentFragmentation;
    }

    /**
     * Returns default threshold for interactive continuation 
     * (relevant on consumer side only).
     * <p>
     * This value will be used when interactive continuation is generally supported 
     * by this endpoint and is particularly applicable for the current response message,   
     * and the corresponding request message does not set the records count threshold 
     * explicitly (RCP-2-1==integer, RCP-2-2=='RD').
     */
    @ManagedAttribute(description = "Interactive Continuation Default Threshold")
    public int getInteractiveContinuationDefaultThreshold() {
        return interactiveContinuationDefaultThreshold;
    }

    /**
     * Returns threshold for unsolicited message fragmentation 
     * (relevant on producer side only).
     */
    @ManagedAttribute(description = "Unsolicited Fragmentation Threshold")
    public int getUnsolicitedFragmentationThreshold() {
        return unsolicitedFragmentationThreshold;
    }

    /**
     * Returns threshold for segment fragmentation. 
     */
    @ManagedAttribute(description = "Segment Fragmentation Threshold")
    public int getSegmentFragmentationThreshold() {
        return segmentFragmentationThreshold;
    }

    /**
     * Returns the interactive continuation storage bean. 
     */
    public InteractiveContinuationStorage getInteractiveContinuationStorage() {
        return interactiveContinuationStorage;
    }

    /**
     * Returns the unsolicited fragmentation storage bean. 
     */
    public UnsolicitedFragmentationStorage getUnsolicitedFragmentationStorage() {
        return unsolicitedFragmentationStorage;
    }

    /**
     * Returns true, when the producer should automatically send a cancel
     * message after it has collected all interactive continuation pieces.
     */
    @ManagedAttribute(description = "Auto Cancel Enabled")
    public boolean isAutoCancel() {
        return autoCancel;
    }

    /**
     * @return the sslContext
     */
    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * @return the sslProtocols
     */
    @ManagedAttribute(description = "Defined SSL Protocols")
    public String[] getSslProtocols() {
        return sslProtocols;
    }

    /**
     * @return the sslCiphers
     */
    @ManagedAttribute(description = "Defined SSL Ciphers")
    public String[] getSslCiphers() {
        return sslCiphers;
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

    @ManagedAttribute(description = "Mina Encoding")
    public String getEncoding() {
        return getConfiguration().getEncoding();
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

    @ManagedAttribute(description = "Interactive Continuation Storage Cache Type")
    public String getInteractiveContinuationStorageType() {
        return isSupportInteractiveContinuation() ?
            getInteractiveContinuationStorage().getClass().getName() : "";
    }

    @ManagedAttribute(description = "Unsolicited Fragmentation Storage Cache Type")
    public String getUnsolicitedFragmentationStorageType() {
        return isSupportUnsolicitedFragmentation() ?
            getUnsolicitedFragmentationStorage().getClass().getName() : "";
    }

    @ManagedAttribute(description = "SSL Secure Enabled")
    public boolean isSslSecure() {
        return getSslContext() != null;
    }

    /**
     * @return the client authentication type.
     */
    public ClientAuthType getClientAuthType() {
        return clientAuthType;
    }

    @ManagedAttribute(description = "Client Authentication Type")
    public String getClientAuthTypeClass() {
        return getClientAuthType().toString();
    }

    /**
     * @return the customInterceptors
     */
    public List<MllpCustomInterceptor> getCustomInterceptors() {
        return customInterceptors;
    }

    /**
     * @return the customInterceptors as array of string names
     */
    @ManagedAttribute(description = "Custom Interceptors")
    public String[] getCustomInterceptorsList() {
        List<MllpCustomInterceptor> interceptors = getCustomInterceptors();
        return toStringArray(interceptors);
    }

    private String[] toStringArray(List<?> list) {
        final String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getClass().getCanonicalName();
        }
        return result;
    }

    /**
     * @return
     *      the wrapped MINA endpoint.
     */
    public MinaEndpoint getWrappedEndpoint() {
        return wrappedEndpoint;
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
            MllpEndpoint that = (MllpEndpoint) object;
            return this.getWrappedEndpoint().equals(that.getWrappedEndpoint());
        }
        else
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

    public MinaConfiguration getConfiguration() {
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
    public Class<Exchange> getExchangeType() {
        return wrappedEndpoint.getExchangeType();
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
