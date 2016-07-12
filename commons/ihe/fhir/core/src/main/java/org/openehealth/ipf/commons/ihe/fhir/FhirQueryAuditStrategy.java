/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * Generic Audit Strategy for FHIR query transactions
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class FhirQueryAuditStrategy extends AuditStrategySupport<FhirQueryAuditDataset> {

    protected FhirQueryAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public FhirQueryAuditDataset enrichAuditDatasetFromRequest(FhirQueryAuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        auditDataset.setUserId((String) parameters.get(HTTP_URI));
        if (parameters.get(HTTP_URL) != null) {
            auditDataset.setServiceEndpointUrl(parameters.get(HTTP_URL).toString());
        }
        auditDataset.setClientIpAddress((String) parameters.get(HTTP_CLIENT_IP_ADDRESS));
        auditDataset.setQueryString((String) parameters.get(HTTP_QUERY));
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(FhirQueryAuditDataset auditDataset, Object response) {
        RFC3881EventCodes.RFC3881EventOutcomeCodes outcomeCodes = getEventOutcomeCode(response);
        auditDataset.setEventOutcomeCode(outcomeCodes);
        return outcomeCodes == RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
    }

    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        return getEventOutcomeCodeFromResource((IBaseResource) pojo);
    }

    /**
     * A resource is returned from the business logic. In general this
     * means success, but this can be overridden here.
     *
     * @param resource FHIR resource
     * @return event outcome code
     */
    protected RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCodeFromResource(IBaseResource resource) {
        return RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
    }

}
