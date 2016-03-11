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

package org.openehealth.ipf.commons.ihe.fhir.iti78;

import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IReadExecutable;
import org.hl7.fhir.instance.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;

import java.util.Map;

/**
 * Request Factory for ITI-78 requests, retrieving a single Patient resource based on a resource ID
 * in the request data.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class Iti78RetrieveResourceClientRequestFactory implements ClientRequestFactory<IReadExecutable<Patient>> {

    @Override
    public IClientExecutable<IReadExecutable<Patient>, Patient> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters) {
        IClientExecutable<IReadExecutable<Patient>, Patient> executable = client.read()
                    .resource(Patient.class)
                    .withId(requestData.toString());
        return executable;

    }
}
