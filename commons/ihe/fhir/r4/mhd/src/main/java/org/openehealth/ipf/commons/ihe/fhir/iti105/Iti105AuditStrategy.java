/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti105;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.DocumentReference;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;

import java.util.Map;

/**
 * @author Boris Stanojevic
 * @since 4.8
 */
public abstract class Iti105AuditStrategy extends FhirAuditStrategy<Iti105AuditDataset> {

    public Iti105AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti105AuditDataset createAuditDataset() {
        return new Iti105AuditDataset(isServerSide());
    }

    @Override
    public Iti105AuditDataset enrichAuditDatasetFromRequest(Iti105AuditDataset auditDataset, Object request,
                                                            Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        if (request instanceof DocumentReference documentReference) {
            dataset.enrichDatasetFromDocumentReference(documentReference);
        }
        return dataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti105AuditDataset auditDataset, Object response, AuditContext auditContext) {
        var methodOutcome = (MethodOutcome) response;
        if (methodOutcome.getResource() != null && methodOutcome.getResource().getIdElement() != null) {
            auditDataset.setDocumentReferenceId(methodOutcome.getResource().getIdElement().getValue());
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }

}
