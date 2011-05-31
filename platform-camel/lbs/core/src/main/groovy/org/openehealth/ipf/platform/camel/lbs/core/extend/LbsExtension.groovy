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
package org.openehealth.ipf.platform.camel.lbs.core.extend;


import org.apache.camel.model.ProcessorDefinition;
import org.openehealth.ipf.platform.camel.lbs.core.model.FetchProcessorDefinition;
import org.openehealth.ipf.platform.camel.lbs.core.model.StoreProcessorDefinition;

/**
 * LBS DSL extensions for usage in a {@link org.apache.camel.builder.RouteBuilder} using the {@code use} keyword.
 *
 * @DSL
 *
 * @author Jens Riemschneider
 */
public class LbsExtension {
    /**
     * Stores content within a large binary store
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Large+binary+support#Largebinarysupport-DSLextensions
     */
    public static StoreProcessorDefinition store(ProcessorDefinition self) {
        StoreProcessorDefinition answer = new StoreProcessorDefinition();
        self.addOutput(answer);
        return answer;
    }        

    /**
     * Fetches content from a large binary store
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Large+binary+support#Largebinarysupport-DSLextensions
     */
    public static FetchProcessorDefinition fetch(ProcessorDefinition self) {
        FetchProcessorDefinition answer = new FetchProcessorDefinition();
        self.addOutput(answer);
        return answer;
    }        
}
