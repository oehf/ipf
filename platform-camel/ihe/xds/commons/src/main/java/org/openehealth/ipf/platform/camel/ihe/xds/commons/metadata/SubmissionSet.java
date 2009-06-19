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

/**
 * Represents an XDS submission set according to the IHE XDS specification.
 * 
 * @author Jens Riemschneider
 */
public class SubmissionSet {
    private Author author;
    private AvailabilityStatus availabilityStatus;
    private LocalizedString comments;
    private Code contentTypeCode;
    private EntryUUID entryUUID;
    private final List<Identifiable> intendedRecipients = new ArrayList<Identifiable>();
    private Identifiable patientID;
    private String sourceID;
    private String submissionTime;
    private String title;
    private UniqueID uniqueID;
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
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
    
    public Code getContentTypeCode() {
        return contentTypeCode;
    }
    
    public void setContentTypeCode(Code contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }
    
    public EntryUUID getEntryUUID() {
        return entryUUID;
    }
    
    public void setEntryUUID(EntryUUID entryUUID) {
        this.entryUUID = entryUUID;
    }
    
    public Identifiable getPatientID() {
        return patientID;
    }
    
    public void setPatientID(Identifiable patientID) {
        this.patientID = patientID;
    }
    
    public String getSourceID() {
        return sourceID;
    }
    
    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }
    
    public String getSubmissionTime() {
        return submissionTime;
    }
    
    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public UniqueID getUniqueID() {
        return uniqueID;
    }
    
    public void setUniqueID(UniqueID uniqueID) {
        this.uniqueID = uniqueID;
    }

    public List<Identifiable> getIntendedRecipients() {
        return intendedRecipients;
    }
}
