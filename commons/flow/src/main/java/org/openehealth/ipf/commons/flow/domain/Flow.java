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

import static org.openehealth.ipf.commons.flow.domain.FlowStatus.CLEAN;
import static org.openehealth.ipf.commons.flow.domain.FlowStatus.ERROR;
import static org.openehealth.ipf.commons.flow.transfer.FlowInfo.ACK_COUNT_EXPECTED_UNDEFINED;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;

/**
 * @author Martin Krasser
 * @author Mitko Kolev 
 */
@Indexed(index="messages.idx")
@Entity
@Table(name = "T_FLOW")
public class Flow {

    // We currently use our own sequence number generation strategy because
    // Hibernate lock upgrade mechanisms don't work with Derby. If you want
    // to use Hibernate's generic sequence number generator annotate the
    // identifier field additionally with:
    // 
    // @GeneratedValue(strategy=GenerationType.TABLE, generator="T_FLOW_SEQ")
    // @SequenceGenerator(name="T_FLOW_SEQ", sequenceName="T_FLOW_SEQ", 
    //         initialValue=1, 
    //         allocationSize=1
    // )
    // 
    // using these imports:
    //
    // import javax.persistence.GeneratedValue;
    // import javax.persistence.GenerationType;
    // import javax.persistence.SequenceGenerator;
    //
    // The above steps are only required if the database doesn't support
    // sequences natively. 
    
    @Id
    @Column(name="C_ID")
    @DocumentId
    private Long identifier;
    
    @Column(name="C_APPLICATION")
    @Index(name="C_APPLICATION_IDX")
    private String application;
    
    @Lob
    @Column(name="C_PACKET", length=Integer.MAX_VALUE)
    private byte[] packet;

    @Column(name="C_CREATION_TIME")
    private Date creationTime;
    
    @Column(name="C_REPLAY_TIME")
    private Date replayTime;
    
    @Column(name="C_REPLAY_COUNT")
    private int replayCount;
    
    // Integer for backwards compatibility
    // with older databases (field is null)
    @Column(name="C_ACK_COUNT_EXPECTED")
    private Integer ackCountExpected;
    
    // Derived status for query optimization
    // (set on insert and update)
    @Column(name="C_DERIVED_STATUS")
    private FlowStatus derivedStatus;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name = "C_FLOW_ID")
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<FlowPart> parts;
    
    @IndexedEmbedded(depth = 1)
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FLOW_MESSAGE_ID", unique=false, nullable=true, updatable=true)
    @Cascade({CascadeType.ALL})
    private FlowMessage flowMessage;
    
    /**
     * Creates a flow.
     */
    public Flow() {
        replayCount = 0;
        derivedStatus = CLEAN;
        ackCountExpected = ACK_COUNT_EXPECTED_UNDEFINED;
    }
    
    /**
     * Creates a flow in context of the given <code>application</code>.
     * 
     * @param application an application identifier.
     */
    public Flow(String application) {
        this.application = application;
        creationTime = currentTime();
        derivedStatus = CLEAN;
        replayCount = 0;
    }
    
    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * Lazily gets the packet from the database. This method <strong>must</strong>
     * be called within a transaction.
     * 
     * @return a byte[] or <code>null</code>.
     */
    public byte[] getPacket() {
        return packet;
    }
    
    /**
     * Writes the given packet to the database. This method <strong>must</strong>
     * be called within a transaction.
     * 
     * @param packet
     *            a byte array or <code>null</code>.
     */
    public void setPacket(byte[] packet) {
        this.packet = packet;
    }
    
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date start) {
        creationTime = start;
    }

    public Date getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Date replayTime) {
        this.replayTime = replayTime;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public void incrementReplayCount() {
        replayCount++;
    }
    
    public int getAckCountExpected() {
        if (ackCountExpected == null) {
            return ACK_COUNT_EXPECTED_UNDEFINED;
        }
        return ackCountExpected;
    }

    public void setAckCountExpected(int ackCountExpected) {
        this.ackCountExpected = ackCountExpected;
    }

    public FlowStatus getDerivedStatus() {
        // Support DB schema upgrade
        if (derivedStatus == null) {
            return CLEAN;
        }
        return derivedStatus;
    }

    public void setDerivedStatus(FlowStatus derivedStatus) {
        this.derivedStatus = derivedStatus;
    }

    public Set<FlowPart> getParts() {
        if (parts == null) {
            parts = new HashSet<>();
        }
        return parts;
    }
    
    public FlowPart getPart(String path, FlowStatus status) {
        for (FlowPart part : getParts()) {
            if (path.equals(part.getPath()) && (status == part.getStatus())) {
                return part;
            }
        }
        return null;
    }
    
    public FlowPart getPart(String path) {
        for (FlowPart part : getParts()) {
            if (part.getPath().equals(path)) {
                return part;
            }
        }
        return null;
    }

    FlowMessage getFlowMessage() {
        return flowMessage;
    }
    
    /**
     * Reads the readable text associated with the flow packet from the
     * database.
     * 
     * @return The readable text of the flow packet. If such does not exist,
     *         returns <code>null</code>.
     */
    public String getFlowMessageText() {
        if (flowMessage != null) {
            return flowMessage.getText();
        }
        return null;
    }
    
    /**
     * Sets the readable text representation of the flow packet. If the message
     * is null, after the current transaction is committed the existing message
     * in the database (if such exists) will be deleted.
     * 
     * 
     * @param message
     *            the text associated with the packet of this flow.
     */
    public void setFlowMessageText(String message) {
        if (message == null) {
            flowMessage = null;
        } else {
            flowMessage = new FlowMessage(message);
        }
    }
  
    public boolean isAckCountExpectationSet() {
        return getAckCountExpected() != ACK_COUNT_EXPECTED_UNDEFINED;
    }
    
    public boolean isAckCountExpectedReached() {
        if (getAckCountExpected() == ACK_COUNT_EXPECTED_UNDEFINED) {
            return false;
        }
        return getStatusCount(FlowStatus.CLEAN) >= getAckCountExpected();
    }
    
    public int getStatusCount(FlowStatus status) {
        int result = 0;
        for (FlowPart part : getParts()) {
            if (part.getStatus() == status) {
                result++;
            }
        }
        return result;
    }
    
    public FlowStatus getStatus() {
        if (parts != null) {
            for (FlowPart part : parts) {
                if (part.getStatus() == ERROR) {
                    return ERROR;
                }
            }
        }
        return CLEAN;
    }
    
    public void clearErrorStatus() {
        setDerivedStatus(CLEAN);
        for (Iterator<FlowPart> iter = parts.iterator(); iter.hasNext();) {
            FlowPart part = iter.next();
            if (part.getStatus() == ERROR) {
                iter.remove();
            }
        }
    }
    
    public Date getLatestUpdate() {
        if (replayTime != null) {
            return replayTime;
        } else {
            return creationTime;
        }
    }
    
    public long getPartDuration(FlowPart part) {
        Date fu = getLatestUpdate();
        Date pu = part.getLatestUpdate();
        if (fu == null || pu == null) {
            return -1;
        }
        return pu.getTime() - fu.getTime();
    }
    
 
    /**
     * Acknowledges this flow for the given path. The flow part representing
     * this path is set to status {@link FlowStatus#CLEAN}. If
     * <code>cleanup</code> is set to <code>true</code> then the
     * <code>packet</code> field containing the initial message is set to
     * <code>null</code> but only if the number of expected acknowledgements
     * has been reached. This saves disk storage but prevents the flow from
     * being replayed. Clears the flow part message text.
     *  
     * @param path
     *          a flow path.
     * @param cleanup
     *         <code>true</code> to enable cleanup of the initial message
     *            if the expected acknowledgement count has been reached.
     *            
     * @see #acknowledge(String, boolean, String)
     * @see #setFlowMessageText(String)
     *
     */
    public void acknowledge(String path, boolean cleanup) {
        acknowledge(path, cleanup, null);
    }
    /**
     * Acknowledges this flow for the given path. The flow part representing
     * this path is set to status {@link FlowStatus#CLEAN}. If
     * <code>cleanup</code> is set to <code>true</code> then the
     * <code>packet</code> field containing the initial message is set to
     * <code>null</code> but only if the number of expected acknowledgements has
     * been reached. This saves disk storage but prevents the flow from being
     * replayed. Saves the partTextMessage in the FlowPart.
     * 
     * @param path
     *            a flow path.
     * @param cleanup
     *            <code>true</code> to enable cleanup of the initial message if
     *            the expected acknowledgement count has been reached.
     * @param partMessageText
     *            a the part text message for the FlowPart with path
     *            <code>path<code>
     *            to be saved.
     * 
     * @see #setFlowMessageText(String)
     */
    public void acknowledge(String path, boolean cleanup, String partMessageText) {
        FlowPart part = update(path, CLEAN);
        if (cleanup && isAckCountExpectedReached()) {
            setPacket(null);
            // clear the flow message
            setFlowMessageText(null);
            // clear the message in the flow parts
            for (FlowPart p : getParts()) {
                p.setFlowPartMessageText(null);
            }
        } else {
            part.setFlowPartMessageText(partMessageText);
        }
    }
    
    
    /**
     * Invalidates this flow for the given path. The flow part representing this
     * path is set to status {@link FlowStatus#ERROR}. Clears the flowPart message
     * text.
     * 
     * @param path 
     *            a flow path.
     */
    public void invalidate(String path) {
        invalidate(path, null);
    }
    
    /**
     * Invalidates this flow for the given path. The flow part representing this
     * path is set to status {@link FlowStatus#ERROR}.
     * 
     * @param path
     *            a flow path.
     * @param partTextMessage
     *            a the part text message of the FlowPart with path
     *            <code>path<code>
     *            to be saved.
     */
    public void invalidate(String path, String partTextMessage) {
        FlowPart part = update(path, ERROR);
        part.setFlowPartMessageText(partTextMessage);
        setDerivedStatus(ERROR); // query optimization

    }
    
    /**
     * Updates this flow for the given path. The flow part representing this
     * path and status is created or updated by incrementing the contribution
     * count and setting the contribution time to the current time.
     * 
     * @param path
     *            flow path.
     * @param status
     *            flow status to set.
     * @return the updated FlowPart
     * 
     * @see FlowPart
     */
    protected FlowPart update(String path, FlowStatus status) {
        FlowPart flowPart = getPart(path, status);
        if (flowPart == null) {
            flowPart = new FlowPart();
            flowPart.setPath(path);
            flowPart.setStatus(status);
            getParts().add(flowPart);
        }
        flowPart.incrementContributionCount();
        flowPart.setContributionTime(currentTime());
        return flowPart;
    }
    
    /**
     * Determines whether to filter this flow at a given path. If the flow
     * should be filtered this method returns <code>true</code> and the filter
     * count is incremented by 1. The filter time is updated to the current
     * time.
     * 
     * @param path a flow path.
     * @return a hint to filter implementations to filter out this flow at the
     *         given path.
     */
    public boolean filter(String path) {
        FlowPart flowPart = getPart(path, FlowStatus.CLEAN);
        if (flowPart == null) {
            return false;
        }
        flowPart.setFilterTime(currentTime());
        flowPart.incrementFilterCount();
        return true;
    }
    
    /**
     * Returns <code>true</code> if this flow can be replayed. A flow can only
     * be replayed if the initial message (packet) is stored i.e. the packet
     * must not be <code>null</code>.
     * 
     * @return <code>true</code> if the flow can be replayed.
     */
    public boolean isReplayable() {
        return packet != null;
    }
    
    /**
     * Prepares this flow for a replay by updating the replay time and replay
     * count. Clears the message text of the flow parts. 
     * 
     * @return the message data (packet) used for replay.
     */
    public byte[] prepareReplay() {
        setReplayTime(currentTime());
        incrementReplayCount();
        clearErrorStatus();
        for (FlowPart part: getParts()){
            if (part.getStatus().equals(FlowStatus.ERROR)){
                part.setFlowPartMessageText(null);
            }
        }
        return getPacket();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Flow)) {
            return false;
        }
        Flow t = (Flow)obj;
        return identifier.equals(t.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    public FlowInfo getInfo() {
        return getInfo(false);
    }
    
    public FlowInfo getInfo(boolean includeText) {
        FlowInfo info = new FlowInfo();
        info.setIdentifier(identifier);
        info.setApplication(application);
        info.setCreationTime(creationTime);
        info.setReplayable(isReplayable());
        info.setReplayTime(replayTime);
        info.setReplayCount(replayCount);
        info.setStatus(getStatus().toString());
        info.setAckCountExpected(getAckCountExpected());
        info.setAckCount(getStatusCount(FlowStatus.CLEAN));
        info.setNakCount(getStatusCount(FlowStatus.ERROR));
        if (includeText) {
            info.setText(getFlowMessageText());
        }
        for (FlowPart part : getParts()) {
            info.getPartInfos().add(part.getInfo(this, includeText));
        }
        return info;
    }
    
    public static List<FlowInfo> getInfos(List<Flow> flows) {
        List<FlowInfo> result = new ArrayList<>(flows.size());
        for (Flow flow : flows) {
            result.add(flow.getInfo());
        }
        return result;
    }
    
    private static Date currentTime() {
        return new Date();
    }
    
}

