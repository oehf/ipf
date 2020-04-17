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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

import static org.openehealth.ipf.modules.cda.CDAR2Constants.*;

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

	private static final String sample_c32 = "/MU_Rev3_HITSP_C32C83_4Sections_RobustEntries_NoErrors.xml";

	@Before
	public void setUp() throws Exception {
		validator = new XsdValidator();
		params = new HashMap<>();
		params.put("phase", "errors");
        schematron = new SchematronValidator();
	}

    @Test
    public void validateSchemaGoodSample24() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_c32));
        validator.validate(testXml, HITSP_32_2_4_SCHEMA);
    }

    @Test
    public void validateSchemaGoodSample25() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_c32));
        validator.validate(testXml, HITSP_32_2_5_SCHEMA);
    }

	@Test
	public void validateComplete24() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_c32));
		schematron.validate(testXml, new SchematronProfile(
				HITSP_32_2_4_SCHEMATRON_RULES, params));
	}

    @Test
    public void validateComplete25() throws Exception {
        Source testXml = new StreamSource(getClass().getResourceAsStream(sample_c32));
        schematron.validate(testXml, new SchematronProfile(
                HITSP_32_2_5_SCHEMATRON_RULES, params));
    }
}
