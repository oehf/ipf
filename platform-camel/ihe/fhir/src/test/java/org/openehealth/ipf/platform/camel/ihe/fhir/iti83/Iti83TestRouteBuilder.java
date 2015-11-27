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

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixQueryResponseToPixmResponseTranslator;
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixmRequestToPixQueryTranslator;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirTestContainer;

import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorFhirToHL7v2;
import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorHL7v2ToFhir;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirResourceValidators.validateSchema;
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.itiValidator;

/**
 *
 */
public class Iti83TestRouteBuilder extends RouteBuilder {

    private final TranslatorFhirToHL7v2 requestTranslator;
    private final TranslatorHL7v2ToFhir responseTranslator;

    private ResponseCase responseCase = ResponseCase.OK;

    public Iti83TestRouteBuilder(UriMapper uriMapper) {
        super();
        this.requestTranslator = new PixmRequestToPixQueryTranslator(uriMapper);
        this.responseTranslator = new PixQueryResponseToPixmResponseTranslator(uriMapper);
    }

    public void setResponse(ResponseCase responseCase) {
        this.responseCase = responseCase;
    }

    @Override
    public void configure() throws Exception {

        from("direct:input")
                .toF("pixm-iti83:localhost:%d", FhirTestContainer.DEMO_APP_PORT);

        from("pixm-iti83:translation?audit=true")
                // Translate into ITI-9
                .errorHandler(noErrorHandler())
                .process(validateSchema())
                .process(translatorFhirToHL7v2(requestTranslator))
                .process(itiValidator())
                // Create some static response
                .transform(new Iti9Responder(responseCase))
                // Translate back into FHIR
                .process(itiValidator())
                .process(translatorHL7v2ToFhir(responseTranslator))
                .process(validateSchema());
    }


}
