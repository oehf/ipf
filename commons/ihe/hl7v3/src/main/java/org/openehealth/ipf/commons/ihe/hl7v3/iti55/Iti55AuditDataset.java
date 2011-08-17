/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * ATNA Audit dataset for ITI-55.
 * @author Dmytro Rud
 */
public class Iti55AuditDataset extends WsAuditDataset {

    private String queryId;
    private String homeCommunityId;
    private String[] patientIds;
    
    public Iti55AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    public String[] getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(String[] patientIds) {
        this.patientIds = patientIds;
    }
}
