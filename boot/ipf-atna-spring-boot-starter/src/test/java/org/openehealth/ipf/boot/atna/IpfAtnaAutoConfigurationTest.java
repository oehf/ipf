/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.atna;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openhealthtools.ihe.atna.auditor.AuditorTLSConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.queue.AsynchronousAuditQueue;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomain;
import org.openhealthtools.ihe.atna.nodeauth.SecurityDomainManager;
import org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestApplication.class })
public class IpfAtnaAutoConfigurationTest {

    @Autowired
    private AuditorModuleConfig auditorModuleConfig;

    @Autowired
    private AuditorModuleContext auditorModuleContext;

    @Autowired
    private AuditorTLSConfig auditorTLSConfig;

    @Autowired
    private IpfAtnaConfigurationProperties ipfAtnaConfigurationProperties;

    private static Properties p;

    @BeforeClass
    public static void setup() {
        p = System.getProperties();
        System.setProperty("javax.net.ssl.keyStore", "target/test-classes/keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "secret");
        System.setProperty("javax.net.ssl.trustStore", "target/test-classes/keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "secret");
    }

    @AfterClass
    public static void tearDown() {
        System.setProperties(p);
    }

    @Test
    public void testAtnaSettings() throws Exception {
        assertEquals("atna-test", auditorModuleConfig.getAuditSourceId());
        assertEquals("mysite", auditorModuleConfig.getAuditEnterpriseSiteId());
        assertEquals("arr.somewhere.com", auditorModuleConfig.getAuditRepositoryHost());
        assertEquals(1234, auditorModuleConfig.getAuditRepositoryPort());
        assertEquals("TLS", auditorModuleConfig.getAuditRepositoryTransport());
        assertTrue(auditorModuleContext.getQueue() instanceof AsynchronousAuditQueue);

        assertNotNull(auditorTLSConfig);
        NodeAuthModuleContext nodeAuthModuleContext = NodeAuthModuleContext.getContext();
        SecurityDomainManager securityDomainManager = nodeAuthModuleContext.getSecurityDomainManager();
        SecurityDomain securityDomain = securityDomainManager.getSecurityDomain("arr.somewhere.com", 1234);
        assertEquals("mydomain", securityDomain.getName());
    }

}
