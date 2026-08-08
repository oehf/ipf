/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.rest.param.BaseParam;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_QUERY;

/**
 * Generic Audit Strategy for FHIR query transactions
 *
 * @author Christian Ohr
 * @since 3.4
 */
public abstract class FhirQueryAuditStrategy extends FhirAuditStrategy<FhirQueryAuditDataset> {

    protected FhirQueryAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Further enrich the audit dataset: add query string and patient IDs in the search parameter
     * (if available).
     *
     * @param auditDataset audit dataset
     * @param request      request object
     * @param parameters   request parameters
     * @return enriched audit dataset
     */
    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        var dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        var query = (String) parameters.get(HTTP_QUERY);
        if (query != null) {
            dataset.setQueryString(URLDecoder.decode(query, StandardCharsets.UTF_8));
        }

        var searchParameter = (FhirSearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
        if (searchParameter != null) {
            var tokenParams = searchParameter.getPatientIdParam();
            if (tokenParams != null) {
                dataset.getPatientIds().addAll(
                        tokenParams.stream()
                                .map(BaseParam::getValueAsQueryToken)
                                .toList());
            }
        }

        return dataset;
    }

    /**
     * The search parameters that name the patient a query is about. The server side does not need them:
     * it gets the request parsed into {@link FhirSearchParameters}, which knows its own patient
     * parameter. The client side has only the query string it sent, so the names have to be spelled out.
     * <p>
     * These are the ones the patient-centric FHIR query transactions use. A transaction whose patient
     * lives somewhere else overrides this.
     *
     * @return names of the search parameters that carry a patient id
     */
    protected Set<String> patientIdQueryParameters() {
        return Set.of("_id", "identifier", "patient", "patient.identifier", "subject", "sourceIdentifier");
    }

    /**
     * Fills in the patient from the query string, for the client side.
     * <p>
     * This runs here rather than in {@link #enrichAuditDatasetFromRequest} because the query string does
     * not exist yet when that is called: the client audit dataset only learns it once HAPI has built the
     * request, which happens on the way out. Without it a client record could never name the patient,
     * and would be stepped down to a weaker profile than the server record of the same transaction.
     *
     * @param auditDataset audit dataset
     * @param response     response object
     * @param auditContext audit context
     * @return whether the transaction is to be considered successful
     */
    @Override
    public boolean enrichAuditDatasetFromResponse(FhirQueryAuditDataset auditDataset, Object response, AuditContext auditContext) {
        if (!isServerSide() && auditDataset.getPatientIds().isEmpty()) {
            auditDataset.getPatientIds().addAll(patientIdsIn(auditDataset.getQueryString()));
        }
        return super.enrichAuditDatasetFromResponse(auditDataset, response, auditContext);
    }

    /**
     * @param queryString the query string of the request, may be null
     * @return the patient ids it carries, in the FHIR token syntax the server side records them in
     */
    private Set<String> patientIdsIn(String queryString) {
        if (queryString == null) {
            return Set.of();
        }
        var patientIdParameters = patientIdQueryParameters();
        return Arrays.stream(queryString.split("&"))
            .map(parameter -> parameter.split("=", 2))
            .filter(parameter -> parameter.length == 2 && patientIdParameters.contains(parameter[0]))
            .map(parameter -> parameter[1])
            .filter(value -> !value.isBlank())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public FhirQueryAuditDataset createAuditDataset() {
        return new FhirQueryAuditDataset(isServerSide());
    }
}
