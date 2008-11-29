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
import static org.openehealth.ipf.commons.flow.transfer.FlowInfoUtils.textString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Martin Krasser
 */
@XmlRootElement(name="flowInfo", 
        namespace = "http://www.openehealth.org/ipf/commons/flow/types/1.0")
@XmlType(
        namespace = "http://www.openehealth.org/ipf/commons/flow/types/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowInfo implements Serializable {

    public static final int ACK_COUNT_EXPECTED_UNDEFINED = -1;
    
    private static final long serialVersionUID = -2604969744637192040L;
    
    @XmlElement(required=true)
    private Long identifier;
    
    @XmlElement
    private String status;
    
    @XmlElement
    private String application;
    
    @XmlElement
    private Date creationTime;
    
    @XmlElement(type=Boolean.class)
    private boolean replayable;
    
    @XmlElement
    private Date replayTime;
    
    @XmlElement(type=Integer.class)
    private int replayCount;
    
    @XmlElement(type=Integer.class)
    private int ackCount;
    
    @XmlElement(type=Integer.class)
    private int ackCountExpected;
    
    @XmlElement(type=Integer.class)
    private int nakCount;

    @XmlElement
    private String text;
    
    @XmlElementRef(name="flow-part-infos")
    private Set<FlowPartInfo> partInfos;
    
    public FlowInfo() {
        partInfos = new HashSet<FlowPartInfo>();
        ackCountExpected = ACK_COUNT_EXPECTED_UNDEFINED;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getAckCount() {
        return ackCount;
    }

    public void setAckCount(int ackCount) {
        this.ackCount = ackCount;
    }

    public int getAckCountExpected() {
        return ackCountExpected;
    }

    public void setAckCountExpected(int ackCountExpected) {
        this.ackCountExpected = ackCountExpected;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public int getNakCount() {
        return nakCount;
    }

    public void setNakCount(int nakCount) {
        this.nakCount = nakCount;
    }

    public boolean isReplayable() {
        return replayable;
    }

    public void setReplayable(boolean replayable) {
        this.replayable = replayable;
    }

    public int getReplayCount() {
        return replayCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public Date getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Date replayTime) {
        this.replayTime = replayTime;
    }

    public Set<FlowPartInfo> getPartInfos() {
        return partInfos;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Flow (id = ").append(identifier).append(")").append(NEWLINE);
        buf.append("- overall status       = ").append(status).append(NEWLINE);
        buf.append("- application          = ").append(application).append(NEWLINE);
        buf.append("- create time          = ").append(dateString(creationTime)).append(NEWLINE);
        buf.append("- replayable           = ").append(replayable).append(NEWLINE);
        buf.append("- replay time          = ").append(dateString(replayTime)).append(NEWLINE);
        buf.append("- replay count         = ").append(replayCount).append(NEWLINE);
        buf.append("- ACK count (expected) = ").append(ackCountExpected).append(NEWLINE);
        buf.append("- ACK count (actual)   = ").append(ackCount).append(NEWLINE);
        buf.append("- NAK count            = ").append(nakCount).append(NEWLINE);
        buf.append("- text                 = ").append(textString(text)).append(NEWLINE);
        for (FlowPartInfo partInfo : getPartInfos()) {
            buf.append(partInfo.toString());
        }
        return buf.toString();
    }
 
}
