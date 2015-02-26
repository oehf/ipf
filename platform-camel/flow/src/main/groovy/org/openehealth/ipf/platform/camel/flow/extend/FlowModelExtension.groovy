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

import org.apache.camel.model.ProcessorDefinition

/**
 * @author Martin Krasser
 */
class FlowModelExtension {
     
    static extensions = {

        ProcessorDefinition.metaClass.initFlow = { ->
            FlowExtensionModule.initFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.initFlow = { String identifier ->
            FlowExtensionModule.initFlow(delegate, identifier)
        }
        
        ProcessorDefinition.metaClass.ackFlow = {
            FlowExtensionModule.ackFlow(delegate)
        }
    
        ProcessorDefinition.metaClass.nakFlow = {
            FlowExtensionModule.nakFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.dedupeFlow = {
            FlowExtensionModule.dedupeFlow(delegate)
        }
        
        ProcessorDefinition.metaClass.ipf = { ->
            FlowExtensionModule.ipf(delegate)
	    }
                
   }   
   
}
