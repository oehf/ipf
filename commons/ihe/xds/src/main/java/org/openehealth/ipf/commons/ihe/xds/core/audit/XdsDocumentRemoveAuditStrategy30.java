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

import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based XDS transactions related to removal of Documents.
 *
 * @author Dmytro Rud
 */
abstract public class XdsDocumentRemoveAuditStrategy30 extends XdsNonconstructiveDocumentSetRequestAuditStrategy30 {

    private static final Pattern OID_PATTERN = Pattern.compile("[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+");

    public XdsDocumentRemoveAuditStrategy30(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public XdsNonconstructiveDocumentSetRequestAuditDataset.Status getDefaultDocumentStatus() {
        return XdsNonconstructiveDocumentSetRequestAuditDataset.Status.SUCCESSFUL;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(XdsNonconstructiveDocumentSetRequestAuditDataset auditDataset, Object pojo) {
        RegistryResponseType response = (RegistryResponseType) pojo;
        if (Status.FAILURE.getOpcode30().equals(response.getStatus())) {
            auditDataset.getDocuments().forEach(x -> x.setStatus(XdsNonconstructiveDocumentSetRequestAuditDataset.Status.NOT_SUCCESSFUL));
        }
        else if (Status.PARTIAL_SUCCESS.getOpcode30().equals(response.getStatus()) &&
                (response.getRegistryErrorList() != null) &&
                (response.getRegistryErrorList().getRegistryError() != null))
        {
            for (RegistryError error : response.getRegistryErrorList().getRegistryError()) {
                String documentUniqueId = extractDocumentUniqueId(error.getCodeContext());
                if (Severity.ERROR.getOpcode30().equals(error.getSeverity()) && (documentUniqueId != null)) {
                    auditDataset.getDocuments().stream()
                            .filter(x -> x.getDocumentUniqueId().equals(documentUniqueId))
                            .findFirst()
                            .ifPresent(x -> x.setStatus(XdsNonconstructiveDocumentSetRequestAuditDataset.Status.NOT_SUCCESSFUL));
                }
            }
        }
        return true;
    }

    private static String extractDocumentUniqueId(String codeContext) {
        if (codeContext == null) {
            return null;
        }
        Matcher matcher = OID_PATTERN.matcher(codeContext);
        return matcher.find() ? matcher.group() : null;
    }

}
