/*
 * Copyright 2009 the original author or authors.
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

package org.openehealth.ipf.platform.camel.core.extend;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.DataFormatClause;
import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.FilterDefinition;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.core.modules.api.Aggregator;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Predicate;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.PredicateAdapter;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregationStrategy;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregator;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingCamelPredicate;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingInterceptor;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingPredicate;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingProcessor;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingTransmogrifier;
import org.openehealth.ipf.platform.camel.core.closures.DelegatingValidator;
import org.openehealth.ipf.platform.camel.core.dataformat.GnodeDataFormat;
import org.openehealth.ipf.platform.camel.core.dataformat.GpathDataFormat;
import org.openehealth.ipf.platform.camel.core.model.DataFormatAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.InterceptDefinition;
import org.openehealth.ipf.platform.camel.core.model.IpfDefinition;
import org.openehealth.ipf.platform.camel.core.model.ParserAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.TransmogrifierAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.model.ValidationDefinition;
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition;
import org.openehealth.ipf.platform.camel.core.util.Expressions;

/**
 * Core DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 * 
 * @DSL
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
public class CoreExtension {
    /**
     * Adds a <a href="http://camel.apache.org/processor.html">processor</a> to the route 
     * that calls the given processor bean
     * @param processorBeanName
     *          name of the processor bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-processbean
     * @DSLClosure currentType(subType(Number))

     */
    public static ProcessorDefinition process(ProcessorDefinition self, String processorBeanName) {
         return self.processRef(processorBeanName);
    }

    /**
     * Adds a <a href="http://camel.apache.org/processor.html">processor</a> to the route 
     * that calls the given processor logic
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
     */
    public static InterceptDefinition intercept(ProcessorDefinition self, DelegateProcessor delegateProcessor) {
        InterceptDefinition answer = new InterceptDefinition(delegateProcessor);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds an interceptor to the route that uses the given interceptor logic
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
     */
    public static InterceptDefinition intercept(ProcessorDefinition self, String interceptorBean) {
        InterceptDefinition answer = new InterceptDefinition(interceptorBean);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Drops the <a href="http://camel.apache.org/error-handler.html">error handler</a> 
     * from the route by using the {@code noErrorHandler}
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-unhandled
     */
    public static ProcessorDefinition unhandled(ProcessorDefinition self) {
        return self.errorHandler(new NoErrorHandlerBuilder());
    }
    
    /**
     * Adds a <a href="http://camel.apache.org/message-filter.html">filter</a> 
     * to the route using the predicate logic to find out if a message passes the 
     * filter or not
     * @param predicateLogic
     *          logic to apply to the filter
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-filterclosure
     */
    public static FilterDefinition filter(ProcessorDefinition self, Closure predicateLogic) {
        return self.filter(new DelegatingCamelPredicate(predicateLogic));
    }
    
    /**
     * Sets a message body via the expression depending on the Message Exchange Pattern
     * @param transformExpression
     *          the expression to set the body
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transformclosure 
     */
    public static ProcessorDefinition transform(ProcessorDefinition self, Closure transformExpression) {
        return self.transform(new DelegatingExpression(transformExpression));
    }

    /**
     * Sets a property of an exchange to the result of the property expression
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
     */
    public static ProcessorDefinition setFaultHeader(ProcessorDefinition self, String name, Closure headerExpression) {
        return self.setFaultHeader(name, new DelegatingExpression(headerExpression));
    }

    /**
     * Sets the input message body via the expression
     * @param bodyExpression
     *          the expression to evaluate
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-setBodyclosure
     */
    public static ProcessorDefinition setBody(ProcessorDefinition self, Closure bodyExpression) {
        return self.setBody(new DelegatingExpression(bodyExpression));
    }

    /**
     * Creates a validation workflow where actual validation is delegated to the given 
     * validator
     * @param validator
     *          the processor used for validation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validationprocessor
     */
    public static ValidationDefinition validation(ProcessorDefinition self, Processor validator) {
        ValidationDefinition answer = new ValidationDefinition(validator);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Creates a validation workflow where actual validation is done by calling the
     * given endpoint
     * @param validationUri
     *          the URI of the endpoint to call for validation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validationendpoint
     */
    public static ValidationDefinition validation(ProcessorDefinition self, String validationUri) {
        ValidationDefinition answer = new ValidationDefinition(validationUri);
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Creates a validation workflow where actual validation is delegated to given 
     * validator logic
     * @param validatorLogic
     *          the closure implementing the validation logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validationclosure
     */
    public static ValidationDefinition validation(ProcessorDefinition self, Closure validatorLogic) {
        return validation(self, new DelegatingProcessor(validatorLogic));
    }
    
    /**
     * Calls an endpoint specified by the URI and merges the original exchange
     * with the result of the call by applying the aggregation logic
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
     * Allows adding of extensions that replace existing extensions provided by Camel (e.g. 
     * {@code split})
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Splitter
     */
    public static IpfDefinition ipf(ProcessorDefinition self) {
        return new IpfDefinition(self);
    }

    /**
     * Adds a transmogrifier to the route for message transformation
     * @param transmogrifier
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifytransmogrifier    
     */
    public static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, Transmogrifier transmogrifier) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition(transmogrifier);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds a transmogrifier defined by a bean to the route
     * @param transmogrifierBeanName
     *          name of the bean implementing the transmogrifier
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifybean
     */
    public static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, String transmogrifierBeanName) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition(transmogrifierBeanName);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds a transmogrifier defined by a closure to the route
     * @param transmogrifierLogic
     *          a closure implementing the transmogrifier logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifyclosure
     */
    public static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, Closure transmogrifierLogic) {
        return transmogrify(self, new DelegatingTransmogrifier(transmogrifierLogic));
    }
    
    /**
     * Adds a transmogrifier to the route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Transmogrifier
     */
    public static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition((Transmogrifier)null);
        self.addOutput(answer);
        return answer;
    }        

    /**
     * Validates a message
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition();
        self.addOutput(answer);
        return answer;
    }

    /**
     * Validates a message using the validator implementation
     * @param validator
     *          the validator implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self, Validator validator) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition(validator);
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Validates a message using a validator bean
     * @param validatorBeanName
     *          the name of the bean implementing the validator
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate 
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self, String validatorBeanName) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition(validatorBeanName);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Validates a message using the validator logic
     * @param validatorLogic   
     *          a closure implementing the validator logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-validate 
     */
    public static ValidatorAdapterDefinition validate(ProcessorDefinition self, Closure validatorLogic) {
        return validate(self, new DelegatingValidator(validatorLogic));
    }

    /**
     * Parses a message to an internal format
     * @param parser
     *          the parser implementation 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-parse
     */
    public static ParserAdapterDefinition parse(ProcessorDefinition self, Parser parser) {
        ParserAdapterDefinition answer = new ParserAdapterDefinition(parser);
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Parses a message to an internal format using a bean
     * @param parserBeanName
     *          name of the bean implementing the parser
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-parse 
     */
    public static ParserAdapterDefinition parse(ProcessorDefinition self, String parserBeanName) {
        ParserAdapterDefinition answer = new ParserAdapterDefinition(parserBeanName);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Renders the message into an external representation
     * @param renderer
     *          the renderer implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-render
     */
    public static RendererAdapterDefinition render(ProcessorDefinition self, Renderer renderer) {
        RendererAdapterDefinition answer = new RendererAdapterDefinition(renderer);
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Renders the message into an external representation via the given bean
     * @param rendererBeanName  
     *          name of the bean implementing the renderer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-render
     */
    public static RendererAdapterDefinition render(ProcessorDefinition self, String rendererBeanName) {
        RendererAdapterDefinition answer = new RendererAdapterDefinition(rendererBeanName);
        self.addOutput(answer);
        return answer;
    }

    /**
     * Adds a routing decision for <a href="http://camel.apache.org/message-router.html">message routing</a> 
     * using the predicate logic
     * @param predicateLogic
     *          logic of the predicate
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-whenclosure
     */
    public static ChoiceDefinition when(ChoiceDefinition self, Closure predicateLogic) {
        return self.when(new DelegatingCamelPredicate(predicateLogic));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     * with a specific schema 
     * @param schemaResource
     *          the schema to use
     * @param namespaceAware
     *          {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self, String schemaResource, boolean namespaceAware) {
        return dataFormat(self, new GnodeDataFormat(schemaResource, namespaceAware));
    }

    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     * @param namespaceAware
     *          {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self, boolean namespaceAware) {
        return dataFormat(self, new GnodeDataFormat(namespaceAware));
    }
    
    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlParser.html">groovy.util.XmlParser</a>
     * (by default namespace aware)
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gnode
     */
    public static ProcessorDefinition gnode(DataFormatClause self) {
        return gnode(self, true);
    }
    
    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     * with a specific schema 
     * @param schemaResource
     *          the schema to use
     * @param namespaceAware
     *          {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self, String schemaResource, boolean namespaceAware) {
        return dataFormat(self, new GpathDataFormat(schemaResource, namespaceAware));
    }       
    
    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     * @param namespaceAware
     *          {@code true} to make use of XML namespaces
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self, boolean namespaceAware) {
        return dataFormat(self, new GpathDataFormat(namespaceAware));
    }
    
    /**
     * Unmarshal an XML stream or string using a 
     * <a href="http://groovy.codehaus.org/api/groovy/util/XmlSlurper.html">groovy.util.XmlSlurper</a>
     * (by default namespace aware)
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-gpath
     */
    public static ProcessorDefinition gpath(DataFormatClause self) {
        return gpath(self, true);
    }

    /**
     * Send exchange to a mock endpoint
     * @param endpointName
     *          endpoint name of the mock 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-TODO
     */
     public static ProcessorDefinition mock(ProcessorDefinition self, String endpointName) {
        return self.to("mock:${endpointName}");
    }
    

    /**
     * Retrieves the exception object from a handled exception in an exception route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Exceptionobjectsandmessages 
     */
    public static Object exceptionObject(ExpressionClause self) {
        return self.expression(Expressions.exceptionObjectExpression());
    }

    /**
     * Retrieves the exception message from a handled exception in an exception route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Exceptionobjectsandmessages
     */
    public static Object exceptionMessage(ExpressionClause self) {
        return self.expression(Expressions.exceptionMessageExpression());
    }
    
    /**
     * Allows for fine-grained <a href="http://camel.apache.org/exception-clause.html">exception handling</a>
     * @param predicate
     *          the predicate to check for the exception
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-onwhenclosure
     */
    public static OnExceptionDefinition onWhen(OnExceptionDefinition self, Closure predicate) {
        return self.onWhen(new DelegatingCamelPredicate(predicate));
    }

    /**
     * Defines a direct consumer endpoint
     * @param endpointName
     *          endpoint name of the direct consumer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features-TODO
     */
    public static RouteDefinition direct(RouteBuilder self, String endpointName) {
        return self.from("direct:${endpointName}")
    }
    
    /**
     * Defines an <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/processor/aggregate/AggregationStrategy.html">AggregationStrategy</a> 
     * via the {@code Aggregator} interface
     * @param aggregator
     *          the aggregator implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Aggregator
     */
    public static AggregatorAdapter aggregationStrategy(RouteBuilder self, Aggregator aggregator) {
        return new AggregatorAdapter(aggregator);
    }

    /**
     * Defines an <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/processor/aggregate/AggregationStrategy.html">AggregationStrategy</a>
     * via a bean 
     * @param aggregatorBeanName
     *          name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Aggregator
     */
    public static AggregatorAdapter aggregationStrategy(RouteBuilder self, String aggregatorBeanName) {
        return aggregationStrategy(self, self.lookup(aggregatorBeanName, Aggregator.class));
    }

    /**
     * Defines an <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/processor/aggregate/AggregationStrategy.html">AggregationStrategy</a>
     * via a closure 
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
     */
    public static PredicateAdapter predicate(RouteBuilder self, Closure predicateLogic) {
        return predicate(self, new DelegatingPredicate(predicateLogic));
    }
    
    /**
     * Parses a message to an internal format
     * @param parser
     *          the parser implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-UnmarshallingviaParser
     */
    public static ProcessorDefinition parse(DataFormatClause self, Parser parser) {        
        return self.processorType.unmarshal(new DataFormatAdapter((Parser)parser));
    }
    
    /**
     * Parses a message to an internal format using a bean
     * @param parserBeanName
     *          the name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-UnmarshallingviaParser
     */
    public static ProcessorDefinition parse(DataFormatClause self, String parserBeanName) {
        return self.processorType.unmarshal((DataFormatDefinition)DataFormatAdapterDefinition.forParserBean(parserBeanName));
    }

    /**
     * Renders the message into an external representation
     * @param renderer
     *          the implementation of the renderer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-MarshallingviaRenderer
     */
    public static ProcessorDefinition render(DataFormatClause self, Renderer renderer) {
        return self.processorType.marshal(new DataFormatAdapter((Renderer)renderer));
    }

    /**
     * Renders the message into an external representation via the given bean
     * @param rendererBeanName
     *          the name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-MarshallingviaRenderer
     */
    public static ProcessorDefinition render(DataFormatClause self, String rendererBeanName) {        
        return self.processorType.marshal((DataFormatDefinition)DataFormatAdapterDefinition.forRendererBean(rendererBeanName));
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


    /**
     * Combines "splitter" and "recipient list" EIPs: Generates a list
     * of N messages and a list of N endpoint URIs, sends each message
     * to its corresponding endpoint, and aggregates their responses.
     *
     * @param routeBuilder
     *      the current Camel route builder.
     * @param splittingExpression
     *      Camel expression which creates the list of messages.
     * @param recipientListExpression
     *      Camel expression which creates the list of target endpoint URIs.
     * @param aggregationStrategy
     *      strategy for aggregating received responses.
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features
     */
    public static ProcessorDefinition multiplast(
            ProcessorDefinition self,
            RouteBuilder routeBuilder,
            Expression splittingExpression,
            Expression recipientListExpression,
            AggregationStrategy aggregationStrategy)
    {
        String dispatcherEndpointUri = 'direct:multiplast-' + UUID.randomUUID().toString();
        routeBuilder.from(dispatcherEndpointUri)
            .process {
                int index = it.properties[Exchange.SPLIT_INDEX];
                it.in.headers['multiplast.uri'] = it.properties['multiplast.endpointUris'][index];
            }
            .recipientList(Builder.header('multiplast.uri'));

        return self.process {
            List bodies = splittingExpression.evaluate(it, List.class);
            List endpointUris = recipientListExpression.evaluate(it, List.class);
            if (bodies.size() != endpointUris.size()) {
                throw new RuntimeException('lists of bodies and endpoints must be of the same lenght');
            }

            it.in.body = bodies;
            it.properties['multiplast.endpointUris'] = endpointUris;
        }
        .split(Builder.body())
            .parallelProcessing()
            .aggregationStrategy(aggregationStrategy)
            .to(dispatcherEndpointUri)
            .end();
    }

}
