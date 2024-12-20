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

import groovy.lang.Closure;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.openehealth.ipf.commons.core.modules.api.Aggregator;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;

import java.util.Arrays;

import static org.apache.camel.builder.Builder.body;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.prepareResult;

/**
 * Adapts an {@link Aggregator}. 
 * 
 * @author Martin Krasser
 */
public class AggregatorAdapter extends AdapterSupport implements AggregationStrategy {

    private Expression aggregationInputExpression;

    private final Aggregator aggregator;

    /**
     * Creates a new {@link AggregatorAdapter} and sets the delegate
     * {@link Aggregator}.
     * 
     * @param aggregator
     *            an aggregator.
     */
    public AggregatorAdapter(Aggregator aggregator) {
        this.aggregator = aggregator;
        aggregationInputExpression = body();
    }
    
    /**
     * Sets an {@link Expression} for obtaining data to be obtained from an
     * additional (new) {@link Exchange}. The default expression obtains the
     * body from the input message.
     * 
     * @param aggregationInputExpression
     *            expression for obtaining aggregation input data.
     * @return this object.
     * 
     * @see #aggregate(Exchange, Exchange)
     */
    public AggregatorAdapter aggregationInput(Expression aggregationInputExpression) {
        this.aggregationInputExpression = aggregationInputExpression;
        return this;
    }

    /**
     * Sets an expression {@link Closure} for obtaining data to be obtained from
     * an additional (new) {@link Exchange}. The default expression obtains the
     * body from the input message.
     * 
     * @param aggregationInputExpressionLogic
     *            expression for obtaining aggregation input data.
     * @return this object.
     * 
     * @see #aggregate(Exchange, Exchange)
     */
    public AggregatorAdapter aggregationInput(@ClosureParams(value = SimpleType.class, options = { "org.apache.camel.Expression"})
                                                      Closure aggregationInputExpressionLogic) {
        return aggregationInput(new DelegatingExpression(aggregationInputExpressionLogic));
    }

    /**
     * Applies expressions to <code>oldExchange</code> and
     * <code>newExchange</code> and delegates further processing to
     * {@link #doAggregate(Exchange, Object, Object, Object...)}
     * 
     * @see #aggregationInput(Expression)
     * @see #input(Expression)
     * @see #params(Expression)
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        var newInput = adaptAggregationInput(newExchange);
        var oldInput = adaptInput(oldExchange);
        var params = adaptParams(oldExchange);
        if (params == null) {
            doAggregate(oldExchange, oldInput, newInput, (Object[])null);
        } else if (params.getClass().isArray()) {
            doAggregate(oldExchange, oldInput, newInput, (Object[])params);
        } else {
            doAggregate(oldExchange, oldInput, newInput, params);
        }
        return oldExchange;
    }

    /**
     * Aggregates <code>oldInputData</code> and <code>newInputData</code>.
     * The aggregation result is written to body of the message returned by
     * .
     * 
     * @param oldExchange original message exchange to write results to.
     * @param oldInputData original input data
     * @param newInputData additional input data
     * @param inputParams input parameters
     */
    protected void doAggregate(Exchange oldExchange, Object oldInputData, 
            Object newInputData, Object... inputParams) {
        
        prepareResult(oldExchange).setBody(
                aggregator.zap(Arrays.asList(oldInputData, newInputData), inputParams));
    }
    
    /**
     * Applies the {@link Expression} set by
     * {@link #aggregationInput(Expression)} to obtain input data from the
     * <code>exchange</code>.
     * 
     * @param exchange
     *            message exchange.
     * @return aggregation input data or <code>null</code> if the expression
     *         evaluates to <code>null</code> or the expression object is
     *         <code>null</code>.
     */
    private Object adaptAggregationInput(Exchange exchange) {
        if (aggregationInputExpression == null) {
            return null;
        }
        return aggregationInputExpression.evaluate(exchange, Object.class);
    }

}
