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
package org.openehealth.ipf.platform.camel.flow.extend;

import static org.junit.Assert.assertEquals;

import org.apache.camel.Exchange;
import org.junit.Test;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.openehealth.ipf.platform.camel.flow.process.AbstractFlowSplitTest;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Martin Krasser
 */
@ContextConfiguration(locations = { "/context-processor-factory.xml", 
                                    "/context-flow-route-groovy.xml" })
public class GroovyProcessorFactoryFlowTest extends AbstractFlowSplitTest {

    
    @Test
    public void testCamelSplitter2() throws InterruptedException {
        mock1.expectedBodiesReceived("test", "test");
        Exchange result = (Exchange)producerTemplate.send("direct:flow-test-split-camel", 
                createMessage("test,test").getExchange());
        mock1.assertIsSatisfied();
        assertEquals(SplitHistory.parse("[(0L),(0),(0)]"), 
                new PlatformMessage(mock1.getExchanges().get(0)).getSplitHistory());
        assertEquals(SplitHistory.parse("[(0L),(0),(1L)]"), 
                new PlatformMessage(mock1.getExchanges().get(1)).getSplitHistory());
        assertEquals(initial, new PlatformMessage(result).getSplitHistory()); assertEquals(initial, new PlatformMessage(result).getSplitHistory()); 
    }
    
    @Test
    public void testSplitterSingleResultSplit() throws Exception {
        mock1.expectedBodiesReceived("test");
        
        Exchange result = (Exchange)producerTemplate.send("direct:flow-test-split-camel", 
                createMessage("test").getExchange());
        
        mock1.assertIsSatisfied();
        assertEquals(initial, 
                new PlatformMessage(mock1.getExchanges().get(0)).getSplitHistory());
        assertEquals(initial, new PlatformMessage(result).getSplitHistory()); 
    }
    
}
