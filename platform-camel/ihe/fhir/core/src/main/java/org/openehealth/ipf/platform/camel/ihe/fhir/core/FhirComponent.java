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
import org.apache.camel.impl.UriEndpointComponent;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
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
        extends UriEndpointComponent implements AuditableComponent<AuditDatasetType>, InterceptableComponent {

    private FhirComponentConfiguration fhirComponentConfiguration;

    public FhirComponent() {
        super(FhirEndpoint.class);
    }

    public FhirComponent(CamelContext context) {
        super(context, FhirEndpoint.class);
    }

    public FhirComponent(FhirComponentConfiguration configuration) {
        this();
        this.fhirComponentConfiguration = configuration;
    }

    protected FhirEndpointConfiguration<AuditDatasetType> createConfig(String remaining, Map<String, Object> parameters) throws Exception {
        return new FhirEndpointConfiguration<>(this, FhirContext.forDstu2Hl7Org(), remaining, parameters);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // Component configuration determines if lazy loading is allowed or not. Otherwise the endpoint has
        // the choice to do so.
        if (!fhirComponentConfiguration.supportsLazyLoading() &&
                parameters.containsKey(FhirEndpointConfiguration.LAZY_LOAD_BUNDLES) &&
                Boolean.parseBoolean((String)parameters.get(FhirEndpointConfiguration.LAZY_LOAD_BUNDLES))) {
            throw new IllegalArgumentException("The FHIR component " + getClass().getSimpleName() +
                    " is configured to not support lazy-loading of bundles, but the endpoint requested to do so.");
        }
        FhirEndpointConfiguration<AuditDatasetType> config = createConfig(remaining, parameters);
        return doCreateEndpoint(uri, config);
    }

    @Override
    public List<Interceptor> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor> getAdditionalProducerInterceptors() {
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
     * @return static component-specific configuration
     */
    public FhirComponentConfiguration getFhirComponentConfiguration() {
        return fhirComponentConfiguration;
    }

    protected void setFhirComponentConfiguration(FhirComponentConfiguration fhirComponentConfiguration) {
        this.fhirComponentConfiguration = fhirComponentConfiguration;
    }
}
