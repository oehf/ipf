/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Common base class of all XDS meta data classes.
 * @author Jens Riemschneider
 */
public abstract class XDSMetaClass {
    private AvailabilityStatus availabilityStatus;
    private LocalizedString comments;
    private String entryUUID;
    private Identifiable patientID;
    private LocalizedString title;
    private String uniqueID;

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public LocalizedString getComments() {
        return comments;
    }

    public void setComments(LocalizedString comments) {
        this.comments = comments;
    }

    public String getEntryUUID() {
        return entryUUID;
    }

    public void setEntryUUID(String entryUUID) {
        this.entryUUID = entryUUID;
    }

    public Identifiable getPatientID() {
        return patientID;
    }

    public void setPatientID(Identifiable patientID) {
        this.patientID = patientID;
    }

    public LocalizedString getTitle() {
        return title;
    }

    public void setTitle(LocalizedString title) {
        this.title = title;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((availabilityStatus == null) ? 0 : availabilityStatus.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result + ((entryUUID == null) ? 0 : entryUUID.hashCode());
        result = prime * result + ((patientID == null) ? 0 : patientID.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((uniqueID == null) ? 0 : uniqueID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XDSMetaClass other = (XDSMetaClass) obj;
        if (availabilityStatus == null) {
            if (other.availabilityStatus != null)
                return false;
        } else if (!availabilityStatus.equals(other.availabilityStatus))
            return false;
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        if (entryUUID == null) {
            if (other.entryUUID != null)
                return false;
        } else if (!entryUUID.equals(other.entryUUID))
            return false;
        if (patientID == null) {
            if (other.patientID != null)
                return false;
        } else if (!patientID.equals(other.patientID))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (uniqueID == null) {
            if (other.uniqueID != null)
                return false;
        } else if (!uniqueID.equals(other.uniqueID))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}