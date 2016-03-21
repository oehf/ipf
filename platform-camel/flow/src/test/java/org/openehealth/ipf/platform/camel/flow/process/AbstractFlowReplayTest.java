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

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.InputStream;

import static org.junit.Assert.*;
import static org.openehealth.ipf.platform.camel.flow.PlatformMessage.FLOW_ID_KEY;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "/context-flow-platform.xml",
        "/context-flow-support.xml",
        "/context-weaver.xml"
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractFlowReplayTest {

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @Autowired
    private CamelContext camelContext;
    
    @Autowired
    private FlowManager flowManager;
    
    @EndpointInject(uri="mock:mock")
    private MockEndpoint mock;
    
    @EndpointInject(uri="mock:error")
    private MockEndpoint error;

    @After
    public void tearDown() throws Exception {
        mock.reset();
        error.reset();
    }

    @Test
    public void testReplayConvert() throws InterruptedException {
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-1", "test");
        mock.assertIsSatisfied();
        Long flowId = firstFlowId(); 
        mock.reset();
        mock.expectedBodiesReceived("test");
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        assertEquals("test-1", firstHeader("foo"));
    }
    
    @Test
    public void testReplayMarshal() throws InterruptedException {
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-2", 1.1);
        mock.assertIsSatisfied();
        Long flowId = firstFlowId(); 
        mock.reset();
        mock.expectedBodiesReceived(1.1);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        assertEquals("test-2", firstHeader("foo"));
    }

    @Test
    public void testReplaySkip() throws InterruptedException {
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-3", 1.1);
        mock.assertIsSatisfied();
        Long flowId = firstFlowId(); 
        mock.reset();
        mock.expectedMessageCount(1);
        flowManager.replayFlow(flowId);
        assertTrue("Expected body instanceOf InputStream, but was "
                + firstBody().getClass(), firstBody() instanceof InputStream);
        mock.assertIsSatisfied();
        assertEquals("test-3", firstHeader("foo"));
    }

    @Test
    public void testReplayDedupe() throws InterruptedException {
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-4", "test");
        mock.assertIsSatisfied();
        Long flowId = firstFlowId(); 
        mock.reset();
        mock.expectedMessageCount(0);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
    }
    
    @Test
    public void testReplayUnhandled() throws InterruptedException {
        mock.expectedMessageCount(0);
        error.expectedMessageCount(0);
        Exchange result = producerTemplate.send("direct:flow-test-5", createExchange("test"));
        mock.assertIsSatisfied();
        error.assertIsSatisfied();
        Long flowId = flowId(result); 
        mock.reset();
        error.reset();
        mock.expectedMessageCount(0);
        error.expectedMessageCount(0);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        error.assertIsSatisfied();
    }
    
    @Test
    public void testReplayHandled() throws InterruptedException {
        mock.expectedMessageCount(0);
        error.expectedMessageCount(0);
        Exchange result = producerTemplate.send("direct:flow-test-6", createExchange("test"));
        mock.assertIsSatisfied();
        error.assertIsSatisfied();
        Long flowId = flowId(result); 
        mock.reset();
        error.reset();
        mock.expectedMessageCount(0);
        error.expectedMessageCount(1);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        error.assertIsSatisfied();
    }
    
    @Test
    public void testOutFormatNoOutConversion() throws InterruptedException {
        // test whether during the creation of a platform packet
        // outFormat(StringDataFormat) is not applied, when outConversion(false)
        // is set.
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-7", 1.1);
        mock.assertIsSatisfied();
        Long flowId = firstFlowId();
        assertEquals(Double.class, firstBody().getClass());
        // tests whether during a replay(), outFormat(StringDataFormat)is
        // correctly applied, when outConversion(false) is set (the setting
        // should be ignored)
        mock.reset();
        mock.expectedMessageCount(1);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        assertEquals(String.class, firstBody().getClass());
    }

    @Test
    public void testOutFormatWithOutConversion() throws InterruptedException {
        // test whether during the creation of a platform packet
        // outFormat(StringDataFormat)
        // is correctly applied, when outConversion(true) is set.
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-8", 1.1);
        mock.assertIsSatisfied();
        Long flowId = firstFlowId();
        assertEquals(String.class, firstBody().getClass());
        // tests whether outFormat(StringDataFormat)is correctly applied
        // during a replay, when outConversion(true) is set.
        mock.reset();
        mock.expectedMessageCount(1);
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
        assertEquals(String.class, firstBody().getClass());
    }

    @Test
    public void testReplayTransacted() throws InterruptedException {
        mock.expectedMessageCount(1);
        producerTemplate.sendBody("direct:flow-test-9", "transacted");
        mock.assertIsSatisfied();
        Long flowId = firstFlowId();
        mock.reset();
        mock.expectedBodiesReceived("transacted");
        flowManager.replayFlow(flowId);
        mock.assertIsSatisfied();
    }

    private Exchange createExchange(String body) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(body);
        return exchange;
    }
    
    private Long firstFlowId() {
        Long flowId = flowId(mock.getExchanges().get(0));
        assertNotNull("flow id is null", flowId);
        return flowId;
    }

    private Long flowId(Exchange exchange) {
        return (Long)exchange.getIn().getHeader(FLOW_ID_KEY);
    }
    
    private Object firstBody() {
        return mock.getExchanges().get(0).getIn().getBody();
    }

    private Object firstHeader(String name) {
        return mock.getExchanges().get(0).getIn().getHeader(name);
    }

}
