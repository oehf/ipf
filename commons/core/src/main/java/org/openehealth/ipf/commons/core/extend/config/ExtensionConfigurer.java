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
package org.openehealth.ipf.commons.core.extend.config;

import groovy.lang.ExpandoMetaClass;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.core.extend.DefaultActivator;
import org.openehealth.ipf.commons.core.extend.ExtensionActivator;

/**
 * Configurer used to autowire all classes implementing the {@link Extension}
 * interface.
 * 
 * As of Groovy 2.0, you should use Groovy's built-in extension mechanism in
 * order to register custom extensions.
 * 
 * @author Boris Stanojevic
 * @see http://docs.codehaus.org/display/GROOVY/Creating+an+extension+module
 */
public class ExtensionConfigurer<R extends Registry> extends
        OrderedConfigurer<Extension, R> {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionConfigurer.class);

    private ExtensionActivator extensionActivator;

    static {
        ExpandoMetaClass.enableGlobally();
    }

    public ExtensionConfigurer() {
        this.extensionActivator = new DefaultActivator();
        setOrder(2);
    }

    @Override
    public void configure(Extension configuration) {
        extensionActivator.activate(configuration);
        LOG.debug("Extension configured..." + configuration);
    }

    @Override
    public Collection<Extension> lookup(Registry registry) {
        return registry.beans(Extension.class).values();
    }

}
