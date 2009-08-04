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
package org.openehealth.ipf.platform.camel.core.model;

import static org.apache.camel.util.ObjectHelper.notNull;
import groovy.lang.Closure;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregationStrategy;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;

/**
 * {@link ProcessorType} for the {@link Splitter} processor
 * This class is needed to create a {@link Splitter} that hands on the
 * sub exchanges to a specified processor. The {@link Splitter} requires 
 * explicit definition of this processor because it has to send multiple 
 * exchanges to the processor. Usually processors only send a single exchange
 * to the next processor in the route, which is done automatically by Camel.
 * 
 * @author Jens Riemschneider
 * @author Martin Krasser
 */
public class SplitterType extends OutputDefinition<ProcessorDefinition> {

    private AggregationStrategy aggregationStrategy;
    private ExpressionDefinition expressionDefinition;
    private String expressionBean;

    /**
     * Creates a split type, i.e. a builder for {@link Splitter}
     * @param expression
     *          The expression to be passed to the {@link Splitter} upon 
     *          creation
     */
    public SplitterType(Expression expression) {
        notNull(expression, "expression");
        this.expressionDefinition = new ExpressionDefinition(expression);
    }

    public SplitterType(String expressionBean) {
        notNull(expressionBean, "expressionBean");
        this.expressionBean = expressionBean;
    }

    /* (non-Javadoc)
     * Creates the actual {@link Splitter} via the data in this builder
     * @see org.apache.camel.model.ProcessorType#createProcessor(org.apache.camel.spi.RouteContext)
     */
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        if (aggregationStrategy == null) {
            aggregationStrategy = new UseLatestAggregationStrategy();
        }
        if (expressionBean != null) {
            expressionDefinition = new ExpressionDefinition(routeContext.lookup(expressionBean, Expression.class));
        }
        Expression expression = expressionDefinition.createExpression(routeContext);
        Splitter splitter = createSplitterInstance(expression, childProcessor);
        
        splitter.aggregate(aggregationStrategy);
        
        return splitter;
    }

    protected Splitter createSplitterInstance(Expression expression, Processor processor) {
        return new Splitter(expression, processor);
    }
    
    /* (non-Javadoc)
     * @see org.apache.camel.model.ExpressionNode#getShortName()
     */
    @Override
    public String getShortName() {
        return "split";
    }
   
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Splitter[" + expressionDefinition + " -> " + getOutputs() + "]";
    }

    /**
     * Defines the aggregation strategy used by the {@link Splitter} created by
     * {@link #createProcessor(RouteContext)}
     * @param aggregationStrategy    
     *          the aggregation strategy. see {@link Splitter}
     * @return this instance for chaining other calls
     */
    public SplitterType aggregate(Closure aggregationStrategy) {
        return RENAMED_aggregate_RENAMED(new DelegatingAggregationStrategy(aggregationStrategy));
    }
    
    /**
     * Defines the aggregation strategy used by the {@link Splitter} created by
     * {@link #createProcessor(RouteContext)}
     * @param aggregationStrategy    
     *          the aggregation strategy to be used by the {@link Splitter} 
     *          created by {@link #createProcessor(RouteContext)}
     * @return this instance for chaining other calls
     */
    // FIXME: after resolving compiler errors
    public SplitterType RENAMED_aggregate_RENAMED(AggregationStrategy aggregationStrategy) {
        notNull(aggregationStrategy, "aggregationStrategy");
        this.aggregationStrategy = aggregationStrategy;
        return this;
    }
    
}
