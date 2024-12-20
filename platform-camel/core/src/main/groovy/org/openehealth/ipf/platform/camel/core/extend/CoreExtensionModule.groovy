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

package org.openehealth.ipf.platform.camel.core.extend


import org.apache.camel.AggregationStrategy
import org.apache.camel.Exchange
import org.apache.camel.Expression
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.ExpressionClause
import org.apache.camel.builder.NoErrorHandlerBuilder
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.DataFormatDefinition
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.model.RouteDefinition
import org.openehealth.ipf.commons.core.modules.api.*
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter
import org.openehealth.ipf.platform.camel.core.closures.DelegatingTransmogrifier
import org.openehealth.ipf.platform.camel.core.closures.DelegatingValidator
import org.openehealth.ipf.platform.camel.core.model.*
import org.openehealth.ipf.platform.camel.core.util.Expressions

import java.lang.reflect.Method
import java.util.concurrent.ExecutorService

import static org.apache.camel.builder.Builder.body

/**
 * Core DSL extensions for usage in a {@link RouteBuilder} using the {@code use} keyword.
 *
 *
 * @DSL
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
@SuppressWarnings("unused")
class CoreExtensionModule {

    // Groovy-specific data formats

    static ProcessorDefinition gnode(DataFormatClause self, boolean namespaceAware) {
        dataFormatFor(self, new DataFormatDefinition(new XmlParserDataFormat(namespaceAware)))
    }

    static ProcessorDefinition gnode(DataFormatClause self) {
        gnode(self, true)
    }

    static ProcessorDefinition gpath(DataFormatClause self, boolean namespaceAware) {
        dataFormatFor(self, new DataFormatDefinition(new XmlSlurperDataFormat(namespaceAware)))
    }

    static ProcessorDefinition gpath(DataFormatClause self) {
        gpath(self, true)
    }


    /**
     * Drops the <a href="http://camel.apache.org/error-handler.html">error handler</a> 
     * from the route by using the {@code noErrorHandler}
     *
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-unhandled
     */
    static RouteDefinition unhandled(RouteDefinition self) {
        self.errorHandler(new NoErrorHandlerBuilder())
    }


    /**
     * Adds a transmogrifier to the route for message transformation
     * @param transmogrifier
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifytransmogrifier    
     */
    static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, Transmogrifier transmogrifier) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition(transmogrifier)
        self.addOutput(answer)
        answer
    }

    /**
     * Adds a transmogrifier defined by a bean to the route
     * @param transmogrifierBeanName
     *          name of the bean implementing the transmogrifier
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifybean
     */
    static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, String transmogrifierBeanName) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition(transmogrifierBeanName)
        self.addOutput(answer)
        answer
    }

    /**
     * Adds a transmogrifier defined by a closure to the route
     * @param transmogrifierLogic
     *          a closure implementing the transmogrifier logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-transmogrifyclosure
     */
    static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self, Closure transmogrifierLogic) {
        transmogrify(self, new DelegatingTransmogrifier(transmogrifierLogic))
    }

    /**
     * Adds a transmogrifier to the route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Transmogrifier
     */
    static TransmogrifierAdapterDefinition transmogrify(ProcessorDefinition self) {
        TransmogrifierAdapterDefinition answer = new TransmogrifierAdapterDefinition((Transmogrifier) null)
        self.addOutput(answer)
        answer
    }

    /**
     * Validates a message using the validator implementation, throwing a ValidationException
     * if validation fails.
     *
     * @param validator
     *          the validator implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-verify
     */
    static ValidatorAdapterDefinition verify(ProcessorDefinition self, Validator validator) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition(validator)
        self.addOutput(answer)
        answer
    }

    /**
     * Validates a message
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-verify
     */
    static ValidatorAdapterDefinition verify(ProcessorDefinition self) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition()
        self.addOutput(answer)
        answer
    }


    /**
     * Validates a message using a validator bean
     * @param validatorBeanName
     *          the name of the bean implementing the validator
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-verify
     */
    static ValidatorAdapterDefinition verify(ProcessorDefinition self, String validatorBeanName) {
        ValidatorAdapterDefinition answer = new ValidatorAdapterDefinition(validatorBeanName)
        self.addOutput(answer)
        answer
    }

    /**
     * Validates a message using the validator logic
     *
     * Note: conflicts with camel-groovy
     *
     * @param validatorLogic
     *          a closure implementing the validator logic
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-verify
     */
    static ValidatorAdapterDefinition verify(ProcessorDefinition self, Closure<Boolean> validatorLogic) {
        verify(self, new DelegatingValidator(validatorLogic))
    }

    /**
     * Parses a message to an internal format
     * @param parser
     *          the parser implementation 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-parse
     */
    static ParserAdapterDefinition parse(ProcessorDefinition self, Parser parser) {
        ParserAdapterDefinition answer = new ParserAdapterDefinition(parser)
        self.addOutput(answer)
        answer
    }

    /**
     * Parses a message to an internal format using a bean
     * @param parserBeanName
     *          name of the bean implementing the parser
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-parse 
     */
    static ParserAdapterDefinition parse(ProcessorDefinition self, String parserBeanName) {
        ParserAdapterDefinition answer = new ParserAdapterDefinition(parserBeanName)
        self.addOutput(answer)
        answer
    }

    /**
     * Renders the message into an external representation
     * @param renderer
     *          the renderer implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-render
     */
    static RendererAdapterDefinition render(ProcessorDefinition self, Renderer renderer) {
        RendererAdapterDefinition answer = new RendererAdapterDefinition(renderer)
        self.addOutput(answer)
        answer
    }

    /**
     * Renders the message into an external representation via the given bean
     * @param rendererBeanName
     *          name of the bean implementing the renderer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-render
     */
    static RendererAdapterDefinition render(ProcessorDefinition self, String rendererBeanName) {
        RendererAdapterDefinition answer = new RendererAdapterDefinition(rendererBeanName)
        self.addOutput(answer)
        answer
    }

    /**
     * Send exchange to a mock endpoint
     * @param endpointName
     *          endpoint name of the mock 
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-mock
     */
    static ProcessorDefinition mock(ProcessorDefinition self, String endpointName) {
        self.to("mock:${endpointName}")
    }

    /**
     * Retrieves the exception object from a handled exception in an exception route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Exceptionobjectsandmessages 
     */
    static Object exceptionObject(ExpressionClause self) {
        self.expression(Expressions.exceptionObjectExpression())
    }

    /**
     * Retrieves the exception message from a handled exception in an exception route
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Exceptionobjectsandmessages
     */
    static Object exceptionMessage(ExpressionClause self) {
        self.expression(Expressions.exceptionMessageExpression())
    }


    /**
     * Defines a direct consumer endpoint
     * @param endpointName
     *          endpoint name of the direct consumer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features-direct
     */
    static RouteDefinition direct(RouteBuilder self, String endpointName) {
        self.from("direct:${endpointName}")
    }

    /**
     * Defines an <a href="http://camel.apache.org/maven/camel-core/apidocs/org/apache/camel/processor/aggregate/AggregationStrategy.html">AggregationStrategy</a> 
     * via the {@code Aggregator} interface
     * @param aggregator
     *          the aggregator implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Aggregator
     */
    static AggregatorAdapter aggregationStrategy(RouteBuilder self, Aggregator aggregator) {
        new AggregatorAdapter(aggregator)
    }


    /**
     * Parses a message to an internal format
     * @param parser
     *          the parser implementation
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-UnmarshallingviaParser
     */
    static ProcessorDefinition parse(DataFormatClause self, Parser parser) {
        self.processorType.unmarshal(new DataFormatAdapter((Parser) parser))
    }

    /**
     * Parses a message to an internal format using a bean
     * @param parserBeanName
     *          the name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-UnmarshallingviaParser
     */
    static ProcessorDefinition parse(DataFormatClause self, String parserBeanName) {
        self.processorType.unmarshal((DataFormatDefinition) DataFormatAdapterDefinition.forParserBean(parserBeanName))
    }

    /**
     * Renders the message into an external representation
     * @param renderer
     *          the implementation of the renderer
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-MarshallingviaRenderer
     */
    static ProcessorDefinition render(DataFormatClause self, Renderer renderer) {
        self.processorType.marshal(new DataFormatAdapter((Renderer) renderer))
    }

    /**
     * Renders the message into an external representation via the given bean
     * @param rendererBeanName
     *          the name of the bean
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-MarshallingviaRenderer
     */
    static ProcessorDefinition render(DataFormatClause self, String rendererBeanName) {
        self.processorType.marshal((DataFormatDefinition) DataFormatAdapterDefinition.forRendererBean(rendererBeanName))
    }


    // DataFormatClause.dataFormat(DataFormatDefinition) is private...
    static ProcessorDefinition dataFormatFor(DataFormatClause self, DataFormatDefinition format) {
        try {
            Method m = self.getClass().getDeclaredMethod("dataFormat", DataFormatDefinition.class)
            m.setAccessible(true)
            (ProcessorDefinition) m.invoke(self, format)
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown DataFormat operation", e)
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
    static ProcessorDefinition multiplast(
            ProcessorDefinition self,
            RouteBuilder routeBuilder,
            Expression splittingExpression,
            Expression recipientListExpression,
            AggregationStrategy aggregationStrategy,
            ExecutorService executorService = null) {
        String uuid = UUID.randomUUID().toString()
        String dispatcherEndpointUri = 'direct:multiplast-' + uuid
        routeBuilder.from(dispatcherEndpointUri)
            .process {
                int index = it.getProperty(Exchange.SPLIT_INDEX, Integer.class)
                it.in.headers['multiplast.uri'] = it.properties['multiplast.endpointUris'][index]
            }
            .recipientList().header('multiplast.uri')

        def fromNode = routeBuilder.from('direct:split-execution-' + uuid)
                .split(body())
                .parallelProcessing()
        if (executorService) {
            fromNode = fromNode.executorService(executorService)
        }
        fromNode
                .aggregationStrategy(aggregationStrategy)
                .to(dispatcherEndpointUri)
                .end()

        self.process {
            List bodies = splittingExpression.evaluate(it, List.class)
            List endpointUris = recipientListExpression.evaluate(it, List.class)
            if (bodies.size() != endpointUris.size()) {
                throw new RuntimeException('lists of bodies and endpoints must be of the same length')
            }
            it.in.body = bodies
            it.properties['multiplast.endpointUris'] = endpointUris
        }
        .to('direct:split-execution-' + uuid)
    }


}
