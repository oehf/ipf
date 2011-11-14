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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;
import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * Validating processors for HL7v3-based IPF components.
 * @author Dmytro Rud
 */
abstract public class PixPdqV3CamelValidators {
    private static final CombinedXmlValidator VALIDATOR = new CombinedXmlValidator();

    private static final Processor ITI_44_REQUEST_VALIDATOR  = validatingProcessor(ITI_44_PIX, true);
    private static final Processor ITI_44_RESPONSE_VALIDATOR = validatingProcessor(ITI_44_PIX, false);
    private static final Processor ITI_45_REQUEST_VALIDATOR  = validatingProcessor(ITI_45, true);
    private static final Processor ITI_45_RESPONSE_VALIDATOR = validatingProcessor(ITI_45, false);
    private static final Processor ITI_46_REQUEST_VALIDATOR  = validatingProcessor(ITI_46, true);
    private static final Processor ITI_46_RESPONSE_VALIDATOR = validatingProcessor(ITI_46, false);
    private static final Processor ITI_47_REQUEST_VALIDATOR  = validatingProcessor(ITI_47, true);
    private static final Processor ITI_47_RESPONSE_VALIDATOR = validatingProcessor(ITI_47, false);

    private static final Processor ITI_55_REQUEST_VALIDATOR  = validatingProcessor(ITI_55, true);
    private static final Processor ITI_55_RESPONSE_VALIDATOR = validatingProcessor(ITI_55, false);
    private static final Processor ITI_56_REQUEST_VALIDATOR  = validatingProcessor(ITI_56, true);
    private static final Processor ITI_56_RESPONSE_VALIDATOR = validatingProcessor(ITI_56, false);

    private static final Processor PCC_1_REQUEST_VALIDATOR   = validatingProcessor(PCC_1, true);
    private static final Processor PCC_1_RESPONSE_VALIDATOR  = validatingProcessor(PCC_1, false);

    
    /**
     * Returns a validating processor for ITI-44 request messages
     * (Patient Identity Feed v3).
     */
    public static Processor iti44RequestValidator() {
        return ITI_44_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-44 response messages
     * (Patient Identity Feed v3).
     */
    public static Processor iti44ResponseValidator() {
        return ITI_44_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-45 request messages
     * (Patient Identity Query v3).
     */
    public static Processor iti45RequestValidator() {
        return ITI_45_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-45 response messages
     * (Patient Identity Query v3).
     */
    public static Processor iti45ResponseValidator() {
        return ITI_45_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-46 request messages
     * (Patient Identity Update Notification v3).
     */
    public static Processor iti46RequestValidator() {
        return ITI_46_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-46 response messages
     * (Patient Identity Update Notification v3).
     */
    public static Processor iti46ResponseValidator() {
        return ITI_46_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-47 request messages
     * (Patient Demographics Query v3).
     */
    public static Processor iti47RequestValidator() {
        return ITI_47_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for ITI-47 response messages
     * (Patient Demographics Query v3).
     */
    public static Processor iti47ResponseValidator() {
        return ITI_47_RESPONSE_VALIDATOR;
    }
    
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

    /**
     * Returns a validating processor for PCC-1 request messages
     * (Query for Existing Data).
     */
    public static Processor pcc1RequestValidator() {
        return PCC_1_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for PCC-1 response messages
     * (Query for Existing Data).
     */
    public static Processor pcc1ResponseValidator() {
        return PCC_1_RESPONSE_VALIDATOR;
    }


    private static Processor validatingProcessor(
            final InteractionId interactionId,
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
            InteractionId interactionId,
            boolean request)
    {
        if (! validationEnabled(exchange)) {
            return;
        }
        String message = exchange.getIn().getBody(String.class);
        VALIDATOR.validate(message, request
                ? Hl7v3ValidationProfiles.getRequestValidationProfile(interactionId)
                : Hl7v3ValidationProfiles.getResponseValidationProfile(interactionId));
    }
}
