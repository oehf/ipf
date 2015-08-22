/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.core.extend

import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.ChoiceDefinition
import org.apache.camel.model.FilterDefinition
import org.apache.camel.model.OnExceptionDefinition
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.DelegateProcessor
import org.apache.camel.spi.DataFormat
import org.openehealth.ipf.commons.core.modules.api.Predicate
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter
import org.openehealth.ipf.platform.camel.core.adapter.PredicateAdapter
import org.openehealth.ipf.platform.camel.core.closures.*
import org.openehealth.ipf.platform.camel.core.dataformat.GnodeDataFormat
import org.openehealth.ipf.platform.camel.core.dataformat.GpathDataFormat
import org.openehealth.ipf.platform.camel.core.model.InterceptDefinition
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition

/**
 * Core DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 *
 *
 * @DSL
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
public class LegacyCoreExtensionModule {
    /**
     * Adds a <a href="http://camel.apache.org/processor.html">processor</a> to the route 
     * that calls the given processor bean
     * @param processorBeanName
     *          name of the processor bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-processbean
     * @DSLClosure currentType ( subType ( Number ) )
     *
     * @deprecated use {@link ProcessorDefinition#processRef(java.lang.String)}
     */
    public static ProcessorDefinition process(ProcessorDefinition self, String processorBeanName) {
        return self.processRef(processorBeanName);
    }

    /**
     * Adds a <a href="http://camel.apache.org/processor.html">processor</a> to the route 
     * that calls the given processor logic.
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param processorLogic
     *          closure that implements the logic of the processor
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-processclosure
     */
    public static ProcessorDefinition process(ProcessorDefinition self, Closure processorLogic) {
        return self.process(new DelegatingProcessor(processorLogic));
    }

    /**
     * Adds an interceptor to the route using the specified processor as a wrapper.
     * @param delegateProcessor
     * 			the processor that intercepts exchanges
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-interceptdelegateprocessor
     *
     * @deprecated
     */
    public static InterceptDefinition intercept(ProcessorDefinition self, DelegateProcessor delegateProcessor) {
        InterceptDefinition answer = new InterceptDefinition(delegateProcessor);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds an interceptor to the route that uses the given interceptor logic
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param interceptorLogic
     *          closure that implements the logic of the interceptor
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-interceptclosure
     */
    public static InterceptDefinition intercept(ProcessorDefinition self, Closure interceptorLogic) {
        return intercept(self, new DelegatingInterceptor(interceptorLogic));
    }

    /**
     * Adds an interceptor to the route that uses the given interceptor logic.
     * @param interceptorBean
     *          name of a bean that implements the {@link DelegateProcessor}.
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-interceptbean
     *
     * @deprecated
     */
    public static InterceptDefinition intercept(ProcessorDefinition self, String interceptorBean) {
        InterceptDefinition answer = new InterceptDefinition(interceptorBean);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds a <a href="http://camel.apache.org/message-filter.html">filter</a> 
     * to the route using the predicate logic to find out if a message passes the 
     * filter or not
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param predicateLogic
     *          logic to apply to the filter
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-filterclosure
     */
    public static FilterDefinition filter(ProcessorDefinition self, Closure predicateLogic) {
        return self.filter(new DelegatingCamelPredicate(predicateLogic));
    }

    /**
     * Sets a message body via the expression depending on the Message Exchange Pattern
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param transformExpression
     *          the expression to set the body
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transformclosure 
     */
    public static ProcessorDefinition transform(ProcessorDefinition self, Closure transformExpression) {
        return self.transform(new DelegatingExpression(transformExpression));
    }

    /**
     * Sets a property of an exchange to the result of the property expression
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param name
     *          name of the property
     * @param propertyExpression
     *          expression that returns the value of the property
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setPropertyclosure
     */
    public static ProcessorDefinition setExchangeProperty(ProcessorDefinition self, String name, Closure propertyExpression) {
        return self.setProperty(name, new DelegatingExpression(propertyExpression));
    }

    /**
     * Sets a header of the input message to the result of the header expression
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param name
     *          name of the header
     * @param headerExpression
     *          expression that returns the value of the header
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setHeaderclosure
     */
    public static ProcessorDefinition setHeader(ProcessorDefinition self, String name, Closure headerExpression) {
        return self.setHeader(name, new DelegatingExpression(headerExpression));
    }

    /**
     * Sets a header of the output message to the result of the header expression
     * @param name
     *          name of the header
     * @param headerExpression
     *          expression that returns the value of the header
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setHeaderclosure
     *
     * @deprecated
     */
    public static ProcessorDefinition setOutHeader(ProcessorDefinition self, String name, Closure headerExpression) {
        return self.setOutHeader(name, new DelegatingExpression(headerExpression));
    }

    /**
     * Sets a header of the fault message to the result of the header expression
     * @param name
     *          name of the header
     * @param headerExpression
     *          expression that returns the value of the header
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setHeaderclosure
     *
     * @deprecated
     */
    public static ProcessorDefinition setFaultHeader(ProcessorDefinition self, String name, Closure headerExpression) {
        return self.setFaultHeader(name, new DelegatingExpression(headerExpression));
    }

    /**
     * Sets the input message body via the expression
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param bodyExpression
     *          the expression to evaluate
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setBodyclosure
     */
    public static ProcessorDefinition setBody(ProcessorDefinition self, Closure bodyExpression) {
        return self.setBody(new DelegatingExpression(bodyExpression));
    }


    /**
     * Calls an endpoint specified by the URI and merges the original exchange
     * with the result of the call by applying the aggregation logic
     *
     * Note: conflicts with extension in camel-groovy
     *
     * @param resourceUri
     *          the URI of the endpoint to call
     * @param aggregationLogic
     *          a closure implementing the logic to aggregate the two exchanges
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Contentenrichment
     */
    public static ProcessorDefinition enrich(ProcessorDefinition self, String resourceUri, Closure aggregationLogic) {
        return self.enrich(resourceUri, new DelegatingAggregationStrategy(aggregationLogic));
    }


    /**
     * Validates a message
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate
     *
     * @deprecated conflicts with {@link ProcessorDefinition#validate()}
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition();
        self.addOutput(answer);
        return answer;
    }


    /**
     * Validates a message using a validator bean
     * @param validatorBeanName
     *          the name of the bean implementing the validator
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate
     *
     * @deprecated
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self, String validatorBeanName) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition(validatorBeanName);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Validates a message using the validator logic
     *
     * Note: conflicts with camel-groovy
     *
     * @param validatorLogic
     *          a closure implementing the validator logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate 
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self, Closure validatorLogic) {
        return validate(self, new DelegatingValidator(validatorLogic));
    }


    /**
     * Adds a routing decision for <a href="http://camel.apache.org/message-router.html">message routing</a> 
     * using the predicate logic
     *
     * Note: conflicts with camel-groovy
     *
     * @param predicateLogic
     *          logic of the predicate
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-whenclosure
     */
    public static ChoiceDefinition when(ChoiceDefinition self, Closure predicateLogic) {
        return self.when(new DelegatingCamelPredicate(predicateLogic));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     * with a specific schema
     *
     * Note: conflicts with camel-groovy
     *
     * @param schemaResource
     *          the schema to use
     * @param namespaceAware
     * {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self, String schemaResource, boolean namespaceAware) {
        return dataFormat(self, new GnodeDataFormat(schemaResource, namespaceAware));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     *
     * Note: conflicts with camel-groovy
     *
     * @param namespaceAware
     * {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self, boolean namespaceAware) {
        return dataFormat(self, new GnodeDataFormat(namespaceAware));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     * (by default namespace aware)
     *
     * Note: conflicts with camel-groovy
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self) {
        return gnode(self, true);
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     * with a specific schema
     *
     * Note: conflicts with camel-groovy
     *
     * @param schemaResource
     *          the schema to use
     * @param namespaceAware
     * {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self, String schemaResource, boolean namespaceAware) {
        return dataFormat(self, new GpathDataFormat(schemaResource, namespaceAware));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     *
     * Note: conflicts with camel-groovy
     *
     * @param namespaceAware
     * {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self, boolean namespaceAware) {
        return dataFormat(self, new GpathDataFormat(namespaceAware));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://docs.groovy-lang.org/latest/html/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     * (by default namespace aware)
     *
     * Note: conflicts with camel-groovy
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self) {
        return gpath(self, true);
    }


    /**
     * Allows for fine-grained <a href="http://camel.apache.org/exception-clause.html">exception handling</a>
     *
     * Note: conflicts with camel-groovy
     *
     * @param predicate
     *          the predicate to check for the exception
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-onwhenclosure
     */
    public static OnExceptionDefinition onWhen(OnExceptionDefinition self, Closure predicate) {
        return self.onWhen(new DelegatingCamelPredicate(predicate));
    }


    /**
     * Defines an <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/processor/aggregate/AggregationStrategy.html">AggregationStrategy</a>
     * via a closure
     *
     * Note: conflicts with camel-groovy
     *
     * @param aggregationLogic
     *          closure implementing the aggregation logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Aggregator
     */
    public static AggregatorAdapter aggregationStrategy(RouteBuilder self, Closure aggregationLogic) {
        return aggregationStrategy(self, new DelegatingAggregator(aggregationLogic));
    }

    /**
     * Defines a <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/Predicate.html">Predicate</a> 
     * via the {@code Predicate} interface
     * @param predicate
     *          the predicate instance
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-predicateextension
     *
     * @deprecated use Camel predicates
     *
     */
    public static PredicateAdapter predicate(RouteBuilder self, Predicate predicate) {
        return new PredicateAdapter(predicate);
    }

    /**
     * Defines a <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/Predicate.html">Predicate</a> 
     * via a bean
     * @param predicateBeanName
     *          name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-predicateextension
     *
     * @deprecated use Camel predicates
     *
     */
    public static PredicateAdapter predicate(RouteBuilder self, String predicateBeanName) {
        return predicate(self, self.lookup(predicateBeanName, Predicate.class));
    }

    /**
     * Defines a <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/Predicate.html">Predicate</a> 
     * via a closure
     * @param predicateLogic
     *          closure implementing the logic of the predicate
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-predicateextension
     *
     * @deprecated use Camel predicates
     */
    public static PredicateAdapter predicate(RouteBuilder self, Closure predicateLogic) {
        return predicate(self, new DelegatingPredicate(predicateLogic));
    }


    static ProcessorDefinition dataFormat(DataFormatClause self, DataFormat dataFormat) {
        if (self.operation == DataFormatClause.Operation.Marshal) {
            return self.processorType.marshal(dataFormat);
        } else if (self.operation == DataFormatClause.Operation.Unmarshal) {
            return self.processorType.unmarshal(dataFormat);
        } else {
            throw new IllegalArgumentException("Unknown data format operation: " + self.operation);
        }
    }


}
