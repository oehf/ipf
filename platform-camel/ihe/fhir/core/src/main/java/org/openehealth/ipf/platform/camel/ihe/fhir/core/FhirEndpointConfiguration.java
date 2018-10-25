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
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import lombok.Getter;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.util.EndpointHelper;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.AmbiguousBeanException;

import javax.net.ssl.HostnameVerifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration of a FHIR endpoint instance
 *
 * @author Christian Ohr
 * @since 3.1
 */
@UriParams
public class FhirEndpointConfiguration<AuditDatasetType extends FhirAuditDataset> extends AuditableEndpointConfiguration {

    static final String STRICT = "strict";
    static final String LENIENT = "lenient";
    static final String LAZY_LOAD_BUNDLES = "lazyLoadBundles";
    static final String CACHE_BUNDLES = "cacheBundles";

    @Getter
    private final String path;

    @Getter
    private FhirContext context;

    @Getter
    @UriParam(defaultValue = "FhirServlet")
    private String servletName = IpfFhirServlet.DEFAULT_SERVLET_NAME;

    @Getter
    @UriParam
    private List<? extends AbstractPlainProvider> resourceProvider;

    // Producer only

    @UriParam
    private ClientRequestFactory<? extends IClientExecutable<?, ?>> clientRequestFactory;


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

    @Getter
    private FhirSecurityInformation securityInformation;


    /**
     * Only considered if {@link #lazyLoadBundles} is true. The (partial) results of paging requests are cached so that subsequent
     * requests only fetch resources that have not yet been requested.
     */
    @Getter
    @UriParam
    private boolean cacheBundles = true;

    protected FhirEndpointConfiguration(FhirComponent<AuditDatasetType> component, String path, Map<String, Object> parameters) throws Exception {
        super(component, parameters);
        this.path = path;


        servletName = component.getAndRemoveParameter(parameters, "servletName", String.class, IpfFhirServlet.DEFAULT_SERVLET_NAME);
        resourceProvider = getAndRemoveOrResolveReferenceParameters(component,
                parameters, "resourceProvider", AbstractPlainProvider.class);
        clientRequestFactory = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "clientRequestFactory", ClientRequestFactory.class);
        hapiClientInterceptorFactories = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hapiClientInterceptorFactories", List.class);
        // TODO: make use of use hapiServerInterceptorFactories
        hapiServerInterceptorFactories = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hapiServerInterceptorFactories", List.class);

        context = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "fhirContext", FhirContext.class);

        if (context == null) {
            context = component.initializeFhirContext();
            String parserErrorHandling = component.getAndRemoveParameter(parameters, "validation", String.class, "lenient");
            if (STRICT.equals(parserErrorHandling)) {
                context.setParserErrorHandler(new StrictErrorHandler());
            } else if (!LENIENT.equals(parserErrorHandling)) {
                throw new IllegalArgumentException("Validation must be either " + LENIENT + " (default) or " + STRICT);
            }

            Integer connectTimeout = component.getAndRemoveParameter(parameters, "connectionTimeout", Integer.class);
            if (connectTimeout != null) {
                setConnectTimeout(connectTimeout);
            }
            Integer timeout = component.getAndRemoveParameter(parameters, "timeout", Integer.class);
            if (timeout != null) {
                setTimeout(timeout);
            }
            setDisableServerValidation(
                    component.getAndRemoveParameter(parameters, "disableServerValidation", Boolean.class, false));
        } else {
            if (!component.isCompatibleContext(context)) {
                throw new IllegalArgumentException("FhirContext with version [" + context.getVersion().getVersion() +
                        "] is not compatible with component of class [" + component.getClass() + "]");
            }
        }

        lazyLoadBundles = component.getAndRemoveParameter(parameters, LAZY_LOAD_BUNDLES, Boolean.class, false);
        cacheBundles = component.getAndRemoveParameter(parameters, CACHE_BUNDLES, Boolean.class, true);

        // Security stuff
        SSLContextParameters sslContextParameters = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "sslContextParameters", SSLContextParameters.class);
        HostnameVerifier hostnameVerifier = component.getAndRemoveOrResolveReferenceParameter(
                parameters, "hostnameVerifier", HostnameVerifier.class);
        boolean secure = component.getAndRemoveParameter(parameters, "secure", Boolean.class, false);
        String username = component.getAndRemoveParameter(parameters, "username", String.class);
        String password = component.getAndRemoveParameter(parameters, "password", String.class);

        if (sslContextParameters == null) {
            Map<String, SSLContextParameters> sslContextParameterMap = component.getCamelContext().getRegistry().findByTypeWithName(SSLContextParameters.class);
            if (sslContextParameterMap.size() == 1) {
                Map.Entry<String, SSLContextParameters> entry = sslContextParameterMap.entrySet().iterator().next();
                sslContextParameters = entry.getValue();
            } else if (sslContextParameterMap.size() > 1) {
                throw new AmbiguousBeanException(SSLContextParameters.class);
            }
        }
        this.securityInformation = new FhirSecurityInformation(
                secure,
                sslContextParameters != null ?
                        sslContextParameters.createSSLContext(component.getCamelContext()) :
                        null,
                hostnameVerifier,
                username,
                password);
    }

    public <T> List<T> getAndRemoveOrResolveReferenceParameters(FhirComponent<AuditDatasetType> component, Map<String, Object> parameters, String key, Class<T> type) {
        String values = component.getAndRemoveParameter(parameters, key, String.class);
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

    public <T extends IClientExecutable<?, ?>> ClientRequestFactory<T> getClientRequestFactory() {
        return (ClientRequestFactory<T>) clientRequestFactory;
    }

    public void setConnectTimeout(int connectTimeout) {
        context.getRestfulClientFactory().setConnectTimeout(connectTimeout);
    }

    public void setTimeout(int timeout) {
        context.getRestfulClientFactory().setSocketTimeout(timeout);
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
        context.getRestfulClientFactory().setServerValidationMode(disableServerValidation ?
                ServerValidationModeEnum.NEVER :
                ServerValidationModeEnum.ONCE);
    }
}
