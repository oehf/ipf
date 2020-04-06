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


import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.audit.SyslogServerFactory.createTCPServerTwoWayTLS;

@RunWith(VertxUnitRunner.class)
public class TLSAuditorFailingIntegrationTest extends AbstractAuditorIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(TLSAuditorFailingIntegrationTest.class);

    private CustomTlsParameters tlsParameters;

    @Before
    public void setup() {
        tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(EXPIRED_CLIENT_KEY_STORE);
        tlsParameters.setKeyStorePassword(CLIENT_KEY_STORE_PASS);
        tlsParameters.setTrustStoreFile(TRUST_STORE);
        tlsParameters.setTrustStorePassword(TRUST_STORE_PASS);
        tlsParameters.setEnabledProtocols("TLSv1.2");
    }

    @Test
    public void testTLSTwoWayTLSWrongClientCert(TestContext testContext) throws Exception {
        auditContext.setTlsParameters(tlsParameters);
        auditContext.setAuditRepositoryTransport("TLS");
        Async async = testContext.async();
        deploy(testContext, createTCPServerTwoWayTLS(port,
                TRUST_STORE,
                TRUST_STORE_PASS,
                SERVER_KEY_STORE,
                SERVER_KEY_STORE_PASS,
                async));
        sendAudit();
        try {
            async.awaitSuccess(WAIT_TIME);
            fail("Expected time-out");
        } catch (Exception e) {
            LOG.info("Exception thrown :" + e.getMessage());
        }
        if (async.isSucceeded()) {
            fail("Expected failure");
        }
        async.complete();
    }


}
