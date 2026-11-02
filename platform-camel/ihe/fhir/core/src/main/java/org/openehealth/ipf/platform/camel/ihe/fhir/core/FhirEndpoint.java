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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.DefaultFhirRegistry;
import org.openehealth.ipf.commons.ihe.fhir.FhirProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirRegistry;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.ConsumerAuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer.ProducerAuditInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Generic FHIR endpoint
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class FhirEndpoint<AuditDatasetType extends FhirAuditDataset>
        extends DefaultEndpoint
        implements InterceptableEndpoint<FhirEndpointConfiguration, FhirComponent<AuditDatasetType>>, AuditableEndpoint<AuditDatasetType> {

    private final FhirEndpointConfiguration config;
    private final FhirComponent<AuditDatasetType> fhirComponent;
    // Remembers which provider instances a consumer has been connected to, so that exactly those are disconnected again
    private final Map<FhirConsumer<AuditDatasetType>, List<FhirProvider>> connectedProviders = new ConcurrentHashMap<>();
    private volatile FhirRegistry fhirRegistry;

    public FhirEndpoint(String uri, FhirComponent<AuditDatasetType> fhirComponent, FhirEndpointConfiguration config) {
        super(uri, fhirComponent);
        this.fhirComponent = fhirComponent;
        this.config = config;
        this.setExchangePattern(ExchangePattern.InOut);
    }

    @Override
    public FhirComponent<AuditDatasetType> getInterceptableComponent() {
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
        var fhirRegistry = getFhirRegistry();
        var connected = new ArrayList<FhirProvider>();
        try {
            for (var provider : getResourceProviders()) {
                provider.connect(consumer, fhirRegistry);
                connected.add(provider);
            }
        } catch (RuntimeException e) {
            disconnect(consumer, connected);
            throw e;
        }
        connectedProviders.put(consumer, connected);
    }

    /**
     * Called when a {@link FhirConsumer} is stopped. Unregisters the resource provider
     *
     * @param consumer FhirConsumer
     */
    public void disconnect(FhirConsumer<AuditDatasetType> consumer) {
        var connected = connectedProviders.remove(consumer);
        if (connected != null) {
            disconnect(consumer, connected);
        }
    }

    private void disconnect(FhirConsumer<AuditDatasetType> consumer, List<FhirProvider> providers) {
        var fhirRegistry = getFhirRegistry();
        for (var provider : providers) {
            provider.disconnect(consumer, fhirRegistry);
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
        var initialChain = new ArrayList<Interceptor>();
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
        var initialChain = new ArrayList<Interceptor>();
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
    public FhirEndpointConfiguration getInterceptableConfiguration() {
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

    /**
     * Returns the resource providers explicitly configured on the endpoint. If there are none, the
     * transaction's default resource providers are used, whereby there is one instance per provider class
     * and {@link FhirRegistry}.
     */
    private List<? extends FhirProvider> getResourceProviders() {
        var providers = config.getResourceProvider();
        if (providers == null || providers.isEmpty()) {
            var fhirRegistry = getFhirRegistry();
            providers = fhirComponent.getFhirTransactionConfiguration().createResourceProviders().stream()
                    .map(fhirRegistry::resolveProvider)
                    .toList();
        }
        return providers;
    }

    /**
     * Returns the {@link FhirRegistry} that connects consumers of this endpoint with the FHIR servlet, which is
     * <ol>
     *     <li>the registry configured on the endpoint, if any</li>
     *     <li>the registry of the {@link IpfFhirServlet} bean with the configured servlet name, if any</li>
     *     <li>the registry shared by servlet name otherwise, see {@link DefaultFhirRegistry#getFhirRegistry(String)}</li>
     * </ol>
     *
     * @return the {@link FhirRegistry} that connects consumers of this endpoint with the FHIR servlet
     * @since 6.1
     */
    public FhirRegistry getFhirRegistry() {
        var registry = fhirRegistry;
        if (registry == null) {
            registry = resolveFhirRegistry();
            fhirRegistry = registry;
        }
        return registry;
    }

    private FhirRegistry resolveFhirRegistry() {
        if (config.getFhirRegistry() != null) {
            return config.getFhirRegistry();
        }
        var servletName = config.getServletName();
        return getCamelContext().getRegistry().findByType(IpfFhirServlet.class).stream()
                .filter(servlet -> servletName.equals(servlet.getServletName()))
                .map(IpfFhirServlet::getFhirRegistry)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> DefaultFhirRegistry.getFhirRegistry(servletName));
    }

    public ClientRequestFactory getClientRequestFactory() {
        ClientRequestFactory factory = config.getClientRequestFactory();
        if (factory == null) {
            factory = fhirComponent.getFhirTransactionConfiguration().getStaticClientRequestFactory();
        }
        return factory;
    }

    public Predicate<RequestDetails> getConsumerSelector() {
        var consumerSelector = config.getConsumerSelector();
        if (consumerSelector == null) {
            consumerSelector = fhirComponent.getFhirTransactionConfiguration().getStaticConsumerSelector();
        }
        return consumerSelector;
    }

}
