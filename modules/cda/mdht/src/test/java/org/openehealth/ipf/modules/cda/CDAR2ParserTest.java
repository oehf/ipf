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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ParseException;

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
        var is = getClass().getResourceAsStream(
                "/builders/content/document/SampleCDADocument.xml");
        var clinicalDocument = parser.parse(is);
        // TODO test document content
        var result = renderer.render(clinicalDocument, (Object[]) null);
        Assert.assertTrue(result.length() > 0);
    }
    
    @Test
    public void testParseCCDDocument() throws Exception {
        CDAR2Utils.initCCD();
        var is = getClass().getResourceAsStream(
                "/builders/content/document/SampleCCDDocument.xml");
        var clinicalDocument = parser.parse(is);
        var result = renderer.render(clinicalDocument, (Object[]) null);
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void testParseHITSPDocument() throws Exception {
        CDAR2Utils.initHITSPC32();
        var is = getClass().getResourceAsStream(
                "/builders/content/document/SampleHITSPC32v25Document.xml");
        var clinicalDocument = parser.parse(is);
        var result = renderer.render(clinicalDocument, (Object[]) null);
        Assert.assertTrue(result.length() > 0);
    }

    @Test
    public void testParseDocumentWithXInclude() throws Exception {
        var is = getClass().getResourceAsStream(
                "/builders/content/document/CDADocumentWithXInclude.xml");
        var clinicalDocument = parser.parse(is);
        Assert.assertEquals("", clinicalDocument.getTitle().getText());
    }

    @Test(expected = ParseException.class)
    public void testParseDocumentWithXXEInjection() throws Exception {
        var is = getClass().getResourceAsStream(
                "/builders/content/document/CDADocumentWithXXEInjection.xml");
        var clinicalDocument = parser.parse(is);
    }
    
}
