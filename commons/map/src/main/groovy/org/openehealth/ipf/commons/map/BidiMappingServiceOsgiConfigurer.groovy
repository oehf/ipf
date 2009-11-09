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
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResourceimport org.springframework.osgi.context.BundleContextAware

/**
 * Searches the bundle space (including fragments) for mapping scripts
 * and configures the BidiMappingService with them.  
 * 
 * @author Martin Krasser
 */
class BidiMappingServiceOsgiConfigurer implements BundleContextAware { 

    static Log LOG = LogFactory.getLog(BidiMappingServiceOsgiConfigurer.class)
     
    static String MAPPING_PATH = 'META-INF/map' 
    static String MAPPING_FILE = '*.map'
     
    BundleContext context
    
    BidiMappingService mappingService
    
    void setBundleContext(BundleContext context) {
        this.context = context
    }
    
    void configure() {
        def resources = context.bundle.findEntries(
                MAPPING_PATH, 
                MAPPING_FILE, 
                false).collect { new UrlResource(it) }

        // configure mapping service with mapping resources
        mappingService.setMappingScripts(resources as Resource[])
        
        resources.each { 
            LOG.info("Added mapping resource ${it} to mapping service")
        }
    }
     
}
