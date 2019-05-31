/*
 * Copyright 2010 the original author or authors.
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

import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;

public class HL7v3ValidatorTest {

	@Test
	public void testValidateOk() throws Exception {
		String message = IOUtils.readStringFromStream(
                getClass().getResourceAsStream("/xsd/prpa-valid.xml"));
        CombinedXmlValidator validator = new CombinedXmlValidator();
        validator.validate(message, PIXV3.Interactions.ITI_44_PIX.getRequestValidationProfile());
	}
	
	@Test(expected = ValidationException.class)
	public void testValidateError() throws Exception {
		String message = IOUtils.readStringFromStream(
                getClass().getResourceAsStream("/xsd/prpa-invalid.xml"));
        CombinedXmlValidator validator = new CombinedXmlValidator();
        validator.validate(message, PIXV3.Interactions.ITI_44_PIX.getRequestValidationProfile());
	}

	/**
	 * With namespace prefix, correct value.
	 */
	@Test
	public void testQnameComparisonNsGood() throws Exception {
		String message = IOUtils.readStringFromStream(
				getClass().getResourceAsStream("/validation/type-ns-good.xml"));
		CombinedXmlValidator validator = new CombinedXmlValidator();
		validator.validate(message, XCPD.Interactions.ITI_55.getResponseValidationProfile());
	}

	/**
	 * Without namespace prefix, correct value.
	 */
	@Test
	public void testQnameComparisonNoNsGood() throws Exception {
		String message = IOUtils.readStringFromStream(
				getClass().getResourceAsStream("/validation/type-nons-good.xml"));
		CombinedXmlValidator validator = new CombinedXmlValidator();
		validator.validate(message, XCPD.Interactions.ITI_55.getResponseValidationProfile());
	}

	/**
	 * With namespace prefix, wrong value.
	 */
	@Test(expected = ValidationException.class)
	public void testQnameComparisonNsWrong() throws Exception {
		String message = IOUtils.readStringFromStream(
				getClass().getResourceAsStream("/validation/type-ns-wrong.xml"));
		CombinedXmlValidator validator = new CombinedXmlValidator();
		validator.validate(message, XCPD.Interactions.ITI_55.getResponseValidationProfile());
	}

	/**
	 * Without namespace prefix, wrong value.
	 */
	@Test(expected = ValidationException.class)
	public void testQnameComparisonNoNsWrong() throws Exception {
		String message = IOUtils.readStringFromStream(
				getClass().getResourceAsStream("/validation/type-nons-wrong.xml"));
		CombinedXmlValidator validator = new CombinedXmlValidator();
		validator.validate(message, XCPD.Interactions.ITI_55.getResponseValidationProfile());
	}
}
