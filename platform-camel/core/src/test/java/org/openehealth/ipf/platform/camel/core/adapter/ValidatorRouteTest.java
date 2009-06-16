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
package org.openehealth.ipf.platform.camel.core.adapter;

import static junit.framework.Assert.assertEquals;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.platform.camel.core.AbstractRouteTest;


/**
 * @author Martin Krasser
 */
public class ValidatorRouteTest extends AbstractRouteTest {

    @EndpointInject(uri="mock:error")
    protected MockEndpoint error;
    
    @After
    public void tearDown() throws Exception {
        error.reset();
        super.tearDown();
    }
    
    @Test
    public void testValidator1() throws InterruptedException {
        String result = (String) producerTemplate.sendBody("direct:validator-test",
                ExchangePattern.InOut, "correct");
        assertEquals("correct", result);
    }

    @Test
    public void testValidator2() throws InterruptedException {
        Exchange exchange = producerTemplate.send("direct:validator-test",
                ExchangePattern.InOut, new Processor() {
                    public void process(Exchange exchange) {
                        exchange.getIn().setBody("incorrect");
                    }
        });
        assertEquals(ValidationException.class, exchange.getException().getClass());
    }

}
