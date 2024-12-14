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

import org.openehealth.ipf.commons.spring.map.MappingResourceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class should be used to define the custom mappings
 * in the spring context definition.
 *
 * <pre class="code">
 *    &lt;!-- either as a list of mapping definitions --&gt;
 *    &lt;bean id="customMapping3"
 *        class="org.openehealth.ipf.commons.map.config.CustomMappings"&gt;
 *        &lt;property name="mappingResources"&gt;
 *            &lt;list&gt;
 *                &lt;value&gt;classpath:configurer1.map&lt;/value&gt;
 *                &lt;value&gt;classpath:configurer2.map&lt;/value&gt;
 *            &lt;/list&gt;
 *        &lt;/property&gt;
 *    &lt;/bean&gt;
 *
 *    &lt;!-- or as a single mapping definition --&gt;
 *    &lt;bean id="customMappingSingle"
 *        class="org.openehealth.ipf.commons.map.config.CustomMappings"&gt;
 *        &lt;property name="mappingResource" value="classpath:configurer3.map" /&gt;
 *    &lt;/bean&gt;</pre>
 *
 * @see CustomMappingsConfigurer
 * @author Christian Ohr
 * @author Boris Stanojevic
 *
 */
public class CustomMappings implements MappingResourceHolder {

    private static final Logger log = LoggerFactory.getLogger(CustomMappings.class);

    private Collection<Resource> mappingResources = new ArrayList<>();

    @Override
    public Collection<? extends Resource> getMappingResources() {
        return Collections.unmodifiableCollection(mappingResources);
    }

    @Override
    public void addMappingResource(Resource mappingResource) {
        if (mappingResource.exists() && mappingResource.isReadable()) {
            mappingResources.add(mappingResource);
        } else {
            log.warn("Could not read mapping script {}", mappingResource.getFilename());
        }
    }

    @Override
    public void setMappingResources(Collection<? extends Resource> mappingResources) {
        this.mappingResources = new ArrayList<>(mappingResources.size());
        mappingResources.forEach(this::addMappingResource);
    }

    @Override
    public void setMappingResource(Resource mappingResource) {
        setMappingResources(Collections.singleton(mappingResource));
    }

}
