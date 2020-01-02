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

import org.apache.camel.Exchange;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-validation.xml" })
public class ValidationExtensionTest extends AbstractExtensionTest {

    @Test
    public void testResponderObjectSuccess() throws InterruptedException {
        testSuccess("direct:input1");
    }

    @Test
    public void testResponderObjectFault() throws InterruptedException {
        testFault("direct:input2");
    }
    
    @Test
    public void testResponderObjectError() throws InterruptedException {
        testError("direct:input3");
    }
    
    @Test
    public void testResponderEndpointSuccess() throws InterruptedException {
        testSuccess("direct:input4");
    }

    @Test
    public void testResponderEndpointFault() throws InterruptedException {
        testFault("direct:input5");
    }
    
    @Test
    public void Endpoint() throws InterruptedException {
        testError("direct:input6");
    }
    
    @Test
    public void testResponderClosureSuccess() throws InterruptedException {
        testSuccess("direct:input7");
    }

    @Test
    public void testResponderClosureFault() throws InterruptedException {
        testFault("direct:input8");
    }
    
    @Test
    public void testResponderClosureError() throws InterruptedException {
        testError("direct:input9");
    }
    
    public void testSuccess(String endpoint) throws InterruptedException {
        mockOutput.expectedBodiesReceived("blah");
        Exchange result = producerTemplate.request(endpoint, exchange -> exchange.getIn().setBody("blah"));
        assertEquals("result", result.getMessage().getBody());
        mockOutput.assertIsSatisfied();
    }

    public void testFault(String endpoint) throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange result = producerTemplate.request(endpoint, exchange -> exchange.getIn().setBody("blah"));
        assertEquals("failed", result.getMessage().getBody());
        mockOutput.assertIsSatisfied();
    }
    
    public void testError(String endpoint) throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        Exchange result = producerTemplate.request(endpoint, exchange -> exchange.getIn().setBody("blah"));
        assertEquals("failed", result.getException().getMessage());
        mockOutput.assertIsSatisfied();
    }

    
}
