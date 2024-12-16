/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditDataset;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Client-side interceptor that contributes to the AuditDataset once the
 * final URI has been constructed.
 */
public class HapiClientAuditInterceptor implements IClientInterceptor {

    private final FhirAuditDataset fhirAuditDataset;

    public HapiClientAuditInterceptor(FhirAuditDataset fhirAuditDataset) {
        this.fhirAuditDataset = fhirAuditDataset;
    }

    @Override
    public void interceptRequest(IHttpRequest request) {
        var requestUri = request.getUri();
        // Ignore requests for ConformanceStatements
        if ("OPTIONS".equalsIgnoreCase(request.getHttpVerbName()) || requestUri.endsWith("/metadata")) {
            return;
        }
        var uri = URI.create(requestUri);
        fhirAuditDataset.setRemoteAddress(uri.getHost());
        var queryPos = requestUri.indexOf("?");
        if (queryPos >= 0) {
            fhirAuditDataset.setDestinationUserId(requestUri.substring(0, queryPos));
            if (fhirAuditDataset instanceof FhirQueryAuditDataset fhirQueryAuditDataset) {
                fhirQueryAuditDataset.setQueryString(URLDecoder.decode(uri.getQuery(), StandardCharsets.UTF_8));
            }
            if (fhirAuditDataset instanceof GenericFhirAuditDataset genericFhirAuditDataset) {
                genericFhirAuditDataset.setQueryString(URLDecoder.decode(uri.getQuery(), StandardCharsets.UTF_8));
            }
        } else {
            fhirAuditDataset.setDestinationUserId(requestUri);
        }
    }

    @Override
    public void interceptResponse(IHttpResponse response) {
    }

}
