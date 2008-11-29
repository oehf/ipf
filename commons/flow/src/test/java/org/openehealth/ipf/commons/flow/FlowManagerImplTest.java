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

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;
import org.openehealth.ipf.commons.flow.domain.FlowStatus;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.openehealth.ipf.commons.flow.util.Flows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;


/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowManagerImplTest {

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
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    @Test
    public void testBegin() throws Exception {
        Long id = flowManager.beginFlow(message, "test");
        Flow flow = flowRepository.find(id);
        assertEquals("test", flow.getApplication());
        assertEquals(0, flow.getParts().size());
    }

    @Test
    public void testCommit() throws Exception {
        Long id = flowManager.beginFlow(message, "test");
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
    public void testOneSplitWithInvalidateAndAcknowledge() throws Exception {
        // the parameters ackCountExpected, application and MessageContent are known in advance
        ManagedMessage initialMessage = new TestMessage("egal");
        long id = flowManager.beginFlow(initialMessage, "test", 3);
        TestMessage testMessage1 = new TestMessage("egal.modified1");
        TestMessage testMessage2 = new TestMessage("egal.modified2");
        TestMessage testMessage3 = new TestMessage("egal.modified3");

        SplitHistory[] history = new SplitHistory().split(3);
        testMessage1.setFlowId(id);
        testMessage2.setFlowId(id);
        testMessage3.setFlowId(id);

        testMessage1.setSplitHistory(history[0]);
        testMessage2.setSplitHistory(history[1]);
        testMessage3.setSplitHistory(history[2]);

        flowManager.invalidateFlow(testMessage1);
        flowManager.acknowledgeFlow(testMessage2);
        flowManager.acknowledgeFlow(testMessage3);

        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // load it and check the properties
        Flow flow = flowRepository.find(id);

        FlowPart flowPart = flow.getPart("0.0", ERROR);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.0", CLEAN) == null);

        flowPart = flow.getPart("0.1", CLEAN);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.1", ERROR) == null);

        flowPart = flow.getPart("0.2", CLEAN);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.2", ERROR) == null);

        assertTrue(flow.getStatusCount(ERROR) == 1);
        assertTrue(flow.getStatusCount(CLEAN) == 2);
    }

    @Test
    public void testInvalidateWithSplit() throws Exception {

        ManagedMessage initialMessage = new TestMessage("egal");

        // the parameters ackCountExpected, application and MessageContent are known in advance
        long id = flowManager.beginFlow(initialMessage, "test", 3);
        // someone starts the flow.
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        TestMessage testMessage1 = new TestMessage("egal.modified.by.processor.1");
        TestMessage testMessage2 = new TestMessage("egal.modified.by.processor.2");
        TestMessage testMessage3 = new TestMessage("egal.modified.by.processor.3");

        SplitHistory[] history = new SplitHistory().split(3);
        testMessage1.setFlowId(id);
        testMessage2.setFlowId(id);
        testMessage3.setFlowId(id);

        testMessage1.setSplitHistory(history[0]);
        testMessage2.setSplitHistory(history[1]);
        testMessage3.setSplitHistory(history[2]);

        // the flow error processor of endpoint 1 is invoked
        flowManager.invalidateFlow(testMessage1);
        // the flow error processor of endpoint 2 is invoked
        flowManager.invalidateFlow(testMessage2);
        // the flow error processor of endpoint 3 is invoked
        flowManager.invalidateFlow(testMessage3);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        // load it and check the properties
        Flow flow = flowRepository.find(id);
        // check if anything is as expected
        int cleans = flow.getStatusCount(CLEAN);
        assertTrue(cleans == 0);
        FlowPart flowPart = flow.getPart("0.0", ERROR);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.0", CLEAN) == null);

        flowPart = flow.getPart("0.1", ERROR);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.1", CLEAN) == null);

        flowPart = flow.getPart("0.2", ERROR);
        assertTrue(flowPart != null);
        assertTrue(flowPart.getContributionCount() == 1);
        assertTrue(flow.getPart("0.2", CLEAN) == null);

        // all the 3 error processors are invoked, therefore ERROR count is 3
        assertTrue(flow.getStatusCount(ERROR) == 3);
        // no end processors is invoked, therefore CLEAN count is 0
        assertTrue(flow.getStatusCount(CLEAN) == 0);

    }

    @Test
    public void testInvalidate() throws Exception {
        Long id = flowManager.beginFlow(message, "test");
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
        // do not create parts for the flow.
        Flow flow = Flows.createFlow(message.createPacket(), false);
        flowRepository.persist(flow);
        flow.setAckCountExpected(2);

        assertTrue(flow.getStatusCount(ERROR) == 0);
        assertTrue(flow.getStatusCount(CLEAN) == 0);

        int count = flow.getReplayCount();
        Date creationTimeTypeDate = flow.getCreationTime();
        byte[] packet = "new Packet".getBytes();
        flow.setPacket(packet);
        // one invalidate

        TestMessage testMessage1 = new TestMessage("Packet 1");
        testMessage1.setFlowId(flow.getIdentifier());
        TestMessage testMessage2 = new TestMessage("Packet 2");
        testMessage2.setFlowId(flow.getIdentifier());
        SplitHistory history = new SplitHistory();
        SplitHistory[] split = history.split(2);
        // setting the split history is important, since the Flows are not created by the
        // FlowManager
        testMessage1.setSplitHistory(split[0]);
        testMessage2.setSplitHistory(split[1]);
        // must increment NAK
        flowManager.invalidateFlow(testMessage1);
        // must increment ACC
        flowManager.acknowledgeFlow(testMessage2);
        assertTrue(flow.getStatusCount(ERROR) == 1);
        assertTrue(flow.getStatusCount(CLEAN) == 1);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        flowManager.replayFlow(flow.getIdentifier());
        testTransactionManager.endTransaction();

        testTransactionManager.beginTransaction();
        flow = flowRepository.find(flow.getIdentifier());
        assertTrue(flow.getStatusCount(CLEAN) == 1);
        assertTrue(flow.getStatusCount(ERROR) == 0);

        flowManager.acknowledgeFlow(testMessage1);
        flowManager.acknowledgeFlow(testMessage2);

        assertTrue(flow.getStatusCount(ERROR) == 0);
        assertTrue(flow.getStatusCount(CLEAN) == 2);

        int newCount = flow.getReplayCount();
        assertTrue(newCount == ++count);

        Date dateNewCreationTypeTimestamp = flow.getCreationTime();
        // assert the creation time is not changed
        assertTrue(creationTimeTypeDate.equals(dateNewCreationTypeTimestamp));

    }

    @Test
    public void testCompleted() throws Exception {
        Flow flow = createFlow(message);
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
    public void testFlowMessage() throws Exception {
        ManagedMessage renderedMessage = new TestMessage("testFlowWithMessage");
        flowManager.beginFlow(renderedMessage, "test");
        Long flowId = renderedMessage.getFlowId();
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        String message = flowManager.findFlowMessageText(flowId);
        assertTrue(message.equals(renderedMessage.render()));

    }
    
    @Test
    public void testFlowPartMessage() throws Exception {
        boolean cleanUpEnabled = flowManager.isFlowCleanupEnabled("test");

        flowManager.setFlowCleanupEnabled("test", false);

        ManagedMessage renderedMessage = new TestMessage("testFlowWithMessage");
        flowManager.beginFlow(renderedMessage, "test");
        Long flowId = renderedMessage.getFlowId();
        flowManager.acknowledgeFlow(renderedMessage);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();

        String flowPartMessage = flowManager.findFlowPartMessageText(flowId,
                "0");
        assertTrue(flowPartMessage.equals(renderedMessage.render()));

        // restore the original cleanup.
        flowManager.setFlowCleanupEnabled("test", cleanUpEnabled);
    }
    
    @Test
    public void testFlowPartErrorAndAcknowledgedMessage() throws Exception {
        boolean cleanUpEnabled = flowManager.isFlowCleanupEnabled("test");

        flowManager.setFlowCleanupEnabled("test", false);

        ManagedMessage renderedMessage = new TestMessage("testFlowWithMessage");
        flowManager.beginFlow(renderedMessage, "test");
        Long flowId = renderedMessage.getFlowId();
        SplitHistory[] history = renderedMessage.getSplitHistory().split(2);
        TestMessage acknowledged = new TestMessage(
                "testFlowPartErrorAndAcknowledgedMessageAcknowledged");
        TestMessage invalidated = new TestMessage(
                "testFlowPartErrorAndAcknowledgedMessageInvalidated");
        acknowledged.setSplitHistory(history[0]);
        acknowledged.setFlowId(flowId);
        invalidated.setSplitHistory(history[1]);
        invalidated.setFlowId(flowId);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        flowManager.acknowledgeFlow(acknowledged);
        flowManager.invalidateFlow(invalidated);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        FlowPartInfo info = new FlowPartInfo();
        info.setPath(history[0].indexPathString());
        info.setStatus(FlowStatus.CLEAN.toString());
        String flowPartMessageAck = flowManager.findFlowPartMessageText(flowId,
                info.getPath());
        assertTrue(flowPartMessageAck.equals(acknowledged.render()));

        FlowPartInfo info1 = new FlowPartInfo();
        info1.setPath(history[1].indexPathString());
        info1.setStatus(FlowStatus.ERROR.toString());
        String flowPartMessageNak = flowManager.findFlowPartMessageText(flowId,
                info1.getPath());
        assertTrue(flowPartMessageNak.equals(invalidated.render()));

        // restore the original cleanup.
        flowManager.setFlowCleanupEnabled("test", cleanUpEnabled);
    }
    
    
    
    @Test
    public void testFlowPartErrorAndAcknowledgedMessageCleanup()
            throws Exception {
        boolean cleanUpEnabled = flowManager.isFlowCleanupEnabled("test");

        flowManager.setFlowCleanupEnabled("test", true);

        ManagedMessage renderedMessage = new TestMessage("testFlowWithMessage");
        flowManager.beginFlow(renderedMessage, "test", 2);
        Long flowId = renderedMessage.getFlowId();
        SplitHistory[] history = renderedMessage.getSplitHistory().split(2);
        TestMessage acknowledged1 = new TestMessage(
                "testFlowPartErrorAndAcknowledgedMessageAcknowledged");
        TestMessage acknowledged2 = new TestMessage(
                "testFlowPartErrorAndAcknowledgedMessageInvalidated");
        acknowledged1.setSplitHistory(history[0]);
        acknowledged1.setFlowId(flowId);
        acknowledged2.setSplitHistory(history[1]);
        acknowledged2.setFlowId(flowId);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        flowManager.acknowledgeFlow(acknowledged1);
        flowManager.acknowledgeFlow(acknowledged2);
        testTransactionManager.commitTransaction();

        testTransactionManager.beginTransaction();
        FlowPartInfo info = new FlowPartInfo();
        info.setPath(history[0].indexPathString());
        info.setStatus(FlowStatus.CLEAN.toString());
        String flowPartMessageAck = flowManager.findFlowPartMessageText(flowId,
                info.getPath());
        assertTrue(flowPartMessageAck.equals("N/A"));

        FlowPartInfo info1 = new FlowPartInfo();
        info1.setPath(history[1].indexPathString());
        info1.setStatus(FlowStatus.CLEAN.toString());
        String flowPartMessageNak = flowManager.findFlowPartMessageText(flowId,
                info1.getPath());
        assertTrue(flowPartMessageNak.equals("N/A"));

        // restore the original cleanup.
        flowManager.setFlowCleanupEnabled("test", cleanUpEnabled);
    }
    
    
    private Flow createFlow(ManagedMessage message) throws IOException {
        Flow flow = Flows.createFlow(message.createPacket());
        flowRepository.persist(flow);
        return flow;
    }

}
