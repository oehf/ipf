/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01Validator;
import org.openehealth.ipf.commons.ihe.hl7v2ws.wan.ContinuaWanValidator;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01.Pcd01Component;

import ca.uhn.hl7v2.parser.Parser;

/**
 * @author Mitko Kolev
 *
 */
public class Hl7v2WsCamelValidators1 {
    
    private static final Validator<Object, Object> PCD01_VALIDATOR = new Pcd01Validator();
    private static final Validator<Object, Object> CONTINUA_WAN_VALIDATOR = new ContinuaWanValidator();

    /**
     * Returns a validating processor for PCD-01 request messages
     * (Communicate Patient Care Device (PCD) data).
     */
    public static Processor pcd01RequestValidator() {
        return newValidatingProcessor(PCD01_VALIDATOR, Pcd01Component.HL7V2_CONFIG.getParser());
    }

    /**
     * Returns a validating processor for PCD-01 response messages
     * (Communicate Patient Care Device (PCD) data).
     */
    public static Processor pcd01ResponseValidator() {
        return newValidatingProcessor(PCD01_VALIDATOR, Pcd01Component.HL7V2_CONFIG.getParser());
    }

    /**
     * Returns a validating processor for Continua WAN - conform request messages.
     */
    public static Processor continuaWanRequestValidator() {
        return newValidatingProcessor(CONTINUA_WAN_VALIDATOR, Pcd01Component.HL7V2_CONFIG.getParser());
    }

    /**
     * Returns a validating processor for Continua WAN - conform response messages.
     */
    public static Processor continuaWanResponseValidator() {
        return newValidatingProcessor(CONTINUA_WAN_VALIDATOR, Pcd01Component.HL7V2_CONFIG.getParser());
    }
    
    private static Processor newValidatingProcessor(final Validator<Object,Object> validator , final Parser parser) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                doValidate(exchange, validator, parser);
            }
        };
    }
    
    private static void doValidate(Exchange exchange, Validator<Object,Object> validator, Parser parser) throws Exception {
        MessageAdapter msg = Hl7v2MarshalUtils.extractMessageAdapter(
                exchange.getIn(),
                exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                parser);  
        validator.validate(msg, null);
    }
}
