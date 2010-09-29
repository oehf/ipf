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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Validator;

/**
 * Validating processors for HL7v3-based IPF components.
 * @author Dmytro Rud
 */
abstract public class PixPdqV3CamelValidators {
    private static final Hl7v3Validator VALIDATOR = new Hl7v3Validator();

    private static final Processor ITI_44_REQUEST_VALIDATOR  = validatingProcessor("iti-44", true);
    private static final Processor ITI_44_RESPONSE_VALIDATOR = validatingProcessor("iti-44", false);
    private static final Processor ITI_45_REQUEST_VALIDATOR  = validatingProcessor("iti-45", true);
    private static final Processor ITI_45_RESPONSE_VALIDATOR = validatingProcessor("iti-45", false);
    private static final Processor ITI_46_REQUEST_VALIDATOR  = validatingProcessor("iti-46", true);
    private static final Processor ITI_46_RESPONSE_VALIDATOR = validatingProcessor("iti-46", false);
    private static final Processor ITI_47_REQUEST_VALIDATOR  = validatingProcessor("iti-47", true);
    private static final Processor ITI_47_RESPONSE_VALIDATOR = validatingProcessor("iti-47", false);

    private static final Processor PCC_1_REQUEST_VALIDATOR   = validatingProcessor("pcc-1", true);
    private static final Processor PCC_1_RESPONSE_VALIDATOR  = validatingProcessor("pcc-1", false);

    
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


    private static Processor validatingProcessor(final String transaction, final boolean request) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                doValidation(exchange, transaction, request);
            }
        };
    }

    private static void doValidation(Exchange exchange, String transaction, boolean request) {
        String message = exchange.getIn().getBody(String.class);
        Map<String, Collection<List<String>>> profilesCollection =
            request ? Hl7v3ValidationProfiles.getREQUEST_TYPES()  
                    : Hl7v3ValidationProfiles.getRESPONSE_TYPES();
        VALIDATOR.validate(message, profilesCollection.get(transaction));
    }
}
