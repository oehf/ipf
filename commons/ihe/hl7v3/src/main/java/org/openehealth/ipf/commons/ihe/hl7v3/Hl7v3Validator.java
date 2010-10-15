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

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

/**
 * XSD- and Schematron-based validator for HL7 v3 messages.
 * @author Dmytro Rud
 */
public class Hl7v3Validator implements Validator<String, Collection<List<String>>> {

    private static final String HL7V3_SCHEMAS_PATH = "HL7V3/NE2008/multicacheschemas/";
    
    private static final XsdValidator XSD_VALIDATOR = new XsdValidator(Hl7v3Validator.class.getClassLoader());
    private static final SchematronValidator SCHEMATRON_VALIDATOR = new SchematronValidator();
    
    
    /**
     * Validates the given HL7 v3 message.
     * 
     * @param message
     *            a String which should contain an HL7 v3 XML document.
     * @param profiles
     *            a collection of string tuples which obey to the following format:
     *            <ol>
     *              <li> name of valid XML root element (with suffix),
     *              <li> either a name of Schematron profile for the corresponding 
     *                   message, or <tt>null</tt>, when only XML Schema-based 
     *                   validation should be performed,
     *              <li> (optional) name of XML schema file relative to 
     *                   "classpath:/schema/", when it does not correspond
     *                   to the root element name, or when the latter does
     *                   not belong to HL7 v3 (as e.g. for ITI-56).
     *                   Suffix ".xsd" should be omitted.
     *            </ol>
     * @throws ValidationException
     *             when the message is not valid.
     */
    @Override
    public void validate(String message, Collection<List<String>> profiles) throws ValidationException {
        Validate.notNull(message, "message");
        Validate.notEmpty(profiles, "list of profiles");

        // check whether the root element name is valid
        String rootElementName = SoapUtils.getRootElementLocalName(message);
        List<String> profile = null;
        for (List<String> list : profiles) {
            if (list.get(0).equals(rootElementName)) {
                profile = list;
                break;
            }
        }
        if (profile == null) {
            throw new ValidationException("Invalid root element '" + rootElementName + "'");
        }

        // delete suffix from the root element name
        int pos1 = rootElementName.indexOf('_');
        int pos2 = rootElementName.indexOf('_', pos1 + 1);
        rootElementName = (pos2 > 0) ? rootElementName.substring(0, pos2) : rootElementName;
        
        // XSD validation
        Source source = getSource(message);
        StringBuilder sb = new StringBuilder("schema/");
        if (profile.size() > 2) {
            sb.append(profile.get(2));
        } else {
            sb.append(HL7V3_SCHEMAS_PATH).append(rootElementName);
        }
        String xsdPath = sb.append(".xsd").toString();
        XSD_VALIDATOR.validate(source, xsdPath);

        // Schematron validation, if configured
        if (profile.get(1) != null) {
            source = getSource(message);
            String schematronPath = new StringBuilder("schematron/")
                .append(profile.get(1))
                .append(".sch.xml")
                .toString();
            SchematronProfile schematronProfile = new SchematronProfile(schematronPath);
            SCHEMATRON_VALIDATOR.validate(source, schematronProfile);
        }
    }

    
    /**
     * Converts the given XML string to a Source object.
     */
    private static Source getSource(String message) {
        return new StreamSource(new ByteArrayInputStream(message.getBytes()));
    }

}
