package org.openehealth.ipf.platform.camel.core.reifier;

import org.apache.camel.*;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.reifier.ProcessorReifier;
import org.openehealth.ipf.platform.camel.core.model.SplitterDefinition;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;

/**
 * @author Christian Ohr
 */
public class SplitterReifier extends ProcessorReifier<SplitterDefinition> {

    public SplitterReifier(Route route, ProcessorDefinition<?> definition) {
        super(route, (SplitterDefinition) definition);
    }

    @Override
    public Processor createProcessor() throws Exception {
        Processor childProcessor = createChildProcessor(false);
        AggregationStrategy aggregationStrategy = definition.getAggregationStrategy();
        if (aggregationStrategy == null) {
            aggregationStrategy = new UseLatestAggregationStrategy();
        }
        ExpressionDefinition expressionDefinition = definition.getExpressionDefinition();
        String expressionBean = definition.getExpressionBean();
        if (expressionBean != null) {
            expressionDefinition = new ExpressionDefinition(camelContext.getRegistry().lookupByNameAndType(expressionBean, Expression.class));
        }
        Expression expression = expressionDefinition.createExpression(camelContext);
        Splitter splitter = createSplitterInstance(expression, childProcessor);

        splitter.aggregate(aggregationStrategy);

        return splitter;
    }


    protected Splitter createSplitterInstance(Expression expression, Processor processor) {
        return new Splitter(expression, processor);
    }
}
