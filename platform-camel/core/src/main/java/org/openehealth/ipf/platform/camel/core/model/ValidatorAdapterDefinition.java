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

import groovy.lang.Closure;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.camel.Expression;
import org.apache.camel.spi.Metadata;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.stream.StreamSource;

import static org.apache.camel.builder.Builder.bodyAs;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
@Metadata(label = "ipf,eip,transformation")
@XmlRootElement(name = "verify")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidatorAdapterDefinition extends ProcessorAdapterDefinition {

    @XmlTransient
    private Validator<?, ?> validator;
    @XmlAttribute
    private String validatorBean;
    @XmlTransient
    private Object profile;
    @XmlTransient
    private Expression profileExpression;
    
    public ValidatorAdapterDefinition() {
        this(new AlwaysValid());
    }
    
    public ValidatorAdapterDefinition(Validator<?, ?> validator) {
        this.validator = validator;
    }
    
    public ValidatorAdapterDefinition(String validatorBean) {
        this.validatorBean = validatorBean;
    }
    
    public void setValidator(Validator<?, ?> validator) {
        this.validator = validator;
    }
    
    /**
     * Defines the static profile for the validation 
     * @param profile
     *          the profile to use
     */
    public ValidatorAdapterDefinition staticProfile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    @Deprecated
    public ValidatorAdapterDefinition profile(Object profile) {
        this.profile = profile;
        return this;
    }
    
    /**
     * Defines the profile for the validation via the given expression 
     * @param profileExpression
     *          the profile expression
     */
    public ValidatorAdapterDefinition profile(Expression profileExpression) {
        this.profileExpression = profileExpression;
        return this;
    }
    
    /**
     * Defines the profile for the validation via the given closure 
     * @param profileExpression
     *          the profile closure
     */
    public ProcessorAdapterDefinition profile(@ClosureParams(value = SimpleType.class, options = { "org.apache.camel.Expression"})
                                                      Closure<Object> profileExpression) {
        this.profileExpression = new DelegatingExpression(profileExpression);
        return this;
    }
    
    /**
     * Interprets the defined profile as W3C schema location and validates against it 
     */
    public ValidatorAdapterDefinition xsd() {
        validator = new XsdValidator();
        return (ValidatorAdapterDefinition)input(bodyAs(StreamSource.class));
    }
    
    /**
     * Interprets the defined profile as Schematron rules location and validates against it 
     */
    public ValidatorAdapterDefinition schematron() {
        validator = new SchematronValidator();
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

    public Validator<?, ?> getValidator() {
        return validator;
    }

    public String getValidatorBean() {
        return validatorBean;
    }

    public Object getProfile() {
        return profile;
    }

    public Expression getProfileExpression() {
        return profileExpression;
    }

    private static class AlwaysValid implements Validator<Object, Object> {

        @Override
        public void validate(Object message, Object profile) {
            // any input is valid
        }
        
    }
    
}
