/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.adapter;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.apache.camel.ExchangePattern;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;


/**
 * @author Martin Krasser
 */
public class ConverterRouteTest extends AbstractRouteTest {

    @Test
    public void testConverter1() throws InterruptedException {
        String result = (String) producerTemplate.sendBody("direct:converter-test",
                ExchangePattern.InOut, "input");
        assertEquals("string: input", result);
    }

    @Test
    public void testConverter2() throws Exception {
        InputStream result = (InputStream) producerTemplate.sendBody("direct:converter-test",
                ExchangePattern.InOut, new ByteArrayInputStream("input".getBytes()));
        assertEquals("stream: input", IOUtils.toString(result));
    }

    @Test
    public void testConverter3() throws Exception {
        Reader result = (Reader) producerTemplate.sendBody("direct:converter-test",
                ExchangePattern.InOut, new StringReader("input"));
        assertEquals("reader: input", IOUtils.toString(result));
    }

    @Test
    public void testConverter4() throws Exception {
        StreamSource result = (StreamSource) producerTemplate.sendBody("direct:converter-test",
                ExchangePattern.InOut, new StreamSource(new StringReader("input")));
        assertEquals("source: input", IOUtils.toString(result.getReader()));
    }

}
