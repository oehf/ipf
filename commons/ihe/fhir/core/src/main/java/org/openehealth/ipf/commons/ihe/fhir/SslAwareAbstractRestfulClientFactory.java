/*
 * Copyright 2021 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * Apache HttpClient RestfulClientFactory that is aware of SSL context parameters.
 *
 * @author Christian Ohr
 */
public abstract class SslAwareAbstractRestfulClientFactory<T> extends RestfulClientFactory {

    private FhirSecurityInformation<T> securityInformation;
    private final T httpClientBuilder;

    public SslAwareAbstractRestfulClientFactory(FhirContext theFhirContext) {
        this(theFhirContext, null);
    }

    public SslAwareAbstractRestfulClientFactory(FhirContext fhirContext, T httpClientBuilder) {
        super(fhirContext);
        this.httpClientBuilder = httpClientBuilder;
    }

    public void setSecurityInformation(FhirSecurityInformation<T> securityInformation) {
        this.securityInformation = securityInformation;
    }

    /**
     * Possibility to customize the httpClientBuilder. The default implementation of this method
     * sets up the SSL config according to the {@link #securityInformation}
     *
     * @param httpClientBuilder HttpClientBuilder
     */
    protected T customizeHttpClientBuilder(T httpClientBuilder) {
        if (securityInformation != null) {
            securityInformation.configureHttpClientBuilder(httpClientBuilder);
        }
        return httpClientBuilder;
    }

    public FhirSecurityInformation<T> getSecurityInformation() {
        return securityInformation;
    }

    public abstract FhirSecurityInformation<T> initializeSecurityInformation(boolean enabled, SSLContext sslContext, HostnameVerifier hostnameVerifier, String userName, String password);

    /**
     * Possibility to instantiate a subclassed builder for building Http Clients. This can be useful
     * if e.g. created Http Clients need to be instrumented or specially configured.
     *
     * The default implementation uses (if present) {@link #httpClientBuilder} or else calls {@link #newHttpClientBuilder()}.
     *
     * @return HttpClientBuilder instance
     */
    protected T httpClientBuilder() {
        return httpClientBuilder != null ? httpClientBuilder : newHttpClientBuilder();
    }

    protected abstract T newHttpClientBuilder();

}
