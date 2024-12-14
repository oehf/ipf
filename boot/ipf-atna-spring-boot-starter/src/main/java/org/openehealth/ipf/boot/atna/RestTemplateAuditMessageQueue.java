/*
 * Copyright 2023 the original author or authors.
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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * Alternative to {@link org.openehealth.ipf.commons.audit.queue.BasicHttpAuditMessageQueue},
 * using Spring's RestTemplate / RestTemplateBuilder classes
 */
public class RestTemplateAuditMessageQueue extends AbstractAuditMessageQueue {

    private final URI uri;
    private final String user;
    private final String password;
    private final RestTemplateBuilder restTemplateBuilder;

    private RestOperations restTemplate;
    private long connectTimeout = -1;
    private long readTimeout = -1;

    public RestTemplateAuditMessageQueue(RestTemplateBuilder restTemplateBuilder, URI uri) {
        this(restTemplateBuilder, uri, null, null);
    }

    /**
     * @param uri       URL
     * @param user      user name, maybe null
     * @param password  password, maybe null
     */
    public RestTemplateAuditMessageQueue(RestTemplateBuilder restTemplateBuilder, URI uri, String user, String password) {
        this.uri = requireNonNull(uri, "url must not be null");
        this.user = user;
        this.password = password;
        this.restTemplateBuilder = restTemplateBuilder
                .defaultHeader(
                        CONTENT_TYPE,
                        MediaType.TEXT_XML.toString());
        initRestTemplate();
    }

    @Override
    protected void handle(AuditContext auditContext, String auditRecord) {
        try {
            var httpHeaders = new HttpHeaders();
            httpHeaders.add(X_IPF_ATNA_TIMESTAMP, auditContext.getAuditMetadataProvider().getTimestamp());
            httpHeaders.add(X_IPF_ATNA_HOSTNAME, auditContext.getAuditMetadataProvider().getHostname());
            httpHeaders.add(X_IPF_ATNA_PROCESSID, auditContext.getAuditMetadataProvider().getProcessID());
            httpHeaders.add(X_IPF_ATNA_APPLICATION, auditContext.getAuditMetadataProvider().getSendingApplication());
            var entity = new HttpEntity<>(auditRecord, httpHeaders);
            restTemplate.postForEntity(uri, entity, Void.class);
        } catch (RestClientException e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditRecord);
        }
    }

    public void setConnectTimeout(long connectTimeoutMillis) {
        this.connectTimeout = connectTimeoutMillis;
        initRestTemplate();
    }

    public void setReadTimeout(long readTimeoutMillis) {
        this.readTimeout = readTimeoutMillis;
        initRestTemplate();
    }

    private synchronized void initRestTemplate() {
        var builder = this.restTemplateBuilder;
        if (connectTimeout >= 0) {
            builder = builder.connectTimeout(Duration.ofMillis(connectTimeout));
        }
        if (readTimeout >= 0) {
            builder = builder.readTimeout(Duration.ofMillis(readTimeout));
        }
        if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
            builder = builder.basicAuthentication(user, password);
        }
        this.restTemplate = builder.build();
    }
}
