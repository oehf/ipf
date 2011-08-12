/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xcpd;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import org.openehealth.ipf.commons.ihe.hl7v3.IpfInteractionId;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;

import static org.openehealth.ipf.commons.ihe.hl7v3.IpfInteractionId.ITI_55;
import static org.openehealth.ipf.commons.ihe.hl7v3.IpfInteractionId.ITI_56;

/**
 * Validating processors for IPF XCPD components.
 * @author Dmytro Rud
 */
abstract public class XcpdCamelValidators {
    private static final CombinedXmlValidator VALIDATOR = new CombinedXmlValidator();

    private static final Processor ITI_55_REQUEST_VALIDATOR =  validatingProcessor(ITI_55, true);
    private static final Processor ITI_55_RESPONSE_VALIDATOR = validatingProcessor(ITI_55, false);
    private static final Processor ITI_56_REQUEST_VALIDATOR =  validatingProcessor(ITI_56, true);
    private static final Processor ITI_56_RESPONSE_VALIDATOR = validatingProcessor(ITI_56, false);


    /**
     * Returns a validating processor for ITI-55 request messages
     * (Cross-Gateway Patient Discovery).
     */
    public static Processor iti55RequestValidator() {
        return ITI_55_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-55 response messages
     * (Cross-Gateway Patient Discovery).
     */
    public static Processor iti55ResponseValidator() {
        return ITI_55_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-56 request messages
     * (Patient Location Query).
     */
    public static Processor iti56RequestValidator() {
        return ITI_56_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-56 response messages
     * (Patient Location Query).
     */
    public static Processor iti56ResponseValidator() {
        return ITI_56_RESPONSE_VALIDATOR;
    }
    
    
    private static Processor validatingProcessor(
            final IpfInteractionId interactionId,
            final boolean request)
    {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                doValidation(exchange, interactionId, request);
            }
        };
    }

    private static void doValidation(
            Exchange exchange,
            IpfInteractionId interactionId,
            boolean request)
    {
        String message = exchange.getIn().getBody(String.class);
        VALIDATOR.validate(message, request
                ? Hl7v3ValidationProfiles.getRequestValidationProfile(interactionId)
                : Hl7v3ValidationProfiles.getResponseValidationProfile(interactionId));
    }
}
