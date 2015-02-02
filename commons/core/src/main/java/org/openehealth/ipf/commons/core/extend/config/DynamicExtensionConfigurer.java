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
package org.openehealth.ipf.commons.core.extend.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurer used to autowire all classes implementing the
 * {@link org.openehealth.ipf.commons.core.extend.config.DynamicExtension}
 * interface using Groovy 2.x Extension Modules
 *
 * @author Christian Ohr
 * @see http://docs.codehaus.org/display/GROOVY/Creating+an+extension+module
 * @see DynamicExtension
 * @see DynamicExtensionModule
 */
public class DynamicExtensionConfigurer<R extends Registry> extends
        OrderedConfigurer<DynamicExtension, R> {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicExtensionConfigurer.class);

    public DynamicExtensionConfigurer() {
        setOrder(2);
    }

    @Override
    public void configure(DynamicExtension extension) throws Exception {
        if (extension != null) {
            LOG.info("Registering new extension module {} defined in class {}",
                    extension.getModuleName(), extension.getClass());
            ExtensionModule module = DynamicExtensionModule.newModule(extension);
            addExtensionMethods(module);
        }
    }

    public static void addExtensionMethods(ExtensionModule module) {
        MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
        ((MetaClassRegistryImpl) metaClassRegistry).getModuleRegistry().addModule(module);
        Map<CachedClass, List<MetaMethod>> classMap = new HashMap<>();
        for (MetaMethod metaMethod : module.getMetaMethods()){
            if (classMap.containsKey(metaMethod.getDeclaringClass())){
                classMap.get(metaMethod.getDeclaringClass()).add(metaMethod);
            } else {
                List<MetaMethod> methodList = new ArrayList<>();
                methodList.add(metaMethod);
                classMap.put(metaMethod.getDeclaringClass(), methodList);
            }
            if (metaMethod.isStatic()){
                ((MetaClassRegistryImpl)metaClassRegistry).getStaticMethods().add(metaMethod);
            } else {
                ((MetaClassRegistryImpl)metaClassRegistry).getInstanceMethods().add(metaMethod);
            }
            LOG.debug("registered method: {}", metaMethod);
        }
        for (CachedClass cachedClass : classMap.keySet()) {
            cachedClass.addNewMopMethods(classMap.get(cachedClass));
        }
    }

    @Override
    public Collection<DynamicExtension> lookup(Registry registry) {
        return registry.beans(DynamicExtension.class).values();
    }

}
