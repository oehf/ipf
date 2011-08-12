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

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSD- and Schematron-based validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class CombinedXmlValidator implements Validator<String, CombinedXmlValidationProfile> {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:\\s*<\\!--.*?-->)*"                             +  // optional comments before prolog (are they allowed?)
        "(?:\\s*<\\?xml.+?\\?>(?:\\s*<\\!--.*?-->)*)?"      +  // optional prolog and comments after it
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))",   // open tag of the root element
        Pattern.DOTALL
    );

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator(CombinedXmlValidator.class.getClassLoader());
    private static final SchematronValidator SCHEMATRON_VALIDATOR = new SchematronValidator();


    @Override
    public void validate(String message, CombinedXmlValidationProfile profile) throws ValidationException {
        Validate.notNull(message, "message");
        Validate.notNull(profile, "validation profile");

        // check whether the root element name is valid
        String rootElementName = getRootElementLocalName(message);
        if (! profile.isValidRootElement(rootElementName)) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        // XSD validation
        String xsdPath = profile.getXsdPath(rootElementName);
        if (xsdPath != null) {
            XSD_VALIDATOR.validate(getSource(message), xsdPath);
        }

        // Schematron validation
        String schematronPath = profile.getSchematronPath(rootElementName);
        if (schematronPath != null) {
            SchematronProfile schematronProfile = new SchematronProfile(
                    schematronPath,
                    profile.getCustomSchematronParameters());
            SCHEMATRON_VALIDATOR.validate(getSource(message), schematronProfile);
        }
    }


    /**
     * Converts the given XML string to a Source object.
     */
    private static Source getSource(String message) {
        return new StreamSource(new ByteArrayInputStream(message.getBytes()));
    }


    /**
     * Returns local name of the root element of the XML document represented
     * by the given string, or <code>null</code>, when the given string does
     * not contain valid XML.
     */
    private static String getRootElementLocalName(String xml) {
        if (xml == null) {
            return null;
        }
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(xml);
        return (matcher.find() && (matcher.start() == 0)) ? matcher.group(1) : null;
    }
}
