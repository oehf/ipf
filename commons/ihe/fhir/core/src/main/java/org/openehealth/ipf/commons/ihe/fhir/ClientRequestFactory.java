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

import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;

import java.util.Map;

/**
 * Factory for creating a FHIR request using a FHIR client and a sequence of input data
 *
 * @author Christian Ohr
 * @since 3.1
 */
public interface ClientRequestFactory<T extends IClientExecutable<?, ?>> {

    /**
     * Returns a FHIR request using a FHIR client and a sequence of input data
     *
     * @param client      FHIR client
     * @param requestData main request data (usually an implementation of {@link org.hl7.fhir.instance.model.api.IBaseResource}
     * @param parameters  parameter map
     * @return FHIR request executable
     */
    IClientExecutable<T, ?> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters);
}
