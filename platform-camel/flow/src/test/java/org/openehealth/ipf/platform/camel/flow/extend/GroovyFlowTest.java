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

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.platform.camel.flow.process.AbstractFlowTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.platform.camel.flow.PlatformMessage.FLOW_ID_KEY;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-flow-route-groovy.xml" })
public class GroovyFlowTest extends AbstractFlowTest {

    @Autowired
    private FlowManager flowManager;
    
    @EndpointInject(uri="mock:wait")
    private MockEndpoint wait;
    
    @After
    public void tearDown() throws Exception {
        wait.reset();
        super.tearDown();
    }

    @Test
    public void testRecipientListSuccess() throws Exception {
        wait.expectedMessageCount(2);
        mock.expectedBodiesReceived("test1", "test2");
        producerTemplate.sendBodyAndHeader("direct:flow-test-recipient-list", "test1,test2", "recipient", "http4://localhost:7799/recipient");
        wait.assertIsSatisfied();
        mock.assertIsSatisfied();
        Long flowId1 = mock.getExchanges().get(0).getIn().getHeader(FLOW_ID_KEY, Long.class);
        Long flowId2 = mock.getExchanges().get(1).getIn().getHeader(FLOW_ID_KEY, Long.class);
        FlowInfo flow1 = flowManager.findFlow(flowId1);
        FlowInfo flow2 = flowManager.findFlow(flowId2);
        assertEquals(1, flow1.getAckCount());
        assertEquals(0, flow1.getNakCount());
        assertEquals(1, flow2.getAckCount());
        assertEquals(0, flow2.getNakCount());
        assertEquals("CLEAN", flow1.getStatus());
        assertEquals("CLEAN", flow2.getStatus());
    }
    
    @Test
    public void testRecipientListFailure() throws Exception {
        mock.expectedMessageCount(2);
        producerTemplate.sendBodyAndHeader("direct:flow-test-recipient-list", "test1,test2", "recipient", "http4://localhost:7799/bullshit");
        mock.assertIsSatisfied();
        Long flowId1 = mock.getExchanges().get(0).getIn().getHeader(FLOW_ID_KEY, Long.class);
        Long flowId2 = mock.getExchanges().get(1).getIn().getHeader(FLOW_ID_KEY, Long.class);
        FlowInfo flow1 = flowManager.findFlow(flowId1);
        FlowInfo flow2 = flowManager.findFlow(flowId2);
        assertEquals(0, flow1.getAckCount());
        assertEquals(1, flow1.getNakCount());
        assertEquals(0, flow2.getAckCount());
        assertEquals(1, flow2.getNakCount());
        assertEquals("ERROR", flow1.getStatus());
        assertEquals("ERROR", flow2.getStatus());
    }
    
    
    @Test
    public void testInitflowAfterSplitWithNoAggregationStrategy() throws Exception {
        String body = "test1,test2";
        mock.expectedMessageCount(1);
        mock.expectedBodyReceived().constant(body);
        
        producerTemplate.sendBody("direct:init-flow-after-split-with-no-explicit-aggregation-strategy", body);
        mock.assertIsSatisfied();
    }
    
}