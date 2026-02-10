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

package org.openehealth.ipf.boot.fhir;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.BalpAuditContext;
import org.openehealth.ipf.commons.audit.queue.AsynchronousAuditMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


/**
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestApplication.class })
@ActiveProfiles("balp")
public class IpfAtnaBalpAutoConfigurationTest {

    @Autowired
    private AuditContext auditContext;

    @Test
    public void testAtnaWithBalpSettings() {
        assertInstanceOf(BalpAuditContext.class, auditContext);

        assertEquals("atna-test", auditContext.getAuditSourceId());
        assertEquals("mysite", auditContext.getAuditEnterpriseSiteId());
        assertEquals("localhost", auditContext.getAuditRepositoryHostName());
        assertEquals(1342, auditContext.getAuditRepositoryPort());
        assertEquals("FHIR-REST-TLS", auditContext.getAuditTransmissionProtocol().getTransportName());
        assertInstanceOf(AsynchronousAuditMessageQueue.class, auditContext.getAuditMessageQueue());

        assertEquals("fhir", ((BalpAuditContext)auditContext).getAuditRepositoryContextPath());
        assertArrayEquals(new String[]{"cid","client-id","my-client-id-path"},
            ((BalpAuditContext) auditContext).getBalpJwtExtractorProperties().getClientIdPath());
        assertArrayEquals(new String[]{},
            ((BalpAuditContext) auditContext).getBalpJwtExtractorProperties().getIdPath());
    }

}
