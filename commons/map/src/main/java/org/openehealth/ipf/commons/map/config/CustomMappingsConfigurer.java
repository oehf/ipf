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
package org.openehealth.ipf.commons.map.config;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.config.SpringConfigurer;
import org.openehealth.ipf.commons.map.BidiMappingService;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.Resource;

/**
 * {@link Configurer} used to add all {@link CustomMappings} 
 * bean occurrences from the spring application context
 * to the provided {@link BidiMappingService}.
 * 
 * @author Boris Stanojevic
 */
public class CustomMappingsConfigurer extends SpringConfigurer<CustomMappings> {

    private BidiMappingService mappingService;
    
    private static final Log LOG = LogFactory.getLog(CustomMappingsConfigurer.class);
    
    /**
     * lookup for the specific {@link CustomMappings} objects inside
     * the given beanFactory
     * 
     * @see SpringConfigurer
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<CustomMappings> lookup(ListableBeanFactory source) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(source,
                CustomMappings.class).values();
    }

    /**
     * configuration logic  
     */
    @Override
    public void configure(CustomMappings configuration) {
        if (configuration.getMappingScript() != null) {
            mappingService.addMappingScript(configuration.getMappingScript());
            LOG.info("Mapping script added" + configuration);
        }
        if (configuration.getMappingScripts() != null) {
            mappingService.addMappingScripts(configuration.getMappingScripts()
                .toArray(new Resource[configuration.getMappingScripts().size()]));
            LOG.info("Mapping scripts added" + configuration);
        }        
    }
    
    public BidiMappingService getMappingService() {
        return mappingService;
    }

    public void setMappingService(BidiMappingService mappingService) {
        this.mappingService = mappingService;
    }

}
