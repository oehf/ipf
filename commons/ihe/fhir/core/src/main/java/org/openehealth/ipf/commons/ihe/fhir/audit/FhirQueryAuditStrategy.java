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

import ca.uhn.fhir.rest.param.TokenParam;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_QUERY;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_URL;

/**
 * Generic Audit Strategy for FHIR query transactions
 *
 * @author Christian Ohr
 * @since 3.4
 */
public abstract class FhirQueryAuditStrategy extends FhirAuditStrategy<FhirQueryAuditDataset> {

    protected FhirQueryAuditStrategy(boolean serverSide, IBaseOperationOutcomeOperations operations) {
        super(serverSide, operations);
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
        FhirQueryAuditDataset dataset = super.enrichAuditDatasetFromRequest(auditDataset, request, parameters);

        String url = (String) parameters.get(HTTP_URL);
        String query = (String) parameters.get(HTTP_QUERY);
        try {
            dataset.setQueryString(URLDecoder.decode(String.format("%s?%s", url, query), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
            // should never occur
        }

        FhirSearchParameters searchParameter = (FhirSearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
        if (searchParameter != null) {
            List<TokenParam> tokenParams = searchParameter.getPatientIdParam();
            if (tokenParams != null) {
                dataset.getPatientIds().addAll(
                        tokenParams.stream()
                                .map(t -> t.getValueAsQueryToken(searchParameter.getFhirContext()))
                                .collect(Collectors.toList()));
            }
        }

        return dataset;
    }

    @Override
    public FhirQueryAuditDataset createAuditDataset() {
        return new FhirQueryAuditDataset(isServerSide());
    }
}
