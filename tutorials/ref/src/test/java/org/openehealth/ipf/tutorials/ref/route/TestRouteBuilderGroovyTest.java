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
package org.openehealth.ipf.tutorials.ref.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ValidationException;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/conf/context-test-groovy.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class TestRouteBuilderGroovyTest {

    @Autowired
    private CamelContext camelContext;
    
    @Autowired
    private ProducerTemplate producerTemplate;
    
    @EndpointInject(uri="mock:animals")
    private MockEndpoint mockAnimals;
    
    @EndpointInject(uri="mock:books")
    private MockEndpoint mockBooks;
    
    @EndpointInject(uri="mock:error-app")
    private MockEndpoint mockErrorApp;
    
    @EndpointInject(uri="mock:error-sys")
    private MockEndpoint mockErrorSys;
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        mockAnimals.reset();
        mockBooks.reset();
        mockErrorApp.reset();
        mockErrorSys.reset();
    }

    @Test
    public void testOrderAnimals() throws Exception {
        mockAnimals.expectedMessageCount(1);
        Exchange exchange1 = producerTemplate.send("direct:order", 
                createExchange(new ClassPathResource("order/order-animals.xml").getInputStream()));
        mockAnimals.assertIsSatisfied();
        assertNull(exchange1.getException());
        assertEquals("message valid", exchange1.getOut().getBody());
        Exchange exchange2 = mockAnimals.getExchanges().get(0);
        assertTrue(exchange2.getIn().getBody(String.class).contains("Customer:"));
    }
 
    @Test
    public void testOrderBooks() throws Exception {
        mockBooks.expectedMessageCount(1);
        Exchange exchange1 = producerTemplate.send("direct:order", createExchange(
                new ClassPathResource("order/order-books.xml").getInputStream()));
        mockBooks.assertIsSatisfied();
        assertNull(exchange1.getException());
        assertEquals("message valid", exchange1.getOut().getBody());
        Exchange exchange2 = mockBooks.getExchanges().get(0);
        assertTrue(exchange2.getIn().getBody(String.class).contains("category='books'"));
    }
 
    @Test
    public void testInvalidContent() throws Exception {
        mockErrorApp.expectedMessageCount(1);
        Exchange exchange = producerTemplate.send("direct:order", 
                createExchange(new ClassPathResource("order/order-invalid-content.xml").getInputStream()));
        assertTrue(((String)exchange.getOut().getBody()).startsWith("Validation failed"));
        assertTrue(exchange.getException() instanceof ValidationException);
        mockErrorApp.assertIsSatisfied();
    }
 
    @Test
    public void testInvalidFormat() throws Exception {
        mockErrorApp.expectedMessageCount(1);
        Exchange exchange = producerTemplate.send("direct:order", 
                createExchange(new ClassPathResource("order/order-invalid-format.xml").getInputStream()));
        assertTrue(exchange.getException() instanceof ValidationException);
        mockErrorApp.assertIsSatisfied();
    }
 
    private Exchange createExchange(Object body) {
        Exchange exchange = Exchanges.createExchange(camelContext, ExchangePattern.InOut);
        exchange.getIn().setBody(body);
        return exchange;
    }
    
}
