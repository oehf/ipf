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
 * Interface with methods with the same signatures, as those of the the
 * server-side FlowPartInfo methods.
 * <p>
 * 
 * @author Mitko Kolev (i000174)
 */
public interface IFlowPartInfo {

    public int getContributionCount();

    public Date getContributionTime();

    public int getFilterCount();

    public Date getFilterTime();

    public long getPathDuration();

    public String getPath();

    public String getStatus();
}
