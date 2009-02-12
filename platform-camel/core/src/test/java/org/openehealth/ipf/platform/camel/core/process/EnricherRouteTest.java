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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;


/**
 * @author Martin Krasser
 */
public class EnricherRouteTest extends AbstractRouteTest {

    // -------------------------------------------------------------
    //  InOnly routes
    // -------------------------------------------------------------
    
    @Test
    public void testEnrichInOnly1() throws InterruptedException {
        mock.expectedBodiesReceived("test:blah");
        producerTemplate.sendBody("direct:enricher-test-1", "test");
        mock.assertIsSatisfied();
    }

    @Test
    public void testEnrichInOnly2() throws InterruptedException {
        mock.expectedBodiesReceived("test:test");
        producerTemplate.sendBody("direct:enricher-test-2", "test");
        mock.assertIsSatisfied();
    }

    @Test
    public void testEnrichFaultInOnly() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:enricher-test-3", new Processor() {
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
    public void testEnrichErrorInOnly() throws InterruptedException {
        mock.expectedMessageCount(0);
        Exchange exchange = producerTemplate.send("direct:enricher-test-4", new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
        });
        mock.assertIsSatisfied();
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        assertNull(exchange.getFault(false));
        // Strange: Camel sets an out message on an in-only exchange if 
        //          the default error handler (dead letter channel) is set
        assertNull(exchange.getOut(false)); // no error handler set
    }

    // -------------------------------------------------------------
    //  InOut routes
    // -------------------------------------------------------------
    
    @Test
    public void testEnrichInOut1() throws InterruptedException {
        String result = (String)producerTemplate.sendBody("direct:enricher-test-5", 
                ExchangePattern.InOut, "test");
        assertEquals("test:blah", result);
    }

    @Test
    public void testEnrichInOut2() throws InterruptedException {
        String result = (String)producerTemplate.sendBody("direct:enricher-test-6", 
                ExchangePattern.InOut, "test");
        assertEquals("test:test", result);
    }

    @Test
    public void testEnrichInOut3() throws InterruptedException {
        Exchange exchange = producerTemplate.send("direct:enricher-test-6",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setHeader("foo", "bar");
                        exchange.getIn().setBody("test");
                    }
                });
        assertEquals("bar", exchange.getIn().getHeader("foo"));
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("bar", exchange.getOut().getHeader("foo"));
        assertEquals("test:test", exchange.getOut().getBody());
        assertNull(exchange.getFault(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testEnrichFaultInOut() throws InterruptedException {
        Exchange exchange = producerTemplate.send("direct:enricher-test-7",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
                });
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getFault(false).getBody());
        assertNull(exchange.getOut(false));
        assertNull(exchange.getException());
    }

    @Test
    public void testEnrichErrorInOut() throws InterruptedException {
        Exchange exchange = producerTemplate.send("direct:enricher-test-8", 
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("test");
                    }
        });
        assertEquals("test", exchange.getIn().getBody());
        assertEquals("failed", exchange.getException().getMessage());
        assertNull(exchange.getFault(false));
        assertNull(exchange.getOut(false));
    }

}
