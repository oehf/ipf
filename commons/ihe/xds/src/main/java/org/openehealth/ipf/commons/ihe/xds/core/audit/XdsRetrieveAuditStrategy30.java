/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.List;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based retrieval-related XDS transactions.
 *
 * @author Dmytro Rud
 */
abstract public class XdsRetrieveAuditStrategy30 extends XdsAuditStrategy<XdsRetrieveAuditDataset> {

    public XdsRetrieveAuditStrategy30(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }


    @Override
    public void enrichDatasetFromRequest(Object pojo, XdsRetrieveAuditDataset auditDataset) {
        RetrieveDocumentSetRequestType request = (RetrieveDocumentSetRequestType) pojo;

        List<DocumentRequest> requestedDocuments = request.getDocumentRequest();
        if (requestedDocuments != null) {
            final int SIZE = requestedDocuments.size();

            String[] documentUniqueIds = new String[SIZE];
            String[] repositoryUniqueIds = new String[SIZE];
            String[] homeCommunityIds = new String[SIZE];

            for (int i = 0; i < SIZE; ++i) {
                DocumentRequest document = requestedDocuments.get(i);
                documentUniqueIds[i] = document.getDocumentUniqueId();
                repositoryUniqueIds[i] = document.getRepositoryUniqueId();
                homeCommunityIds[i] = document.getHomeCommunityId();
            }

            auditDataset.setDocumentUniqueIds(documentUniqueIds);
            auditDataset.setRepositoryUniqueIds(repositoryUniqueIds);
            auditDataset.setHomeCommunityIds(homeCommunityIds);
        }
    }


    @Override
    public XdsRetrieveAuditDataset createAuditDataset() {
        return new XdsRetrieveAuditDataset(isServerSide());
    }


    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response.getRegistryResponse()); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
