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
package org.openehealth.ipf.commons.ihe.hl7v3;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.xml.XsdValidator;

/**
 * XSD-based validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class Hl7v3Validator implements Validator<String, Collection<String>> {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:\\s*<\\!--.*?-->)*"                             +  // optional comments before prolog (are they allowed?)
        "(?:\\s*<\\?xml.+?\\?>(?:\\s*<\\!--.*?-->)*)?"      +  // optional prolog and comments after it 
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))"    // open tag of the root element
    );

    private static final String XSD_PATH = "schema/HL7V3/NE2008/multicacheschemas/";
    
    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();

    
    public static XsdValidator getXsdValidator() {
        return XSD_VALIDATOR;
    }
    
    
    /**
     * Returns path to the XML Schema document which contains the definition 
     * of the root HL7 v3 element with the given name. 
     */
    public static String getXsdPathForElement(String rootElementName) {
        return new StringBuilder(XSD_PATH)
            .append(rootElementName)
            .append(".xsd")
            .toString();
    }

    
    /**
     * Checks whether the given String seems to represent an XML document 
     * and whether its root element is a valid one.
     * 
     * @param message
     *            a String which should contain an HL7 v3 XML document.
     * @param validRootElementNames
     *            list of valid root element names (with suffixes).
     * @return name of the extracted root element without suffix 
     *         (i.e. <code>QUQI_IN000003UV01</code> instead of
     *         <code>QUQI_IN000003UV01_Cancel</code>).
     * @throws ValidationException
     *             when the given String does not contain XML payload 
     *             or the root element is not valid.
     */
    public static String getRootElementName(String message, Collection<String> validRootElementNames) {
        Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(message);
        if(( ! matcher.find()) || (matcher.start() != 0)) {
            throw new ValidationException("Cannot extract root element, probably bad XML");
        }

        String rootElementName = matcher.group(1);
        if( ! validRootElementNames.contains(rootElementName)) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        int pos1 = rootElementName.indexOf('_');
        int pos2 = rootElementName.indexOf('_', pos1 + 1);
        return (pos2 > 0) ? rootElementName.substring(0, pos2) : rootElementName;
    }


    /**
     * Converts the given XML string to a Source object.
     */
    public static Source getSource(String message) {
        return new StreamSource(new ByteArrayInputStream(message.getBytes()));
    }
    

    /**
     * Validates the given HL7 v3 message.
     */
    @Override
    public void validate(String message, Collection<String> validRootElementNames) throws ValidationException {
        Validate.notNull(message, "message");
        Validate.notEmpty(validRootElementNames, "list of valid root element names");

        String rootElementName = getRootElementName(message, validRootElementNames);
        Source source = getSource(message);
        String xsdPath = getXsdPathForElement(rootElementName);
        getXsdValidator().validate(source, xsdPath);
    }

}
