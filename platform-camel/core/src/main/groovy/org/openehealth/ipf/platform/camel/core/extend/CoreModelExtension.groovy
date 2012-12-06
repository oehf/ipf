/*
 * Copyright 2008-2009 the original author or authors.
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

import org.apache.camel.Expression
import org.apache.camel.Processor
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.ExpressionClause
import org.apache.camel.model.ChoiceDefinition
import org.apache.camel.model.OnExceptionDefinition
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.DelegateProcessor
import org.apache.camel.processor.aggregate.AggregationStrategy
import org.openehealth.ipf.commons.core.modules.api.*
import org.apache.camel.builder.RouteBuilder
import java.util.concurrent.ExecutorService

/**
 * @author Martin Krasser
 */
class CoreModelExtension {
    
    RouteBuilder routeBuilder // for config backwards-compatibility only (not used internally)
    
    static extensions = { 
        
        // ----------------------------------------------------------------
        //  Core Extensions
        // ----------------------------------------------------------------

        ProcessorDefinition.metaClass.process = { String processorBeanName ->
            CoreExtension.process(delegate, processorBeanName)
        }

        ProcessorDefinition.metaClass.process = { Closure processorLogic ->
            CoreExtension.process(delegate, processorLogic)
        }
            
        ProcessorDefinition.metaClass.intercept = {DelegateProcessor delegateProcessor ->
            CoreExtension.intercept(delegate, delegateProcessor)
        }

        ProcessorDefinition.metaClass.intercept = { Closure interceptorLogic ->
            CoreExtension.intercept(delegate, interceptorLogic)
        }

        ProcessorDefinition.metaClass.intercept = { String interceptorBean ->
            CoreExtension.intercept(delegate, interceptorBean)
        }
        
        ProcessorDefinition.metaClass.unhandled = { ProcessorDefinition processorDefinition ->
            CoreExtension.unhandled(delegate, processorDefinition)
        }
        
        ProcessorDefinition.metaClass.filter = { Closure predicateLogic ->
            CoreExtension.filter(delegate, predicateLogic)
        }
        
        ProcessorDefinition.metaClass.transform = { Closure transformExpression ->
            CoreExtension.transform(delegate, transformExpression)
        }
    
        ProcessorDefinition.metaClass.setExchangeProperty = { String name, Closure propertyExpression ->
            CoreExtension.setExchangeProperty(delegate, name, propertyExpression)
        }

        ProcessorDefinition.metaClass.setHeader = { String name, Closure headerExpression ->
            CoreExtension.setHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setOutHeader = { String name, Closure headerExpression ->
            CoreExtension.setOutHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setFaultHeader = { String name, Closure headerExpression ->
            CoreExtension.setFaultHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setBody = {Closure bodyExpression ->
            CoreExtension.setBody(delegate, bodyExpression)
        }
        
        ChoiceDefinition.metaClass.when = { Closure predicateLogic ->
            CoreExtension.when(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Platform Processor Extensions
        // ----------------------------------------------------------------
        
        ProcessorDefinition.metaClass.validation = { Processor validator ->
            CoreExtension.validation(delegate, validator)
        }

        ProcessorDefinition.metaClass.validation = { String validationUri ->
            CoreExtension.validation(delegate, validationUri)
        }
        
        ProcessorDefinition.metaClass.validation = { Closure validatorLogic ->
            CoreExtension.validation(delegate, validatorLogic)
        }
            
        ProcessorDefinition.metaClass.enrich = { String resourceUri, Closure aggregationLogic ->
            CoreExtension.enrich(delegate, resourceUri, aggregationLogic)
        }
    
        ProcessorDefinition.metaClass.ipf = { ->
            CoreExtension.ipf(delegate)
	    }
                
         // ----------------------------------------------------------------
         //  Platform DataFormatClause extensions
         // ----------------------------------------------------------------
         
         DataFormatClause.metaClass.gnode = { String schemaResource, boolean namespaceAware ->
             CoreExtension.gnode(delegate, schemaResource, namespaceAware)
         }
        
         DataFormatClause.metaClass.gnode = { boolean namespaceAware ->
             CoreExtension.gnode(delegate, namespaceAware)
         }
     
         DataFormatClause.metaClass.gnode = { ->
             CoreExtension.gnode(delegate)
         }

         DataFormatClause.metaClass.gpath = { String schemaResource, boolean namespaceAware ->
             CoreExtension.gpath(delegate, schemaResource, namespaceAware)
         }       
        
         DataFormatClause.metaClass.gpath = { boolean namespaceAware ->
             CoreExtension.gpath(delegate, namespaceAware)
         }

         DataFormatClause.metaClass.gpath = { ->
             CoreExtension.gpath(delegate)
         }
     
         // ----------------------------------------------------------------
         //  Platform ExpressionClause extensions
         // ----------------------------------------------------------------
         
         ExpressionClause.metaClass.exceptionObject = { ->
             CoreExtension.exceptionObject(delegate)
         }
  
         ExpressionClause.metaClass.exceptionMessage = { ->
             CoreExtension.exceptionMessage(delegate)
         }
  
        // ----------------------------------------------------------------
        //  Platform ExceptionDefinition extensions
        // ----------------------------------------------------------------
        
        OnExceptionDefinition.metaClass.onWhen = { Closure predicate ->
            CoreExtension.onWhen(delegate, predicate)
        }

        // ----------------------------------------------------------------
        //  Adapter Extensions for RouteBuilder
        // ----------------------------------------------------------------

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Aggregator aggregator ->
            CoreExtension.aggregationStrategy(delegate, aggregator)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { String aggregatorBeanName ->
            CoreExtension.aggregationStrategy(delegate, aggregatorBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Closure aggregationLogic ->
            CoreExtension.aggregationStrategy(delegate, aggregationLogic)
        }
        
        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Predicate predicate ->
            CoreExtension.predicate(delegate, predicate)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { String predicateBeanName ->
            CoreExtension.predicate(delegate, predicateBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Closure predicateLogic ->
            CoreExtension.predicate(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for ProcessorDefinition
        // ----------------------------------------------------------------
        
        ProcessorDefinition.metaClass.transmogrify = { Transmogrifier transmogrifier ->
            CoreExtension.transmogrify(delegate, transmogrifier)
        }

        ProcessorDefinition.metaClass.transmogrify = { String transmogrifierBeanName ->
            CoreExtension.transmogrify(delegate, transmogrifierBeanName)
        }

        ProcessorDefinition.metaClass.transmogrify = { Closure transmogrifierLogic ->
            CoreExtension.transmogrify(delegate, transmogrifierLogic)
        }
        
        ProcessorDefinition.metaClass.transmogrify = { ->
            CoreExtension.transmogrify(delegate)
        }        

        ProcessorDefinition.metaClass.validate = { ->
            CoreExtension.validate(delegate)
        }
    
        ProcessorDefinition.metaClass.validate = { Validator validator ->
            CoreExtension.validate(delegate, validator)
        }
        
        ProcessorDefinition.metaClass.validate = { String validatorBeanName ->
            CoreExtension.validate(delegate, validatorBeanName)
        }
    
        ProcessorDefinition.metaClass.validate = { Closure validatorLogic ->
            CoreExtension.validate(delegate, validatorLogic)
        }
    
        ProcessorDefinition.metaClass.parse = { Parser parser ->
            CoreExtension.parse(delegate, parser)
        }
        
        ProcessorDefinition.metaClass.parse = { String parserBeanName ->
            CoreExtension.parse(delegate, parserBeanName)
        }
    
        ProcessorDefinition.metaClass.render = { Renderer renderer ->
            CoreExtension.render(delegate, renderer)
        }
        
        ProcessorDefinition.metaClass.render = { String rendererBeanName ->
            CoreExtension.render(delegate, rendererBeanName)
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for DataFormatClause
        // ----------------------------------------------------------------

        DataFormatClause.metaClass.parse = { Parser parser ->
            CoreExtension.parse(delegate, parser)
        }
        
        DataFormatClause.metaClass.render = { Renderer renderer ->
            CoreExtension.render(delegate, renderer)
        }
    
        DataFormatClause.metaClass.parse = { String parserBeanName ->
            CoreExtension.parse(delegate, parserBeanName)
        }
    
        DataFormatClause.metaClass.render = { String rendererBeanName ->
            CoreExtension.render(delegate, rendererBeanName)
        }

        // ----------------------------------------------------------------
        //  Multiplast = Splitter + Multicast
        // ----------------------------------------------------------------

        ProcessorDefinition.metaClass.multiplast = {
            RouteBuilder routeBuilder,
            Expression splittingExpression,
            Expression recipientListExpression,
            AggregationStrategy aggregationStrategy,
            ExecutorService executorService = null ->

            return CoreExtension.multiplast(
                    delegate,
                    routeBuilder,
                    splittingExpression,
                    recipientListExpression,
                    aggregationStrategy,
                    executorService)
        }

    }
}
