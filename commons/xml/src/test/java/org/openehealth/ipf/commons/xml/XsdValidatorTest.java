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
package org.openehealth.ipf.commons.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.lang.reflect.Field;
import java.util.Map;

public class XsdValidatorTest {

	private XsdValidator validator;
	private Map<String, ?> cache;
	private static final String SCHEMA_RESOURCE = "/xsd/test.xsd";

	@Before
	public void setUp() throws Exception {
		validator = new XsdValidator();

		Field field = XsdValidator.class.getDeclaredField("XSD_CACHE");
		field.setAccessible(true);
		cache = (Map<String, ?>) field.get(null);
	}

	@Test
	public void testValidate() throws Exception {
		cache.clear();
		Source testXml = new StreamSource(getClass().getResourceAsStream("/xsd/test.xml"));
		validator.validate(testXml, SCHEMA_RESOURCE);
		Assert.assertTrue(cache.containsKey(SCHEMA_RESOURCE));
	}

	@Test(expected = ValidationException.class)
	public void testValidateFails() throws Exception {
		boolean schemaExisted = cache.containsKey(SCHEMA_RESOURCE);
		int cacheSize = cache.size();
		Source testXml = new StreamSource(getClass().getResourceAsStream(
				"/xsd/invalidtest.xml"));
		validator.validate(testXml, SCHEMA_RESOURCE);
		if (schemaExisted) {
			Assert.assertEquals(cacheSize, cache.size());
		} else {
			Assert.assertEquals(cacheSize + 1, cache.size());
		}
	}

}
