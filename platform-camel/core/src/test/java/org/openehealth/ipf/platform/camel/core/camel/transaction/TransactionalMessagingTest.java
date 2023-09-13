/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.camel.transaction;

import org.apache.activemq.artemis.junit.EmbeddedActiveMQExtension;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openehealth.ipf.platform.camel.core.camel.TestSupport;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = {
        "/context-camel-transaction.xml"
})
public class TransactionalMessagingTest extends TestSupport {

    @RegisterExtension
    private static EmbeddedActiveMQExtension server = new EmbeddedActiveMQExtension("transient-artemis.xml");

    private static final long TIMEOUT = 2000L;
    
    @EndpointInject(value="mock:txm-mock")
    private MockEndpoint txmMock;
    
    @EndpointInject(value="mock:txm-error")
    private MockEndpoint txmError;
    
    @EndpointInject(value="mock:error")
    private MockEndpoint errorMock;

    @AfterEach
    public void tearDown() throws Exception {
        txmMock.reset();
        txmError.reset();
        errorMock.reset();
    }

    @Test
    public void testRollbackDelivery() throws InterruptedException {
        txmMock.expectedBodiesReceived("blub");
        txmError.expectedBodiesReceived("blub", "blub");
        sendBodies("amq:queue:noRedeliveryQueue", ExchangePattern.InOnly, "blub", 1);
        txmError.assertIsSatisfied();
        txmMock.assertIsSatisfied();
    }

    @Test @Disabled
    public void testRollbackProcess() throws InterruptedException {
        txmMock.expectedMessageCount(0);
        txmError.expectedBodiesReceived("blah");
        sendBodies("amq:queue:noRedeliveryQueue", ExchangePattern.InOnly, "blah", 1);
        txmError.assertIsSatisfied();
        Thread.sleep(TIMEOUT);
        txmMock.assertIsSatisfied();
    }

    @Test
    public void testCommit() throws InterruptedException {
        txmError.expectedMessageCount(0);
        txmMock.expectedBodiesReceived("clean", "clean");
        sendBodies("amq:queue:noRedeliveryQueue", ExchangePattern.InOnly, "clean", 1);
        txmMock.assertIsSatisfied();
        Thread.sleep(TIMEOUT);
        txmError.assertIsSatisfied();
    }

}
