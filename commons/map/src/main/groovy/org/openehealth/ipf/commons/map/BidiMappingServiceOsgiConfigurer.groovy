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
package org.openehealth.ipf.commons.map

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleEvent
import org.osgi.framework.BundleListener
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource

/**
 * Searches in all bundles for mapping scripts 
 * and configures the BidiMappingService with them. 
 * 
 * @author Martin Krasser
 * @author Boris Stanojevic
 */
class BidiMappingServiceOsgiConfigurer implements BundleListener { 

    static Log LOG = LogFactory.getLog(BidiMappingServiceOsgiConfigurer.class)
     
    BidiMappingService service
    
    BundleContext context
    
    static String MAPPING_PATH = 'META-INF/map'
    static String MAPPING_FILE = '*.map'
    
    
    BidiMappingServiceOsgiConfigurer(BidiMappingService service, BundleContext context){
        this.service = service
        this.context = context
        context.addBundleListener(this)
        initMappings()
    }
    
    
    
    void bundleChanged(BundleEvent event) {
        try {
            Bundle bundle = event.getBundle();
            if (event.getType() == BundleEvent.RESOLVED) {
                // this might get called several times on the same bundle,
                // but it doesn't matter, because the underlying map doesn't care.
                // the effort tracking each bundle is to high end error prone for no benefit.
                activateMapping(bundle)
            } 
        } catch (Throwable e) {
            LOG.error('Exception occured during bundleChanged for event: ' + event, e)
        }    
    }
    
    
    void initMappings() {
        for (Bundle bundle : context.getBundles()) {
            if (bundle.getState() == Bundle.RESOLVED
            || bundle.getState() == Bundle.STARTING
            || bundle.getState() == Bundle.ACTIVE
            || bundle.getState() == Bundle.STOPPING) {
                activateMapping(bundle)
            }
        }
        LOG.debug('initialized mapping service extender')
    }
    
    
    void activateMapping(Bundle bundle){
        def resources = bundle.findEntries(
        MAPPING_PATH, 
        MAPPING_FILE, false).collect { new UrlResource(it) }

        // configure mapping service with mapping resources
        if (resources != null && resources.size() > 0) {
            service.addMappingScripts(resources as Resource[])
            resources.each { 
                LOG.info("Added mapping resource ${it} to mapping service")
            }
            if (LOG.debugEnabled){
                LOG.debug("Current mappings: ${service.reverseMap}")
            }
        }
    }
         
}
