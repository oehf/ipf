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
import org.openehealth.ipf.commons.ihe.core.chain.Chainable;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirEndpoint;


/**
 * Camel interceptor interface for FHIR transactions.
 *
 * @author Christian Ohr
 */
public interface FhirInterceptor<T extends FhirAuditDataset> extends Processor, Chainable {

    /**
     * @return the processor instance wrapped by this interceptor.
     */
    Processor getWrappedProcessor();

    /**
     * Lets this interceptor wrap the given processor.
     *
     * @param wrappedProcessor processor instance to be wrapped.
     */
    void setWrappedProcessor(Processor wrappedProcessor);

    FhirEndpoint<T> getFhirEndpoint();
}
