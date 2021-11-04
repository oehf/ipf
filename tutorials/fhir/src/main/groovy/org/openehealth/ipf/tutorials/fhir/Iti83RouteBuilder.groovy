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
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixQueryResponseToPixmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixmRequestToPixQueryTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateFhir
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateToFhir

/**
 *
 */
class Iti83RouteBuilder extends RouteBuilder {

    private final FhirTranslator<Message> requestTranslator
    private final ToFhirTranslator<Message> responseTranslator
    private final String host
    private final int port

    private Boolean validateIti9Request = false
    private Boolean validateIti9Response = false

    Iti83RouteBuilder(UriMapper uriMapper, String host, int iti9Port) {
        super()
        this.requestTranslator = new PixmRequestToPixQueryTranslator(uriMapper)
        this.responseTranslator = new PixQueryResponseToPixmResponseTranslator(uriMapper)
        this.host = host
        this.port = iti9Port
    }

    void setValidateIti9Request(boolean validateIti9Request) {
        this.validateIti9Request = validateIti9Request
    }

    void setValidateIti9Response(boolean validateIti9Response) {
        this.validateIti9Response = validateIti9Response
    }

    @Override
    void configure() throws Exception {
        from("pixm-iti83:translation?audit=true")
                .routeId("pixm-adapter")
                // pass back errors to the endpoint
                .errorHandler(noErrorHandler())
                // translate, forward, translate back
                .process(translateFhir(requestTranslator))
                .to("pix-iti9:${host}:${port}?audit=false")
                .process(translateToFhir(responseTranslator, Message.class))
    }
}
