/*
 * Copyright 2011 the original author or authors.
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

import net.sf.saxon.lib.FeatureKeys;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

public class XqjTransmogrifierTest {

    private XqjTransmogrifier<String> transformer;


    @Before
    public void setUp() {
        transformer = new XqjTransmogrifier<>(String.class);
    }

    @Test
    public void zapSimple() throws IOException {
        var zapResult = transformer.zap(source("/xquery/string.xml"), "/xquery/string-q1.xq");
        check(zapResult, "/xquery/string-q1.xml");
    }

    @Test
    public void zapStringParameter() throws IOException {
        Map<String, Object> dynamicParams = new HashMap<>();
        dynamicParams.put("language", "English");
        var params = new Object[]{"/xquery/string-q2.xq", dynamicParams};
        var zapResult1 = transformer.zap(source("/xquery/string.xml"), params);
        check(zapResult1, "/xquery/string-q2.xml");
        dynamicParams.put("language", "German");
        var zapResult2 = transformer.zap(source("/xquery/string.xml"), params);
        check(zapResult2, "/xquery/string-q2g.xml");
    }

    @Test(expected = RuntimeException.class)
    public void zapMissingParameter() {
        transformer.zap(source("/xquery/string.xml"), "/xquery/string-q2.xq");
    }

    @Test
    public void zapLocalFunction() throws IOException {
        Map<String, Object> dynamicEvalParams = new HashMap<>();
        dynamicEvalParams.put("language", "Bulgarian");
        dynamicEvalParams.put("author_name", "John");
        var params = new Object[]{"/xquery/string-q3.xq", dynamicEvalParams};
        var zapResult = transformer.zap(source("/xquery/string.xml"), params);
        check(zapResult, "/xquery/string-q3.xml");
    }

    @Test
    public void zapMainFunction() throws IOException {
        var zapResult = transformer.zap(source("/xquery/string.xml"), "/xquery/string-q4.xq");
        check(zapResult, "/xquery/string.xml");
    }

    @Test
    public void zapClasspathResolver() throws IOException {
        var zapResult = transformer.zap(source("/xquery/string.xml"), "/xquery/string-q5.xq");
        check(zapResult, "/xquery/string.xml");
    }

    @Test
    public void zapParametrisedConstructor() throws IOException {
        Map<String, Object> configParams = new HashMap<>();
        configParams.put(FeatureKeys.PRE_EVALUATE_DOC_FUNCTION, Boolean.TRUE);
        var localTransformer = new XqjTransmogrifier<>(String.class, configParams);
        var zapResult = localTransformer.zap(source("/xquery/string.xml"), "/xquery/string-q5.xq");
        check(zapResult, "/xquery/string.xml");
    }

    @Test
    public void zapParametrisedConstructorNoParams() throws IOException {
        var localTransformer = new XqjTransmogrifier<>(String.class);
        var zapResult = localTransformer.zap(source("/xquery/string.xml"), "/xquery/string-q5.xq");
        check(zapResult, "/xquery/string.xml");
    }

    @Test
    public void zapWithNamespaces() throws IOException {
        var zapResult = transformer.zap(source("/xquery/ns.xml"), "/xquery/ns-q1.xq");
        check(zapResult, "/xquery/ns-q1.xml");
    }

    @Test
    public void zapRCase() throws IOException {
        var zapResult = transformer.zap(new StreamSource(new StringReader("<empty/>")), "/xquery/r-q1.xq");
        check(zapResult, "/xquery/r-q1.xml");
    }

    private Source source(String path) {
        return new StreamSource(getClass().getResourceAsStream(path));
    }

    private String result(String path) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(path), Charset.defaultCharset());
    }

    private void check(String zapResult, String s) throws IOException {
        var diff = DiffBuilder.compare(Input.fromString(result(s)))
                .withTest(zapResult)
                .ignoreComments()
                .ignoreWhitespace()
                .checkForSimilar()
                .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }
}
