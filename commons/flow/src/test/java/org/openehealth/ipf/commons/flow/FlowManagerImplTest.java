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
package org.openehealth.ipf.commons.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.openehealth.ipf.commons.flow.util.Flows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Martin Krasser
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowManagerImplTest {

    private static final String APPLICATION = "test";
        
    @Autowired
    private TestTransactionManager testTransactionManager;

    @Autowired
    private FlowManager flowManager;

    @Autowired
    private FlowRepository flowRepository;

    private ManagedMessage message;

    @Before
    public void setUp() throws Exception {
        message = new TestMessage("egal");
        testTransactionManager.beginTransaction();
        flowManager.setFlowCleanupEnabled(APPLICATION, false);
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    @Test
    public void testBegin() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        Flow flow = flowRepository.find(id);
        assertEquals(APPLICATION, flow.getApplication());
        assertEquals(0, flow.getParts().size());
    }

    @Test
    public void testCommit() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        testTransactionManager.endTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);
        assertEquals(id, message.getFlowId());
        flowManager.acknowledgeFlow(message);
        assertEquals(1, flow.getParts().size());
        FlowPart flowPart = flow.getPart("0", CLEAN);
        assertEquals(1, flowPart.getContributionCount());
    }

    @Test
    public void testSplitWithInvalidateAndAcknowledge() throws Exception {
        long id = flowManager.beginFlow(new TestMessage("egal"), APPLICATION, 3);
        TestMessage testMessage1 = new TestMessage("egal.modified1");
        TestMessage testMessage2 = new TestMessage("egal.modified2");
        TestMessage testMessage3 = new TestMessage("egal.modified3");
        testMessage1.setFlowId(id);
        testMessage2.setFlowId(id);
        testMessage3.setFlowId(id);

        SplitHistory[] history = new SplitHistory().split(3);
        testMessage1.setSplitHistory(history[0]);
        testMessage2.setSplitHistory(history[1]);
        testMessage3.setSplitHistory(history[2]);
        flowManager.invalidateFlow(testMessage1);
        flowManager.acknowledgeFlow(testMessage2);
        flowManager.acknowledgeFlow(testMessage3);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);

        FlowPart flowPart = flow.getPart("0.0", ERROR);
        assertEquals(1, flowPart.getContributionCount());
        flowPart = flow.getPart("0.1", CLEAN);
        assertEquals(1, flowPart.getContributionCount());
        flowPart = flow.getPart("0.2", CLEAN);
        assertEquals(1, flowPart.getContributionCount());
        assertEquals(1, flow.getStatusCount(ERROR));
        assertEquals(2, flow.getStatusCount(CLEAN));
    }

    @Test
    public void testSplitWithInvalidate() throws Exception {
        long id = flowManager.beginFlow(new TestMessage("egal"), APPLICATION, 3);
        TestMessage testMessage1 = new TestMessage("egal.modified1");
        TestMessage testMessage2 = new TestMessage("egal.modified2");
        TestMessage testMessage3 = new TestMessage("egal.modified3");
        testMessage1.setFlowId(id);
        testMessage2.setFlowId(id);
        testMessage3.setFlowId(id);

        SplitHistory[] history = new SplitHistory().split(3);
        testMessage1.setSplitHistory(history[0]);
        testMessage2.setSplitHistory(history[1]);
        testMessage3.setSplitHistory(history[2]);
        flowManager.invalidateFlow(testMessage1);
        flowManager.invalidateFlow(testMessage2);
        flowManager.invalidateFlow(testMessage3);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);

        FlowPart flowPart = flow.getPart("0.0", ERROR);
        assertEquals(1, flowPart.getContributionCount());
        flowPart = flow.getPart("0.1", ERROR);
        assertEquals(1, flowPart.getContributionCount());
        flowPart = flow.getPart("0.2", ERROR);
        assertEquals(1, flowPart.getContributionCount());
        assertEquals(3, flow.getStatusCount(ERROR));
        assertEquals(0, flow.getStatusCount(CLEAN));
    }

    @Test
    public void testInvalidate() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        testTransactionManager.endTransaction();
        testTransactionManager.beginTransaction();
        Flow flow = flowRepository.find(id);
        assertEquals(id, message.getFlowId());
        flowManager.invalidateFlow(message);
        assertEquals(1, flow.getParts().size());
        FlowPart flowPart = flow.getPart("0", ERROR);
        assertEquals(1, flowPart.getContributionCount());
    }

    @Test
    public void testReplay() throws Exception {
        Flow flow = Flows.createFlow("blah", 0);
        flowRepository.persist(flow);
        flow.setAckCountExpected(2);
        assertEquals(0, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
        assertEquals(0, flow.getReplayCount());
        TestMessage testMessage1 = new TestMessage("blah1");
        TestMessage testMessage2 = new TestMessage("blah2");
        testMessage1.setFlowId(flow.getIdentifier());
        testMessage2.setFlowId(flow.getIdentifier());
        SplitHistory history = new SplitHistory();
        SplitHistory[] split = history.split(2);
        testMessage1.setSplitHistory(split[0]);
        testMessage2.setSplitHistory(split[1]);
        flowManager.invalidateFlow(testMessage1);
        flowManager.acknowledgeFlow(testMessage2);
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flowManager.replayFlow(flow.getIdentifier());
        testTransactionManager.endTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(flow.getIdentifier());
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
        flowManager.acknowledgeFlow(testMessage1);
        flowManager.acknowledgeFlow(testMessage2);
        assertEquals(2, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
        assertEquals(1, flow.getReplayCount());
    }

    @Test
    public void testFlowCompleted() throws Exception {
        Flow flow = Flows.createFlow("blah");
        flowRepository.persist(flow);
        testTransactionManager.endTransaction();
        testTransactionManager.beginTransaction();
        Long id = flow.getIdentifier();
        flow = flowRepository.find(id);
        try {
            flowManager.flowCompleted(id);
            fail("method returned with no flow expectations set");
        } catch (FlowStatusException e) {
            // expected
        }
        flow.setAckCountExpected(3);
        assertFalse(flowManager.flowCompleted(id));
        flow.setAckCountExpected(2);
        assertTrue(flowManager.flowCompleted(id));
    }
    
    @Test
    public void testFindFlowMessageText() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        String text = flowManager.findFlowMessageText(id);
        assertEquals(message.render(), text);
    }
    
    @Test
    public void testFindFlowPartMessageText() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        flowManager.acknowledgeFlow(message);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        String text = flowManager.findFlowPartMessageText(id, "0");
        assertEquals(message.render(), text);
    }
    
    @Test
    public void testRenderNoCleanup() throws Exception {
        Long id = flowManager.beginFlow(message, APPLICATION);
        TestMessage acknowledged = new TestMessage("blah1");
        TestMessage invalidated = new TestMessage("blah2");
        SplitHistory[] history = message.getSplitHistory().split(2);
        acknowledged.setFlowId(id);
        invalidated.setFlowId(id);
        acknowledged.setSplitHistory(history[0]);
        invalidated.setSplitHistory(history[1]);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flowManager.acknowledgeFlow(acknowledged);
        flowManager.invalidateFlow(invalidated);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        String ackText = flowManager.findFlowPartMessageText(id, history[0].indexPathString());
        String nakText = flowManager.findFlowPartMessageText(id, history[1].indexPathString());
        assertEquals(acknowledged.render(), ackText);
        assertEquals(invalidated.render(), nakText);
    }
    
    @Test
    public void testRenderCleanup() throws Exception {
        flowManager.setFlowCleanupEnabled(APPLICATION, true);
        Long id = flowManager.beginFlow(message, APPLICATION, 2);
        TestMessage acknowledged1 = new TestMessage("blah1");
        TestMessage acknowledged2 = new TestMessage("blah2");
        SplitHistory[] history = message.getSplitHistory().split(2);
        acknowledged1.setFlowId(id);
        acknowledged2.setFlowId(id);
        acknowledged1.setSplitHistory(history[0]);
        acknowledged2.setSplitHistory(history[1]);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flowManager.acknowledgeFlow(acknowledged1);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        String ackText1 = flowManager.findFlowPartMessageText(id, history[0].indexPathString());
        String ackText2 = null;
        assertEquals(acknowledged1.render(), ackText1);
        flowManager.acknowledgeFlow(acknowledged2);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        ackText1 = flowManager.findFlowPartMessageText(id, history[0].indexPathString());
        ackText2 = flowManager.findFlowPartMessageText(id, history[1].indexPathString());
        assertEquals("N/A", ackText1);
        assertEquals("N/A", ackText2);
    }
 
    @Test
    public void testSetFlowFilterEnabled() {
        assertTrue(flowManager.isFlowFilterEnabled("a"));
        assertTrue(flowManager.isFlowFilterEnabled("b"));
        flowManager.setFlowFilterEnabled("a", false);
        assertFalse(flowManager.isFlowFilterEnabled("a"));
        assertTrue(flowManager.isFlowFilterEnabled("b"));
    }

    @Test
    public void testSetFlowCleanupEnabled() {
        assertFalse(flowManager.isFlowCleanupEnabled("a"));
        assertFalse(flowManager.isFlowCleanupEnabled("b"));
        flowManager.setFlowCleanupEnabled("a", true);
        assertTrue(flowManager.isFlowCleanupEnabled("a"));
        assertFalse(flowManager.isFlowCleanupEnabled("b"));
    }

}
