/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.rest.server.IResourceProvider;
import org.apache.camel.Exchange;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract resource provider that allows subclasses to forward the received payload into the
 * Camel route served by the consumer.
 */
public abstract class AbstractResourceProvider<T extends FhirAuditDataset> implements IResourceProvider, Serializable {

    private transient FhirConsumer<T> consumer;

    /**
     *
     * @param payload FHIR payload
     * @param resultType exepcted result type
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @param <T> AuditDataSet type
     * @return result of route processing
     */
    protected final <T extends IBaseResource> T processInRoute(Object payload, Class<T> resultType,
                                                               HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }

        // Populate some headers
        Map<String, Object> headers = new HashMap<>();
        headers.put(Exchange.HTTP_URI, httpServletRequest.getRequestURI());
        headers.put(Exchange.HTTP_METHOD, httpServletRequest.getMethod());
        headers.put(Exchange.HTTP_QUERY, httpServletRequest.getQueryString());
        headers.put(Exchange.HTTP_CHARACTER_ENCODING, httpServletRequest.getCharacterEncoding());
        headers.put(Exchange.CONTENT_TYPE, httpServletRequest.getContentType());
        headers.put(Exchange.HTTP_PROTOCOL_VERSION, httpServletRequest.getProtocol());
        headers.put(Exchange.HTTP_SERVLET_REQUEST, httpServletRequest);
        headers.put(Exchange.HTTP_SERVLET_RESPONSE, httpServletResponse);

        return consumer.processInRoute(payload, headers, resultType);
    }

    // Ensure this is only used once!
    void setConsumer(FhirConsumer<T> consumer) {
        if (this.consumer != null) {
            throw new IllegalStateException("This provider is already used by a different consumer");
        }
        this.consumer = consumer;
    }
}
