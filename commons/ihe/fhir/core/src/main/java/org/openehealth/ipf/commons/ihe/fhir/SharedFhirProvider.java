/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Shared Resource provider, primarily (but not exclusively)  meant for batch/transaction requests.
 * Use this resource provider if you have several consumers that share the same FHIR interface.
 * The request is dispatched to the first consumer that returns true on {@link RequestConsumer#test(Object)}.
 * <p>
 * Components/Endpoints that use this resource provider must reference a (shared) singleton instance of a
 * concrete implementation of this class.
 * </p>
 * <p>
 * Request consumers must share the same {@link FhirContext} instance.
 * </p>
 *
 * @author Christian Ohr
 */
public abstract class SharedFhirProvider extends FhirProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SharedFhirProvider.class);

    private FhirContext fhirContext;
    private final List<RequestConsumer> consumers = new ArrayList<>();

    @Override
    protected FhirContext getFhirContext() {
        return fhirContext;
    }

    @Override
    protected Optional<RequestConsumer> getConsumer(Object payload) {
        return consumers.stream()
                .filter(c -> c.test(payload))
                .findFirst();
    }

    /**
     * @return true if the first consumer has been added, false otherwise
     */
    @Override
    public boolean requiresRegistration() {
        return consumers.size() == 1;
    }

    /**
     * @return false if the last consumer has been removed, false otherwise
     */
    @Override
    public boolean requiresDeregistration() {
        return consumers.isEmpty();
    }

    /**
     * Submits a transaction request bundle, expecting a corresponding response bundle
     *
     * @param payload             transaction bundle
     * @param httpServletRequest  servlet request
     * @param httpServletResponse servlet response
     * @return result of processing
     */
    protected final <T extends IBaseBundle> T requestTransaction(
            Object payload,
            Class<T> bundleClass,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            RequestDetails requestDetails) {
        var consumer = getConsumer(payload).orElseThrow(() ->
                new IllegalStateException("Request does not match any consumer or consumers are not initialized"));
        var headers = enrichParameters(null, httpServletRequest, requestDetails);
        return consumer.handleTransactionRequest(payload, headers, bundleClass);
    }

    /**
     * Adds a request consumer for this resource provider
     *
     * @param consumer request consumer
     * @throws IllegalStateException if the consumer is already present or if the FhirContext
     *                               is different compared to the FhirContext of the other consumers.
     */
    @Override
    public void setConsumer(RequestConsumer consumer) {
        if (consumers.isEmpty()) {
            fhirContext = consumer.getFhirContext();
        } else if (consumers.contains(consumer)) {
            throw new IllegalStateException("This provider has this consumer already registered: " + consumer);
        } else if (consumer.getFhirContext() != fhirContext) {
            throw new IllegalStateException("Consumer has a different FhirContext than the others: " + consumer);
        }
        consumers.add(consumer);
        LOG.info("Connected consumer {} to provider {}", consumer, this);
    }

    /**
     * Removes the request consumer. If the consumer was not registered, this method does nothing.
     *
     * @param consumer request consumer
     */
    @Override
    public void unsetConsumer(RequestConsumer consumer) {
        if (consumers.remove(consumer)) {
            LOG.info("Disconnected consumer {} from provider {}", consumer, this);
        }
        if (consumers.isEmpty()) {
            fhirContext = null;
        }
    }
}
