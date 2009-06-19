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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Identifiable;

/**
 * Represents a stored query for GetAll.
 * @author Jens Riemschneider
 */
public class GetAllQuery extends StoredQuery {
    private final Identifiable patientId;
    private final QueryList<AvailabilityStatus> statusDocuments;
    private final QueryList<AvailabilityStatus> statusSubmissionSets;
    private final QueryList<AvailabilityStatus> statusFolders;

    private final QueryList<Code> confidentialityCodes = new QueryList<Code>();
    private final QueryList<Code> formatCodes = new QueryList<Code>();
    
    public GetAllQuery(Identifiable patientId, 
            QueryList<AvailabilityStatus> statusDocuments,
            QueryList<AvailabilityStatus> statusSubmissionSets, 
            QueryList<AvailabilityStatus> statusFolders) {
        
        this.patientId = patientId;
        this.statusDocuments = new QueryList<AvailabilityStatus>(statusDocuments);
        this.statusSubmissionSets = new QueryList<AvailabilityStatus>(statusSubmissionSets);
        this.statusFolders = new QueryList<AvailabilityStatus>(statusFolders);
    }

    public Identifiable getPatientId() {
        return patientId;
    }

    public QueryList<AvailabilityStatus> getStatusDocuments() {
        return statusDocuments;
    }

    public QueryList<AvailabilityStatus> getStatusSubmissionSets() {
        return statusSubmissionSets;
    }

    public QueryList<AvailabilityStatus> getStatusFolders() {
        return statusFolders;
    }

    public QueryList<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    public QueryList<Code> getFormatCodes() {
        return formatCodes;
    }
}
