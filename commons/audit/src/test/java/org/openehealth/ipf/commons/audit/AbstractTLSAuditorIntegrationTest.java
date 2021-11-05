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


import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.server.TlsSyslogServer;
import org.openehealth.ipf.commons.audit.server.support.SyslogEventCollector;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractTLSAuditorIntegrationTest extends AbstractAuditorIntegrationTest {

    protected abstract String transport();
    
    @Test
    public void testTwoWayTLS() throws Exception {
        var defaultTls = setupDefaultTlsParameter();
        auditContext.setTlsParameters(defaultTls);
        auditContext.setAuditRepositoryTransport(transport());
        var count = 10;
        var consumer = SyslogEventCollector.newInstance().withExpectation(count);

        try (var ignored = new TlsSyslogServer(consumer, Throwable::printStackTrace, defaultTls)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> sendAudit());
            assertTrue(consumer.await(5, TimeUnit.SECONDS));
        }
    }

    @Test
    public void testTwoWayTLSFlooding() throws Exception {
        var defaultTls = setupDefaultTlsParameter();
        auditContext.setTlsParameters(defaultTls);
        auditContext.setAuditRepositoryTransport(transport());
        var count = 500;
        var threads = 2;
        var consumer = SyslogEventCollector.newInstance()
                .withExpectation(count)
                .withDelay(100); // even if handling takes artificially long, we're done fast enough
        var executor = Executors.newFixedThreadPool(threads);
        try (var ignored = new TlsSyslogServer(consumer, Throwable::printStackTrace, defaultTls)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> executor.execute(() -> sendAudit(Integer.toString(i))));
            var completed = consumer.await(15, TimeUnit.SECONDS);
            assertTrue(completed, "Consumer only received " + consumer.getSyslogEvents().size());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    public void testTwoWayTLSInterrupted() throws Exception {
        var defaultTls = setupDefaultTlsParameter();
        auditContext.setTlsParameters(defaultTls);
        auditContext.setAuditRepositoryTransport(transport());
        var count = 10;
        var consumer = SyslogEventCollector.newInstance().withExpectation(count);

        try (var ignored = new TlsSyslogServer(consumer, Throwable::printStackTrace, defaultTls)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> sendAudit());
            assertTrue(consumer.await(5, TimeUnit.SECONDS));
        }

        try (var ignored = new TlsSyslogServer(consumer, Throwable::printStackTrace, defaultTls)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> sendAudit());
            assertTrue(consumer.await(5, TimeUnit.SECONDS));
        }

    }

}