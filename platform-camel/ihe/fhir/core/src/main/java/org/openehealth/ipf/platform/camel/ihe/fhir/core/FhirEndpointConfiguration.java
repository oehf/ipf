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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.support.EndpointHelper;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.FhirContextProvider;
import org.openehealth.ipf.commons.ihe.fhir.FhirProvider;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareAbstractRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareApacheRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.AmbiguousBeanException;

import javax.net.ssl.HostnameVerifier;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration of a FHIR endpoint instance
 *
 * @author Christian Ohr
 * @since 3.1
 */
@Slf4j
@UriParams
public class FhirEndpointConfiguration<AuditDatasetType extends FhirAuditDataset> extends AuditableEndpointConfiguration {

    static final String STRICT = "strict";
    static final String LENIENT = "lenient";
    static final String LAZY_LOAD_BUNDLES = "lazyLoadBundles";
    static final String CACHE_BUNDLES = "cacheBundles";
    static final String HTTP_CLIENT_TYPE = "httpClient";
    static final String CONSUMER_SELECTOR = "consumerSelector";
    static final String SORT = "sort";

    @Getter
    private final String path;

    @Getter
    private final FhirContext context;

    @Getter
    @UriParam(defaultValue = IpfFhirServlet.DEFAULT_SERVLET_NAME)
    private final String servletName;

    @Getter
    @UriParam
    private final List<? extends FhirProvider> resourceProvider;

    // Producer only

    @UriParam
    private final ClientRequestFactory<? extends IClientExecutable<?, ?>> clientRequestFactory;


    @Getter
    @UriParam
    private final List<HapiClientInterceptorFactory> hapiClientInterceptorFactories;

    @Getter
    @UriParam
    private final List<HapiServerInterceptorFactory> hapiServerInterceptorFactories;

    /**
     * If this is true, all paging requests are routed into the route (see {@link org.openehealth.ipf.commons.ihe.fhir.LazyBundleProvider}
     * fo details. Otherwise, all results are fetched once and cached in order to serve subsequent paging requests.
     */
    @Getter
    @UriParam
    private final boolean lazyLoadBundles;

    /**
     * If this is true, sorting is executed in the BundleProvider if _sort parameters are provided. The sorting logic is expected to
     * be implemented in {@link org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters} implementations. If this is false,
     * the Camel route is expected to return sorted results if the query has requested this.
     */
    @Getter
    @UriParam
    private final boolean sort;

    @Getter
    private final FhirSecurityInformation<?> securityInformation;

    @Getter
    private final Predicate<RequestDetails> consumerSelector;

    /**
     * Only considered if {@link #lazyLoadBundles} is true. The (partial) results of paging requests are cached so that subsequent
     * requests only fetch resources that have not yet been requested.
     */
    @Getter
    @UriParam
    private final boolean cacheBundles;

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, String path, Map<String, Object> parameters) throws Exception {
        super(component, parameters);

        this.path = path;
        this.servletName = component.getAndRemoveParameter(parameters, "servletName", String.class, IpfFhirServlet.DEFAULT_SERVLET_NAME);
        this.resourceProvider = getAndRemoveOrResolveReferenceParameters(component,
                parameters, "resourceProvider", FhirProvider.class);
        this.clientRequestFactory = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "clientRequestFactory", ClientRequestFactory.class);
        this.hapiClientInterceptorFactories = component.resolveAndRemoveReferenceListParameter(
                parameters, "hapiClientInterceptorFactories", HapiClientInterceptorFactory.class);
        this.hapiServerInterceptorFactories = component.resolveAndRemoveReferenceListParameter(
                parameters, "hapiServerInterceptorFactories", HapiServerInterceptorFactory.class);
        this.lazyLoadBundles = component.getAndRemoveParameter(
                parameters, LAZY_LOAD_BUNDLES, Boolean.class, false);
        this.cacheBundles = component.getAndRemoveParameter(
                parameters, CACHE_BUNDLES, Boolean.class, true);
        this.sort = component.getAndRemoveParameter(
                parameters, SORT, Boolean.class, false);
        this.consumerSelector = component.getAndRemoveOrResolveReferenceParameter(
                parameters, CONSUMER_SELECTOR, Predicate.class);
        boolean secure = component.getAndRemoveParameter(parameters, "secure", Boolean.class, false);


        FhirContext fhirContext = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "fhirContext", FhirContext.class);

        // If a (shared) FhirContext parameter is set on the endpoint, no further producer-related parameters
        // are evaluated as it would change the FhirContext instance
        if (fhirContext != null) {
            if (!component.isCompatibleContext(fhirContext)) {
                throw new IllegalArgumentException("FhirContext with version [" + fhirContext.getVersion().getVersion() +
                        "] is not compatible with component of class [" + component.getClass() + "]");
            }
            this.context = fhirContext;
            this.securityInformation = new SslAwareApacheRestfulClientFactory(fhirContext)
                    .initializeSecurityInformation(secure, null, null, null, null);
            if (!parameters.isEmpty()) {
                log.info("Disregarding URI parameters {} for endpoint {}, because static fhirContext is provided.", parameters, path);
                parameters.clear();
            }
            return;
        }

        // Create a new FhirContext either by a custom FhirContextProvider
        // or by component itself according to its transaction configuration
        FhirContextProvider fhirContextProvider = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "fhirContextProvider", FhirContextProvider.class);
        this.context = fhirContextProvider != null ?
                fhirContextProvider.apply(component.getFhirTransactionConfiguration().getFhirVersion()) :
                component.initializeDefaultFhirContext();

        // Now further customize the FhirContext by evaluating other parameters
        var parserErrorHandling = component.getAndRemoveParameter(parameters, "validation", String.class, LENIENT);
        if (STRICT.equals(parserErrorHandling)) {
            this.context.setParserErrorHandler(new StrictErrorHandler());
        } else if (!LENIENT.equals(parserErrorHandling)) {
            throw new IllegalArgumentException("Validation must be either " + LENIENT + " (default) or " + STRICT);
        }

        SslAwareAbstractRestfulClientFactory<?> restfulClientFactory;
        var httpClientType = component.getAndRemoveParameter(parameters, HTTP_CLIENT_TYPE, String.class);
        if ("methanol".equalsIgnoreCase(httpClientType)) {
            restfulClientFactory = new SslAwareMethanolRestfulClientFactory(this.context);
            this.context.setRestfulClientFactory(restfulClientFactory);
        } else if ("apache".equalsIgnoreCase(httpClientType)) {
            restfulClientFactory = new SslAwareApacheRestfulClientFactory(this.context);
            this.context.setRestfulClientFactory(restfulClientFactory);
        } else {
            restfulClientFactory = getRestfulClientFactory();
        }

        var connectTimeout = component.getAndRemoveParameter(parameters, "connectionTimeout", Integer.class);
        if (connectTimeout != null) {
            setConnectTimeout(connectTimeout);
        }
        var connectRequestTimeout = component.getAndRemoveParameter(parameters, "connectionRequestTimeout", Integer.class);
        if (connectRequestTimeout != null) {
            setConnectRequestTimeout(connectRequestTimeout);
        }
        var socketTimeout = component.getAndRemoveParameter(parameters, "timeout", Integer.class);
        if (socketTimeout != null) {
            setTimeout(socketTimeout);
        }
        var poolMax = component.getAndRemoveParameter(parameters, "poolMax", Integer.class);
        if (poolMax != null) {
            setPoolSize(poolMax);
        }
        var disableServerValidation = component.getAndRemoveParameter(parameters, "disableServerValidation", Boolean.class);
        if (disableServerValidation != null) {
            setDisableServerValidation(disableServerValidation);
        }

        // Security stuff

        var sslContextParameters = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "sslContextParameters", SSLContextParameters.class);
        var hostnameVerifier = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hostnameVerifier", HostnameVerifier.class);

        var username = component.getAndRemoveParameter(parameters, "username", String.class);
        var password = component.getAndRemoveParameter(parameters, "password", String.class);

        if (secure && sslContextParameters == null) {
            var sslContextParameterMap = component.getCamelContext().getRegistry().findByTypeWithName(SSLContextParameters.class);
            if (sslContextParameterMap.size() == 1) {
                var entry = sslContextParameterMap.entrySet().iterator().next();
                sslContextParameters = entry.getValue();
            } else if (sslContextParameterMap.size() > 1) {
                throw new AmbiguousBeanException(SSLContextParameters.class);
            }
        }

        this.securityInformation = restfulClientFactory.initializeSecurityInformation(
                secure,
                sslContextParameters != null ?
                        sslContextParameters.createSSLContext(component.getCamelContext()) :
                        null,
                hostnameVerifier,
                username,
                password);
    }

    public <T> List<T> getAndRemoveOrResolveReferenceParameters(FhirComponent<AuditDatasetType> component, Map<String, Object> parameters, String key, Class<T> type) {
        var values = component.getAndRemoveParameter(parameters, key, String.class);
        if (values == null) {
            return null;
        } else {
            return Stream.of(values.split(","))
                    .map(value ->
                            EndpointHelper.isReferenceParameter(value) ?
                                    EndpointHelper.resolveReferenceParameter(component.getCamelContext(), value, type) :
                                    component.getCamelContext().getTypeConverter().convertTo(type, value))
                    .collect(Collectors.toList());
        }
    }

    public SslAwareAbstractRestfulClientFactory<?> getRestfulClientFactory() {
        return (SslAwareAbstractRestfulClientFactory<?>) this.context.getRestfulClientFactory();
    }

    public <T extends IClientExecutable<?, ?>> ClientRequestFactory<T> getClientRequestFactory() {
        return (ClientRequestFactory<T>) clientRequestFactory;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.context.getRestfulClientFactory().setConnectTimeout(connectTimeout);
    }

    public void setConnectRequestTimeout(int connectTimeout) {
        this.context.getRestfulClientFactory().setConnectionRequestTimeout(connectTimeout);
    }

    public void setTimeout(int timeout) {
        this.context.getRestfulClientFactory().setSocketTimeout(timeout);
    }

    public void setPoolSize(int poolSize) {
        this.context.getRestfulClientFactory().setPoolMaxPerRoute(poolSize);
        this.context.getRestfulClientFactory().setPoolMaxPerRoute(poolSize);
    }

    /**
     * By default, the client will query the server before the very first operation to download the server's conformance/metadata
     * statement and verify that the server is appropriate for the given client.
     * This check is only done once per server endpoint for a given FhirContext and is useful to prevent bugs or unexpected behaviour
     * when talking to servers.
     * <p>
     * It may introduce unneccesary overhead however in circumstances where the client and server are known to be compatible.
     * Setting this to true disables this check.
     */
    public void setDisableServerValidation(boolean disableServerValidation) {
        this.context.getRestfulClientFactory().setServerValidationMode(disableServerValidation ?
                ServerValidationModeEnum.NEVER :
                ServerValidationModeEnum.ONCE);
    }
}
