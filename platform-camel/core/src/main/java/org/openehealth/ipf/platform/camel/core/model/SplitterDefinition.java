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

import groovy.lang.Closure;
import groovy.transform.stc.ClosureParams;
import groovy.transform.stc.SimpleType;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Expression;
import org.apache.camel.model.OutputDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.spi.Metadata;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregationStrategy;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * {@link OutputDefinition} for the {@link Splitter} processor
 * This class is needed to create a {@link Splitter} that hands on the
 * sub exchanges to a specified processor. The {@link Splitter} requires 
 * explicit definition of this processor because it has to send multiple 
 * exchanges to the processor. Usually processors only send a single exchange
 * to the next processor in the route, which is done automatically by Camel.
 * 
 * @author Jens Riemschneider
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip")
@XmlRootElement(name = "splitter")
@XmlType(name = "CoreSplitterDefinitionType")
@XmlAccessorType(XmlAccessType.FIELD)
public class SplitterDefinition extends OutputDefinition<RouteDefinition> {

    @XmlTransient
    private AggregationStrategy aggregationStrategy;
    @XmlElementRef
    private ExpressionDefinition expressionDefinition;
    @XmlAttribute
    private String expressionBean;

    public SplitterDefinition() {
    }

    /**
     * Creates a split definition, i.e. a builder for {@link Splitter}.
     * @param expression
     *          The expression to be passed to the {@link Splitter} upon 
     *          creation.
     */
    public SplitterDefinition(Expression expression) {
        notNull(expression, "expression");
        expressionDefinition = new ExpressionDefinition(expression);
    }

    /**
     * Creates a split type, i.e. a builder for {@link Splitter}
     * @param expressionBean
     *          The name of the expression bean to be passed to the {@link Splitter} 
     *          upon creation
     */
    public SplitterDefinition(String expressionBean) {
        notNull(expressionBean, "expressionBean");
        this.expressionBean = expressionBean;
    }
    
    @Override
    public String getShortName() {
        return "split";
    }
   
    @Override
    public String toString() {
        return "Splitter[" + expressionDefinition + " -> " + getOutputs() + "]";
    }

    /**
     * Defines the aggregation logic for the split results as a closure
     * @param aggregationStrategy    
     *          the aggregation strategy
     */
    public SplitterDefinition aggregationStrategy(@ClosureParams(value = SimpleType.class, options = { "org.apache.camel.Exchange", "org.apache.camel.Exchange"})
                                                          Closure aggregationStrategy) {
        return aggregationStrategy(new DelegatingAggregationStrategy(aggregationStrategy));
    }
    
    /**
     * Defines the aggregation logic for the split results via a strategy interface
     * @param aggregationStrategy    
     *          the aggregation strategy
     */
    public SplitterDefinition aggregationStrategy(AggregationStrategy aggregationStrategy) {
        notNull(aggregationStrategy, "aggregationStrategy");
        this.aggregationStrategy = aggregationStrategy;
        return this;
    }

    public AggregationStrategy getAggregationStrategy() {
        return aggregationStrategy;
    }

    public ExpressionDefinition getExpressionDefinition() {
        return expressionDefinition;
    }

    public String getExpressionBean() {
        return expressionBean;
    }
}
