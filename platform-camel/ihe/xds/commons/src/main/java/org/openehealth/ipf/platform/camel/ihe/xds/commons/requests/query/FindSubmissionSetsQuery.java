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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.TimeRange;

/**
 * Represents a stored query for FindSubmissionSets.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
public class FindSubmissionSetsQuery extends StoredQuery {
    private Identifiable patientId;
    
    private final List<AvailabilityStatus> status = new ArrayList<AvailabilityStatus>();
    private final List<String> sourceIds = new ArrayList<String>();
    private final List<Code> contentTypeCodes = new ArrayList<Code>();
    private final TimeRange submissionTime = new TimeRange();
    private String authorPerson;  // For some reason spec says this cannot be a list

    /**
     * Constructs the query.
     */
    public FindSubmissionSetsQuery() {
        super(QueryType.FIND_SUBMISSION_SETS);
    }

    /**
     * @return the patient ID to search for.
     */
    public Identifiable getPatientId() {
        return patientId;
    }

    /**
     * @param patientId 
     *          the patient ID to search for.
     */
    public void setPatientId(Identifiable patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the states for filtering {@link SubmissionSet#getAvailabilityStatus()}.
     */
    public List<AvailabilityStatus> getStatus() {
        return status;
    }
    
    /**
     * @return the time range for filtering {@link SubmissionSet#getSubmissionTime()}.
     */
    public TimeRange getSubmissionTime() {
        return submissionTime;
    }

    /**
     * @return the author person text for filtering {@link SubmissionSet#getAuthors()}.
     */
    public String getAuthorPerson() {
        return authorPerson;
    }

    /**
     * @param authorPerson
     *          the author person text for filtering {@link SubmissionSet#getAuthors()}.
     */
    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    /**
     * @return the IDs for filtering {@link SubmissionSet#getSourceID()}.
     */
    public List<String> getSourceIds() {
        return sourceIds;
    }

    /**
     * @return the codes for filtering {@link SubmissionSet#getContentTypeCode()}.
     */
    public List<Code> getContentTypeCodes() {
        return contentTypeCodes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorPerson == null) ? 0 : authorPerson.hashCode());
        result = prime * result + ((contentTypeCodes == null) ? 0 : contentTypeCodes.hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
        result = prime * result + ((sourceIds == null) ? 0 : sourceIds.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((submissionTime == null) ? 0 : submissionTime.hashCode());
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
        FindSubmissionSetsQuery other = (FindSubmissionSetsQuery) obj;
        if (authorPerson == null) {
            if (other.authorPerson != null)
                return false;
        } else if (!authorPerson.equals(other.authorPerson))
            return false;
        if (contentTypeCodes == null) {
            if (other.contentTypeCodes != null)
                return false;
        } else if (!contentTypeCodes.equals(other.contentTypeCodes))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
            return false;
        if (sourceIds == null) {
            if (other.sourceIds != null)
                return false;
        } else if (!sourceIds.equals(other.sourceIds))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
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
