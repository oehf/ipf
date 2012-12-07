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
package org.openehealth.ipf.platform.camel.flow.process;

import static org.junit.Assert.assertEquals;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Jens Riemschneider
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "/context-flow-platform.xml",
        "/context-flow-processor.xml",
        "/context-flow-support.xml",
        "/context-weaver.xml"
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractSplitTest {

    @Autowired
    private CamelContext camelContext;
    
    @Autowired
    private ProducerTemplate producerTemplate;
    
    @EndpointInject(uri="mock:mock-1")
    private MockEndpoint mock1;
    
    @EndpointInject(uri="mock:mock-2")
    private MockEndpoint mock2;

    private SplitHistory initial;
    
    @Before
    public void setUp() throws Exception {
        initial = SplitHistory.parse("[(0L),(0)]");
    }

    @After
    public void tearDown() throws Exception {
        mock1.reset();
        mock2.reset();
    }

    @Test
    public void testIpfSplitter() throws InterruptedException {
        mock1.expectedBodiesReceived("test", "bla");
        
        Exchange result = (Exchange)producerTemplate.send("direct:split-test-ipfsplit", 
                createMessage("test,bla").getExchange());
        
        mock1.assertIsSatisfied();
        assertEquals(SplitHistory.parse("[(0L),(0),(0)]"), 
                new PlatformMessage(mock1.getExchanges().get(0)).getSplitHistory());
        assertEquals(SplitHistory.parse("[(0L),(0),(1L)]"), 
                new PlatformMessage(mock1.getExchanges().get(1)).getSplitHistory());
        assertEquals(initial, new PlatformMessage(result).getSplitHistory()); 
    }
    
    @Test
    public void testIpfSplitterAgg() throws InterruptedException {
        mock1.expectedBodiesReceived("test", "bla");
        mock2.expectedBodiesReceived("bla");
        
        Exchange exchange = createMessage("test,bla").getExchange();
        
        Exchange result = (Exchange)producerTemplate.send(
                "direct:split-test-ipfsplit-agg", exchange);
        
        mock1.assertIsSatisfied();
        assertEquals(SplitHistory.parse("[(0L),(0),(0)]"), 
                new PlatformMessage(mock1.getExchanges().get(0)).getSplitHistory());
        assertEquals(SplitHistory.parse("[(0L),(0),(1L)]"), 
                new PlatformMessage(mock1.getExchanges().get(1)).getSplitHistory());

        assertEquals(initial,
                new PlatformMessage(mock2.getExchanges().get(0)).getSplitHistory());
        
        assertEquals(initial, new PlatformMessage(result).getSplitHistory()); 
    }
    
    @Test
    public void testIpfSplitterSingleResultSplit() throws Exception {
        mock1.expectedBodiesReceived("test");
        
        Exchange result = (Exchange)producerTemplate.send("direct:split-test-ipfsplit", 
                createMessage("test").getExchange());
        
        mock1.assertIsSatisfied();
        assertEquals(initial, 
                new PlatformMessage(mock1.getExchanges().get(0)).getSplitHistory());
        assertEquals(initial, new PlatformMessage(result).getSplitHistory()); 
    }
    
    private PlatformMessage createMessage(String body) {
        PlatformMessage platformMessage = new PlatformMessage(createExchange(body));
        platformMessage.setSplitHistory(initial);
        return platformMessage;
    }
    
    private Exchange createExchange(String body) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(body);
        return exchange;
    }
    
}
