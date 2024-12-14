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
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import com.github.mizosoft.methanol.Methanol;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * RestfulClientFactory that is aware of SSL context parameters and uses the HTTP Client built in JDK 11
 * with some added functionality provided by the lightweight Methanol library.
 *
 * @author Christian Ohr
 * @see <a href="https://mizosoft.github.io/methanol">Methanol library documentation</a>
 */
public class SslAwareMethanolRestfulClientFactory extends SslAwareAbstractRestfulClientFactory<Methanol.Builder> {

    private Methanol httpClient;
    private InetSocketAddress proxy;
    private boolean async;
    private Executor executor;

    public SslAwareMethanolRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
    }

    public SslAwareMethanolRestfulClientFactory(FhirContext fhirContext, Methanol.Builder httpClientBuilder) {
        super(fhirContext, httpClientBuilder);
    }

    @Override
    protected IHttpClient getHttpClient(String theServerBase) {
        return new MethanolHttpClient(getNativeHttpClient(), async,
                new StringBuilder(theServerBase), null, null, null, null);
    }

    @Override
    protected void resetHttpClient() {
        httpClient = null;
    }

    @Override
    public IHttpClient getHttpClient(StringBuilder url, Map<String, List<String>> ifNoneExistParams, String ifNoneExistString, RequestTypeEnum requestType, List<Header> headers) {
        return new MethanolHttpClient(getNativeHttpClient(), async,
                url, ifNoneExistParams, ifNoneExistString, requestType, headers);
    }

    /**
     * Sets a completely configured Http Client to be used. No further configuration is done
     * before it is used.
     *
     * @param httpClient Http client instance
     */
    @Override
    public void setHttpClient(Object httpClient) {
        this.httpClient = (Methanol) httpClient;
    }

    @Override
    public void setProxy(String host, Integer port) {
        proxy = host != null ? new InetSocketAddress(host, port) : null;
    }

    /**
     * Whether to use a thread pool to send the requests
     *
     * @param async true to send the requests asynchronosly, false otherwise
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Define an executor to be used for asynchronous request processing
     *
     * @param executor executor to be used for asynchronous request processing
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    protected Methanol.Builder newHttpClientBuilder() {
        return Methanol.newBuilder();
    }


    protected synchronized Methanol getNativeHttpClient() {
        if (httpClient == null) {
            var builder = httpClientBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .priority(1)
                    .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE))
                    .connectTimeout(Duration.ofMillis(getConnectTimeout()))
                    .readTimeout(Duration.ofMillis(getSocketTimeout()))
                    .autoAcceptEncoding(true)
                    .proxy(proxy != null ? ProxySelector.of(proxy) : ProxySelector.getDefault());
            if (executor != null) {
                builder.executor(executor);
            }
            // Chance to override or instrument
            httpClient = customizeHttpClientBuilder(builder).build();
        }

        return httpClient;
    }

    @Override
    public synchronized void setPoolMaxTotal(int poolMaxTotal) {
        System.setProperty("jdk.httpclient.connectionPoolSize", Integer.toString(poolMaxTotal));
    }

    @Override
    public FhirSecurityInformation<Methanol.Builder> initializeSecurityInformation(boolean enabled, SSLContext sslContext, HostnameVerifier hostnameVerifier, String userName, String password) {
        var securityInformation = new MethanolFhirSecurityInformation(enabled, sslContext, hostnameVerifier, userName, password);
        setSecurityInformation(securityInformation);
        return securityInformation;
    }

    public static class MethanolFhirSecurityInformation extends FhirSecurityInformation<Methanol.Builder> {

        public MethanolFhirSecurityInformation(boolean secure, SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
            super(secure, sslContext, hostnameVerifier, username, password);
        }

        @Override
        public void configureHttpClientBuilder(Methanol.Builder builder) {
            if (isSecure()) {
                if (getSslContext() != null) {
                    builder.sslContext(getSslContext());
                }
            }

        }

    }
}
