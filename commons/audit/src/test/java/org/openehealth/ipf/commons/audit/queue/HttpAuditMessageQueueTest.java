package org.openehealth.ipf.commons.audit.queue;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
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

import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class HttpAuditMessageQueueTest {

    private int port;
    private ClientAndServer mockServer;
    private HttpClientAuditMessageQueue atnaQueue;
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
    public void testSuccessfulAudit() {
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        atnaQueue = new HttpClientAuditMessageQueue(httpClient, "http://localhost:" + port + "/audit");
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
    }

    @Test
    public void testUnsuccessfulAudit() {
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        atnaQueue = new HttpClientAuditMessageQueue(httpClient, "http://localhost:" + port + "/audit");
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
        assertThat(caught, instanceOf(HttpResponseException.class));
    }

    @Test
    public void testAuditSomewhere() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(500) // milliseconds
                        .build())
                .build();
        atnaQueue = new HttpClientAuditMessageQueue(httpClient, "http://localhost:" + freePort() + "/audit");
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
        assertThat(caught, instanceOf(ConnectTimeoutException.class));
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
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            return -1;
        }
    }
}
