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

import java.util.Date;

import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerSearchCriteria;

/**
 * Bean representing search criteria.
 * <p>
 * 
 * @see IFlowManagerSearchCriteria
 * 
 * @author Mitko Kolev
 */
public class FlowManagerSearchCriteriaImpl implements
        IFlowManagerSearchCriteria {

    private final Date from;

    private final Date to;

    private final boolean isRestrictedToAciveFlows;

    private final boolean isRestrictedToErrorFlows;

    /**
     * Creates FlowManagerSearchCriteriaImpl
     * 
     * @param from
     *            the begin date of the results.
     * @param to
     *            the end date of the results
     * @param showOnlyActive
     *            restrict the results to active flows
     * @param showOnlyError
     *            restricts the results only to error flows.
     */
    public FlowManagerSearchCriteriaImpl(Date from, Date to, boolean restrict,
            boolean showOnlyError) {
        this.from = from;
        this.to = to;
        this.isRestrictedToAciveFlows = restrict;
        this.isRestrictedToErrorFlows = showOnlyError;
    }

    @Override
    public Date getFromDate() {
        return from;
    }

    @Override
    public Date getToDate() {
        return to;
    }

    @Override
    public boolean isRestrictedToUnacknowledgedFlows() {
        return isRestrictedToAciveFlows;
    }

    @Override
    public boolean isRestrictedToErrorFlows() {
        return isRestrictedToErrorFlows;
    }
}
