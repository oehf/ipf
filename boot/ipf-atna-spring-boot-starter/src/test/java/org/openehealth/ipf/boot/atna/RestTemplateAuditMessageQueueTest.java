package org.openehealth.ipf.boot.atna;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.protocol.RecordingAuditMessageTransmission;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;

import java.net.ServerSocket;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class RestTemplateAuditMessageQueueTest {


    private int port;
    private ClientAndServer mockServer;
    private RestTemplateAuditMessageQueue atnaQueue;
    private DefaultAuditContext auditContext;
    private Throwable caught;


    @BeforeEach
    public void setup() {
        port = freePort();
        mockServer = startClientAndServer(port);
        auditContext = new DefaultAuditContext();
        auditContext.setAuditTransmissionProtocol(new RecordingAuditMessageTransmission());
        auditContext.setAuditExceptionHandler((auditContext, t, auditMessages) -> caught = t);
        auditContext.setAuditEnabled(true);
    }

    @AfterEach
    public void tearDown() {
        caught = null;
        if (mockServer != null) mockServer.stop();
    }

    @Test
    public void testSuccessfulAudit() {
        try (var server = new MockServerClient("127.0.0.1", port)) {
            server
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
            atnaQueue = new RestTemplateAuditMessageQueue(new RestTemplateBuilder(), URI.create("http://localhost:" + port + "/audit"));
            auditContext.setAuditMessageQueue(atnaQueue);
            sendAudit();
        }
    }

    @Test
    public void testUnsuccessfulAudit() {
        try (var server = new MockServerClient("127.0.0.1", port)) {
            server
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
            atnaQueue = new RestTemplateAuditMessageQueue(new RestTemplateBuilder(), URI.create("http://localhost:" + port + "/audit"));
            auditContext.setAuditMessageQueue(atnaQueue);
            sendAudit();
            assertThat(caught, instanceOf(RestClientException.class));
        }
    }

    @Test
    public void testAuditSomewhere() {
        atnaQueue = new RestTemplateAuditMessageQueue(new RestTemplateBuilder(), URI.create("http://localhost:" + freePort() + "/audit"));
        atnaQueue.setConnectTimeout(500);
        auditContext.setAuditMessageQueue(atnaQueue);
        sendAudit();
        assertThat(caught, instanceOf(RestClientException.class));
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
