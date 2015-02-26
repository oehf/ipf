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

import org.apache.camel.Expression
import org.apache.camel.Processor
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.ExpressionClause
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.ChoiceDefinition
import org.apache.camel.model.OnExceptionDefinition
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.processor.DelegateProcessor
import org.apache.camel.processor.aggregate.AggregationStrategy
import org.openehealth.ipf.commons.core.modules.api.*

import java.util.concurrent.ExecutorService

/**
 * @author Martin Krasser
 */
class LegacyCoreModelExtension {
    
    RouteBuilder routeBuilder // for config backwards-compatibility only (not used internally)
    
    static extensions = { 
        
        // ----------------------------------------------------------------
        //  Core Extensions
        // ----------------------------------------------------------------

        ProcessorDefinition.metaClass.process = { String processorBeanName ->
            LegacyCoreExtensionModule.process(delegate, processorBeanName)
        }

        ProcessorDefinition.metaClass.process = { Closure processorLogic ->
            LegacyCoreExtensionModule.process(delegate, processorLogic)
        }
            
        ProcessorDefinition.metaClass.intercept = {DelegateProcessor delegateProcessor ->
            LegacyCoreExtensionModule.intercept(delegate, delegateProcessor)
        }

        ProcessorDefinition.metaClass.intercept = { Closure interceptorLogic ->
            LegacyCoreExtensionModule.intercept(delegate, interceptorLogic)
        }

        ProcessorDefinition.metaClass.intercept = { String interceptorBean ->
            LegacyCoreExtensionModule.intercept(delegate, interceptorBean)
        }
        
        ProcessorDefinition.metaClass.filter = { Closure predicateLogic ->
            LegacyCoreExtensionModule.filter(delegate, predicateLogic)
        }
        
        ProcessorDefinition.metaClass.transform = { Closure transformExpression ->
            LegacyCoreExtensionModule.transform(delegate, transformExpression)
        }
    
        ProcessorDefinition.metaClass.setExchangeProperty = { String name, Closure propertyExpression ->
            LegacyCoreExtensionModule.setExchangeProperty(delegate, name, propertyExpression)
        }

        ProcessorDefinition.metaClass.setHeader = { String name, Closure headerExpression ->
            LegacyCoreExtensionModule.setHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setOutHeader = { String name, Closure headerExpression ->
            LegacyCoreExtensionModule.setOutHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setFaultHeader = { String name, Closure headerExpression ->
            LegacyCoreExtensionModule.setFaultHeader(delegate, name, headerExpression)
        }

        ProcessorDefinition.metaClass.setBody = {Closure bodyExpression ->
            LegacyCoreExtensionModule.setBody(delegate, bodyExpression)
        }
        
        ChoiceDefinition.metaClass.when = { Closure predicateLogic ->
            LegacyCoreExtensionModule.when(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Platform Processor Extensions
        // ----------------------------------------------------------------

            
        ProcessorDefinition.metaClass.enrich = { String resourceUri, Closure aggregationLogic ->
            LegacyCoreExtensionModule.enrich(delegate, resourceUri, aggregationLogic)
        }
    
        ProcessorDefinition.metaClass.ipf = { ->
            LegacyCoreExtensionModule.ipf(delegate)
	    }
                
         // ----------------------------------------------------------------
         //  Platform DataFormatClause extensions
         // ----------------------------------------------------------------
         
         DataFormatClause.metaClass.gnode = { String schemaResource, boolean namespaceAware ->
             LegacyCoreExtensionModule.gnode(delegate, schemaResource, namespaceAware)
         }
        
         DataFormatClause.metaClass.gnode = { boolean namespaceAware ->
             LegacyCoreExtensionModule.gnode(delegate, namespaceAware)
         }
     
         DataFormatClause.metaClass.gnode = { ->
             LegacyCoreExtensionModule.gnode(delegate)
         }

         DataFormatClause.metaClass.gpath = { String schemaResource, boolean namespaceAware ->
             LegacyCoreExtensionModule.gpath(delegate, schemaResource, namespaceAware)
         }       
        
         DataFormatClause.metaClass.gpath = { boolean namespaceAware ->
             LegacyCoreExtensionModule.gpath(delegate, namespaceAware)
         }

         DataFormatClause.metaClass.gpath = { ->
             LegacyCoreExtensionModule.gpath(delegate)
         }

  
        // ----------------------------------------------------------------
        //  Platform ExceptionDefinition extensions
        // ----------------------------------------------------------------
        
        OnExceptionDefinition.metaClass.onWhen = { Closure predicate ->
            LegacyCoreExtensionModule.onWhen(delegate, predicate)
        }

        // ----------------------------------------------------------------
        //  Adapter Extensions for RouteBuilder
        // ----------------------------------------------------------------

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Aggregator aggregator ->
            LegacyCoreExtensionModule.aggregationStrategy(delegate, aggregator)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { String aggregatorBeanName ->
            LegacyCoreExtensionModule.aggregationStrategy(delegate, aggregatorBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.aggregationStrategy = { Closure aggregationLogic ->
            LegacyCoreExtensionModule.aggregationStrategy(delegate, aggregationLogic)
        }
        
        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Predicate predicate ->
            LegacyCoreExtensionModule.predicate(delegate, predicate)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { String predicateBeanName ->
            LegacyCoreExtensionModule.predicate(delegate, predicateBeanName)
        }

        org.apache.camel.spring.SpringRouteBuilder.metaClass.predicate = { Closure predicateLogic ->
            LegacyCoreExtensionModule.predicate(delegate, predicateLogic)
        }
    
        // ----------------------------------------------------------------
        //  Adapter Extensions for ProcessorDefinition
        // ----------------------------------------------------------------


        ProcessorDefinition.metaClass.validate = { ->
            LegacyCoreExtensionModule.validate(delegate)
        }
    
        ProcessorDefinition.metaClass.validate = { Validator validator ->
            LegacyCoreExtensionModule.validate(delegate, validator)
        }
        
        ProcessorDefinition.metaClass.validate = { String validatorBeanName ->
            LegacyCoreExtensionModule.validate(delegate, validatorBeanName)
        }
    
        ProcessorDefinition.metaClass.validate = { Closure validatorLogic ->
            LegacyCoreExtensionModule.validate(delegate, validatorLogic)
        }


    }
}
