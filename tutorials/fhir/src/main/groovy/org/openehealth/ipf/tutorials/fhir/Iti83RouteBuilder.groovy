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

package org.openehealth.ipf.tutorials.fhir

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixQueryResponseToPixmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixmRequestToPixQueryTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter
import org.openehealth.ipf.platform.camel.hl7.HL7v2
import org.openehealth.ipf.platform.camel.ihe.fhir.iti83.Iti9Responder
import org.openehealth.ipf.platform.camel.ihe.fhir.iti83.ResponseCase

import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorFhirToHL7v2
import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorHL7v2ToFhir

/**
 *
 */
class Iti83RouteBuilder extends RouteBuilder {

    private final TranslatorFhirToHL7v2 requestTranslator
    private final TranslatorHL7v2ToFhir responseTranslator
    private final String host;
    private final int port

    private Boolean validateIti9Request = false;
    private Boolean validateIti9Response = false;

    public Iti83RouteBuilder(UriMapper uriMapper, String host, int iti9Port) {
        super();
        this.requestTranslator = new PixmRequestToPixQueryTranslator(uriMapper);
        this.responseTranslator = new PixQueryResponseToPixmResponseTranslator(uriMapper);
        this.host = host;
        this.port = iti9Port
    }

    void setValidateIti9Request(boolean validateIti9Request) {
        this.validateIti9Request = validateIti9Request
    }

    void setValidateIti9Response(boolean validateIti9Response) {
        this.validateIti9Response = validateIti9Response
    }

    @Override
    public void configure() throws Exception {
        from("pixm-iti83:translation?audit=true")
                .errorHandler(noErrorHandler())
        // Translate into ITI-9 QBP_Q23
                .process(translatorFhirToHL7v2(requestTranslator))
        // Validate translated request if desired
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, { validateIti9Request })
                .process(HL7v2.validatingProcessor())
        // Call ITI-9 endpoint. TODO add logging
                .transform(new Iti9Responder(ResponseCase.OK))
                //.to("pix-iti9:${host}:${port}?audit=false")
        // Validate if response desired
                .setHeader(ValidatorAdapter.NEED_VALIDATION_HEADER_NAME, { validateIti9Response })
                .process(HL7v2.validatingProcessor())
        // Translate back into FHIR
                .process(translatorHL7v2ToFhir(responseTranslator))
    }
}
