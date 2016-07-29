/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti78;

import org.hl7.fhir.instance.model.IdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.FhirQueryAuditStrategy;

import java.util.Map;

/**
 * Strategy for auditing ITI-78 transactions
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class Iti78AuditStrategy extends FhirQueryAuditStrategy<FhirQueryAuditDataset> {

    protected Iti78AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public FhirQueryAuditDataset createAuditDataset() {
        return new FhirQueryAuditDataset(isServerSide());
    }

    @Override
    public void doAudit(FhirQueryAuditDataset auditDataset) {
        AuditorManager.getFhirAuditor().auditIti78(
                isServerSide(),
                auditDataset.getEventOutcomeCode(),
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getClientIpAddress(),
                auditDataset.getQueryString(),
                auditDataset.getPatientIds());
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        FhirQueryAuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        if (request instanceof IdType) {
            IdType idType = (IdType) request;
            dataset.getPatientIds().add(idType.getValue());
        }
        return dataset;
    }
}
