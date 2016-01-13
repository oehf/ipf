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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.RendererAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip,transformation")
@XmlRootElement(name = "render")
@XmlAccessorType(XmlAccessType.FIELD)
public class RendererAdapterDefinition extends ProcessorAdapterDefinition {

    private Renderer renderer;
    
    private String rendererBean;
    
    public RendererAdapterDefinition(Renderer renderer) {
        this.renderer = renderer;
    }
    
    public RendererAdapterDefinition(String rendererBean) {
        this.rendererBean = rendererBean;
    }
    
    @Override
    public String toString() {
        return "RendererAdapter[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "rendererAdapter";
    }

    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
        if (rendererBean != null) {
            renderer = routeContext.lookup(rendererBean, Renderer.class);
        }
        return new RendererAdapter(renderer);
    }

}
