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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-responder.xml" })
public class ResponderExtensionTest extends AbstractExtensionTest {

    @Test
    public void testValidInOnlyProc() throws InterruptedException {
        testValidInOnly("direct:responder-test-1");
    }
    
    @Test
    public void testValidInOnlyRef() throws InterruptedException {
        testValidInOnly("direct:responder-test-2");
    }
    
    @Test
    public void testValidInOutProc() throws InterruptedException {
        testValidInOut("direct:responder-test-1");
    }
    
    @Test
    public void testValidInOutRef() throws InterruptedException {
        testValidInOut("direct:responder-test-2");
    }
    
    private void testValidInOnly(String uri) throws InterruptedException {
        mockOutput.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send(uri,
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

    public void testValidInOut(String uri) throws InterruptedException {
    	mockOutput.expectedBodiesReceived("test");
        Exchange exchange = producerTemplate.send(uri,
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

}
