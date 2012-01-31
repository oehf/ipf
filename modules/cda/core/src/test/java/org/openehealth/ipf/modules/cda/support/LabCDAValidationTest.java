package org.openehealth.ipf.modules.cda.support;


import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.modules.cda.CDAR2Constants;
import org.springframework.core.io.ClassPathResource;

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
    
    private String sample = "IHE_LabReport_20080103.xml";
    private String sample_extended = "IHE_LabReport_21_Extended.xml";
    private String sample2 = "IHE_LabReport_20080103_Errored.xml";

    @Before
    public void setUp() throws Exception {
        validator = new XsdValidator();
        schematron = new SchematronValidator();
        params = new HashMap<String, Object>();
        params.put("phase", "errors");
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testSchemaValidate() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
    }
    
    @Test
    public void testSchemaValidateExtended() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample_extended).getInputStream());
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
    }
    
    @Test
    public void testValidateErrors() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
            params));
    }
    
    @Test
    public void testValidateErrorsExtended() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample_extended).getInputStream());
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
            params));
    }    
    
    @Test(expected = ValidationException.class)
    public void testValidateOnlyErrors() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample2).getInputStream());
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES,
            params));
    }

    @Test(expected = ValidationException.class)
    public void testValidateWarnings() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        schematron
            .validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_SCHEMATRON_RULES));
    }


}
