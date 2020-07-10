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

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.xml.XmlUtils.rootElementName;
import static org.openehealth.ipf.commons.xml.XmlUtils.source;

/**
 * XSD- and Schematron-based validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class CombinedXmlValidator implements Validator<String, CombinedXmlValidationProfile> {

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator(CombinedXmlValidator.class.getClassLoader());
    private static final SchematronValidator SCHEMATRON_VALIDATOR = new SchematronValidator();


    public void validate(String message, CombinedXmlValidationProfile profile) throws ValidationException {
        requireNonNull(profile, "validation profile must be not null");
        // check whether the root element name is valid
        var rootElementName = rootElementName(requireNonNull(message, "message must be not null"));
        if (! profile.isValidRootElement(rootElementName)) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        // XSD validation
        var xsdPath = profile.getXsdPath(rootElementName);
        if (xsdPath != null) {
            XSD_VALIDATOR.validate(source(message), xsdPath);
        }

        // Schematron validation
        var schematronPath = profile.getSchematronPath(rootElementName);
        if (schematronPath != null) {
            var schematronProfile = new SchematronProfile(
                    schematronPath,
                    profile.getCustomSchematronParameters(rootElementName));
            SCHEMATRON_VALIDATOR.validate(source(message), schematronProfile);
        }
    }
}
