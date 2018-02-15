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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Facade to an active registry, providing static access to their registered
 * beans.
 * <p/>
 * The ContextFacade is supposed to be used by Groovy Extension Modules that
 * require a global piece of configuration or context. As Extension Modules
 * are stateless, they have to obtain this information at runtime by means of
 * statically access, i.e. calling the getBean methods of this class. Before
 * this can be done, the registry must be set by calling {@link #setRegistry(Registry)}.
 *
 * @since 2.5
 */
public class ContextFacade {

    private static Registry instance;
    private static Logger LOG = LoggerFactory.getLogger(ContextFacade.class);

    public static synchronized void setRegistry(Registry registry) {
        if (instance != null && !registry.equals(instance))
            LOG.info("Re-initializing the registry");
        instance = registry;
    }

    /**
     * @param requiredType required bean type
     * @return bean of the required type
     * @since 2.5
     */
    public static <B> B getBean(Class<B> requiredType) {
        return instance.bean(requiredType);
    }

    /**
     * @param requiredType required bean type
     * @return bean of the required type
     * @since 3.5
     */
    public static <B> Collection<B> getBeans(Class<B> requiredType) {
        return instance.beans(requiredType).values();
    }

    /**
     * @param beanName name
     * @return bean of the required type
     * @since 2.5
     */
    @SuppressWarnings("unchecked")
    public static <B> B getBean(String beanName) {
        return (B) instance.bean(beanName);
    }

    /**
     * Empties the registry
     */
    public static synchronized void clearRegistry() {
        instance = null;
    }

}
