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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Christian Ohr
 */
public class SchematronValidatorTest {

    private SchematronValidator v;

    @Before
    public void setUp() throws Exception {
        v = new SchematronValidator();
    }

    @Test
    public void testValidate() throws IOException {
        Source testXml = new StreamSource(new ClassPathResource(
                "schematron/schematron-test.xml").getInputStream());
        v.validate(testXml, new SchematronProfile(
                "schematron/schematron-test-rules.xml"));
    }

    @Test
    public void testValidateFail() throws IOException {
        Source testXml = new StreamSource(new ClassPathResource(
                "schematron/schematron-test-fail.xml").getInputStream());
        try {
            v.validate(testXml, new SchematronProfile(
                    "schematron/schematron-test-rules.xml"));
        } catch (ValidationException e) {
            Throwable[] cause = e.getCauses();
            assertEquals(3, cause.length);
        }
    }

}
