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

import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.commons.flow.transfer.FlowInfoFinderCriteria;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Krasser
 */
public interface FlowManager {

    @Transactional(readOnly=true)
    List<Long> findFlowIds(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional(readOnly=true)
    List<Long> findErrorFlowIds(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional(readOnly=true)
    List<Long> findUnackFlowIds(FlowInfoFinderCriteria finderCriteria);

    @Transactional(readOnly=true)
    FlowInfo findFlow(Long flowId);
    
    @Transactional(readOnly=true)
    FlowInfo findFlow(Long flowId, boolean includeText);
    
    @Transactional(readOnly=true)
    boolean flowCompleted(Long flowId);
    
    @Transactional(readOnly=true)
    List<FlowInfo> findFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional(readOnly=true)
    List<FlowInfo> findErrorFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional(readOnly=true)
    List<FlowInfo> findUnackFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional(readOnly=true)
    String findFlowMessageText(Long flowId);
    
    @Transactional(readOnly=true)
    String findFlowPartMessageText(Long flowId, String flowPartPath);
    
    @Transactional
    int replayFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional
    int replayErrorFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional
    int replayUnackFlows(FlowInfoFinderCriteria finderCriteria);
    
    @Transactional
    void replayFlow(Long flowId);
    
    @Transactional
    Long beginFlow(ManagedMessage managedMessage, String application);

    @Transactional
    Long beginFlow(ManagedMessage managedMessage, String application, int ackCountExpected);

    @Transactional
    void acknowledgeFlow(ManagedMessage managedMessage);

    @Transactional
    void invalidateFlow(ManagedMessage managedMessage);
    
    @Transactional
    boolean filterFlow(ManagedMessage managedMessage);
    
    @Transactional
    void setFlowFilterEnabled(String application, boolean flowFilterEnabled);
    
    @Transactional(readOnly=true)
    boolean isFlowFilterEnabled(String application);
    
    @Transactional
    void setFlowCleanupEnabled(String application, boolean flowCleanupEnabled);
    
    @Transactional(readOnly=true)
    boolean isFlowCleanupEnabled(String application);
    
}
