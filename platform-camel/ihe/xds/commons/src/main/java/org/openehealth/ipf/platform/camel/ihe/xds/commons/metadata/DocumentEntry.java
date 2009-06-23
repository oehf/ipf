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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    private String title;
    private Code typeCode;
    private UniqueID uniqueID;
    private URI uri;
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

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
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
    
    public URI getUri() {
        return uri;
    }
    
    public void setUri(URI uri) {
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
}
