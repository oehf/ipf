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
package org.openehealth.ipf.platform.camel.core.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;


/**
 * @author Martin Krasser
 */
public class ValidationRouteTest extends AbstractRouteTest {

    @Test
    public void testValidInOnly() throws InterruptedException {
        mock.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send("direct:validation-test-1",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("blah", exchange.getIn().getBody());
        assertNull(exchange.getOut(false));
        assertNull(exchange.getFault(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testValidInOut() throws InterruptedException {
        mock.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send("direct:validation-test-1",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("blah", exchange.getOut(false).getBody());
        assertNull(exchange.getFault(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testFaultInOnly() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-2",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getFault(false).getBody());
        assertNull(exchange.getOut(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testFaultInOut() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-2",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getFault(false).getBody());
        assertNull(exchange.getOut(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testErrorInOnly() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-3",
                new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        // Strange: Camel sets an out message on an in-only exchange if 
        //          the default error handler (dead letter channel) is set
        assertNull(exchange.getOut(false)); // no error handler set
        assertNull(exchange.getFault(false));
    }

    @Test
    public void testErrorInOut() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:validation-test-3",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });

        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        assertNull(exchange.getOut(false));
        assertNull(exchange.getFault(false));
    }

}
