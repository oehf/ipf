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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openehealth.ipf.commons.flow.jmx.FlowManagerMBean;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowInfoFinderCriteria;
import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;

/**
 * @author Mitko Kolev
 */
public class FlowManagerFindCriteriaUnawareMock extends FlowManagerMock {

	private final HashMap<Long, FlowInfo> flows;
	private long flowID;
	private final HashMap<String, Boolean> enabledCleanup = new HashMap<String, Boolean>();
	private final HashMap<String, Boolean> enabledFiltering = new HashMap<String, Boolean>();

	public FlowManagerFindCriteriaUnawareMock() {
		flows = new HashMap<Long, FlowInfo>();
		flowID = 0;
	}

	@Override
	public void acknowledgeFlow(ManagedMessage managedMessage) {

	}

	@Override
	public Long beginFlow(ManagedMessage managedMessage, String application) {
		FlowInfo info = new FlowInfo();
		info.setIdentifier(++flowID);
		info.setStatus("CLEAN");
		info.setNakCount(0);
		info.setCreationTime(new Date());
		info.setApplication(application);
		managedMessage.setFlowId(flowID);
		flows.put(flowID, info);
		return flowID;
	}

	@Override
	public Long beginFlow(ManagedMessage managedMessage, String application,
			int ackCountExpected) {
		FlowInfo info = new FlowInfo();
		info.setIdentifier(++flowID);
		info.setStatus("CLEAN");
		info.setNakCount(0);
		info.setCreationTime(new Date());
		info.setAckCountExpected(ackCountExpected);
		if (ackCountExpected > 0) {
			for (int t = 0; t < ackCountExpected; t++) {
				FlowPartInfo partInfo = new FlowPartInfo();
				partInfo.setPath("0." + t);
				partInfo.setStatus("CLEAN");
				info.getPartInfos().add(partInfo);
			}
		}
		info.setApplication(application);
		flows.put(flowID, info);
		managedMessage.setFlowId(flowID);
		return flowID;
	}

	@Override
	public boolean filterFlow(ManagedMessage managedMessage) {
		return false;
	}

	@Override
	public List<Long> findErrorFlowIds(FlowInfoFinderCriteria finderCriteria) {
		return new ArrayList<Long>();
	}

	@Override
	public List<FlowInfo> findErrorFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public FlowInfo findFlow(Long flowId) {
		return flows.get(flowId);
	}

	@Override
	public List<Long> findFlowIds(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<FlowInfo> findFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<Long> findUnackFlowIds(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<FlowInfo> findUnackFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public boolean flowCompleted(Long flowId) {
		return false;
	}

	@Override
	public void invalidateFlow(ManagedMessage managedMessage) {
		FlowInfo info = flows.get(managedMessage.getFlowId());
		info.setNakCount(info.getNakCount() + 1);
		info.setStatus("ERROR");
		for (FlowPartInfo partInfo : info.getPartInfos()) {
			partInfo.setStatus("ERROR");
		}
	}

	@Override
	public boolean isFlowCleanupEnabled(String application) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		assertTrue(elements[2].getClassName().equals(
				FlowManagerMBean.class.getName()));
		assertTrue(elements[2].getMethodName().equals("isEnableCleanup"));
		if (!enabledCleanup.containsKey(application))
			return false;
		return enabledCleanup.get(application);

	}

	@Override
	public boolean isFlowFilterEnabled(String application) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		assertTrue(elements[2].getClassName().equals(
				FlowManagerMBean.class.getName()));
		assertTrue(elements[2].getMethodName().equals("isEnableFiltering"));
		if (!enabledFiltering.containsKey(application))
			return false;
		return enabledFiltering.get(application);
	}

	@Override
	public int replayErrorFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void replayFlow(Long flowId) {
		FlowInfo info = flows.get(flowId);
		info.setReplayCount(info.getReplayCount() + 1);
		info.setStatus("CLEAN");
		info.setAckCount(0);
		info.setNakCount(0);
		info.setReplayTime(new Date());
	}

	@Override
	public int replayFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public int replayUnackFlows(FlowInfoFinderCriteria finderCriteria) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void setFlowCleanupEnabled(String application,
			boolean flowCleanupEnabled) {
		if (this.enabledCleanup.containsKey(application)) {
			this.enabledCleanup.remove(application);
		}
		this.enabledCleanup.put(application, flowCleanupEnabled);

	}

	@Override
	public void setFlowFilterEnabled(String application,
			boolean flowFilterEnabled) {
		if (this.enabledFiltering.containsKey(application)) {
			this.enabledFiltering.remove(application);
		}
		this.enabledFiltering.put(application, flowFilterEnabled);

	}

}
