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
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirSecurityInformation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RestfulClientFactory that is aware of SSL context parameters
 */
public class SslAwareApacheRestfulClientFactory extends RestfulClientFactory {

    private HttpClient httpClient;
    private HttpHost proxy;
    private FhirSecurityInformation securityInformation;

    public SslAwareApacheRestfulClientFactory(FhirContext theFhirContext) {
        super(theFhirContext);
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

    @Override
    public void setHttpClient(Object httpClient) {
        this.httpClient = (HttpClient) httpClient;
    }

    @Override
    public void setProxy(String host, Integer port) {
        proxy = host != null ? new HttpHost(host, port, "http") : null;
    }

    public void setSecurityInformation(FhirSecurityInformation securityInformation) {
        this.securityInformation = securityInformation;
    }

    protected synchronized HttpClient getNativeHttpClient() {
        if (httpClient == null) {

            // @formatter:off
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(getConnectTimeout())
                    .setSocketTimeout(getSocketTimeout())
                    .setConnectionRequestTimeout(getConnectionRequestTimeout())
                    .setProxy(proxy)
                    .setStaleConnectionCheckEnabled(true)
                    .build();

            HttpClientBuilder builder = HttpClients.custom()
                    .useSystemProperties()
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .setMaxConnTotal(getPoolMaxTotal())
                    .setMaxConnPerRoute(getPoolMaxPerRoute())
                    .setConnectionTimeToLive(5, TimeUnit.SECONDS)
                    .disableCookieManagement();

            if (securityInformation != null) {
                securityInformation.configureHttpClientBuilder(builder);
            }

            // Need to authenticate against proxy
            if (proxy != null && StringUtils.isNotBlank(getProxyUsername()) && StringUtils.isNotBlank(getProxyPassword())) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(proxy.getHostName(), proxy.getPort()),
                        new UsernamePasswordCredentials(getProxyUsername(), getProxyPassword()));
                builder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
                builder.setDefaultCredentialsProvider(credentialsProvider);
            }

            httpClient = builder.build();
        }

        return httpClient;
    }
}
