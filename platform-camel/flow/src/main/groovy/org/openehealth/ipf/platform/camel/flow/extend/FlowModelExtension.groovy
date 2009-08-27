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
import org.openehealth.ipf.platform.camel.flow.model.DedupeDefinition
import org.openehealth.ipf.platform.camel.flow.model.IpfDefinition
import org.openehealth.ipf.platform.camel.flow.model.FlowBeginProcessorDefinition
import org.openehealth.ipf.platform.camel.flow.model.FlowEndProcessorDefinition
import org.openehealth.ipf.platform.camel.flow.model.FlowErrorProcessorDefinition
import org.openehealth.ipf.platform.camel.flow.model.SplitterDefinition
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression
import org.apache.camel.Expression

import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.spi.DataFormat

/**
 * @author Martin Krasser
 */
class FlowModelExtension {
    
    RouteBuilder routeBuilder // for config backwards-compatibility only (not used internally)
     
    static extensions = {

        ProcessorDefinition.metaClass.initFlow = { ->
            FlowBeginProcessorDefinition answer = new FlowBeginProcessorDefinition();
            delegate.addOutput(answer);
            return answer;
        }
        
        ProcessorDefinition.metaClass.initFlow = { String identifier ->
            FlowBeginProcessorDefinition answer = new FlowBeginProcessorDefinition(identifier);
            delegate.addOutput(answer);
            return answer;
        }
        
        ProcessorDefinition.metaClass.ackFlow = {
            FlowEndProcessorDefinition answer = new FlowEndProcessorDefinition();
            delegate.addOutput(answer);
            return answer;
        }
    
        ProcessorDefinition.metaClass.nakFlow = {
            FlowErrorProcessorDefinition answer = new FlowErrorProcessorDefinition();
            delegate.addOutput(answer);
            return answer;
        }
        
        ProcessorDefinition.metaClass.dedupeFlow = {
            DedupeDefinition answer = new DedupeDefinition()
            delegate.addOutput(answer);
            return answer;
        }
        
        ProcessorDefinition.metaClass.ipf = { ->
            new IpfDefinition(delegate)
	    }
                
   }   
   
}
