package org.openehealth.ipf.modules.cda.support;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.springframework.core.io.ClassPathResource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openehealth.ipf.modules.cda.CDAR2Constants.HITSP_24_SCHEMATRON_RULES;

/**
 * Validates the HITSP C37 schematron rule set.
 * 
 * @author Stefan Ivanov
 * 
 */
public class HITSPC32ValidationTest {

	private XsdValidator validator;
	private SchematronValidator schematron;
	private Map<String, Object> params;

	private String sample_c32 = "MU_Rev3_HITSP_C32C83_4Sections_RobustEntries_NoErrors.xml";

	@Before
	public void setUp() throws Exception {
		validator = new XsdValidator();
		params = new HashMap<String, Object>();
		params.put("phase", "errors");
        schematron = new SchematronValidator();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void validateComplete() throws Exception {
		Source testXml = new StreamSource(
				new ClassPathResource(sample_c32).getInputStream());
		schematron.validate(testXml, new SchematronProfile(
				HITSP_24_SCHEMATRON_RULES, params));
	}


}
