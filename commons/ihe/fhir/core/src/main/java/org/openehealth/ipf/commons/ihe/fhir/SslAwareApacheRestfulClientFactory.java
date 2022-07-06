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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.ApacheHttpClient;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RestfulClientFactory that is aware of SSL context parameters.
 *
 * @author Christian Ohr
 */
public class SslAwareApacheRestfulClientFactory extends SslAwareAbstractRestfulClientFactory<HttpClientBuilder> {

    private HttpClient httpClient;
    private HttpHost proxy;

    public SslAwareApacheRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    public SslAwareApacheRestfulClientFactory(FhirContext fhirContext, HttpClientBuilder httpClientBuilder) {
        super(fhirContext, httpClientBuilder);
    }

    @Override
    protected IHttpClient getHttpClient(String theServerBase) {
        return new ApacheHttpClient(getNativeHttpClient(), new StringBuilder(theServerBase), null, null, null, null);
    }

    @Override
    protected void resetHttpClient() {
        httpClient = null;
    }

    @Override
    public IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> ifNoneExistParams, String ifNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders) {
        return new ApacheHttpClient(getNativeHttpClient(), url, ifNoneExistParams, ifNoneExistString, theRequestType,
                theHeaders);
    }

    /**
     * Sets a completely configured HttpClient to be used. No further configuration is done
     * (timeouts, security etc.) before it is used.
     *
     * @param httpClient Http client instance
     */
    @Override
    public void setHttpClient(Object httpClient) {
        this.httpClient = (HttpClient) httpClient;
    }

    @Override
    public void setProxy(String host, Integer port) {
        proxy = host != null ? new HttpHost(host, port, "http") : null;
    }

    @Override
    protected HttpClientBuilder newHttpClientBuilder() {
        return HttpClients.custom();
    }

    protected synchronized HttpClient getNativeHttpClient() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager connectionManager;
            if (getSecurityInformation() != null && getSecurityInformation().isSecure()) {
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        getSecurityInformation().getSslContext(),
                        getSecurityInformation().getHostnameVerifier());
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf).build();
                connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, null, null, null, 5000L, TimeUnit.MILLISECONDS);
            } else {
                connectionManager = new PoolingHttpClientConnectionManager(5000L, TimeUnit.MILLISECONDS);
            }
            connectionManager.setMaxTotal(getPoolMaxTotal());
            connectionManager.setDefaultMaxPerRoute(getPoolMaxPerRoute());
            var defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(getSocketTimeout())
                    .setConnectTimeout(getConnectTimeout())
                    .setConnectionRequestTimeout(getConnectionRequestTimeout())
                    .setProxy(proxy)
                    .build();
            HttpClientBuilder builder = httpClientBuilder()
                    // .useSystemProperties()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .disableCookieManagement();

            // Need to authenticate against proxy
            if (proxy != null && StringUtils.isNotBlank(getProxyUsername()) && StringUtils.isNotBlank(getProxyPassword())) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(proxy.getHostName(), proxy.getPort()),
                        new UsernamePasswordCredentials(getProxyUsername(), getProxyPassword()));
                builder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
                builder.setDefaultCredentialsProvider(credentialsProvider);
            }

            // Chance to override or instrument
            httpClient = customizeHttpClientBuilder(builder).build();
        }

        return httpClient;
    }

    @Override
    public FhirSecurityInformation<HttpClientBuilder> initializeSecurityInformation(boolean enabled, SSLContext sslContext, HostnameVerifier hostnameVerifier, String userName, String password) {
        var securityInformation = new ApacheFhirSecurityInformation(enabled, sslContext, hostnameVerifier, userName, password);
        setSecurityInformation(securityInformation);
        return securityInformation;
    }

    static class ApacheFhirSecurityInformation extends FhirSecurityInformation<HttpClientBuilder> {

        public ApacheFhirSecurityInformation(boolean secure, SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
            super(secure, sslContext, hostnameVerifier, username, password);
        }

        @Override
        public void configureHttpClientBuilder(HttpClientBuilder builder) {
            if (isSecure()) {
                builder.setSSLContext(getSslContext());
                if (getHostnameVerifier() != null) {
                    builder.setSSLHostnameVerifier(getHostnameVerifier());
                }
            }
        }

    }
}
