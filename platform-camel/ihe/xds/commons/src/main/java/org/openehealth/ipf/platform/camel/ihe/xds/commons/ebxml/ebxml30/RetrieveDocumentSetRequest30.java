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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import static org.apache.commons.lang.Validate.notNull;

import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.RetrieveDocumentSetRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.RetrieveDocument;

/**
 * The ebXML 3.0 version of the {@link RetrieveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetRequest30 implements RetrieveDocumentSetRequest {
    private final RetrieveDocumentSetRequestType request;
    
    private RetrieveDocumentSetRequest30(RetrieveDocumentSetRequestType request) {
        notNull(request, "request cannot be null");
        this.request = request;
    }
    
    static RetrieveDocumentSetRequest30 create() {
        return new RetrieveDocumentSetRequest30(new RetrieveDocumentSetRequestType());
    }
    
    public static RetrieveDocumentSetRequest30 create(RetrieveDocumentSetRequestType request) {
        return new RetrieveDocumentSetRequest30(request);
    }
    
    @Override
    public RetrieveDocumentSetRequestType getInternal() {
        return request;
    }

    @Override
    public List<RetrieveDocument> getDocuments() {
        List<RetrieveDocument> docs = new ArrayList<RetrieveDocument>();
        for (DocumentRequest documentRequest : request.getDocumentRequest()) {
            RetrieveDocument doc = new RetrieveDocument();
            doc.setDocumentUniqueID(documentRequest.getDocumentUniqueId());
            doc.setHomeCommunityID(documentRequest.getHomeCommunityId());
            doc.setRepositoryUniqueID(documentRequest.getRepositoryUniqueId());
            docs.add(doc);
        }
        return docs;
    }

    @Override
    public void setDocuments(List<RetrieveDocument> documents) {
        request.getDocumentRequest().clear();
        if (documents != null) {
            for (RetrieveDocument doc : documents) {
                DocumentRequest documentRequest = new DocumentRequest();
                documentRequest.setDocumentUniqueId(doc.getDocumentUniqueID());
                documentRequest.setHomeCommunityId(doc.getHomeCommunityID());
                documentRequest.setRepositoryUniqueId(doc.getRepositoryUniqueID());
                request.getDocumentRequest().add(documentRequest);
            }
        }
    }
}
