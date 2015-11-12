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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.hl7.fhir.instance.model.Parameters;
import org.hl7.fhir.instance.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.atna.iti83.Iti83AuditDataset;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirProducer;

/**
 * Producer for PIXm Queries
 *
 * @since 3.1
 */
public class Iti83Producer extends FhirProducer<Iti83AuditDataset> {

    public Iti83Producer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    protected void doProcess(Exchange exchange, IGenericClient client) throws InvalidPayloadException {
        Parameters parameters = exchange.getIn().getMandatoryBody(Parameters.class);
        Parameters result = client.operation()
                .onType(Patient.class)
                .named(Constants.PIXM_OPERATION_NAME)
                .withParameters(parameters)
                .useHttpGet()
                .execute();
        Message resultMessage = Exchanges.resultMessage(exchange);
        resultMessage.setBody(result);
    }
}
