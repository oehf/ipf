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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
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
    public synchronized void replayFlows(
            IConnectionConfiguration connectionConfiguration,
            List<IFlowInfo> flows, IProgressMonitor monitor, String taskName) {
        if (connectionConfiguration == null) {
            return;
        }
        if (flows == null)
            return;
        if (monitor == null)
            monitor = new NullProgressMonitor();
        log.info("Invoking replayFlows on connection "
                + connectionConfiguration);
        IFlowManagerMBean fm = flowManagerRepository
                .getFlowManager(connectionConfiguration);
        final int TOTAL_WORK = flows.size() + 1;
        try {
            if (fm != null) {
                monitor.beginTask(taskName != null ? taskName : "", TOTAL_WORK);
                for (IFlowInfo flow : flows) {
                    long flowId = flow.getIdentifier();
                    if (flow.isReplayable()) {
                        fm.replayFlow(flowId);
                    } else {
                        // System.out.println("flow with ID " + flowId + " is
                        // not replayable!");
                    }
                    // Thread.sleep(10);
                    monitor.worked(1);
                    if (monitor.isCanceled())
                        break;
                }

            }

            // finally search the flows to update the GUI
            if (lastSearchCriteria != null) {
                Thread.sleep(flowManagerRepository.getTimeoutOnReply());
                List<FlowInfo> infos = searchFlowsInternal(
                        connectionConfiguration, lastSearchCriteria, monitor,
                        false);
                if (infos != null) {
                    flowManagerRepository.registerFlows(
                            connectionConfiguration, infos);
                }
            }
        } catch (Throwable t) {
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, t);
        } finally {
            monitor.done();
        }
    }

    @Override
    public synchronized void searchFlows(
            IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria, IProgressMonitor monitor,
            String taskName) {

        if (connectionConfiguration == null) {
            return;
        }
        log.info("Invoking searchFlows on connection "
                + connectionConfiguration);
        final int TOTAL_WORK = 3;
        monitor.beginTask(taskName != null ? taskName : "", TOTAL_WORK);
        boolean updateProgress = true;
        // requires synchronization
        this.lastSearchCriteria = criteria;

        List<FlowInfo> flows = searchFlowsInternal(connectionConfiguration,
                criteria, monitor, updateProgress);
        if (flows != null) {
            flowManagerRepository.registerFlows(connectionConfiguration, flows);
        }

    }

    private synchronized List<FlowInfo> searchFlowsInternal(
            IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria, IProgressMonitor monitor,
            boolean updateProgress) {
        try {
            IFlowManagerMBean fm = flowManagerRepository
                    .getFlowManager(connectionConfiguration);
            if (fm != null) {
                DateFormat format = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.MEDIUM);

                // if the to Date is null, set the current date
                Date toDate = new Date(System.currentTimeMillis());
                if (criteria != null && criteria.getToDate() != null) {
                    toDate = criteria.getToDate();
                }
                long msInterval = toDate.getTime();
                if (criteria != null && criteria.getFromDate() != null) {
                    // this is the case where
                    msInterval = toDate.getTime()
                            - criteria.getFromDate().getTime();
                }
                String toDateString = format.format(toDate);
                fm.setUpperTimeLimit(toDateString);
                if (updateProgress) {
                    monitor.worked(1);
                }
                List<FlowInfo> flowsInfos = null;
                if (criteria == null) {
                    log.debug("Criteria are not restricted.");
                    flowsInfos = fm.findLastFlows(Long.toString(msInterval));
                } else if (criteria.isRestrictedToErrorFlows()) {
                    log.debug("Criteria are restricted to error flows.");
                    flowsInfos = fm.findLastErrorFlows(Long
                            .toString(msInterval));
                } else if (criteria.isRestrictedToUnacknowledgedFlows()) {
                    log.debug("Criteria are restricted ot UAK flows.");
                    flowsInfos = fm.findLastUnackFlows(Long
                            .toString(msInterval));
                } else {
                    log.debug("Criteria are not restricted.");
                    flowsInfos = fm.findLastFlows(Long.toString(msInterval));
                }
                if (updateProgress) {
                    monitor.worked(1);
                }
                // List<IFlowInfo> flows = buildFlows(flowsAsString);
                if (updateProgress) {
                    monitor.worked(1);
                }
                return flowsInfos;
            }

        } catch (Throwable e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.rmi.UnmarshalException) {
                log.error("UNmarshall Excpetion", e);
            }
            flowManagerRepository.handleUncheckedException(
                    connectionConfiguration, e);
            // do nothing
        }
        log.error("Returning no flows");
        return null;
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
