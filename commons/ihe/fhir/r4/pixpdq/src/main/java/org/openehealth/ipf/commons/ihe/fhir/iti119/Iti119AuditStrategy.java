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

    /**
     * Records the {@code $match} request body as the query of the transaction.
     * <p>
     * ITI-119 has no query string: the search criteria travel as a {@code Parameters} resource in the
     * body of a POST. The PDQm audit profiles nevertheless inherit the mandatory query entity of the
     * BALP query pattern, and the AuditEvent examples of the PDQm implementation guide put the encoded
     * request body there -- so that is what is recorded here.
     * <p>
     * The body is assembled the same way the client request factory assembles it, because a route may
     * pass the {@code Parameters} resource, the {@code Patient} to match, or endpoint parameters, and
     * all three have to end up audited as the request they turn into.
     *
     * @param auditDataset audit dataset
     * @param request      request object
     * @param parameters   request parameters
     * @return enriched audit dataset
     */
    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);
        if (dataset.getFhirContext() != null) {
            dataset.setQueryString(dataset.getFhirContext().newJsonParser()
                .encodeResourceToString(Iti119ClientRequestFactory.matchParameters(request, parameters)));
        }
        return dataset;
    }

}
