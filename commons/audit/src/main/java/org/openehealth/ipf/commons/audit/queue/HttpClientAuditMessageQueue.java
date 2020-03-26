/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.queue;

import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * Message Queue that sends off audit messages using a HTTP connection. It is recommended
 * that the connection factory implements a pool or caches connection objects for performance reasons.
 *
 * This implementation is based on {@link org.apache.http.client.HttpClient}; other HTTP Client
 * implementations will work similarly.
 *
 * @author Christian Ohr
 * @since 3.7
 */
public class HttpClientAuditMessageQueue extends AbstractAuditMessageQueue {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientAuditMessageQueue.class);

    private final CloseableHttpClient httpClient;
    private final String url;
    private final String user;
    private final String password;

    public HttpClientAuditMessageQueue(CloseableHttpClient httpClient, String url) {
        this(httpClient, url, null, null);
    }

    /**
     * @param httpClient Apache HttpClient
     * @param url        URL
     * @param user       user name, maybe null
     * @param password,  may be null
     */
    public HttpClientAuditMessageQueue(CloseableHttpClient httpClient, String url, String user, String password) {
        this.httpClient = requireNonNull(httpClient, "httpClient must not be null");
        this.url = requireNonNull(url, "url must not be null");
        this.user = user;
        this.password = password;
    }


    @Override
    protected void handle(AuditContext auditContext, String... auditRecords) {
        try {
            for (String auditMessage : auditRecords) {
                HttpPost post = new HttpPost(url);
                post.setEntity(new StringEntity(auditMessage, ContentType.create("text/xml", StandardCharsets.UTF_8)));
                if (user != null && !user.isEmpty()) {
                    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
                    post.addHeader(new BasicScheme().authenticate(credentials, post, null));
                }
                try (CloseableHttpResponse response = httpClient.execute(post)) {
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() >= 400) {
                        throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    }
                } catch (IOException e) {
                    auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditMessage);
                }
            }
        } catch (AuthenticationException e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditRecords);
        }
    }

}
