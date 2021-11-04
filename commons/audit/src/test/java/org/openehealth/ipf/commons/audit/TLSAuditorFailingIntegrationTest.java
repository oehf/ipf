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


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.server.TlsSyslogServer;
import org.openehealth.ipf.commons.audit.server.support.SyslogEventCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TLSAuditorFailingIntegrationTest extends AbstractAuditorIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(TLSAuditorFailingIntegrationTest.class);

    private CustomTlsParameters tlsParameters;

    @BeforeEach
    public void setupTls() {
        tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(EXPIRED_CLIENT_KEY_STORE);
        tlsParameters.setKeyStorePassword(CLIENT_KEY_STORE_PASS);
        tlsParameters.setTrustStoreFile(TRUST_STORE);
        tlsParameters.setTrustStorePassword(TRUST_STORE_PASS);
        tlsParameters.setEnabledProtocols("TLSv1.2");
    }

    @Test
    public void testTLSTwoWayTLSWrongClientCert() throws InterruptedException {
        auditContext.setTlsParameters(tlsParameters);
        auditContext.setAuditRepositoryTransport("TLS");
        var consumer = SyslogEventCollector.newInstance().withExpectation(1);
        try (var ignored = new TlsSyslogServer(consumer, Throwable::printStackTrace, TlsParameters.getDefault())
                .start("localhost", port)) {
            sendAudit();
            assertFalse(consumer.await(1, TimeUnit.SECONDS));
        }

    }


}
