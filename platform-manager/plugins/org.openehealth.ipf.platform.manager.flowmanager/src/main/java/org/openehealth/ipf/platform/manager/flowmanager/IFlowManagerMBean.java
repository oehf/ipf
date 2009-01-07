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
package org.openehealth.ipf.platform.manager.flowmanager;

import java.text.ParseException;
import java.util.List;

import org.openehealth.ipf.commons.flow.transfer.FlowInfo;

/**
 * The interface will be removed when the FlowManagerMBean is refactored to be
 * an interface.
 * 
 * @author Mitko Kolev
 */
public interface IFlowManagerMBean {

    public String getApplication();

    public void setApplication(String application);

    public String getUpperTimeLimit();

    public void setUpperTimeLimit(String upperTimeLimit) throws ParseException;

    public String getMaxFlows();

    public void setMaxFlows(String maxFlows);

    public boolean isEnableFiltering();

    public void setEnableFiltering(boolean enableFiltering);

    public boolean isEnableCleanup();

    public void setEnableCleanup(boolean enableCleanup);

    public void setUpperTimeLimitToCurrentTime();

    public List<FlowInfo> findLastFlows(String last);

    public List<FlowInfo> findLastErrorFlows(String last);

    public List<FlowInfo> findLastUnackFlows(String last);

    public List<FlowInfo> findLastFlowsWithMessageText(String last,
            String searchExpression);

    public List<FlowInfo> findLastErrorFlowsWithMessageText(String last,
            String searchExpression);

    public List<FlowInfo> findLastUnackFlowsWithMessageText(String last,
            String searchExpression);

    public int replayLastFlows(String last);

    public int replayLastErrorFlows(String last);

    public int replayLastUnackFlows(String last);

    public FlowInfo findFlow(long flowId);

    public void replayFlow(long flowId);

    public String findFlowMessageText(long flowId);

    public String findFlowPartMessageText(long flowId, String flowPartInfoPath);
}
