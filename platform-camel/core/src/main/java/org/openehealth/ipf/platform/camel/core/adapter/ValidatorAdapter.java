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
package org.openehealth.ipf.platform.camel.core.adapter;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.faultMessage;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.prepareResult;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

/**
 * Adapts a {@link Validator} 
 * 
 * @author Martin Krasser
 */
public class ValidatorAdapter extends ProcessorAdapter {

    private Validator validator;
    
    private Object profile;
    
    private Expression profileExpression;
    
    /**
     * Creates a new {@link ValidatorAdapter} and sets the delegate
     * {@link Validator}.
     * 
     * @param validator
     *            a validator.
     */
    public ValidatorAdapter(Validator validator) {
        this.validator = validator;
    }
    
    /**
     * Sets the profile to validate the input data against.
     * 
     * @param profile
     *            validation profile.
     * @return this object.
     */
    public ValidatorAdapter staticProfile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    /**
     * Sets the profile to validate the input data against.
     * 
     * @param profile
     *            validation profile.
     * @return this object.
     */
    @Deprecated
    public ValidatorAdapter profile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    /**
     * Sets the profile expression to validate the input data against.
     * Sets an {@link Expression} for obtaining profile data from an
     * {@link Exchange}. Profile data are passed to adapted
     * transform-support-library objects.
     * 
     * @param profile
     *            validation profile expression.
     * @return this object.
     */
    public ValidatorAdapter profile(Expression profileExpression) {
        this.profileExpression = profileExpression;
        return this;
    }
    
    /**
     * Delegates validation of input data against a profile to a
     * {@link Validator} object. Validation errors messages are written
     * to body of the message returned by {@link #faultMessage(Exchange)}.
     * 
     * @param exchange
     *            message exchange where to write processing results.
     * @param inputData
     *            input data.
     * @param inputParams
     *            input parameters.
     * @throws IOException
     *             if a general processing error occurs.
     */
    @Override
    protected void doProcess(Exchange exchange, Object inputData, 
            Object... inputParams) throws IOException {
        
        prepareResult(exchange);
        
        try {
            validator.validate(inputData, getProfile(exchange));
        } catch (ValidationException e) {
            Message fault = faultMessage(exchange);
            // TODO: revise exception handling 
            fault.setBody(e.getMessage());
        }
    }

    private Object getProfile(Exchange exchange) {
        if (profileExpression != null) {
            return profileExpression.evaluate(exchange);
        } else {
            return profile;
        }
    }
}
