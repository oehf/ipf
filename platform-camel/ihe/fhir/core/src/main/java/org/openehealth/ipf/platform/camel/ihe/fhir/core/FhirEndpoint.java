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
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.impl.DefaultEndpoint;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.ConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer.ProducerAuditInterceptor;

import java.util.ArrayList;
import java.util.List;

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
    public Producer doCreateProducer() throws Exception {
        return new FhirProducer<AuditDatasetType>(this);
    }

    /**
     * Called when a {@link FhirConsumer} is started. Registers the resource provider
     *
     * @param consumer FhirConsumer
     * @throws Exception
     */
    public void connect(FhirConsumer<AuditDatasetType> consumer) throws Exception {
        AbstractPlainProvider provider = getResourceProvider();
        // Make consumer known to provider
        provider.setConsumer(consumer);
        fhirComponent.connect(consumer, provider);
    }

    /**
     * Called when a {@link FhirConsumer} is stopped. Unregisters the resource provider
     *
     * @param consumer FhirConsumer
     * @throws Exception
     */
    public void disconnect(FhirConsumer<AuditDatasetType> consumer) throws Exception {
        AbstractPlainProvider provider = getResourceProvider();
        provider.unsetConsumer(consumer);
        fhirComponent.disconnect(consumer, provider);
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
            initialChain.add(new ConsumerAuditInterceptor<>());
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
            initialChain.add(new ProducerAuditInterceptor<>());
        }
        return initialChain;
    }

    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    @Override
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return config.isAudit();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public FhirTransactionConfiguration getFhirComponentConfiguration() {
        return fhirComponent.getFhirComponentConfiguration();
    }

    @Override
    public FhirEndpointConfiguration<AuditDatasetType> getInterceptableConfiguration() {
        return config;
    }

    /**
     * Returns client-side audit strategy instance.
     */
    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return fhirComponent.getClientAuditStrategy();
    }

    /**
     * Returns server-side audit strategy instance.
     */
    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return fhirComponent.getServerAuditStrategy();
    }

    @Override
    public Consumer doCreateConsumer(Processor processor) throws Exception {
        return new FhirConsumer<>(this, processor);
    }

    // Private stuff

    private AbstractPlainProvider getResourceProvider() {
        AbstractPlainProvider provider = config.getResourceProvider();
        if (provider == null) {
            provider = getFhirComponentConfiguration().getStaticResourceProvider();
        }
        return provider;
    }

    public ClientRequestFactory<?> getClientRequestFactory() {
        ClientRequestFactory<?> factory = config.getClientRequestFactory();
        if (factory == null) {
            factory = getFhirComponentConfiguration().getStaticClientRequestFactory();
        }
        return factory;
    }

}
