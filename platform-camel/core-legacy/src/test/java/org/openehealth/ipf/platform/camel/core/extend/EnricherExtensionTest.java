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

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-core-extend-enricher.xml" })
public class EnricherExtensionTest extends AbstractExtensionTest {

    @Test
    public void testStrategyObjectAggregatorObject() throws InterruptedException {
        testAggregation("direct:input1", "blah:halb");
    }
    
    @Test
    public void testStrategyObjectAggregatorBean() throws InterruptedException {
        testAggregation("direct:input2", "blah:halb");
    }
    
    @Test
    public void testStrategyObjectAggregatorClosure() throws InterruptedException {
        testAggregation("direct:input3", "blah:halb");
    }
    
    @Test
    public void testStrategyObjectAggregatorClosureCustom() throws InterruptedException {
        testAggregation("direct:input4", "blahblah:ha");
    }
    
    @Test
    public void testStrategyClosureWithResult() throws InterruptedException {
        testAggregation("direct:input5", "blah:halb");
    }
    
    @Test
    public void testStrategyClosureWithoutResult() throws InterruptedException {
        testAggregation("direct:input6", "blah:halb");
    }
    
    public void testAggregation(String endpoint, String expectedBody) throws InterruptedException {
        mockOutput.expectedBodiesReceived(expectedBody);
        producerTemplate.sendBody(endpoint, "blah");
        mockOutput.assertIsSatisfied();
    }
    
}
