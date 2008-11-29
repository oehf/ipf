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
package org.openehealth.ipf.platform.manager.flowmanager.mock;

import java.util.Date;

import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;

/**
 * @author Mitko Kolev
 */
public class FlowPartInfoMock implements IFlowPartInfo {
    private final String path;
    private int contributionCount;
    private Date contributionDate;
    private int filterCount;
    private Date filterTime;
    private boolean status;
    private long pathDuration;

    /**
     * Constructs a flowPart info with the given path with status CLEAN with
     * initial values.
     * 
     * @param path
     */
    public FlowPartInfoMock(String path) {
        this.path = path;
        this.status = true;
        this.contributionDate = new Date();
        this.pathDuration = 100;
        this.contributionCount = 1;
        this.filterTime = null;
        this.filterCount = 0;
    }

    @Override
    public int getContributionCount() {
        return contributionCount;
    }

    @Override
    public Date getContributionTime() {
        return contributionDate;
    }

    @Override
    public int getFilterCount() {
        return filterCount;
    }

    @Override
    public Date getFilterTime() {
        return filterTime;
    }

    
    @Override
    public String getPath() {
        return path;
    }

  
    @Override
    public long getPathDuration() {
        return pathDuration;
    }

    
    @Override
    public String getStatus() {
        if (status == true)
            return "CLEAN";
        return "ERROR";
    }

    public void setContributionCount(int contributionCount) {
        this.contributionCount = contributionCount;
    }

    public void setContributionDate(Date contributionDate) {
        this.contributionDate = contributionDate;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public void setFilterTime(Date filterTime) {
        this.filterTime = filterTime;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPathDuration(long pathDuration) {
        this.pathDuration = pathDuration;
    }

}
