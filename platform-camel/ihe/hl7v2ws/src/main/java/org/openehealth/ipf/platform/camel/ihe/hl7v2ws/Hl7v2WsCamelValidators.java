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

import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.gazelle.validation.profile.PcdTransactions;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.hl7.validation.ConformanceProfileValidators;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * @author Mitko Kolev
 *
 */
public class Hl7v2WsCamelValidators {

    // private static final Validator<Object, Object> CONTINUA_WAN_VALIDATOR = new ContinuaWanValidator();

    /**
     * Returns a validating processor for PCD-01 request messages
     * (Communicate Patient Care Device (PCD) data).
     *
     * @deprecated use {@link org.openehealth.ipf.commons.ihe.hl7v2.definitions.ConformanceProfileBasedValidationBuilder}
     * with {@link org.openehealth.ipf.gazelle.validation.profile.PcdTransactions#PCD1} as parameter for the validation
     * rule of the HAPIContext, or directly ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1)
     */
    public static Processor pcd01RequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for PCD-01 response messages
     * (Communicate Patient Care Device (PCD) data).
     *
     * @deprecated use {@link org.openehealth.ipf.commons.ihe.hl7v2.definitions.ConformanceProfileBasedValidationBuilder}
     * with {@link org.openehealth.ipf.gazelle.validation.profile.PcdTransactions#PCD1} as parameter for the validation
     * rule of the HAPIContext, or directly ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1)
     */
    public static Processor pcd01ResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for Continua WAN - conform request messages.
     */
    public static Processor continuaWanRequestValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }

    /**
     * Returns a validating processor for Continua WAN - conform response messages.
     */
    public static Processor continuaWanResponseValidator() {
        return ConformanceProfileValidators.validatingProcessor(PcdTransactions.PCD1);
    }
    
    private static Processor newValidatingProcessor(final Validator<Object,Object> validator , final Parser parser) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                doValidate(exchange, validator, parser);
            }
        };
    }
    
    private static void doValidate(Exchange exchange, Validator<Object, Object> validator, Parser parser) throws Exception {
        if (! validationEnabled(exchange)) {
            return;
        }
        MessageAdapter<?> msg = Hl7v2MarshalUtils.extractMessageAdapter(
                exchange.getIn(),
                exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                parser);
        validator.validate(msg, null);
    }
}
