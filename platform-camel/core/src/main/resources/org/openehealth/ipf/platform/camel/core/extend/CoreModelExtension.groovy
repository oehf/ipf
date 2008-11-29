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
package org.openehealth.ipf.platform.camel.core.extend

import static org.openehealth.ipf.platform.camel.core.util.Expressions.headersExpression

import static org.apache.camel.builder.DataFormatClause.Operation.Marshal;
import static org.apache.camel.builder.DataFormatClause.Operation.Unmarshal;

import org.openehealth.ipf.commons.core.modules.api.Parser
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openehealth.ipf.platform.camel.core.adapter.AggregatorAdapter
import org.openehealth.ipf.platform.camel.core.adapter.Adapter
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilder
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregator
import org.openehealth.ipf.platform.camel.core.closures.DelegatingAggregationStrategy
import org.openehealth.ipf.platform.camel.core.closures.DelegatingCamelPredicate
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression
import org.openehealth.ipf.platform.camel.core.closures.DelegatingInterceptor
import org.openehealth.ipf.platform.camel.core.closures.DelegatingPredicate
import org.openehealth.ipf.platform.camel.core.closures.DelegatingProcessor
import org.openehealth.ipf.platform.camel.core.closures.DelegatingTransmogrifier 
import org.openehealth.ipf.platform.camel.core.closures.DelegatingValidator 
import org.openehealth.ipf.platform.camel.core.dataformat.GnodeDataFormat
import org.openehealth.ipf.platform.camel.core.dataformat.GpathDataFormat
import org.openehealth.ipf.platform.camel.core.model.SplitterType
import org.openehealth.ipf.platform.camel.core.model.ParserAdapterType
import org.openehealth.ipf.platform.camel.core.model.RendererAdapterType
import org.openehealth.ipf.platform.camel.core.model.TransmogrifierAdapterType
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterType

import org.apache.camel.Expression
import org.apache.camel.Processor
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.ChoiceType
import org.apache.camel.model.ProcessorType
import org.apache.camel.processor.aggregate.AggregationStrategy
import org.apache.camel.spi.DataFormat

/**
 * @author Martin Krasser
 */
class CoreModelExtension {
    
    RouteBuilder routeBuilder
    
    def extensions = { 
            
        // ----------------------------------------------------------------
        //  Core Extensions
        // ----------------------------------------------------------------
            
        ProcessorType.metaClass.process = { Closure processorLogic ->
            delegate.process(new DelegatingProcessor(processorLogic))
        }
            
        ProcessorType.metaClass.intercept = { Closure interceptorLogic ->
            delegate.intercept(new DelegatingInterceptor(interceptorLogic))
        }
        
        ProcessorType.metaClass.unhandled = { ProcessorType processorType ->
            delegate.errorHandler(routeBuilder.noErrorHandler())
        }
        
        ProcessorType.metaClass.filter = { Closure predicateLogic ->
            delegate.filter(new DelegatingCamelPredicate(predicateLogic))
        }
        
        ProcessorType.metaClass.setHeader = { String name, Closure headerExpression ->
            delegate.setHeader(name, new DelegatingExpression(headerExpression))
        }

        ProcessorType.metaClass.setOutHeader = { String name, Closure headerExpression ->
            delegate.setOutHeader(name, new DelegatingExpression(headerExpression))
        }

        ProcessorType.metaClass.setFaultHeader = { String name, Closure headerExpression ->
            delegate.setFaultHeader(name, new DelegatingExpression(headerExpression))
        }

        ChoiceType.metaClass.when = { Closure predicateLogic ->
            delegate.when(new DelegatingCamelPredicate(predicateLogic))
        }
    
        // ----------------------------------------------------------------
        //  Platform Processor Extensions
        // ----------------------------------------------------------------
        
        /**
         * Defines the <code>enrich</code> DSL extension that uses an
         * {@link AggregationStrategy} object to define enricher-merge logic.
         */
        ProcessorType.metaClass.enrich = {String resourceUri, AggregationStrategy aggregationStrategy ->
            delegate.process(routeBuilder.enricher(aggregationStrategy, resourceUri))
        }
        
        /**
         * Defines the <code>enrich</code> DSL extension that uses a
         * {@link Closure} implementing enricher-merge logic. Closures define 
         * two parameters, an {@link Exchange} parameter for the original 
         * exchange and another {@link Exchange} parameter used for the 
         * communication with the enricher resource. If the closure returns an
         * {@link Exchange} result it is used as the enricher-merge result, 
         * otherwise, the first {@link Exchange} argument is used as result.
         */
        ProcessorType.metaClass.enrich = { String resourceUri, Closure aggregationLogic ->
            delegate.process(routeBuilder.enricher(new DelegatingAggregationStrategy(aggregationLogic), resourceUri))
        }
        
        /**
         * Defines the <code>validation</code> DSL extension for running a
         * validation process. The process uses a {@link Processor} to perform
         * the actual validation.
         */
        ProcessorType.metaClass.validation = { Processor validator ->
            delegate.intercept(routeBuilder.validation(validator))
        }

        /**
         * Defines the <code>validation</code> DSL extension for running a
         * validation process. The process delegates the actual validation to 
         * an endpoint identified by <code>validationUri</code>.
         */
        ProcessorType.metaClass.validation = { String validationUri ->
            delegate.intercept(routeBuilder.validation(validationUri))
        }
        
        /**
         * Defines the <code>validation</code> DSL extension for running a
         * validation process. The process uses a {@link Closure} implementing
         * validation logic to perform the actual validation. Closures define 
         * an {@link Exchange} parameter. Optional validation responses must be
         * set on the exchange's out-message. If validation fails the closure 
         * must either throw an exception or set the exchange's fault-message.
         * Values returned by the closure are ignored.
         */
        ProcessorType.metaClass.validation = { Closure validatorLogic ->
            delegate.intercept(routeBuilder.validation(new DelegatingProcessor(validatorLogic)))
        }
    
        /**
         * Defines the <code>inOnly</code> DSL extension for converting the 
         * current exchange to an in-only exchange.
         */
        ProcessorType.metaClass.inOnly = { ->
            delegate.intercept(routeBuilder.inOnlyBridge())
        }
        
        ProcessorType.metaClass.split = { Expression expression -> 
            SplitterType answer = new SplitterType(expression)        
            delegate.addOutput(answer)
            answer
        }
    
        ProcessorType.metaClass.split = { String expressionLogicBean -> 
            delegate.split(routeBuilder.bean(Expression.class, expressionLogicBean))        
        }        
    
        ProcessorType.metaClass.split = { Closure expressionLogic -> 
            delegate.split(new DelegatingExpression(expressionLogic))        
        }
        
         // ----------------------------------------------------------------
         //  Platform DataFormatClause extensions
         // ----------------------------------------------------------------
         
         DataFormatClause.metaClass.gnode = { boolean namespaceAware ->
             delegate.dataFormat(new GnodeDataFormat(namespaceAware))
         }
     
         DataFormatClause.metaClass.gnode = { ->
             delegate.gnode(true)
         }
     
         DataFormatClause.metaClass.gpath = { boolean namespaceAware ->
             delegate.dataFormat(new GpathDataFormat(namespaceAware))
         }

         DataFormatClause.metaClass.gpath = { ->
             delegate.gpath(true)
         }
     
        // ----------------------------------------------------------------
        //  Adapter Extensions for RouteBuilder
        // ----------------------------------------------------------------

        RouteBuilder.metaClass.aggregationStrategy = { Closure aggregationLogic ->
            delegate.aggregationStrategy(new DelegatingAggregator(aggregationLogic))
        }
        
        RouteBuilder.metaClass.predicate = { Closure predicateLogic ->
            delegate.predicate(new DelegatingPredicate(predicateLogic))
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for ProcessorType
        // ----------------------------------------------------------------
    
        ProcessorType.metaClass.transmogrify = { Transmogrifier transmogrifier ->
            TransmogrifierAdapterType answer = new TransmogrifierAdapterType(transmogrifier)
            delegate.addOutput(answer)
            return answer
        }

        ProcessorType.metaClass.transmogrify = { String transmogrifierBeanName ->
            delegate.transmogrify(routeBuilder.bean(Transmogrifier.class, transmogrifierBeanName))
        }

        ProcessorType.metaClass.transmogrify = { Closure closure ->
            delegate.transmogrify(new DelegatingTransmogrifier(closure))
        }

        ProcessorType.metaClass.validate = { Validator validator ->
            ValidatorAdapterType answer = new ValidatorAdapterType(validator)
            delegate.addOutput(answer)
            return answer
        }
        
        ProcessorType.metaClass.validate = { String validatorBeanName ->
            delegate.validate(routeBuilder.bean(Validator.class, validatorBeanName))
        }
        
        ProcessorType.metaClass.validate = { Closure validatorLogic ->
            delegate.validate(new DelegatingValidator(validatorLogic))
        }
    
        ProcessorType.metaClass.parse = { Parser parser ->
            ParserAdapterType answer = new ParserAdapterType(parser)
            delegate.addOutput(answer)
            return answer
        }
        
        ProcessorType.metaClass.parse = { String parserBeanName ->
            delegate.parse(routeBuilder.bean(Parser.class, parserBeanName))
        }
    
        ProcessorType.metaClass.render = { Renderer renderer ->
            RendererAdapterType answer = new RendererAdapterType(renderer)
            delegate.addOutput(answer)
            return answer
        }
        
        ProcessorType.metaClass.render = { String rendererBeanName ->
            delegate.render(routeBuilder.bean(Renderer.class, rendererBeanName))
        }

        // ----------------------------------------------------------------
        //  Adapter Extensions for DataFormatClause
        // ----------------------------------------------------------------

        DataFormatClause.metaClass.parse = { Parser parser ->
            delegate.processorType.unmarshal(routeBuilder.dataFormatParser(parser))
        }
        
        DataFormatClause.metaClass.parse = { String parserBeanName ->
            delegate.processorType.unmarshal(routeBuilder.dataFormatParser(parserBeanName))
        }
    
        DataFormatClause.metaClass.render = { Renderer renderer ->
            delegate.processorType.marshal(routeBuilder.dataFormatRenderer(renderer))
        }
    
        DataFormatClause.metaClass.render = { String rendererBeanName ->
            delegate.processorType.marshal(routeBuilder.dataFormatRenderer(rendererBeanName))
        }

        // ----------------------------------------------------------------
        //  Not part of DSL
        // ----------------------------------------------------------------

        DataFormatClause.metaClass.dataFormat = { DataFormat dataFormat ->
            if (operation == Marshal) {
                return delegate.processorType.marshal(dataFormat)
            } else if (operation == Unmarshal) {
                return delegate.processorType.unmarshal(dataFormat)
            } else {
                throw new IllegalArgumentException("Unknown data format operation: " + operation)
            }
        }
        
    }
        
}