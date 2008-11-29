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
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * The FlowManager controller is observer of the connection repository It
 * describes the FlowManager application behavior.
 * 
 * It serves as a UI controller and offers functionality for the flow manager UI
 * application.
 * <p>
 * 
 * @author Mitko Kolev
 */
public interface IFlowManagerApplicationController extends Observer {

    public void addObserver(Observer flowRepositoryObserver);

    public void deleteObserver(Observer flowRepositoryObserver);

    public IFlowManagerRepository getFlowManagerRepository();

    public void onAttributeChangeNotification(
            IConnectionConfiguration connectionConfiguration,
            String attributeName, Object newValue, Object oldValue);

    public void setMaxFlows(IConnectionConfiguration connectionConfiguration,
            String maxFlows);

    public void setApplication(
            IConnectionConfiguration connectionConfiguration, String application);

    public void changeFlowFiltering(
            IConnectionConfiguration connectionConfiguration);

    public void changeFlowCleanup(
            IConnectionConfiguration connectionConfiguration);

    public void searchFlows(IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria, IProgressMonitor monitor,
            String taskName);

    /**
     * Flow Manager functionality.
     * 
     * @param connectionConfiguration
     *            the connection on which to replay the flows
     * @param flows
     *            the IFlowInfos to replay.
     * @param monitor
     *            reports the progress to the UI
     * @param taskName
     *            localized String for the replay task name.
     */
    public void replayFlows(IConnectionConfiguration connectionConfiguration,
            List<IFlowInfo> flows, IProgressMonitor monitor, String taskName);

    /**
     * Keeps track of the actively UI selected FlowManager in focus.
     * 
     * @param connectionConfigurationContext
     */
    public void onActiveFlowManagerChanged(
            IConnectionConfiguration connectionConfigurationContext);

    public void onSetTimeoutForReply(int timeout);
}
