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

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Martin Krasser
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
        "/context-core-extend.xml", 
        "/context-core-extend-support.xml" 
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractExtensionTest {

    @Autowired
    protected CamelContext camelContext;
    
    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @EndpointInject(value="mock:output")
    protected MockEndpoint mockOutput;
    
    @EndpointInject(value="mock:error")
    protected MockEndpoint mockError;
    
    @AfterEach
    public void tearDown() {
        mockOutput.reset();
        mockError.reset();
    }

}
