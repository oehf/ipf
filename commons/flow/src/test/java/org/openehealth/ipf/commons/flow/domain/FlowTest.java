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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;
import static org.openehealth.ipf.commons.flow.util.Flows.createFlow;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Mitko Kolev
 * @author Martin Krasser 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowTest {

    @Test
    public void testStatusCount()throws Exception {
        Flow flow = createFlow("blah");
        flow.setAckCountExpected(3);
        flow.invalidate("0.2");
        assertEquals(2, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
        flow.invalidate("0.2");
        assertEquals(2, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
        flow.acknowledge("0.2", false);
        assertEquals(3, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
    }

	@Test
	public void testPrepareReplayWithErrorParts() throws Exception {
	    Flow flow = createFlow("blah", 2);
	    flow.getPart("0.0").setStatus(ERROR);
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
		flow.prepareReplay();
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
	}

	@Test
	public void testClearErrorStatus() throws Exception {
        Flow flow = createFlow("blah", 2);
        flow.getPart("0.0").setStatus(ERROR);
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
		flow.clearErrorStatus();
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
	}

	@Test
	public void testIsReplayable() throws Exception {
        Flow flow = createFlow("blah", 1);
        flow.setAckCountExpected(1);
		assertTrue(flow.isReplayable());
		flow.acknowledge("0.0", true);
		assertFalse(flow.isReplayable());
	}

	@Test
	public void testFlowInfoProperties() throws Exception {
		Flow flow = createFlow("blah", 1);
		FlowInfo info = flow.getInfo();
		assertEquals(flow.getCreationTime(), info.getCreationTime());
        assertEquals(flow.getApplication(), info.getApplication());
        assertEquals(flow.getIdentifier(), info.getIdentifier());
        assertEquals(flow.getReplayCount(), info.getReplayCount());
        assertEquals(flow.getStatus().toString(), info.getStatus());
	}

	@Test
	public void testHashCodeAndEquals() throws IOException {
		Flow flow1 = createFlow("blah");
        Flow flow2 = createFlow("blub");
        flow1.setIdentifier(1L);
        flow2.setIdentifier(1L);
        assertEquals(flow1, flow2);
        assertEquals(flow1.hashCode(), flow2.hashCode());
	}

	@Test
	public void testAcknowledge() throws Exception {
	    Flow flow = createFlow("blah", 0);
	    assertEquals(0, flow.getStatusCount(CLEAN));
        flow.acknowledge("0.0", false);
	    flow.acknowledge("0.1", false);
        assertEquals(2, flow.getStatusCount(CLEAN));
	}

	@Test
	public void testPrepareReplay() throws Exception {
        Flow flow = createFlow("blah");
        flow.getPart("0.0").setStatus(ERROR);
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(1, flow.getStatusCount(ERROR));
        flow.prepareReplay();
        assertEquals(1, flow.getStatusCount(CLEAN));
        assertEquals(0, flow.getStatusCount(ERROR));
	}

	@Test
	public void testFilter() throws Exception {
        Flow flow = createFlow("blah");
        flow.getPart("0.0").setStatus(ERROR);
        assertEquals(0, flow.getPart("0.0").getFilterCount());
        assertEquals(0, flow.getPart("0.1").getFilterCount());
        assertFalse(flow.filter("0.0"));
        assertTrue(flow.filter("0.1"));
        assertEquals(0, flow.getPart("0.0").getFilterCount());
        assertEquals(1, flow.getPart("0.1").getFilterCount());
	}

	@Test
	public void testIsAckCountExpectedReached() throws Exception {
        Flow flow = createFlow("blah", 0);
		flow.setAckCountExpected(2);
		assertFalse(flow.isAckCountExpectedReached());
		flow.acknowledge("0.0", false);
        assertFalse(flow.isAckCountExpectedReached());
        flow.acknowledge("0.1", false);
        assertTrue(flow.isAckCountExpectedReached());
        flow.setAckCountExpected(FlowInfo.ACK_COUNT_EXPECTED_UNDEFINED);
        assertFalse(flow.isAckCountExpectedReached());
	}

	@Test
	public void testGetLatestUpdate() throws IOException {
		Flow flow = createFlow("blah");
		Date date1 = new Date(1000L);
		flow.setCreationTime(date1);
		assertEquals(date1, flow.getLatestUpdate());
        Date date2 = new Date(2000L);
        flow.setReplayTime(date2);
        assertEquals(date2, flow.getLatestUpdate());
	}
	
	@Test
    public void testGetNullPart() throws IOException {
        Flow flow = createFlow("blah");
        assertNull(flow.getPart("non-existing-part-path"));
        assertNull(flow.getPart(null));
    }
	
}
