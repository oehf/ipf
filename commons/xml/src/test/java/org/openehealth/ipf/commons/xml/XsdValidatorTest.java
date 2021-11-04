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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.Map;

public class XsdValidatorTest {

	private XsdValidator validator;
	private Map<String, ?> cache;
	private static final String SCHEMA_RESOURCE = "/xsd/test.xsd";

	@BeforeEach
	public void setUp() throws Exception {
		validator = new XsdValidator();

		var field = XsdValidator.class.getDeclaredField("XSD_CACHE");
		field.setAccessible(true);
		cache = (Map<String, ?>) field.get(null);
	}

	@Test
	public void testValidate() throws Exception {
		cache.clear();
		Source testXml = new StreamSource(getClass().getResourceAsStream("/xsd/test.xml"));
		validator.validate(testXml, SCHEMA_RESOURCE);
		Assertions.assertTrue(cache.containsKey(SCHEMA_RESOURCE));
	}

	@Test
	public void testValidateFails() throws Exception {
		var schemaExisted = cache.containsKey(SCHEMA_RESOURCE);
		var cacheSize = cache.size();
		Source testXml = new StreamSource(getClass().getResourceAsStream(
				"/xsd/invalidtest.xml"));
		Assertions.assertThrows(ValidationException.class, () -> {
			validator.validate(testXml, SCHEMA_RESOURCE);
		});

		if (schemaExisted) {
			Assertions.assertEquals(cacheSize, cache.size());
		} else {
			Assertions.assertEquals(cacheSize + 1, cache.size());
		}
	}

}
