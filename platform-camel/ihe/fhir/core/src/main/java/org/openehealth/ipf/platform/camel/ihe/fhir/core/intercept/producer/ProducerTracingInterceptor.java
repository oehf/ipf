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

package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.producer;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.HapiClientInterceptorFactory;

import java.io.IOException;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.core.Constants.*;

/**
 * HAPI interceptor that adds tracing information from the Camel message headers, if available,
 * to the HTTP request headers
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class ProducerTracingInterceptor implements IClientInterceptor, HapiClientInterceptorFactory {

    private final Map<String, Object> headers;

    private ProducerTracingInterceptor(Map<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public IClientInterceptor newInstance(FhirEndpoint endpoint, Exchange exchange) {
        return new ProducerTracingInterceptor(exchange.getIn().getHeaders());
    }

    @Override
    public void interceptRequest(IHttpRequest request) {
        if (headers.containsKey(TRACE_ID)) {
            request.addHeader(TRACE_ID, headers.get(TRACE_ID).toString());
        }
        if (headers.containsKey(SPAN_ID)) {
            request.addHeader(SPAN_ID, headers.get(SPAN_ID).toString());
        }
        if (headers.containsKey(PARENT_SPAN_ID)) {
            request.addHeader(PARENT_SPAN_ID, headers.get(PARENT_SPAN_ID).toString());
        }
        if (headers.containsKey(SAMPLED)) {
            request.addHeader(SAMPLED, headers.get(SAMPLED).toString());
        }
        if (headers.containsKey(FLAGS)) {
            request.addHeader(FLAGS, headers.get(FLAGS).toString());
        }
    }

    @Override
    public void interceptResponse(IHttpResponse theResponse) throws IOException {
        // nothing to do here?
    }
}
