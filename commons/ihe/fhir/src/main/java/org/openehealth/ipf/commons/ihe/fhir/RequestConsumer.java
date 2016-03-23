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

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;

/**
 * Consumer interface of FHIR requests. Plain providers or resource providers forward the
 * request data received to an instance of this interface to be processed. This decouples
 * request reception from request handling, so you can e.g. use the IHE resource providers
 * outside of Apache Camel.
 *
 * @author Christian Ohr
 * @since 3.1
 */

public interface RequestConsumer {

    /**
     * Handles a Create / Update / Validate / Delete action request.
     * @param payload request payload
     * @param headers request parameters, e.g. search parameters
     * @return result of the action execution
     */
    MethodOutcome handleAction(Object payload, Map<String, Object> headers);

    /**
     * Handles the request for a resource
     *
     * @param payload    request payload
     * @param headers    request parameters, e.g. search parameters
     * @param resultType type of the returned resource
     * @param <R>        type of the returned resource
     * @return resource to be returned
     */
    <R extends IBaseResource> R handleResourceRequest(Object payload, Map<String, Object> headers, Class<R> resultType);

    // NOTE: instead of returning a List of resources, we could as well return an instance of
    // IBundleProvider, that can be implemented in way that resources are only loaded once they
    // are actually requested e.g. thruogh paging. For, now that seems to be over-optimization
    // at the expense of genericity.

    /**
     * Handles the request for a bundle, effectively being a list of resources.
     *
     * @param payload request payload
     * @param headers request parameters, e.g. search parameters
     * @param <R>     type of the returned resources contained in the bundle
     * @return list of resources to be returned
     */
    <R extends IBaseResource> List<R> handleBundleRequest(Object payload, Map<String, Object> headers);

    /**
     * Handles transaction requests
     * @param payload request payload
     * @param headers request parameters
     * @return transaction response bundle
     */
    Bundle handleTransactionRequest(Object payload, Map<String, Object> headers);
}
