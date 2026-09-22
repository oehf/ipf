/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.spring.map;

import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * Something mapping resources can be added to, i.e. the other end of a
 * {@link MappingResourceHolder}.
 * <p>
 * {@link SpringMappings} is the one to wire in new applications;
 * {@link SpringBidiMappingService} implements it as well so that
 * {@link org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer} keeps working
 * against either.
 *
 * @since 6.0
 */
public interface MappingResourceTarget {

    /**
     * Reads every resource and registers the mappings it contains. Which format a resource is in
     * is decided by its file extension.
     *
     * @param resources mapping resources
     */
    void setMappingResources(Collection<? extends Resource> resources);

    /**
     * Reads every resource in a format named explicitly, for resources whose name does not
     * identify one - a location without a file extension, or a file called {@code gender.xml}.
     *
     * @param resources mapping resources
     * @param format    a {@link org.openehealth.ipf.commons.map.MappingLoader#format() format id}
     *                  such as {@code xml} or {@code conceptmap-r4-json}, or {@code null} to dispatch
     *                  by file extension
     * @throws UnsupportedOperationException if this target reads one fixed format, which is the
     *                                       case for the deprecated {@link SpringBidiMappingService}
     */
    default void setMappingResources(Collection<? extends Resource> resources, String format) {
        if (format != null && !format.isBlank()) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " reads mapping"
                    + " resources in one fixed format and cannot be told to read '" + format
                    + "'. Wire a SpringMappings bean instead");
        }
        setMappingResources(resources);
    }
}
