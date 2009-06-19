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

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.TimeRange;

/**
 * Represents a stored query for FindSubmissionSets.
 * @author Jens Riemschneider
 */
public class FindSubmissionSetsQuery extends StoredQuery {
    private final Identifiable patientId;
    private final QueryList<AvailabilityStatus> status;

    private final QueryList<Identifiable> sourceIds = new QueryList<Identifiable>();
    private final QueryList<Code> contentTypeCodes = new QueryList<Code>();
    private final TimeRange submissionTime = new TimeRange();
    private String authorPerson;  // For some reason spec says this cannot be a list

    public FindSubmissionSetsQuery(Identifiable patientId, QueryList<AvailabilityStatus> status) {
        notNull(patientId, "patientId cannot be null");
        notNull(status, "status cannot be null");
        
        this.patientId = patientId;
        this.status = new QueryList<AvailabilityStatus>(status);
    }
    
    public Identifiable getPatientId() {
        return patientId;
    }

    public QueryList<AvailabilityStatus> getStatus() {
        return status;
    }
    
    public TimeRange getSubmissionTime() {
        return submissionTime;
    }

    public String getAuthorPerson() {
        return authorPerson;
    }

    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    public QueryList<Identifiable> getSourceIds() {
        return sourceIds;
    }

    public QueryList<Code> getContentTypeCodes() {
        return contentTypeCodes;
    }
}
