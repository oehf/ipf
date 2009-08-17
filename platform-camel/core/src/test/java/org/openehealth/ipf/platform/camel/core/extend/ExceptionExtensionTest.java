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
package org.openehealth.ipf.platform.camel.core.extend;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-exception.xml" })
public class ExceptionExtensionTest extends AbstractExtensionTest {

    @EndpointInject(uri="mock:error1")
    protected MockEndpoint mockError1;
    
    @EndpointInject(uri="mock:error2")
    protected MockEndpoint mockError2;
    
    @Override
    @After
    public void tearDown() throws Exception {
        mockError1.reset();
        mockError2.reset();
        super.tearDown();
    }

    @Test
    public void testError1() throws InterruptedException {
        mockError1.expectedMessageCount(1);
        mockError2.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input", "abc");
        mockError1.assertIsSatisfied();
        mockError2.assertIsSatisfied();
    }
    
    @Test
    public void testError2() throws InterruptedException {
        mockError1.expectedMessageCount(0);
        mockError2.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input", "def");
        mockError1.assertIsSatisfied();
        mockError2.assertIsSatisfied();
    }
    
    @Test
    public void testError1Regex() throws InterruptedException {
        mockError1.expectedMessageCount(1);
        mockError2.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input", "uvw");
        mockError1.assertIsSatisfied();
        mockError2.assertIsSatisfied();
    }
    
    @Test
    public void testError2Regex() throws InterruptedException {
        mockError1.expectedMessageCount(0);
        mockError2.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input", "xyz");
        mockError1.assertIsSatisfied();
        mockError2.assertIsSatisfied();
    }
    
}
