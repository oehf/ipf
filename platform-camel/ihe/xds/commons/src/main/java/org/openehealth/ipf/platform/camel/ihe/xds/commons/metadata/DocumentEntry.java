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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an XDS document entry according to the IHE XDS specification.
 * 
 * @author Jens Riemschneider
 */
public class DocumentEntry {
    private Author author;
    private AvailabilityStatus availabilityStatus;
    private Code classCode;
    private LocalizedString comments;
    private final List<Code> confidentialityCodes = new ArrayList<Code>();
    private String creationTime;
    private EntryUUID entryUUID;
    private final List<Code> eventCodeList = new ArrayList<Code>();
    private Code formatCode;
    private String hash;
    private Code healthcareFacilityTypeCode;
    private String languageCode;
    private Person legalAuthenticator;
    private String mimeType;
    private Identifiable patientID;
    private Code practiceSettingCode;
    private String serviceStartTime;
    private String serviceStopTime;
    private Long size;
    private Identifiable sourcePatientID;
    private PatientInfo sourcePatientInfo;
    private LocalizedString title;
    private Code typeCode;
    private UniqueID uniqueID;
    private String uri;
    private String repositoryUniqueId; 

    /**
     * @return the author information. Can be <code>null</code>.
     */
    public Author getAuthor() {
        return author;
    }
    
    /**
     * @param author
     *          the author information. Can be <code>null</code>.
     */
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    public Code getClassCode() {
        return classCode;
    }
    
    public void setClassCode(Code classCode) {
        this.classCode = classCode;
    }
    
    public LocalizedString getComments() {
        return comments;
    }
    
    public void setComments(LocalizedString comments) {
        this.comments = comments;
    }
    
    public String getCreationTime() {
        return creationTime;
    }
    
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    
    public EntryUUID getEntryUUID() {
        return entryUUID;
    }
    
    public void setEntryUUID(EntryUUID entryUUID) {
        this.entryUUID = entryUUID;
    }
    
    public Code getFormatCode() {
        return formatCode;
    }
    
    public void setFormatCode(Code formatCode) {
        this.formatCode = formatCode;
    }
    
    public String getHash() {
        return hash;
    }
    
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public Code getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }
    
    public void setHealthcareFacilityTypeCode(Code healthcareFacilityTypeCode) {
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    }
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    public Person getLegalAuthenticator() {
        return legalAuthenticator;
    }
    
    public void setLegalAuthenticator(Person legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public Identifiable getPatientID() {
        return patientID;
    }
    
    public void setPatientID(Identifiable patientID) {
        this.patientID = patientID;
    }
    
    public Code getPracticeSettingCode() {
        return practiceSettingCode;
    }
    
    public void setPracticeSettingCode(Code practiceSettingCode) {
        this.practiceSettingCode = practiceSettingCode;
    }
    
    public String getServiceStartTime() {
        return serviceStartTime;
    }
    
    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
    
    public String getServiceStopTime() {
        return serviceStopTime;
    }
    
    public void setServiceStopTime(String serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public PatientInfo getSourcePatientInfo() {
        return sourcePatientInfo;
    }
    
    public void setSourcePatientInfo(PatientInfo sourcePatientInfo) {
        this.sourcePatientInfo = sourcePatientInfo;
    }
    
    public Identifiable getSourcePatientID() {
        return sourcePatientID;
    }

    public void setSourcePatientID(Identifiable sourcePatientID) {
        this.sourcePatientID = sourcePatientID;
    }

    public LocalizedString getTitle() {
        return title;
    }
    
    public void setTitle(LocalizedString title) {
        this.title = title;
    }
    
    public Code getTypeCode() {
        return typeCode;
    }
    
    public void setTypeCode(Code typeCode) {
        this.typeCode = typeCode;
    }
    
    public UniqueID getUniqueID() {
        return uniqueID;
    }
    
    public void setUniqueID(UniqueID uniqueID) {
        this.uniqueID = uniqueID;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }
    
    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }
    
    public List<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    public List<Code> getEventCodeList() {
        return eventCodeList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result
                + ((availabilityStatus == null) ? 0 : availabilityStatus.hashCode());
        result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((entryUUID == null) ? 0 : entryUUID.hashCode());
        result = prime * result + ((eventCodeList == null) ? 0 : eventCodeList.hashCode());
        result = prime * result + ((formatCode == null) ? 0 : formatCode.hashCode());
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime
                * result
                + ((healthcareFacilityTypeCode == null) ? 0 : healthcareFacilityTypeCode.hashCode());
        result = prime * result + ((languageCode == null) ? 0 : languageCode.hashCode());
        result = prime * result
                + ((legalAuthenticator == null) ? 0 : legalAuthenticator.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + ((patientID == null) ? 0 : patientID.hashCode());
        result = prime * result
                + ((practiceSettingCode == null) ? 0 : practiceSettingCode.hashCode());
        result = prime * result
                + ((repositoryUniqueId == null) ? 0 : repositoryUniqueId.hashCode());
        result = prime * result + ((serviceStartTime == null) ? 0 : serviceStartTime.hashCode());
        result = prime * result + ((serviceStopTime == null) ? 0 : serviceStopTime.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((sourcePatientID == null) ? 0 : sourcePatientID.hashCode());
        result = prime * result + ((sourcePatientInfo == null) ? 0 : sourcePatientInfo.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((typeCode == null) ? 0 : typeCode.hashCode());
        result = prime * result + ((uniqueID == null) ? 0 : uniqueID.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
        DocumentEntry other = (DocumentEntry) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (availabilityStatus == null) {
            if (other.availabilityStatus != null)
                return false;
        } else if (!availabilityStatus.equals(other.availabilityStatus))
            return false;
        if (classCode == null) {
            if (other.classCode != null)
                return false;
        } else if (!classCode.equals(other.classCode))
            return false;
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        if (confidentialityCodes == null) {
            if (other.confidentialityCodes != null)
                return false;
        } else if (!confidentialityCodes.equals(other.confidentialityCodes))
            return false;
        if (creationTime == null) {
            if (other.creationTime != null)
                return false;
        } else if (!creationTime.equals(other.creationTime))
            return false;
        if (entryUUID == null) {
            if (other.entryUUID != null)
                return false;
        } else if (!entryUUID.equals(other.entryUUID))
            return false;
        if (eventCodeList == null) {
            if (other.eventCodeList != null)
                return false;
        } else if (!eventCodeList.equals(other.eventCodeList))
            return false;
        if (formatCode == null) {
            if (other.formatCode != null)
                return false;
        } else if (!formatCode.equals(other.formatCode))
            return false;
        if (hash == null) {
            if (other.hash != null)
                return false;
        } else if (!hash.equals(other.hash))
            return false;
        if (healthcareFacilityTypeCode == null) {
            if (other.healthcareFacilityTypeCode != null)
                return false;
        } else if (!healthcareFacilityTypeCode.equals(other.healthcareFacilityTypeCode))
            return false;
        if (languageCode == null) {
            if (other.languageCode != null)
                return false;
        } else if (!languageCode.equals(other.languageCode))
            return false;
        if (legalAuthenticator == null) {
            if (other.legalAuthenticator != null)
                return false;
        } else if (!legalAuthenticator.equals(other.legalAuthenticator))
            return false;
        if (mimeType == null) {
            if (other.mimeType != null)
                return false;
        } else if (!mimeType.equals(other.mimeType))
            return false;
        if (patientID == null) {
            if (other.patientID != null)
                return false;
        } else if (!patientID.equals(other.patientID))
            return false;
        if (practiceSettingCode == null) {
            if (other.practiceSettingCode != null)
                return false;
        } else if (!practiceSettingCode.equals(other.practiceSettingCode))
            return false;
        if (repositoryUniqueId == null) {
            if (other.repositoryUniqueId != null)
                return false;
        } else if (!repositoryUniqueId.equals(other.repositoryUniqueId))
            return false;
        if (serviceStartTime == null) {
            if (other.serviceStartTime != null)
                return false;
        } else if (!serviceStartTime.equals(other.serviceStartTime))
            return false;
        if (serviceStopTime == null) {
            if (other.serviceStopTime != null)
                return false;
        } else if (!serviceStopTime.equals(other.serviceStopTime))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (sourcePatientID == null) {
            if (other.sourcePatientID != null)
                return false;
        } else if (!sourcePatientID.equals(other.sourcePatientID))
            return false;
        if (sourcePatientInfo == null) {
            if (other.sourcePatientInfo != null)
                return false;
        } else if (!sourcePatientInfo.equals(other.sourcePatientInfo))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (typeCode == null) {
            if (other.typeCode != null)
                return false;
        } else if (!typeCode.equals(other.typeCode))
            return false;
        if (uniqueID == null) {
            if (other.uniqueID != null)
                return false;
        } else if (!uniqueID.equals(other.uniqueID))
            return false;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }
    
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
