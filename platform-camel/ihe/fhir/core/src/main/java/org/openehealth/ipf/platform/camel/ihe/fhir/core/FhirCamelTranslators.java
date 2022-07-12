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
package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.support.ExpressionAdapter;
import org.hl7.fhir.instance.model.api.IIdType;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator;
import org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator;

/**
 * Camel processors for translation of messages between FHIR and something else
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
    /**
     * Returns a processor for translating FHIR messages to XDS
     * using the given translator instance.
     */
    public static Processor translateFhir(final FhirTranslator<?> translator) {
        return exchange -> {
            var fhir = exchange.getIn().getBody();
            var parameters = exchange.getIn().getHeaders();
            var resultMessage = exchange.getMessage();
            resultMessage.setBody(translator.translateFhir(fhir, parameters));
            resultMessage.getHeaders().putAll(parameters);
            if (fhir instanceof IIdType) {
                resultMessage.setHeader(Constants.FHIR_REQUEST_GET_ONLY, true);
            }
        };
    }

    public static Expression transformFhir(final FhirTranslator<?> translator) {
        return new ExpressionAdapter() {
            @Override
            public Object evaluate(Exchange exchange) {
                var fhir = exchange.getIn().getBody();
                var parameters = exchange.getIn().getHeaders();
                return translator.translateFhir(fhir, parameters);
            }
        };
    }


    /**
     * Returns a processor for translating HL7v2 messages to FHIR
     * using the given translator instance.
     */
    public static <T> Processor translateToFhir(final ToFhirTranslator<T> translator, Class<T> clazz) {
        return exchange -> {
            var msg = exchange.getIn().getMandatoryBody(clazz);
            var parameters = exchange.getIn().getHeaders();
            var resultMessage = exchange.getMessage();
            resultMessage.setBody(translator.translateToFhir(msg, parameters));
            resultMessage.getHeaders().putAll(parameters);
        };
    }

}
