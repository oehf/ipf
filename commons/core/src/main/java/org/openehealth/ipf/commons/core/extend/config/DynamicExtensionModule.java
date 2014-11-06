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
import java.util.Collections;
import java.util.List;

import org.codehaus.groovy.runtime.m12n.SimpleExtensionModule;

/**
 * A primitive factory for a {@link org.codehaus.groovy.runtime.m12n.ExtensionModule}
 * that simply takes an already loaded {@link DynamicExtension} instance.
 *
 * @see DynamicExtensionConfigurer
 */
class DynamicExtensionModule extends SimpleExtensionModule {

    private DynamicExtension extension;

    private DynamicExtensionModule(DynamicExtension extension) {
        super(extension.getModuleName(), extension.getModuleVersion());
        this.extension = extension;
    }

    @Override
    public List<Class> getInstanceMethodsExtensionClasses() {
        return extension.isStatic() ? Collections.<Class>emptyList() : classList(extension);
    }

    @Override
    public List<Class> getStaticMethodsExtensionClasses() {
        return extension.isStatic() ? classList(extension) : Collections.<Class>emptyList();
    }

    private static List<Class> classList(Object o) {
        List<Class> l = new ArrayList<>(1);
        l.add(o.getClass());
        return Collections.unmodifiableList(l);
    }

    public static DynamicExtensionModule newModule(final DynamicExtension extension) {
        String name = extension.getModuleName();
        if (name == null)
            throw new RuntimeException("Module name is null");
        String version = extension.getModuleVersion();
        if (version == null)
            throw new RuntimeException("Module version is null");

        return new DynamicExtensionModule(extension);
    }
}
