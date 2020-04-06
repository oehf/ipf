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
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol.*;


/**
 *
 */
abstract class AbstractAuditorIntegrationTest {

    private Logger LOG = LoggerFactory.getLogger(AbstractAuditorIntegrationTest.class);

    static final String CLIENT_KEY_STORE;
    static final String CLIENT_KEY_STORE_PASS = "initinit";
    static final String EXPIRED_CLIENT_KEY_STORE;
    static final String SERVER_KEY_STORE;
    static final String SERVER_KEY_STORE_PASS = "initinit";
    static final String TRUST_STORE;
    static final String TRUST_STORE_PASS = "initinit";

    static final String LOCALHOST = "localhost";
    static final long WAIT_TIME = 2000L;

    static {
        try {
            CLIENT_KEY_STORE = Paths.get(AbstractAuditorIntegrationTest.class.getResource("/security/client.keystore").toURI()).toString();
            SERVER_KEY_STORE = Paths.get(AbstractAuditorIntegrationTest.class.getResource("/security/server.keystore").toURI()).toString();
            EXPIRED_CLIENT_KEY_STORE = Paths.get(AbstractAuditorIntegrationTest.class.getResource("/security/expired.keystore").toURI()).toString();
            TRUST_STORE = Paths.get(AbstractAuditorIntegrationTest.class.getResource("/security/ca.keystore").toURI()).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Vertx vertx;
    int port;
    DefaultAuditContext auditContext;

    private Properties p;
    private String deploymentId;

    @Before
    public void setup(TestContext context) throws Exception {
        p = System.getProperties();
        port = freePort();
        this.auditContext = new DefaultAuditContext();
        auditContext.setAuditRepositoryPort(port);
        auditContext.setAuditRepositoryHost(LOCALHOST);
        auditContext.setAuditEnabled(true);
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown(TestContext context) {
        System.setProperties(p);
        deploymentId = null;
        vertx.close(undeployVertx(context));
    }

    void deploy(TestContext testContext, Verticle verticle) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        vertx.deployVerticle(verticle, deployHandler(testContext, latch));
        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    private Handler<AsyncResult<String>> deployHandler(TestContext testContext, CountDownLatch latch) {
        return testContext.asyncAssertSuccess(id -> {
            LOG.info("Server deployed with ID {}", id);
            deploymentId = id;
            latch.countDown();
        });
    }

    void undeploy(TestContext testContext) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        vertx.undeploy(deploymentId, undeployHandler(testContext, latch));
        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    private Handler<AsyncResult<Void>> undeployHandler(TestContext testContext, CountDownLatch latch) {
        return testContext.asyncAssertSuccess(id -> {
            LOG.info("Server undeployed with ID {}", deploymentId);
            deploymentId = null;
            latch.countDown();
        });
    }

    Handler<AsyncResult<Void>> undeployVertx(TestContext testContext) {
        return testContext.asyncAssertSuccess(event -> LOG.info("Vertx stopped"));
    }

    void sendAudit() {
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

    void initTLSSystemProperties(String clientKeyStore) {
        System.setProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD, CLIENT_KEY_STORE_PASS);
        System.setProperty(JAVAX_NET_SSL_KEYSTORE, clientKeyStore != null ? clientKeyStore : CLIENT_KEY_STORE);
        System.setProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD, TRUST_STORE_PASS);
        System.setProperty(JAVAX_NET_SSL_TRUSTSTORE, TRUST_STORE);
        System.setProperty(JDK_TLS_CLIENT_PROTOCOLS, "TLSv1.2");
    }

    int freePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return -1;
        }
    }

}
