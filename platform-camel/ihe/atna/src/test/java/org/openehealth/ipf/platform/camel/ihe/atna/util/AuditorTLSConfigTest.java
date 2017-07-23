/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.atna.util;

import java.net.InetAddress;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.AuditorTLSConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.openhealthtools.ihe.atna.auditor.sender.TLSSyslogSenderImpl;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class AuditorTLSConfigTest {

    private Properties p;

    @Before
    public void setup() {
        p = System.getProperties();
        System.setProperty("javax.net.ssl.keyStore", "target/test-classes/keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "secret");
        System.setProperty("javax.net.ssl.trustStore", "target/test-classes/keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "secret");
    }

    @After
    public void tearDown() {
        System.setProperties(p);
    }

    @Test
    public void testTLSAudit() throws Exception {
        int port = 97465;
        InetAddress localhost = InetAddress.getLocalHost();
        String host = localhost.getHostAddress();
        AuditorModuleContext context = AuditorModuleContext.getContext();
        AuditorModuleConfig moduleConfig = context.getConfig();
        moduleConfig.setAuditRepositoryTransport("TLS");
        moduleConfig.setAuditRepositoryHost(host);
        moduleConfig.setAuditRepositoryPort(port);
        AuditorTLSConfig config = new AuditorTLSConfig(moduleConfig);
        config.init();

        NodeAuthModuleContext nodeContext = NodeAuthModuleContext.getContext();
        SecurityDomainManager securityDomainManager = nodeContext
                .getSecurityDomainManager();
        assertNotNull(securityDomainManager
                .getSecurityDomain(AuditorTLSConfig.DEFAULT_SECURITY_DOMAIN_NAME));
        assertNotNull(securityDomainManager.getSecurityDomain(host, port));
        AuditMessageSender ams = context.getSender();
        assertTrue(ams instanceof TLSSyslogSenderImpl);
    }
}

