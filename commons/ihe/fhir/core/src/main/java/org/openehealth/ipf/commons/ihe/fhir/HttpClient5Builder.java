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

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;

import java.util.function.Consumer;

/**
 * Helper class, combining HttpClientBuilder and PoolingHttpClientConnectionManagerBuilder; also
 * adding some convenience methods
 */
public class HttpClient5Builder {

    private final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    private final PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();

    public static HttpClient5Builder create() {
        return new HttpClient5Builder();
    }

    private HttpClient5Builder() {
    }

    HttpClient5Builder configureConnectionManager(Consumer<PoolingHttpClientConnectionManagerBuilder> consumer) {
        consumer.accept(connectionManagerBuilder);
        return this;
    }

    HttpClient5Builder configureClient(Consumer<HttpClientBuilder> consumer) {
        consumer.accept(httpClientBuilder);
        return this;
    }

    HttpClient5Builder configureProxy(HttpHost proxy, String proxyUserName, String proxyPassword) {
        httpClientBuilder.setProxy(proxy);
        if (proxy != null && StringUtils.isNotBlank(proxyUserName) && StringUtils.isNotBlank(proxyPassword)) {
            var credentialsProvider = CredentialsProviderBuilder.create()
                .add(new AuthScope(proxy), proxyUserName, proxyPassword.toCharArray())
                .build();
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }
        return this;
    }

    public CloseableHttpClient build() {
        return httpClientBuilder
            .setConnectionManager(connectionManagerBuilder.build())
            .build();
    }

    void setSSLSocketFactory(SSLConnectionSocketFactory sslSocketFactory) {
        connectionManagerBuilder.setSSLSocketFactory(sslSocketFactory);
    }

}
