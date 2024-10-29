/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.iti119;

import org.hl7.fhir.r4.model.Parameters;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.ihe.fhir.audit.events.BalpQueryInformationBuilder;

import java.util.Map;

/**
 * Strategy for auditing ITI-78 transactions
 *
 * @author Christian Ohr
 * @since 3.6
 */
class Iti119AuditStrategy extends FhirQueryAuditStrategy {

    protected Iti119AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new BalpQueryInformationBuilder(auditContext, auditDataset, FhirEventTypeCode.PatientDemographicsMatch)
                .addPatients(auditDataset.getPatientIds())
                .setQueryParameters(
                        "PatientDemographicsMatch",
                        FhirParticipantObjectIdTypeCode.PatientDemographicsMatch,
                        auditDataset.getQueryString())

                .getMessages();
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        if (request instanceof Parameters p && auditDataset.getFhirContext() != null) {
            dataset.setQueryString(auditDataset.getFhirContext().newJsonParser().encodeToString(p));
        }
        return dataset;
    }

}
