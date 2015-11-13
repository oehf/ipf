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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.http.client.HttpClient;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpointConfiguration;

import java.util.Map;

/**
 * Configuration of a FHIR endpoint instance
 *
 * @since 3.1
 */
@UriParams
public class FhirEndpointConfiguration<AuditDatasetType extends FhirAuditDataset> extends InterceptableEndpointConfiguration {

    private final String path;
    private FhirContext context;

    @UriParam(defaultValue = "false")
    private boolean audit = false;

    @UriParam(defaultValue = "FhirServlet")
    private String servletName = CamelFhirServlet.DEFAULT_SERVLET_NAME;

    @UriParam
    private AbstractResourceProvider<AuditDatasetType> resourceProvider;

    @UriParam
    private ClientRequestFactory<? extends IClientExecutable<?, ?>> clientRequestFactory;

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, String path, Map<String, Object> parameters) throws Exception {
        this(component, FhirContext.forDstu2Hl7Org(), path, parameters);
    }

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, FhirContext context, String path, Map<String, Object> parameters) throws Exception {
        super(component, parameters);

        this.path = path;
        this.context = context;
        audit = component.getAndRemoveParameter(parameters, "audit", boolean.class, true);
        servletName = component.getAndRemoveParameter(parameters, "servletName", String.class, CamelFhirServlet.DEFAULT_SERVLET_NAME);
        resourceProvider = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "resourceProvider", AbstractResourceProvider.class, null);
        clientRequestFactory = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "clientRequestFactory", ClientRequestFactory.class, null);

        Integer connectTimeout = component.getAndRemoveParameter(parameters, "connectionTimeout", Integer.class);
        if (connectTimeout != null) {
            setConnectTimeout(connectTimeout);
        }
        Integer timeout = component.getAndRemoveParameter(parameters, "timeout", Integer.class);
        if (timeout != null) {
            setTimeout(timeout);
        }

        boolean secure = component.getAndRemoveParameter(parameters, "secure", Boolean.class, false);
        if (secure) {
            throw new UnsupportedOperationException("secure transport not yet supported");
        }

    }

    public FhirContext getContext() {
        return context;
    }

    public boolean isAudit() {
        return audit;
    }

    public AbstractResourceProvider<AuditDatasetType> getResourceProvider() {
        return resourceProvider;
    }

    public String getServletName() {
        return servletName;
    }

    public String getPath() {
        return path;
    }

    public <T extends IClientExecutable<?, ?>> ClientRequestFactory<T> getClientRequestFactory() {
        return (ClientRequestFactory<T>) clientRequestFactory;
    }

    public void setConnectTimeout(int connectTimeout) {
        context.getRestfulClientFactory().setConnectTimeout(connectTimeout);
    }

    public void setTimeout(int timeout) {
        context.getRestfulClientFactory().setSocketTimeout(timeout);
    }
}
