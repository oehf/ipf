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
package org.openehealth.ipf.platform.camel.ihe.fhir.translation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

import java.util.Map;

/**
 * Camel processors for translation of messages between FHIR and HL7v2
 *
 * @author Christian Ohr
 * @since 3.1
 */
public final class FhirCamelTranslators {

    private FhirCamelTranslators() {

    }

    /**
     * Returns a processor for translating FHIR messages to Hl7v2
     * using the given translator instance. 
     */
    public static Processor translatorFhirToHL7v2(final TranslatorFhirToHL7v2 translator) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                // ca.uhn.hl7v2.model.Message initial = exchange.getProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, ca.uhn.hl7v2.model.Message.class);
                Object fhir = exchange.getIn().getBody();
                Map<String, Object> parameters = exchange.getIn().getHeaders();
                // exchange.setProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, xmlText);
                org.apache.camel.Message resultMessage = Exchanges.resultMessage(exchange);
                resultMessage.getHeaders().putAll(exchange.getIn().getHeaders());
                resultMessage.setBody(translator.translateFhirToHL7v2(fhir, parameters));
            }
        };
    }
    
    
    /**
     * Returns a processor for translating HL7v2 messages to FHIR
     * using the given translator instance. 
     */
    public static Processor translatorHL7v2ToFhir(final TranslatorHL7v2ToFhir translator) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                // String initial = exchange.getProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, String.class);
                ca.uhn.hl7v2.model.Message msg = exchange.getIn().getMandatoryBody(ca.uhn.hl7v2.model.Message.class);
                Map<String, Object> parameters = exchange.getIn().getHeaders();
                // exchange.setProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, msg);
                org.apache.camel.Message resultMessage = Exchanges.resultMessage(exchange);
                resultMessage.getHeaders().putAll(exchange.getIn().getHeaders());
                resultMessage.setBody(translator.translateHL7v2ToFhir(msg, parameters));
            }
        };
    }
    
}
