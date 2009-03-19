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
package org.openehealth.ipf.platform.camel.flow.extend;

import static org.junit.Assert.*;
import static org.openehealth.ipf.platform.camel.flow.PlatformMessage.FLOW_ID_KEY;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "/context-flow-platform.xml",
        "/context-flow-processor.xml",
        "/context-flow-support.xml",
        "/context-render-route-groovy.xml"
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class GroovyFlowRenderTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    
    @Autowired
    private FlowManager flowManager;
    
    @EndpointInject(uri="mock:out")
    private MockEndpoint out;
    
    @EndpointInject(uri="mock:err")
    private MockEndpoint err;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        out.reset();
        err.reset();
    }

    @Test
    public void testAck() throws Exception {
        out.expectedMessageCount(1);
        err.expectedMessageCount(0);
        producerTemplate.sendBody("direct:render-test", "clean");
        out.assertIsSatisfied();
        err.assertIsSatisfied();
        FlowInfo flow = flowManager.findFlow(flowId(out), true);
        assertEquals("Init: clean", flow.getText());
        assertEquals("Ack: clean", flow.getPartInfos().iterator().next().getText());
    }

    @Test
    public void testNak() throws Exception {
        out.expectedMessageCount(0);
        err.expectedMessageCount(1);
        try {
            producerTemplate.sendBody("direct:render-test", "error");
            fail("failure not reported");
        } catch (RuntimeCamelException e) {
            // ok
        }
        out.assertIsSatisfied();
        err.assertIsSatisfied();
        FlowInfo flow = flowManager.findFlow(flowId(err), true);
        assertEquals("Init: error", flow.getText());
        assertEquals("Nak: error", flow.getPartInfos().iterator().next().getText());
    }

    private static Long flowId(MockEndpoint endpoint) {
        return (Long)endpoint.getExchanges().get(0).getIn().getHeader(FLOW_ID_KEY);
    }
    
}
