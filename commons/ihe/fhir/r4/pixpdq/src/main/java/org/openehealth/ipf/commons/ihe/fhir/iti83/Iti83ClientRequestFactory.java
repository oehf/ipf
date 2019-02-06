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

package org.openehealth.ipf.commons.ihe.fhir.iti83;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;

import java.util.Map;

/**
 * Request Factory for Iti-83 requests
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti83ClientRequestFactory implements ClientRequestFactory<IOperationUntypedWithInput<Parameters>> {

    @Override
    public IClientExecutable<IOperationUntypedWithInput<Parameters>, ?> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters) {

        if (requestData instanceof Parameters) {
            return getClientExecutable(client, (Parameters) requestData);
        } else {
            Parameters p = new Parameters();
            parameters.entrySet().stream()
                    .filter(entry -> Iti83Constants.ITI83_PARAMETERS.contains(entry.getKey()))
                    .forEach(entry -> p.addParameter()
                            .setName(entry.getKey())
                            .setValue(new StringType(entry.getValue().toString())));
            return getClientExecutable(client, p);
        }

    }

    private IClientExecutable<IOperationUntypedWithInput<Parameters>, ?> getClientExecutable(IGenericClient client, Parameters requestData) {
        return client.operation()
                .onType(Patient.class)
                .named(Iti83Constants.PIXM_OPERATION_NAME)
                .withParameters(requestData)
                .useHttpGet();
    }
}
