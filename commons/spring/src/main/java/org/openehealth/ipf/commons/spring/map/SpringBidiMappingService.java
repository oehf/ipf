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

import lombok.NonNull;
import org.openehealth.ipf.commons.map.BidiMappingService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.io.Resource;

import java.util.Collection;

/**
 * BidiMappingService implementation that can be configured with Spring {@link Resource resources}.
 * <p>
 * A resource is dispatched to a loader by its file extension, so the mapping files an application
 * registers may be in any format whose module is on the classpath - {@code .mapping.xml},
 * {@code .mapping.yaml}, {@code .conceptmap.r4.json} or the legacy {@code .map} - and one list may
 * mix them.
 * <p>
 * When this service owns its mappings, it collects the {@link MappingResourceHolder} beans (usually
 * {@link org.openehealth.ipf.commons.spring.map.config.CustomMappings} instances) of the
 * application context itself, so no further beans are required:
 *
 * <pre class="code">
 *     &lt;bean id="mappingService"
 *           class="org.openehealth.ipf.commons.spring.map.SpringBidiMappingService"/&gt;
 * </pre>
 *
 * Collecting happens in {@link #afterSingletonsInstantiated()}, i.e. from within
 * {@code finishBeanFactoryInitialization} and thus before the {@code ContextRefreshedEvent}
 * that starts e.g. the Camel context. Contributions can be ordered relative to each other
 * with Spring's {@code @Order} / {@code Ordered} on the {@link MappingResourceHolder} bean.
 * <p>
 * Reading resources is left to a {@link SpringMappings}: one this service creates for itself, or,
 * with {@link #SpringBidiMappingService(SpringMappings)}, the application's bean, which then does
 * the collecting so that the mappings the two share are read exactly once.
 *
 * @since 3.1
 * @deprecated as of 6.0, use {@link SpringMappings} and the typed
 * {@link org.openehealth.ipf.commons.map.Mappings} API. This class remains as an adapter for
 * applications still holding a {@link org.openehealth.ipf.commons.map.MappingService}, and is
 * scheduled for removal in IPF 7.0.
 */
@Deprecated(since = "6.0", forRemoval = true)
@SuppressWarnings("removal")
public class SpringBidiMappingService extends BidiMappingService
        implements MappingResourceTarget, BeanFactoryAware, SmartInitializingSingleton {

    private final SpringMappings mappings;

    /**
     * Whether this service owns the mappings it delegates to, i.e. whether nobody else collects
     * the {@link MappingResourceHolder} beans into them.
     */
    private final boolean ownsMappings;

    public SpringBidiMappingService() {
        this(new SpringMappings(), true);
    }

    /**
     * Adapts mappings somebody else owns, so that an application can keep using the untyped
     * service while the mappings themselves are configured, and read, through
     * {@link SpringMappings}. The {@link SpringMappings} must be a bean of the application context,
     * since it collects the {@link MappingResourceHolder} beans itself; one created in place and
     * handed in here never does.
     *
     * @param mappings the mappings to delegate to
     */
    public SpringBidiMappingService(SpringMappings mappings) {
        this(mappings, false);
    }

    private SpringBidiMappingService(SpringMappings mappings, boolean ownsMappings) {
        super(mappings);
        this.mappings = mappings;
        this.ownsMappings = ownsMappings;
    }

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        if (ownsMappings) {
            mappings.setBeanFactory(beanFactory);
        }
    }

    /**
     * Collects the mapping resources of all {@link MappingResourceHolder} beans of the
     * application context and its ancestors, honouring their {@code @Order}. Does nothing if the
     * mappings belong to a {@link SpringMappings} bean, which collects them itself.
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (ownsMappings) {
            mappings.afterSingletonsInstantiated();
        }
    }

    public Collection<? extends Resource> getMappingResources() {
        return mappings.getMappingResources();
    }

    /**
     * @see SpringMappings#setMappingResource(Resource)
     */
    public void setMappingResource(Resource resource) {
        mappings.setMappingResource(resource);
    }

    @Override
    public void setMappingResources(Collection<? extends Resource> resources) {
        mappings.setMappingResources(resources);
    }

    @Override
    public void setMappingResources(Collection<? extends Resource> resources, String format) {
        mappings.setMappingResources(resources, format);
    }

    /**
     * Whether a resource that does not exist or cannot be read is skipped instead of failing the
     * context. Applies to the {@link SpringMappings} this service reads into.
     */
    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        mappings.setIgnoreResourceNotFound(ignoreResourceNotFound);
    }

    public boolean getIgnoreResourceNotFound() {
        return mappings.isIgnoreResourceNotFound();
    }

}
