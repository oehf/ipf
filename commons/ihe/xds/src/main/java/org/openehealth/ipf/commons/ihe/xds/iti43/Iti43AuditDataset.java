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
package org.openehealth.ipf.commons.ihe.xds.iti43;

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;


/**
 * ITI-43 specific Audit Dataset.
 * @author Dmytro Rud
 */
public class Iti43AuditDataset extends XdsAuditDataset {
    private static final long serialVersionUID = -8776033207572005899L;

    private String[] documentUuids;
    private String[] repositoryUuids;
    private String[] homeCommunityUuids;

    public Iti43AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public void setDocumentUuids(String[] documentUuids) {
        this.documentUuids = documentUuids;
    }
    
    public String[] getDocumentUuids() {
        return documentUuids;
    }
    
    public void setRepositoryUuids(String[] repositoryUuids) {
        this.repositoryUuids = repositoryUuids;
    }
    
    public String[] getRepositoryUuids() {
        return repositoryUuids;
    }
    
    public void setHomeCommunityUuids(String[] homeCommunityUuids) {
        this.homeCommunityUuids = homeCommunityUuids;
    }
    
    public String[] getHomeCommunityUuids() {
        return homeCommunityUuids;
    }
}
