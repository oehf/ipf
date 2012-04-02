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

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Audit strategy for ITI-43.
 * @author Dmytro Rud
 */
abstract public class Iti43AuditStrategy extends XdsAuditStrategy<Iti43AuditDataset> {

    public Iti43AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDatasetFromRequest(Object pojo, Iti43AuditDataset auditDataset) {
        RetrieveDocumentSetRequestType request = (RetrieveDocumentSetRequestType) pojo;

        List<DocumentRequest> requestedDocuments = request.getDocumentRequest();
        if (requestedDocuments != null) {
            final int SIZE = requestedDocuments.size();

            String[] documentUuids = new String[SIZE];
            String[] repositoryUuids = new String[SIZE];
            String[] homeCommunityUuids = new String[SIZE];

            for (int i = 0; i < SIZE; ++i) {
                DocumentRequest document = requestedDocuments.get(i);
                documentUuids[i] = document.getDocumentUniqueId();
                repositoryUuids[i] = document.getRepositoryUniqueId();
                homeCommunityUuids[i] = document.getHomeCommunityId();
            }

            auditDataset.setDocumentUuids(documentUuids);
            auditDataset.setRepositoryUuids(repositoryUuids);
            auditDataset.setHomeCommunityUuids(homeCommunityUuids);
        }
    }
    
    @Override
    public Iti43AuditDataset createAuditDataset() {
        return new Iti43AuditDataset(isServerSide());
    }

    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response.getRegistryResponse()); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
