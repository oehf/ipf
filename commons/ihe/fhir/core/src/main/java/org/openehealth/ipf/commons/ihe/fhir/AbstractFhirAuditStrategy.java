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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * Generic Audit Strategy for FHIR transactions
 *
 * @author Christian Ohr
 * @since 3.2
 */
public abstract class AbstractFhirAuditStrategy<T extends FhirAuditDataset, O extends IBaseOperationOutcome> extends AuditStrategySupport<T> {


    protected AbstractFhirAuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public T enrichAuditDatasetFromRequest(T auditDataset, Object request, Map<String, Object> parameters) {
        auditDataset.setUserId((String) parameters.get(HTTP_URI));
        auditDataset.setServiceEndpointUrl((String)parameters.get(HTTP_URL));
        auditDataset.setClientIpAddress((String) parameters.get(HTTP_CLIENT_IP_ADDRESS));
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(T auditDataset, Object response) {
        if (response instanceof IBaseResource) {
            RFC3881EventCodes.RFC3881EventOutcomeCodes outcomeCodes = getEventOutcomeCode(response);
            auditDataset.setEventOutcomeCode(outcomeCodes);
            return outcomeCodes == RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
        }
        return true;
    }

    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        return getEventOutcomeCodeFromResource((IBaseResource) pojo);
    }

    /**
     * A resource is returned from the business logic. This may usually success unless it's OperationOutcome
     *
     * @param resource FHIR resource
     * @return event outcome code
     */
    protected RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCodeFromResource(IBaseResource resource) {
        return resource instanceof IBaseOperationOutcome ?
                getEventOutcomeCodeFromOperationOutcome((O)resource) :
                RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
    }

    /**
     * Operation Outcomes are sets of error, warning and information messages that provide detailed information
     * about the outcome of some attempted system operation. They are provided as a direct system response,
     * or component of one, where they provide information about the outcome of the operation.
     *
     * @param response {@link IBaseOperationOutcome} to be analyzed
     * @return ATNA outcome code
     */
    public abstract RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCodeFromOperationOutcome(O response);

}
