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

/**
 * Represents a stored query for GetAll.
 * @author Jens Riemschneider
 */
public class GetAllQuery extends StoredQuery {
    private Identifiable patientId;
    
    private final List<AvailabilityStatus> statusDocuments = new ArrayList<AvailabilityStatus>();
    private final List<AvailabilityStatus> statusSubmissionSets = new ArrayList<AvailabilityStatus>();
    private final List<AvailabilityStatus> statusFolders = new ArrayList<AvailabilityStatus>();

    private final QueryList<Code> confidentialityCodes = new QueryList<Code>();
    private final List<Code> formatCodes = new ArrayList<Code>();
    
    public GetAllQuery() {
        super(QueryType.GET_ALL);
    }

    public Identifiable getPatientId() {
        return patientId;
    }

    public void setPatientId(Identifiable patientId) {
        this.patientId = patientId;
    }

    public List<AvailabilityStatus> getStatusDocuments() {
        return statusDocuments;
    }

    public List<AvailabilityStatus> getStatusSubmissionSets() {
        return statusSubmissionSets;
    }

    public List<AvailabilityStatus> getStatusFolders() {
        return statusFolders;
    }

    public QueryList<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    public List<Code> getFormatCodes() {
        return formatCodes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((formatCodes == null) ? 0 : formatCodes.hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
        result = prime * result + ((statusDocuments == null) ? 0 : statusDocuments.hashCode());
        result = prime * result + ((statusFolders == null) ? 0 : statusFolders.hashCode());
        result = prime * result
                + ((statusSubmissionSets == null) ? 0 : statusSubmissionSets.hashCode());
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
        GetAllQuery other = (GetAllQuery) obj;
        if (confidentialityCodes == null) {
            if (other.confidentialityCodes != null)
                return false;
        } else if (!confidentialityCodes.equals(other.confidentialityCodes))
            return false;
        if (formatCodes == null) {
            if (other.formatCodes != null)
                return false;
        } else if (!formatCodes.equals(other.formatCodes))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
            return false;
        if (statusDocuments == null) {
            if (other.statusDocuments != null)
                return false;
        } else if (!statusDocuments.equals(other.statusDocuments))
            return false;
        if (statusFolders == null) {
            if (other.statusFolders != null)
                return false;
        } else if (!statusFolders.equals(other.statusFolders))
            return false;
        if (statusSubmissionSets == null) {
            if (other.statusSubmissionSets != null)
                return false;
        } else if (!statusSubmissionSets.equals(other.statusSubmissionSets))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
