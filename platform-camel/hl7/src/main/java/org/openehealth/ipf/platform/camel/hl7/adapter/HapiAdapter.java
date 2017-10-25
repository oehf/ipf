/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.hl7.adapter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 *
 */
public abstract class HapiAdapter extends ProcessorAdapter {

    private static final Parser FALLBACK = new GenericParser();

    @Override
    protected void doProcess(Exchange exchange, Object inputData, Object... inputParams) throws Exception {
        Message message = toMessage(inputData, exchange);
        Message result = doProcessMessage(
                message,
                exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class),
                inputParams);
        Exchanges.prepareResult(exchange).setBody(result);
    }

    protected abstract Message doProcessMessage(Message message, Throwable t, Object... inputParams);

    private static Message toMessage(Object inputData, Exchange exchange) throws HL7Exception {
        Message message;
        if (inputData instanceof Message) {
            message = (Message)inputData;
        } else if (inputData instanceof String) {
            HapiContext context = exchange.getIn().getHeader("CamelHL7Context", HapiContext.class);
            Parser parser = context != null ? context.getGenericParser() : FALLBACK;
            message = parser.parse((String)inputData);
        } else {
            // try type conversion
            message = exchange.getIn().getBody(Message.class);
        }
        Validate.notNull(message, "Exchange does not contain or can be converted to the required 'ca.uhn.hl7v2.model.Message' type");
        return message;
    }
}
