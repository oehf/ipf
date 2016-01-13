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
package org.openehealth.ipf.platform.camel.flow.model;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.flow.FlowManager;
import org.openehealth.ipf.commons.flow.transfer.FlowInfo;
import org.openehealth.ipf.platform.camel.core.util.Contexts;
import org.openehealth.ipf.platform.camel.flow.ReplayStrategyRegistry;
import org.openehealth.ipf.platform.camel.flow.process.FlowBeginProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowProcessor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip,flow")
@XmlRootElement(name = "initFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowBeginProcessorDefinition extends FlowProcessorDefinition {

    private final String identifier;
    private String application;
    private String replayErrorUri;
    
    private int expectedAckCount = FlowInfo.ACK_COUNT_EXPECTED_UNDEFINED;
    
    
    public FlowBeginProcessorDefinition(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Assigns an application name to created flows
     * @param application
     *          the name of the application
     */
    public FlowProcessorDefinition application(String application) {
        this.application = application;
        return this;
    }
    
    /**
     * Defines the endpoint URI for error messages in case a flow replay fails
     * @param replayErrorUri
     *          the endpoint URI
     */
    public FlowBeginProcessorDefinition replayErrorHandler(String replayErrorUri) {
        this.replayErrorUri = replayErrorUri;
        return this;
    }
    
    /**
     * Defines the number of acknowledgments that are expected for a created flow
     * @param expectedAckCount
     *          the expected number of acknowledgments 
     */
    public FlowBeginProcessorDefinition expectedAckCount(int expectedAckCount) {
        this.expectedAckCount = expectedAckCount;
        return this;
    }
    
    @Override
    public String toString() {
        return "FlowBeginProcessor[" + identifier + " -> " + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "flowBeginProcessor";
    }

    @Override
    public FlowProcessor doCreateProcessor(RouteContext routeContext) throws Exception {
        FlowBeginProcessor processor = createFlowBeginProcessor(routeContext);
        processor
            .identifier(identifier)
            .application(application)
            .expectedAckCount(expectedAckCount)
            .replayErrorHandler(replayErrorUri)
            .register();
        return processor;
    }

    private static FlowBeginProcessor createFlowBeginProcessor(RouteContext routeContext) {
        CamelContext camelContext = routeContext.getCamelContext();
        
        // Try to obtain a FlowBeginProcessor bean (its definition is optional)
        FlowBeginProcessor processor = Contexts.beanOrNull(FlowBeginProcessor.class, camelContext);
        
        if (processor != null) {
            return processor;
        }
        
        // No FlowBeginProcessor bean found so let's create one. We need a
        // - reference to a ReplayStrategyRegistry
        // - reference to a FlowManager
        processor = new FlowBeginProcessor();
        processor.setCamelContext(camelContext);
        processor.setFlowManager(Contexts.bean(FlowManager.class, camelContext));
        processor.setRegistry(Contexts.bean(ReplayStrategyRegistry.class, camelContext));
        return processor;
    }
    
}
