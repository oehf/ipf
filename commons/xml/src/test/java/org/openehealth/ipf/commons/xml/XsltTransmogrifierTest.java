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

import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * @author Christian Ohr
 */
public class XsltTransmogrifierTest {

    private XsltTransmogrifier<String> transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new XsltTransmogrifier<>(String.class);
    }

    @Test
    public void testConvertString() throws IOException {
        Source source = new StreamSource(getClass().getResourceAsStream("/xslt/createPatient.xml"));
        var result = transformer.zap(source, "/xslt/createPatient.xslt");
        assertNotNull(result);
    }

}
