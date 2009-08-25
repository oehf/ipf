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
package org.openehealth.ipf.commons.flow.repository;

import java.util.List;

import org.openehealth.ipf.commons.flow.domain.Flow;

/**
 * @author Martin Krasser
 */
public interface FlowRepository {

    void persist(Flow flow); 
 
    void merge(Flow flow);
    
    void remove(Flow flow);

    Flow find(Long id);
    
    Flow lock(Long id);

    int purgeFlows(FlowPurgeCriteria purgeCriteria);
    
    List<Flow> findFlows(FlowFinderCriteria finderCriteria);
    
    List<Flow> findErrorFlows(FlowFinderCriteria finderCriteria);
    
    List<Flow> findUnackFlows(FlowFinderCriteria finderCriteria);
    
    List<Long> findFlowIds(FlowFinderCriteria finderCriteria);
    
    List<Long> findErrorFlowIds(FlowFinderCriteria finderCriteria);
    
    List<Long> findUnackFlowIds(FlowFinderCriteria finderCriteria);
    
}
