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
package org.openehealth.ipf.commons.flow.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.openehealth.ipf.commons.flow.transfer.FlowPartInfo;

/**
 * @author Martin Krasser
 * @author Mitko Kolev 
 */
@Indexed(index="messages.idx")
@Entity
@Table(name = "T_FLOW_PART")
public class FlowPart {

    @DocumentId
    @Id
    @Column(name="C_ID", length=128)
    private final String identifier; // internal
    
    @Column(name="C_STATUS")
    private FlowStatus status;

    @Column(name="C_PATH")
    private String path;
    
    @Column(name="C_CONTRIBUTION_TIME")
    private Date contributionTime;
    
    @Column(name="C_CONTRIBUTION_COUNT")
    private int contributionCount;
    
    @Column(name="C_FILTER_TIME")
    private Date filterTime;
    
    @Column(name="C_FILTER_COUNT")
    private int filterCount;
    
    @Column(name="C_FLOW_ID")
    private Long flowId;
    
    @IndexedEmbedded(depth = 1)
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FLOW_PART_MESSAGE_ID", unique=true, nullable=true, updatable=true)
    @Cascade({CascadeType.ALL})
    private FlowPartMessage flowPartMessage;
    
    
    
    public FlowPart() {
        identifier = UUID.randomUUID().toString();
        contributionCount = 0;
        filterCount = 0;
    }

    public Long getFlowId() {
        return flowId;
    }
    
    public FlowStatus getStatus() {
        return status;
    }

    public void setStatus(FlowStatus status) {
        this.status = status;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String branch) {
        path = branch;
    }

    public Date getContributionTime() {
        return contributionTime;
    }

    public void setContributionTime(Date contributionTime) {
        this.contributionTime = contributionTime;
    }
    
    public int getContributionCount() {
        return contributionCount;
    }

    public void setContributionCount(int contributionCount) {
        this.contributionCount = contributionCount;
    }

    public Date getFilterTime() {
        return filterTime;
    }

    public void setFilterTime(Date filterTime) {
        this.filterTime = filterTime;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public void incrementContributionCount() {
        contributionCount++;
    }
    
    public void incrementFilterCount() {
        filterCount++;
    }
    /**
     * 
     * Reads the readable text associated with the flow packet from the
     * database.
     * 
     * @return The readable text of the flow packet. If such does not exist,
     *         returns null.
     */
    public String getFlowPartMessageText() {
        if (flowPartMessage != null) {
            return flowPartMessage.getText();
        }
        return null;
    }

    FlowPartMessage getFlowPartMessage() {
        return flowPartMessage;
    }
    
    /**
     * Sets the readable text representation of the flow packet. If the message
     * is null, after the current transaction is committed the existing message
     * in the database (if such exists) will be deleted.
     * 
     * @param message
     *            the text associated with the packet of this flow.
     */
    public void setFlowPartMessageText(String message) {
        if (message == null) {
            flowPartMessage = null;
        } else {
            flowPartMessage = new FlowPartMessage(message);
        }
    }
    
    public Date getLatestUpdate() {
        if (contributionTime == null) {
            return filterTime;
        } else if (filterTime == null) {
            return contributionTime;
        } else if (filterTime.after(contributionTime)) {
            return filterTime;
        } else {
            return contributionTime;
        }
    }
    
    public FlowPartInfo getInfo(Flow owner) {
        return getInfo(owner, false);
    }
    
    public FlowPartInfo getInfo(Flow owner, boolean includeText) {
        FlowPartInfo info = new FlowPartInfo();
        info.setStatus(status.toString());
        info.setPath(path);
        info.setPathDuration(owner.getPartDuration(this));
        info.setContributionTime(contributionTime);
        info.setContributionCount(contributionCount);
        info.setFilterTime(filterTime);
        info.setFilterCount(filterCount);
        if (includeText) {
            info.setText(getFlowPartMessageText());
        }
        return info;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FlowPart)) {
            return false;
        }
        FlowPart p = (FlowPart)obj;
        return identifier.equals(p.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

}
