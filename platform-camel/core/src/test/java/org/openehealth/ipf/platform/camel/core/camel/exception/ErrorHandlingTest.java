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
@ContextConfiguration(locations = {"/context-camel-error.xml"})
public class ErrorHandlingTest extends TestSupport {

    @EndpointInject(uri="mock:output")
    private MockEndpoint output;
    
    @EndpointInject(uri="mock:inter")
    private MockEndpoint inter;
    
    @EndpointInject(uri="mock:check")
    private MockEndpoint check;
    
    @EndpointInject(uri="mock:error")
    private MockEndpoint error;

    @After
    public void tearDown() {
        output.reset();
        inter.reset();
        check.reset();
        error.reset();
    }

    @Test
    public void testGlobal() throws Exception {
        output.expectedMessageCount(0);
        inter.expectedMessageCount(1);
        error.expectedMessageCount(0);
        try {
            producerTemplate.sendBody("direct:input-1", "blah");
            fail("failure not reported");
        } catch (RuntimeCamelException e) {
            assertEquals("message rejected", e.getCause().getMessage());
        }
        output.assertIsSatisfied();
        inter.assertIsSatisfied();
        error.assertIsSatisfied();
    }
 
    @Test
    public void testLocal() throws Exception {
        output.expectedMessageCount(0);
        inter.expectedMessageCount(1);
        check.expectedMessageCount(3);
        error.expectedMessageCount(1);
        try {
            producerTemplate.sendBody("direct:input-2", "blah");
            fail("failure not reported");
        } catch (RuntimeCamelException e) {
            assertEquals("message rejected", e.getCause().getMessage());
        }
        output.assertIsSatisfied();
        inter.assertIsSatisfied();
        check.assertIsSatisfied();
        error.assertIsSatisfied();
    }
 
}
