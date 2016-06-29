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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.util.ExchangeHelper;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * A processor that splits an exchange into multiple exchanges by using a rule.
 * The rule generates the individual sub exchanges (i.e. the result exchanges
 * of the split).
 * An {@link AggregationStrategy} allows aggregation of the sub exchanges.
 *
 * @author Jens Riemschneider
 */
public class Splitter extends DelegateProcessor {
    private final Expression splitRule;

    private static final UseLatestAggregationStrategy DEFAULT_AGGREGATION_STRATEGY =
            new UseLatestAggregationStrategy();

    private AggregationStrategy aggregationStrategy = DEFAULT_AGGREGATION_STRATEGY;

    /**
     * Creates a splitter
     *
     * @param splitRule expression that performs the splitting of the original exchange
     * @param processor destination processor for all sub exchanges. Can be {@code null}
     *                  if the destination is set later via {@link #setProcessor(Processor)},
     *                  e.g. via an intercept.
     */
    public Splitter(Expression splitRule, Processor processor) {
        super(processor);

        notNull(splitRule, "splitRule");
        this.splitRule = splitRule;
    }

    /**
     * Sets the strategy to aggregate data over all sub exchanges created by
     * the splitter
     * This method allows for chain configuration
     *
     * @param strategy the aggregation strategy
     * @return the splitter for chaining
     */
    public Splitter aggregate(AggregationStrategy strategy) {
        aggregationStrategy =
                strategy != null ? strategy : DEFAULT_AGGREGATION_STRATEGY;
        return this;
    }

    /**
     * Processes the given exchange
     * This method is the entry point for splitting the given exchange into
     * its parts via the split rule. Subclasses can change the created sub
     * exchanges by overriding {@link #finalizeSubExchange(Exchange, Exchange, SplitIndex)}
     * and the aggregate result by overriding {@link #finalizeAggregate(Exchange, Exchange)}.
     *
     * @param origExchange exchange that should be split by this processor
     */
    @Override
    protected void processNext(Exchange origExchange) throws Exception {
        notNull(origExchange, "origExchange");
        Iterable splitResult = evaluateSplitRule(origExchange);
        Exchange aggregate = processAllResults(origExchange, splitResult);
        finalizeAggregate(origExchange, aggregate);
    }

    /**
     * Creates the actual aggregation result of the processor
     * This method is called by {@link #processNext(Exchange)} to calculate the
     * aggregation result of the splitter. The base implementation in this class
     * simply copies the results of the aggregate into the original exchange.
     * Sub classes should call this base method implementation to ensure
     * compatibility with upcoming version.
     *
     * @param origExchange the exchange that was originally passed to {@link #process(Exchange)}.
     *                     The base implementation changes the content of this exchange to
     *                     ensure that further processing in the route is performed with
     *                     the aggregated result.
     * @param aggregate    the aggregation result. This is the exchange that was determined
     *                     by passing all processed sub exchanges to the {@link AggregationStrategy}
     *                     defined by {@link #aggregate(AggregationStrategy)}. This parameter
     *                     can be {@code null} if the  {@link AggregationStrategy} returned
     *                     null.
     */
    protected void finalizeAggregate(Exchange origExchange, Exchange aggregate) {
        if (aggregate != null) {
            ExchangeHelper.copyResults(origExchange, aggregate);
        }
    }

    /**
     * Updates the contents of sub exchanges
     * This method is called by {@link #processNext(Exchange)} before sending
     * a sub exchange to the destination processor. It is the final chance to
     * change the contents (body, header, etc.) of the sub exchange.
     * The base implementation of this method currently performs no operations.
     * However, it is recommended to call it in sub classes to ensure
     * compatibility with upcoming version of this class.
     *
     * @param origExchange original exchange passed to {@link #process(Exchange)}
     * @param subExchange  sub exchange that was split off. The content of this exchange
     *                     can be changed by this method.
     * @param index        index of the sub exchange in the result list of the split
     */
    protected void finalizeSubExchange(Exchange origExchange, Exchange subExchange, SplitIndex index) {
        // Do nothing
    }

    private Exchange processAllResults(Exchange origExchange,
                                       Iterable splitResult) throws Exception {

        Exchange aggregate = null;
        Iterator iterator = splitResult.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            Object splitPart = iterator.next();

            SplitIndex idx = SplitIndex.valueOf(counter, !iterator.hasNext());
            Exchange subExchange = processResult(origExchange, idx, splitPart);
            aggregate = doAggregate(aggregate, subExchange);

            ++counter;
        }
        return aggregate;
    }

    private Exchange processResult(final Exchange origExchange,
                                   final SplitIndex index,
                                   final Object splitPart) throws Exception {

        final Exchange subExchange = origExchange.copy();

        Message message = subExchange.getIn();
        message.setBody(splitPart);
        finalizeSubExchange(origExchange, subExchange, index);

        super.processNext(subExchange);
        return subExchange;
    }

    private Exchange doAggregate(Exchange aggregate, Exchange subExchange) {
        if (aggregationStrategy != null) {
            if (aggregate == null) {
                aggregate = subExchange;
            } else {
                aggregate = aggregationStrategy.aggregate(aggregate, subExchange);
            }
        }

        return aggregate;
    }

    private Iterable evaluateSplitRule(Exchange origExchange) {
        final Object splitResult = splitRule.evaluate(origExchange, Object.class);

        if (null == splitResult) {
            return Collections.emptySet();
        }

        if (splitResult instanceof Iterable) {
            return (Iterable) splitResult;
        }

        if (splitResult instanceof Iterator) {
            return () -> (Iterator) splitResult;
        }

        if (splitResult.getClass().isArray()) {
            return Arrays.asList((Object[]) splitResult);
        }

        return Collections.singleton(splitResult);
    }

}
