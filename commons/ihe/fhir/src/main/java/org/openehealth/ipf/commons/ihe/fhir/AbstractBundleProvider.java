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
import ca.uhn.fhir.rest.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.RequestConsumer;

import java.util.List;
import java.util.Map;

/**
 * Base class of a {@link IBundleProvider} delegating to a {@link RequestConsumer} instance
 */
public abstract class AbstractBundleProvider implements IBundleProvider {

    private final RequestConsumer consumer;
    private Object payload;
    private Map<String, Object> headers;
    private FhirValidator validator;

    public AbstractBundleProvider(RequestConsumer consumer, Object payload, Map<String, Object> headers, FhirValidator validator) {
        this.consumer = consumer;
        this.payload = payload;
        this.headers = headers;
        this.validator = validator;
    }

    @Override
    public InstantDt getPublished() {
        return InstantDt.withCurrentTime();
    }

    @Override
    public Integer preferredPageSize() {
        return null;
    }

    protected List<IBaseResource> obtainResources(Object payload, Map<String, Object> headers) {
        List<IBaseResource> resources = consumer.handleBundleRequest(payload, headers);
        validator.validateResponse(resources);
        return resources;
    }

    protected RequestConsumer getConsumer() {
        return consumer;
    }

    protected Map<String, Object> getHeaders() {
        return headers;
    }

    protected Object getPayload() {
        return payload;
    }
}
