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

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Class that bridges the {@link Registry} interface to a Spring
 * {@link ListableBeanFactory}.
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpringRegistry other = (SpringRegistry) obj;
        if (beanFactory == null) {
            if (other.beanFactory != null)
                return false;
        } else if (beanFactory != other.beanFactory)
            return false;
        return true;
    }

}
