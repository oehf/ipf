/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.hl7.expression;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * @author Martin Krasser
 */
public class Hl7InputExpression implements Expression {

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        Object body = exchange.getIn().getBody();
        return type.cast(body);
    }

}
