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
package org.openehealth.ipf.commons.xml;

import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

import static org.openehealth.ipf.commons.xml.XmlUtils.*;

/**
 * XSD- and Schematron-based validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class CombinedXmlValidator implements Validator<Object, CombinedXmlValidationProfile> {

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator(CombinedXmlValidator.class.getClassLoader());
    private static final SchematronValidator SCHEMATRON_VALIDATOR = new SchematronValidator();


    @Override
    public void validate(Object message, CombinedXmlValidationProfile profile) throws ValidationException {
        validate(message, profile, null);
    }

    public void validate(Object message, CombinedXmlValidationProfile profile, String encoding) throws ValidationException {
        Validate.notNull(message, "message must be not null");
        Validate.notNull(profile, "validation profile must be not null");

        String sMessage = XmlUtils.toString(message, encoding);

        // check whether the root element name is valid
        String rootElementName = rootElementName(sMessage, encoding).getLocalPart();
        if (! profile.isValidRootElement(rootElementName)) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        // XSD validation
        String xsdPath = profile.getXsdPath(rootElementName);
        if (xsdPath != null) {
            XSD_VALIDATOR.validate(source(sMessage, encoding), xsdPath);
        }

        // Schematron validation
        String schematronPath = profile.getSchematronPath(rootElementName);
        if (schematronPath != null) {
            SchematronProfile schematronProfile = new SchematronProfile(
                    schematronPath,
                    profile.getCustomSchematronParameters());
            SCHEMATRON_VALIDATOR.validate(source(sMessage, encoding), schematronProfile);
        }
    }
}
