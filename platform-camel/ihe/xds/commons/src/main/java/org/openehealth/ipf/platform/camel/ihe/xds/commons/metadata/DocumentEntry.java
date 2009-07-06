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
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
public class DocumentEntry extends XDSMetaClass {
    private final List<Author> authors = new ArrayList<Author>();
    private Code classCode;
    private final List<Code> confidentialityCodes = new ArrayList<Code>();
    private String creationTime;
    private final List<Code> eventCodeList = new ArrayList<Code>();
    private Code formatCode;
    private String hash;
    private Code healthcareFacilityTypeCode;
    private String languageCode;
    private Person legalAuthenticator;
    private String mimeType;
    private Code practiceSettingCode;
    private String serviceStartTime;
    private String serviceStopTime;
    private Long size;
    private Identifiable sourcePatientID;
    private PatientInfo sourcePatientInfo;
    private Code typeCode;
    private String uri;
    private String repositoryUniqueId;
    private String homeCommunityId;

    /**
     * @return the list of authors of the document. Cannot be <code>null</code>.
     */
    public List<Author> getAuthors() {
        return authors;
    }
    
    /**
     * @return the class code of the entry.
     */
    public Code getClassCode() {
        return classCode;
    }
    
    /**
     * @param classCode
     *          the class code of the entry.
     */
    public void setClassCode(Code classCode) {
        this.classCode = classCode;
    }
    
    /**
     * @return the time of the document creation.
     */
    public String getCreationTime() {
        return creationTime;
    }
    
    /**
     * @param creationTime
     *          the time of the document creation.
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    
    /**
     * @return the code of the document's format.
     */
    public Code getFormatCode() {
        return formatCode;
    }
    
    /**
     * @param formatCode
     *          the code of the document's format.
     */
    public void setFormatCode(Code formatCode) {
        this.formatCode = formatCode;
    }
    
    /**
     * @return the hash calculated via the document's content.
     */
    public String getHash() {
        return hash;
    }
    
    /**
     * @param hash
     *          the hash calculated via the document's content.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    /**
     * @return the code of the healthcare facility creating the document.
     */
    public Code getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }
    
    /**
     * @param healthcareFacilityTypeCode
     *          the code of the healthcare facility creating the document.
     */
    public void setHealthcareFacilityTypeCode(Code healthcareFacilityTypeCode) {
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    }
    
    /**
     * @return the language used within the document.
     */
    public String getLanguageCode() {
        return languageCode;
    }
    
    /**
     * @param languageCode
     *          the language used within the document.
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
    
    /**
     * @return the person that is the legal authenticator of the document.
     */
    public Person getLegalAuthenticator() {
        return legalAuthenticator;
    }
    
    /**
     * @param legalAuthenticator
     *          the person that is the legal authenticator of the document.
     */
    public void setLegalAuthenticator(Person legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }
    
    /**
     * @return the mime type of the document.
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * @param mimeType
     *          the mime type of the document.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    /**
     * @return the code of the practice setting.
     */
    public Code getPracticeSettingCode() {
        return practiceSettingCode;
    }
    
    /**
     * @param practiceSettingCode
     *          the code of the practice setting.
     */
    public void setPracticeSettingCode(Code practiceSettingCode) {
        this.practiceSettingCode = practiceSettingCode;
    }
    
    /**
     * @return the time the service was started.
     */
    public String getServiceStartTime() {
        return serviceStartTime;
    }
    
    /**
     * @param serviceStartTime
     *          the time the service was started.
     */
    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
    
    /**
     * @return the time the service was stopped.
     */
    public String getServiceStopTime() {
        return serviceStopTime;
    }
    
    /**
     * @param serviceStopTime
     *          the time the service was stopped.
     */
    public void setServiceStopTime(String serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }
    
    /**
     * @return the size of the document.
     */
    public Long getSize() {
        return size;
    }
    
    /**
     * @param size
     *          the size of the document.
     */
    public void setSize(Long size) {
        this.size = size;
    }
    
    /**
     * @return information about the source patient that the document is created for.
     */
    public PatientInfo getSourcePatientInfo() {
        return sourcePatientInfo;
    }
    
    /**
     * @param sourcePatientInfo
     *          information about the source patient that the document is created for.
     */
    public void setSourcePatientInfo(PatientInfo sourcePatientInfo) {
        this.sourcePatientInfo = sourcePatientInfo;
    }
    
    /**
     * @return the ID of the patient that the document is created for.
     */
    public Identifiable getSourcePatientID() {
        return sourcePatientID;
    }

    /**
     * @param sourcePatientID
     *          the ID of the patient that the document is created for.
     */
    public void setSourcePatientID(Identifiable sourcePatientID) {
        this.sourcePatientID = sourcePatientID;
    }

    /**
     * @return the code of the type of the document.
     */
    public Code getTypeCode() {
        return typeCode;
    }
    
    /**
     * @param typeCode
     *          the code of the type of the document.
     */
    public void setTypeCode(Code typeCode) {
        this.typeCode = typeCode;
    }
    
    /**
     * @return the URI of the document for downloads via XDS.a
     */
    public String getUri() {
        return uri;
    }
    
    /**
     * @param uri
     *          the URI of the document for downloads via XDS.a.
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    /**
     * @return the ID of the repository that the document resides in.
     */
    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }
    
    /**
     * @param repositoryUniqueId
     *          the ID of the repository that the document resides in.
     */
    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }
    
    /**
     * @return the codes describing the confidentiality of the document.
     */
    public List<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    /**
     * @return the codes describing the event for which the document was created.
     */
    public List<Code> getEventCodeList() {
        return eventCodeList;
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((authors == null) ? 0 : authors.hashCode());
        result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((eventCodeList == null) ? 0 : eventCodeList.hashCode());
        result = prime * result + ((formatCode == null) ? 0 : formatCode.hashCode());
        result = prime * result + ((hash == null) ? 0 : hash.hashCode());
        result = prime
                * result
                + ((healthcareFacilityTypeCode == null) ? 0 : healthcareFacilityTypeCode.hashCode());
        result = prime * result + ((homeCommunityId == null) ? 0 : homeCommunityId.hashCode());
        result = prime * result + ((languageCode == null) ? 0 : languageCode.hashCode());
        result = prime * result
                + ((legalAuthenticator == null) ? 0 : legalAuthenticator.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result
                + ((practiceSettingCode == null) ? 0 : practiceSettingCode.hashCode());
        result = prime * result
                + ((repositoryUniqueId == null) ? 0 : repositoryUniqueId.hashCode());
        result = prime * result + ((serviceStartTime == null) ? 0 : serviceStartTime.hashCode());
        result = prime * result + ((serviceStopTime == null) ? 0 : serviceStopTime.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((sourcePatientID == null) ? 0 : sourcePatientID.hashCode());
        result = prime * result + ((sourcePatientInfo == null) ? 0 : sourcePatientInfo.hashCode());
        result = prime * result + ((typeCode == null) ? 0 : typeCode.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DocumentEntry other = (DocumentEntry) obj;
        if (authors == null) {
            if (other.authors != null)
                return false;
        } else if (!authors.equals(other.authors))
            return false;
        if (classCode == null) {
            if (other.classCode != null)
                return false;
        } else if (!classCode.equals(other.classCode))
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
        if (homeCommunityId == null) {
            if (other.homeCommunityId != null)
                return false;
        } else if (!homeCommunityId.equals(other.homeCommunityId))
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
        if (typeCode == null) {
            if (other.typeCode != null)
                return false;
        } else if (!typeCode.equals(other.typeCode))
            return false;
        if (uri == null) {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }
}
