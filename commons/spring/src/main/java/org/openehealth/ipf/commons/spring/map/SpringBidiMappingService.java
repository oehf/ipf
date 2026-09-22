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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
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
 * A service constructed over a {@link SpringMappings} bean leaves the collecting to it, so that
 * the mappings the two share are read exactly once.
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

    private static final Logger log = LoggerFactory.getLogger(SpringBidiMappingService.class);

    private final Collection<Resource> resources = new ArrayList<>();

    /**
     * Whether this service owns the mappings it delegates to, i.e. whether nobody else collects
     * the {@link MappingResourceHolder} beans into them.
     */
    private final boolean ownsMappings;

    private BeanFactory beanFactory;

    public SpringBidiMappingService() {
        this.ownsMappings = true;
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
        this.ownsMappings = false;
    }

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Collects the mapping resources of all {@link MappingResourceHolder} beans of the
     * application context and its ancestors, honouring their {@code @Order}. Does nothing if the
     * mappings belong to a {@link SpringMappings} bean, which collects them itself.
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (beanFactory == null || !ownsMappings) {
            return;
        }
        var holders = beanFactory.getBeanProvider(MappingResourceHolder.class).orderedStream().toList();
        log.debug("Number of mapping resource holder beans: {}", holders.size());
        holders.forEach(holder -> setMappingResources(holder.getMappingResources()));
    }

    public Collection<? extends Resource> getMappingResources() {
        return resources;
    }

    /**
     * Evaluates the given mapping resource, unless it has been registered before. Skipping
     * already known resources keeps the mapping service idempotent, so that a contribution
     * reaching it both directly and through a deprecated
     * {@link org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer} is only
     * evaluated once.
     *
     * @param resource mapping resource
     */
    public synchronized void setMappingResource(Resource resource) {
        if (resources.contains(resource)) {
            log.debug("Mapping resource {} is already registered, skipping", resource.getFilename());
            return;
        }
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
