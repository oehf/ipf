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
package org.openehealth.ipf.modules.hl7.config;

import java.util.Collection;

import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;

/**
 * Configurer used to configure all {@link CustomModelClasses}
 * bean occurrences in the spring application context. Provides
 * the logic to add the new package definitions after the
 * existing ones.
 *
 * @author Boris Stanojevic
 * @author Christian Ohr
 *
 * @deprecated declare a {@link CustomModelClassesRegistrar} instead, which collects the
 * {@link CustomModelClasses} beans of the application context itself and therefore needs
 * neither this configurer nor a
 * {@code org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor}:
 * <pre class="code">
 *     &lt;bean class="org.openehealth.ipf.modules.hl7.config.CustomModelClassesRegistrar"/&gt;
 * </pre>
 * The registrar also works in Spring Boot, where custom HL7 model classes could not be
 * contributed through this configurer at all. Both mechanisms may be active at the same time:
 * {@link CustomModelClassesRegistrar#addModels} skips package definitions the factory already
 * knows about.
 */
@Deprecated(since = "6.0.0", forRemoval = true)
@SuppressWarnings("removal")
public class CustomModelClassFactoryConfigurer<R extends Registry> extends OrderedConfigurer<CustomModelClasses, R> {

    private CustomModelClassFactory customModelClassFactory;

    boolean configureRecursively = true;

    @Override
    public Collection<CustomModelClasses> lookup(R registry) {
        return registry.beans(CustomModelClasses.class).values();
    }

    @Override
    public void configure(CustomModelClasses configuration) {
        CustomModelClassesRegistrar.addModels(customModelClassFactory, configuration, isConfigureRecursively());
    }

    public CustomModelClassFactory getCustomModelClassFactory() {
        return customModelClassFactory;
    }

    public void setCustomModelClassFactory(
            CustomModelClassFactory customModelClassFactory) {
        this.customModelClassFactory = customModelClassFactory;
    }

    public boolean isConfigureRecursively() {
        return configureRecursively;
    }

    public void setConfigureRecursively(boolean configureRecursively) {
        this.configureRecursively = configureRecursively;
    }
}
