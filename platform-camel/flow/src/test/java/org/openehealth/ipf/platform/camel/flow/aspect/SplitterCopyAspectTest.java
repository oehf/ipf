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
package org.openehealth.ipf.platform.camel.flow.aspect;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.Splitter;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.flow.ManagedMessage;
import org.openehealth.ipf.commons.flow.history.SplitHistory;
import org.openehealth.ipf.platform.camel.flow.PlatformMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-weaver.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class SplitterCopyAspectTest {

    private Processor splitter;
    
    private List<ManagedMessage> messages;
    
    @Before
    public void setUp() throws Exception {
        CamelContext context = new DefaultCamelContext();
        messages = new ArrayList<>();
        splitter = new Splitter(
                context,
                new TestExpression(),
                new TestProcessor(),
                new UseLatestAggregationStrategy());
    }

    @After
    public void tearDown() throws Exception {
        messages.clear();
    }

    @Test
    public void testMulticast() throws Exception {
        PlatformMessage message = createMessage();
        SplitHistory original = message.getSplitHistory();
        splitter.process(message.getExchange());
        assertEquals(original, message.getSplitHistory());
        for (int i = 0; i < messages.size(); i++) {
            SplitHistory expected = SplitHistory.parse("[(0/1),(" + i + "/3)]");
            assertEquals(expected, messages.get(i).getSplitHistory());
        }
    }
    
    private PlatformMessage createMessage() {
        return createMessage(new DefaultExchange(new DefaultCamelContext()));
    }
    
    private PlatformMessage createMessage(Exchange exchange) {
        return new PlatformMessage(exchange);
    }
    
    private class TestProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            messages.add(new PlatformMessage(exchange));
        }
    }
    
    private static class TestExpression implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            return type.cast(new String[] {"a", "b", "c"});
        }
    }

}
