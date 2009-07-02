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
public class SubmissionSet extends XDSMetaClass {
    private Author author;
    private Code contentTypeCode;
    private final List<Recipient> intendedRecipients = new ArrayList<Recipient>(); 
    private String sourceID;
    private String submissionTime;
    private String homeCommunityId;
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
    
    public Code getContentTypeCode() {
        return contentTypeCode;
    }
    
    public void setContentTypeCode(Code contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
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
    
    public List<Recipient> getIntendedRecipients() {
        return intendedRecipients;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((contentTypeCode == null) ? 0 : contentTypeCode.hashCode());
        result = prime * result + ((homeCommunityId == null) ? 0 : homeCommunityId.hashCode());
        result = prime * result
                + ((intendedRecipients == null) ? 0 : intendedRecipients.hashCode());
        result = prime * result + ((sourceID == null) ? 0 : sourceID.hashCode());
        result = prime * result + ((submissionTime == null) ? 0 : submissionTime.hashCode());
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
        SubmissionSet other = (SubmissionSet) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (contentTypeCode == null) {
            if (other.contentTypeCode != null)
                return false;
        } else if (!contentTypeCode.equals(other.contentTypeCode))
            return false;
        if (homeCommunityId == null) {
            if (other.homeCommunityId != null)
                return false;
        } else if (!homeCommunityId.equals(other.homeCommunityId))
            return false;
        if (intendedRecipients == null) {
            if (other.intendedRecipients != null)
                return false;
        } else if (!intendedRecipients.equals(other.intendedRecipients))
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
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
