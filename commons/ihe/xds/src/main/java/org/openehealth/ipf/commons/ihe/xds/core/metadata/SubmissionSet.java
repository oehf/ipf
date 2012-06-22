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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.DateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XDS submission set according to the IHE XDS specification.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmissionSet", propOrder = {
        "sourceId", "submissionTime", "authors", "intendedRecipients", "contentTypeCode"})
@XmlRootElement(name = "submissionSet")
public class SubmissionSet extends XDSMetaClass implements Serializable {
    private static final long serialVersionUID = 5961980266312684583L;
    
    @XmlElement(name = "author")
    private final List<Author> authors = new ArrayList<Author>();
    private Code contentTypeCode;
    @XmlElement(name = "intendedRecipient")
    private final List<Recipient> intendedRecipients = new ArrayList<Recipient>(); 
    private String sourceId;
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(value = DateAdapter.class)
    private String submissionTime;

    /**
     * @return the list of authors of the submission. Cannot be <code>null</code>.
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * @param author
     *          the author of the submission.
     */
    public void setAuthor(Author author) {
        authors.clear();
        authors.add(author);
    }

    /**
     * @return the author of the submission. If the submission has multiple authors
     *          this method returns only the first in the list. If the submission
     *          has no authors, this method returns {@code null}.
     */
    public Author getAuthor() {
        return authors.isEmpty() ? null : authors.get(0);
    }

    /**
     * @return the code describing the content type.
     */
    public Code getContentTypeCode() {
        return contentTypeCode;
    }
    
    /**
     * @param contentTypeCode
     *          the code describing the content type.
     */
    public void setContentTypeCode(Code contentTypeCode) {
        this.contentTypeCode = contentTypeCode;
    }
    
    /**
     * @return the ID of the source.
     */
    public String getSourceId() {
        return sourceId;
    }
    
    /**
     * @param sourceId
     *          the ID of the source.
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    /**
     * @return the time this set was submitted.
     */
    public String getSubmissionTime() {
        return submissionTime;
    }
    
    /**
     * @param submissionTime
     *          the time this set was submitted.
     */
    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }
    
    /**
     * @return the recipients that this submission set was created for. 
     *          Never <code>null</code>.
     */
    public List<Recipient> getIntendedRecipients() {
        return intendedRecipients;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((authors == null) ? 0 : authors.hashCode());
        result = prime * result + ((contentTypeCode == null) ? 0 : contentTypeCode.hashCode());
        result = prime * result
                + ((intendedRecipients == null) ? 0 : intendedRecipients.hashCode());
        result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
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
        if (authors == null) {
            if (other.authors != null)
                return false;
        } else if (!authors.equals(other.authors))
            return false;
        if (contentTypeCode == null) {
            if (other.contentTypeCode != null)
                return false;
        } else if (!contentTypeCode.equals(other.contentTypeCode))
            return false;
        if (intendedRecipients == null) {
            if (other.intendedRecipients != null)
                return false;
        } else if (!intendedRecipients.equals(other.intendedRecipients))
            return false;
        if (sourceId == null) {
            if (other.sourceId != null)
                return false;
        } else if (!sourceId.equals(other.sourceId))
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
