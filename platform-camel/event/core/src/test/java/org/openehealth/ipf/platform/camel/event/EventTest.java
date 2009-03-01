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
package org.openehealth.ipf.platform.camel.event;

import static org.junit.Assert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-groovy.xml" })
public class EventTest {
    @Autowired
    private CamelContext context;
    
    @Autowired
    private ProducerTemplate<Exchange> template;
    
    @EndpointInject(uri="mock:mock")
    protected MockEndpoint mock;
    
    @Autowired
    @Qualifier("eventHandler")
    private MyHandler1 handler;
    
    @After
    public void tearDown() {
        mock.reset();
        handler.reset();
    }
    
    @Test
    public void testSimpleEvent() throws Exception {
        mock.expectedMessageCount(1);
        
        template.send("direct:start_simple", new DefaultExchange(context));
        
        mock.assertIsSatisfied();

        assertTrue(handler.isHandled());
        assertEquals("hello world", handler.getProp());
    }

    @Test
    public void testUnsubscribedTopicEvent() throws Exception {
        mock.expectedMessageCount(1);
        
        template.send("direct:start_unsub_topic", new DefaultExchange(context));
        
        mock.assertIsSatisfied();

        assertFalse(handler.isHandled());
    }

    @Test
    public void testTopicEvent() throws Exception {
        mock.expectedMessageCount(1);
        
        template.send("direct:start_topic", new DefaultExchange(context));
        
        mock.assertIsSatisfied();

        assertTrue(handler.isHandled());
        assertEquals("hello world", handler.getProp());
    }


    @Test
    public void testFilteredEvent() throws Exception {
        mock.expectedMessageCount(1);
        
        template.send("direct:start_filter", new DefaultExchange(context));
        
        mock.assertIsSatisfied();

        assertTrue(handler.isHandled());
        assertEquals("hello world", handler.getProp());
    }
}
