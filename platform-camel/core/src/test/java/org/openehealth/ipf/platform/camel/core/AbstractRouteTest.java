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
package org.openehealth.ipf.platform.camel.core;

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-core.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public abstract class AbstractRouteTest {

    @Autowired
    protected ProducerTemplate producerTemplate;
    
    @EndpointInject(value="mock:mock")
    protected MockEndpoint mock;

    @After
    public void tearDown() {
        mock.reset();
    }

    protected List<String> sendBodies(String endpointUri, ExchangePattern pattern, String body, int repeats) {
        var result = new ArrayList<String>(repeats);
        for (var i = 0; i < repeats; i++) {
            result.add((String)producerTemplate.sendBody(endpointUri, pattern, body));
        }
        return result;
    }
    
}
