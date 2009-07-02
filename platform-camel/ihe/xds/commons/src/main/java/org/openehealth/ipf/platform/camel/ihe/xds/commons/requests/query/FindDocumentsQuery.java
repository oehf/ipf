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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.TimeRange;

/**
 * Represents a stored query for FindDocuments.
 * @author Jens Riemschneider
 */
public class FindDocumentsQuery extends StoredQuery {
    private Identifiable patientId;
    
    private final List<AvailabilityStatus> status = new ArrayList<AvailabilityStatus>();    
    private final List<Code> classCodes = new ArrayList<Code>();
    private final List<Code> practiceSettingCodes = new ArrayList<Code>();
    private final List<Code> healthCareFacilityTypeCodes = new ArrayList<Code>();
    private final QueryList<Code> eventCodes = new QueryList<Code>();
    private final QueryList<Code> confidentialityCodes = new QueryList<Code>();
    private final List<Code> formatCodes = new ArrayList<Code>();
    private final List<String> authorPersons = new ArrayList<String>();
    private final TimeRange creationTime = new TimeRange();
    private final TimeRange serviceStartTime = new TimeRange();
    private final TimeRange serviceStopTime = new TimeRange();
    
    public FindDocumentsQuery() {
        super(QueryType.FIND_DOCUMENTS);
    }

    public Identifiable getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Identifiable patientId) {
        this.patientId = patientId;
    }

    public List<AvailabilityStatus> getStatus() {
        return status;
    }
    
    public TimeRange getCreationTime() {
        return creationTime;
    }

    public TimeRange getServiceStartTime() {
        return serviceStartTime;
    }

    public TimeRange getServiceStopTime() {
        return serviceStopTime;
    }

    public List<Code> getClassCodes() {
        return classCodes;
    }
    
    public List<Code> getPracticeSettingCodes() {
        return practiceSettingCodes;
    }
    
    public List<Code> getHealthCareFacilityTypeCodes() {
        return healthCareFacilityTypeCodes;
    }
    
    public QueryList<Code> getEventCodes() {
        return eventCodes;
    }

    public QueryList<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    public List<Code> getFormatCodes() {
        return formatCodes;
    }

    public List<String> getAuthorPersons() {
        return authorPersons;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorPersons == null) ? 0 : authorPersons.hashCode());
        result = prime * result + ((classCodes == null) ? 0 : classCodes.hashCode());
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((eventCodes == null) ? 0 : eventCodes.hashCode());
        result = prime * result + ((formatCodes == null) ? 0 : formatCodes.hashCode());
        result = prime
                * result
                + ((healthCareFacilityTypeCodes == null) ? 0 : healthCareFacilityTypeCodes
                        .hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
        result = prime * result
                + ((practiceSettingCodes == null) ? 0 : practiceSettingCodes.hashCode());
        result = prime * result + ((serviceStartTime == null) ? 0 : serviceStartTime.hashCode());
        result = prime * result + ((serviceStopTime == null) ? 0 : serviceStopTime.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        FindDocumentsQuery other = (FindDocumentsQuery) obj;
        if (authorPersons == null) {
            if (other.authorPersons != null)
                return false;
        } else if (!authorPersons.equals(other.authorPersons))
            return false;
        if (classCodes == null) {
            if (other.classCodes != null)
                return false;
        } else if (!classCodes.equals(other.classCodes))
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
        if (eventCodes == null) {
            if (other.eventCodes != null)
                return false;
        } else if (!eventCodes.equals(other.eventCodes))
            return false;
        if (formatCodes == null) {
            if (other.formatCodes != null)
                return false;
        } else if (!formatCodes.equals(other.formatCodes))
            return false;
        if (healthCareFacilityTypeCodes == null) {
            if (other.healthCareFacilityTypeCodes != null)
                return false;
        } else if (!healthCareFacilityTypeCodes.equals(other.healthCareFacilityTypeCodes))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
            return false;
        if (practiceSettingCodes == null) {
            if (other.practiceSettingCodes != null)
                return false;
        } else if (!practiceSettingCodes.equals(other.practiceSettingCodes))
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
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
