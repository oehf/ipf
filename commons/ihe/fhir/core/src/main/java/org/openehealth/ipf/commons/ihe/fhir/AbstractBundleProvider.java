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

import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_DETAILS;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_PARAMETERS;

/**
 * Base class of a {@link IBundleProvider} delegating to a {@link RequestConsumer} instance
 */
public abstract class AbstractBundleProvider implements IBundleProvider {

    private final RequestConsumer consumer;
    private final Object payload;
    private final Map<String, Object> headers;
    private final boolean sort;
    protected final HttpServletResponse httpServletResponse;

    public AbstractBundleProvider(RequestConsumer consumer, Object payload, Map<String, Object> headers, HttpServletResponse httpServletResponse) {
        this(consumer, false, payload, headers, httpServletResponse);
    }

    public AbstractBundleProvider(RequestConsumer consumer, boolean sort, Object payload, Map<String, Object> headers, HttpServletResponse httpServletResponse) {
        this.consumer = consumer;
        this.payload = payload;
        this.headers = headers;
        this.sort = sort;
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * Copies a bundle provider and binds the copy to a later request, typically one that asks for
     * another page of a result that has been cached with {@link SpringCachePagingProvider}.
     * <p>
     * The servlet request and response of the original request are recycled by the servlet
     * container once it has completed, so the copy writes to the response of the current request and
     * gets {@link RequestDetails} that describe the original search, but are backed by the current
     * servlet request.
     *
     * @param original       bundle provider that was created for the original request
     * @param currentRequest the request that is being processed now
     */
    protected AbstractBundleProvider(AbstractBundleProvider original, RequestDetails currentRequest) {
        this.consumer = original.consumer;
        this.payload = original.payload;
        this.sort = original.sort;
        this.headers = new HashMap<>(original.headers);
        if (currentRequest instanceof ServletRequestDetails current) {
            this.httpServletResponse = current.getServletResponse();
            if (original.headers.get(FHIR_REQUEST_DETAILS) instanceof RequestDetails originalRequest) {
                this.headers.put(FHIR_REQUEST_DETAILS, rebind(originalRequest, current));
            }
        } else {
            this.httpServletResponse = original.httpServletResponse;
        }
    }

    /**
     * Returns a bundle provider that can serve the given request. Subclasses that call the
     * {@link RequestConsumer} again after the original request has completed must return a copy
     * created with {@link #AbstractBundleProvider(AbstractBundleProvider, RequestDetails)}.
     *
     * @param requestDetails the request that is being processed now
     * @return a bundle provider bound to the request, or this instance if it does not need to be bound
     */
    public AbstractBundleProvider boundTo(RequestDetails requestDetails) {
        return this;
    }

    /**
     * The current request of a paging call is a {@code GET_PAGE} without resource name or search
     * parameters, so it is used only as the servlet-bound part, while everything describing the
     * operation is taken over from the original request.
     */
    private static ServletRequestDetails rebind(RequestDetails original, ServletRequestDetails current) {
        var rebound = new ServletRequestDetails(current);
        rebound.setRestOperationType(original.getRestOperationType());
        rebound.setResourceName(original.getResourceName());
        rebound.setId(original.getId());
        rebound.setOperation(original.getOperation());
        rebound.setSecondaryOperation(original.getSecondaryOperation());
        rebound.setCompartmentName(original.getCompartmentName());
        rebound.setRequestType(original.getRequestType());
        if (original.getRequestPath() != null) {
            rebound.setRequestPath(original.getRequestPath());
        }
        rebound.setCompleteUrl(original.getCompleteUrl());
        rebound.setParameters(original.getParameters());
        rebound.setTenantId(original.getTenantId());
        rebound.setResource(original.getResource());
        if (original.getRequestContentsIfLoaded() != null) {
            rebound.setRequestContents(original.getRequestContentsIfLoaded());
        }
        original.getUserData().forEach(rebound.getUserData()::putIfAbsent);
        return rebound;
    }

    @Override
    public InstantDt getPublished() {
        return InstantDt.withCurrentTime();
    }

    @Override
    public Integer preferredPageSize() {
        return null;
    }

    protected List<IBaseResource> obtainResources(Object payload, Map<String, Object> inHeaders) {
        HashMap<String, Object> outHeaders = new HashMap<>();
        List<IBaseResource> resources = consumer.handleBundleRequest(payload, inHeaders, outHeaders);
        FhirProvider.processOutHeaders(outHeaders, httpServletResponse);
        return resources;
    }

    protected RequestConsumer getConsumer() {
        return consumer;
    }

    /**
     * @return a copy of the original query parameters
     */
    protected Map<String, Object> getHeaders() {
        return new HashMap<>(headers);
    }

    protected Object getPayload() {
        return payload;
    }

    @Override
    public String getUuid() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> void sortIfApplicable(List<T> resources) {
        if (sort && headers.containsKey(FHIR_REQUEST_PARAMETERS)) {
            var searchParameters =  headers.get(FHIR_REQUEST_PARAMETERS);
            if (searchParameters instanceof FhirSearchAndSortParameters fhirSearchAndSortParameters) {
                fhirSearchAndSortParameters.sort(resources);
            }
        }
    }

}
