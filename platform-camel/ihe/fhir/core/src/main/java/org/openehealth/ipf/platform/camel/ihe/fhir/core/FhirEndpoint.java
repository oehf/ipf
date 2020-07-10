/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirProvider;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.ConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer.ProducerAuditInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generic FHIR endpoint
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class FhirEndpoint<AuditDatasetType extends FhirAuditDataset, ComponentType extends FhirComponent<AuditDatasetType>>
        extends DefaultEndpoint
        implements InterceptableEndpoint<FhirEndpointConfiguration<AuditDatasetType>, ComponentType>, AuditableEndpoint<AuditDatasetType> {

    private final FhirEndpointConfiguration<AuditDatasetType> config;
    private final ComponentType fhirComponent;

    public FhirEndpoint(String uri, ComponentType fhirComponent, FhirEndpointConfiguration<AuditDatasetType> config) {
        super(uri, fhirComponent);
        this.fhirComponent = fhirComponent;
        this.config = config;
        this.setExchangePattern(ExchangePattern.InOut);
    }

    @Override
    public ComponentType getInterceptableComponent() {
        return fhirComponent;
    }

    @Override
    public Producer doCreateProducer() {
        return new FhirProducer<AuditDatasetType>(this);
    }

    /**
     * Called when a {@link FhirConsumer} is started. Registers the resource provider
     *
     * @param consumer FhirConsumer
     */
    public void connect(FhirConsumer<AuditDatasetType> consumer) {
        for (FhirProvider provider : getResourceProviders()) {
            // Make consumer known to provider
            provider.setConsumer(consumer);
            fhirComponent.connect(consumer, provider);
        }

    }

    /**
     * Called when a {@link FhirConsumer} is stopped. Unregisters the resource provider
     *
     * @param consumer FhirConsumer
     * @throws Exception if resource provider could not be unregistered
     */
    public void disconnect(FhirConsumer<AuditDatasetType> consumer) throws Exception {
        for (FhirProvider provider : getResourceProviders()) {
            provider.unsetConsumer(consumer);
            fhirComponent.disconnect(consumer, provider);
        }
    }

    public FhirContext getContext() {
        return getInterceptableConfiguration().getContext();
    }

    /**
     * Returns a list of interceptors that are default for FHIR consumers. Subclasses
     * can add additional interceptors that are required for a concrete FHIR endpoint.
     *
     * @return list of default interceptors
     */
    @Override
    public List<Interceptor> createInitialConsumerInterceptorChain() {
        List<Interceptor> initialChain = new ArrayList<>();
        if (isAudit()) {
            initialChain.add(new ConsumerAuditInterceptor<>(getAuditContext()));
        }
        return initialChain;
    }

    /**
     * Returns a list of interceptors that are default for FHIR producers. Subclasses
     * can add additional interceptors that are required for a concrete FHIR endpoint.
     *
     * @return list of default interceptors
     */
    @Override
    public List<Interceptor> createInitialProducerInterceptorChain() {
        List<Interceptor> initialChain = new ArrayList<>();
        if (isAudit()) {
            initialChain.add(new ProducerAuditInterceptor<>(getAuditContext()));
        }
        return initialChain;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public FhirEndpointConfiguration<AuditDatasetType> getInterceptableConfiguration() {
        return config;
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return fhirComponent.getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return fhirComponent.getServerAuditStrategy();
    }

    @Override
    public AuditContext getAuditContext() {
        return getInterceptableConfiguration().getAuditContext();
    }

    @Override
    public Consumer doCreateConsumer(Processor processor) {
        return new FhirConsumer<>(this, processor);
    }

    // Private stuff

    private List<? extends FhirProvider> getResourceProviders() {
        var providers = config.getResourceProvider();
        if (providers == null || providers.isEmpty()) {
            providers = fhirComponent.getFhirTransactionConfiguration().getStaticResourceProvider();
        }
        return providers;
    }

    public ClientRequestFactory<?> getClientRequestFactory() {
        ClientRequestFactory<?> factory = config.getClientRequestFactory();
        if (factory == null) {
            factory = fhirComponent.getFhirTransactionConfiguration().getStaticClientRequestFactory();
        }
        return factory;
    }

    public Predicate<Object> getConsumerSelector() {
        var consumerSelector = config.getConsumerSelector();
        if (consumerSelector == null) {
            consumerSelector = fhirComponent.getFhirTransactionConfiguration().getStaticConsumerSelector();
        }
        return consumerSelector;
    }

}
