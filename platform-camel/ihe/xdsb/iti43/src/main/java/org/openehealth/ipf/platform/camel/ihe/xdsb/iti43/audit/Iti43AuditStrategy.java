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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.audit;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service.RetrieveDocumentSetRequestType.DocumentRequest;


/**
 * Audit strategy for ITI-43.
 * 
 * @author Dmytro Rud
 */
abstract public class Iti43AuditStrategy implements AuditStrategy {

    @Override
    public void enrichDataset(
            Object pojo, 
            AuditDataset genericAuditDataset) 
    {
        RetrieveDocumentSetRequestType request = (RetrieveDocumentSetRequestType)pojo;
        Iti43AuditDataset auditDataset = (Iti43AuditDataset)genericAuditDataset;
        
        List<DocumentRequest> requestedDocuments = request.getDocumentRequest();
        if(requestedDocuments != null) {
            final int SIZE = requestedDocuments.size();
            
            String[] documentUuids      = new String[SIZE];
            String[] repositoryUuids    = new String[SIZE];
            String[] homeCommunityUuids = new String[SIZE];
            
            for(int i = 0; i < SIZE; ++i) {
                DocumentRequest document = requestedDocuments.get(i);
                documentUuids[i]      = document.getDocumentUniqueId();
                repositoryUuids[i]    = document.getRepositoryUniqueId();
                homeCommunityUuids[i] = document.getHomeCommunityId();
            }
            
            auditDataset.setDocumentUuids(documentUuids);
            auditDataset.setRepositoryUuids(repositoryUuids);
            auditDataset.setHomeCommunityUuids(homeCommunityUuids);
        }
    }


    @Override
    public boolean needSavePayload() {
        return false;
    }

}
