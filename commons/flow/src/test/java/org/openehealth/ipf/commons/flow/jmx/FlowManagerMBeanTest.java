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
package org.openehealth.ipf.commons.flow.jmx;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowManagerFindCriteriaUnawareMock;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * @author Mitko Kolev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-jmx.xml" })
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class })
public class FlowManagerMBeanTest {

	@Autowired
	private FlowManagerFindCriteriaUnawareMock flowManager;

	@Autowired
	private FlowManagerMBean flowManagerMBean;

	public FlowManagerMBeanTest() {
	}

	@Before
	public void setUp() {
		flowManagerMBean.setApplication("test");
	}

	@Test
	public void testFlowManagerApplication() throws Exception {

		assertTrue(flowManagerMBean != null);
		assertTrue(flowManagerMBean.getApplication() != null);
		String application = flowManagerMBean.getApplication();
		flowManagerMBean.setApplication("test1");
		assertTrue(flowManagerMBean.getApplication().equals("test1"));
		// restore the original application
		flowManagerMBean.setApplication(application);
	}

	@Test
	public void testEnabledCleanup() {
		String originalApplication = flowManagerMBean.getApplication();

		boolean enabledCleanup = flowManagerMBean.isEnableCleanup();
		flowManagerMBean.setEnableCleanup(!enabledCleanup);
		assertTrue(flowManagerMBean.isEnableCleanup() == !enabledCleanup);
		flowManagerMBean.setEnableCleanup(false);
		assertTrue(flowManagerMBean.isEnableCleanup() == false);

		// new Application
		flowManagerMBean.setApplication("newAppplication");
		enabledCleanup = flowManagerMBean.isEnableCleanup();
		flowManagerMBean.setEnableCleanup(true);
		assertTrue(flowManagerMBean.isEnableCleanup() == true);

		flowManagerMBean.setApplication(originalApplication);
		assertTrue(flowManagerMBean.isEnableCleanup() == false);
	}

	public void testEnabledFiltering() {
		String originalApplication = flowManagerMBean.getApplication();

		boolean enabledFiltering = flowManagerMBean.isEnableFiltering();
		flowManagerMBean.setEnableFiltering(!enabledFiltering);
		assertTrue(flowManagerMBean.isEnableFiltering() == !enabledFiltering);
		flowManagerMBean.setEnableFiltering(false);
		assertTrue(flowManagerMBean.isEnableFiltering() == false);

		// new Application
		flowManagerMBean.setApplication("newAppplication");
		enabledFiltering = flowManagerMBean.isEnableFiltering();
		flowManagerMBean.setEnableCleanup(true);
		assertTrue(flowManagerMBean.isEnableFiltering() == true);

		flowManagerMBean.setApplication(originalApplication);
		assertTrue(flowManagerMBean.isEnableFiltering() == false);

	}

	@Test
	public void testMaxFlows() {
		flowManagerMBean.setMaxFlows("100");
		assertTrue(flowManagerMBean.getMaxFlows().equals("100"));
	}

	@Test
	public void testUpperTimeLimit() throws Exception {
		flowManagerMBean.setUpperTimeLimitToCurrentTime();
		String upperTimeLimit = flowManagerMBean.getUpperTimeLimit();
		assertTrue(upperTimeLimit != null);
		Date date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM).parse(flowManagerMBean.getUpperTimeLimit());
		assertTrue(date != null);

		Date dateNew = new Date();
		String formattedNew = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM).format(dateNew);
		flowManagerMBean.setUpperTimeLimit(formattedNew);
		upperTimeLimit = flowManagerMBean.getUpperTimeLimit();

		assertTrue(upperTimeLimit.equals(formattedNew));

	}

	// @Test
	public void testFindFlowById() throws Exception {
		TestMessage message = new TestMessage("Packet");
		Long id = flowManager.beginFlow(message, flowManagerMBean
				.getApplication());
		FlowInfo flowInfo = flowManagerMBean.findFlow(id);
		assertTrue(flowInfo.getIdentifier().equals(id));
	}

	@Test
	public void testReplayFlow() throws Exception {
		TestMessage message = new TestMessage("Packet error!");
		Long flowId = flowManager.beginFlow(message, flowManagerMBean
				.getApplication());
		flowManager.invalidateFlow(message);
		flowManagerMBean.replayFlow(flowId);
		FlowInfo flowInfo = flowManagerMBean.findFlow(flowId);
		assertTrue(flowInfo.getIdentifier().equals(flowId));
		assertTrue(flowInfo.getStatus().equals("CLEAN"));
	}
}
