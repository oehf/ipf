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
package org.openehealth.ipf.commons.core.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * PostProcessor which holds the instances of all {@link SpringConfigurer}.
 * In the Method afterPropertiesSet will be assumed that all &lt;bean .. /&gt;
 * and &lt;lang:groovy ... /&gt; defined beans are loaded and can be configured.
 * 
 * @author Boris Stanojevic
 */
public class SpringConfigurationPostProcessor implements InitializingBean, BeanFactoryAware {

    private List<SpringConfigurer<Customized>> springConfigurers;
    
    private BeanFactory beanFactory;
    
    protected void configure(ListableBeanFactory lbf){
        for (SpringConfigurer<Customized> sc: springConfigurers){
            Collection<? extends Customized> configurations = sc.lookup(lbf);
            if (configurations != null && configurations.size() > 0){
                for (Customized configuration: configurations){
                    sc.configure(configuration);
                }
            }
        }
    }

    public List<SpringConfigurer<Customized>> getSpringConfigurers() {
        return springConfigurers;
    }

    public void setSpringConfigurers(List<SpringConfigurer<Customized>> springConfigurers) {
        this.springConfigurers = springConfigurers;
        Collections.sort(springConfigurers);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.beanFactory instanceof ListableBeanFactory){
            ListableBeanFactory lbf = (ListableBeanFactory)beanFactory;
            configure(lbf);
        }else {
            throw new IllegalStateException(
                    "SpringConfigurationPostProcessor doesn't work with a BeanFactory " +
                    "which does not implement ListableBeanFactory: " + beanFactory.getClass());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}