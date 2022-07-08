/*
 * Copyright 2022 the original author or authors.
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
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import lombok.experimental.Delegate;

/**
 * Delegating FhirContext with a custom IRestfulClientFactory. This is more lightweight
 * than to create new FhirContext instances just due to a different HTTP client setup.
 */
public class DelegatingFhirContext extends FhirContext {

    interface Excluded {
        @SuppressWarnings("unused")
        IRestfulClientFactory getRestfulClientFactory();
    }

    @Delegate(excludes = Excluded.class)
    private final FhirContext fhirContext;

    private final IRestfulClientFactory restfulClientFactory;

    public DelegatingFhirContext(FhirContext fhirContext, IRestfulClientFactory restfulClientFactory) {
        this.fhirContext = fhirContext;
        this.restfulClientFactory = restfulClientFactory;
    }

    @Override
    public IRestfulClientFactory getRestfulClientFactory() {
        return restfulClientFactory != null ? restfulClientFactory : fhirContext.getRestfulClientFactory();
    }

    public FhirContext getDelegate() {
        return fhirContext;
    }
}


