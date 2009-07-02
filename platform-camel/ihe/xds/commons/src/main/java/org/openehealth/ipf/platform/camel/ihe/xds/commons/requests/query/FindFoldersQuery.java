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
 * Represents a stored query for FindFolders.
 * @author Jens Riemschneider
 */
public class FindFoldersQuery extends StoredQuery {
    private Identifiable patientId;
    
    private final List<AvailabilityStatus> status = new ArrayList<AvailabilityStatus>();
    private final TimeRange lastUpdateTime = new TimeRange();
    private final QueryList<Code> codes = new QueryList<Code>();
    
    public FindFoldersQuery() {
        super(QueryType.FIND_FOLDERS);
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
    
    public TimeRange getLastUpdateTime() {
        return lastUpdateTime;
    }

    public QueryList<Code> getCodes() {
        return codes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codes == null) ? 0 : codes.hashCode());
        result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
        result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
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
        FindFoldersQuery other = (FindFoldersQuery) obj;
        if (codes == null) {
            if (other.codes != null)
                return false;
        } else if (!codes.equals(other.codes))
            return false;
        if (lastUpdateTime == null) {
            if (other.lastUpdateTime != null)
                return false;
        } else if (!lastUpdateTime.equals(other.lastUpdateTime))
            return false;
        if (patientId == null) {
            if (other.patientId != null)
                return false;
        } else if (!patientId.equals(other.patientId))
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
