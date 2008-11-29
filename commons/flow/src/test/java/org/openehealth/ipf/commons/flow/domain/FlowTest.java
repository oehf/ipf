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
package org.openehealth.ipf.commons.flow.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowException;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.openehealth.ipf.commons.flow.util.Flows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowTest {

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
		testTransactionManager.commitTransaction();
	}

	@Test
	public void testCreateAndRollbackSingleFlow() throws Exception {
		Flow flow = persistFlow("flow1.binary");
		Flow flowReturned = flowRepository.find(flow.getIdentifier());
		assertTrue(Flows.deepEquals(flowReturned, flow));
		testTransactionManager.rollbackTransaction();

		// search for the flow
		testTransactionManager.beginTransaction();
		try {
			flowRepository.find(flow.getIdentifier());
			fail();
		} catch (Throwable t) {
			assertTrue(t instanceof FlowException);
		}
	}

	@Test
	public void testStatusCountWithInvalidate2TimesAndThenValidate()
			throws Exception {

		Flow flow = this.createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);

		// clear the fourth part
		for (FlowPart part : flow.getParts()) {
			if (part.getPath().equals("0.1.1")) {
				flow.getParts().remove(part);
				break;
			}
		}
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		// simulate fourth part fails
		flow.invalidate("0.1.1");
		testTransactionManager.commitTransaction();

		// update the error flow to error for the second time
		testTransactionManager.beginTransaction();
		int cleans = flow.getStatusCount(FlowStatus.CLEAN);
		int errors = flow.getStatusCount(FlowStatus.ERROR);
		assertTrue(cleans == 3);
		assertTrue(errors == 1);
		// this is the same as update("0.1.1", ERROR)
		flow.invalidate("0.1.1");
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		// the ERROR status must not be deleted at this point
		cleans = flow.getStatusCount(FlowStatus.CLEAN);
		errors = flow.getStatusCount(FlowStatus.ERROR);
		assertTrue(cleans == 3);
		assertTrue(errors == 1);
		testTransactionManager.commitTransaction();

		// the ERROR status must not be deleted at this point, and CLEANS must
		// be incremented
		testTransactionManager.beginTransaction();
		flow.acknowledge("0.1.1", false);
		errors = flow.getStatusCount(FlowStatus.ERROR);
		cleans = flow.getStatusCount(FlowStatus.CLEAN);
		assertTrue(cleans == 4);
		assertTrue(errors == 1);
	}

	@Test
	public void testPrepareReplayWithErrorParts() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.ERROR);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow.prepareReplay();
		int errorsAfterClean = flow.getStatusCount(FlowStatus.ERROR);
		int cleansAfterClean = flow.getStatusCount(FlowStatus.CLEAN);
		assertTrue(cleansAfterClean == 0);
		assertTrue(errorsAfterClean == 0);
	}

	@Test
	public void testClearErrorStatus() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.ERROR);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow.clearErrorStatus();

		int errorsAfterClean = flow.getStatusCount(FlowStatus.ERROR);
		int cleansAfterClean = flow.getStatusCount(FlowStatus.CLEAN);
		assertTrue(cleansAfterClean == 0);
		assertTrue(errorsAfterClean == 0);
	}

	@Test
	public void testCleanupMessageOnFinishedParts() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		assertTrue(flow.isReplayable());
		for (FlowPart part : flow.getParts()) {
			flow.acknowledge(part.getPath(), true);// use cleanup
		}
		assertFalse(flow.isReplayable());
	}

	@Test
	public void testFlowInfoProperties() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		assertTrue(flow.isReplayable());
		FlowInfo info = flow.getInfo();
		assertFlowAndFlowInfoEqual(flow, info);

		List<Flow> flows = new ArrayList<Flow>();
		flows.add(flow);
		List<FlowInfo> infos = Flow.getInfos(flows);
		for (FlowInfo i : infos) {
			assertFlowAndFlowInfoEqual(flow, i);
		}
	}

	@Test
	public void testHashCodeAndEquals() throws IOException {
		Flow flow1 = createFlowWith4Parts(FlowStatus.CLEAN);
		flow1.setAckCountExpected(4);

		Flow flow2 = createFlowWith4Parts(FlowStatus.CLEAN);
		flow2.setAckCountExpected(4);

		// Hibernate will not behave properly if it assigns the ids!
		assertTrue(flow1.getIdentifier() != null);
		assertTrue(flow2.getIdentifier() != null);

		// test hashcode and equals
		assertTrue(flow1.equals(flow1));
		assertFalse(flow1.equals(flow1.getIdentifier()));
		assertFalse(flow1.equals(flow2));
		assertFalse(flow2.equals(flow1));
		assertFalse(flow1.hashCode() == flow2.hashCode());
	}

	private void assertFlowAndFlowInfoEqual(Flow flow, FlowInfo info) {
		assertTrue(info.getCreationTime().equals(flow.getCreationTime()));
		assertTrue(info.getApplication().equals(flow.getApplication()));
		assertTrue(info.getIdentifier().equals(flow.getIdentifier()));
		assertTrue(info.getReplayCount() == flow.getReplayCount());
		assertTrue(info.getStatus().equals(flow.getStatus().toString()));
	}

	@Test
	public void testDoNotCleanupMessageOnFinishedParts() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		assertTrue(flow.isReplayable());
		for (FlowPart part : flow.getParts()) {
			flow.acknowledge(part.getPath(), false);// do not use cleanup
		}
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow = flowRepository.find(flow.getIdentifier());
		assertTrue(flow.isReplayable());
		int errorsAfterAcknowledge = flow.getStatusCount(FlowStatus.ERROR);
		int cleansAfterAcknowledge = flow.getStatusCount(FlowStatus.CLEAN);
		assertTrue(cleansAfterAcknowledge == 4);
		assertTrue(errorsAfterAcknowledge == 0);
	}

	@Test
	public void testPrepareReplayWithCleanParts() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow.prepareReplay();
		flowRepository.merge(flow);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		int errorsAfterClean = flow.getStatusCount(FlowStatus.ERROR);
		int cleansAfterClean = flow.getStatusCount(FlowStatus.CLEAN);
		assertTrue(cleansAfterClean == 4);
		assertTrue(errorsAfterClean == 0);
	}

	@Test
	public void testFilterError() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.ERROR);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		for (FlowPart part : flow.getParts()) {
			assertFalse(flow.filter(part.getPath()));
			assertTrue(part.getFilterCount() == 0);
		}
	}

	@Test
	public void testIsAckCountExpectedReached() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		assertTrue(flow.isAckCountExpectationSet());
		assertTrue(flow.isAckCountExpectedReached());
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow = createFlowWith4Parts(FlowStatus.ERROR);
		flow.setAckCountExpected(4);
		assertTrue(flow.isAckCountExpectationSet());
		assertFalse(flow.isAckCountExpectedReached());
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow = createFlowWith4Parts(FlowStatus.ERROR);
		flow.setAckCountExpected(-1);
		assertFalse(flow.isAckCountExpectationSet());
		assertFalse(flow.isAckCountExpectedReached());
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(-1);
		assertFalse(flow.isAckCountExpectationSet());
		assertFalse(flow.isAckCountExpectedReached());
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(0);
		assertTrue(flow.isAckCountExpectationSet());
		assertTrue(flow.isAckCountExpectedReached());

	}

	@Test
	public void testFilterClean() throws Exception {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		for (FlowPart part : flow.getParts()) {
			assertTrue(flow.filter(part.getPath()));
			assertTrue(part.getFilterCount() == 1);
		}

	}

	@Test
	public void testGetLatestUpdateIsNotNull() throws IOException {
		Flow flow = createFlowWith4Parts(FlowStatus.CLEAN);
		flow.setAckCountExpected(4);
		assertTrue(flow.getLatestUpdate() != null);

		flow.acknowledge("0.1.1", true);
		assertTrue(flow
				.getPartDuration(flow.getPart("0.0.1", FlowStatus.CLEAN)) >= 0);

	}
	
	private Flow createFlowWith4Parts(FlowStatus status) throws IOException {
		String packet = "Initial Packet";
		Flow flow = persistFlow(packet);
		flow.getParts().clear();
		int cleans = flow.getStatusCount(status);
		assertTrue(cleans == 0);
		// also create the part
		flow.update("0.0.0", status);
		cleans = flow.getStatusCount(status);
		assertTrue(cleans == 1);

		flow.update("0.0.1", status);
		cleans = flow.getStatusCount(status);
		assertTrue(cleans == 2);

		flow.update("0.1.0", status);
		cleans = flow.getStatusCount(status);
		assertTrue(cleans == 3);

		flow.update("0.1.1", status);
		cleans = flow.getStatusCount(status);
		assertTrue(cleans == 4);
		return flow;
	}

	private Flow persistFlow(String packet) throws IOException {
		Flow flow = Flows.createFlow(packet);
		flowRepository.persist(flow);
		return flow;
	}
	@Test
    public void testGetPartsByPath() throws IOException {
        Flow flow = this.createFlowWith4Parts(FlowStatus.CLEAN);
        flow.setAckCountExpected(4);
        assertTrue("This method must not create parts", flow
                .getPart("non-existing-part-path") == null);
        assertTrue("This method must be null friendly",
                flow.getPart(null) == null);
        for (FlowPart part : flow.getParts()) {
            assertTrue("Must return the exising parts correctly", part
                    .equals(flow.getPart(part.getPath())));
        }

    }
}
