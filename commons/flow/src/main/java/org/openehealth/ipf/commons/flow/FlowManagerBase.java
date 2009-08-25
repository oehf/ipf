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
package org.openehealth.ipf.commons.flow;

import java.util.List;

import org.openehealth.ipf.commons.flow.config.ApplicationConfig;
import org.openehealth.ipf.commons.flow.domain.Flow;
import org.openehealth.ipf.commons.flow.domain.FlowPart;
import org.openehealth.ipf.commons.flow.repository.ConfigRepository;
import org.openehealth.ipf.commons.flow.repository.FlowFinderCriteria;
import org.openehealth.ipf.commons.flow.repository.FlowPurgeCriteria;
import org.openehealth.ipf.commons.flow.repository.FlowRepository;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowInfoFinderCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.textString;

/**
 * @author Martin Krasser
 * @author Mitko Kolev 
 */
public class FlowManagerBase implements FlowManager {

    @Autowired
    private FlowRepository flowRepository;
    
    @Autowired
    private ConfigRepository configRepository;
    
    public List<Long> findFlowIds(FlowInfoFinderCriteria finderCriteria) {
        return flowRepository.findFlowIds(repositoryFinderCriteria(finderCriteria));
    }

    public List<Long> findErrorFlowIds(FlowInfoFinderCriteria finderCriteria) {
        return flowRepository.findErrorFlowIds(repositoryFinderCriteria(finderCriteria));
    }

    public List<Long> findUnackFlowIds(FlowInfoFinderCriteria finderCriteria) {
        return flowRepository.findUnackFlowIds(repositoryFinderCriteria(finderCriteria));
    }

    public FlowInfo findFlow(Long flowId) {
        return loadFlow(flowId).getInfo();
    }

    public FlowInfo findFlow(Long flowId, boolean includeText) {
        return loadFlow(flowId).getInfo(includeText);
    }

    public boolean flowCompleted(Long flowId) {
        Flow flow = loadFlow(flowId);
        if (flow.isAckCountExpectationSet()) {
            return flow.isAckCountExpectedReached();
        }
        throw new FlowStatusException("acknowledgement count expectation not set on flow object");
    }

    public List<FlowInfo> findFlows(FlowInfoFinderCriteria finderCriteria) {
        return Flow.getInfos(flowRepository.findFlows(repositoryFinderCriteria(finderCriteria)));
    }

    public List<FlowInfo> findErrorFlows(FlowInfoFinderCriteria finderCriteria) {
        return Flow.getInfos(flowRepository.findErrorFlows(repositoryFinderCriteria(finderCriteria)));
    }

    public List<FlowInfo> findUnackFlows(FlowInfoFinderCriteria finderCriteria) {
        return Flow.getInfos(flowRepository.findUnackFlows(repositoryFinderCriteria(finderCriteria)));
    }
    
    public String findFlowMessageText(Long flowId){
        return textString(loadFlow(flowId).getFlowMessageText());
    } 
    
    public String findFlowPartMessageText(Long flowId, String flowPath) {
        Flow flow = loadFlow(flowId);
        FlowPart part = flow.getPart(flowPath);
        if (part == null) {
            throw new IllegalArgumentException(
                "No flow part with path " + flowPath + " exists");
        }
        return textString(part.getFlowPartMessageText());
    }
    
    public int purgeFlows(FlowPurgeCriteria purgeCriteria) {
        return flowRepository.purgeFlows(purgeCriteria);
    }

    public int replayFlows(FlowInfoFinderCriteria finderCriteria) {
        return replayFlows(findFlowIds(finderCriteria));
    }

    public int replayErrorFlows(FlowInfoFinderCriteria finderCriteria) {
        return replayFlows(findErrorFlowIds(finderCriteria));
    }

    public int replayUnackFlows(FlowInfoFinderCriteria finderCriteria) {
        return replayFlows(findUnackFlowIds(finderCriteria));
    }
    
    public void acknowledgeFlow(final ManagedMessage managedMessage) {
        Flow flow = lockFlow(managedMessage);
        boolean cleanup = isFlowCleanupEnabled(flow.getApplication());
        String path = managedMessage.getSplitHistory().indexPathString();
        flow.acknowledge(path, cleanup, managedMessage.render());
        
    }

    public void invalidateFlow(final ManagedMessage managedMessage) {
        Flow flow = lockFlow(managedMessage);
        String path = managedMessage.getSplitHistory().indexPathString();
        flow.invalidate(path, managedMessage.render());
    }

    public boolean filterFlow(final ManagedMessage managedMessage) {
        Flow flow = lockFlow(managedMessage);
        if (!isFlowFilterEnabled(flow.getApplication())) {
            return false;
        }
        return flow.filter(managedMessage.getSplitHistory().indexPathString());
    }

    public Long beginFlow(ManagedMessage managedMessage, String application) {
        return beginFlow(managedMessage, application, FlowInfo.ACK_COUNT_EXPECTED_UNDEFINED);
    }
    
    public Long beginFlow(ManagedMessage managedMessage, String application, int ackCountExpected) {
        Long flowId = managedMessage.getFlowId();
        if (flowId != null) {
            return flowId; // replay is running
        } 
        Flow flow = new Flow(application);
        flow.setAckCountExpected(ackCountExpected);
        flowRepository.persist(flow);
        // update message with generated flow id
        managedMessage.setFlowId(flow.getIdentifier());
        // update created flow with packet
        flow.setPacket(managedMessage.createPacket());
        //set the message text
        flow.setFlowMessageText(managedMessage.render());
        // return the newly created flow identifier
        return flow.getIdentifier();
    }

    public void replayFlow(Long flowId) {
        Flow flow = loadFlow(flowId);
        if (!flow.isReplayable()) {
            throw new FlowReplayException("flow not replayable");
        }
        
        // prepare flow for replay and obtain package
        byte[] packet = flow.prepareReplay();
        
        try {
            // delegate to replay template method
            packet = replayFlow(packet);
            // update flow with potentially changed packet
            flow.setPacket(packet);
        } catch (Exception e) {
            throw new FlowReplayException("flow replay failed", e);
        }
    }

    public boolean isFlowFilterEnabled(String application) {
        ApplicationConfig config = configRepository.find(application);
        if (config == null) {
            return ApplicationConfig.FLOW_FILTER_ENABLED_DEFAULT;
        }
        return config.isFlowFilterEnabled();
    }

    public void setFlowFilterEnabled(String application, boolean flowFilterEnabled) {
        ApplicationConfig config = configRepository.find(application);
        if (config == null) {
            mergeApplicationConfig(application, 
                    flowFilterEnabled, 
                    ApplicationConfig.FLOW_CLEANUP_ENABLED_DEFAULT);
        } else {
            config.setFlowFilterEnabled(flowFilterEnabled);
        }
    }

    public boolean isFlowCleanupEnabled(String application) {
        ApplicationConfig config = configRepository.find(application);
        if (config == null) {
            return ApplicationConfig.FLOW_CLEANUP_ENABLED_DEFAULT;
        }
        return config.isFlowCleanupEnabled();
    }
    
    public void setFlowCleanupEnabled(String application, boolean flowCleanupEnabled) {
        ApplicationConfig config = configRepository.find(application);
        if (config == null) {
            mergeApplicationConfig(application, 
                    ApplicationConfig.FLOW_FILTER_ENABLED_DEFAULT,
                    flowCleanupEnabled);
        } else {
            config.setFlowCleanupEnabled(flowCleanupEnabled);
        }
    }
    
    public List<ApplicationConfig> findApplicationConfigs() {
        return configRepository.find();
    }

    public ApplicationConfig getApplicationConfig(String application) {
        ApplicationConfig applicationConfig = configRepository.find(application);
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig(application);
        }
        return applicationConfig;
    }

    public void mergeApplicationConfig(ApplicationConfig applicationConfig) {
        configRepository.merge(applicationConfig);
    }
        
    private void mergeApplicationConfig(String application, 
            boolean flowFilterEnabled, 
            boolean flowCleanupEnabled) {
        ApplicationConfig config = new ApplicationConfig();
        config.setApplication(application);
        config.setFlowFilterEnabled(flowFilterEnabled);
        config.setFlowCleanupEnabled(flowCleanupEnabled);
        configRepository.merge(config);
    }
    
    protected byte[] replayFlow(byte[] packet) throws Exception {
        return packet;
    }
    
    private int replayFlows(List<Long> ids) {
        int counter = 0;
        for (Long id : ids) {
            try {
                replayFlow(id);
                counter++;
            } catch (FlowReplayException e) {
                // ignore
            }
        }
        return counter;
    }
    
    private Flow loadFlow(Long flowId) {
        return flowRepository.find(flowId);
    }
    
    private Flow lockFlow(ManagedMessage managedMessage) {
        return flowRepository.lock(managedMessage.getFlowId());
    }
    
    private static FlowFinderCriteria repositoryFinderCriteria(FlowInfoFinderCriteria flowInfoFinderCriteria) {
        return new FlowFinderCriteria(
                flowInfoFinderCriteria.getFrom(),
                flowInfoFinderCriteria.getTo(),
                flowInfoFinderCriteria.getApplication(),
                flowInfoFinderCriteria.getMaxResults(), 
                flowInfoFinderCriteria.getInboundMessageQuery(),
                flowInfoFinderCriteria.getOutboundMessageQuery());
    }
    
}
