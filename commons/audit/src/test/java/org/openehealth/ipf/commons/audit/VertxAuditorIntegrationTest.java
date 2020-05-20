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


import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.audit.protocol.VertxTLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.VertxUDPSyslogSenderImpl;

import static org.openehealth.ipf.commons.audit.SyslogServerFactory.createTCPServerTwoWayTLS;
import static org.openehealth.ipf.commons.audit.SyslogServerFactory.createUDPServer;


/**
 *
 */
@RunWith(VertxUnitRunner.class)
public class VertxAuditorIntegrationTest extends AbstractAuditorIntegrationTest {

    private CustomTlsParameters tlsParameters;

    @Before
    public void setup() {
        tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(CLIENT_KEY_STORE);
        tlsParameters.setKeyStorePassword(CLIENT_KEY_STORE_PASS);
        tlsParameters.setTrustStoreFile(TRUST_STORE);
        tlsParameters.setTrustStorePassword(TRUST_STORE_PASS);
        tlsParameters.setEnabledProtocols("TLSv1.2");
    }

    @Test
    public void testUDPVertx(TestContext testContext) throws Exception {
        auditContext.setAuditTransmissionProtocol(new VertxUDPSyslogSenderImpl(vertx));
        var count = 10;
        var async = testContext.async(count);
        deploy(testContext, createUDPServer(LOCALHOST, port, async));
        for (var i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTwoWayVertxTLS(TestContext testContext) throws Exception {
        auditContext.setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl(vertx, tlsParameters));
        var count = 10;
        var async = testContext.async(count);
        deploy(testContext, createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async));
        for (var i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

    @Test
    public void testTwoWayVertxTLSInterrupted(TestContext testContext) throws Exception {
        auditContext.setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl(vertx, tlsParameters));
        var count = 5;
        var async = testContext.async(count);
        var tcpServer = createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async);
        deploy(testContext, tcpServer);
        for (var i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
        undeploy(testContext);
        deploy(testContext, tcpServer);
        for (var i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }

}
