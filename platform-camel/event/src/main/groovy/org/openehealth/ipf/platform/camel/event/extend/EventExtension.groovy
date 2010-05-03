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
package org.openehealth.ipf.platform.camel.event.extend;

import org.apache.camel.model.ProcessorDefinition;

import org.openehealth.ipf.platform.camel.event.model.PublishProcessorDefinition;

/**
 * Extension that defines the DSL elements related to the IPF event infrastructure.
 *
 * @dsl
 * 
 * @author Martin Krasser
 * @author Jens Riemschneider
 */
public class EventExtension {
     /**
      * Publishes an event object
      * @ipfdoc http://repo.openehealth.org/confluence/display/ipf2/Event+infrastructure#Eventinfrastructure-DSLextensions
      */
    public static PublishProcessorDefinition publish(ProcessorDefinition self, Closure publishingLogic) {
        PublishProcessorDefinition answer = new PublishProcessorDefinition();
        answer.eventFactoryClosure(publishingLogic);
        self.addOutput(answer);
        return answer;
    }
}
