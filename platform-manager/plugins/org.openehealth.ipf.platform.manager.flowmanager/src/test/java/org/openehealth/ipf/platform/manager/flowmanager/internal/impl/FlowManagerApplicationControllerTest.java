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
package org.openehealth.ipf.platform.manager.flowmanager.internal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.openehealth.ipf.platform.manager.connection.ConnectionConfigurationImpl;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.mock.JMXConnectionManagerImplMock;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerApplicationControllerImpl;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerRepositorylImpl;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerSearchCriteriaImpl;
import org.openehealth.ipf.platform.manager.flowmanager.mock.FlowInfoMock;
import org.openehealth.ipf.platform.manager.flowmanager.mock.internal.FlowManagerApplicationControllerImplMock;

/**
 * 
 * @author Mitko Kolev
 */
public class FlowManagerApplicationControllerTest extends TestCase {

    private FlowManagerApplicationControllerImpl flowManagerApplicationController;

    private JMXConnectionManagerImplMock connectionManager;

    private IConnectionConfiguration targetConnectionConfiguration;

    private FlowManagerRepositorylImpl flowManagerRepository;

    @Override
    public void setUp() {
        connectionManager = new JMXConnectionManagerImplMock();
        flowManagerApplicationController = new FlowManagerApplicationControllerImplMock(
                connectionManager);
        flowManagerRepository = (FlowManagerRepositorylImpl) flowManagerApplicationController
                .getFlowManagerRepository();

        connectionManager.addObserver(flowManagerApplicationController);
        targetConnectionConfiguration = new ConnectionConfigurationImpl("new",
                "localhost", JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);

    }

    public void testSetMaxFlowsFlowManager() throws Exception {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertTrue(connectionManager.isConnected(connectionConfiguration));
        String flows = "1000";
        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration));
        flowManagerApplicationController.setMaxFlows(connectionConfiguration,
                flows);
        assertTrue(flowManagerRepository.getMaxFlows(connectionConfiguration)
                .equals(flows));

        // expect no exception
        flowManagerApplicationController.setMaxFlows(null, "1");

        // expect no exception at this point
        flowManagerApplicationController.setMaxFlows(connectionConfiguration,
                "asdfasdf");

    }

    public void testSetApplication() throws Exception {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertTrue(connectionManager.isConnected(connectionConfiguration));

        String application = "tutorial";
        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration));
        flowManagerApplicationController.setApplication(
                connectionConfiguration, application);
        assertTrue(flowManagerRepository
                .getApplication(connectionConfiguration).equals(application));

    }

    public void testSetCleanup() throws Exception {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertTrue(connectionManager.isConnected(connectionConfiguration));

        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration));
        boolean flowCleanup = flowManagerRepository
                .isEnabledCleanup(connectionConfiguration);
        flowManagerApplicationController
                .changeFlowCleanup(connectionConfiguration);
        assertTrue(flowManagerRepository
                .isEnabledCleanup(connectionConfiguration) != flowCleanup);

        // expect no exception
        flowManagerApplicationController.changeFlowCleanup(null);

    }

    // expect no exception
    public void testNullAware() {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);

        flowManagerApplicationController.setApplication(null, null);
        flowManagerApplicationController.setApplication(null, "a");
        flowManagerApplicationController.setApplication(
                connectionConfiguration, null);
        flowManagerApplicationController.setApplication(
                connectionConfiguration, "");

        // expect no exception
        flowManagerApplicationController.changeFlowCleanup(null);
        flowManagerApplicationController.replayFlows(null, null, null, null);
        flowManagerApplicationController.searchFlows(null,
                new FlowManagerSearchCriteriaImpl(new Date(System
                        .currentTimeMillis() / 10000), new Date(System
                        .currentTimeMillis()), true, false), null, null);

    }

    public void testSetFiltering() throws Exception {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertTrue(connectionManager.isConnected(connectionConfiguration));

        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration));
        boolean EnabledFiltering = flowManagerRepository
                .isEnabledFiltering(connectionConfiguration);
        flowManagerApplicationController
                .changeFlowFiltering(connectionConfiguration);
        assertTrue(flowManagerRepository
                .isEnabledFiltering(connectionConfiguration) != EnabledFiltering);

    }

    public void testReplayFlows() throws Exception {
        IConnectionConfiguration connectionConfiguration = connectionManager
                .getConnectionConfigurations().get(0);
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertTrue(connectionManager.isConnected(connectionConfiguration));
        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration));

        // replay an empty array
        // use null progress monitor
        flowManagerApplicationController.replayFlows(connectionConfiguration,
                new ArrayList<IFlowInfo>(), null, "task");

        // replay null array
        // use null progress monitor
        flowManagerApplicationController.replayFlows(connectionConfiguration,
                null, null, "task");

        FlowInfoMock fiMock = new FlowInfoMock();
        fiMock.setCreationTime(new Date());
        List<IFlowInfo> flows = new ArrayList<IFlowInfo>();
        flows.add(fiMock);

        // use null progress monitor
        flowManagerApplicationController.replayFlows(connectionConfiguration,
                flows, null, "task");

    }

    public void testConnectDisconnect() throws Exception {
        connectionManager
                .addConnectionConfiguration(targetConnectionConfiguration);

        connectionManager
                .openConnectionConfiguration(targetConnectionConfiguration);
        assertTrue(connectionManager.isConnected(targetConnectionConfiguration));
        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(targetConnectionConfiguration));

        connectionManager
                .closeConnectionConfiguration(targetConnectionConfiguration);
        assertTrue(!connectionManager
                .isConnected(targetConnectionConfiguration));
        assertTrue(!flowManagerRepository
                .isFlowManagerRegistered(targetConnectionConfiguration));

        // the other test with remove connection
        connectionManager
                .openConnectionConfiguration(targetConnectionConfiguration);
        assertTrue(connectionManager.isConnected(targetConnectionConfiguration));
        assertTrue(flowManagerRepository
                .isFlowManagerRegistered(targetConnectionConfiguration));

        // the other test with remove connection
        connectionManager
                .removeConnectionConfiguration(targetConnectionConfiguration);
        assertTrue(!connectionManager
                .isConnected(targetConnectionConfiguration));
        assertTrue(!flowManagerRepository
                .isFlowManagerRegistered(targetConnectionConfiguration));
        // restore the connection controller state.
        connectionManager
                .removeConnectionConfiguration(targetConnectionConfiguration);
    }

    public void testFlowInfo() throws Exception {
        connectionManager
                .addConnectionConfiguration(targetConnectionConfiguration);

        connectionManager
                .openConnectionConfiguration(targetConnectionConfiguration);
        assertTrue(connectionManager.isConnected(targetConnectionConfiguration));

        flowManagerApplicationController.searchFlows(
                targetConnectionConfiguration, null, new NullProgressMonitor(),
                "");
        List<IFlowInfo> flows = flowManagerRepository
                .getFlowInfos(targetConnectionConfiguration);
        assertTrue(flows.size() > 0);
        assertTrue(flows.get(0) instanceof IFlowInfo);
    }
}
