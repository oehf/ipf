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
package org.openehealth.ipf.commons.flow.transfer;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.FlowManagerFindCriteriaUnawareMock;
import org.openehealth.ipf.commons.flow.impl.TestMessage;
import org.openehealth.ipf.commons.flow.jmx.FlowManagerMBean;
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
public class FlowPartInfoTest {

	@Autowired
	private FlowManagerFindCriteriaUnawareMock flowManager;

	@Autowired
	private FlowManagerMBean flowManagerMBean;

	public void setUp() {
		flowManagerMBean.setApplication("test");
	}

	@Test
	public void testPartInfo() {
		// create a flow with 2 parts
		Long flowId = flowManager.beginFlow(new TestMessage("bytes"),
				flowManagerMBean.getApplication(), 2);

		FlowInfo info = flowManager.findFlow(flowId);
		Set<FlowPartInfo> infos = info.getPartInfos();
		for (FlowPartInfo partInfo : infos) {
			assertTrue(partInfo.getPath() != null);
			assertTrue(partInfo.getFilterTime() == null);
			assertTrue(partInfo.getStatus().equals("CLEAN"));
			assertTrue(partInfo.getFilterCount() == 0);
			// make sure that the toString method will not yield an Exception
			assertTrue(!partInfo.toString().isEmpty());
		}

	}
}
