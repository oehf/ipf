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
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract plain provider that allows subclasses to forward the received payload into the
 * Camel route served by the consumer. Note that this can be subclassed for writing so-called
 * plain providers, while resource-specific providers should extend from {@link AbstractResourceProvider}.
 *
 * Providers that inherit from this class may only be connected to one consumer.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class AbstractPlainProvider extends FhirProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPlainProvider.class);

    private RequestConsumer consumer;

    @Override
    protected FhirContext getFhirContext() {
        return consumer != null ? consumer.getFhirContext() : null;
    }

    @Override
    protected Optional<RequestConsumer> getConsumer(Object payload) {
        return Optional.ofNullable(consumer)
                .filter(c -> c.test(payload));
    }

    @Override
    public void setConsumer(RequestConsumer consumer) {
        if (this.consumer != null) {
            throw new IllegalStateException("This provider is already used by a different consumer: " + consumer);
        }
        this.consumer = consumer;
        LOG.info("Connected consumer {} to provider {}", consumer, this);
    }

    @Override
    public void unsetConsumer(RequestConsumer consumer) {
        if (this.consumer == consumer) {
            this.consumer = null;
            LOG.info("Disconnected consumer {} from provider {}", consumer, this);
        }
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
        RequestConsumer consumer = getConsumer(payload).orElseThrow(() ->
                new IllegalStateException("Consumer is not initialized"));
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        return consumer.handleResourceRequest(payload, headers, resultType);
    }

    /**
     * Requests a list of resources
     *
     * @param payload             FHIR request resource
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> List<R> requestBundle(Object payload,
                                                                    HttpServletRequest httpServletRequest,
                                                                    HttpServletResponse httpServletResponse) {
        return requestBundle(payload, null, httpServletRequest, httpServletResponse);
    }

    /**
     * Requests a list of resources with parameters
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
        return requestBundle(payload, parameters, null, httpServletRequest, httpServletResponse);
    }

    /**
     * Requests a list of resources with parameters
     *
     * @param payload             FHIR request resource (often null)
     * @param parameters          FHIR search parameters
     * @param resourceType        FHIR resource type being searched
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @param <R>                 Result type
     * @return result of processing
     */
    protected final <R extends IBaseResource> List<R> requestBundle(
            Object payload, FhirSearchParameters parameters,
            String resourceType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RequestConsumer consumer = getConsumer(payload).orElseThrow(() ->
                new IllegalStateException("Consumer is not initialized"));
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        if (resourceType != null) {
            headers.put(Constants.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }
        return consumer.handleBundleRequest(payload, headers);
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
        return requestBundleProvider(payload, searchParameters, null, httpServletRequest, httpServletResponse);
    }

    /**
     * Requests a {@link IBundleProvider} that takes over the responsibility to fetch requested
     * bundles. The type of the returned {@link IBundleProvider} instance is determined
     * by the {@link #consumer RequestConsumer} impelmentation.
     *
     * @param payload             FHIR request resource (often null)
     * @param searchParameters    FHIR search parameters
     * @param resourceType        FHIR resource type
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return IBundleProvider
     */
    protected final IBundleProvider requestBundleProvider(
            Object payload, FhirSearchParameters searchParameters,
            String resourceType,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RequestConsumer consumer = getConsumer(payload).orElseThrow(() ->
                new IllegalStateException("Consumer is not initialized"));
        Map<String, Object> headers = enrichParameters(searchParameters, httpServletRequest);
        if (resourceType != null) {
            headers.put(Constants.FHIR_RESOURCE_TYPE_HEADER, resourceType);
        }
        return consumer.handleBundleProviderRequest(payload, headers);
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
        RequestConsumer consumer = getConsumer(payload).orElseThrow(() ->
                new IllegalStateException("Consumer is not initialized"));
        Map<String, Object> headers = enrichParameters(parameters, httpServletRequest);
        return consumer.handleAction(payload, headers);
    }


}
