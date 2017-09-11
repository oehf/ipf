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

import ca.uhn.hl7v2.model.Message
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqResponseToPdqmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.iti78.PdqmRequestToPdqQueryTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators

/**
 *
 */
class Iti78RouteBuilder extends RouteBuilder {

    private final FhirTranslator<Message> requestTranslator
    private final ToFhirTranslator<Message> responseTranslator
    private final String host
    private final int port

    private Boolean validateIti21Request = false
    private Boolean validateIti21Response = false

    public Iti78RouteBuilder(UriMapper uriMapper, String host, int iti21Port) {
        super();
        this.requestTranslator = new PdqmRequestToPdqQueryTranslator(uriMapper);
        this.responseTranslator = new PdqResponseToPdqmResponseTranslator(uriMapper);
        this.host = host;
        this.port = iti21Port
    }

    void setValidateIti21Request(boolean validateIti21Request) {
        this.validateIti21Request = validateIti21Request
    }

    void setValidateIti21Response(boolean validateIti21Response) {
        this.validateIti21Response = validateIti21Response
    }

    @Override
    void configure() throws Exception {
        from("pdqm-iti78:translation?audit=true")
                .routeId("pdqm-adapter")
        // pass back errors to the endpoint
                .errorHandler(noErrorHandler())
        // translate, forward, translate back
                .process(FhirCamelTranslators.translateFhir(requestTranslator))
                .to("pdq-iti21:${host}:${port}?audit=false")
                .process(FhirCamelTranslators.translateToFhir(responseTranslator, Message.class))
    }
}
