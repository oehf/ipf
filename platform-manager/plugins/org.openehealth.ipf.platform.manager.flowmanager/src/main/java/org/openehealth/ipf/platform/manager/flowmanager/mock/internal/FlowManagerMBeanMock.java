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
package org.openehealth.ipf.platform.manager.flowmanager.mock.internal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerMBean;

/**
 * Basic implementation of the IFlowManagerMBean interface.
 * 
 * 
 * @author Mitko Kolev
 */
public class FlowManagerMBeanMock implements IFlowManagerMBean {

    private String maxFlows;

    private String application;

    private boolean isEnableCleaunup;

    private boolean isEnableFiltering;

    @Override
    public FlowInfo findFlow(long flowId) {
        FlowInfo info = new FlowInfo();
        info.setIdentifier(flowId);
        return info;
    }

    @Override
    public List<FlowInfo> findLastErrorFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        info.setNakCount(1);
        info.setStatus("ERROR");
        flows.add(info);
        return flows;
    }

    @Override
    public List<FlowInfo> findLastFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        flows.add(info);
        return flows;
    }

    @Override
    public List<FlowInfo> findLastUnackFlows(String last) {
        ArrayList<FlowInfo> flows = new ArrayList<FlowInfo>();
        FlowInfo info = new FlowInfo();
        info.setAckCountExpected(2);
        info.setAckCount(1);
        info.setStatus("CLEAN");
        flows.add(info);
        return flows;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public String getMaxFlows() {
        return maxFlows;
    }

    @Override
    public String getUpperTimeLimit() {
        return null;
    }

    @Override
    public boolean isEnableCleanup() {
        return isEnableCleaunup;
    }

    @Override
    public boolean isEnableFiltering() {
        return this.isEnableFiltering;
    }

    @Override
    public void replayFlow(long flowId) {

    }

    @Override
    public int replayLastErrorFlows(String last) {
        return 0;
    }

    @Override
    public int replayLastFlows(String last) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int replayLastUnackFlows(String last) {
        return 0;
    }

    @Override
    public void setApplication(String application) {
        this.application = application;
    }

    @Override
    public void setEnableCleanup(boolean enableCleanup) {
        this.isEnableCleaunup = enableCleanup;

    }

    @Override
    public void setEnableFiltering(boolean enableFiltering) {
        this.isEnableFiltering = enableFiltering;

    }

    @Override
    public void setMaxFlows(String maxFlows) {
        try {
            this.maxFlows = maxFlows;
            new Integer(maxFlows);
        } catch (NumberFormatException e) {
            throw e;
        }

    }

    @Override
    public void setUpperTimeLimit(String upperTimeLimit) throws ParseException {
    }

    @Override
    public void setUpperTimeLimitToCurrentTime() {
    }

    @Override
    public String findFlowMessageText(long flowId) {
        return null;
    }

    @Override
    public String findFlowPartMessageText(long flowId, String flowPartInfoPath) {
        return null;
    }

    @Override
    public List<FlowInfo> findLastErrorFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastErrorFlows(last);
    }

    @Override
    public List<FlowInfo> findLastFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastFlows(last);
    }

    @Override
    public List<FlowInfo> findLastUnackFlowsWithMessageText(String last,
            String searchExpression) {
        return findLastUnackFlows(last);
    }

}
