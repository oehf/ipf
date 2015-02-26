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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-misc.xml" })
public class MiscExtensionTest extends AbstractExtensionTest {
    
    @Test
    public void testSetBodyClosure() throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        producerTemplate.sendBodyAndHeader("direct:input1", null, "foo", "blah");
        mockOutput.assertIsSatisfied();
    }
    
    @Test
    public void testSetPropertyClosure() throws InterruptedException {
        Exchange result = producerTemplate.request("direct:input2", new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("blah");
            }
        });
        assertEquals("blah", result.getProperty("test"));
    }
    
    @Test
    public void testTransformClosure() throws InterruptedException {
        Object result = producerTemplate.requestBody("direct:input3", "blah");
        assertEquals("blahblah", result);
    }
    
}
