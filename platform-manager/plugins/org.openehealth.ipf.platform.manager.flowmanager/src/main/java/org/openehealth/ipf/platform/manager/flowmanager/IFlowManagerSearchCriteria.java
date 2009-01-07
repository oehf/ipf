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
package org.openehealth.ipf.platform.manager.flowmanager;

import java.util.Date;

/**
 * Flow-search criteria (not an interface of a transfer object). Used
 * internally.
 * <p>
 * 
 * @author Mitko Kolev
 */
public interface IFlowManagerSearchCriteria {

    public Date getFromDate();

    public Date getToDate();

    public boolean isRestrictedToUnacknowledgedFlows();

    public boolean isRestrictedToErrorFlows();
    
    public String getIncomingFlowMessageSearchExpression();
    
    public boolean hasFullTextSearchExpression();

}
