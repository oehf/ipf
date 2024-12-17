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

import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditContext;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * Message Queue that sends off audit messages using a HTTP connection. It is recommended
 * that the connection factory implements a pool or caches connection objects for performance reasons.
 * <p>
 * This is primarily meant to send audit messages to a HTTP-based relay that eventually sends the
 * audit record to an audit repository. Therefore, RFC 5425 metadata is placed in X-IPF-ATNA-*
 * HTTP headers, so the receiver is able to restore them.
 *
 * @author Christian Ohr
 * @since 3.7
 */
public class BasicHttpAuditMessageQueue extends AbstractAuditMessageQueue {

    private final URL url;
    private final String user;
    private final String password;

    @Setter
    private int connectTimeout = -1;
    @Setter
    private int readTimeout = -1;


    public BasicHttpAuditMessageQueue(URL url) {
        this(url, null, null);
    }

    /**
     * @param url       URL
     * @param user      user name, maybe null
     * @param password  password, maybe null
     */
    public BasicHttpAuditMessageQueue(URL url, String user, String password) {
        this.url = requireNonNull(url, "url must not be null");
        this.user = user;
        this.password = password;
    }

    @Override
    protected void handle(AuditContext auditContext, String auditMessage) {
        try {
            var connection = openConnection();
            var buffer = auditMessage.getBytes(StandardCharsets.UTF_8);
            initializeConnection(connection, auditContext, buffer.length);

            // Send request
            try (var wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(buffer);
                wr.flush();
            }

            var response = connection.getResponseCode();
            var responseMessage = connection.getResponseMessage();
            if (response >= 400) {
                throw new IOException("Encountered Status " + response + " with message " + responseMessage);
            }

        } catch (IOException e) {
            auditContext.getAuditExceptionHandler().handleException(auditContext, e, auditMessage);
        }
    }

    private void initializeConnection(HttpURLConnection connection, AuditContext auditContext, int length) throws ProtocolException {
        connection.setFixedLengthStreamingMode(length);
        connection.addRequestProperty("Content-Length", Long.toString(length));
        connection.addRequestProperty(X_IPF_ATNA_TIMESTAMP, auditContext.getAuditMetadataProvider().getTimestamp());
        connection.addRequestProperty(X_IPF_ATNA_HOSTNAME, auditContext.getAuditMetadataProvider().getHostname());
        connection.addRequestProperty(X_IPF_ATNA_PROCESSID, auditContext.getAuditMetadataProvider().getProcessID());
        connection.addRequestProperty(X_IPF_ATNA_APPLICATION, auditContext.getAuditMetadataProvider().getSendingApplication());
        connection.addRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);
    }

    private HttpURLConnection openConnection() throws IOException {
        var connection = (HttpURLConnection) url.openConnection();
        if (user != null) {
            var auth = user + ":" + password;
            var authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", authHeaderValue);
        }
        if (connectTimeout >= 0) {
            connection.setConnectTimeout(connectTimeout);
        }
        if (readTimeout >= 0) {
            connection.setConnectTimeout(readTimeout);
        }
        return connection;
    }

}
