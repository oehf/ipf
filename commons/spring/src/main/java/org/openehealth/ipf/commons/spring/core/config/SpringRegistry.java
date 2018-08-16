/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.spring.core.config;

import java.util.Map;
import java.util.Objects;

import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.core.config.Registry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Class that bridges the {@link Registry} interface to a Spring
 * {@link ListableBeanFactory}. You simply need to declare a Spring
 * bean like this:
 * <p>
 * <bean class="org.openehealth.ipf.commons.core.config.SpringRegistry"/>
 * </p>
 * or, even easier, using the globalContext tag in the
 * http://openehealth.org/schema/ipf-commons-core extension namespace:
 * <pre>
 *     <ipf-commons-core:globalContext/>
 * </pre>
 * Then the "stateful" Groovy Extension Modules will access the Spring
 * registry.
 *
 * @since 2.5
 */
public class SpringRegistry implements Registry, BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    public SpringRegistry() {
    }

    @Override
    public Object bean(String name) {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T bean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public <T> Map<String, T> beans(Class<T> requiredType) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory,
                ProxyUtils.isJDKDynamicProxy(requiredType) ?
                        ProxyUtils.getFirstProxiedInterface(requiredType) :
                        requiredType);
    }

    /**
     * Stores the beanFactory and initialized the {@link ContextFacade}.
     * 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
        ContextFacade.setRegistry(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpringRegistry)) return false;
        SpringRegistry that = (SpringRegistry) o;
        return Objects.equals(beanFactory, that.beanFactory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanFactory);
    }
}
