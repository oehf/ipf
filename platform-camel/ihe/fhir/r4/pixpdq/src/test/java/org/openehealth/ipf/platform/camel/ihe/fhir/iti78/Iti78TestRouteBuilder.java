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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti78;

import ca.uhn.hl7v2.model.Message;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqResponseToPdqmResponseTranslator;
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqmRequestToPdqQueryTranslator;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateFhir;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateToFhir;
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.itiValidator;

/**
 *
 */
public class Iti78TestRouteBuilder extends RouteBuilder {

    private final PdqmRequestToPdqQueryTranslator requestTranslator;
    private final PdqResponseToPdqmResponseTranslator responseTranslator;

    private ResponseCase responseCase = ResponseCase.OK;

    public Iti78TestRouteBuilder(UriMapper uriMapper) {
        super();
        this.requestTranslator = new PdqmRequestToPdqQueryTranslator(uriMapper);
        requestTranslator.setPdqSupplierResourceIdentifierUri("urn:oid:1.2.3.4.5.6");
        this.responseTranslator = new PdqResponseToPdqmResponseTranslator(uriMapper);
        responseTranslator.setPdqSupplierResourceIdentifierUri("urn:oid:1.2.3.4.5.6");
    }

    public void setResponse(ResponseCase responseCase) {
        this.responseCase = responseCase;
    }

    @Override
    public void configure() {

        from("direct:input")
                .toF("pdqm-iti78:localhost:%d?fhirContext=#fhirContext", FhirTestContainer.DEMO_APP_PORT);

        from("pdqm-iti78:translation?audit=true&fhirContext=#fhirContext")
                // Translate into ITI-9
                .errorHandler(noErrorHandler())
                .process(translateFhir(requestTranslator))
                .process(itiValidator())
                // Create some static response
                .transform(new Iti21Responder(responseCase))
                // Translate back into FHIR
                .process(itiValidator())
                .process(translateToFhir(responseTranslator, Message.class));
    }


}
