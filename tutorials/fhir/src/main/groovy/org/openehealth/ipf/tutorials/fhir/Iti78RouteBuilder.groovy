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

import org.apache.camel.LoggingLevel
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
class Iti78RouteBuilder extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("pdqm-iti78:translation?audit=true")
                .routeId("pdqm-adapter")
                .errorHandler(noErrorHandler())
                .log(LoggingLevel.ERROR, "Not implemented yet");
    }
}
