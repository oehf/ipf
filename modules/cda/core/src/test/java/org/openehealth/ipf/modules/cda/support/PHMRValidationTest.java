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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Validates the PHMR schematron rule set.
 *
 * @author Stefan Ivanov
 */
public class PHMRValidationTest {

    private XsdValidator validator;
    private SchematronValidator schematron;
    private Map<String, Object> params;

    private static final String sample_good = "/CDA_PHMR_GOOD.xml";
    private static final String sample_wrong = "/CDA_PHMR_WRONG.xml";

    @BeforeEach
    public void setUp() throws Exception {
        validator = new XsdValidator();
        schematron = new SchematronValidator();
        params = new HashMap<>();
        params.put("phase", "errors");
    }

    @Test
    public void validateSchemaGoodSample() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        validator.validate(testXml, CDAR2Constants.CDAR2_SCHEMA);
    }

    @Test
    public void validateSchemaWrongSample() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        validator.validate(testXml, CDAR2Constants.CDAR2_SCHEMA);
    }

    /* validate header template */
    @Test
    public void validateHeaderPositive() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch", params));
    }

    @Test
    public void validateHeaderErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(11, ex.getCauses().length);
        }
    }

    @Disabled
    public void validateHeaderWarnings() {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        params.put("phase", "warnings");
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(4, ex.getCauses().length);
        }
    }

    @Test
    public void validateHeaderAll() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch"));
            fail();
        } catch (ValidationException ex) {
            assertEquals(17, ex.getCauses().length);
        }
    }

    @Test
    public void validateBodyPositive() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch", params));
    }

    @Test
    public void validateBodyErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(11, ex.getCauses().length);
        }
    }

    @Test
    public void validateProductInstancePositive() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.9.sch", params));
    }

    @Test
    public void validateDeviceDefinitionOrganizerPositive() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.4.sch", params));
    }

    @Test
    public void validateProductInstanceErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.9.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(2, ex.getCauses().length);
        }
    }

    @Test
    public void validateNumericObservationPositive() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.8.sch", params));
    }

    @Test
    public void validateNumericObservationErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.8.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(1, ex.getCauses().length);
        }
    }

    @Test
    public void validateMedicalEquipmentErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        schematron.validate(testXml, new SchematronProfile(
                "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.1.sch"));
    }

    @Test
    public void validateVitalSignsErrors() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.2.sch"));
            fail();
        } catch (ValidationException ex) {
            assertEquals(2, ex.getCauses().length);
        }
    }

    @Test
    public void validateResultsWarnings() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "/schematron/cda_phmr/templates/2.16.840.1.113883.10.20.9.14.sch"));
            fail();
        } catch (ValidationException ex) {
            assertEquals(4, ex.getCauses().length);
        }
    }

    @Test
    public void validateCompleteGood() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        schematron.validate(testXml, new SchematronProfile(
                CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES, params));
    }

    @Test
    public void validateCompleteGoodWithWarnings() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_good));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES));
        } catch (ValidationException ex) {
            assertEquals(11, ex.getCauses().length);
        }
    }

    @Test
    public void validateCompleteWrong() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES, params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(17, ex.getCauses().length);
        }
    }

    @Test
    public void validateAllCompleteWrong() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_wrong));
        try {
            schematron.validate(testXml, new SchematronProfile(
                    CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES));
            fail();
        } catch (ValidationException ex) {
            assertEquals(27, ex.getCauses().length);
        }
    }

}
