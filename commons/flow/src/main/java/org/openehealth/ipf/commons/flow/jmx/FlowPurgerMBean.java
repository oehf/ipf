/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.flow.jmx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.config.ApplicationConfig;
import org.openehealth.ipf.commons.flow.purge.FlowPurgeJob;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * MBean for managing application-specific purge operations for out-dated flows.
 * Out-dated flows are removed from the database either manually or periodically
 * based on cron expressions.
 * 
 * @author Martin Krasser
 */
@ManagedResource(
        objectName="org.openehealth.ipf:type=service,name=FlowPurger",
        description="Flow purge service") 
public class FlowPurgerMBean implements InitializingBean, DisposableBean {

    private static final Log LOG = LogFactory.getLog(FlowPurgerMBean.class);

    @Autowired
    private FlowManager flowManager; 

    private String application; 

    private Scheduler scheduler;
    
    private final Map<String, FlowPurgeJob> flowPurgeJobs;

    public FlowPurgerMBean() {
        flowPurgeJobs = new HashMap<String, FlowPurgeJob>();
    }

    @ManagedAttribute(description="Application name")
    public String getApplication() {
        return application;
    }

    @ManagedAttribute(description="Application name")
    public void setApplication(String application) {
        this.application = application;
    }

    @ManagedAttribute(description="Purge flows older than given duration "
            + "(e.g. \"30d\" will purge flows older than 30 days)")
    public String getPurgeFlowsOlderThan() {
        return flowManager.getApplicationConfig(application).getPurgeFlowsOlderThan();
    }

    @ManagedAttribute(description="Set to true to exclude ERROR flows from being purged. "
        + "Set to false to purge CLEAN and ERROR flows")
    public boolean isDoNotPurgeErrorFlows() {
        return flowManager.getApplicationConfig(application).isDoNotPurgeErrorFlows();
    }

    @ManagedAttribute(description="Cron expression for purge schedule")
    public String getPurgeSchedule() {
        return flowManager.getApplicationConfig(application).getFlowPurgeSchedule();
    }

    @ManagedAttribute(description="Purge job status for current application")
    public boolean isPurgeScheduled() {
        return flowManager.getApplicationConfig(application).isFlowPurgeScheduled();
    }

    @ManagedAttribute(description="Purge flows older than given duration "
            + "(e.g. \"30d\" will purge flows older than 30 days)")
    public void setPurgeFlowsOlderThan(String purgeFlowsOlderThan) {
        ApplicationConfig applicationConfig = flowManager.getApplicationConfig(application);
        applicationConfig.setPurgeFlowsOlderThan(formatInput(purgeFlowsOlderThan));
        flowManager.mergeApplicationConfig(applicationConfig);
    }

    @ManagedAttribute(description="Set to true to exclude ERROR flows from being purged. "
        + "Set to false to purge CLEAN and ERROR flows")
    public void setDoNotPurgeErrorFlows(boolean doNotPurgeErrorFlows) {
        ApplicationConfig applicationConfig = flowManager.getApplicationConfig(application);
        applicationConfig.setDoNotPurgeErrorFlows(doNotPurgeErrorFlows);
        flowManager.mergeApplicationConfig(applicationConfig);
    }

    @ManagedAttribute(description="Cron expression for purge schedule")
    public void setPurgeSchedule(String purgeSchedule) {
        ApplicationConfig applicationConfig = flowManager.getApplicationConfig(application);
        applicationConfig.setFlowPurgeSchedule(formatInput(purgeSchedule));
        flowManager.mergeApplicationConfig(applicationConfig);
    }

    @ManagedOperation(description = "Executes a purge job once for current application")
    public void execute() {
        executeJob(flowManager.getApplicationConfig(application));
    }

    @ManagedOperation(description = "Schedules or reschedules a purge job for current application and schedule")
    public void schedule() {
        scheduleJob(flowManager.getApplicationConfig(application));
    }

    @ManagedOperation(description = "Unschedules a purge job for current application and schedule")
    public void unschedule() {
        unscheduleJob(flowManager.getApplicationConfig(application));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (scheduler == null){
    	    scheduler = StdSchedulerFactory.getDefaultScheduler();
        }
    	if (!scheduler.isStarted()){ 
            scheduler.start();
        }
        LOG.info("Scheduler started:" + scheduler.isStarted());
        initJobs();
    }

    @Override
    public void destroy() throws Exception {
        scheduler.shutdown();
    }

    private void executeJob(ApplicationConfig config) {
        FlowPurgeJob flowPurgeJob = getFlowPurgeJob(config);
        flowPurgeJob.execute(config);
    }

    private void scheduleJob(ApplicationConfig config) {
        FlowPurgeJob flowPurgeJob = getFlowPurgeJob(config);
        if (flowPurgeJob.isScheduled()) {
            flowPurgeJob.unschedule();
        }
        flowPurgeJob.schedule(config);
    }

    private void unscheduleJob(ApplicationConfig config) {
        getFlowPurgeJob(config).unschedule();
    }

    private void initJobs() {
        LOG.info("Initialize flow purge jobs ... ");
        for (ApplicationConfig config : flowManager.findApplicationConfigs()) {
            if (config.isFlowPurgeScheduled()) {
                scheduleJob(config);
            } else {
                LOG.info("Skip scheduling of job for application " + config.getApplication());
            }
        }
        LOG.info("Initialization done. ");
    }

    private static String formatInput(String input) {
        if (input == null) {
            return null;
        }
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return null;
        }
        return trimmedInput;
    }

    private FlowPurgeJob getFlowPurgeJob(ApplicationConfig config) {
        FlowPurgeJob flowPurgeJob = flowPurgeJobs.get(config.getApplication());
        if (flowPurgeJob == null) {
            flowPurgeJob = new FlowPurgeJob(flowManager, scheduler, application);
            flowPurgeJob.setFlowPurgeSchedule(config.getFlowPurgeSchedule());
            flowPurgeJob.setPurgeFlowsOlderThan(config.getPurgeFlowsOlderThan());
            flowPurgeJob.setDoNotPurgeErrorFlows(config.isDoNotPurgeErrorFlows());
            flowPurgeJobs.put(application, flowPurgeJob);
        }
        return flowPurgeJob;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @ManagedAttribute(description="Schedulers metadata")
    public Map<String, String> getSchedulerMetaData() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("Scheduler Name", scheduler.getMetaData().getSchedulerName());
            map.put("Scheduler Class", scheduler.getMetaData().getSchedulerClass().getCanonicalName());
            map.put("Thread Pool Class", scheduler.getMetaData().getThreadPoolClass().getCanonicalName());
            map.put("Thread Pool Size", String.valueOf(scheduler.getMetaData().getThreadPoolSize()));
            map.put("Number Of Jobs Executed", String.valueOf(scheduler.getMetaData().getNumberOfJobsExecuted()));
            map.put("Running Since", MBeanUtils.formatDate(scheduler.getMetaData().getRunningSince()));
            return map;
        } catch (SchedulerException e) {
            return null;
        }
    }

}
