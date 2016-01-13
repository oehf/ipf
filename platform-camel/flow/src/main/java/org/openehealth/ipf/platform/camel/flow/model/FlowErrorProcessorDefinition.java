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
import org.openehealth.ipf.platform.camel.core.util.Contexts;
import org.openehealth.ipf.platform.camel.flow.process.FlowErrorProcessor;
import org.openehealth.ipf.platform.camel.flow.process.FlowProcessor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip,flow")
@XmlRootElement(name = "nakFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlowErrorProcessorDefinition extends FlowProcessorDefinition {

    @Override
    public String toString() {
        return "FlowErrorProcessor[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "flowErrorProcessor";
    }

    @Override
    public FlowProcessor doCreateProcessor(RouteContext routeContext) throws Exception {
        return createFlowErrorProcessor(routeContext);
    }

    private static FlowErrorProcessor createFlowErrorProcessor(RouteContext routeContext) {
        CamelContext camelContext = routeContext.getCamelContext();
        FlowErrorProcessor processor = Contexts.beanOrNull(FlowErrorProcessor.class, camelContext);
        
        if (processor != null) {
            return processor;
        }
        
        processor = new FlowErrorProcessor();
        processor.setCamelContext(camelContext);
        processor.setFlowManager(Contexts.bean(FlowManager.class, camelContext));
        return processor;
    }
    
}
