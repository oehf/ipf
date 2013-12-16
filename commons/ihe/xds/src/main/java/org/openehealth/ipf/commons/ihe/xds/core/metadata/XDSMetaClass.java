/*
 * Copyright 2009-2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Common base class of all XDS meta data classes.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentifiedObject", propOrder = {
        "homeCommunityId", "entryUuid", "logicalUuid", "version", "uniqueId",
        "patientId", "availabilityStatus", "title", "comments", "extraMetadata"})
abstract public class XDSMetaClass implements Serializable, ExtraMetadataHolder {
    private static final long serialVersionUID = -1193012076778493996L;
    
    private AvailabilityStatus availabilityStatus;
    private LocalizedString comments;
    private String entryUuid;
    private Identifiable patientId;
    private LocalizedString title;
    private String uniqueId;
    private String homeCommunityId;
    private String logicalUuid;
    private Version version;
    @Getter @Setter private Map<String, ArrayList<String>> extraMetadata;

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

    /**
     * @return the logical UUID of the entry.
     */
    public String getLogicalUuid() {
        return logicalUuid;
    }

    /**
     * @param logicalUuid
     *          the logical UUID of the entry.
     */
    public void setLogicalUuid(String logicalUuid) {
        this.logicalUuid = logicalUuid;
    }

    /**
     * @return the version of the entry.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @param version
     *          the version of the entry.
     */
    public void setVersion(Version version) {
        this.version = version;
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
        result = prime * result + ((logicalUuid == null) ? 0 : logicalUuid.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((extraMetadata == null) ? 0 : extraMetadata.hashCode());
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
        if (logicalUuid == null) {
            if (other.logicalUuid != null)
                return false;
        } else if (!logicalUuid.equals(other.logicalUuid))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (extraMetadata == null) {
            if (other.extraMetadata != null)
                return false;
        } else if (!extraMetadata.equals(other.extraMetadata))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}