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
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.aggregate.AggregationStrategy
import org.openehealth.ipf.commons.core.modules.api.*

import java.util.concurrent.ExecutorService

/**
 * @author Martin Krasser
 *
 * @deprecated use Extension Modules
 */
class CoreModelExtension {
    
    RouteBuilder routeBuilder // for config backwards-compatibility only (not used internally)
    
    static extensions = { 
        
        // ----------------------------------------------------------------
        //  Core Extensions
        // ----------------------------------------------------------------

        ProcessorDefinition.metaClass.unhandled = { ProcessorDefinition processorDefinition ->
            CoreExtensionModule.unhandled(delegate, processorDefinition)
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
        //  Adapter Extensions for RouteBuilder
        // ----------------------------------------------------------------

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Aggregator aggregator ->
            CoreExtensionModule.aggregationStrategy(delegate, aggregator)
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
    
        ProcessorDefinition.metaClass.validate = { Validator validator ->
            CoreExtensionModule.validate(delegate, validator)
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
        //  Adapter Extensions for ProcessorDefinition
        // ----------------------------------------------------------------


        ProcessorDefinition.metaClass.verify = { ->
            CoreExtensionModule.verify(delegate)
        }

        ProcessorDefinition.metaClass.verify = { Validator validator ->
            CoreExtensionModule.verify(delegate, validator)
        }

        ProcessorDefinition.metaClass.verify = { String validatorBeanName ->
            CoreExtensionModule.verify(delegate, validatorBeanName)
        }

        ProcessorDefinition.metaClass.verify = { Closure validatorLogic ->
            CoreExtensionModule.verify(delegate, validatorLogic)
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
