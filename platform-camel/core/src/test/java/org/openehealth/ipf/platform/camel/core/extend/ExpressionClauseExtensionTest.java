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
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-expressionclause.xml" })
public class ExpressionClauseExtensionTest extends AbstractExtensionTest {

    @Test
    public void testExceptionObject() throws InterruptedException {
        mockOutput.expectedMessageCount(1);
        var result = producerTemplate.request("direct:input1", exchange -> {
            exchange.getIn().setBody("blah");
        });
        mockOutput.assertIsSatisfied();
        var received = mockOutput.getExchanges().get(0);
        var exception = (Exception)received.getIn().getHeader("foo");
        assertEquals("message rejected", result.getException().getMessage());
        assertEquals("message rejected", exception.getMessage());
        assertNull(received.getException());
    }
    
    @Test
    public void testExceptionMessage() throws InterruptedException {
        mockOutput.expectedMessageCount(1);
        var result = producerTemplate.request("direct:input2", exchange -> exchange.getIn().setBody("blah"));
        mockOutput.assertIsSatisfied();
        var received = mockOutput.getExchanges().get(0);
        assertEquals("message rejected", result.getException().getMessage());
        assertEquals("message rejected", result.getMessage().getBody());
        assertNull(received.getException());
    }
    
}
