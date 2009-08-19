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
package org.openehealth.ipf.platform.camel.core.extend;

import static org.junit.Assert.*;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-validation.xml" })
public class ValidationExtensionTest extends AbstractExtensionTest {

    @Test
    public void testResponderObjectSuccess() throws InterruptedException {
        testSuccess("direct:input1");
    }

    @Test
    public void testResponderObjectFault() throws InterruptedException {
        testFault("direct:input2");
    }
    
    @Test
    public void testResponderObjectError() throws InterruptedException {
        testError("direct:input3");
    }
    
    @Test
    public void testResponderEndpointSuccess() throws InterruptedException {
        testSuccess("direct:input4");
    }

    @Test
    public void testResponderEndpointFault() throws InterruptedException {
        testFault("direct:input5");
    }
    
    @Test
    public void Endpoint() throws InterruptedException {
        testError("direct:input6");
    }
    
    @Test
    public void testResponderClosureSuccess() throws InterruptedException {
        testSuccess("direct:input7");
    }

    @Test
    public void testResponderClosureFault() throws InterruptedException {
        testFault("direct:input8");
    }
    
    @Test
    public void testResponderClosureError() throws InterruptedException {
        testError("direct:input9");
    }
    
    public void testSuccess(String endpoint) throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        Exchange result = producerTemplate.request(endpoint, new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody("blah");
            }
        });
        assertEquals("result", result.getOut().getBody());
        mockOutput.assertIsSatisfied();
    }

    public void testFault(String endpoint) throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange result = producerTemplate.request(endpoint, new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody("blah");
            }
        });
        assertEquals("failed", result.getOut().getBody());
        assertTrue(result.getOut().isFault());
        mockOutput.assertIsSatisfied();
    }
    
    public void testError(String endpoint) throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange result = producerTemplate.request(endpoint, new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody("blah");
            }
        });
        assertEquals("failed", result.getException().getMessage());
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testValidInOnly() throws InterruptedException {
        mockOutput.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send("direct:validation-test-1",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("blah", exchange.getIn().getBody());
        assertFalse(exchange.hasOut());
        assertNull(exchange.getException());
    }

    @Test
    public void testValidInOut() throws InterruptedException {
        mockOutput.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send("direct:validation-test-1",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("blah", exchange.getOut().getBody());
        assertFalse(exchange.getOut().isFault());
        assertNull(exchange.getException());
    }

    @Test
    public void testFaultInOnly() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-2",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("failed", exchange.getIn().getBody());
        assertFalse(exchange.hasOut()); // as per definition of an in-only exchange
        assertTrue(exchange.getIn().isFault());
        assertNull(exchange.getException());
    }

    @Test
    public void testFaultInOut() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-2",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getOut().getBody());
        assertTrue(exchange.getOut().isFault());
        assertNull(exchange.getException());
    }

    @Test
    public void testErrorInOnly() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-3",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        // Strange: Camel sets an out message on an in-only exchange if 
        //          the default error handler (dead letter channel) is set
        assertFalse(exchange.hasOut()); // no error handler set
    }

    @Test
    public void testErrorInOut() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-3",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mockOutput.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        assertFalse(exchange.hasOut());
    }
    
}
