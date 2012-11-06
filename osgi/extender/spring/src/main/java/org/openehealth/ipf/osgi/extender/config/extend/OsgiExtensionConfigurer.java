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
package org.openehealth.ipf.osgi.extender.config.extend;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.core.extend.DefaultActivator;
import org.openehealth.ipf.commons.core.extend.ExtensionActivator;
import org.openehealth.ipf.osgi.extender.config.OsgiSpringConfigurer;
import org.openehealth.ipf.osgi.extender.spring.ExtensionBeans;
import org.osgi.framework.BundleContext;

/**
 * @author Boris Stanojevic
 */
@Deprecated
public class OsgiExtensionConfigurer<R extends Registry> extends OsgiSpringConfigurer<Object, R>{

    private static final Logger LOG = LoggerFactory.getLogger(OsgiExtensionConfigurer.class);
    
    private final ExtensionActivator extensionActivator;
    
    public OsgiExtensionConfigurer() {
        extensionActivator = new DefaultActivator();
    }
    
    @Override
    public Collection<Object> lookup(BundleContext bc,
            R registry) {
        return ExtensionBeans.loadAll(bc.getBundle(), registry);
    }

    @Override
    public void configure(Object configuration) {
        extensionActivator.activate(configuration);
        LOG.info("Activated extension bean {}", configuration);
    }

    @Override
    public Collection<Object> lookup(R registry) {
        throw new UnsupportedOperationException();
    }

}
