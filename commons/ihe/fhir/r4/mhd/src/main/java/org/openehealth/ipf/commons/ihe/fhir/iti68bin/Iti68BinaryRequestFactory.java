/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti68bin;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IReadExecutable;
import org.hl7.fhir.r4.model.Binary;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;

import java.util.Map;

/**
 * A {@link ClientRequestFactory} for ITI-105 requests
 *
 * @author Boris Stanojevic
 * @since 4.8
 */
public class Iti68BinaryRequestFactory implements ClientRequestFactory {

    @Override
    public IReadExecutable<Binary> getClientExecutable(
            IGenericClient client,
            Object requestData,
            Map<String, Object> parameters) {
        return client.read().resource(Binary.class).withId(requestData.toString());
    }

}
