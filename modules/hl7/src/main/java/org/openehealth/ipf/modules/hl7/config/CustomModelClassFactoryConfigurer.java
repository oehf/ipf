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

import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurer used to configure all {@link CustomModelClasses}
 * bean occurrences in the spring application context. Provides
 * the logic to add the new package definitions after the
 * existing ones.
 *
 * @author Boris Stanojevic
 * @author Christian Ohr
 */
public class CustomModelClassFactoryConfigurer<R extends Registry> extends OrderedConfigurer<CustomModelClasses, R> {

    private CustomModelClassFactory customModelClassFactory;

    private static final Logger log = LoggerFactory.getLogger(CustomModelClassFactoryConfigurer.class);

    boolean configureRecursively = true;

    @Override
    public Collection<CustomModelClasses> lookup(R registry) {
        return registry.beans(CustomModelClasses.class).values();
    }

    @Override
    public void configure(CustomModelClasses configuration) {
        // update the top ModelClassFactory
        var delegateFactory = configureAndDelegate(customModelClassFactory, configuration);
        // delegate if required
        CustomModelClassFactory currentFactory;
        while (isConfigureRecursively() && (delegateFactory instanceof CustomModelClassFactory)) {
            currentFactory = (CustomModelClassFactory) delegateFactory;
            delegateFactory = configureAndDelegate(currentFactory, configuration);
        }
        log.debug("Custom model classes configured: {}", configuration);
    }

    private ModelClassFactory configureAndDelegate(CustomModelClassFactory factory,
                                                   CustomModelClasses configuration) {
        factory.addModels(configuration.getModelClasses());
        return factory.getDelegate();
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
