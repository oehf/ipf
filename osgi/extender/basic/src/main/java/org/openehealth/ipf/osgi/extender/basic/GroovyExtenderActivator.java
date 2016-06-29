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
package org.openehealth.ipf.osgi.extender.basic;

import org.codehaus.groovy.runtime.m12n.ExtensionModuleScanner;
import org.openehealth.ipf.commons.core.extend.config.DynamicExtensionConfigurer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

import static org.osgi.framework.BundleEvent.STARTED;

/**
 * OSGi Extender which looks for the Groovy Model Extension definitions in every active
 * and starting bundle and adds those extension methods to the Groovy module registry.
 *
 * @author Boris Stanojevic
 */
public class GroovyExtenderActivator implements BundleActivator, SynchronousBundleListener {

    private static final Logger LOG = LoggerFactory.getLogger(GroovyExtenderActivator.class);


    @Override
    public void bundleChanged(BundleEvent event) {
        if (event.getType() == STARTED) {
            addExtensionMethods(event.getBundle());
        }
    }

    @Override
    public void start(BundleContext context) throws Exception {
        context.addBundleListener(this);

        // also check already installed bundles
        Bundle[] bundles = context.getBundles();
        // should not happen, but be defensive
        if (bundles != null) {
            LOG.debug("loading extension for already deployed (and started) bundles");
            for (Bundle bundle : bundles) {
                try {
                    // only check for active ones... others will notify the listener themselves
                    if (bundle.getState() == Bundle.ACTIVE) {
                        addExtensionMethods(bundle);
                    }
                } catch (Throwable e) {
                    // this can happen at any time if a bundle was uninstalled after gathering
                    // the list of previous installed bundles.
                }
            }
        }
        LOG.debug("initialized extender activator");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeBundleListener(this);
        LOG.debug("destroyed extender activator");
    }

    private void addExtensionMethods(final Bundle bundle) {
        if (bundle.getResource(ExtensionModuleScanner.MODULE_META_INF_FILE) != null) {
            ClassLoader classLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundle);
            new ExtensionModuleScanner(module -> {
                DynamicExtensionConfigurer.addExtensionMethods(module);
                LOG.info("Added extension methods for bundle {}", bundle);
            }, classLoader).scanClasspathModules();
        }
    }

}
