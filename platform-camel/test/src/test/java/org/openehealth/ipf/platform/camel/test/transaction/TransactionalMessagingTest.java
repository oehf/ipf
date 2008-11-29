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
package org.openehealth.ipf.platform.camel.test.transaction;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.test.TestSupport;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = {"/context-transaction.xml"})
public class TransactionalMessagingTest extends TestSupport {

    private static final long TIMEOUT = 2000L;
    
    @EndpointInject(uri="mock:transactional-messaging-mock")
    private MockEndpoint transactionalMessagingMock;
    
    @EndpointInject(uri="mock:transactional-messaging-error")
    private MockEndpoint transactionalMessagingError;
    
    @EndpointInject(uri="mock:error")
    private MockEndpoint errorMock;
    
    @After
    public void tearDown() throws Exception {
        transactionalMessagingMock.reset();
        transactionalMessagingError.reset();
        errorMock.reset();
        super.tearDown();
    }

    @Test
    public void testRollback() throws InterruptedException {
        transactionalMessagingError.expectedMessageCount(2); // 1 send, 1 retry -> 2 messages
        transactionalMessagingMock.expectedMessageCount(0);  // transaction rolled back
        sendBodies("amq:queue:transactional-messaging-input", ExchangePattern.InOnly, "blah", 1);
        transactionalMessagingError.assertIsSatisfied();
        Thread.sleep(TIMEOUT); // final JMS processing
        transactionalMessagingMock.assertIsSatisfied();
    }

    @Test
    public void testCommit() throws InterruptedException {
        transactionalMessagingError.expectedMessageCount(0); // 1 send, no retry -> 1 message
        transactionalMessagingMock.expectedMessageCount(2);  // transaction committed
        sendBodies("amq:queue:transactional-messaging-input", ExchangePattern.InOnly, "blub", 1);
        transactionalMessagingMock.assertIsSatisfied();
        transactionalMessagingError.assertIsSatisfied();
        assertEquals(2, Collections.frequency(bodies(transactionalMessagingMock.getReceivedExchanges()), "blub"));
    }

}
