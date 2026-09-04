/*
 * Copyright 2026 the original author or authors.
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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.queue.CompositeAuditMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * The audit message queue comes in two variants that live in separate, classpath-conditional configuration
 * classes -- one plain, one publishing to Spring's {@link AuditEventRepository} as well. This test covers
 * the second: with the actuator present, {@code ipf.atna.spring-audit-event-enabled} switched on and an
 * AuditEventRepository bean around, the composite queue must win over the plain one.
 * <p>
 * The plain variant is covered wherever the actuator is absent, e.g. by the startup tests of the
 * transaction family starters, none of which pull the actuator in.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@SpringBootTest(
        classes = {TestApplication.class, SpringAuditEventQueueTest.Config.class},
        properties = "ipf.atna.spring-audit-event-enabled=true")
public class SpringAuditEventQueueTest {

    @TestConfiguration
    static class Config {

        @Bean
        AuditEventRepository auditEventRepository() {
            return new InMemoryAuditEventRepository();
        }
    }

    @Autowired
    private AuditContext auditContext;

    @Test
    public void testCompositeQueueIsUsed() {
        assertInstanceOf(CompositeAuditMessageQueue.class, auditContext.getAuditMessageQueue());
    }
}
