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
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import lombok.Getter;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpointConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Configuration of a FHIR endpoint instance
 *
 * @author Christian Ohr
 * @since 3.1
 */
@UriParams
public class FhirEndpointConfiguration<AuditDatasetType extends FhirAuditDataset> extends InterceptableEndpointConfiguration {

    private static final String STRICT = "strict";
    private static final String LENIENT = "lenient";

    @Getter
    private final String path;

    @Getter
    private FhirContext context;

    // Consumer only

    @Getter
    @UriParam(defaultValue = "false")
    private boolean audit = false;

    @Getter
    @UriParam(defaultValue = "FhirServlet")
    private String servletName = IpfFhirServlet.DEFAULT_SERVLET_NAME;

    @Getter
    @UriParam
    private AbstractPlainProvider resourceProvider;

    // Producer only

    @UriParam
    private ClientRequestFactory<? extends IClientExecutable<?, ?>> clientRequestFactory;

    @Getter
    @UriParam
    private String authUserName;

    @Getter
    @UriParam
    private String authPassword;

    @Getter
    @UriParam
    private List<HapiClientInterceptorFactory> hapiClientInterceptorFactories;

    @Getter
    @UriParam
    private List<HapiServerInterceptorFactory> hapiServerInterceptorFactories;

    /**
     * If this is true, all paging requests are routed into the route (see {@link org.openehealth.ipf.commons.ihe.fhir.LazyBundleProvider}
     * fo details. Otherwise, all results are fetched once and cached in order to serve subsequent paging requests.
     */
    @Getter
    @UriParam
    private boolean lazyLoadBundles;

    /**
     * Only considered if {@link #lazyLoadBundles} is true. The (partial) results of paging requests are cached so that subsequent
     * requests only fetch resources that have not yet been requested.
     */
    @Getter
    @UriParam
    private boolean cacheBundles;

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, String path, Map<String, Object> parameters) throws Exception {
        this(component, FhirContext.forDstu2Hl7Org(), path, parameters);
    }

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, FhirContext context, String path, Map<String, Object> parameters) throws Exception {
        super(component, parameters);
        this.path = path;
        this.context = context;
        audit = component.getAndRemoveParameter(parameters, "audit", boolean.class, true);
        servletName = component.getAndRemoveParameter(parameters, "servletName", String.class, IpfFhirServlet.DEFAULT_SERVLET_NAME);
        resourceProvider = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "resourceProvider", AbstractPlainProvider.class, null);
        clientRequestFactory = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "clientRequestFactory", ClientRequestFactory.class, null);
        hapiClientInterceptorFactories = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hapiClientInterceptorFactories", List.class);
        // TODO: make use of use hapiServerInterceptorFactories
        hapiServerInterceptorFactories = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hapiServerInterceptorFactories", List.class);


        String parserErrorHandling = component.getAndRemoveParameter(parameters, "validation", String.class, "lenient");
        if (STRICT.equals(parserErrorHandling)) {
            context.setParserErrorHandler(new StrictErrorHandler());
        } else if (!LENIENT.equals(parserErrorHandling)) {
            throw new IllegalArgumentException("Validation must be either " + LENIENT + " (default) or " + STRICT);
        }
        boolean secure = component.getAndRemoveParameter(parameters, "secure", Boolean.class, false);
        if (secure) {
            throw new UnsupportedOperationException("secure transport not yet supported");
        }
        HttpClientBuilder clientBuilder = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "httpClientBuilder", HttpClientBuilder.class);
        if (clientBuilder != null) {
            setHttpClientBuilder(clientBuilder);
        }
        Integer connectTimeout = component.getAndRemoveParameter(parameters, "connectionTimeout", Integer.class);
        if (connectTimeout != null) {
            setConnectTimeout(connectTimeout);
        }
        Integer timeout = component.getAndRemoveParameter(parameters, "timeout", Integer.class);
        if (timeout != null) {
            setTimeout(timeout);
        }
        Boolean lazyLoadBundles = component.getAndRemoveParameter(parameters, "lazyLoadBundles", Boolean.class);
        if (lazyLoadBundles != null) {
            this.lazyLoadBundles = lazyLoadBundles;
        }
        Boolean cacheBundles = component.getAndRemoveParameter(parameters, "cacheBundles", Boolean.class);
        if (cacheBundles != null) {
            this.cacheBundles = cacheBundles;
        }

        authUserName = component.getAndRemoveParameter(
                parameters, "authUserName", String.class, null);
        authPassword = component.getAndRemoveParameter(
                parameters, "authPassword", String.class, null);

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

    public void setHttpClientBuilder(HttpClientBuilder builder) {
        context.getRestfulClientFactory().setHttpClient(builder.build());
    }
}
