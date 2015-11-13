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

import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

/**
 * Request Factory for Iti-83 requests
 *
 * @since 3.1
 */
public class Iti83ClientRequestFactory implements ClientRequestFactory<IOperationUntypedWithInput<Parameters>> {


    @Override
    public IClientExecutable<IOperationUntypedWithInput<Parameters>, ?> getClientExecutable(IGenericClient client, Object parameters) {
        return client.operation()
                .onType(Patient.class)
                .named(Constants.PIXM_OPERATION_NAME)
                .withParameters((Parameters)parameters)
                .useHttpGet();
    }
}
