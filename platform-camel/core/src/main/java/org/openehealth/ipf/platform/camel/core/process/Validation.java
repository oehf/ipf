/*
 * Copyright 2008 the original author or authors.
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
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.processor.DelegateProcessor;

/**
 * Implements a validation process to be used in combination with
 * {@link ProcessorType#intercept(DelegateProcessor)}. The incoming
 * {@link Exchange} is validated against a <code>validator</code> (a {#link
 * Processor}) set at construction time. If validation succeeds the validation
 * result is returned to the initiator and the in-message of the incoming
 * {@link Exchange} is forwarded to the next processor using an in-only
 * {@link Exchange}. If validation fails (validator returned fault or exception)
 * then the validation fault or exception is returned to the initiator and
 * processing stops.
 * 
 * @author Martin Krasser
 */
public class Validation extends Responder {

    /**
     * Creates a new {@link Validation} process.
     * 
     * @param validator
     *            processor that creates a validation response.
     */
    public Validation(Processor validator) {
        super(validator);
    }

    /**
     * Creates a new {@link Validation} process.
     * 
     * @param validator
     *            producer that creates a validation response.
     */
    public Validation(Producer validator) {
        super(validator);
    }

    /**
     * Returns <code>false</code> if the <code>response</code> exchange
     * failed indicating that processing shall stop.
     * 
     * @param original
     *            original message exchange.
     * @param response
     *            response message exchange.
     * @return <code>false</code> if <code>response</code> failed,
     *         <code>true</code> otherwise.
     */
    @Override
    protected boolean process(Exchange original, Exchange response) {
        return !response.isFailed();
    }
    
}
