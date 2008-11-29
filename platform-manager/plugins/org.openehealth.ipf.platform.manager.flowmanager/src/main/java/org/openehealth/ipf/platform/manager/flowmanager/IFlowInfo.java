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
import java.util.List;

/**
 * Hides the org.openehealth.ipf.commons.flow.transfer.FlowInfo. Restricts the
 * use of the server side interface only in this OSGI bundle. The implementation
 * of the interface will act as a proxy.
 * 
 * @author Mitko Kolev
 */
public interface IFlowInfo {

    public String getStatus();

    public String getApplication();

    public int getAckCount();

    public int getAckCountExpected();

    public Date getCreationTime();

    public Long getIdentifier();

    public int getNakCount();

    public boolean isReplayable();

    public int getReplayCount();

    public Date getReplayTime();

    public List<IFlowPartInfo> getPartInfos();
}
