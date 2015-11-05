/*
 * Copyright 2015 the original author or authors.
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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.impl.DefaultEndpoint;
import org.openehealth.ipf.commons.ihe.core.chain.ChainUtils;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.FhirInterceptor;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer.ConsumerAuditInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic FHIR endpoint
 */
public abstract class FhirEndpoint<T extends FhirAuditDataset> extends DefaultEndpoint {

    private final FhirEndpointConfiguration config;
    private String servletName;
    private final FhirComponent<T> fhirComponent;

    public FhirEndpoint(String uri, FhirComponent<T> fhirComponent, FhirEndpointConfiguration config) {
        super(uri, fhirComponent);
        this.fhirComponent = fhirComponent;
        this.config = config;
    }

    @Override
    public final Consumer createConsumer(Processor originalProcessor) throws Exception {

        // Configure interceptor chain
        List<FhirInterceptor> chain = getConsumerInterceptorChain();
        Processor processor = originalProcessor;
        for (int i = chain.size() - 1; i >= 0; --i) {
            FhirInterceptor interceptor = chain.get(i);
            interceptor.setWrappedProcessor(processor);
            processor = interceptor;
        }
        // Create the component-specific consumer
        return doCreateConsumer(processor);
    }

    /**
     * Create the actual consumer
     *
     * @param processor processor that may have been wrapped with a number of interceptors
     * @return the actual consumer
     */
    protected abstract Consumer doCreateConsumer(Processor processor);

    /**
     * Called when a {@link FhirConsumer} is started. Registers the resource provider
     *
     * @param consumer FhirConsumer
     * @throws Exception
     */
    public void connect(FhirConsumer<T> consumer) throws Exception {
        AbstractResourceProvider<T> provider = getResourceProvider();
        // Make consumer known to provider
        provider.setConsumer(consumer);
        // Register provider with CamelFhirServlet
        CamelFhirServlet.registerProvider(servletName, provider);
    }

    /**
     * Called when a {@link FhirConsumer} is stopped. Unregisters the resource provider
     *
     * @param consumer FhirConsumer
     * @throws Exception
     */
    public void disconnect(FhirConsumer<T> consumer) throws Exception {
        AbstractResourceProvider<T> provider = getResourceProvider();
        CamelFhirServlet.unregisterProvider(servletName, provider);
    }

    /**
     * Returns a list of interceptors that are default for FHIR endpoints. Subclasses
     * can add additional interceptors that are required for a concrete FHIR endpoint.
     *
     * @return list of default interceptors
     */
    protected List<FhirInterceptor> createInitialConsumerInterceptorChain() {
        List<FhirInterceptor> initialChain = new ArrayList<>();
        if (isAudit()) {
            initialChain.add(new ConsumerAuditInterceptor<T>());
        }
        return initialChain;
    }

    /**
     * Returns <tt>true</tt> when ATNA auditing should be performed.
     */
    @ManagedAttribute(description = "Audit Enabled")
    public boolean isAudit() {
        return config.isAudit();
    }


    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public FhirComponentConfiguration<T> getFhirComponentConfiguration() {
        return fhirComponent.getFhirComponentConfiguration();
    }

    /**
     * Returns client-side audit strategy instance.
     */
    public FhirAuditStrategy<T> getClientAuditStrategy() {
        return fhirComponent.getClientAuditStrategy();
    }

    /**
     * Returns server-side audit strategy instance.
     */
    public FhirAuditStrategy<T> getServerAuditStrategy() {
        return fhirComponent.getServerAuditStrategy();
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }


    // Private stuff

    private AbstractResourceProvider<T> getResourceProvider() {
        AbstractResourceProvider<T> provider = config.getResourceProvider();
        if (provider == null) {
            provider = getFhirComponentConfiguration().getStaticResourceProvider();
        }
        return provider;
    }

    private List<FhirInterceptor> getConsumerInterceptorChain() {
        // set up initial interceptor chain
        List<FhirInterceptor> initialChain = createInitialConsumerInterceptorChain();

        List<FhirInterceptor> additionalInterceptors = new ArrayList<>();
        additionalInterceptors.addAll(fhirComponent.getAdditionalConsumerInterceptors());
        // add interceptors provided by the user
        additionalInterceptors.addAll(getCustomInterceptors());

        return ChainUtils.createChain(initialChain, additionalInterceptors);
    }

    /**
     * @return a list of endpoint-specific interceptors
     */
    private synchronized List<FhirInterceptor<?>> getCustomInterceptors() {
        List<FhirInterceptor<?>> result = new ArrayList<>();
        /*
        for (FhirInterceptorFactory customInterceptorFactory : config.getCustomInterceptorFactories()) {
            result.add(customInterceptorFactory.getNewInstance());
        }
        */
        return result;
    }
}
