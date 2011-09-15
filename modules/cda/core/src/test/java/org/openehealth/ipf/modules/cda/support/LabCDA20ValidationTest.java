package org.openehealth.ipf.modules.cda.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
 * Validates the LabCDA 2.0 schematron rule set.
 * 
 * @author Stefan Ivanov
 * 
 */
public class LabCDA20ValidationTest {

    private XsdValidator validator;
    private SchematronValidator schematron;
    private Map<String, Object> params;

    private String sample = "IHE_LabReport_20070816.xml";
    private String sample_errored = "IHE_LabReport_20070816_Errored.xml";

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
    public void testValidateErrors() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        schematron.validate(testXml, new SchematronProfile(CDAR2Constants.IHE_LAB_20_SCHEMATRON_RULES, params));
    }

    @Test
    public void validateBodyPositive() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
        testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        schematron.validate(testXml, new SchematronProfile(
                "schematron/ihe_lab_v20_20070816/templates/1.3.6.1.4.1.19376.1.3.1.sch", params));
    }

    @Test
    public void validateBodyErrors() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample_errored).getInputStream());
        try {
            schematron.validate(testXml, new SchematronProfile(
                    "schematron/ihe_lab_v20_20070816/templates/1.3.6.1.4.1.19376.1.3.1.sch", params));
            fail();
        } catch (ValidationException ex) {
            assertEquals(9, ex.getCauses().length);
        }
    }

    @Test
    public void validateHeaderPositive() throws Exception {
        Source testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        validator.validate(testXml, CDAR2Constants.IHE_LAB_SCHEMA);
        testXml = new StreamSource(new ClassPathResource(sample).getInputStream());
        schematron.validate(testXml, new SchematronProfile(
                "schematron/ihe_lab_v20_20070816/templates/1.3.6.1.4.1.19376.1.3.3.sch", params));
    }

}
