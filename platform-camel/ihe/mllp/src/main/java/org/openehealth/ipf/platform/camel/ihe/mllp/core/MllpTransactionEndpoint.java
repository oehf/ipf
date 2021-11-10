/*
 * Copyright 2014 the original author or authors.
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

import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.component.netty.NettyEndpoint;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.InteractiveContinuationStorage;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.UnsolicitedFragmentationStorage;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerRequestInteractionSetterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerAuthenticationFailureInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerInteractiveResponseSenderInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerRequestDefragmenterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerStringProcessingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerMarshalAndInteractiveResponseReceiverInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerRequestFragmenterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerStringProcessingInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Camel endpoint for MLLP-based eHealth transactions (like IHE PIX, PDQ, XAD-PID, etc.).
 *
 * @author Dmytro Rud
 */
public class MllpTransactionEndpoint<AuditDatasetType extends MllpAuditDataset>
        extends MllpEndpoint<MllpTransactionEndpointConfiguration, AuditDatasetType, MllpTransactionComponent<AuditDatasetType>>
        implements AuditableEndpoint<AuditDatasetType> {

    /**
     * Constructor.
     *
     * @param mllpComponent   MLLP Component instance which is creating this endpoint.
     * @param wrappedEndpoint The original camel-netty endpoint instance.
     * @param config          Configuration parameters.
     */
    public MllpTransactionEndpoint(
            MllpTransactionComponent<AuditDatasetType> mllpComponent,
            NettyEndpoint wrappedEndpoint,
            MllpTransactionEndpointConfiguration config) {
        super(mllpComponent, wrappedEndpoint, config);
    }


    @Override
    public List<Interceptor> createInitialConsumerInterceptorChain() {
        var initialChain = new ArrayList<Interceptor>();
        initialChain.add(new ConsumerRequestInteractionSetterInterceptor());
        initialChain.add(new ConsumerStringProcessingInterceptor());
        if (isSupportUnsolicitedFragmentation()) {
            initialChain.add(new ConsumerRequestDefragmenterInterceptor());
        }
        initialChain.add(new ConsumerMarshalInterceptor(getConfig().isCopyOriginalMessage()));
        initialChain.add(new ConsumerRequestAcceptanceInterceptor());
        if (isSupportInteractiveContinuation()) {
            initialChain.add(new ConsumerInteractiveResponseSenderInterceptor());
        }
        if (isAudit()) {
            initialChain.add(new ConsumerAuditInterceptor<AuditDatasetType>(getAuditContext()));
        }
        initialChain.add(new ConsumerResponseAcceptanceInterceptor());
        initialChain.add(new ConsumerAdaptingInterceptor(getCharsetName()));
        if (isAudit()) {
            initialChain.add(new ConsumerAuthenticationFailureInterceptor(getAuditContext()));
        }
        return initialChain;
    }


    @Override
    public List<Interceptor> createInitialProducerInterceptorChain() {
        var initialChain = new ArrayList<Interceptor>();
        initialChain.add(new ProducerStringProcessingInterceptor());
        if (isSupportUnsolicitedFragmentation()) {
            initialChain.add(new ProducerRequestFragmenterInterceptor());
        }
        initialChain.add(isSupportInteractiveContinuation()
                ? new ProducerMarshalAndInteractiveResponseReceiverInterceptor(getCharsetName())
                : new ProducerMarshalInterceptor(getCharsetName()));
        initialChain.add(new ProducerResponseAcceptanceInterceptor());
        if (isAudit()) {
            initialChain.add(new ProducerAuditInterceptor<AuditDatasetType>(getAuditContext()));
        }
        initialChain.add(new ProducerRequestAcceptanceInterceptor());
        initialChain.add(new ProducerAdaptingInterceptor());
        return initialChain;
    }

    @Override
    public AuditContext getAuditContext() {
        return getConfig().getAuditContext();
    }

    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    @Override
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return getAuditContext().isAuditEnabled();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return getMllpComponent().getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return getMllpComponent().getServerAuditStrategy();
    }

    /**
     * Returns <code>true</code> if this endpoint supports unsolicited message fragmentation.
     */
    @ManagedAttribute(description = "Support Unsolicited Fragmentation Enabled")
    public boolean isSupportUnsolicitedFragmentation() {
        return getConfig().isSupportUnsolicitedFragmentation();
    }

    /**
     * Returns threshold for unsolicited message fragmentation
     * (relevant on producer side only).
     */
    @ManagedAttribute(description = "Unsolicited Fragmentation Threshold")
    public int getUnsolicitedFragmentationThreshold() {
        return getConfig().getUnsolicitedFragmentationThreshold();
    }

    /**
     * Returns the unsolicited fragmentation storage bean.
     */
    public UnsolicitedFragmentationStorage getUnsolicitedFragmentationStorage() {
        return getConfig().getUnsolicitedFragmentationStorage();
    }

    @ManagedAttribute(description = "Unsolicited Fragmentation Storage Cache Type")
    public String getUnsolicitedFragmentationStorageType() {
        return isSupportUnsolicitedFragmentation() ?
                getUnsolicitedFragmentationStorage().getClass().getName() : "";
    }

    /**
     * Returns <code>true</code> if this endpoint supports interactive continuation.
     */
    @ManagedAttribute(description = "Support Interactive Continuation Enabled")
    public boolean isSupportInteractiveContinuation() {
        return getConfig().isSupportInteractiveContinuation();
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
        return getConfig().getInteractiveContinuationDefaultThreshold();
    }

    /**
     * Returns the interactive continuation storage bean.
     */
    public InteractiveContinuationStorage getInteractiveContinuationStorage() {
        return getConfig().getInteractiveContinuationStorage();
    }

    /**
     * Returns true, when the producer should automatically send a cancel
     * message after it has collected all interactive continuation pieces.
     */
    @ManagedAttribute(description = "Auto Cancel Enabled")
    public boolean isAutoCancel() {
        return getConfig().isAutoCancel();
    }

    @ManagedAttribute(description = "Interactive Continuation Storage Cache Type")
    public String getInteractiveContinuationStorageType() {
        return isSupportInteractiveContinuation() ?
                getInteractiveContinuationStorage().getClass().getName() : "";
    }

}
