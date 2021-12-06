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
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.*;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract FHIR Camel component
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class FhirComponent<AuditDatasetType extends FhirAuditDataset>
        extends DefaultComponent implements AuditableComponent<AuditDatasetType>, InterceptableComponent {

    private FhirInteractionId<AuditDatasetType> fhirInteractionId;

    public FhirComponent(FhirInteractionId<AuditDatasetType> fhirInteractionId) {
        this.fhirInteractionId = fhirInteractionId;
    }

    public FhirComponent(CamelContext context, FhirInteractionId<AuditDatasetType> fhirInteractionId) {
        super(context);
        this.fhirInteractionId = fhirInteractionId;
    }

    /**
     * Connects the URL specified on the endpoint to the specified processor.
     *
     * @param consumer         the consumer
     * @param resourceProvider the resource provider
     */
    public void connect(FhirConsumer<AuditDatasetType> consumer, FhirProvider resourceProvider) {
        var name = consumer.getEndpoint().getInterceptableConfiguration().getServletName();
        DefaultFhirRegistry.getFhirRegistry(name).register(resourceProvider);
    }

    /**
     * Disconnects the URL specified on the endpoint from the specified processor.
     *
     * @param consumer the consumer
     * @throws Exception can be thrown
     */
    public void disconnect(FhirConsumer<AuditDatasetType> consumer, FhirProvider resourceProvider) throws Exception {
        var name = consumer.getEndpoint().getInterceptableConfiguration().getServletName();
        DefaultFhirRegistry.getFhirRegistry(name).unregister(resourceProvider);
    }

    /**
     * Initializes a default FHIR context for this component. Only called if the endpoint does
     * not define its own.
     *
     * @return initialized default FHIR context
     */
    public FhirContext initializeFhirContext() {
        return getFhirTransactionConfiguration().initializeFhirContext();
    }

    public boolean isCompatibleContext(FhirContext fhirContext) {
        return getFhirTransactionConfiguration().getFhirVersion() == fhirContext.getVersion().getVersion();
    }

    protected FhirEndpointConfiguration<AuditDatasetType> createConfig(String remaining, Map<String, Object> parameters) throws Exception {
        return new FhirEndpointConfiguration<>(this, remaining, parameters);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        var config = createConfig(remaining, parameters);
        // Component configuration determines if lazy loading is allowed or not. Otherwise the endpoint has
        // the choice to do so.
        if (!fhirInteractionId.getFhirTransactionConfiguration().supportsLazyLoading() &&
                parameters.containsKey(FhirEndpointConfiguration.LAZY_LOAD_BUNDLES) &&
                Boolean.parseBoolean((String) parameters.get(FhirEndpointConfiguration.LAZY_LOAD_BUNDLES))) {
            throw new IllegalArgumentException("The FHIR component " + getClass().getSimpleName() +
                    " is configured to not support lazy-loading of bundles, but the endpoint requested to do so.");
        }
        return doCreateEndpoint(uri, config);
    }

    @Override
    public List<Interceptor<?>> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor<?>> getAdditionalProducerInterceptors() {
        return Collections.emptyList();
    }

    /**
     * Returns a new endpoint instance
     *
     * @param uri    the endpoint URI
     * @param config FhirEndpointConfiguration
     * @return a new endpoint instance
     */
    protected abstract FhirEndpoint<?, ?> doCreateEndpoint(String uri, FhirEndpointConfiguration<AuditDatasetType> config);

    /**
     * @return component-specific configuration
     */
    public FhirTransactionConfiguration<AuditDatasetType> getFhirTransactionConfiguration() {
        return getInteractionId().getFhirTransactionConfiguration();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getServerAuditStrategy() {
        return getFhirTransactionConfiguration().getServerAuditStrategy();
    }

    @Override
    public AuditStrategy<AuditDatasetType> getClientAuditStrategy() {
        return getFhirTransactionConfiguration().getClientAuditStrategy();
    }

    public FhirInteractionId<AuditDatasetType> getInteractionId() {
        return fhirInteractionId;
    }

    /**
     * Sets the FHIR interactionID.
     * @param fhirInteractionId interactionID
     */
    public void setFhirInteractionId(FhirInteractionId<AuditDatasetType> fhirInteractionId) {
        this.fhirInteractionId = fhirInteractionId;
    }
}
