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
package org.openehealth.ipf.platform.camel.core.process;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.copyExchange;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.createExchange;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.prepareResult;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.producerTemplate;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * A content enricher that enriches input data by first obtaining additional
 * data from a <i>resource</i> identified by an <code>resourceUri</code> and
 * second by aggregating input data and additional data. Aggregation of input
 * data and additional data is delegated to an {@link AggregationStrategy}
 * object.
 * 
 * @author Martin Krasser
 */
public class Enricher implements Processor {

    private String resourceUri;
    
    private AggregationStrategy aggregationStrategy;
    
    /**
     * Creates a new {@link Enricher}. The implicit aggregation strategy is to
     * copy the additional data obtained from the enricher's resource over the
     * input data. When using the copy aggregation strategy the enricher
     * degenerates to a normal transformer.
     * 
     * @param resourceUri
     *            URI of resource endpoint for obtaining additional data.
     */
    public Enricher(String resourceUri) {
        this(defauAggregationStrategy(), resourceUri);
    }
    
    /**
     * Creates a new {@link Enricher}.
     * 
     * @param aggregationStrategy
     *            aggregation strategy to aggregate input data and additional
     *            data.
     * @param resourceUri
     *            URI of resource endpoint for obtaining additional data.
     */
    public Enricher(AggregationStrategy aggregationStrategy, String resourceUri) {
        this.aggregationStrategy = aggregationStrategy;
        this.resourceUri = resourceUri;
    }
    
    /**
     * Sets the aggregation strategy for this enricher.
     * 
     * @param aggregationStrategy the aggregationStrategy to set
     */
    public void setAggregationStrategy(AggregationStrategy aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;
    }
    
    /**
     * Sets the default aggregation strategy for this enricher.
     * 
     * @param aggregationStrategy the aggregationStrategy to set
     */
    public void setDefaultAggregationStrategy() {
        this.aggregationStrategy = defauAggregationStrategy();
    }
    
    /**
     * Enriches the input data (<code>exchange</code>) by first obtaining
     * additional data from an endpoint identified by an
     * <code>resourceUri</code> and second by aggregating input data and
     * additional data. Aggregation of input data and additional data is
     * delegated to an {@link AggregationStrategy} object set at construction
     * time. If the message exchange with the resource endpoint fails then no
     * aggregation will be done and the failed exchange content is copied over
     * to the original message exchange.
     * 
     * @param exchange
     *            input data.
     */
    public void process(Exchange exchange) throws Exception {
        // create in-out exchange to obtain additional data from resource
        Exchange resourceExchange = createExchange(exchange, ExchangePattern.InOut);
        // send created exchange to resource endpoint
        resourceExchange = producerTemplate(exchange).send(resourceUri, resourceExchange);
        
        if (resourceExchange.isFailed()) {
            // copy resource exchange onto original exchange (preserving pattern)
            copyExchange(resourceExchange, exchange);
        } else {
            prepareResult(exchange);
            // aggregate original exchange and resource exchange
            Exchange aggregatedExchange = aggregationStrategy.aggregate(exchange, resourceExchange);
            // copy aggregation result onto original exchange (preserving pattern)
            copyExchange(aggregatedExchange, exchange);
        }
    }
    
    private static AggregationStrategy defauAggregationStrategy() {
        return new CopyAggregationStrategy();
    }
    
    private static class CopyAggregationStrategy implements AggregationStrategy {

        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            copyExchange(newExchange, oldExchange);
            return oldExchange;
        }
        
    }
    
}
