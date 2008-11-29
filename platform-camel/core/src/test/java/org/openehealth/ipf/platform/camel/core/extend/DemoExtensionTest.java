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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-demo.xml" })
public class DemoExtensionTest extends AbstractExtensionTest {

    @Test
    public void testInput1Filtered() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input1", "blub");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput1Unfiltered() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        producerTemplate.sendBody("direct:input1", "blah");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput2Filtered() throws InterruptedException {
        mockOutput.expectedBodiesReceived("test");
        producerTemplate.sendBody("direct:input2", "blub");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput2Unfiltered() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input2", "blah");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput3Unfiltered() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        producerTemplate.sendBodyAndHeader("direct:input3", "blah", "foo", "test");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput3Filtered() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        producerTemplate.sendBodyAndHeader("direct:input3", "blub", "foo", "test");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput4Unfiltered() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        producerTemplate.sendBodyAndHeaders("direct:input4", "blah", headers());
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testInput4Filtered() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        producerTemplate.sendBodyAndHeaders("direct:input4", "blub", headers());
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testInput5() throws InterruptedException {
        mockOutput.expectedBodiesReceived("result");
        assertEquals("result", producerTemplate.requestBody("direct:input5", "blah"));
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testInput6Success() throws InterruptedException {
        assertEquals("blah", producerTemplate.requestBody("direct:input6", "blah"));
    }

    @Test
    public void testInput6Failure() throws InterruptedException {
        assertEquals("failure", producerTemplate.requestBody("direct:input6", "error"));
    }

    @Test
    public void testInput7Failure() throws InterruptedException {
        assertEquals("failure", producerTemplate.requestBody("direct:input7", "error"));
    }

    private static Map<String, Object> headers() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("foo", "te");
        result.put("bar", "st");
        return result;
    }
    
}
