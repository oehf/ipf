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
package org.openehealth.ipf.platform.camel.core.extend;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
@ContextConfiguration(locations = { "/context-core-extend-gxml.xml" })
public class GxmlExtensionTest extends AbstractExtensionTest {

    @Test
    public void testUnmarshalGnode() throws InterruptedException {
        mockOutput.expectedBodiesReceived("abc");
        producerTemplate.sendBody("direct:input1", "<input><details>abc</details></input>");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testUnmarshalGpath() throws InterruptedException {
        mockOutput.expectedBodiesReceived("def");
        producerTemplate.sendBody("direct:input2", "<input><details>def</details></input>");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testUnmarshalGnodeWithSchema() throws InterruptedException, IOException {
        String xml = IOUtils.toString(new ClassPathResource("test.xml").getInputStream());
        mockOutput.expectedBodiesReceived("blub");
        producerTemplate.sendBody("direct:input4", xml);
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testMarshalGnode() throws InterruptedException {
        String result = (String)producerTemplate.requestBody("direct:input3", "<input><details>ghi</details></input>");
        assertTrue(result.contains("<input>"));
        assertTrue(result.contains("<details>"));
        assertTrue(result.contains("ghi"));
    }
    
    @Test
    public void testUnmarshalGnodeWithNonmatchingSchema() throws InterruptedException, IOException {
        String xml = IOUtils.toString(new ClassPathResource("invalidtest.xml").getInputStream());
        mockOutput.expectedMessageCount(0);
        mockError.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input4", xml);
        mockOutput.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }
    
    @Test
    public void testUnmarshalGpathWithSchema() throws InterruptedException, IOException {
        String xml = IOUtils.toString(new ClassPathResource("test.xml").getInputStream());
        mockOutput.expectedBodiesReceived("blub");
        producerTemplate.sendBody("direct:input5", xml);
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testUnmarshalGpathWithNonmatchingSchema() throws InterruptedException, IOException {
        String xml = IOUtils.toString(new ClassPathResource("invalidtest.xml").getInputStream());
        mockOutput.expectedMessageCount(0);
        mockError.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input5", xml);
        mockOutput.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }    

    
}
