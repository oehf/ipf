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

package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 * Abstract plain provider that allows subclasses to forward the received payload into the
 * Camel route served by the consumer. Note that this can be subclassed for writing so-called
 * plain providers, while resource-specific providers should extend from {@link AbstractResourceProvider}.
 *
 * @since 3.1
 */
public abstract class AbstractPlainProvider implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPlainProvider.class);

    private RequestConsumer consumer;

    /**
     *
     * @param payload FHIR request resource
     * @param resultType expected result type
     * @param httpServletRequest servlet request
     * @param httpServletResponse servlet response
     * @param <R> Result type
     * @return result of route processing
     */
    protected final <R extends IBaseResource> R requestResource(
            Object payload, Class<R> resultType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return requestResource(payload, null, resultType, httpServletRequest, httpServletResponse);
    }

    protected final <R extends IBaseResource> R requestResource(
            Object payload, Map<String, Object> parameters, Class<R> resultType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        return consumer.handleResourceRequest(payload, headers, resultType);
    }

    protected final <R extends IBaseResource> List<R> requestBundle(Object payload,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return requestBundle(payload, null, httpServletRequest, httpServletResponse);
    }

    protected final <R extends IBaseResource> List<R> requestBundle(
            Object payload, Map<String, Object> parameters,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        return consumer.handleBundleRequest(payload, headers);
    }

    private Map<String, Object> enrichParameters(Map<String, Object> parameters, HttpServletRequest httpServletRequest) {
        // Populate some headers.
        Map<String, Object> enriched = new HashMap<>();
        enriched.put(HTTP_URI, httpServletRequest.getRequestURI());
        enriched.put(HTTP_URL, httpServletRequest.getRequestURL());
        enriched.put(HTTP_METHOD, httpServletRequest.getMethod());
        enriched.put(HTTP_QUERY, httpServletRequest.getQueryString());
        enriched.put(HTTP_CHARACTER_ENCODING, httpServletRequest.getCharacterEncoding());
        enriched.put(HTTP_CONTENT_TYPE, httpServletRequest.getContentType());
        enriched.put(HTTP_PROTOCOL_VERSION, httpServletRequest.getProtocol());
        enriched.put(HTTP_CLIENT_IP_ADDRESS, httpServletRequest.getRemoteAddr());

        if (parameters != null && !parameters.isEmpty()) {
            enriched.put(FHIR_REQUEST_PARAMETERS, parameters);
        }
        return enriched;
    }

    /**
     * @return the configured consumer
     */
    public RequestConsumer getConsumer() {
        return consumer;
    }

    // Ensure this is only used once!
    public void setConsumer(RequestConsumer consumer) {
        if (this.consumer != null) {
            throw new IllegalStateException("This provider is already used by a different consumer: " + consumer);
        }
        this.consumer = consumer;
        LOG.info("Connected consumer {} to provider {}", consumer, this);
    }

    public void unsetConsumer(RequestConsumer consumer) {
        if (this.consumer == consumer) {
            this.consumer = null;
            LOG.info("Disconnected consumer {} from provider {}", consumer, this);
        }
    }
}
