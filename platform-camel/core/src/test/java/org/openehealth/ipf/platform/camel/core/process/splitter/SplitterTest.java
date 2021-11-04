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

import org.apache.camel.*;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.util.ObjectHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.platform.camel.core.process.splitter.support.TextFileIterator;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Jens Riemschneider
 */
public class SplitterTest {
    private static CamelContext camelContext;

    private Splitter splitter;
    private TestProcessor dest;

    @BeforeAll
    public static void setUpClass() {
        camelContext = new DefaultCamelContext();
    }

    @BeforeEach
    public void setUp() {
        var splitRule = new TestSplitRule();
        var aggregationStrat = new TestAggregationStrategy();
        dest = new TestProcessor();
        
        splitter = new Splitter(splitRule, dest);
        splitter.aggregate(aggregationStrat);
    }
    
    @Test
    public void testProcess() throws Exception {
        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitter.process(origExchange);

        var received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("bla:blu", origExchange.getMessage().getBody());
    }
    
    @Test
    public void testForNullSafeConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Splitter(null, dest));
    }
    
    @Test
    public void testResetToDefaults() throws Exception {
        splitter.aggregate(null);

        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitter.process(origExchange);

        var received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("blu", origExchange.getMessage().getBody());
    }
    
    @Test
    public void testSplitRuleWithArrayResult() throws Exception {
        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        var splitterWithArrayResult = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return type.cast(getContent(exchange).split(","));            
            }}, dest);
        splitterWithArrayResult.aggregate(new TestAggregationStrategy());

        splitterWithArrayResult.process(origExchange);

        var received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));
        
        assertEquals("bla:blu", origExchange.getMessage().getBody());
    }
    
    @Test
    public void testSplitRuleWithNonIterableResult() throws Exception {
        var splitterSimpleRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return type.cast("smurf:" + exchange.getIn().getBody());
            }}, dest);

        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterSimpleRule.process(origExchange);

        var received = dest.getReceived();
        assertEquals(1, received.size());        
        assertEquals("smurf:bla,blu", getContent(received.get(0)));
    }
    
    @Test
    public void testSplitRuleResultsInNothing() throws Exception {
        var splitterEmptyRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return null;
            }}, dest);

        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterEmptyRule.process(origExchange);

        var received = dest.getReceived();
        assertEquals(0, received.size());        
    }
    
    @Test
    public void testSplitRuleResultsInIterator() throws Exception {
        final var results = Arrays.asList("bla", "blu");

        var splitterIteratorRule = new Splitter(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                return type.cast(results.iterator());
            }}, dest);

        var origExchange = createTestExchange();
        origExchange.getIn().setBody("bla,blu");
        splitterIteratorRule.process(origExchange);

        var received = dest.getReceived();
        assertEquals(2, received.size());
        
        assertEquals("bla", getContent(received.get(0)));
        assertEquals("blu", getContent(received.get(1)));        
        assertEquals("blu", origExchange.getMessage().getBody());
    }

    private Exchange createTestExchange() {
        return new DefaultExchange(camelContext, ExchangePattern.InOut);
    }
    
    private static String getContent(Exchange exchange) {
        var message = exchange.getIn();
        return (String)message.getBody();
    }
    
    /**
     * Split rule that splits a comma separated message body into its parts
     * E.g.: "blue,smurf" creates two messages "blue" and "smurf"
     */
    public static class TestSplitRule implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            var parts = getContent(exchange).split(",");
            return type.cast(Arrays.asList(parts));
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

            var parts = getContent(exchange).split(",");
            return type.cast(new OneTimeUsageIterable<>(Arrays.asList(parts)));
        }

        private boolean evaluateCalled;
    }
    
    public static class TestSplitFileRule implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            var filename = (String)exchange.getIn().getBody();
            try {
                return type.cast(new TextFileIterator(filename));
            } catch (IOException e) {
                fail("Caught exception: " + e);
            }
            return null;
        }
    }
    
    public static class TestAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            var oldContent = getContent(oldExchange);
            var newContent = getContent(newExchange);
            var aggregateContent = oldContent + ":" + newContent;
            var aggregate = oldExchange.copy();
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
        
        private List<Exchange> received = new ArrayList<>();
    }
}
