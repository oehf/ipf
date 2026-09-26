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

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.fhir.iti104.PixFeedResponseToPixmFeedResponseTranslator;
import org.openehealth.ipf.commons.ihe.fhir.iti104.PixmFeedRequestToPixFeedTranslator;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;
import org.openehealth.ipf.platform.camel.ihe.fhir.test.FhirTestContainer;

import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateFhir;
import static org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators.translateToFhir;
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.itiValidator;

/**
 * Acts as Patient Identifier Cross-reference Manager that proxies ITI-104 to a PIX Feed (ITI-8). The PIX
 * Manager behind it is simulated: it acknowledges every message, except for a patient identifier
 * {@link #REJECTED_PATIENT}, which it reports as unknown.
 */
public class Iti104PixFeedTestRouteBuilder extends RouteBuilder {

    public static final String REJECTED_PATIENT = "rejected";

    private final PixmFeedRequestToPixFeedTranslator requestTranslator;
    private final PixFeedResponseToPixmFeedResponseTranslator responseTranslator;

    public Iti104PixFeedTestRouteBuilder(UriMapper uriMapper) {
        this.requestTranslator = new PixmFeedRequestToPixFeedTranslator(uriMapper);
        this.responseTranslator = new PixFeedResponseToPixmFeedResponseTranslator();
    }

    @Override
    public void configure() {

        from("direct:input")
            .toF("pixm-iti104:localhost:%d", FhirTestContainer.DEMO_APP_PORT);

        from("pixm-iti104:translation?audit=true")
            .errorHandler(noErrorHandler())
            // Translate into ITI-8
            .process(translateFhir(requestTranslator))
            .process(itiValidator())
            // Simulate the PIX Manager
            .process(exchange -> {
                var request = exchange.getIn().getBody(Message.class);
                var patientId = new Terser(request).get("/.PID-3-1");
                Message ack;
                if (REJECTED_PATIENT.equals(patientId)) {
                    var exception = new HL7Exception("Unknown key identifier", ErrorCode.UNKNOWN_KEY_IDENTIFIER);
                    exception.setLocation(new Location().withSegmentName("PID").withSegmentRepetition(1).withField(3));
                    ack = request.generateACK(AcknowledgmentCode.AE, exception);
                } else {
                    ack = request.generateACK();
                }
                exchange.getMessage().setBody(ack);
            })
            .process(itiValidator())
            // Translate back into FHIR
            .process(translateToFhir(responseTranslator, Message.class));
    }
}
