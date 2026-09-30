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

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Interface that isolates methods for registering {@link Resource resources} for a
 * {@link org.openehealth.ipf.commons.map.MappingService mapping service}.
 * <p>
 * A resource may be in any mapping format whose loader is on the classpath; the file extension
 * decides which one reads it, unless the holder names a format for all of its resources.
 */
public interface MappingResourceHolder {

    /**
     * Registers a mapping resource.
     *
     * @param resource mapping resource
     */
    void setMappingResource(Resource resource);

    /**
     * Registers a collection of mapping resources
     *
     * @param resources mapping resources
     */
    void setMappingResources(Collection<? extends Resource> resources);

    void addMappingResource(Resource resource);

    /**
     * @return immutable registered mapping resources
     */
    Collection<? extends Resource> getMappingResources();

    /**
     * The format the resources of this holder are read in, for resources whose name does not
     * identify one. One holder groups the mappings of one module, which are typically all in the
     * same format, so it is declared here rather than per resource.
     *
     * @return a {@link org.openehealth.ipf.commons.map.MappingLoader#format() format id}, or
     * {@code null} to dispatch each resource by its file extension
     */
    default String getMappingFormat() {
        return null;
    }

}
