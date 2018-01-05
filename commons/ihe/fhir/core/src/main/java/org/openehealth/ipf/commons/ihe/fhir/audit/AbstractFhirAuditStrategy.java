/*
 * Copyright 2018 the original author or authors.
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;

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
            EventOutcomeIndicator eventOutcomeIndicator = getEventOutcomeIndicator(response);
            auditDataset.setEventOutcomeIndicator(eventOutcomeIndicator);
            return eventOutcomeIndicator == EventOutcomeIndicator.Success;
        }
        return true;
    }

    @Override
    public EventOutcomeIndicator getEventOutcomeIndicator(Object response) {
        return getEventOutcomeCodeFromResource((IBaseResource) response);
    }

    /**
     * A resource is returned from the business logic. This may usually success unless it's OperationOutcome
     *
     * @param resource FHIR resource
     * @return event outcome code
     */
    protected EventOutcomeIndicator getEventOutcomeCodeFromResource(IBaseResource resource) {
        return resource instanceof IBaseOperationOutcome ?
                getEventOutcomeCodeFromOperationOutcome((O)resource) :
                EventOutcomeIndicator.Success;
    }

    /**
     * Operation Outcomes are sets of error, warning and information messages that provide detailed information
     * about the outcome of some attempted system operation. They are provided as a direct system response,
     * or component of one, where they provide information about the outcome of the operation.
     *
     * @param response {@link IBaseOperationOutcome} to be analyzed
     * @return ATNA outcome code
     */
    public abstract EventOutcomeIndicator getEventOutcomeCodeFromOperationOutcome(O response);

}
