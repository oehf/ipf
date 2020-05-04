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

package org.openehealth.ipf.platform.camel.hl7;

import ca.uhn.hl7v2.HL7Exception;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;

/**
 * Expression that returns a predefined response parsed with the current message's {@link ca.uhn.hl7v2.HapiContext}
 */
class StaticResponseExpression implements Expression {

    private final String message;

    public StaticResponseExpression(String message) {
        this.message = message;
    }

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        try {
            var msg = HL7v2.bodyMessage(exchange);
            var result = msg.getParser().parse(message);
            return type.cast(result);
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }

    }
}
