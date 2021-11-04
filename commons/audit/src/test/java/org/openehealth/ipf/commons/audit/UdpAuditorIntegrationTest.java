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
import org.openehealth.ipf.commons.audit.server.UdpSyslogServer;
import org.openehealth.ipf.commons.audit.server.support.SyslogEventCollector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UdpAuditorIntegrationTest extends AbstractAuditorIntegrationTest {

    @Test
    public void testUDP() throws InterruptedException {
        auditContext.setAuditRepositoryTransport("UDP");
        var count = 10;
        var consumer = SyslogEventCollector.newInstance().withExpectation(count);
        try (var ignored = new UdpSyslogServer(consumer, Throwable::printStackTrace)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> sendAudit(Integer.toString(i)));
            boolean completed = consumer.await(5, TimeUnit.SECONDS);
            assertTrue(completed, "Consumer only received " + consumer.getSyslogEvents().size());
        }
    }

    // FIXME: the server is losing packets when more than ~90 events are sent basically parallel that
    //  cannot be handled in comparable speed.
    @Test
    public void testUDPFlooding() throws Exception {
        auditContext.setAuditRepositoryTransport("UDP");
        var count = 50;
        var threads = 2;
        var consumer = SyslogEventCollector.newInstance()
                .withExpectation(count)
                .withDelay(100);  // even if handling artificially takes long, we're done fast enough
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        try (var ignored = new UdpSyslogServer(consumer, Throwable::printStackTrace)
                .start("localhost", port)) {
            IntStream.range(0, count).forEach(i -> executor.execute(() -> sendAudit(Integer.toString(i))));
            boolean completed = consumer.await(5, TimeUnit.SECONDS);
            assertTrue(completed, "Consumer only received " + consumer.getSyslogEvents().size());
        } finally {
            executor.shutdownNow();
        }
    }

}
