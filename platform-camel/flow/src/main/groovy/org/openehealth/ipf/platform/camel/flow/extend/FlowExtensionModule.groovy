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
package org.openehealth.ipf.platform.camel.flow.extend;

import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.flow.model.*;

/**
 * Flow DSL extensions for usage in a RouteBuilder using the {@code use} keyword.
 * 
 * @DSL
 * 
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
public class FlowExtensionModule {
    /**
     * Starts recording a message flow
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Flow+management#Flowmanagement-initflowextension
     */
    public static FlowBeginProcessorDefinition initFlow(ProcessorDefinition self) {
        FlowBeginProcessorDefinition answer = new FlowBeginProcessorDefinition();
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Starts recording a message flow with a specific identifier
     * @param identifier
     * 			the identifier to use
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Flow+management#Flowmanagement-initflowextension
     */
    public static FlowBeginProcessorDefinition initFlow(ProcessorDefinition self, String identifier) {
        FlowBeginProcessorDefinition answer = new FlowBeginProcessorDefinition(identifier);
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Logs the successful delivery of a message
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Flow+management#Flowmanagement-ackflowextension
     */
    public static FlowEndProcessorDefinition ackFlow(ProcessorDefinition self) {
        FlowEndProcessorDefinition answer = new FlowEndProcessorDefinition();
        self.addOutput(answer);
        return answer;
    }

    /**
     * Logs the erroneous processing of a message
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Flow+management#Flowmanagement-nakflowextension
     */
    public static FlowErrorProcessorDefinition nakFlow(ProcessorDefinition self) {
        FlowErrorProcessorDefinition answer = new FlowErrorProcessorDefinition();
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Filters messages during flow replay operations to avoid delivery of duplicates
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Flow+management#Flowmanagement-dedupeextension
     */
    public static DedupeDefinition dedupeFlow(ProcessorDefinition self) {
        DedupeDefinition answer = new DedupeDefinition();
        self.addOutput(answer);
        return answer;
    }
    
    /**
     * Allows adding of extensions that replace existing extensions provided by Camel (e.g.
     * {@code split})
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Core+features#Corefeatures-Splitter
     */
    public static IpfDefinition ipf(ProcessorDefinition self) {
        return new IpfDefinition(self)
    }
}
