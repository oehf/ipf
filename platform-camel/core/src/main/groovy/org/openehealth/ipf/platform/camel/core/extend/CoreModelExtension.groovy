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
            CoreExtensionModule.process(delegate, processorBeanName)
        }

        ProcessorDefinition.metaClass.process = { Closure processorLogic ->
            CoreExtensionModule.process(delegate, processorLogic)
        }
            
        ProcessorDefinition.metaClass.intercept = {DelegateProcessor delegateProcessor ->
            CoreExtensionModule.intercept(delegate, delegateProcessor)
        }

        ProcessorDefinition.metaClass.intercept = { Closure interceptorLogic ->
            CoreExtensionModule.intercept(delegate, interceptorLogic)
        }

        ProcessorDefinition.metaClass.intercept = { String interceptorBean ->
            CoreExtensionModule.intercept(delegate, interceptorBean)
        }
        
        ProcessorDefinition.metaClass.unhandled = { ProcessorDefinition processorDefinition ->
            CoreExtensionModule.unhandled(delegate, processorDefinition)
        }
        
        ProcessorDefinition.metaClass.filter = { Closure predicateLogic ->
            CoreExtensionModule.filter(delegate, predicateLogic)
        }
        
        ProcessorDefinition.metaClass.transform = { Closure transformExpression ->
            CoreExtensionModule.transform(delegate, transformExpression)
        }
    
        ProcessorDefinition.metaClass.setExchangeProperty = { String name, Closure propertyExpression ->
            CoreExtensionModule.setExchangeProperty(delegate, name, propertyExpression)
        }

        ProcessorDefinition.metaClass.setHeader = { String name, Closure headerExpression ->
            CoreExtensionModule.setHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setOutHeader = { String name, Closure headerExpression ->
            CoreExtensionModule.setOutHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setFaultHeader = { String name, Closure headerExpression ->
            CoreExtensionModule.setFaultHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setBody = {Closure bodyExpression ->
            CoreExtensionModule.setBody(delegate, bodyExpression)
        }
        
        ChoiceDefinition.metaClass.when = { Closure predicateLogic ->
            CoreExtensionModule.when(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Platform Processor Extensions
        // ----------------------------------------------------------------
        
        ProcessorDefinition.metaClass.validation = { Processor validator ->
            CoreExtensionModule.validation(delegate, validator)
        }

        ProcessorDefinition.metaClass.validation = { String validationUri ->
            CoreExtensionModule.validation(delegate, validationUri)
        }
        
        ProcessorDefinition.metaClass.validation = { Closure validatorLogic ->
            CoreExtensionModule.validation(delegate, validatorLogic)
        }
            
        ProcessorDefinition.metaClass.enrich = { String resourceUri, Closure aggregationLogic ->
            CoreExtensionModule.enrich(delegate, resourceUri, aggregationLogic)
        }
    
        ProcessorDefinition.metaClass.ipf = { ->
            CoreExtensionModule.ipf(delegate)
	    }
                
         // ----------------------------------------------------------------
         //  Platform DataFormatClause extensions
         // ----------------------------------------------------------------
         
         DataFormatClause.metaClass.gnode = { String schemaResource, boolean namespaceAware ->
             CoreExtensionModule.gnode(delegate, schemaResource, namespaceAware)
         }
        
         DataFormatClause.metaClass.gnode = { boolean namespaceAware ->
             CoreExtensionModule.gnode(delegate, namespaceAware)
         }
     
         DataFormatClause.metaClass.gnode = { ->
             CoreExtensionModule.gnode(delegate)
         }

         DataFormatClause.metaClass.gpath = { String schemaResource, boolean namespaceAware ->
             CoreExtensionModule.gpath(delegate, schemaResource, namespaceAware)
         }       
        
         DataFormatClause.metaClass.gpath = { boolean namespaceAware ->
             CoreExtensionModule.gpath(delegate, namespaceAware)
         }

         DataFormatClause.metaClass.gpath = { ->
             CoreExtensionModule.gpath(delegate)
         }
     
         // ----------------------------------------------------------------
         //  Platform ExpressionClause extensions
         // ----------------------------------------------------------------
         
         ExpressionClause.metaClass.exceptionObject = { ->
             CoreExtensionModule.exceptionObject(delegate)
         }
  
         ExpressionClause.metaClass.exceptionMessage = { ->
             CoreExtensionModule.exceptionMessage(delegate)
         }
  
        // ----------------------------------------------------------------
        //  Platform ExceptionDefinition extensions
        // ----------------------------------------------------------------
        
        OnExceptionDefinition.metaClass.onWhen = { Closure predicate ->
            CoreExtensionModule.onWhen(delegate, predicate)
        }

        // ----------------------------------------------------------------
        //  Adapter Extensions for RouteBuilder
        // ----------------------------------------------------------------

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Aggregator aggregator ->
            CoreExtensionModule.aggregationStrategy(delegate, aggregator)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { String aggregatorBeanName ->
            CoreExtensionModule.aggregationStrategy(delegate, aggregatorBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Closure aggregationLogic ->
            CoreExtensionModule.aggregationStrategy(delegate, aggregationLogic)
        }
        
        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Predicate predicate ->
            CoreExtensionModule.predicate(delegate, predicate)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { String predicateBeanName ->
            CoreExtensionModule.predicate(delegate, predicateBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Closure predicateLogic ->
            CoreExtensionModule.predicate(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for ProcessorDefinition
        // ----------------------------------------------------------------
        
        ProcessorDefinition.metaClass.transmogrify = { Transmogrifier transmogrifier ->
            CoreExtensionModule.transmogrify(delegate, transmogrifier)
        }

        ProcessorDefinition.metaClass.transmogrify = { String transmogrifierBeanName ->
            CoreExtensionModule.transmogrify(delegate, transmogrifierBeanName)
        }

        ProcessorDefinition.metaClass.transmogrify = { Closure transmogrifierLogic ->
            CoreExtensionModule.transmogrify(delegate, transmogrifierLogic)
        }
        
        ProcessorDefinition.metaClass.transmogrify = { ->
            CoreExtensionModule.transmogrify(delegate)
        }        

        ProcessorDefinition.metaClass.validate = { ->
            CoreExtensionModule.validate(delegate)
        }
    
        ProcessorDefinition.metaClass.validate = { Validator validator ->
            CoreExtensionModule.validate(delegate, validator)
        }
        
        ProcessorDefinition.metaClass.validate = { String validatorBeanName ->
            CoreExtensionModule.validate(delegate, validatorBeanName)
        }
    
        ProcessorDefinition.metaClass.validate = { Closure validatorLogic ->
            CoreExtensionModule.validate(delegate, validatorLogic)
        }
    
        ProcessorDefinition.metaClass.parse = { Parser parser ->
            CoreExtensionModule.parse(delegate, parser)
        }
        
        ProcessorDefinition.metaClass.parse = { String parserBeanName ->
            CoreExtensionModule.parse(delegate, parserBeanName)
        }
    
        ProcessorDefinition.metaClass.render = { Renderer renderer ->
            CoreExtensionModule.render(delegate, renderer)
        }
        
        ProcessorDefinition.metaClass.render = { String rendererBeanName ->
            CoreExtensionModule.render(delegate, rendererBeanName)
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for DataFormatClause
        // ----------------------------------------------------------------

        DataFormatClause.metaClass.parse = { Parser parser ->
            CoreExtensionModule.parse(delegate, parser)
        }
        
        DataFormatClause.metaClass.render = { Renderer renderer ->
            CoreExtensionModule.render(delegate, renderer)
        }
    
        DataFormatClause.metaClass.parse = { String parserBeanName ->
            CoreExtensionModule.parse(delegate, parserBeanName)
        }
    
        DataFormatClause.metaClass.render = { String rendererBeanName ->
            CoreExtensionModule.render(delegate, rendererBeanName)
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

            return CoreExtensionModule.multiplast(
                    delegate,
                    routeBuilder,
                    splittingExpression,
                    recipientListExpression,
                    aggregationStrategy,
                    executorService)
        }

    }
}
