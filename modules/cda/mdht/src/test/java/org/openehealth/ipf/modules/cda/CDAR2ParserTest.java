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
package org.openehealth.ipf.modules.cda;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

/**
 * @author Christian Ohr
 */
public class CDAR2ParserTest {

    private CDAR2Parser parser;
    private CDAR2Renderer renderer;

    @Before
    public void setUp() throws Exception {
        parser = new CDAR2Parser();
        renderer = new CDAR2Renderer();
    }

    @Test
    public void testParseCDADocument() throws Exception {
        InputStream is = getClass().getResourceAsStream(
                "/builders/content/document/SampleCDADocument.xml");
        ClinicalDocument clinicalDocument = parser.parse(is);
        Assert.assertTrue(clinicalDocument instanceof ClinicalDocument);
    }
    
    @Test
    public void testParseCCDDocument() throws Exception {
        InputStream is = getClass().getResourceAsStream(
                "/builders/content/document/SampleCCDDocument.xml");
        ClinicalDocument clinicalDocument = parser.parse(is);
        Assert.assertTrue(clinicalDocument instanceof ClinicalDocument);
        String result = renderer.render(clinicalDocument, (Object[]) null);
        Assert.assertTrue(result.length() > 0);
    }
    
}
