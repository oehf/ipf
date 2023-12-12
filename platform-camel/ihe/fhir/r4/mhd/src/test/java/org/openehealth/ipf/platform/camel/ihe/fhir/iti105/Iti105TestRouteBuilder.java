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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti105;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.support.ExpressionAdapter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.util.UUID;

import static org.apache.camel.component.http.HttpMethods.POST;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.SCHEMA;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.SCHEMATRON;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.VALIDATION_MODE;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelValidators.itiRequestValidator;

/**
 *
 */
public class Iti105TestRouteBuilder extends RouteBuilder {

    private final boolean returnError;

    public Iti105TestRouteBuilder(boolean returnError) {
        this.returnError = returnError;
    }

    @Override
    public void configure() {

        from("direct:input")
            .setHeader(Constants.HTTP_METHOD, POST)
            .toF("mhd-iti105:localhost:%d", FhirTestContainer.DEMO_APP_PORT);

        from("mhd-iti105:stub?audit=true&fhirContext=#fhirContext")
                .errorHandler(noErrorHandler())
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, constant(true))
                .setHeader(VALIDATION_MODE, constant(SCHEMA | SCHEMATRON )) // | MODEL))
                .process(itiRequestValidator())
                .transform(new Responder());
    }


    private class Responder extends ExpressionAdapter {

        @Override
        public Object evaluate(Exchange exchange) {

            if (returnError) throw new InternalErrorException("Something went wrong");

            var methodOutcome = new MethodOutcome(new IdType(UUID.randomUUID().toString()), true);
            var outcome = new OperationOutcome();
            outcome.setId(new IdType(UUID.randomUUID().toString()));
            methodOutcome.setOperationOutcome(outcome);
            return methodOutcome;
        }

    }
}
