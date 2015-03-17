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
package org.openehealth.ipf.commons.flow.transfer;

import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.NEWLINE;
import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.dateString;
import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.durationString;
import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.textString;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Martin Krasser
 */
@XmlRootElement(name="flowPartInfo",
        namespace = "http://www.openehealth.org/ipf/commons/flow/types/1.0")
@XmlType(
        namespace = "http://www.openehealth.org/ipf/commons/flow/types/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowPartInfo implements Serializable {

    private static final long serialVersionUID = -1524880457609230104L;

    @XmlElement
    private String status;

    @XmlElement
    private String path;
    
    @XmlElement(type=Long.class)
    private long pathDuration;
    
    @XmlElement
    private Date contributionTime;
    
    @XmlElement(type=Integer.class)
    private int contributionCount;
    
    @XmlElement
    private Date filterTime;
    
    @XmlElement(type=Integer.class)
    private int filterCount;

    @XmlElement
    private String text;
    
    public int getContributionCount() {
        return contributionCount;
    }

    public void setContributionCount(int contributionCount) {
        this.contributionCount = contributionCount;
    }

    public Date getContributionTime() {
        return contributionTime;
    }

    public void setContributionTime(Date contributionTime) {
        this.contributionTime = contributionTime;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public Date getFilterTime() {
        return filterTime;
    }

    public void setFilterTime(Date filterTime) {
        this.filterTime = filterTime;
    }

    public long getPathDuration() {
        return pathDuration;
    }

    public void setPathDuration(long partDuration) {
        pathDuration = partDuration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Flow part (path = " + path + ")" + NEWLINE + "- status             = " + status + NEWLINE + "- flow duration (ms) = " + durationString(pathDuration) + NEWLINE + "- contribution time  = " + dateString(contributionTime) + NEWLINE + "- filter time        = " + dateString(filterTime) + NEWLINE + "- contribution count = " + contributionCount + NEWLINE + "- filter count       = " + filterCount + NEWLINE + "- text               = " + textString(text) + NEWLINE;
    }
    
}
