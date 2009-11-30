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
import org.apache.camel.ExchangePattern;
import org.apache.camel.processor.DelegateProcessor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-interceptor.xml" })
public class InterceptorExtensionTest extends AbstractExtensionTest {

    @Test
    public void testInterceptProceed() throws InterruptedException {
        mockOutput.expectedBodiesReceived("abcd");
        producerTemplate.sendBody("direct:input1", "blah");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testInterceptStop() throws InterruptedException {
        mockOutput.expectedMessageCount(0);
        producerTemplate.sendBody("direct:input1", "blub");
        mockOutput.assertIsSatisfied();
    }

    @Test
    public void testInterceptWithBeanName() throws InterruptedException {
        mockOutput.expectedMessageCount(1);
        mockOutput.expectedBodiesReceived("before");
        Object result = producerTemplate.sendBody("direct:input2", ExchangePattern.InOut, "foo");
        mockOutput.assertIsSatisfied();
        assertEquals("after", result);
    }

    public static class TestInterceptor extends DelegateProcessor {
        public TestInterceptor() {
            System.out.println("hallo");
        }

        @Override
        protected void processNext(Exchange exchange) throws Exception {
            exchange.getIn().setBody("before");
            super.processNext(exchange);
            exchange.getOut().setBody("after");
        }
    }
}
