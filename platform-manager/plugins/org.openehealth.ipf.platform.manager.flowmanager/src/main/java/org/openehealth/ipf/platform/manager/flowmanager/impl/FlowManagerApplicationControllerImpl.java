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
package org.openehealth.ipf.platform.manager.flowmanager.impl;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerMBean;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerRepository;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerSearchCriteria;

/**
 * The class is used when a single instance of a FloManager is to be modified.
 * <p>
 * 
 * TODO: The search flows method should be moved to the FlowRepository. TODO:
 * TODO: Add proper handling to date parse error on search (if the server
 * implementation fails to parse the date). currently the validation of the date
 * is done in the client UI.
 * 
 * @author Mitko Kolev
 */
public class FlowManagerApplicationControllerImpl implements
        IFlowManagerApplicationController {

    private final FlowManagerRepositorylImpl flowManagerRepository;

    private IFlowManagerSearchCriteria lastSearchCriteria;

    private final Log log = LogFactory
            .getLog(FlowManagerApplicationControllerImpl.class);

    public FlowManagerApplicationControllerImpl(
            IJMXConnectionManager jMXConnectionManager,
            FlowManagerRepositorylImpl repository) {
        this.flowManagerRepository = repository;
    }

    @Override
    public void addObserver(Observer flowObserver) {
        flowManagerRepository.addObserver(flowObserver);
    }

    @Override
    public void deleteObserver(Observer flowObserver) {
        flowManagerRepository.deleteObserver(flowObserver);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ConnectionEvent) {
            ConnectionEvent event = (ConnectionEvent) arg;
            IConnectionConfiguration connectionConfiguration = event
                    .getConnectionConfigurationContext();
            if (event.getType() == ConnectionEvent.JMX_CONNECTION_OPEN) {
                if (flowManagerRepository
                        .isFlowManagerRegistered(connectionConfiguration)) {
                    // update the stale reference
                    flowManagerRepository
                            .unRegisterFlowManager(connectionConfiguration);
                }
                log.info("Loading and Register FlowManager");
                loadAndRegisterFlowManager(connectionConfiguration);
                // update observers
            } else if (event.getType() == ConnectionEvent.JMX_CONNECTION_CLOSED) {
                log.debug("Unregistering FlowManager.");
                flowManagerRepository
                        .unRegisterFlowManager(connectionConfiguration);

            } else if (event.getType() == ConnectionEvent.CONNECTION_REMOVED) {
                // unregistter the flow Manager adapter
                flowManagerRepository
                        .unRegisterFlowManager(connectionConfiguration);
            }
        }
    }

    /**
     * Creates a flowManagerInstance and registers it in the repository. After
     * the connection has been closed a call must be made to connectionClosed
     * 
     * @param connectionConfiguration
     */
    private void loadAndRegisterFlowManager(
            IConnectionConfiguration connectionConfiguration) {
        FlowNotificationListener flowListener = new FlowNotificationListener(
                this, connectionConfiguration);
        try {
            IFlowManagerMBean flowManager = flowManagerRepository
                    .loadFlowManager(connectionConfiguration, flowListener);
            if (flowManager != null) {
                flowManagerRepository.registerFlowManager(
                        connectionConfiguration, flowManager);
            }
        } catch (Throwable e) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
        }
    }

    public IFlowManagerRepository getFlowManagerRepository() {
        return flowManagerRepository;
    }

    public void setMaxFlows(IConnectionConfiguration connectionConfiguration,
            String maxFlows) {
        if (connectionConfiguration == null) {
            return;
        }
        log
                .info("Settings max flows for connection "
                        + connectionConfiguration);
        try {
            flowManagerRepository
                    .setMaxFlows(connectionConfiguration, maxFlows);
        } catch (Throwable e) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
        }
    }

    @Override
    public void setApplication(
            IConnectionConfiguration connectionConfiguration, String application) {
        if (connectionConfiguration == null) {
            return;
        }
        log.info("Invoking setApplication on connection "
                + connectionConfiguration);
        try {
            flowManagerRepository.setApplication(connectionConfiguration,
                    application);
        } catch (Throwable e) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
        }
    }

    @Override
    public void changeFlowFiltering(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null) {
            return;
        }
        log.info("Invoking changeFlowFiltering on connection "
                + connectionConfiguration);
        try {
            flowManagerRepository.changeFlowFiltering(connectionConfiguration);
        } catch (Throwable e) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
        }
    }

    @Override
    public void changeFlowCleanup(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null) {
            return;
        }
        log.info("Invoking changeFlowCleanup on connection "
                + connectionConfiguration);
        try {
            flowManagerRepository.changeFlowCleanup(connectionConfiguration);
        } catch (Throwable t) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, t);
        }

    }

    @Override
    public void replayFlows(IConnectionConfiguration connectionConfiguration,
            List<IFlowInfo> flows, IProgressMonitor monitor, String taskName) {
        if (connectionConfiguration == null) {
            return;
        }
        if (flows == null)
            return;
        if (monitor == null)
            monitor = new NullProgressMonitor();
        try {
            monitor.beginTask(taskName != null ? taskName : "",
                    IProgressMonitor.UNKNOWN);
            flowManagerRepository.replayFlows(connectionConfiguration, flows,
                    lastSearchCriteria);
        } catch (Throwable t) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, t);
        } finally {
            monitor.done();
        }
    }

    @Override
    public void searchFlows(IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria, IProgressMonitor monitor,
            String taskName) {

        if (connectionConfiguration == null) {
            return;
        }
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        try {
            log.info("Invoking searchFlows on connection "
                    + connectionConfiguration);
            monitor.beginTask(taskName != null ? taskName : "",
                    IProgressMonitor.UNKNOWN);
            // requires synchronization
            this.lastSearchCriteria = criteria;
            flowManagerRepository
                    .searchFlows(connectionConfiguration, criteria);

        } catch (Throwable e) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
        } finally {
            monitor.done();
        }
    }

    @Override
    public void onAttributeChangeNotification(
            IConnectionConfiguration connectionConfiguration,
            String attributeName, Object newValue, Object oldValue) {
        if (attributeName == null || connectionConfiguration == null) {
            return;
        }
        flowManagerRepository.handleAttributeNotification(
                connectionConfiguration, attributeName, newValue, oldValue);
    }

    @Override
    public void onActiveFlowManagerChanged(
            IConnectionConfiguration connectionConfiguration) {
        flowManagerRepository.setActiveFlowManager(connectionConfiguration);

    }

    @Override
    public void onSetTimeoutForReply(int timeout) {
        flowManagerRepository.setTimeoutOnReply(timeout);

    }
}
