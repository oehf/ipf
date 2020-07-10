/*
 * Copyright 2020 the original author or authors.
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.protocol.RecordingAuditMessageTransmission;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class BasicHttpAuditMessageQueueTest {

    private int port;
    private ClientAndServer mockServer;
    private BasicHttpAuditMessageQueue atnaQueue;
    private DefaultAuditContext auditContext;
    private Throwable caught;


    @Before
    public void setup() {
        port = freePort();
        mockServer = startClientAndServer(port);
        auditContext = new DefaultAuditContext();
        auditContext.setAuditTransmissionProtocol(new RecordingAuditMessageTransmission());
        auditContext.setAuditExceptionHandler((auditContext, t, auditMessages) -> caught = t);
        auditContext.setAuditEnabled(true);
    }

    @After
    public void tearDown() {
        caught = null;
        mockServer.stop();
    }

    @Test
    public void testSuccessfulAudit() throws MalformedURLException {
        new MockServerClient("127.0.0.1", port)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/audit"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withDelay(TimeUnit.MILLISECONDS, 10)
                );

        // Setup producer
        atnaQueue = new BasicHttpAuditMessageQueue(new URL("http://localhost:" + port + "/audit"));
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
    }

    @Test
    public void testUnsuccessfulAudit() throws MalformedURLException {
        new MockServerClient("127.0.0.1", port)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/audit"))
                .respond(
                        response()
                                .withStatusCode(404)
                                .withDelay(TimeUnit.MILLISECONDS, 10)
                );

        // Setup producer
        atnaQueue = new BasicHttpAuditMessageQueue(new URL("http://localhost:" + port + "/audit"));
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
        assertThat(caught, instanceOf(IOException.class));
    }

    @Test
    public void testAuditSomewhere() throws MalformedURLException {
        atnaQueue = new BasicHttpAuditMessageQueue(new URL("http://localhost:" + freePort() + "/audit"));
        atnaQueue.setConnectTimeout(500);
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
        assertThat(caught, anyOf(instanceOf(ConnectException.class), instanceOf(SocketTimeoutException.class)));
    }

    private void sendAudit() {
        auditContext.audit(
                new ApplicationActivityBuilder.ApplicationStart(EventOutcomeIndicator.Success)
                        .setAuditSource(auditContext)
                        .setApplicationParticipant(
                                "appName",
                                null,
                                null,
                                AuditUtils.getLocalHostName())
                        .addApplicationStarterParticipant(System.getProperty("user.name"))
                        .getMessages());
    }

    private static int freePort() {
        try (var serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            return -1;
        }
    }
}
