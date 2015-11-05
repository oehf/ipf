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

import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.FhirInterceptorFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Configuration of a FHIR endpoint instance
 */
@UriParams
public class FhirEndpointConfiguration<T extends FhirAuditDataset> implements Serializable {

    @UriParam(defaultValue = "false")
    private boolean audit = false;

    @UriParam
    private AbstractResourceProvider<T> resourceProvider;

    @UriParam
    private List<FhirInterceptorFactory> customInterceptorFactories;

    protected FhirEndpointConfiguration(FhirComponent component, Map<String, Object> parameters) throws Exception {
        audit = component.getAndRemoveParameter(parameters, "audit", boolean.class, true);
        resourceProvider = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "resourceProvider", AbstractResourceProvider.class, null);
        customInterceptorFactories = component.resolveAndRemoveReferenceListParameter(
                parameters, "interceptorFactories", FhirInterceptorFactory.class);
    }

    public boolean isAudit() {
        return audit;
    }

    public AbstractResourceProvider<T> getResourceProvider() {
        return resourceProvider;
    }

    public List<FhirInterceptorFactory> getCustomInterceptorFactories() {
        return customInterceptorFactories;
    }
}
