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

import org.apache.camel.ExchangePattern;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Martin Krasser
 */
public class ParserRouteTest extends AbstractRouteTest {

    @Test
    public void testParser1() throws InterruptedException {
        var result = (String) producerTemplate.sendBody("direct:parser-test",
                ExchangePattern.InOut, "input");
        assertEquals("string: input", result);
    }

    @Test
    public void testParser2() throws InterruptedException {
        var result = (String) producerTemplate.sendBody("direct:parser-test",
                ExchangePattern.InOut, new ByteArrayInputStream("input".getBytes()));
        assertEquals("stream: input", result);
    }

    @Test
    public void testParser3() throws InterruptedException {
        var result = (String) producerTemplate.sendBody("direct:parser-test",
                ExchangePattern.InOut, new StringReader("input"));
        assertEquals("reader: input", result);
    }

    @Test
    public void testParser4() throws InterruptedException {
        var result = (String) producerTemplate.sendBody("direct:parser-test",
                ExchangePattern.InOut, new StreamSource(new StringReader("input")));
        assertEquals("source: input", result);
    }

}
