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
package org.openehealth.ipf.platform.camel.flow.extend

import org.openehealth.ipf.platform.camel.flow.builder.RouteBuilder
import org.apache.camel.model.ProcessorDefinition

/**
 * @author Martin Krasser
 */
class FlowModelExtension {
    
    RouteBuilder routeBuilder // for config backwards-compatibility only (not used internally)
     
    static extensions = {

        ProcessorDefinition.metaClass.initFlow = { ->
            FlowExtension.initFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.initFlow = { String identifier ->
            FlowExtension.initFlow(delegate, identifier)
        }
        
        ProcessorDefinition.metaClass.ackFlow = {
            FlowExtension.ackFlow(delegate)
        }
    
        ProcessorDefinition.metaClass.nakFlow = {
            FlowExtension.nakFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.dedupeFlow = {
            FlowExtension.dedupeFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.ipf = { ->
            FlowExtension.ipf(delegate)
	    }
                
   }   
   
}
