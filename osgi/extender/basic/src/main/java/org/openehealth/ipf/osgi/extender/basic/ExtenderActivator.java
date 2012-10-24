/*
 * Copyright 2009 the original author or authors.
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

import static org.osgi.framework.BundleEvent.STARTED;
import groovy.lang.ExpandoMetaClass;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.extend.DefaultActivator;
import org.openehealth.ipf.commons.core.extend.ExtensionActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

/**
 * @author Martin Krasser
 */
public class ExtenderActivator implements BundleActivator, SynchronousBundleListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExtenderActivator.class);
    
    private final ExtensionActivator extensionActivator;
    
    static {
        ExpandoMetaClass.enableGlobally();
    }
    
    public ExtenderActivator() {
        extensionActivator = new DefaultActivator();
    }
    
    @Override
    public void bundleChanged(BundleEvent event) {
        if (event.getType() == STARTED) {
            activateExtensionClasses(ExtensionClasses.loadAll(event.getBundle()));
        }
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        context.addBundleListener(this);
        
        // also check already installed bundles
        Bundle[] bundles = context.getBundles();
        // should not happen, but be defensive
        if(bundles != null){
        	LOG.debug("loading extension for already deployed (and started) bundles");
        	for (Bundle bundle : bundles) {
        		try{
	        		// only check for active ones... others will notify the listener themselves
					if(bundle.getState() == Bundle.ACTIVE){
						activateExtensionClasses(ExtensionClasses.loadAll(bundle));
					}
        		} catch(Throwable e){
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
    
    private void activateExtensionClasses(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            try {
                extensionActivator.activate(clazz);
                LOG.info("Activated extension class " + clazz);
            } catch (Exception e) {
                LOG.error("Couldn't activate extension class " + clazz, e);
            }
        }
    }
    
}
