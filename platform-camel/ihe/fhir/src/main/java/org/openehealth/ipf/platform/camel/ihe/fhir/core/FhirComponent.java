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

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.openehealth.ipf.commons.ihe.fhir.atna.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract FHIR Camel component
 *
 * @since 3.1
 */
public abstract class FhirComponent<AuditDatasetType extends FhirAuditDataset>
        extends UriEndpointComponent implements AuditableComponent<AuditDatasetType> {

    public FhirComponent() {
        super(FhirEndpoint.class);
    }

    public FhirComponent(CamelContext context) {
        super(context, FhirEndpoint.class);
    }

    protected FhirEndpointConfiguration<AuditDatasetType> createConfig(Map<String, Object> parameters) throws Exception {
        return new FhirEndpointConfiguration<>(this, parameters);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        FhirEndpointConfiguration<AuditDatasetType> config = createConfig(parameters);
        return doCreateEndpoint(uri, config);
    }

    /**
     * Returns a list of component-specific (i.e. transaction-specific)
     * FHIR interceptors which will be integrated into the interceptor
     * chain of each consumer instance created by this component.
     * <p/>
     * Per default returns an empty list.
     * <br>
     * When overwriting this method, please note:
     * <ul>
     * <li>Neither the returned list nor any element of it
     * are allowed to be <code>null</code>.
     * <li>Each invocation should return freshly created instances
     * of interceptors (like prototype-scope beans in Spring),
     * because interceptors cannot be reused by multiple endpoints.
     * </ul>
     *
     * @return a list of component-specific (i.e. transaction-specific) FHIR interceptors
     */
    public List<Interceptor> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    /**
     * Returns a new endpoint instance
     *
     * @param uri    the endpoint URI
     * @param config FhirEndpointConfiguration
     * @return a new endpoint instance
     */
    protected abstract FhirEndpoint<AuditDatasetType> doCreateEndpoint(String uri, FhirEndpointConfiguration<AuditDatasetType> config);

    /**
     * @return static component-specific configuration
     */
    public abstract FhirComponentConfiguration<AuditDatasetType> getFhirComponentConfiguration();

}
