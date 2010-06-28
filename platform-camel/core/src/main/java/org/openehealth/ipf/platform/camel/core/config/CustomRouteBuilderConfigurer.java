/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.config.SpringConfigurer;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * 
 * Configurer used to autowire all classes extending
 * the {@link CustomRouteBuilder} abstract class.
 * 
 * @author Boris Stanojevic
 * 
 */
public class CustomRouteBuilderConfigurer extends SpringConfigurer<CustomRouteBuilder> 
       implements Comparator<CustomRouteBuilder> {

    private CamelContext camelContext;
    
    private static final Log LOG = LogFactory.getLog(CustomRouteBuilderConfigurer.class);

    @Override
    public Collection<CustomRouteBuilder> lookup(ListableBeanFactory source) {        
        List list = new ArrayList(
                BeanFactoryUtils.beansOfTypeIncludingAncestors(source,
                        CustomRouteBuilder.class).values());
        Collections.sort(list);
        return list;
    }

    
    @Override
    public int compare(CustomRouteBuilder rb1, CustomRouteBuilder rb2) {
        return rb1.compareTo(rb2);
    }
    
    @Override
    public void configure(CustomRouteBuilder configuration){
        try {
            if (configuration.getIntercepted() != null) {
                configuration.getIntercepted().includeRoutes(configuration);
            } else {
                camelContext.addRoutes(configuration);
            }
        } catch (Exception e) {
            LOG.error("Custom route configuration failed: " + e);
        }
        LOG.debug("Custom route builder configured: " + configuration);
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

}