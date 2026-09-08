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

package org.openehealth.ipf.commons.spring.map;

import org.openehealth.ipf.commons.map.BidiMappingService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * BidiMappingService implementation that can be configured with Spring {@link Resource resources}
 * and using the {@link org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer}.
 * <p>
 * A resource is dispatched to a loader by its file extension, so the mapping files an application
 * registers may be in any format whose module is on the classpath - {@code .mapping.xml},
 * {@code .mapping.yaml}, {@code .conceptmap.r4.json} or the legacy {@code .map} - and one list may
 * mix them. Functions that a computed fallback refers to are a property of this bean, so they are
 * registered before any configurer adds a resource that names one.
 *
 * @since 3.1
 * @deprecated as of 6.0, use {@link SpringMappings} and the typed
 * {@link org.openehealth.ipf.commons.map.Mappings} API. This class remains as an adapter for
 * applications still holding a {@link org.openehealth.ipf.commons.map.MappingService}, and is
 * scheduled for removal in IPF 7.0.
 */
@Deprecated(since = "6.0", forRemoval = true)
@SuppressWarnings("removal")
public class SpringBidiMappingService extends BidiMappingService implements MappingResourceTarget {

    private final Collection<Resource> resources = new ArrayList<>();

    public SpringBidiMappingService() {
    }

    /**
     * Adapts mappings somebody else owns, so that an application can keep using the untyped
     * service while the mappings themselves are configured, and read, through
     * {@link SpringMappings}.
     *
     * @param mappings the mappings to delegate to
     */
    public SpringBidiMappingService(SpringMappings mappings) {
        super(mappings);
    }

    public Collection<? extends Resource> getMappingResources() {
        return resources;
    }

    public synchronized void setMappingResource(Resource resource) {
        try {
            setMappingScript(resource.getURL());
            resources.add(resource);
        } catch (IOException e) {
            if (!getIgnoreResourceNotFound())
                throw new IllegalArgumentException(resource.getFilename() + " could not be read", e);
        }
    }

    @Override
    public void setMappingResources(Collection<? extends Resource> resources) {
        resources.forEach(this::setMappingResource);
    }

}
