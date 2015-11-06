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

package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.core.chain.ChainableImpl;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;

/**
 *
 */
public abstract class AbstractFhirInterceptor extends ChainableImpl implements FhirInterceptor {

    private Processor wrappedProcessor;
    private FhirEndpoint<?> fhirEndpoint;

    @Override
    public Processor getWrappedProcessor() {
        return wrappedProcessor;
    }

    @Override
    public void setWrappedProcessor(Processor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }

    @Override
    public FhirEndpoint<?> getFhirEndpoint() {
        return fhirEndpoint;
    }

    @Override
    public void setFhirEndpoint(FhirEndpoint<?> fhirEndpoint) {
        this.fhirEndpoint = fhirEndpoint;
    }
}
