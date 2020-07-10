/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.config;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/context-custom-configurer.xml",
        "/context-custom-routes.xml" })
public class RouteBuilderConfigurerTest {

    @Autowired
    ProducerTemplate producer;
    
    @EndpointInject(value = "mock:output")
    MockEndpoint mockOut;
    
    @Test
    public void testConfigurer() throws Exception{
        mockOut.setExpectedMessageCount(5);
        producer.sendBody("direct:input", "MLAH");
        producer.sendBody("direct:input1", "BLAH");
        producer.sendBody("direct:input2", "WLAH");
        mockOut.assertIsSatisfied();
    }
}
