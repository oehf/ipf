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

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.SuspendableService;
import org.apache.camel.support.DefaultConsumer;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.EagerBundleProvider;
import org.openehealth.ipf.commons.ihe.fhir.LazyBundleProvider;
import org.openehealth.ipf.commons.ihe.fhir.RequestConsumer;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_SIZE_ONLY;

/**
 * FHIR consumer, which is an implementation of a {@link RequestConsumer} that handles requests
 * by sending the request data and parameters into a Camel route and returning the result of
 * the route processing.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class FhirConsumer<AuditDatasetType extends FhirAuditDataset> extends DefaultConsumer
        implements SuspendableService, RequestConsumer {


    public FhirConsumer(FhirEndpoint<AuditDatasetType, ? extends FhirComponent<AuditDatasetType>> endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public boolean test(RequestDetails requestDetails) {
        return getEndpoint().getConsumerSelector().test(requestDetails);
    }

    @Override
    public String getName() {
        return getEndpoint().getId();
    }

    @Override
    public FhirContext getFhirContext() {
        return getEndpoint().getInterceptableConfiguration().getContext();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        getEndpoint().connect(this);
    }

    @Override
    protected void doStop() throws Exception {
        getEndpoint().disconnect(this);
        super.doStop();
    }

    @Override
    public FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>> getEndpoint() {
        return (FhirEndpoint<AuditDatasetType, FhirComponent<AuditDatasetType>>) super.getEndpoint();
    }

    /**
     * This method can be called by {@link ca.uhn.fhir.rest.server.IResourceProvider} objects to send the received
     * (and potentially handled) request further down a Camel route.
     *
     * @param payload     FHIR request content
     * @param headers     headers
     * @param resultClass class of the result resource
     * @param <R>         Resource type being returned
     * @return result of processing the FHIR request in Camel
     */
    @Override
    public final <R extends IBaseResource> R handleResourceRequest(Object payload, Map<String, Object> headers, Class<R> resultClass) {
        Object result = handleInRoute(payload, headers, resultClass);
        if (result == null) return null;
        if (resultClass.isAssignableFrom(result.getClass())) {
            return resultClass.cast(result);
        } else {
            throw new IllegalArgumentException("Expected a resource of type " + resultClass.getName() +
                    " or a list thereof, but was " + result.getClass());
        }
    }

    /**
     * @param payload request payload
     * @param headers request parameters, e.g. search parameters
     * @param <R> resource type
     * @return list of resources to be packaged into a bundle
     */
    @Override
    public <R extends IBaseResource> List<R> handleBundleRequest(Object payload, Map<String, Object> headers) {
        return handleInRoute(payload, headers, List.class);
    }

    @Override
    public IBundleProvider handleBundleProviderRequest(Object payload, Map<String, Object> headers) {
        return getBundleProvider(payload, headers);
    }

    @Override
    public <T extends IBaseBundle> T handleTransactionRequest(Object payload, Map<String, Object> headers, Class<T> bundleClass) {
        return handleInRoute(payload, headers, bundleClass);
    }

    @Override
    public MethodOutcome handleAction(Object payload, Map<String, Object> headers) {
        return handleInRoute(payload, headers, MethodOutcome.class);
    }

    @Override
    public int handleSizeRequest(Object payload, Map<String, Object> headers) {
        var exchange = runRoute(payload, headers);
        var resultMessage = exchange.getMessage();
        var size = resultMessage.getHeader(FHIR_REQUEST_SIZE_ONLY, Integer.class);
        if (size == null) {
            throw new InternalErrorException("Server did not obtain result size");
        }
        return size;
    }

    @Override
    public boolean supportsLazyLoading() {
        return getEndpoint().getInterceptableConfiguration().isLazyLoadBundles();
    }

    /**
     * Forwards the request to be handled into a Camel route
     *
     * @param payload     request payload, will become the Camel message body
     * @param headers     request parameters, will be added to the Camel headers
     * @param resultClass expected body type to be returned
     * @return request result, type-converted into the required result class
     */
    protected <T> T handleInRoute(Object payload, Map<String, Object> headers, Class<T> resultClass) {
        var exchange = runRoute(payload, headers);
        var resultMessage = exchange.getMessage();
        if (resultMessage.getBody() instanceof List && IBaseResource.class.isAssignableFrom(resultClass)) {
            var singletonList = (List<T>)resultMessage.getBody();
            if (singletonList.isEmpty() && payload instanceof IIdType) {
                throw new ResourceNotFoundException((IIdType)payload);
            }
            resultMessage.setBody(singletonList.isEmpty() ? null : singletonList.get(0));
        }
        return getEndpoint().getCamelContext().getTypeConverter().convertTo(resultClass, exchange, resultMessage.getBody());
    }

    protected Exchange runRoute(Object payload, Map<String, Object> headers) {
        var exchange = getEndpoint().createExchange();
        exchange.getIn().setBody(payload);
        if (headers != null) {
            exchange.getIn().getHeaders().putAll(headers);
        }

        // Add the FHIR context and InteractionID as header
        exchange.getIn().setHeader(Constants.FHIR_CONTEXT,
                getEndpoint().getInterceptableConfiguration().getContext());
        exchange.getIn().setHeader(org.openehealth.ipf.commons.ihe.core.Constants.INTERACTION_ID_NAME,
                getEndpoint().getInterceptableComponent().getInteractionId());

        try {
            createUoW(exchange);
            getProcessor().process(exchange);
        } catch (Exception e) {
            getExceptionHandler().handleException(e);
        } finally {
            doneUoW(exchange);
        }

        // If the exchange has failed, throw the exception back into the servlet
        // TODO care about auditing?
        if (exchange.isFailed()) {
            var e = exchange.getException(BaseServerResponseException.class);
            throw (e != null) ? e : new InternalErrorException("Unexpected server error", exchange.getException());
        }
        return exchange;
    }


    /**
     * Returns the {@link IBundleProvider}, providing the matching bundles.
     * Depending on {@link FhirEndpointConfiguration#isLazyLoadBundles()}, the bundle provider either eagerly fetches all
     * matching bundles or fetches the requested subset on request.
     *
     * @param payload request payload
     * @param headers request headers
     * @return resulting bundle provider
     */
    protected IBundleProvider getBundleProvider(Object payload, Map<String, Object> headers) {
        var endpointConfiguration = getEndpoint().getInterceptableConfiguration();
        return supportsLazyLoading() ?
                new LazyBundleProvider(this,
                        endpointConfiguration.isCacheBundles(),
                        endpointConfiguration.isSort(),
                        payload,
                        headers) :
                new EagerBundleProvider(this,
                        endpointConfiguration.isSort(),
                        payload, headers);
    }
}
