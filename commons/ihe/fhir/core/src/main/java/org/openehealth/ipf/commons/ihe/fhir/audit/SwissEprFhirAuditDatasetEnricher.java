/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.audit;

import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_INCOMING_HEADERS;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.HTTP_OUTGOING_HEADERS;

/**
 * Audit dataset enricher for FHIR based transactions which implements requirements
 * of the Swiss Electronic Patient Record.
 *
 * @author Dmytro Rud
 */
public class SwissEprFhirAuditDatasetEnricher implements FhirAuditDatasetEnricher {

    @Override
    public void enrichAuditDataset(AuditDataset auditDataset, Object request, Map<String, Object> parameters) {
        // the outbound value, whenever present, shall override the incoming one
        extractW3cTraceContextId(parameters, HTTP_INCOMING_HEADERS, auditDataset);
        extractW3cTraceContextId(parameters, HTTP_OUTGOING_HEADERS, auditDataset);
    }

    private static void extractW3cTraceContextId(Map<String, Object> parameters, String key, AuditDataset auditDataset) {
        Object value = parameters.get(key);
        if (value != null) {
            Map<String, List<String>> headers = (Map<String, List<String>>) value;
            for (String name : headers.keySet()) {
                if ("traceparent".equalsIgnoreCase(name)) {
                    auditDataset.setW3cTraceContextId(headers.get(name).get(0));
                    return;
                }
            }
        }
    }

}
