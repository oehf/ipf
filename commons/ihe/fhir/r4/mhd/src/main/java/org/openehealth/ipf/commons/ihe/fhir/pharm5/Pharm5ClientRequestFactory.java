/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Parameters;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;
import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.Map;

/**
 * Request Factory for PHARM-5 requests returning a bundle of document references.
 *
 * @author Quentin Ligier
 * @since 4.3
 **/
public class Pharm5ClientRequestFactory implements ClientRequestFactory<IOperationUntypedWithInput<Bundle>> {

    @Override
    public IClientExecutable<IOperationUntypedWithInput<Bundle>, ?> getClientExecutable(final IGenericClient client,
                                                                                            final Object requestData,
                                                                                            final Map<String, Object> parameters) {

        final Parameters urlParameters;
        final String operation;

        if (parameters == null) {
            throw new IllegalArgumentException("In CMPD PHARM-5, either 'FhirRequestParameters' or 'OPERATION_HEADER' shall be set");
        }

        if (parameters.containsKey(Constants.FHIR_REQUEST_PARAMETERS)) {
            final var searchParameters = (Pharm5SearchParameters) parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
            urlParameters = searchParameters.toParameters();
            operation = searchParameters.getOperation().getOperation();
        } else if (requestData instanceof Parameters) {
            urlParameters = (Parameters) requestData;
            if (!parameters.containsKey(Constants.FHIR_OPERATION_HEADER)) {
                throw new IllegalArgumentException("In CMPD PHARM-5, if using Parameters, the operation name shall be" +
                        " set in 'FHIR_OPERATION_HEADER'");
            }
            operation = ((Pharm5Operations) parameters.get(Constants.FHIR_OPERATION_HEADER)).getOperation();
        } else {
            throw new IllegalArgumentException("Missing search parameters for CMPD PHARM-5");
        }

        return client.operation()
                .onType(DocumentReference.class)
                .named(operation)
                .withParameters(urlParameters)
                .returnResourceType(Bundle.class)
                .useHttpGet();
    }
}
