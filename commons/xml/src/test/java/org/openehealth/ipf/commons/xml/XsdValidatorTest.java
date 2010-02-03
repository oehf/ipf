package org.openehealth.ipf.commons.xml;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.springframework.core.io.ClassPathResource;

public class XsdValidatorTest {

	private XsdValidator validator;
	private static final String SCHEMA_RESOURCE = "/xsd/test.xsd";

	@Before
	public void setUp() throws Exception {
		validator = new XsdValidator();
	}

	@Test
	public void testValidate() throws Exception {
		XsdValidator.getCachedSchemas().clear();
		Source testXml = new StreamSource(new ClassPathResource("xsd/test.xml")
				.getInputStream());
		validator.validate(testXml, SCHEMA_RESOURCE);
		Assert.assertTrue(XsdValidator.getCachedSchemas().containsKey(
				SCHEMA_RESOURCE));
	}

	@Test(expected = ValidationException.class)
	public void testValidateFails() throws Exception {
		boolean schemaExisted = XsdValidator.getCachedSchemas().containsKey(
				SCHEMA_RESOURCE);
		int cacheSize = XsdValidator.getCachedSchemas().size();
		Source testXml = new StreamSource(new ClassPathResource(
				"xsd/invalidtest.xml").getInputStream());
		validator.validate(testXml, SCHEMA_RESOURCE);
		if (schemaExisted) {
			Assert.assertEquals(cacheSize, XsdValidator.getCachedSchemas()
					.size());
		} else {
			Assert.assertEquals(cacheSize + 1, XsdValidator.getCachedSchemas()
					.size());
		}
	}

}
