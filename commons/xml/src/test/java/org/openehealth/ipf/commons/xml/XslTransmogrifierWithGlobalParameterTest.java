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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests passing in global parameters (e.g. services) into a stylesheet. See
 * parameter.xslt.
 *
 * Note that with Saxon-HE, this feature is not available anymore
 * 
 * @author Christian Ohr
 * 
 */
@Disabled
public class XslTransmogrifierWithGlobalParameterTest {

    private XsltTransmogrifier<String> transformer;
    Map<String, Object> parameters;

    @BeforeEach
    public void setUp() {
        parameters = new HashMap<>();
        parameters.put("service", new XsltTestService());
        transformer = new XsltTransmogrifier<>(String.class);
    }

    /**
     * Demonstrates how to include a Groovy class to be used in an XSLT script
     *
     */
    @Test
    public void testConvertString() {
        Source content = new StreamSource(getClass().getResourceAsStream("/xslt/parameterExample.xml"));
        var s = transformer.zap(content, "/xslt/parameter.xslt", parameters);
        assertTrue(s.contains("ein negeR mi tgaz ellezaG tim regeN niE"));
    }

}
