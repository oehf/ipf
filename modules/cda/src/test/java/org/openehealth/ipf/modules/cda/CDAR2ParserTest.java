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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument;

/**
 * @author Christian Ohr
 */
public class CDAR2ParserTest {

    private CDAR2Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = new CDAR2Parser();
    }



    @Test
    public void testParseSampleDocument() throws IOException {
        InputStream is = getClass().getResourceAsStream(
                "/builders/content/document/SampleCDADocument.xml");
        POCDMT000040ClinicalDocument clinicalDocument = parser.parse(is);
        Assert.assertNotNull(clinicalDocument);
    }
}
