/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.modules.cda.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.modules.cda.constants.CDAR2Constants;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Validates the LabCDA schematron rule set.
 * 
 * @author Stefan Ivanov
 * 
 */
public class LabCDAValidationTest {
    
    private XsdValidator validator;
    private SchematronValidator schematron;
    private Map<String, Object> params;
    
    private String sample = "/IHE_LabReport_20080103.xml";
    private String sample_extended = "/IHE_LabReport_21_Extended.xml";
    private String sample2 = "/IHE_LabReport_20080103_Errored.xml";

    @BeforeEach
    public void setUp() throws Exception {
        validator = new XsdValidator();
        schematron = new SchematronValidator();
        params = new HashMap<>();
        params.put("phase", "errors");
    }
    
    @Test
    public void testSchemaValidate() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample));
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
    }
    
    @Test
    public void testSchemaValidateExtended() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_extended));
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
    }
    
    @Test
    public void testValidateErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample));
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
            params));
    }
    
    @Test
    public void testValidateErrorsExtended() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_extended));
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
            params));
    }    
    
    @Test
    public void testValidateOnlyErrors() {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample2));
        Assertions.assertThrows(ValidationException.class,
                () -> schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
                        params)));
    }

    @Test
    public void testValidateWarnings() {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample));
        Assertions.assertThrows(ValidationException.class,
                () -> schematron
                        .validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES)));
    }


}
