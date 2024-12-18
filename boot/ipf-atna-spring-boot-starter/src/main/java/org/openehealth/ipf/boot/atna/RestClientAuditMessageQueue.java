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
package org.openehealth.ipf.boot.atna;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.queue.AbstractAuditMessageQueue;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.encodeBasicAuth;

/**
 * Alternative to {@link org.openehealth.ipf.commons.audit.queue.BasicHttpAuditMessageQueue},
 * using Spring's RestClient / RestClient.Builder classes
 */
public class RestClientAuditMessageQueue extends AbstractAuditMessageQueue {

    private final RestClient.Builder restClientBuilder;
    private final URI uri;
    private long connectTimeout = -1;
    private long readTimeout = -1;

    private RestClient restClient;

    public RestClientAuditMessageQueue(RestClient.Builder restClientBuilder, URI uri) {
        this(restClientBuilder, uri, null, null);
    }

    /**
     * @param uri       URL
     * @param user      user name, maybe null
     * @param password  password, maybe null
     */
    public RestClientAuditMessageQueue(RestClient.Builder restClientBuilder, URI uri, String user, String password) {
        var builder = restClientBuilder
                .defaultHeader(CONTENT_TYPE, MediaType.TEXT_XML.toString());
        if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
            builder = builder.defaultHeader(
                HttpHeaders.AUTHORIZATION,
                encodeBasicAuth(user, password, null));
        }
        this.uri = uri;
        this.restClientBuilder = customize(builder);
        initRestClient();
    }

    protected RestClient.Builder customize(RestClient.Builder builder) {
        return builder;
    }

    @Override
    protected void handle(AuditContext auditContext, String auditRecord) {
        try {
            var response = restClient.post()
                .uri(uri)
                .headers(httpHeaders -> {
                    httpHeaders.add(X_IPF_ATNA_TIMESTAMP, auditContext.getAuditMetadataProvider().getTimestamp());
                    httpHeaders.add(X_IPF_ATNA_HOSTNAME, auditContext.getAuditMetadataProvider().getHostname());
                    httpHeaders.add(X_IPF_ATNA_PROCESSID, auditContext.getAuditMetadataProvider().getProcessID());
                    httpHeaders.add(X_IPF_ATNA_APPLICATION, auditContext.getAuditMetadataProvider().getSendingApplication());
                })
                .body(auditRecord)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditRecord);
        }
    }

    public void setConnectTimeout(long connectTimeoutMillis) {
        this.connectTimeout = connectTimeoutMillis;
        initRestClient();
    }

    public void setReadTimeout(long readTimeoutMillis) {
        this.readTimeout = readTimeoutMillis;
        initRestClient();
    }

    private synchronized void initRestClient() {
        this.restClient = this.restClientBuilder
            .requestFactory(ClientHttpRequestFactoryBuilder.httpComponents()
                .withDefaultRequestConfigCustomizer(rcc -> {
                    if (connectTimeout > 0) {
                        rcc.setConnectionRequestTimeout(connectTimeout, TimeUnit.MILLISECONDS);
                    }
                    if (readTimeout > 0) {
                        rcc.setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS);
                    }
                })
                .build())
            .build();
    }
}
