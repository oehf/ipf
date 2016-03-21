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
package org.openehealth.ipf.commons.flow.jmx;

import static org.openehealth.ipf.commons.flow.jmx.MBeanUtils.formatDate;
import static org.openehealth.ipf.commons.flow.jmx.MBeanUtils.parseDate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.openehealth.ipf.commons.core.datetime.Duration;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.repository.FlowFinderCriteria;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowInfoFinderCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author Martin Krasser
 * @author Mitko Kolev
 */
@ManagedResource(
        objectName="org.openehealth.ipf:type=service,name=FlowManager",
        description="Application flow manager") 
public class FlowManagerMBean {

    @Autowired
    private FlowManager flowManager;
    
    private String application;
    
    private Date upperTimeLimit;
    
    private Integer maxResults;
    
    public FlowManagerMBean() {
        maxResults = 100;
    }
    
    @ManagedAttribute(description="Application name")
    public String getApplication() {
        return application;
    }
    
    @ManagedAttribute(description="Application name")
    public void setApplication(String application) {
        this.application = application;
    }
    
    @ManagedAttribute(description="Upper time limit")
    public String getUpperTimeLimit() {
        if (upperTimeLimit == null) {
            return null;
        }
        return formatDate(upperTimeLimit);
    }

    @ManagedAttribute(description="Upper time limit")
    public void setUpperTimeLimit(String upperTimeLimit) throws ParseException {
        if (upperTimeLimit == null || upperTimeLimit.trim().equals("")) {
            this.upperTimeLimit = null;
        } else {
            this.upperTimeLimit = parseDate(upperTimeLimit);
        }
    }

    @ManagedAttribute(description = 
    "Maximum number of flows returned by finder operations or processed during replay operations")
    public String getMaxFlows() {
        if (maxResults == null) {
            return null;
        }
        return maxResults.toString();
    }

    @ManagedAttribute(description = 
    "Maximum number of flows returned by finder operations or processed during replay operations")
    public void setMaxFlows(String maxFlows) {
        if (maxFlows == null || maxFlows.trim().equals("")) {
            maxResults = null;
        } else {
            maxResults = Integer.valueOf(maxFlows);
        }
    }

    @ManagedAttribute(description="Toggle duplicate filtering")
    public boolean isEnableFiltering() {
        return flowManager.isFlowFilterEnabled(application);
    }

    @ManagedAttribute(description="Toggle duplicate filtering")
    public void setEnableFiltering(boolean enableFiltering) {
        flowManager.setFlowFilterEnabled(application, enableFiltering);
    }

    @ManagedAttribute(description="Toggle flow cleanup")
    public boolean isEnableCleanup() {
        return flowManager.isFlowCleanupEnabled(application);
    }

    @ManagedAttribute(description="Toggle flow cleanup")
    public void setEnableCleanup(boolean enableCleanup) {
        flowManager.setFlowCleanupEnabled(application, enableCleanup);
    }

    @ManagedOperation(description="Set upper time limit to current time")
    public void setUpperTimeLimitToCurrentTime() {
        upperTimeLimit = new Date();
    }
    @ManagedOperation(description = "Find flows within given timespan "
            + "containing flowMessageSearchQuery in the flow message")
    @ManagedOperationParameters(@ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
            + "seconds (e.g. 2s), "
            + "minutes (e.g. 2m) or "
            + "hours (e.g. 2h)"))
    public List<FlowInfo> findLastFlows(String last) {
        return flowManager.findFlows(finderCriteria(last));
    }
    
    @ManagedOperation(description = "Find flows with an ERROR acknowledgement within given timespan "
            + "containing flowMessageSearchQuery in the flow message")
    @ManagedOperationParameters(@ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
            + "seconds (e.g. 2s), "
            + "minutes (e.g. 2m) or "
            + "hours (e.g. 2h)"))
    public List<FlowInfo> findLastErrorFlows(String last) {
        return flowManager.findErrorFlows(finderCriteria(last));
    }

    @ManagedOperation(description = "Find flows without any acknowledgement within given timespan "
            + "containing flowMessageSearchQuery in the flow message")
    @ManagedOperationParameters(@ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
            + "seconds (e.g. 2s), "
            + "minutes (e.g. 2m) or "
            + "hours (e.g. 2h)"))
    public List<FlowInfo> findLastUnackFlows(String last) {
        return flowManager.findUnackFlows(finderCriteria(last));
    }
    
    @ManagedOperation(description = "Find flows within given timespan " +
    		" containing searchExpression in the inbound flow message text")
    @ManagedOperationParameters( {
            @ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
                    + "seconds (e.g. 2s), "
                    + "minutes (e.g. 2m) or "
                    + "hours (e.g. 2h)"),
            @ManagedOperationParameter(name = "searchExpression", description = "The query "
                    + "for the inbound message's string representation.\n "
                    + "e.g. \"+hl7 -test\" will match flows "
                    + "with inbound message containing \"hl7\", not containing \"test\".\n"
                    + "e.g. \"hl7\" will match flows "
                    + "with inbound message containing the word \"hl7\".") })
    public List<FlowInfo> findLastFlowsWithMessageText(String last, String searchExpression) {
        return flowManager.findFlows(finderCriteria(last, searchExpression));
    }
    
    @ManagedOperation(description = "Find flows with an ERROR acknowledgement within given timespan " + 
            "containing searchExpression in the inbound flow message text")
    @ManagedOperationParameters( {
            @ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
                    + "seconds (e.g. 2s), "
                    + "minutes (e.g. 2m) or "
                    + "hours (e.g. 2h)"),
            @ManagedOperationParameter(name = "searchExpression", description = "The query "
                    + "for the inbound message's string representation.\n "
                    + "e.g. \"+hl7 -test\" will match flows "
                    + "with inbound message containing \"hl7\", not containing \"test\".\n"
                    + "e.g. \"hl7\" will match flows "
                    + "with inbound message containing the word \"hl7\".") })
    public List<FlowInfo> findLastErrorFlowsWithMessageText(String last, String searchExpression) {
        return flowManager.findErrorFlows(finderCriteria(last, searchExpression));
    }
    
    @ManagedOperation(description = "Find flows without any acknowledgement within given timespan " +
            "containing searchExpression in the inbound flow message text")
    @ManagedOperationParameters( {
            @ManagedOperationParameter(name = "timespan", description = "Last n milliseconds (e.g. 2000), "
                    + "seconds (e.g. 2s), "
                    + "minutes (e.g. 2m) or "
                    + "hours (e.g. 2h)"),
            @ManagedOperationParameter(name = "searchExpression", description = "The query "
                    + "for the inbound message's string representation.\n "
                    + "e.g. \"+hl7 -test\" will match flows "
                    + "with inbound message containing \"hl7\", not containing \"test\".\n"
                    + "e.g. \"hl7\" will match flows "
                    + "with inbound message containing the word \"hl7\".") })
    public List<FlowInfo> findLastUnackFlowsWithMessageText(String last, String searchExpression) {
        return flowManager.findUnackFlows(finderCriteria(last, searchExpression));
    }
    
    @ManagedOperation(description="Find inbound message text of flow with given identifier.")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="identifier", description="Flow identifier")
    )
    public String findFlowMessageText(long flowId) {
        return flowManager.findFlowMessageText(flowId);
    } 
    
    @ManagedOperation(description = "Find outbound message text of flow with given identifier and path")
    @ManagedOperationParameters( {
            @ManagedOperationParameter( name = "identifier", description = "Flow identifier"),
            @ManagedOperationParameter(name = "flowPath", description = "Flow path") })
    public String findFlowPartMessageText(long flowId, String flowPath) {
        return flowManager.findFlowPartMessageText(flowId, flowPath);
    }

    @ManagedOperation(description="Replay flows within given timespan")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="timespan", 
                    description="Last n milliseconds (e.g. 2000), " +
                            "seconds (e.g. 2s), " +
                            "minutes (e.g. 2m) or " +
                            "hours (e.g. 2h)")
    )
    public int replayLastFlows(String last) {
        return flowManager.replayFlows(finderCriteria(last));
    }

    @ManagedOperation(description="Replay flows with an ERROR acknowledgement within given timespan")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="timespan", 
                    description="Last n milliseconds (e.g. 2000), " +
                            "seconds (e.g. 2s), " +
                            "minutes (e.g. 2m) or " +
                            "hours (e.g. 2h)")
    )
    public int replayLastErrorFlows(String last) {
        return flowManager.replayErrorFlows(finderCriteria(last));
    }

    @ManagedOperation(description="Replay flows without any acknowledgement within given timespan")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="timespan", 
                    description="Last n milliseconds (e.g. 2000), " +
                            "seconds (e.g. 2s), " +
                            "minutes (e.g. 2m) or " +
                            "hours (e.g. 2h)")
    )
    public int replayLastUnackFlows(String last) {
        return flowManager.replayUnackFlows(finderCriteria(last));
    }

    @ManagedOperation(description="Find flow with given identifier")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="identifier", description="Flow identifier")
    )
    public FlowInfo findFlow(long flowId) {
        return flowManager.findFlow(flowId);
    }
    
    @ManagedOperation(description="Find flow with given identifier")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="identifier", description="Flow identifier"),
            @ManagedOperationParameter(name="includeText", description="Include flow message text")
    })
    public FlowInfo findFlow(long flowId, boolean includeText) {
        return flowManager.findFlow(flowId, includeText);
    }
    
    @ManagedOperation(description="Replay flow with given identifier")
    @ManagedOperationParameters(
            @ManagedOperationParameter(name="identifier", description="Flow identifier")
    )
    public void replayFlow(long flowId) {
        flowManager.replayFlow(flowId);
    }
    
    private FlowInfoFinderCriteria finderCriteria(String last) {
        return finderCriteria(last, null);
    }
    
    private FlowInfoFinderCriteria finderCriteria(String last, String searchExpression) {
        int mr = maxResults == null ? FlowFinderCriteria.DEFAULT_MAX_RESULTS : maxResults;
        return new FlowInfoFinderCriteria(from(Duration.parse(last),
                upperTimeLimit), upperTimeLimit, application, mr,
                formatSearchExpression(searchExpression));
    }
    
    private static Date from(Duration duration, Date to) {
        if (to == null) {
            return duration.since();
        } else {
            return duration.since(to);
        }
    }

    private static String formatSearchExpression(String searchExpression) {
        if (searchExpression == null) {
            return searchExpression;
        }
        if (searchExpression.trim().isEmpty()) {
            return null;
        }
        return searchExpression.trim();
    }

}

