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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextFacade {

    private static Registry instance;
    private static Log LOG = LogFactory.getLog(ContextFacade.class);

    public ContextFacade(Registry registry) {
        if (instance != null)
            LOG.warn("Re-initializing the application context");
        instance = registry;        
    }
    
    public static <B> B getBean(String name, Class<B> requiredType) {
        return instance.bean(name, requiredType);
    }

    public static <B> B getBean(Class<B> requiredType) {
        return instance.bean(requiredType);
    }

}
