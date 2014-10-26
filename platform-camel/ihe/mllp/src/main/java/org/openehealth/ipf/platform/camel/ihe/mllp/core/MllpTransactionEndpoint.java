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
import org.apache.camel.component.mina2.Mina2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerRequestAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerResponseAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.*;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerMarshalAndInteractiveResponseReceiverInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerRequestFragmenterInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerStringProcessingInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Camel endpoint for MLLP-based eHealth transactions (like IHE PIX, PDQ, XAD-PID, etc.).
 * @author Dmytro Rud
 */
public class MllpTransactionEndpoint<AuditDatasetType extends MllpAuditDataset>
        extends MllpEndpoint<MllpTransactionEndpointConfiguration, MllpTransactionComponent<AuditDatasetType>>
{

    /**
     * Constructor.
     * @param mllpComponent
     *      MLLP Component instance which is creating this endpoint.
     * @param wrappedEndpoint
     *      The original camel-mina endpoint instance.
     * @param config
     *      Configuration parameters.
     */
    public MllpTransactionEndpoint(
            MllpTransactionComponent<AuditDatasetType> mllpComponent,
            Mina2Endpoint wrappedEndpoint,
            MllpTransactionEndpointConfiguration config)
    {
        super(mllpComponent, wrappedEndpoint, config);
    }


    @Override
    protected List<Hl7v2Interceptor> createInitialConsumerInterceptorChain() {
        List<Hl7v2Interceptor> initialChain = new ArrayList<Hl7v2Interceptor>();
        initialChain.add(new ConsumerStringProcessingInterceptor());
        if (isSupportUnsolicitedFragmentation()) {
            initialChain.add(new ConsumerRequestDefragmenterInterceptor());
        }
        initialChain.add(new ConsumerMarshalInterceptor());
        initialChain.add(new ConsumerRequestAcceptanceInterceptor());
        if (isSupportInteractiveContinuation()) {
            initialChain.add(new ConsumerInteractiveResponseSenderInterceptor());
        }
        if (isAudit()) {
            initialChain.add(new ConsumerAuditInterceptor<AuditDatasetType>());
        }
        initialChain.add(new ConsumerResponseAcceptanceInterceptor());
        initialChain.add(new ConsumerAdaptingInterceptor(getCharsetName()));
        if (isAudit()) {
            initialChain.add(new ConsumerAuthenticationFailureInterceptor());
        }
        return initialChain;
    }


    @Override
    protected List<Hl7v2Interceptor> createInitialProducerInterceptorChain() {
        List<Hl7v2Interceptor> initialChain = new ArrayList<Hl7v2Interceptor>();
        initialChain.add(new ProducerStringProcessingInterceptor());
        if (isSupportUnsolicitedFragmentation()) {
            initialChain.add(new ProducerRequestFragmenterInterceptor());
        }
        initialChain.add(isSupportInteractiveContinuation()
                ? new ProducerMarshalAndInteractiveResponseReceiverInterceptor()
                : new ProducerMarshalInterceptor());
        initialChain.add(new ProducerResponseAcceptanceInterceptor());
        if (isAudit()) {
            initialChain.add(new ProducerAuditInterceptor<AuditDatasetType>());
        }
        initialChain.add(new ProducerRequestAcceptanceInterceptor());
        initialChain.add(new ProducerAdaptingInterceptor());
        return initialChain;
    }


    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return getConfig().isAudit();
    }


    /**
     * Returns client-side audit strategy instance.
     */
    public MllpAuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return getMllpComponent().getClientAuditStrategy();
    }


    /**
     * Returns server-side audit strategy instance.
     */
    public MllpAuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return getMllpComponent().getServerAuditStrategy();
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
