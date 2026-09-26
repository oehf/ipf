/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti104;

import ca.uhn.fhir.rest.api.MethodOutcome;
import org.apache.camel.builder.RouteBuilder;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.iti104.Iti104Parameters;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.MODEL;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.VALIDATION_MODE;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.itiRequestValidator;

/**
 * Acts as Patient Identifier Cross-reference Manager: validates the Patient and pretends to have
 * created it if its identifier value starts with {@link #NEW_PATIENT_PREFIX}, and to have updated or
 * removed it otherwise.
 */
public class Iti104TestRouteBuilder extends RouteBuilder {

    public static final String NEW_PATIENT_PREFIX = "new";

    private String consumerOptions = "";

    /**
     * @param consumerOptions additional options of the consumer endpoint, e.g. {@code &resourceProvider=#provider}
     */
    public void setConsumerOptions(String consumerOptions) {
        this.consumerOptions = consumerOptions;
    }

    @Override
    public void configure() {

        from("direct:input")
            .toF("pixm-iti104:localhost:%d", FhirTestContainer.DEMO_APP_PORT);

        from("pixm-iti104:feed?audit=true" + consumerOptions)
            .errorHandler(noErrorHandler())
            .setHeader(VALIDATION_MODE, constant(MODEL))
            .process(itiRequestValidator())
            .process(exchange -> {
                var parameters = exchange.getIn().getHeader(Constants.FHIR_REQUEST_PARAMETERS, Iti104Parameters.class);
                var identifier = parameters.getPatientIdentifier();
                var outcome = new MethodOutcome(new IdType("Patient", identifier.getValue()));
                if (exchange.getIn().getBody() instanceof Patient && identifier.getValue().startsWith(NEW_PATIENT_PREFIX)) {
                    outcome.setCreated(true);
                }
                exchange.getMessage().setBody(outcome);
            });
    }
}
