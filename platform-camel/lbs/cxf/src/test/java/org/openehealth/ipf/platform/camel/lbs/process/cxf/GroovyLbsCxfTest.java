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
package org.openehealth.ipf.platform.camel.lbs.process.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.activation.DataHandler;
import javax.xml.ws.Holder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.spring.CxfEndpointBean;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.message.MessageContentsList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.platform.camel.test.junit.DirtySpringContextJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * This test uses {@link DirtySpringContextJUnit4ClassRunner} which is an 
 * alternative to {@link SpringJUnit4ClassRunner} that recreates the Spring
 * application context for the next test class. 
 * <p><b>
 * Do not simply copy this code. It could result it bad performance of tests.
 * Use the standard {@link SpringJUnit4ClassRunner} if you don't need this, 
 * which is usually the case.
 * </b><p>
 * This class requires that the Spring application context is recreated because
 * it creates HTTP endpoints. These endpoints will not be thrown away and use 
 * the tcp/ip ports and endpoint names. Subsequent quests could fail because
 * the ports are in use and exchanges might not be received by the correct 
 * endpoint.
 * <p> 
 * This class runs all tests in its base class with a specific configuration
 * 
 * @author Jens Riemschneider
 */
@RunWith(DirtySpringContextJUnit4ClassRunner.class) // DO NOT SIMPLY COPY!!! see above
@ContextConfiguration(locations = { "/context-lbs-route-cxf-groovy.xml" })
public class GroovyLbsCxfTest extends AbstractLbsCxfTest {
    
    @Autowired @Qualifier("soapEndpointExample1")
    private CxfEndpointBean endpointExample1;
    
    @Test
    public void testExample1() throws Exception {
        enableMTOM(); 
        setEndpoint(endpointExample1);
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                Holder<DataHandler> param = (Holder<DataHandler>) params.get(1);
                assertTrue(param.value.getDataSource() instanceof ResourceDataSource);
                DataHandler onewayParam = (DataHandler) params.get(2);
                assertTrue(onewayParam.getDataSource() instanceof ResourceDataSource);
                assertEquals("yes", exchange.getIn().getHeader("tokenfound"));
            }
        });        
        
        Holder<String> nameHolder = new Holder("Hello Camel!!");
        Holder<DataHandler> handlerHolder = new Holder(inputDataHandler);
        greeter.postMe(nameHolder, handlerHolder, onewayDataHandler);
        
        assertEquals("Greetings from Apache Camel!!!! Request was Hello Camel!!", nameHolder.value);
        assertEquals(FILE_CONTENT + FILE_CONTENT, toString(handlerHolder));
    }    

    @Test
    public void testExample2() throws Exception {
        final boolean called[] = new boolean[] { false };
        serviceBean.setCheckProcessor(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                called[0] = true;
                MessageContentsList params = exchange.getIn().getBody(MessageContentsList.class);
                Holder<DataHandler> param = (Holder<DataHandler>) params.get(1);
                assertTrue(param.value.getDataSource() instanceof ResourceDataSource);
                DataHandler onewayParam = (DataHandler) params.get(2);
                assertTrue(onewayParam.getDataSource() instanceof ResourceDataSource);
            }
        });        
        
        Exchange sendExchange = new DefaultExchange(camelContext);        
        producerTemplate.send("direct:start", sendExchange);
        
        assertTrue(called[0]);
    }        
}
