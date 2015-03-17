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
package org.openehealth.ipf.platform.camel.flow.process;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "/context-flow-platform.xml",
        "/context-flow-processor.xml",
        "/context-flow-support.xml"
})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractFlowTest {

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @EndpointInject(uri="mock:mock")
    protected MockEndpoint mock;

    @After
    public void tearDown() throws Exception {
        mock.reset();
    }

    @Test
    public void testConvert() throws InterruptedException {
        mock.expectedBodiesReceived("test");
        producerTemplate.sendBody("direct:flow-test-1", "test");
        mock.assertIsSatisfied();
    }
    
    @Test
    public void testMarshal() throws InterruptedException {
        mock.expectedBodiesReceived(1.1);
        producerTemplate.sendBody("direct:flow-test-2", 1.1);
        mock.assertIsSatisfied();
    }
    
    @Test
    public void testSkip() throws InterruptedException {
        mock.expectedBodiesReceived(1.1);
        producerTemplate.sendBody("direct:flow-test-3", 1.1);
        mock.assertIsSatisfied();
    }
    
}
