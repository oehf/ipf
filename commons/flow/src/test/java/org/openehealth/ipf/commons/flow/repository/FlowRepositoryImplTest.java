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
package org.openehealth.ipf.commons.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;
import static org.openehealth.ipf.commons.flow.util.Flows.createFlow;
import static org.openehealth.ipf.commons.flow.util.Flows.createFlowPart;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowException;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class FlowRepositoryImplTest {

    @Autowired
    private TestTransactionManager testTransactionManager;
    
    @Autowired
    private FlowRepository flowRepository;
    
    @Before
    public void setUp() throws Exception {
        testTransactionManager.beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        testTransactionManager.endTransaction();
    }

    @Test
    public void testFind() throws Exception {
        Flow flow = createFlow("blah");
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        try {
            flowRepository.find(0L);
            fail("found non-existing flow");
        } catch (FlowException e) {
            // expected
        }
        flow = flowRepository.find(id);
        assertEquals(CLEAN, flow.getStatus());
        assertEquals("blah", new String(flow.getPacket()));
        assertNotNull(flow.getPart("0.0", CLEAN));
        assertNotNull(flow.getPart("0.1", CLEAN));
    }
    
    @Test
    public void testLock() throws Exception {
        Flow flow = createFlow("blah");
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        try {
            flowRepository.lock(0L);
            fail("found non-existing flow");
        } catch (FlowException e) {
            // expected
        }
        flow = flowRepository.lock(id);
        flow.setAckCountExpected(111);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertEquals(111, flow.getAckCountExpected());
    }
    
    @Test
    public void testMerge() throws Exception {
        Flow flow = createFlow("blah");
        Date timestamp = flow.getCreationTime();
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        FlowPart flowPart = createFlowPart("0.2");
        flowPart.setStatus(ERROR);
        flow.incrementReplayCount();
        flow.getParts().add(flowPart);
        flow.setPacket("blub".getBytes());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertEquals(ERROR, flow.getStatus());
        assertEquals(1, flow.getReplayCount());
        assertEquals(timestamp, flow.getCreationTime());
        assertEquals("blub", new String(flow.getPacket()));
        assertNotNull(flow.getPart("0.2", ERROR));
    }
    
    @Test
    public void testDelete() throws Exception {
        Flow flow = createFlow("blah");
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        flow = flowRepository.find(id);
        assertNotNull(flow);
        flowRepository.remove(flow);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        try {
            flowRepository.find(id);
            fail("found deleted flow");
        } catch (FlowException e) {
            // expected
        }
    }
    
    @Test
    public void testAcknowledgeCleanup() throws Exception {
        Flow flow = createFlow("blah".getBytes(), false);
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        flow = flowRepository.find(id);
        flow.setAckCountExpected(2);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.0", true);
        assertFalse(flow.isAckCountExpectedReached());
        assertTrue(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.1", true);
        assertTrue(flow.isAckCountExpectedReached());
        assertFalse(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertFalse(flow.isReplayable());
    }
    
    @Test
    public void testAcknowledgeNoCleanup() throws Exception {
        Flow flow = createFlow("blah".getBytes(), false);
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        flow = flowRepository.find(id);
        flow.setAckCountExpected(2);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.0", false);
        assertFalse(flow.isAckCountExpectedReached());
        assertTrue(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.1", false);
        assertTrue(flow.isAckCountExpectedReached());
        assertTrue(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertTrue(flow.isReplayable());
    }
    
    @Test
    public void testAcknowledgeNoExpectation() throws Exception {
        Flow flow = createFlow("blah".getBytes(), false);
        flowRepository.persist(flow);
        Long id = flow.getIdentifier();
        flow = flowRepository.find(id);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.0", true);
        assertFalse(flow.isAckCountExpectedReached());
        assertTrue(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        flow.acknowledge("0.1", true);
        assertFalse(flow.isAckCountExpectedReached());
        assertTrue(flow.isReplayable());
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flow = flowRepository.find(id);
        assertTrue(flow.isReplayable());
    }
    
    @Test
    public void testFindFlowIdsVarSince() throws Exception {
        Thread.sleep(2L); // isolate from other tests
        Flow flow = persistFlow("blah");
        Date d1 = flow.getCreationTime();
        Date d2 = new Date(d1.getTime() + 1L);
        Date d3 = new Date(d1.getTime() - 1L);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<Long> ids = flowRepository.findFlowIds(new FlowFinderCriteria(d1, null, "test"));
        assertEquals(1, ids.size());
        assertTrue(ids.contains(flow.getIdentifier()));
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(d1, d1, "test"));
        assertEquals(1, ids.size());
        assertTrue(ids.contains(flow.getIdentifier()));
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(d1, d2, "test"));
        assertEquals(1, ids.size());
        assertTrue(ids.contains(flow.getIdentifier()));
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(d1, d3, "test"));
        assertEquals(0, ids.size());
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(d1, null, "wrong"));
        assertEquals(0, ids.size());
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(d2, null, "test"));
        assertEquals(0, ids.size());
    }
    
    @Test
    public void testFindErrorUnackFlowIds() throws Exception {
        Thread.sleep(2L); // isolate from other tests
        Date since = new Date();
        Flow flow1 = persistFlow("blah");
        Flow flow2 = persistFlow("blah");
        Flow flow3 = persistFlow("blah");
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<Long> ids = flowRepository.findErrorFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(0, ids.size());
        ids = flowRepository.findUnackFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(0, ids.size());
        flowRepository.find(flow2.getIdentifier()).getPart("0.0", CLEAN).setStatus(ERROR);
        flowRepository.find(flow3.getIdentifier()).getParts().clear();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        ids = flowRepository.findErrorFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(1, ids.size());
        ids = flowRepository.findErrorFlowIds(new FlowFinderCriteria(since, null, null));
        assertEquals(1, ids.size());
        assertTrue(ids.contains(flow2.getIdentifier()));
        ids = flowRepository.findUnackFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(1, ids.size());
        assertTrue(ids.contains(flow3.getIdentifier()));
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(3, ids.size());
        assertTrue(ids.contains(flow1.getIdentifier()));
        assertTrue(ids.contains(flow2.getIdentifier()));
        assertTrue(ids.contains(flow3.getIdentifier()));
    }
    
    @Test
    public void testFindMaxResults() throws Exception {
        Thread.sleep(2L); // isolate from other tests
        Date since = new Date();
        persistFlow("blah");
        persistFlow("blah");
        persistFlow("blah");
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<Long> ids = flowRepository.findFlowIds(new FlowFinderCriteria(since, null, "test"));
        assertEquals(3, ids.size());
        ids = flowRepository.findFlowIds(new FlowFinderCriteria(since, null, "test", 2));
        assertEquals(2, ids.size());
    }

    @Test
    public void testFindFlowsVarSince() throws Exception {
        Thread.sleep(2L); // isolate from other tests
        Flow flow = persistFlow("blah");
        Date d1 = flow.getCreationTime();
        Date d2 = new Date(d1.getTime() + 1L);
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<Flow> flows = flowRepository.findFlows(new FlowFinderCriteria(d1, null, "test"));
        assertEquals(1, flows.size());
        assertTrue(flows.contains(flow));
        flows = flowRepository.findFlows(new FlowFinderCriteria(d1, null, "wrong"));
        assertEquals(0, flows.size());
        flows = flowRepository.findFlows(new FlowFinderCriteria(d2, null, "test"));
        assertEquals(0, flows.size());
    }
    
    @Test
    public void testFindErrorUnackFlows() throws Exception {
        Thread.sleep(2L); // isolate from other tests
        Date since = new Date();
        Flow flow1 = persistFlow("blah");
        Flow flow2 = persistFlow("blah");
        Flow flow3 = persistFlow("blah");
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        List<Flow> flows = flowRepository.findErrorFlows(new FlowFinderCriteria(since, null, "test"));
        assertEquals(0, flows.size());
        flows = flowRepository.findUnackFlows(new FlowFinderCriteria(since, null, "test"));
        assertEquals(0, flows.size());
        flowRepository.find(flow2.getIdentifier()).getPart("0.0", CLEAN).setStatus(ERROR);
        flowRepository.find(flow3.getIdentifier()).getParts().clear();
        testTransactionManager.commitTransaction();
        testTransactionManager.beginTransaction();
        flows = flowRepository.findErrorFlows(new FlowFinderCriteria(since, null, "test"));
        assertEquals(1, flows.size());
        assertTrue(flows.contains(flow2));
        flows = flowRepository.findUnackFlows(new FlowFinderCriteria(since, null, "test"));
        assertEquals(1, flows.size());
        assertTrue(flows.contains(flow3));
        flows = flowRepository.findFlows(new FlowFinderCriteria(since, null, "test"));
        assertEquals(3, flows.size());
        assertTrue(flows.contains(flow1));
        assertTrue(flows.contains(flow2));
        assertTrue(flows.contains(flow3));
    }
    
    private Flow persistFlow(String packet) throws IOException {
        Flow flow = createFlow(packet);
        flowRepository.persist(flow);
        return flow;
    }
    
}
