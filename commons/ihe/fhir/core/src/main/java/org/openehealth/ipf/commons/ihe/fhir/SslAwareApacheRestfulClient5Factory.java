/*
 * Copyright 2024 the original author or authors.
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
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.Map;

/**
 * RestfulClientFactory based on Apache HttpClient5 that is aware of SSL context parameters. Note that with
 * Spring 6, its RestTemplate also works with HttpClient5, so this is an option to reduce the number of
 * HTTP client libraries.
 *
 * @author Christian Ohr
 */
public class SslAwareApacheRestfulClient5Factory extends SslAwareAbstractRestfulClientFactory<HttpClient5Builder> {

    private CloseableHttpClient httpClient;
    private HttpHost proxy;

    public SslAwareApacheRestfulClient5Factory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    public SslAwareApacheRestfulClient5Factory(FhirContext fhirContext, HttpClient5Builder httpClientBuilder) {
        super(fhirContext, httpClientBuilder);
    }

    @Override
    protected IHttpClient getHttpClient(String serverBase) {
        return new ApacheHttpClient5(getNativeHttpClient(),
            new StringBuilder(serverBase), null, null, null, null);
    }

    @Override
    protected void resetHttpClient() {
        httpClient = null;
    }

    @Override
    public IHttpClient getHttpClient(StringBuilder url,
                                     Map<String, List<String>> ifNoneExistParams,
                                     String ifNoneExistString,
                                     RequestTypeEnum requestType,
                                     List<Header> headers) {
        return new ApacheHttpClient5(getNativeHttpClient(), url, ifNoneExistParams, ifNoneExistString, requestType, headers);
    }

    /**
     * Sets a completely configured HttpClient to be used. No further configuration is done
     * (timeouts, security etc.) before it is used.
     *
     * @param httpClient Http client instance
     */
    @Override
    public void setHttpClient(Object httpClient) {
        this.httpClient = (CloseableHttpClient) httpClient;
    }

    @Override
    public void setProxy(String host, Integer port) {
        this.proxy = host != null ? new HttpHost("http", host, port) : null;
    }

    @Override
    protected HttpClient5Builder newHttpClientBuilder() {
        return HttpClient5Builder.create();
    }

    protected synchronized CloseableHttpClient getNativeHttpClient() {
        if (httpClient == null) {
            var httpClient5Builder = newHttpClientBuilder()
                .configureConnectionManager(builder ->
                    builder.setConnectionConfigResolver(route ->
                            ConnectionConfig.custom()
                                .setConnectTimeout(Timeout.ofMilliseconds(getConnectTimeout()))
                                .setSocketTimeout(Timeout.ofMilliseconds(getSocketTimeout()))
                                .setTimeToLive(Timeout.ofMilliseconds(5000L))
                                .build())
                        .setMaxConnPerRoute(getPoolMaxPerRoute())
                        .setMaxConnTotal(getPoolMaxTotal()))
                .configureClient(builder ->
                    builder
                        .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectionRequestTimeout(Timeout.ofMilliseconds(getConnectionRequestTimeout()))
                            .setResponseTimeout(Timeout.ofMilliseconds(getSocketTimeout()))
                            .build())

                        .disableCookieManagement())
                .configureProxy(proxy, getProxyUsername(), getProxyPassword());

            // Chance to override or instrument
            httpClient = customizeHttpClientBuilder(httpClient5Builder).build();
        }

        return httpClient;
    }

    @Override
    public FhirSecurityInformation<HttpClient5Builder> initializeSecurityInformation(boolean enabled, SSLContext sslContext, HostnameVerifier hostnameVerifier, String userName, String password) {
        var securityInformation = new ApacheFhirSecurityInformation(enabled, sslContext, hostnameVerifier, userName, password);
        setSecurityInformation(securityInformation);
        return securityInformation;
    }

    static class ApacheFhirSecurityInformation extends FhirSecurityInformation<HttpClient5Builder> {

        public ApacheFhirSecurityInformation(boolean secure, SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
            super(secure, sslContext, hostnameVerifier, username, password);
        }

        @Override
        public void configureHttpClientBuilder(HttpClient5Builder builder) {
            if (isSecure()) {
                var connectionSocketFactoryBuilder = SSLConnectionSocketFactoryBuilder.create().useSystemProperties();
                if (getSslContext() != null) {
                    connectionSocketFactoryBuilder.setSslContext(getSslContext());
                }
                if (getHostnameVerifier() != null) {
                    connectionSocketFactoryBuilder.setHostnameVerifier(getHostnameVerifier());
                }
                builder.setSSLSocketFactory(connectionSocketFactoryBuilder.build());
            }
        }

    }
}
