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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Renderer;
import org.openehealth.ipf.platform.camel.core.adapter.DataFormatAdapter;

/**
 * @author Martin Krasser
 */
public class DataFormatAdapterDefinition extends DataFormatDefinition {

    private String parserBeanName;
    private String rendererBeanName;
    
    private DataFormatAdapterDefinition(String parserBeanName, String rendererBeanName) {
        this.parserBeanName = parserBeanName;
        this.rendererBeanName = rendererBeanName;
    }
    
    public static DataFormatAdapterDefinition forParserBean(String beanName) {
        return new DataFormatAdapterDefinition(beanName, null);
    }
    
    public static DataFormatAdapterDefinition forRendererBean(String beanName) {
        return new DataFormatAdapterDefinition(null, beanName);
    }
    
    @Override
    public DataFormat getDataFormat(RouteContext routeContext) {
        if (parserBeanName != null) {
            return new DataFormatAdapter(routeContext.lookup(parserBeanName, Parser.class));
        } else {
            return new DataFormatAdapter(routeContext.lookup(rendererBeanName, Renderer.class));
        }
    }

}
