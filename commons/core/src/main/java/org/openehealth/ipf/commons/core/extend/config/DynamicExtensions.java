/*
 * Copyright 2026 the original author or authors.
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

import groovy.lang.GroovySystem;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Registers {@link DynamicExtension} instances in Groovy's metaclass/metamethod registry.
 * <p>
 * {@link #register(DynamicExtension)} is idempotent per extension instance: Groovy's
 * metaclass registry is process-global, so registering the same extension twice would
 * add its methods twice. The guard is therefore global as well, which also makes it safe
 * for the deprecated {@link DynamicExtensionConfigurer} and a self-collecting registrar
 * such as {@code SpringDynamicExtensionRegistrar} to be active at the same time.
 * <p>
 * Note that {@link #register(DynamicExtension)} deliberately does not mention any Groovy
 * type in its signature, so that callers do not need Groovy on their compile classpath.
 *
 * @author Christian Ohr
 *
 * @see DynamicExtension
 * @since 6.0
 */
public final class DynamicExtensions {

    private static final Logger log = LoggerFactory.getLogger(DynamicExtensions.class);

    // Identity, not equality: two distinct extension instances contribute two modules even if
    // they happen to be equal. Retaining them costs nothing, since Groovy's module registry
    // holds on to every registered extension anyway.
    private static final Set<DynamicExtension> REGISTERED =
            Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));

    private DynamicExtensions() {
    }

    /**
     * Registers the extension methods of the given {@link DynamicExtension} in Groovy's
     * metaclass registry, unless this extension instance has already been registered.
     *
     * @param extension extension to register, may be null
     * @return true if the extension has been registered by this call, false if it was
     * null or had already been registered before
     */
    public static boolean register(DynamicExtension extension) {
        if (extension == null || !REGISTERED.add(extension)) {
            return false;
        }
        log.info("Registering new extension module {} defined in class {}",
                extension.getModuleName(), extension.getClass());
        addExtensionMethods(DynamicExtensionModule.newModule(extension));
        return true;
    }

    /**
     * Adds the meta methods of the given extension module to Groovy's metaclass registry.
     *
     * @param module extension module
     */
    public static void addExtensionMethods(ExtensionModule module) {
        var metaClassRegistry = GroovySystem.getMetaClassRegistry();
        ((MetaClassRegistryImpl) metaClassRegistry).getModuleRegistry().addModule(module);
        var classMap = new HashMap<CachedClass, List<MetaMethod>>();
        for (var metaMethod : module.getMetaMethods()) {
            classMap.computeIfAbsent(metaMethod.getDeclaringClass(), c -> new ArrayList<>()).add(metaMethod);
            if (metaMethod.isStatic()) {
                ((MetaClassRegistryImpl) metaClassRegistry).getStaticMethods().add(metaMethod);
            } else {
                ((MetaClassRegistryImpl) metaClassRegistry).getInstanceMethods().add(metaMethod);
            }
            log.debug("registered method: {}", metaMethod);
        }
        for (var cachedClassEntry : classMap.entrySet()) {
            cachedClassEntry.getKey().addNewMopMethods(cachedClassEntry.getValue());
        }
    }

}
