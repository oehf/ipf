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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.queue.AsynchronousAuditMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestApplication.class })
public class IpfAtnaAutoConfigurationTest {

    @Autowired
    private AuditContext auditContext;


    @Autowired
    private IpfAtnaConfigurationProperties ipfAtnaConfigurationProperties;


    @Test
    public void testAtnaSettings() throws Exception {
        assertEquals("atna-test", auditContext.getAuditSourceId());
        assertEquals("mysite", auditContext.getAuditEnterpriseSiteId());
        assertEquals("localhost", auditContext.getAuditRepositoryHostName());
        assertEquals(1234, auditContext.getAuditRepositoryPort());
        assertEquals("TLS", auditContext.getAuditTransmissionProtocol().getTransportName());
        assertTrue(auditContext.getAuditMessageQueue() instanceof AsynchronousAuditMessageQueue);
    }

}
