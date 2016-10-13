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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.core.Constants.*;
/**
 * Abstract plain provider that allows subclasses to forward the received payload into the
 * Camel route served by the consumer. Note that this can be subclassed for writing so-called
 * plain providers, while resource-specific providers should extend from {@link AbstractResourceProvider}.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class AbstractPlainProvider implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPlainProvider.class);

    private RequestConsumer consumer;
    private FhirValidator validator = FhirValidator.NO_VALIDATION;

    public void setValidator(FhirValidator validator) {
        this.validator = validator;
    }

    protected FhirContext getFhirContext() {
        return consumer.getFhirContext();
    }

    /**
     * Requests a single resource
     *
     * @param payload             FHIR request resource
     * @param resultType          expected result type
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> R requestResource(
            Object payload, Class<R> resultType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return requestResource(payload, null, resultType, httpServletRequest, httpServletResponse);
    }

    /**
     * Requests a single resource with parameters
     *
     * @param payload             FHIR request resource (often null)
     * @param parameters          FHIR parameters
     * @param resultType          expected result type
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> R requestResource(
            Object payload, FhirSearchParameters parameters, Class<R> resultType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        validator.validateRequest(consumer.getFhirContext(), payload, headers);
        R response = consumer.handleResourceRequest(payload, headers, resultType);
        validator.validateResponse(consumer.getFhirContext(), response, headers);
        return response;
    }

    /**
     * Requests a {@link Bundle} of resources
     *
     * @param payload             FHIR request resource
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> List<R> requestBundle(Object payload,
                                                                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return requestBundle(payload, null, httpServletRequest, httpServletResponse);
    }

    /**
     * Requests a {@link Bundle} of resources with parameters
     *
     * @param payload             FHIR request resource (often null)
     * @param parameters          FHIR search parameters
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> List<R> requestBundle(
            Object payload, FhirSearchParameters parameters,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        validator.validateRequest(consumer.getFhirContext(), payload, headers);
        List<R> response = consumer.handleBundleRequest(payload, headers);
        validator.validateResponse(consumer.getFhirContext(), response, headers);
        return response;
    }

    /**
     * Requests a {@link IBundleProvider} that takes over the responsibility to fetch requested
     * bundles. The type of the returned {@link IBundleProvider} instance is determined
     * by the {@link #consumer RequestConsumer} impelmentation.
     *
     * @param payload             FHIR request resource (often null)
     * @param searchParameters    FHIR search parameters
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return IBundleProvider
     */
    protected final IBundleProvider requestBundleProvider(
            Object payload, FhirSearchParameters searchParameters,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(searchParameters, httpServletRequest);
        validator.validateRequest(consumer.getFhirContext(), payload, headers);
        return consumer.handleBundleProviderRequest(payload, headers, validator);
    }

    /**
     * Submits a resource to be created or updated
     *
     * @param payload             resource payload
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return result of processing
     */
    protected final MethodOutcome requestAction(
            Object payload,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return requestAction(payload, null, httpServletRequest, httpServletResponse);
    }

    /**
     * Submits a resource to be created or updated
     *
     * @param payload             resource payload
     * @param parameters          parameters
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return result of processing
     */
    protected final MethodOutcome requestAction(
            Object payload,
            FhirSearchParameters parameters,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        validator.validateRequest(consumer.getFhirContext(), payload, headers);
        MethodOutcome response = consumer.handleAction(payload, headers);
        validator.validateResponse(consumer.getFhirContext(), response, headers);
        return response;
    }

    /**
     * Subsmits a transaction request bundle, expecting a corresponding response bundle
     *
     * @param payload             transaction bundle
     * @param parameters          parameters
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return result of processing
     */
    protected final Bundle requestTransaction(
            Object payload,
            FhirSearchParameters parameters,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        validator.validateRequest(consumer.getFhirContext(), payload, headers);
        Bundle response = consumer.handleTransactionRequest(payload, headers);
        validator.validateResponse(consumer.getFhirContext(), response, headers);
        return response;
    }

    private Map<String, Object> enrichParameters(FhirSearchParameters parameters, HttpServletRequest httpServletRequest) {
        // Populate some headers.
        Map<String, Object> enriched = new HashMap<>();
        enriched.put(Constants.HTTP_URI, httpServletRequest.getRequestURI());
        enriched.put(Constants.HTTP_URL, httpServletRequest.getRequestURL().toString());
        enriched.put(Constants.HTTP_METHOD, httpServletRequest.getMethod());
        enriched.put(Constants.HTTP_QUERY, httpServletRequest.getQueryString());
        enriched.put(Constants.HTTP_CHARACTER_ENCODING, httpServletRequest.getCharacterEncoding());
        enriched.put(Constants.HTTP_CONTENT_TYPE, httpServletRequest.getContentType());
        enriched.put(Constants.HTTP_PROTOCOL_VERSION, httpServletRequest.getProtocol());
        enriched.put(Constants.HTTP_SCHEME, httpServletRequest.getScheme());
        enriched.put(Constants.HTTP_CLIENT_IP_ADDRESS, httpServletRequest.getRemoteAddr());

        Map<String, List<String>> headers = extractHttpHeaders(httpServletRequest);
        enriched.put(Constants.HTTP_HEADERS, headers);

        enriched.put(TRACE_ID, headers.get(TRACE_ID));
        enriched.put(SPAN_ID, headers.get(SPAN_ID));
        enriched.put(PARENT_SPAN_ID, headers.get(PARENT_SPAN_ID));
        enriched.put(SAMPLED, headers.get(SAMPLED));
        enriched.put(FLAGS, headers.get(FLAGS));

        enriched.put(Constants.FHIR_REQUEST_PARAMETERS, parameters);
        return enriched;
    }

    /**
     * @param httpServletRequest HTTP servlet request.
     * @return A map from header name to a list of header values.  This map is never <code>null</code>,
     * its values are never <code>null</code> and never empty.
     */
    private static Map<String, List<String>> extractHttpHeaders(HttpServletRequest httpServletRequest) {
        Map<String, List<String>> result = new HashMap<>();
        Enumeration<String> enumNames = httpServletRequest.getHeaderNames();
        if (enumNames != null) {
            while (enumNames.hasMoreElements()) {
                String name = enumNames.nextElement();
                Enumeration<String> enumValues = httpServletRequest.getHeaders(name);
                if (enumValues != null) {
                    List<String> list = new ArrayList<>();
                    while (enumValues.hasMoreElements()) {
                        list.add(enumValues.nextElement());
                    }
                    if (!list.isEmpty()) {
                        result.put(name, list);
                    }
                }
            }
        }
        return result;
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
