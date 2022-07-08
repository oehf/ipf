/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Document;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;

import java.util.Map;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing in ebXML 3.0-based XDS transactions
 * related to non-constructive operations (Read+Delete as opposed to Create+Update in CRUD)
 * on document sets in an XDS Repository.
 *
 * @author Dmytro Rud
 */
public abstract class XdsNonconstructiveDocumentSetRequestAuditStrategy30 extends XdsAuditStrategy<XdsNonconstructiveDocumentSetRequestAuditDataset> {

    public XdsNonconstructiveDocumentSetRequestAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Specifies status of newly registered documents.
     * For retrieval operations, this status is NOT_SUCCESSFUL, and will be changed to SUCCESSFUL for actually delivered documents.
     * For removal operations, this status is SUCCESSFUL, and will be changed to NOT_SUCCESSFUL for documents mentioned in
     * {@link org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryErrorList}.
     */
    abstract public Status getDefaultDocumentStatus();

    @Override
    public XdsNonconstructiveDocumentSetRequestAuditDataset enrichAuditDatasetFromRequest(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Object pojo, Map<String, Object> parameters) {
        if (pojo instanceof RetrieveDocumentSetRequestType) {
            var request = (RetrieveDocumentSetRequestType) pojo;
            if (request.getDocumentRequest() != null) {
                request.getDocumentRequest().forEach(document ->
                        auditDataset.getDocuments().add(new Document(
                                document.getDocumentUniqueId(),
                                document.getRepositoryUniqueId(),
                                document.getHomeCommunityId(),
                                null,
                                null,
                                getDefaultDocumentStatus())));
            }
        }
        return auditDataset;
    }

    @Override
    public XdsNonconstructiveDocumentSetRequestAuditDataset createAuditDataset() {
        return new XdsNonconstructiveDocumentSetRequestAuditDataset(isServerSide());
    }

}
