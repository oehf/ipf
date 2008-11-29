package org.openehealth.ipf.platform.camel.core.closures;

import groovy.lang.Closure;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * An aggregation strategy that delegates to a {@link Closure}.
 */
public class DelegatingAggregationStrategy extends ClosureAdapter implements AggregationStrategy {

    public DelegatingAggregationStrategy(Closure closure) {
        super(closure);
    }

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Object result = call(oldExchange, newExchange);
        if (result instanceof Exchange) {
            return (Exchange)result;
        } else {
            return oldExchange;
        }
    }
    
}