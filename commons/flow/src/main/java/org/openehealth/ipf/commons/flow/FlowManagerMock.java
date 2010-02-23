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
import org.openehealth.ipf.commons.flow.repository.FlowPurgeCriteria;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowInfoFinderCriteria;

/**
 * @author Martin Krasser
 */
public class FlowManagerMock implements FlowManager {

    @Override
    public FlowInfo findFlow(Long flowId) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public FlowInfo findFlow(Long flowId, boolean includeText) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean flowCompleted(Long flowId) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<FlowInfo> findFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<FlowInfo> findErrorFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<FlowInfo> findUnackFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    @Override
    public List<Long> findFlowIds(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<Long> findErrorFlowIds(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<Long> findUnackFlowIds(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }
   
    @Override
    public String findFlowMessageText(Long flowId) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String findFlowPartMessageText(Long flowId, String flowPartPath) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int purgeFlows(FlowPurgeCriteria purgeCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int replayFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int replayErrorFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public int replayUnackFlows(FlowInfoFinderCriteria finderCriteria) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    @Override
    public void acknowledgeFlow(ManagedMessage managedMessage) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void invalidateFlow(ManagedMessage managedMessage) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean filterFlow(ManagedMessage managedMessage) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    @Override
    public Long beginFlow(ManagedMessage managedMessage, String application) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Long beginFlow(ManagedMessage managedMessage, String application, int ackCountExpected) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void replayFlow(Long flowId) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean isFlowFilterEnabled(String application) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setFlowFilterEnabled(String application, boolean flowFilterEnabled) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public boolean isFlowCleanupEnabled(String application) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void setFlowCleanupEnabled(String application, boolean flowCleanupEnabled) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public List<ApplicationConfig> findApplicationConfigs() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public ApplicationConfig getApplicationConfig(String application) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void mergeApplicationConfig(ApplicationConfig applicationConfig) {
        throw new UnsupportedOperationException("not implemented");
    }
    
}
