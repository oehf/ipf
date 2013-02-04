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
package org.openehealth.ipf.commons.flow.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openehealth.ipf.commons.core.datetime.Duration;
import org.quartz.spi.OperableTrigger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Persistent configuration data for an application.
 * 
 * @author Martin Krasser
 */
@Entity
@Table(name = "T_APPLICATION_CONFIG")
public class ApplicationConfig {

    public static final boolean FLOW_FILTER_ENABLED_DEFAULT = true;
    public static final boolean FLOW_CLEANUP_ENABLED_DEFAULT = false;
    public static final boolean FLOW_PURGE_SCHEDULED_DEFAULT = false;
    public static final boolean DO_NOT_PURGE_ERROR_FLOWS_DEFAULT = false;
    public static final String PURGE_FLOWS_OLDER_THAN_DEFAULT = "30d";
    public static final String FLOW_PURGE_SCHEDULE_DEFAULT = "0 0 1 * * ?"; // 1:00 am every day 
    
    @Id
    @Column(name="C_APPLICATION")
    private String application;
    
    @Column(name="C_FLOW_FILTER_ENABLED")
    private boolean flowFilterEnabled;

    @Column(name="C_FLOW_CLEANUP_ENABLED")
    private boolean flowCleanupEnabled;

    @Column(name="C_PURGE_SCHEDULED")
    private Boolean flowPurgeScheduled;
    
    @Column(name="C_DO_NOT_PURGE_ERROR_FLOWS")
    private Boolean doNotPurgeErrorFlows;
    
    @Column(name="C_PURGE_SCHEDULE")
    private String flowPurgeSchedule;
    
    @Column(name="C_PURGE_FLOW_OLDER_THAN")
    private String purgeFlowsOlderThan;

    /**
     * Creation a new {@link ApplicationConfig} with default settings and
     * application name set to <code>null</code>.
     */
    public ApplicationConfig() {
        this(null);
    }

    /**
     * Creation a new {@link ApplicationConfig} with default settings and given
     * application name.
     * 
     * @param application application name.
     */
    public ApplicationConfig(String application) { 
        this.application = application;
        flowFilterEnabled = FLOW_FILTER_ENABLED_DEFAULT;
        flowCleanupEnabled = FLOW_CLEANUP_ENABLED_DEFAULT;
        flowPurgeScheduled = FLOW_PURGE_SCHEDULED_DEFAULT;
        doNotPurgeErrorFlows = DO_NOT_PURGE_ERROR_FLOWS_DEFAULT;
        flowPurgeSchedule = FLOW_PURGE_SCHEDULE_DEFAULT;
        purgeFlowsOlderThan = PURGE_FLOWS_OLDER_THAN_DEFAULT;
    }
    
    public String getApplication() {
        return application;
    }

    /**
     * Sets the name of the application to which this configuration applies.
     * 
     * @param application
     *            application name.
     */
    public void setApplication(String application) {
        this.application = application;
    }

    public boolean isFlowFilterEnabled() {
        return flowFilterEnabled;
    }

    /**
     * Set to <code>true</code> to enable filtering of flows. Default is
     * <code>true</code>.
     * 
     * @param flowFilterEnabled
     * @see #FLOW_FILTER_ENABLED_DEFAULT
     */
    public void setFlowFilterEnabled(boolean flowFilterEnabled) {
        this.flowFilterEnabled = flowFilterEnabled;
    }

    public boolean isFlowCleanupEnabled() {
        return flowCleanupEnabled;
    }

    /**
     * Set to <code>true</code> to cleanup (remove message content from)
     * successful flow. Default is <code>false</code>.
     * 
     * @param flowCleanupEnabled
     * @see #FLOW_CLEANUP_ENABLED_DEFAULT
     */
    public void setFlowCleanupEnabled(boolean flowCleanupEnabled) {
        this.flowCleanupEnabled = flowCleanupEnabled;
    }

    public boolean isFlowPurgeScheduled() {
        // Support DB schema upgrade
        if (flowPurgeScheduled == null) {
            return FLOW_PURGE_SCHEDULED_DEFAULT;
        }
        return flowPurgeScheduled;
    }

    /**
     * Set to <code>true</code> is a flow purge job has been scheduled. Default
     * is <code>false</code>.
     * 
     * @param flowPurgeScheduled
     * @see #FLOW_PURGE_SCHEDULED_DEFAULT
     */
    public void setFlowPurgeScheduled(boolean flowPurgeScheduled) {
        this.flowPurgeScheduled = flowPurgeScheduled;
    }

    public boolean isDoNotPurgeErrorFlows() {
        // Support DB schema upgrade
        if (doNotPurgeErrorFlows == null) {
            return DO_NOT_PURGE_ERROR_FLOWS_DEFAULT;
        }
        return doNotPurgeErrorFlows;
    }

    /**
     * Set to <code>true</code> to exclude ERROR flows from being purged.
     * Default is <code>false</code>.
     * 
     * @param doNotPurgeErrorFlows
     * @see #DO_NOT_PURGE_ERROR_FLOWS_DEFAULT
     */
    public void setDoNotPurgeErrorFlows(boolean doNotPurgeErrorFlows) {
        this.doNotPurgeErrorFlows = doNotPurgeErrorFlows;
    }

    public String getFlowPurgeSchedule() {
        // Support DB schema upgrade
        if (flowPurgeSchedule == null) {
            return FLOW_PURGE_SCHEDULE_DEFAULT;
        }
        return flowPurgeSchedule;
    }

    /**
     * Sets the purge schedule's Cron expression. Default is
     * <code>0 0 1 * * ?</code> which means 1:00 am every day.
     * 
     * @param flowPurgeSchedule
     * @see #FLOW_PURGE_SCHEDULE_DEFAULT
     */
    public void setFlowPurgeSchedule(String flowPurgeSchedule) {
        validateFlowPurgeSchedule(flowPurgeSchedule);
        this.flowPurgeSchedule = flowPurgeSchedule;
    }

    public String getPurgeFlowsOlderThan() {
        // Support DB schema upgrade
        if (purgeFlowsOlderThan == null) {
            return PURGE_FLOWS_OLDER_THAN_DEFAULT;
        }
        return purgeFlowsOlderThan;
    }

    /**
     * Set the duration or minimum age of flows to be purged. Default is
     * <code>30d</code> which means flows older than 30 days will be purged by
     * jobs triggered by the given purge schedule.
     * 
     * @param purgeFlowsOlderThan
     * @see #PURGE_FLOWS_OLDER_THAN_DEFAULT
     */
    public void setPurgeFlowsOlderThan(String purgeFlowsOlderThan) {
        validatePurgeFlowsOlderThan(purgeFlowsOlderThan);
        this.purgeFlowsOlderThan = purgeFlowsOlderThan;
    }

    private static void validateFlowPurgeSchedule(String flowPurgeSchedule) {
        String name = "default";
        try {
            ((OperableTrigger)newTrigger()
                .withIdentity(name, name)
                .forJob(name, name)
                .build())
                .validate();
        } catch (Exception e) {
            throw new ApplicationConfigException("invalid schedule definition: " + e.getMessage());
        }
    }

    private static void validatePurgeFlowsOlderThan(String purgeFlowsOlderThan) {
        try {
            Duration.parse(purgeFlowsOlderThan);
        } catch (Exception e) {
            throw new ApplicationConfigException("invalid duration definition: " + e.getMessage());
        }
    }
    
}
