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

import java.util.List;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Place where all flow manager proxies are cached.
 * <p>
 * 
 * @author Mitko Kolev
 */
public interface IFlowManagerRepository {

    public void registerFlowManager(
            IConnectionConfiguration connectionConfiguration,
            IFlowManagerMBean flowManager);

    public void unRegisterFlowManager(
            IConnectionConfiguration connectionConfiguration);

    public boolean isFlowManagerRegistered(
            IConnectionConfiguration connectionConfiguration);

    public List<IFlowInfo> getFlowInfos(
            IConnectionConfiguration connectionConfiguration);

    public boolean isEnabledFiltering(
            IConnectionConfiguration connectionConfiguration);

    public String getApplication(
            IConnectionConfiguration connectionConfiguration);

    public String getMaxFlows(IConnectionConfiguration connectionConfiguration);

    public boolean isEnabledCleanup(
            IConnectionConfiguration connectionConfiguration);

    public void findFlowMessage(
            IConnectionConfiguration connectionConfiguration, Long flowId);

    /**
     * Searches for the message text of the flow with id flowId and
     * 
     * @param connectionConfiguration
     * @param flowId
     * @param flowPartInfo
     */
    public void findFlowPartMessage(
            IConnectionConfiguration connectionConfiguration, Long flowId,
            IFlowPartInfo flowPartInfo);

}
