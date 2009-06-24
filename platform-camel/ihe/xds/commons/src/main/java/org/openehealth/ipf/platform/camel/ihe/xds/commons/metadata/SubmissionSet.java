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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result
                + ((availabilityStatus == null) ? 0 : availabilityStatus.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result + ((contentTypeCode == null) ? 0 : contentTypeCode.hashCode());
        result = prime * result + ((entryUUID == null) ? 0 : entryUUID.hashCode());
        result = prime * result
                + ((intendedRecipients == null) ? 0 : intendedRecipients.hashCode());
        result = prime * result + ((patientID == null) ? 0 : patientID.hashCode());
        result = prime * result + ((sourceID == null) ? 0 : sourceID.hashCode());
        result = prime * result + ((submissionTime == null) ? 0 : submissionTime.hashCode());
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
        SubmissionSet other = (SubmissionSet) obj;
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
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        if (contentTypeCode == null) {
            if (other.contentTypeCode != null)
                return false;
        } else if (!contentTypeCode.equals(other.contentTypeCode))
            return false;
        if (entryUUID == null) {
            if (other.entryUUID != null)
                return false;
        } else if (!entryUUID.equals(other.entryUUID))
            return false;
        if (intendedRecipients == null) {
            if (other.intendedRecipients != null)
                return false;
        } else if (!intendedRecipients.equals(other.intendedRecipients))
            return false;
        if (patientID == null) {
            if (other.patientID != null)
                return false;
        } else if (!patientID.equals(other.patientID))
            return false;
        if (sourceID == null) {
            if (other.sourceID != null)
                return false;
        } else if (!sourceID.equals(other.sourceID))
            return false;
        if (submissionTime == null) {
            if (other.submissionTime != null)
                return false;
        } else if (!submissionTime.equals(other.submissionTime))
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
