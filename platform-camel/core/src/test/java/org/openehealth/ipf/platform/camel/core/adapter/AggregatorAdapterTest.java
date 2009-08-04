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
package org.openehealth.ipf.platform.camel.core.adapter;

import static org.apache.camel.builder.Builder.outBody;
import static org.junit.Assert.assertEquals;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.core.support.transform.min.TestAggregator;

/**
 * @author Martin Krasser
 */
public class AggregatorAdapterTest {

    private AggregationStrategy strategy;
    
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAggregateDefault() {
        strategy = new AggregatorAdapter(new TestAggregator());
        Exchange a = exchangeWithInBody("a");
        Exchange b = exchangeWithInBody("b");
        strategy.aggregate(a, b);
        assertEquals("a:b", a.getIn().getBody());
    }
    
    @Test
    public void testAggregateCustom() {
        strategy = new AggregatorAdapter(new TestAggregator()).aggregationInput(outBody());
        Exchange a = exchangeWithInBody("a");
        Exchange b = exchangeWithOutBody("b");
        strategy.aggregate(a, b);
        assertEquals("a:b", a.getIn().getBody());
    }
    
    private static Exchange exchangeWithInBody(Object body) {
        Exchange exchange = exchange();
        exchange.getIn().setBody(body);
        return exchange;
    }
    
    private static Exchange exchangeWithOutBody(Object body) {
        Exchange exchange = exchange();
        exchange.getOut().setBody(body);
        return exchange;
    }
    
    private static Exchange exchange() {
        return new DefaultExchange(new DefaultCamelContext());
    }
}
