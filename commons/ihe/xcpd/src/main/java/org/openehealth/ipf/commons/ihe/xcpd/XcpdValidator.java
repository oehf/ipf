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
package org.openehealth.ipf.commons.ihe.xcpd;

import java.util.Collection;

import javax.xml.transform.Source;

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Validator;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;

/**
 * Validator for XCPD transactions, which combines XSD and Schematron validations.
 * @author Dmytro Rud
 */
public class XcpdValidator extends Hl7v3Validator {

    private static final SchematronValidator SCHEMATRON_VALIDATOR = new SchematronValidator();
    
    public static SchematronValidator getSchematronValidator() {
        return SCHEMATRON_VALIDATOR;
    }

    
    /**
     * Returns path to the Schematron patterns which correspond to the  
     * root HL7 v3 element with the given name. 
     */
    protected static String getSchematronPathForElement(String rootElementName) {
        return new StringBuilder("schematron/iti55/")
            .append(rootElementName)
            .append(".sch.xml")
            .toString();
    }
    

    @Override
    public void validate(String message, Collection<String> validRootElementNames) throws ValidationException {
        Validate.notNull(message, "message");
        Validate.notEmpty(validRootElementNames, "list of valid root element names");

        String rootElementName = getRootElementName(message, validRootElementNames);
        Source source;
        
        // XSD validation
        source = getSource(message);
        String xsdPath = getXsdPathForElement(rootElementName);
        getXsdValidator().validate(source, xsdPath);
        
        // Schematron validation
        source = getSource(message);
        String schPath = getSchematronPathForElement(rootElementName);
        SchematronProfile schematronProfile = new SchematronProfile(schPath);
        getSchematronValidator().validate(source, schematronProfile);
    }
    
}
