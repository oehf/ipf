/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.spring.map.config;

import org.openehealth.ipf.commons.core.config.Configurer;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.commons.spring.map.MappingResourceHolder;
import org.openehealth.ipf.commons.spring.map.MappingResourceTarget;
import org.openehealth.ipf.commons.spring.map.SpringMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * {@link Configurer} used to add all {@link CustomMappings}
 * bean occurrences from the spring application context
 * to the provided {@link MappingResourceTarget}, normally a {@link SpringMappings}.
 * <p>
 * This is what lets an application layer its own mappings over the ones IPF ships, in whichever
 * format each file is written in.
 *
 * @author Boris Stanojevic
 *
 * @deprecated {@link SpringMappings} -- and the deprecated
 * {@link org.openehealth.ipf.commons.spring.map.SpringBidiMappingService} where it still owns its
 * mappings -- collects the {@link MappingResourceHolder} beans of the application context itself,
 * so this configurer -- and the
 * {@link org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor} that
 * drives it -- are no longer needed:
 * <pre class="code">
 *     &lt;bean id="mappings"
 *           class="org.openehealth.ipf.commons.spring.map.SpringMappings"/&gt;
 * </pre>
 * Doing so also populates the mappings before the {@code ContextRefreshedEvent} instead of after
 * it, so that mappings can already be resolved while beans are being initialized. Both mechanisms
 * may be active at the same time: {@link SpringMappings#setMappingResource(org.springframework.core.io.Resource, String)}
 * ignores resources it has already evaluated.
 */
@Deprecated(since = "6.0.0", forRemoval = true)
@SuppressWarnings("removal")
public class CustomMappingsConfigurer<R extends Registry> extends OrderedConfigurer<MappingResourceHolder, R> {

    private MappingResourceTarget mappingService;
    
    private static final Logger log = LoggerFactory.getLogger(CustomMappingsConfigurer.class);
    
    /**
     * lookup for the specific {@link CustomMappings} objects inside
     * the given beanFactory
     * 
     * @see OrderedConfigurer
     */
    @Override
    public Collection<MappingResourceHolder> lookup(Registry registry) {
        return registry.beans(MappingResourceHolder.class).values().stream()
                // a target that also declares resources would otherwise feed itself its own
                .filter(holder -> holder != mappingService)
                .toList();
    }

    /**
     * configuration logic  
     */
    @Override
    public void configure(MappingResourceHolder configuration) {
        if (configuration.getMappingResources() != null) {
            mappingService.setMappingResources(configuration.getMappingResources(),
                    configuration.getMappingFormat());
            log.debug("Mapping resources added {}", configuration);
        }
    }
    
    public MappingResourceTarget getMappingService() {
        return mappingService;
    }

    public void setMappingService(MappingResourceTarget mappingService) {
        this.mappingService = mappingService;
    }

}
