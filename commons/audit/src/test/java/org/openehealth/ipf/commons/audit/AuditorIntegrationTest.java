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

package org.openehealth.ipf.commons.audit;


import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxFactoryImpl;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.protocol.VertxTLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.VertxUDPSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

import static org.openehealth.ipf.commons.audit.SyslogServerFactory.*;
import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.*;


/**
 *
 */
@RunWith(VertxUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditorIntegrationTest {

    private Logger LOG = LoggerFactory.getLogger(AuditorIntegrationTest.class);

    private static final String CLIENT_KEY_STORE =
            AuditorIntegrationTest.class.getResource("/security/client.keystore").getPath();
    private static final String CLIENT_KEY_STORE_PASS = "initinit";

    private static final String EXPIRED_CLIENT_KEY_STORE =
            AuditorIntegrationTest.class.getResource("/security/expired.keystore").getPath();

    private static final String SERVER_KEY_STORE =
            AuditorIntegrationTest.class.getResource("/security/server.keystore").getPath();
    private static final String SERVER_KEY_STORE_PASS = "initinit";

    private static final String TRUST_STORE =
            AuditorIntegrationTest.class.getResource("/security/ca.keystore").getPath();
    private static final String TRUST_STORE_PASS = "initinit";

    private static final String LOCALHOST = "localhost";
    private static final long WAIT_TIME = 10000L;

    private Vertx vertx;
    private int port;
    private Properties p;
    private String deploymentId;

    private DefaultAuditContext auditContext;

    @Before
    public void setup(TestContext context) throws Exception {
        p = System.getProperties();
        port = freePort();
        this.auditContext = new DefaultAuditContext();
        auditContext.setAuditRepositoryPort(port);
        auditContext.setAuditRepositoryHost(LOCALHOST);
        auditContext.setAuditRepositoryTransport("TLS");
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown(TestContext context) {
        System.setProperties(p);
        deploymentId = null;
        vertx.close(undeployVertx(context));
    }

    @Test
    public void testUDP(TestContext testContext) throws Exception {
        auditContext.setAuditRepositoryTransport("UDP");
        int count = 10;
        Async async = testContext.async(count + 1);
        vertx.deployVerticle(createUDPServer(LOCALHOST, port, async), deployHandler(testContext));
        while (async.count() > count) {
            Thread.sleep(10);
        }
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testUDPVertx(TestContext testContext) throws Exception {
        auditContext.setAuditTransmissionProtocol(new VertxUDPSyslogSenderImpl(vertx));
        int count = 10;
        Async async = testContext.async(count);
        vertx.deployVerticle(createUDPServer(LOCALHOST, port, async), deployHandler(testContext));
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTwoWayVertxTLS(TestContext testContext) throws Exception {
        initTLSSystemProperties(null);
        auditContext.setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl(vertx));
        int count = 10;
        Async async = testContext.async(count);
        vertx.deployVerticle(createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async),
                deployHandler(testContext));
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }


    @Test
    public void testOneWayTLS(TestContext testContext) throws Exception {
        initTLSSystemProperties(null);
        Async async = testContext.async();
        vertx.deployVerticle(createTCPServerOneWayTLS(port,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async),
                deployHandler(testContext));
        sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTwoWayTLS(TestContext testContext) throws Exception {
        initTLSSystemProperties(null);
        int count = 10;
        Async async = testContext.async(count);
        vertx.deployVerticle(createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async),
                deployHandler(testContext));
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }



    @Test
    public void testTwoWayTLSInterrupted(TestContext testContext) throws Exception {
        initTLSSystemProperties(null);
        int count = 5;
        Async async = testContext.async(count * 2);
        Verticle tcpServer = createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async);
        vertx.deployVerticle(tcpServer, testContext.asyncAssertSuccess(result -> deploymentId = result));
        for (int i = 0; i < count; i++) sendAudit();
        vertx.undeploy(deploymentId);
        Thread.sleep(1000);
        vertx.deployVerticle(tcpServer, testContext.asyncAssertSuccess(result -> deploymentId = result));
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTwoWayVertxTLSInterrupted(TestContext testContext) throws Exception {
        initTLSSystemProperties(null);
        auditContext.setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl(vertx));
        int count = 5;
        Async async = testContext.async(count * 2);
        Verticle tcpServer = createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async);
        vertx.deployVerticle(tcpServer, deployHandler(testContext));
        for (int i = 0; i < count; i++) sendAudit();
        vertx.undeploy(deploymentId, undeployHandler(testContext));
        Thread.sleep(500);
        vertx.deployVerticle(tcpServer, deployHandler(testContext));
        Thread.sleep(500);
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTLSTwoWayTLSWrongClientCert(TestContext testContext) throws Exception {
        initTLSSystemProperties(EXPIRED_CLIENT_KEY_STORE);
        Async async = testContext.async();
        vertx.deployVerticle(createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async),
                deployHandler(testContext));
        sendAudit();
        try {
            async.awaitSuccess(WAIT_TIME);
            Assert.fail();
        } catch (Exception e) {
            LOG.info("Exception thrown :" + e.getMessage());
        }
        if (async.isSucceeded()) {
            Assert.fail();
        }
        async.complete();
    }

    private Handler<AsyncResult<String>> deployHandler(TestContext testContext) {
        return testContext.asyncAssertSuccess(id -> {
            LOG.info("Server deployed with ID {}", id);
            deploymentId = id;
        });
    }

    private Handler<AsyncResult<Void>> undeployHandler(TestContext testContext) {
        return testContext.asyncAssertSuccess(event -> {
            LOG.info("Server undeployed with ID {}", deploymentId);
            deploymentId = null;
        });
    }

    private Handler<AsyncResult<Void>> undeployVertx(TestContext testContext) {
        return testContext.asyncAssertSuccess(event -> {
            LOG.info("Vertx stopped");
        });
    }

    private void sendAudit() throws Exception {
        LOG.debug("Sending audit record");
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

    private void initTLSSystemProperties(String clientKeyStore) {
        System.setProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD, CLIENT_KEY_STORE_PASS);
        System.setProperty(JAVAX_NET_SSL_KEYSTORE, clientKeyStore != null ? clientKeyStore : CLIENT_KEY_STORE);
        System.setProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD, TRUST_STORE_PASS);
        System.setProperty(JAVAX_NET_SSL_TRUSTSTORE, TRUST_STORE);
        // System.setProperty(JDK_TLS_CLIENT_PROTOCOLS, "TLSv1.2");
    }

    private int freePort() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return -1;
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
