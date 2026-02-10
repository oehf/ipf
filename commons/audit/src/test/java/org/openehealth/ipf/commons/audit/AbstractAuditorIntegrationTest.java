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


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.event.ApplicationActivityBuilder;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.openehealth.ipf.commons.core.ssl.CustomTlsParameters;

import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.Properties;


/**
 *
 */
abstract class AbstractAuditorIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(AbstractAuditorIntegrationTest.class);

    static final String CLIENT_KEY_STORE;
    static final String CLIENT_KEY_STORE_PASS = "initinit";
    static final String EXPIRED_CLIENT_KEY_STORE;
    static final String SERVER_KEY_STORE;
    static final String SERVER_KEY_STORE_PASS = "initinit";
    static final String TRUST_STORE;
    static final String TRUST_STORE_PASS = "initinit";

    static final String LOCALHOST = "localhost";
    int port;
    DefaultAuditContext auditContext;
    private Properties p;

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

    TlsParameters setupDefaultTlsParameter() {
        var tlsParameters = new CustomTlsParameters();
        tlsParameters.setKeyStoreFile(CLIENT_KEY_STORE);
        tlsParameters.setKeyStorePassword(CLIENT_KEY_STORE_PASS);
        tlsParameters.setTrustStoreFile(TRUST_STORE);
        tlsParameters.setTrustStorePassword(TRUST_STORE_PASS);
        tlsParameters.setEnabledProtocols("TLSv1.2,TLSv1.3");
        return tlsParameters;
    }

    @BeforeEach
    public void setup() {
        p = System.getProperties();
        port = freePort();
        this.auditContext = new DefaultAuditContext();
        auditContext.setAuditRepositoryPort(port);
        auditContext.setAuditRepositoryHost(LOCALHOST);
        auditContext.setAuditEnabled(true);
    }

    @AfterEach
    public void tearDown() {
        System.setProperties(p);
    }

    void sendAudit() {
        sendAudit("appName");
    }

    void sendAudit(String userName) {
        log.debug("Sending audit record");
        auditContext.audit(
                new ApplicationActivityBuilder.ApplicationStart(EventOutcomeIndicator.Success)
                        .setAuditSource(auditContext)
                        .setApplicationParticipant(
                                userName,
                                null,
                                null,
                                AuditUtils.getLocalHostName())
                        .addApplicationStarterParticipant(System.getProperty("user.name"))
                        .getMessages());
    }

    int freePort() {
        try (var serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
    }

}
