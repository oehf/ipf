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
package org.openehealth.ipf.osgi.extender.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.SpringRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.osgi.extender.OsgiBeanFactoryPostProcessor;

/**
 * OSGi-Extender which holds the instances of all {@link OrderedConfigurer}
 * and {@link OsgiSpringConfigurer}. Every time a new bundle with existing
 * spring definition is registered inside of the BundleContext, this
 * extender loops through its defined configurers and looks-up for
 * eventual configuration objects inside of them. 
 * 
 * @author Boris Stanojevic
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class OsgiSpringConfigurationPostProcessor implements OsgiBeanFactoryPostProcessor {

    private List<OrderedConfigurer> springConfigurers;
    
    private List<OsgiSpringConfigurer> osgiSpringConfigurers;

    @Override
    public void postProcessBeanFactory(BundleContext bundleContext,
            ConfigurableListableBeanFactory beanFactory) throws BeansException,
            InvalidSyntaxException, BundleException {

        SpringRegistry registry = new SpringRegistry();
        registry.setBeanFactory(beanFactory);
        
        for (OsgiSpringConfigurer osc: osgiSpringConfigurers){
            Collection configurations = osc.lookup(bundleContext, registry);
            if (configurations != null && configurations.size() > 0){
                for (Object configuration: configurations){
                    osc.configure(configuration);
                }
            }
        }
        
        for (OrderedConfigurer sc: springConfigurers){
            Collection configurations = sc.lookup(registry);
            if (configurations != null && configurations.size() > 0){
                for (Object configuration: configurations){
                    sc.configure(configuration);
                }
            }
        }
    }

    public List<OrderedConfigurer> getSpringConfigurers() {
        return springConfigurers;
    }

    public void setSpringConfigurers(List<OrderedConfigurer> springConfigurers) {
        this.springConfigurers = springConfigurers;
        Collections.sort(springConfigurers);
    }

    public List<OsgiSpringConfigurer> getOsgiSpringConfigurers() {
        return osgiSpringConfigurers;
    }

    public void setOsgiSpringConfigurers(
            List<OsgiSpringConfigurer> osgiSpringConfigurers) {
        this.osgiSpringConfigurers = osgiSpringConfigurers;
    }
    
}
