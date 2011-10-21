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
package org.openehealth.ipf.platform.camel.core.process;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;

/**
 * A {@link Validator} which allows the usage of Camel validating  
 * processors from IPF IHE components in Groovy DSL.
 *
 * @deprecated because is used only from deprecated DSL model extensions.
 *
 * @author Dmytro Rud
 */
@Deprecated
public class ProcessorBasedExchangeValidator implements Validator<Exchange, Processor> {

    private static final Expression EXPRESSION = new Expression() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            return (T) exchange;
        }
    };
    
    @Override
    public void validate(Exchange exchange, Processor processor) {
        try {
            processor.process(exchange);
        } catch (Exception e) {
            // should actually not occur, because the validating processor
            // is supposed to throw ValidationException by itself
            throw new ValidationException("Validation failed", e);
        }
    }
    
    
    /**
     * Implementation of a generic usage pattern for this validator
     * for using in DSL extension definitions. 
     */
    public static ValidatorAdapterDefinition definition(
            ValidatorAdapterDefinition self, 
            Processor processor) 
    {
        self.setValidator(new ProcessorBasedExchangeValidator());
        self.staticProfile(processor); 
        return (ValidatorAdapterDefinition) self.input(EXPRESSION);
    }

}
