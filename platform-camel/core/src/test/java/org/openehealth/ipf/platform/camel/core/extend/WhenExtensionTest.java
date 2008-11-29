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

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-when.xml" })
public class WhenExtensionTest extends AbstractExtensionTest {
    
    @EndpointInject(uri="mock:output1")
    protected MockEndpoint mockOutput1;
    
    @EndpointInject(uri="mock:output2")
    protected MockEndpoint mockOutput2;
    
    @After
    public void tearDown() throws Exception {
        mockOutput1.reset();
        mockOutput2.reset();
    }

    @Test
    public void testWhen1() throws InterruptedException {
        mockOutput1.expectedMessageCount(1);
        mockOutput2.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input1", "a");
        mockOutput1.assertIsSatisfied();
        mockOutput2.assertIsSatisfied();
    }
    
    @Test
    public void testWhen2() throws InterruptedException {
        mockOutput1.expectedMessageCount(0);
        mockOutput2.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input1", "b");
        mockOutput1.assertIsSatisfied();
        mockOutput2.assertIsSatisfied();
    }
    
}
