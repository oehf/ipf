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


import org.apache.camel.Expression
import org.apache.camel.builder.DataFormatClause
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.ProcessorType

import org.openehealth.ipf.commons.core.modules.api.Parser
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
import org.openehealth.ipf.commons.core.modules.api.Validator

/**
 * DSL extensions that require a route builder for bean lookups.
 * <strong>DO NOT USE THESE EXTENSIONS INSIDE AN OSGi ENVIRONMENT!</strong>
 * 
 * @author Martin Krasser
 */
class CoreModelExtension {
    
    RouteBuilder routeBuilder
    
    def extensions = {
            
        // ----------------------------------------------------------------
        //  Activate static extensions
        // ----------------------------------------------------------------
             
        StaticCoreModelExtension.extensions.call()
        
        // ----------------------------------------------------------------
        //  Platform Processor Extensions
        // ----------------------------------------------------------------
         
        ProcessorType.metaClass.split = { String expressionLogicBean -> 
            delegate.split(routeBuilder.bean(Expression.class, expressionLogicBean))        
        }        
        
        // ----------------------------------------------------------------
        //  Adapter Extensions for ProcessorType
        // ----------------------------------------------------------------

        ProcessorType.metaClass.transmogrify = { String transmogrifierBeanName ->
            delegate.transmogrify(routeBuilder.bean(Transmogrifier.class, transmogrifierBeanName))
        }

        ProcessorType.metaClass.validate = { String validatorBeanName ->
            delegate.validate(routeBuilder.bean(Validator.class, validatorBeanName))
        }
        
        ProcessorType.metaClass.parse = { String parserBeanName ->
            delegate.parse(routeBuilder.bean(Parser.class, parserBeanName))
        }

        ProcessorType.metaClass.render = { String rendererBeanName ->
            delegate.render(routeBuilder.bean(Renderer.class, rendererBeanName))
        }

        // ----------------------------------------------------------------
        //  Adapter Extensions for DataFormatClause
        // ----------------------------------------------------------------

        DataFormatClause.metaClass.parse = { String parserBeanName ->
            delegate.processorType.unmarshal(routeBuilder.dataFormatParser(parserBeanName))
        }
    
        DataFormatClause.metaClass.render = { String rendererBeanName ->
            delegate.processorType.marshal(routeBuilder.dataFormatRenderer(rendererBeanName))
        }

    }
    
}