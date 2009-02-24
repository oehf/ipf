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
package org.openehealth.ipf.platform.camel.core.bridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.bridge.InOnlyBridge;


/**
 * @author Martin Krasser
 */
@Deprecated
public class InOnlyBridgeTest {

    private Exchange target;
    
    private DefaultExchange exchange;
    
    @Before
    public void setUp() throws Exception {
        exchange = new DefaultExchange(new DefaultCamelContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProcessInOut() throws Exception {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getIn().setBody("in-body");
        exchange.getOut().setBody("out-body");
     
        InOnlyBridge inOnlyBridge = new InOnlyBridge();
        inOnlyBridge.setProcessor(new NextProcessor());
        inOnlyBridge.process(exchange);
        
        assertEquals(ExchangePattern.InOnly, target.getPattern());
        assertEquals("out-body", target.getIn().getBody());
        assertNull(target.getOut(false));
    }
    
    @Test
    public void testProcessInOnly() throws Exception {
        exchange.setPattern(ExchangePattern.InOnly);
        exchange.getIn().setBody("in-body");
     
        InOnlyBridge inOnlyBridge = new InOnlyBridge();
        inOnlyBridge.setProcessor(new NextProcessor());
        inOnlyBridge.process(exchange);
        
        assertEquals(ExchangePattern.InOnly, target.getPattern());
        assertEquals("in-body", target.getIn().getBody());
        assertNull(target.getOut(false));
    }
    
    private class NextProcessor implements Processor {
        public void process(Exchange exchange) throws Exception {
            target = exchange;
        }
    }
}
