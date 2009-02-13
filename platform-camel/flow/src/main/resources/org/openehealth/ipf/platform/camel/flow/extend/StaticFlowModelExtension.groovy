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
package org.openehealth.ipf.platform.camel.flow.extend

import org.openehealth.ipf.platform.camel.flow.model.DedupeType
import org.openehealth.ipf.platform.camel.flow.model.FlowBeginProcessorType
import org.openehealth.ipf.platform.camel.flow.model.FlowEndProcessorType
import org.openehealth.ipf.platform.camel.flow.model.FlowErrorProcessorType
import org.openehealth.ipf.platform.camel.flow.model.SplitterType
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression
import org.apache.camel.Expression

import org.apache.camel.model.ProcessorType
import org.apache.camel.spi.DataFormat

/**
 * @author Martin Krasser
 */
public class StaticFlowModelExtension {

    static extensions = {

         ProcessorType.metaClass.initFlow = { ->
             FlowBeginProcessorType answer = new FlowBeginProcessorType();
             delegate.addOutput(answer);
             return answer;
         }
         
         ProcessorType.metaClass.initFlow = { String identifier ->
             FlowBeginProcessorType answer = new FlowBeginProcessorType(identifier);
             delegate.addOutput(answer);
             return answer;
         }
         
         ProcessorType.metaClass.ackFlow = {
             FlowEndProcessorType answer = new FlowEndProcessorType();
             delegate.addOutput(answer);
             return answer;
         }
     
         ProcessorType.metaClass.nakFlow = {
             FlowErrorProcessorType answer = new FlowErrorProcessorType();
             delegate.addOutput(answer);
             return answer;
         }
         
         ProcessorType.metaClass.dedupeFlow = {
             DedupeType answer = new DedupeType()
             delegate.addOutput(answer);
             return answer;
         }
         
         // ----------------------------------------------------------------
         //  ProcessorType Extensions
         // ----------------------------------------------------------------

         ProcessorType.metaClass.split = { Closure expressionLogic -> 
             SplitterType answer = new SplitterType(new DelegatingExpression(expressionLogic))        
             delegate.addOutput(answer)
             answer
         }
         
         ProcessorType.metaClass.split = { Expression expression -> 
             SplitterType answer = new SplitterType(expression)        
             delegate.addOutput(answer)
             answer
         }
    }   
    
}
