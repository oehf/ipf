/*
 * Copyright 2013 the original author or authors.
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

import java.util.Properties;

import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.m12n.MetaInfExtensionModule;
import org.codehaus.groovy.runtime.m12n.PropertiesModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension module that wraps the registration inside logging
 * statements so that the extension process can be observed if necessary.
 *
 * In order to load the extensions with this class,
 * the extension module descriptor must contain a corresponding line:
 * <p>
 * moduleFactory=org.openehealth.ipf.commons.core.config.ExtensionModuleFactory
 * </p>
 */
public class ExtensionModuleFactory extends PropertiesModuleFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionModuleFactory.class);
    private static final String MODULE_NAME_INITIALIZER_CLASS = "moduleInitializerClass";

    public ExtensionModuleFactory() {
    }

    @Override
    public ExtensionModule newModule(Properties properties, ClassLoader classLoader) {
        LOG.info("Registering new extension module {} defined in class {}",
                properties.getProperty(MODULE_NAME_KEY),
                properties.getProperty(MetaInfExtensionModule.MODULE_INSTANCE_CLASSES_KEY));
        ExtensionModule module = createExtensionModule(properties, classLoader);
        if (LOG.isDebugEnabled()) {
            for(MetaMethod method : module.getMetaMethods()) {
                LOG.debug("registered method: {}", method);
            }
        }
        if (properties.containsKey(MODULE_NAME_INITIALIZER_CLASS)) {
            String initializerClassName = properties.getProperty(MODULE_NAME_INITIALIZER_CLASS);
            try {
                Class<?> initializerClass = Class.forName(initializerClassName);
                initializerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                LOG.warn("Unable to initialize extension using {}.", initializerClassName, e);
            }
        }


        return module;
    }

    /**
     * Delegate that actually creates the ExtensionModule. Defaults to calling
     * {@link MetaInfExtensionModule#newModule(java.util.Properties, ClassLoader)}.
     *
     * @param properties extension module properties
     * @param classLoader classloader
     * @return new ExtensionModule
     */
    protected ExtensionModule createExtensionModule(Properties properties, ClassLoader classLoader) {
        return MetaInfExtensionModule.newModule(properties, classLoader);
    }
}
