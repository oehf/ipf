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
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.openehealth.ipf.commons.audit.SyslogServerFactory.createUDPServer;


/**
 *
 */
@RunWith(VertxUnitRunner.class)
public class UdpAuditorIntegrationTest extends AbstractAuditorIntegrationTest {

    @Test
    public void testUDP(TestContext testContext) throws Exception {
        auditContext.setAuditRepositoryTransport("UDP");
        int count = 10;
        Async async = testContext.async(count + 1);
        deploy(testContext, createUDPServer(LOCALHOST, port, async));
        while (async.count() > count) {
            Thread.sleep(10);
        }
        for (int i = 0; i < count; i++) sendAudit();
        async.awaitSuccess(WAIT_TIME);
    }


}
