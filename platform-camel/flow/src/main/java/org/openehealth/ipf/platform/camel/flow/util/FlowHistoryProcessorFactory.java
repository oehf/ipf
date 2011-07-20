/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.flow.util;

import org.apache.camel.Processor;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.SplitDefinition;
import org.apache.camel.processor.MulticastProcessor;
import org.apache.camel.processor.Splitter;
import org.apache.camel.spi.ProcessorFactory;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.platform.camel.flow.process.FlowMulticastProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowSplitProcessor;

/**
 * A factory that tracks the flow history for: <br>
 * <li>splits <br> 
 * <li>multicasts <br>
 * 
 * To use the factory, declare a bean in the application context. Camel will
 * automatically find it an use it to update the flow history on splits and
 * multicasts. <br>
 * This approach is alternative to the LTW aspects
 * 
 * @author Mitko Kolev
 */
public class FlowHistoryProcessorFactory implements ProcessorFactory {

    
    @Override
    @SuppressWarnings("rawtypes")
    public Processor createChildProcessor(RouteContext routeContext,
            ProcessorDefinition definition, boolean mandatory) throws Exception {
        return null;
    }

   
    @Override
    @SuppressWarnings("rawtypes")
    public Processor createProcessor(RouteContext routeContext, ProcessorDefinition definition) throws Exception {
        if (definition instanceof SplitDefinition) {
            return flowSplitProcessor(routeContext, (SplitDefinition)definition);
        } else if (definition instanceof MulticastDefinition) {
            return flowMulticastProcessor(routeContext, (MulticastDefinition)definition);
        }
        //do not change the default behavior
        return null;
    }
    
    protected FlowSplitProcessor flowSplitProcessor(RouteContext routeContext, SplitDefinition original) throws Exception {
        return new FlowSplitProcessor((Splitter)original.createProcessor(routeContext));
    }
    protected FlowMulticastProcessor flowMulticastProcessor(RouteContext routeContext, MulticastDefinition original) throws Exception {
        return new FlowMulticastProcessor((MulticastProcessor)original.createProcessor(routeContext));
    }
}
