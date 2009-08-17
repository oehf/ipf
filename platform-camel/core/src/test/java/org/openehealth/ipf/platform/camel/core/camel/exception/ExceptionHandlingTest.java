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
package org.openehealth.ipf.platform.camel.core.camel.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.camel.EndpointInject;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.camel.TestSupport;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = {"/context-camel-exception.xml"})
public class ExceptionHandlingTest extends TestSupport {

    @EndpointInject(uri="mock:success")
    private MockEndpoint success;
    
    @EndpointInject(uri="mock:error1")
    private MockEndpoint error1;
    
    @EndpointInject(uri="mock:error2")
    private MockEndpoint error2;
    
    @EndpointInject(uri="mock:error3")
    private MockEndpoint error3;
    
    @Override
    @After
    public void tearDown() throws Exception {
        success.reset();
        error1.reset();
        error2.reset();
        error3.reset();
        super.tearDown();
    }

    @Test
    public void testError1() throws Exception {
        error1.expectedMessageCount(1);
        error2.expectedMessageCount(0);
        error3.expectedMessageCount(0);
        success.expectedMessageCount(0);
        try {
            producerTemplate.sendBody("direct:input", "blah");
            fail("failure not reported");
        } catch (RuntimeCamelException e) {
            assertEquals(Exception1.class, e.getCause().getClass());
        }
        error1.assertIsSatisfied();
        error2.assertIsSatisfied();
        error3.assertIsSatisfied();
        success.assertIsSatisfied();
    }
 
    @Test
    public void testError2() throws Exception {
        error1.expectedMessageCount(0);
        error2.expectedMessageCount(1);
        error3.expectedMessageCount(0);
        success.expectedMessageCount(0);
        try {
            producerTemplate.sendBody("direct:input", "blub");
        } catch (RuntimeCamelException e) {
            assertEquals(Exception2.class, e.getCause().getClass());
        }
        error1.assertIsSatisfied();
        error2.assertIsSatisfied();
        error3.assertIsSatisfied();
        success.assertIsSatisfied();
    }
 
    @Test
    public void testError3() throws Exception {
        error1.expectedMessageCount(0);
        error2.expectedMessageCount(0);
        error3.expectedMessageCount(1);
        success.expectedMessageCount(0);
        try {
            producerTemplate.sendBody("direct:input", "oink");
        } catch (RuntimeCamelException e) {
            assertEquals(Exception3.class, e.getCause().getClass());
        }
        error1.assertIsSatisfied();
        error2.assertIsSatisfied();
        error3.assertIsSatisfied();
        success.assertIsSatisfied();
    }
 
    @Test
    public void testSuccess() throws Exception {
        error1.expectedMessageCount(0);
        error2.expectedMessageCount(0);
        error3.expectedMessageCount(0);
        success.expectedMessageCount(1);
        producerTemplate.sendBody("direct:input", "clean");
        error1.assertIsSatisfied();
        error2.assertIsSatisfied();
        error3.assertIsSatisfied();
        success.assertIsSatisfied();
    }
 
}
