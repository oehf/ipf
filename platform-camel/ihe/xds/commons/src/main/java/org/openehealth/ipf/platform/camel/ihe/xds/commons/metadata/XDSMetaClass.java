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
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public abstract class XDSMetaClass {
    private AvailabilityStatus availabilityStatus;
    private LocalizedString comments;
    private String entryUuid;
    private Identifiable patientId;
    private LocalizedString title;
    private String uniqueId;
    private String homeCommunityId;

    /**
     * @return the availability status.
     */
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * @param availabilityStatus
     *          the availability status.
     */
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * @return the comments.
     */
    public LocalizedString getComments() {
        return comments;
    }

    /**
     * @param comments
     *          the comments.
     */
    public void setComments(LocalizedString comments) {
        this.comments = comments;
    }

    /**
     * @return the UUID of the entry.
     */
    public String getEntryUuid() {
        return entryUuid;
    }

    /**
     * @param entryUuid 
     *          the UUID of the entry.
     */
    public void setEntryUuid(String entryUuid) {
        this.entryUuid = entryUuid;
    }

    /**
     * @return the patient ID.
     */
    public Identifiable getPatientId() {
        return patientId;
    }

    /**
     * @param patientId
     *          the patient ID.
     */
    public void setPatientId(Identifiable patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the title.
     */
    public LocalizedString getTitle() {
        return title;
    }

    /**
     * @param title
     *          the title.
     */
    public void setTitle(LocalizedString title) {
        this.title = title;
    }

    /**
     * @return the unique ID of the entry.
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId
     *          the unique ID of the entry.
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * @return the ID of the community that this document was created in.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * @param homeCommunityId
     *          the ID of the community that this document was created in.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((availabilityStatus == null) ? 0 : availabilityStatus.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result + ((entryUuid == null) ? 0 : entryUuid.hashCode());
        result = prime * result + ((homeCommunityId == null) ? 0 : homeCommunityId.hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
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
        if (entryUuid == null) {
            if (other.entryUuid != null)
                return false;
        } else if (!entryUuid.equals(other.entryUuid))
            return false;
        if (homeCommunityId == null) {
            if (other.homeCommunityId != null)
                return false;
        } else if (!homeCommunityId.equals(other.homeCommunityId))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (uniqueId == null) {
            if (other.uniqueId != null)
                return false;
        } else if (!uniqueId.equals(other.uniqueId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}