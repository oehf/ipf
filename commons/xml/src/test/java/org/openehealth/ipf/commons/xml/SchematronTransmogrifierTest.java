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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class SchematronTransmogrifierTest {

    private SchematronTransmogrifier<String> svi;

    @Before
    public void setUp() throws Exception {
        svi = new SchematronTransmogrifier<>(String.class);
    }

    @Test
    public void testConvert() throws IOException {
        Source testXml = new StreamSource(new ClassPathResource(
                "schematron/schematron-test.xml").getInputStream());
        String result = svi
                .zap(testXml, "schematron/schematron-test-rules.xml");
        assertFalse(result.contains("svrl:failed-assert"));
    }

    @Test
    public void testConvertFail() throws IOException {
        Source testXml = new StreamSource(new ClassPathResource(
                "schematron/schematron-test-fail.xml").getInputStream());
        String result = svi
                .zap(testXml, "schematron/schematron-test-rules.xml");
        assertTrue(result.contains("<svrl:failed-assert")); // 3 occurrences
    }
}
