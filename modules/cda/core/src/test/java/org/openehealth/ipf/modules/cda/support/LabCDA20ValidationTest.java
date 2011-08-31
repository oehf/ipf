package org.openehealth.ipf.modules.cda.support;


import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class LabCDA20ValidationTest {
    
    private XsdValidator validator;
    private SchematronValidator schematron;
    private Map<String, Object> params;
    
    // as of C37 should be also Lab 2.0 conform
    private String sample2 = "HITSP_C37_With_CBC_GTT_GS_Sensitivity.xml";

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
        Source testXml = new StreamSource(new ClassPathResource(sample2).getInputStream());
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
    }
    
    @Test
    public void testValidateErrors() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample2).getInputStream());
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_20_SCHEMATRON_RULES, params));
    }

}
