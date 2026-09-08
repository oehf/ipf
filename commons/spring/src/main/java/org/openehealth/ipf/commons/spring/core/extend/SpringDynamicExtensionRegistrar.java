/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.spring.core.extend;

import lombok.NonNull;
import org.openehealth.ipf.commons.core.extend.config.DynamicExtension;
import org.openehealth.ipf.commons.core.extend.config.DynamicExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * Collects the {@link DynamicExtension} beans of the application context and registers their
 * extension methods in Groovy's metaclass registry. A single bean declaration is enough:
 *
 * <pre class="code">
 *     &lt;bean class="org.openehealth.ipf.commons.spring.core.extend.SpringDynamicExtensionRegistrar"/&gt;
 * </pre>
 *
 * Registration happens in {@link #afterSingletonsInstantiated()}, i.e. before Spring starts its
 * {@code Lifecycle} beans and before the {@code ContextRefreshedEvent} is published. Anything
 * building Groovy DSL code later on -- most notably
 * {@code org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer} -- therefore
 * sees the registered extension methods.
 * <p>
 * This replaces the deprecated
 * {@code org.openehealth.ipf.commons.core.extend.config.DynamicExtensionConfigurer}. Both may be
 * active at the same time without registering any extension twice.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class SpringDynamicExtensionRegistrar implements BeanFactoryAware, SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(SpringDynamicExtensionRegistrar.class);

    private BeanFactory beanFactory;

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (beanFactory == null) {
            return;
        }
        var extensions = beanFactory.getBeanProvider(DynamicExtension.class).orderedStream().toList();
        log.debug("Number of dynamic extension beans: {}", extensions.size());
        extensions.forEach(DynamicExtensions::register);
    }

}
