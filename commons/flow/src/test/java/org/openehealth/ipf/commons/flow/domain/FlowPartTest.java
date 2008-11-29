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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;
import org.openehealth.ipf.commons.flow.tx.TestTransactionManager;
import org.openehealth.ipf.commons.flow.util.Flows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-tx-explicit.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowPartTest {
	@Autowired
	private TestTransactionManager testTransactionManager;

	@Before
	public void setUp() throws Exception {
		testTransactionManager.beginTransaction();
	}

	@After
	public void tearDown() throws Exception {
		testTransactionManager.commitTransaction();
	}

	@Test
	public void testFlowPartEquals() throws Exception {
		Flow flow = Flows.createFlowWithParts("packet", 2);
		Set<FlowPart> parts = flow.getParts();

		for (FlowPart part : parts) {
			assertTrue(part.equals(part));
			assertFalse(part.equals(null));
			FlowPart partNew = new FlowPart();
			assertFalse(part.equals(partNew));
			assertFalse(partNew.equals(part));
			// the part must always have identifier
			assertTrue(partNew.hashCode() != part.hashCode());
		}
	}

	@Test
	public void testFlowPartGetLatestUpdate() throws Exception {
		Flow flow = Flows.createFlowWithParts("packet", 2);
		Set<FlowPart> parts = flow.getParts();
		for (FlowPart part : parts) {
			part.setContributionTime(null);
			part.setFilterTime(null);
		}
		checkLatestUpdate(parts);
		for (FlowPart part : parts) {
			part.setContributionTime(new Date(System.currentTimeMillis()));
			part.setFilterTime(null);
		}
		checkLatestUpdate(parts);
		for (FlowPart part : parts) {
			part.setContributionTime(null);
			part.setFilterTime(new Date(System.currentTimeMillis()));
		}
		for (FlowPart part : parts) {
			part.setContributionTime(new Date(
					System.currentTimeMillis() - 10000));
			part.setFilterTime(new Date(System.currentTimeMillis()));
		}
		checkLatestUpdate(parts);

		for (FlowPart part : parts) {
			part.setContributionTime(new Date());
			part.setFilterTime(new Date(System.currentTimeMillis() + 10000));
		}
		checkLatestUpdate(parts);

		for (FlowPart part : parts) {
			part.setContributionTime(new Date(
					System.currentTimeMillis() + 10000));
			part.setFilterTime(new Date());
		}
		checkLatestUpdate(parts);

	}

	private void checkLatestUpdate(Set<FlowPart> parts) {
		for (FlowPart part : parts) {

			if (part.getLatestUpdate() != null) {
				assertTrue((part.getLatestUpdate().equals(part
						.getContributionTime()))
						|| part.getLatestUpdate().equals(part.getFilterTime()));
			} else {
				assertTrue(part.getContributionTime() == null);
				assertTrue(part.getFilterTime() == null);
			}
		}
	}

	@Test
	public void testFlowPartInfoSerialization() throws Exception {
		// creates flow with parts 0.0 and 0.1
		Flow flow = Flows.createFlowWithParts("packet", 2);
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		Set<FlowPart> parts = flow.getParts();
		for (FlowPart part : parts) {
			FlowPartInfo info = part.getInfo(flow);
			assertTrue(info.getContributionCount() == 0);
			assertTrue(info.getStatus().equals("CLEAN"));
			if (!(info.getPath().equals("0.1") || info.getPath().equals("0.0"))) {
				fail();
			}
		}
		testTransactionManager.commitTransaction();

		testTransactionManager.beginTransaction();
		flow.acknowledge("0.0", false);
		for (FlowPart part : parts) {
			FlowPartInfo info = part.getInfo(flow);
			if (info.getPath().equals("0.1")) {
				assertTrue(info.getStatus().equals("CLEAN"));
				assertTrue(info.getContributionCount() == 0);
			} else if (info.getPath().equals("0.0")) {
				assertTrue(info.getStatus().equals("CLEAN"));
				assertTrue(info.getContributionCount() == 1);
				assertTrue(info.getContributionTime() != null);
			}
		}

	}
}
