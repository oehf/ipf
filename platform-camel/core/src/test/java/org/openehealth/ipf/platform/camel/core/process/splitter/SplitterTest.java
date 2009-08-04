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
package org.openehealth.ipf.platform.camel.core.process.splitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.util.ObjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;
import org.openehealth.ipf.platform.camel.core.process.splitter.support.TextFileIterator;


/**
 * @author Jens Riemschneider
 */
public class SplitterTest {
    private Splitter splitter;

    private TestSplitRule splitRule;
    private TestAggregationStrategy aggregationStrat;
    private TestProcessor dest;
    
    @Before
    public void setUp() {
        splitRule = new TestSplitRule();
        aggregationStrat = new TestAggregationStrategy();
        dest = new TestProcessor();
        
        splitter = new Splitter(splitRule, dest);
        splitter.aggregate(aggregationStrat);
    }
    
    @Test
    public void testProcess() throws Exception {
        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitter.process(origExchange);
        
        List<Exchange> received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("bla:blu", origExchange.getOut().getBody());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testForNullSafeConstructor() {
        splitter = new Splitter(null, dest);
    }
    
    @Test
    public void testResetToDefaults() throws Exception {
        splitter.aggregate(null);
        
        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitter.process(origExchange);
        
        List<Exchange> received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("blu", origExchange.getOut().getBody());
    }
    
    @Test
    public void testSplitRuleWithArrayResult() throws Exception {
        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        Splitter splitterWithArrayResult = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)getContent(exchange).split(",");            
            }}, dest);
        splitterWithArrayResult.aggregate(new TestAggregationStrategy());

        splitterWithArrayResult.process(origExchange);
        
        List<Exchange> received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("bla:blu", origExchange.getOut().getBody());
    }
    
    @Test
    public void testSplitRuleWithNonIterableResult() throws Exception {
        Splitter splitterSimpleRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)("smurf:" + exchange.getIn().getBody());
            }}, dest);

        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterSimpleRule.process(origExchange);

        List<Exchange> received = dest.getReceived();
        assertEquals(1, received.size());        
        assertEquals("smurf:bla,blu", getContent(received.get(0)));
    }
    
    @Test
    public void testSplitRuleResultsInNothing() throws Exception {
        Splitter splitterEmptyRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return null;
            }}, dest);

        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterEmptyRule.process(origExchange);

        List<Exchange> received = dest.getReceived();
        assertEquals(0, received.size());        
    }
    
    @Test
    public void testSplitRuleResultsInIterator() throws Exception {
        final List<String> results = Arrays.asList("bla", "blu"); 
        
        Splitter splitterIteratorRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return (T)results.iterator();
            }}, dest);

        Exchange origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterIteratorRule.process(origExchange);

        List<Exchange> received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));        
        assertEquals("blu", origExchange.getOut().getBody());
    }

    private static Exchange createTestExchange() {
        return new DefaultExchange((CamelContext)null, ExchangePattern.InOut);
    }
    
    private static String getContent(Exchange exchange) {
        Message message = exchange.getIn();
        return (String)message.getBody();
    }
    
    /**
     * Split rule that splits a comma separated message body into its parts
     * E.g.: "blue,smurf" creates two messages "blue" and "smurf"
     */
    public static class TestSplitRule implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            String[] parts = getContent(exchange).split(",");            
            return (T)Arrays.asList(parts);
        }
    }
    
    /**
     * An {@link Iterable} that only allows to call {@link #iterator()} once
     */
    private static class OneTimeUsageIterable<T> implements Iterable<T> {
        public OneTimeUsageIterable(Iterable<T> baseIterable) {
            ObjectHelper.notNull(baseIterable, "baseIterable");
            this.baseIterable = baseIterable;
        }

        /**
         * Delegates to the base iterable
         */
        @Override
        public Iterator<T> iterator() {
            if (iteratorCalled) {
                throw new IllegalStateException("iterator() can only be called once");
            }
            
            iteratorCalled = true;
            return baseIterable.iterator();
        }        

        private Iterable<T> baseIterable;
        private boolean iteratorCalled;
    }
    
    public static class TestSplitRuleSingleUse implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            if (evaluateCalled) {
                throw new IllegalStateException("evaluate() can only be called once");
            }
            evaluateCalled = true;
            
            String[] parts = getContent(exchange).split(",");
            return (T)new OneTimeUsageIterable<String>(Arrays.asList(parts)); 
        }

        private boolean evaluateCalled;
    }
    
    public static class TestSplitFileRule implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            String filename = (String)exchange.getIn().getBody();
            try {
                return (T)new TextFileIterator(filename);
            } catch (IOException e) {
                fail("Caught exception: " + e);
            }
            return null;
        }
    }
    
    public static class TestAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            String oldContent = getContent(oldExchange);
            String newContent = getContent(newExchange);
            String aggregateContent = oldContent + ":" + newContent;
            Exchange aggregate = oldExchange.copy();
            aggregate.getIn().setBody(aggregateContent);
            return aggregate;
        }
    }
    
    private static class TestProcessor implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            received.add(exchange);
        }
        
        public List<Exchange> getReceived() {
            return Collections.unmodifiableList(received);
        }
        
        private List<Exchange> received = new ArrayList<Exchange>();
    }
}
