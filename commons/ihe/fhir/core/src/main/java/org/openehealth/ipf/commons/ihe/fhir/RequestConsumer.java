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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Consumer interface of FHIR requests. Plain providers or resource providers forward the
 * request data received to an instance of this interface to be processed. This decouples
 * request reception from request handling, so you can e.g. use the IHE resource providers
 * outside of Apache Camel.
 * <p>
 * The request parameters may contain the following parameters entries indicating a "lazy-loading" mode
 * on searches.
 * <ul>
 * <li>{@link Constants#FHIR_REQUEST_SIZE_ONLY}: if this entry is present, the requester expects only
 * the result size to be returned in an parameter entry with the same name and calls {@link #handleSizeRequest(Object, Map, Map)}
 * to do so. If possible, implementations should only request the result size from the backend rather than
 * a complete result set.</li>
 * <li>{@link Constants#FHIR_FROM_INDEX} and {@link Constants#FHIR_TO_INDEX}: if these entries are present,
 * the requester expects only the specified subset of the result. If possible, implementations
 * should only request this result set from the backend rather than a complete result set.</li>
 * </ul>
 * </p>
 *
 * @author Christian Ohr
 * @since 3.1
 */

public interface RequestConsumer extends Predicate<RequestDetails> {

    /**
     * @return the FhirContext used by this consumer
     */
    FhirContext getFhirContext();

    /**
     * Returns true if this RequestConsumer can handle the provided FHIR payload
     *
     * @param requestDetails FHIR requestDetails
     * @return true if this RequestConsumer can handle the provided FHIR request, false otherwise
     */
    @Override
    default boolean test(RequestDetails requestDetails) {
        return true;
    }

    /**
     * Handles a Create / Update / Validate / Delete action request.
     *
     * @param payload    request payload
     * @param inHeaders  request parameters, e.g. search parameters
     * @param outHeaders map where Camel response headers will be copied into
     * @return result of the action execution
     */
    MethodOutcome handleAction(Object payload, Map<String, Object> inHeaders, Map<String, Object> outHeaders);

    /**
     * Handles the request for a resource
     *
     * @param payload    request payload
     * @param inHeaders  request parameters, e.g. search parameters
     * @param outHeaders map where Camel response headers will be copied into
     * @param resultType type of the returned resource
     * @param <R>        type of the returned resource
     * @return resource to be returned
     */
    <R extends IBaseResource> R handleResourceRequest(Object payload, Map<String, Object> inHeaders, Map<String, Object> outHeaders, Class<R> resultType);

    /**
     * Handles the (search) request for a bundle, effectively being a list of resources.
     * <p>
     * If {@link #supportsLazyLoading()}
     * returns true, the headers may contain {@link Constants#FHIR_FROM_INDEX} and {@link Constants#FHIR_TO_INDEX} entries,
     * indicating that only a part of the result is requested. Implementations must return only the requested entries.
     * </p>
     *
     * @param payload    request payload
     * @param inHeaders  request parameters, e.g. search parameters or
     * @param outHeaders map where Camel response headers will be copied into
     * @param <R>        type of the returned resources contained in the bundle
     * @return list of resources to be returned
     */
    <R extends IBaseResource> List<R> handleBundleRequest(Object payload, Map<String, Object> inHeaders, Map<String, Object> outHeaders);

    /**
     * Handles the request for a bundle provider, effectively constructing a list of resources. The returned
     * IBundleProvider takes over the responsibility to fetch the required subset of the result, usually
     * by indirectly calling {@link #handleBundleRequest(Object, Map, Map)} as required.
     *
     * @param payload request payload
     * @param headers request parameters, e.g. search parameters
     * @param httpServletResponse HTTP servlet response
     * @return a bundle provider
     */
    IBundleProvider handleBundleProviderRequest(Object payload, Map<String, Object> headers, HttpServletResponse httpServletResponse);

    /**
     * Handles transaction requests
     *
     * @param payload    request payload
     * @param inHeaders  request parameters
     * @param outHeaders map where Camel response headers will be copied into
     * @return transaction response bundle
     */
    <T extends IBaseBundle> T handleTransactionRequest(Object payload, Map<String, Object> inHeaders, Map<String, Object> outHeaders, Class<T> bundleClass);

    /**
     * Optional method that request the result size of a bundle request. Only used for lazy
     * bundle providers. The headers contain a {@link Constants#FHIR_REQUEST_SIZE_ONLY} entry flag.
     * This method only needs to be implemented is {@link #supportsLazyLoading()} returns true.
     *
     * @param payload request payload
     * @param headers request parameters
     * @return transaction response bundle
     */
    int handleSizeRequest(Object payload, Map<String, Object> headers);

    /**
     * @return returns true indicating that lazy loading of search results is supported, false otherwise.
     */
    boolean supportsLazyLoading();

    default String getName() {
        return getClass().getName();
    }
}
