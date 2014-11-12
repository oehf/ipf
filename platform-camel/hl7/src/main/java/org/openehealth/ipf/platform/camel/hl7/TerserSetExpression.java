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
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;

/**
 *
 */
public class TerserSetExpression implements Expression {

    private final String spec;
    private final Expression value;

    public TerserSetExpression(String spec, Expression value) {
        super();
        this.spec = spec;
        this.value = value;
    }

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        try {
            Message msg = HL7v2.bodyMessage(exchange);
            new Terser(msg).set(spec, value.evaluate(exchange, String.class));
            return (T)msg;
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }


    public String getDescription() {
        // Terser Expression is equivalent with Location
        return "";
    }

}
