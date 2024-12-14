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
package org.openehealth.ipf.platform.camel.ihe.fhir.pharm5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.ExpressionAdapter;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.pharm5.Pharm5SearchParameters;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 *
 */
public class Pharm5TestRouteBuilder extends RouteBuilder {

    private final boolean returnError;

    public Pharm5TestRouteBuilder(boolean returnError) {
        this.returnError = returnError;
    }

    @Override
    public void configure() {

        from("direct:input")
                .toF("cmpd-pharm5:localhost:%d?fhirContext=#fhirContext", FhirTestContainer.DEMO_APP_PORT);

        from("cmpd-pharm5:translation?audit=true&fhirContext=#fhirContext")
                .errorHandler(noErrorHandler())
                .transform(new Pharm5Responder());
    }


    private class Pharm5Responder extends ExpressionAdapter {

        @Override
        public Object evaluate(Exchange exchange) {
            if (!returnError) {
                var resource = FhirContext.forR4().newXmlParser()
                        .parseResource(Bundle.class, new InputStreamReader(getClass().getResourceAsStream("/FindDocumentReferencesResponse.xml")));
                // The endpoint expects a list of resources rather than a bundle
                final var resources = resource.getEntry().stream()
                        .map(Bundle.BundleEntryComponent::getResource)
                        .collect(Collectors.toList());

                final var searchParameters = exchange.getIn().getHeader(Constants.FHIR_REQUEST_PARAMETERS, Pharm5SearchParameters.class);
                ((DocumentReference) resources.get(0)).setDescription(searchParameters.getOperation().getOperation());
                return resources;
            } else {
                throw new InternalErrorException("Something went wrong");
            }
        }
    }

}
