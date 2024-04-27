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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status.NOT_SUCCESSFUL;
import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsNonconstructiveDocumentSetRequestAuditDataset.Status.SUCCESSFUL;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based XDS transactions related to removal of Documents.
 *
 * @author Dmytro Rud
 */
public abstract class XdsRemoveDocumentAuditStrategy30 extends XdsNonconstructiveDocumentSetRequestAuditStrategy30 {

    public XdsRemoveDocumentAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public XdsNonconstructiveDocumentSetRequestAuditDataset.Status getDefaultDocumentStatus() {
        return SUCCESSFUL;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Object pojo, AuditContext auditContext) {
        if (pojo instanceof RegistryResponseType response) {
            if (Status.FAILURE.getOpcode30().equals(response.getStatus())) {
                auditDataset.getDocuments().forEach(x -> x.setStatus(NOT_SUCCESSFUL));
            } else if (Status.PARTIAL_SUCCESS.getOpcode30().equals(response.getStatus()) &&
                    (response.getRegistryErrorList() != null) &&
                    (response.getRegistryErrorList().getRegistryError() != null)) {
                for (var error : response.getRegistryErrorList().getRegistryError()) {
                    if (Severity.ERROR.getOpcode30().equals(error.getSeverity())) {
                        auditDataset.getDocuments().stream()
                                .filter(document -> error.getCodeContext().contains(document.getDocumentUniqueId()))
                                .findAny()
                                .ifPresent(document -> document.setStatus(NOT_SUCCESSFUL));
                    }
                }
            }
        }
        return true;
    }

}
