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
package org.openehealth.ipf.platform.camel.ihe.mllp;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.hl7v2.MessageAdapterValidator;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti10.Iti10Component;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti21.Iti21Component;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti22.Iti22Component;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti64.Iti64Component;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti8.Iti8Component;
import org.openehealth.ipf.platform.camel.ihe.mllp.iti9.Iti9Component;

import ca.uhn.hl7v2.parser.Parser;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * Validating processors for MLLP-based IPF IHE components.
 * @author Dmytro Rud
 */
abstract public class PixPdqCamelValidators {
    private static final MessageAdapterValidator VALIDATOR = new MessageAdapterValidator(); 

    private static final Parser ITI_8_PARSER  = Iti8Component.CONFIGURATION.getParser();
    private static final Parser ITI_9_PARSER  = Iti9Component.CONFIGURATION.getParser();
    private static final Parser ITI_10_PARSER = Iti10Component.CONFIGURATION.getParser();
    private static final Parser ITI_21_PARSER = Iti21Component.CONFIGURATION.getParser();
    private static final Parser ITI_22_PARSER = Iti22Component.CONFIGURATION.getParser();
    private static final Parser ITI_64_PARSER = Iti64Component.CONFIGURATION.getParser();
    
    private static final Processor ITI_8_VALIDATOR  = validatingProcessor(ITI_8_PARSER);
    private static final Processor ITI_9_VALIDATOR  = validatingProcessor(ITI_9_PARSER);
    private static final Processor ITI_10_VALIDATOR = validatingProcessor(ITI_10_PARSER);
    private static final Processor ITI_21_VALIDATOR = validatingProcessor(ITI_21_PARSER);
    private static final Processor ITI_22_VALIDATOR = validatingProcessor(ITI_22_PARSER);
    private static final Processor ITI_64_VALIDATOR = validatingProcessor(ITI_64_PARSER);

    /**
     * Returns a validating processor for ITI-8 request messages
     * (Patient Identity Feed).
     */
    public static Processor iti8RequestValidator() {
        return ITI_8_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-8 response messages
     * (Patient Identity Feed).
     */
    public static Processor iti8ResponseValidator() {
        return ITI_8_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-9 request messages.
     * (Patient Identity Query).
     */
    public static Processor iti9RequestValidator() {
        return ITI_9_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-9 response messages 
     * (Patient Identity Query).
     */
    public static Processor iti9ResponseValidator() {
        return ITI_9_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-10 request messages 
     * (PIX Update Notification).
     */
    public static Processor iti10RequestValidator() {
        return ITI_10_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-10 response messages 
     * (PIX Update Notification).
     */
    public static Processor iti10ResponseValidator() {
        return ITI_10_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-21 request messages 
     * (Patient Demographics Query).
     */
    public static Processor iti21RequestValidator() {
        return ITI_21_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-21 response messages 
     * (Patient Demographics Query).
     */
    public static Processor iti21ResponseValidator() {
        return ITI_21_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-22 request messages 
     * (Patient Demographics and Visit Query).
     */
    public static Processor iti22RequestValidator() {
        return ITI_22_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-22 response messages 
     * (Patient Demographics and Visit Query).
     */
    public static Processor iti22ResponseValidator() {
        return ITI_22_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-64 request messages
     * (XAD-PID Change Management).
     */
    public static Processor iti64RequestValidator() {
        return ITI_64_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-64 response messages
     * (XAD-PID Change Management).
     */
    public static Processor iti64ResponseValidator() {
        return ITI_64_VALIDATOR;
    }
    
    
    private static Processor validatingProcessor(final Parser parser) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                doValidate(exchange, parser);
            }
        };
    }
    
    private static void doValidate(Exchange exchange, Parser parser) throws Exception {
        if (! validationEnabled(exchange)) {
            return;
        }
        MessageAdapter<?> msg = Hl7v2MarshalUtils.extractMessageAdapter(
                exchange.getIn(),
                exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                parser);  
        VALIDATOR.validate(msg, null);
    }
    
}
