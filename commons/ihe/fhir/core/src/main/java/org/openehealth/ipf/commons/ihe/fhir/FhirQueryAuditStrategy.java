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

import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * Generic Audit Strategy for FHIR query transctions
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
        auditDataset.setUserId((String)parameters.get(HTTP_URI));
        if (parameters.get(HTTP_URL) != null) {
            auditDataset.setServiceEndpointUrl(parameters.get(HTTP_URL).toString());
        }
        auditDataset.setClientIpAddress((String)parameters.get(HTTP_CLIENT_IP_ADDRESS));
        auditDataset.setQueryString((String)parameters.get(HTTP_QUERY));
        return auditDataset;
    }
}
