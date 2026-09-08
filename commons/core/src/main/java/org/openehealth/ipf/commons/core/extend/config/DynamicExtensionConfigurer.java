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

import java.util.Collection;

import org.codehaus.groovy.runtime.m12n.ExtensionModule;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;

/**
 * Configurer used to autowire all classes implementing the
 * {@link DynamicExtension}
 * interface using Groovy 2.x Extension Modules
 *
 * @author Christian Ohr
 *
 * @see DynamicExtension
 * @see DynamicExtensionModule
 *
 * @deprecated this configurer requires a
 * {@code org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor}
 * to drive it, and only runs once the application context has been fully refreshed. Declare a
 * {@code org.openehealth.ipf.commons.spring.core.extend.SpringDynamicExtensionRegistrar}
 * bean instead, which collects the {@link DynamicExtension} beans itself and registers them
 * while the singletons are being initialized. Both mechanisms may be active at the same time;
 * {@link DynamicExtensions#register(DynamicExtension)} makes sure that no extension is
 * registered twice.
 */
@Deprecated(since = "6.0.0", forRemoval = true)
@SuppressWarnings("removal")
public class DynamicExtensionConfigurer<R extends Registry> extends
        OrderedConfigurer<DynamicExtension, R> {

    public DynamicExtensionConfigurer() {
        setOrder(2);
    }

    @Override
    public void configure(DynamicExtension extension) {
        DynamicExtensions.register(extension);
    }

    /**
     * @deprecated use {@link DynamicExtensions#addExtensionMethods(ExtensionModule)}
     */
    @Deprecated(since = "6.0.0", forRemoval = true)
    public static void addExtensionMethods(ExtensionModule module) {
        DynamicExtensions.addExtensionMethods(module);
    }

    @Override
    public Collection<DynamicExtension> lookup(Registry registry) {
        return registry.beans(DynamicExtension.class).values();
    }

}
