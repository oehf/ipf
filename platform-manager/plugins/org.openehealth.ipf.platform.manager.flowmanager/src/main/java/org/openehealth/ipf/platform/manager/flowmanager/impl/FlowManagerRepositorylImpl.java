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

import java.io.IOException;
import java.rmi.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerMBean;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerRepository;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerSearchCriteria;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;

/**
 * Provides implementation of IFlowManagerRepository.
 * 
 * @see IFlowManagerRepository
 * @author Mitko Kolev
 */
public class FlowManagerRepositorylImpl extends Observable implements
        IFlowManagerRepository {

    private final String FLOW_MANAGER_URI = "org.openehealth.ipf:type=service,name=FlowManager";

    private final Log log = LogFactory.getLog(FlowManagerRepositorylImpl.class);

    private final HashMap<IConnectionConfiguration, IFlowManagerMBean> flowManagers;

    private final HashMap<IConnectionConfiguration, List<FlowInfo>> flows;

    private final IJMXConnectionManager jMXConnectionManager;

    private int timeoutForSearchAfterReplay;

    public FlowManagerRepositorylImpl(IJMXConnectionManager jMXConnectionManager) {
        this.flowManagers = new HashMap<IConnectionConfiguration, IFlowManagerMBean>();
        this.jMXConnectionManager = jMXConnectionManager;
        this.flows = new HashMap<IConnectionConfiguration, List<FlowInfo>>();
        this.timeoutForSearchAfterReplay = 0;
    }

    @Override
    public void registerFlowManager(
            IConnectionConfiguration connectionConfiguration,
            IFlowManagerMBean flowManager) {
        if (flowManager != null) {
            final FlowManagerEvent event;
            synchronized (this) {
                flowManagers.remove(connectionConfiguration);
                flowManagers.put(connectionConfiguration, flowManager);
                event = new FlowManagerEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_REGISTERED);
            }
            flowManagerStateChanged(event);

        }
    }

    @Override
    public void unRegisterFlowManager(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            log.info("Unregistering flow manager adapter for connection "
                    + connectionConfiguration);
            final FlowManagerEvent event;
            synchronized (this) {
                flowManagers.remove(connectionConfiguration);
                flows.remove(connectionConfiguration);
                event = new FlowManagerEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_UNREGISTERED);
            }
            flowManagerStateChanged(event);
        }

    }

    private void flowManagerStateChanged(ConnectionEvent event) {
        if (event == null)
            return;
        this.setChanged();
        this.notifyObservers(event);
    }

   

    public boolean isFlowManagerRegistered(
            IConnectionConfiguration connectionConfiguration) {
        synchronized (this) {
            if (this.flowManagers.containsKey(connectionConfiguration)) {
                return true;
            } else {
                log
                        .error("isFlowManagerRegistered called for an offline connection!");
            }
            return false;
        }
    }

    protected void changeFlowCleanup(
            IConnectionConfiguration connectionConfiguration) throws Throwable {
        final ConnectionEvent event;
        synchronized (this) {
            IFlowManagerMBean fm = flowManagers.get(connectionConfiguration);
            if (fm != null) {
                boolean enableCleanup = fm.isEnableCleanup();
                fm.setEnableCleanup(!enableCleanup);
                event = new ConnectionEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_ATTRIBUTE_CHANGED);
            } else {
                event = null;
            }
        }

        flowManagerStateChanged(event);
    }

    protected void changeFlowFiltering(
            IConnectionConfiguration connectionConfiguration) throws Throwable {
        final FlowManagerEvent event;
        ;
        synchronized (this) {
            IFlowManagerMBean fm = flowManagers.get(connectionConfiguration);
            if (fm != null) {
                boolean enableFiltering = fm.isEnableFiltering();
                fm.setEnableFiltering(!enableFiltering);
                event = new FlowManagerEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_ATTRIBUTE_CHANGED);
            } else {
                event = null;
                log.error("Flow Manager not found! Cannot set flow Filtering.");
            }
        }
        flowManagerStateChanged(event);
    }

    protected void setApplication(
            IConnectionConfiguration connectionConfiguration, String application)
            throws Throwable {
        final FlowManagerEvent event;

        synchronized (this) {
            IFlowManagerMBean fm = flowManagers.get(connectionConfiguration);
            log.info("Setting application for connection "
                    + connectionConfiguration + " to " + application);
            if (fm != null) {
                fm.setApplication(application);
                event = new FlowManagerEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_APPLICATION_CHANGED);
            } else {
                log
                        .error("Flow Manager not found! Cannot set max application.");
                event = null;
            }
        }
        flowManagerStateChanged(event);
    }

    protected void setMaxFlows(
            IConnectionConfiguration connectionConfiguration, String maxFlows)
            throws Throwable {
        final FlowManagerEvent event;

        synchronized (this) {
            IFlowManagerMBean fm = flowManagers.get(connectionConfiguration);
            if (fm != null) {
                fm.setMaxFlows(maxFlows);
                event = new FlowManagerEvent(connectionConfiguration,
                        FlowManagerEvent.FLOWMANAGER_ATTRIBUTE_CHANGED);
                log.info("Settings max flows for connection "
                        + connectionConfiguration);
            } else {
                event = null;
                log.error("Flow Manager not found! Cannot set max flows.");
            }
        }
        flowManagerStateChanged(event);
    }

    public void handleAttributeNotification(
            IConnectionConfiguration connectionConfiguration,
            String attributeName, Object newValue, Object oldValue) {
        // do not set the attributes to the proxy, because this will generate
        // event to the server, which will be received back(infinite loop)
        if (attributeName.equals("UpperTimeLimit")) {
            // do not generate event in this case.
            return;
        }
        FlowManagerEvent event = new FlowManagerEvent(connectionConfiguration,
                FlowManagerEvent.FLOWMANAGER_ATTRIBUTE_CHANGED);
        flowManagerStateChanged(event);
    }

    @Override
    public List<IFlowInfo> getFlowInfos(
            IConnectionConfiguration connectionConfiguration) {
        synchronized (this) {
            if (flows.containsKey(connectionConfiguration)) {
                List<FlowInfo> infos = flows.get(connectionConfiguration);
                if (infos == null) {
                    return new ArrayList<IFlowInfo>();
                } else {
                    List<IFlowInfo> fi = new ArrayList<IFlowInfo>();
                    // create to's for the flows
                    for (FlowInfo info : infos) {
                        fi.add(new FlowInfoImpl(info));
                    }
                    return fi;
                }
            }
        }
        return new ArrayList<IFlowInfo>();
    }

    /**
     * Does not keep track of the active flowManager! Notifies the listeners
     * with FLOWMANAGER_VIEW_CHANGE only.
     * 
     * @param connectionConfiguration
     */
    protected void setActiveFlowManager(
            IConnectionConfiguration connectionConfiguration) {
        FlowManagerEvent event = new FlowManagerEvent(connectionConfiguration,
                FlowManagerEvent.FLOWMANAGER_VIEW_CHANGE);
        this.setChanged();
        this.notifyObservers(event);
    }

    protected void setTimeoutOnReply(int timeout) {
        this.timeoutForSearchAfterReplay = timeout;
    }

    public int getTimeoutOnReply() {
        return timeoutForSearchAfterReplay;
    }

    @Override
    public String getApplication(
            IConnectionConfiguration connectionConfiguration) {

        try {
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    return mbean.getApplication();
                }
            }
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }

        return "";
    }

    @Override
    public String getMaxFlows(IConnectionConfiguration connectionConfiguration) {
        try {
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    return mbean.getMaxFlows();
                }
            }
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }
        return "";
    }

    @Override
    public boolean isEnabledCleanup(
            IConnectionConfiguration connectionConfiguration) {
        try {
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    return mbean.isEnableCleanup();
                }
            }
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }
        return false;
    }

    @Override
    public boolean isEnabledFiltering(
            IConnectionConfiguration connectionConfiguration) {
        try {
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    return mbean.isEnableFiltering();
                }
            }
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }
        return false;
    }

    @Override
    public void findFlowMessage(
            IConnectionConfiguration connectionConfiguration, Long flowId) {
        try {

            final FlowManagerEvent event;
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    String text = mbean.findFlowMessageText(flowId);
                    event = new FlowManagerEvent(connectionConfiguration,
                            FlowManagerEvent.FLOWMANAGER_MESSAGE_RECEIVED, text);
                } else {
                    event = null;
                }
            }
            flowManagerStateChanged(event);
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }
    }

    @Override
    public void findFlowPartMessage(
            IConnectionConfiguration connectionConfiguration, Long flowId,
            IFlowPartInfo partInfo) {
        try {
            final FlowManagerEvent event;
            synchronized (this) {
                if (flowManagers.containsKey(connectionConfiguration)) {
                    IFlowManagerMBean mbean = this.flowManagers
                            .get(connectionConfiguration);
                    String text = mbean.findFlowPartMessageText(flowId,
                            partInfo.getPath());
                    event = new FlowManagerEvent(connectionConfiguration,
                            FlowManagerEvent.FLOWMANAGER_MESSAGE_RECEIVED, text);

                } else {
                    event = null;
                }
                flowManagerStateChanged(event);
            }
        } catch (Throwable t) {
            log.error("The connection was broken!" + t);
            handleUncheckedException(connectionConfiguration, t);
        }
    }

    /**
     * Replays the given flows. 
     * After the replay, makes a search with the given criteria.
     * 
     * @param connectionConfiguration the target connection configuration.
     * @param flows the flows to be replayed
     * @param searchCriteria 
     * @throws InterruptedException 
     * @throws ParseException if the dates in the serach criteria are invalid 
     */
    public void replayFlows(IConnectionConfiguration connectionConfiguration,
            List<IFlowInfo> flows, IFlowManagerSearchCriteria searchCriteria)
            throws InterruptedException, ParseException {
        synchronized (this) {
            if (flowManagers.containsKey(connectionConfiguration)) {
                IFlowManagerMBean mbean = this.flowManagers
                        .get(connectionConfiguration);
                for (IFlowInfo flow : flows) {
                    long flowId = flow.getIdentifier();
                    if (flow.isReplayable()) {
                        mbean.replayFlow(flowId);
                    }
                }
                if (searchCriteria != null) {
                    Thread.sleep(getTimeoutOnReply());
                    List<FlowInfo> infos = searchFlowsInternal(
                            connectionConfiguration, searchCriteria);
                    if (infos != null) {
                        registerFlows(connectionConfiguration, infos);
                    }
                }
            }
        }// synchronized
    }

    public void searchFlows(IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria) throws ParseException {
        List<FlowInfo> flows = new ArrayList<FlowInfo>();
        synchronized (this) {
            //search
            flows = searchFlowsInternal(connectionConfiguration,
                    criteria);
            registerFlows(connectionConfiguration, flows);
        }
    }
    
    // just fire event
    private void registerFlows(IConnectionConfiguration connectionConfiguration,
            List<FlowInfo> flowsToRegister) {
        log.info("Notifying observers for registered flows for connection "
                + connectionConfiguration);
        if (flows.containsKey(connectionConfiguration)) {
            flows.remove(connectionConfiguration);
            flows.put((IConnectionConfiguration) connectionConfiguration
                    .clone(), flowsToRegister);
        } else {
            flows.put((IConnectionConfiguration) connectionConfiguration
                    .clone(), flowsToRegister);
        }

        this.setChanged();
        // convert the newly created flows to IFlowInfos
        FlowManagerEvent event = new FlowManagerEvent(connectionConfiguration,
                FlowManagerEvent.FLOWS_RECEIVED,
                getFlowInfos(connectionConfiguration));
        this.notifyObservers(event);
    }

    private List<FlowInfo> searchFlowsInternal(
            IConnectionConfiguration connectionConfiguration,
            IFlowManagerSearchCriteria criteria) throws ParseException {

        if (flowManagers.containsKey(connectionConfiguration)) {
            IFlowManagerMBean fm = this.flowManagers
                    .get(connectionConfiguration);
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
                // do the search
                List<FlowInfo> flowsInfos = doFlowManagerFind(fm, Long
                        .toString(msInterval), criteria);
                return flowsInfos;
            }
        }
        log.error("Returning no flows, flow manager is unregistered!");
        return new ArrayList<FlowInfo>();
    }

    private List<FlowInfo> doFlowManagerFind(IFlowManagerMBean fm,
            String lastInMs, IFlowManagerSearchCriteria criteria) {
        if (criteria == null) {
            log.debug("Criteria are not restricted.");
            // in this case we do not have any restrictions
            return fm.findLastFlows(lastInMs);
        }
        List<FlowInfo> flowsInfos = new ArrayList<FlowInfo>();
        if (criteria.hasFullTextSearchExpression()) {
            String fullTextSearchExpression = criteria
                    .getIncomingFlowMessageSearchExpression();
            if (criteria.isRestrictedToErrorFlows()) {
                flowsInfos = fm.findLastErrorFlowsWithMessageText(lastInMs,
                        fullTextSearchExpression);
            } else if (criteria.isRestrictedToUnacknowledgedFlows()) {
                flowsInfos = fm.findLastUnackFlowsWithMessageText(lastInMs,
                        fullTextSearchExpression);
            } else {
                flowsInfos = fm.findLastFlowsWithMessageText(lastInMs,
                        fullTextSearchExpression);
            }
        } else {
            if (criteria.isRestrictedToErrorFlows()) {
                flowsInfos = fm.findLastErrorFlows(lastInMs);
            } else if (criteria.isRestrictedToUnacknowledgedFlows()) {
                flowsInfos = fm.findLastUnackFlows(lastInMs);
            } else {
                flowsInfos = fm.findLastFlows(lastInMs);
            }
        }
        return flowsInfos;

    }

    /**
     * Opens a real connection to the
     * 
     * @param connectionConfiguration
     * @return
     */
    protected IFlowManagerMBean loadFlowManager(
            IConnectionConfiguration connectionConfiguration,
            NotificationListener listener) {

        if (connectionConfiguration == null
                || !this.jMXConnectionManager
                        .isConnected(connectionConfiguration))
            return null;
        IFlowManagerMBean flowManager = null;
        try {
            flowManager = jMXConnectionManager.loadProxyForClass(
                    connectionConfiguration, FLOW_MANAGER_URI,
                    IFlowManagerMBean.class, listener);
        } catch (IOException ioe) {
            log.error(ioe);
            return null;
        } catch (MalformedObjectNameException e) {
            log.error(e);
            return null;
        } catch (InstanceNotFoundException e) {
            log.error(e);
            return null;
        }

        return flowManager;
    }

    protected void handleUncheckedException(
            IConnectionConfiguration connectionConfiguration,
            Throwable exception) {
        Throwable cause = exception.getCause();
        if (cause == null) {
            log.error("Unknown Exception detected for connection"
                    + connectionConfiguration, cause);
        }
        if (cause instanceof ConnectException) {
            log.info("ConnectException detected for connection "
                    + connectionConfiguration);
            this.unRegisterFlowManager(connectionConfiguration);
        } else if (cause instanceof IllegalArgumentException) {
            log.info("IllegalArgumentException detected for connection "
                    + connectionConfiguration, cause);
        } else if (cause instanceof IOException) {
            log.error("Unknown Exception detected for connection"
                    + connectionConfiguration, cause);
        } else {
            log.error("Unknown Exception detected for connection"
                    + connectionConfiguration, cause);
        }

    }
}
