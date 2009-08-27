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
package org.openehealth.ipf.platform.camel.core.model;

import static org.apache.camel.builder.Builder.bodyAs;
import groovy.lang.Closure;

import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Expression;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
public class ValidatorAdapterDefinition extends ProcessorAdapterDefinition {

    private Validator validator;

    private String validatorBean;
    
    private Object profile;

    private Expression profileExpression;
    
    public ValidatorAdapterDefinition() {
        this(new AlwaysValid());
    }
    
    public ValidatorAdapterDefinition(Validator validator) {
        this.validator = validator;
    }
    
    public ValidatorAdapterDefinition(String validatorBean) {
        this.validatorBean = validatorBean;
    }
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
    public ValidatorAdapterDefinition staticProfile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    @Deprecated
    public ValidatorAdapterDefinition profile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    public ValidatorAdapterDefinition profile(Expression profileExpression) {
        this.profileExpression = profileExpression;
        return this;
    }
    
    public ProcessorAdapterDefinition profile(Closure profileExpression) {
        this.profileExpression = new DelegatingExpression(profileExpression);
        return this;
    }
    
    public ValidatorAdapterDefinition xsd() {
        this.validator = new XsdValidator();
        return (ValidatorAdapterDefinition)input(bodyAs(StreamSource.class));
    }
    
    public ValidatorAdapterDefinition schematron() {
        this.validator = new SchematronValidator();
        return (ValidatorAdapterDefinition)input(bodyAs(StreamSource.class));
    }
    
    @Override
    public String toString() {
        return "ValidatorAdapter[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "validatorAdapter";
    }

    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
        if (validatorBean != null) {
            validator = routeContext.lookup(validatorBean, Validator.class);
        }
        ValidatorAdapter adapter = new ValidatorAdapter(validator);
        if (profile != null) {
            return adapter.staticProfile(profile);
        }
        if (profileExpression != null) {
            return adapter.profile(profileExpression);
        }
        return adapter;
    }

    private static class AlwaysValid implements Validator<Object, Object> {

        @Override
        public void validate(Object message, Object profile) {
            // any input is valid
        }
        
    }
    
}
