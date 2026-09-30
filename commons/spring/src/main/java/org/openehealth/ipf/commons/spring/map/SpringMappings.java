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

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.openehealth.ipf.commons.map.DefaultMappings;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingFunctionProvider;
import org.openehealth.ipf.commons.map.Mappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * {@link Mappings} configured with Spring {@link Resource resources}, and the bean to wire into a
 * Spring application.
 * <pre>
 * &lt;bean id="mappings" class="org.openehealth.ipf.commons.spring.map.SpringMappings"&gt;
 *     &lt;property name="mappingResources"&gt;
 *         &lt;list&gt;
 *             &lt;value&gt;classpath:gender.mapping.xml&lt;/value&gt;
 *             &lt;value&gt;classpath:encounter.mapping.yaml&lt;/value&gt;
 *         &lt;/list&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;</pre>
 * A resource is dispatched to a loader by its file extension, so the formats may be mixed freely -
 * whichever modules are on the classpath decide which ones can be read. Where a name does not
 * identify a format - a location without a file extension, a file called {@code gender.xml} - set
 * {@link #setMappingFormat(String) mappingFormat} to name one for this bean's resources, or pass
 * it per resource to {@link #setMappingResource(Resource, String)}. Applications add further
 * mappings on top with {@link org.openehealth.ipf.commons.spring.map.config.CustomMappings}: this
 * bean collects the {@link MappingResourceHolder} beans of the application context itself, so no
 * configurer and no post processor is needed.
 * <p>
 * Collecting happens in {@link #afterSingletonsInstantiated()}, i.e. once all singletons have
 * been instantiated and before the {@code ContextRefreshedEvent} that starts e.g. the Camel
 * context, so the mappings are complete when routes are built. They are not complete yet while
 * singletons are being initialized: a {@code @PostConstruct} method sees only the resources set
 * on this bean directly. Contributions can be ordered relative to each other with Spring's {@code @Order} /
 * {@code Ordered} on the {@link MappingResourceHolder} bean.
 * <p>
 * Functions that a computed fallback names are a property of this bean, so they are registered
 * while it is created - before any configurer adds a resource that refers to one. A module that
 * ships both a mapping file and the functions it names should contribute a
 * {@link MappingFunctionProvider} service instead, and needs no configuration at all.
 * <p>
 * This holds the model's constraints - and so does {@link SpringBidiMappingService}, which reads
 * into one: a duplicate mapping name is an error unless the later declaration says
 * {@code override}, and a value with two inverses is an error rather than a reverse lookup that
 * silently answers whichever key was written last. Both can be relaxed with {@link #setAllowOverride} and
 * {@link #setAllowReverseCollisions} for a source that cannot express the intent.
 *
 * @since 6.0
 */
public class SpringMappings extends DefaultMappings
        implements MappingResourceTarget, BeanFactoryAware, SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(SpringMappings.class);

    private final Collection<Resource> resources = new ArrayList<>();

    /**
     * whether a resource that does not exist or cannot be read is skipped instead of failing the
     * context. A resource that exists but is not valid for its format always fails
     */
    @Setter
    @Getter
    private boolean ignoreResourceNotFound = false;

    /**
     *  Names the format of every resource of this bean that is not given one of its own, for
     *  resources whose name does not identify a format:
     *  <pre>
     *  &lt;bean id="mappings" class="org.openehealth.ipf.commons.spring.map.SpringMappings"&gt;
     *      &lt;property name="mappingFormat" value="conceptmap-r4-json"/&gt;
     *      &lt;property name="mappingResources"&gt;
     *          &lt;list&gt;&lt;value&gt;https://tx.example.org/ConceptMap/gender&lt;/value&gt;&lt;/list&gt;
     *      &lt;/property&gt;
     *  &lt;/bean&gt;</pre>
     *  Set it before the resources, which for a bean definition means declaring the property
     *  first. {@link org.openehealth.ipf.commons.map.MappingLoader#format() format id}, or {@code null}
     *  to dispatch by file extension
     */
    @Setter
    @Getter
    private String mappingFormat;

    private BeanFactory beanFactory;

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Collects the mapping resources of all {@link MappingResourceHolder} beans of the application
     * context and its ancestors, honouring their {@code @Order} and the format each of them names.
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (beanFactory == null) {
            return;
        }
        var holders = beanFactory.getBeanProvider(MappingResourceHolder.class).orderedStream().toList();
        log.debug("Number of mapping resource holder beans: {}", holders.size());
        holders.forEach(holder -> setMappingResources(holder.getMappingResources(), holder.getMappingFormat()));
    }

    /**
     * @return the resources read so far, in the order they were read
     */
    public Collection<? extends Resource> getMappingResources() {
        return Collections.unmodifiableCollection(resources);
    }

    @Override
    public synchronized void setMappingResources(Collection<? extends Resource> resources) {
        resources.forEach(this::setMappingResource);
    }

    @Override
    public synchronized void setMappingResources(Collection<? extends Resource> resources, String format) {
        resources.forEach(resource -> setMappingResource(resource, format));
    }

    /**
     * Reads one resource and registers the mappings it contains.
     *
     * @param resource a mapping resource, in any format a loader on the classpath claims
     * @throws IllegalArgumentException if the resource does not exist or cannot be read, and
     *                                  {@link #isIgnoreResourceNotFound()} is {@code false}
     * @throws MappingException         if the resource is not valid for its format
     */
    public synchronized void setMappingResource(Resource resource) {
        setMappingResource(resource, mappingFormat);
    }

    /**
     * Reads one resource in a format named explicitly rather than left to its file extension.
     * <p>
     * A resource that has been read before is skipped, which keeps this bean idempotent so that a
     * contribution reaching it both directly and through a deprecated
     * {@link org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer} is only
     * evaluated once.
     *
     * @param resource a mapping resource
     * @param format   a {@link org.openehealth.ipf.commons.map.MappingLoader#format() format id}
     *                 such as {@code xml} or {@code conceptmap-r4-json}, or {@code null} to fall back
     *                 to {@link #setMappingFormat(String)} and then to the file extension
     * @see #setMappingResource(Resource)
     */
    public synchronized void setMappingResource(Resource resource, String format) {
        if (resources.contains(resource)) {
            log.debug("Mapping resource {} is already registered, skipping", resource.getFilename());
            return;
        }
        // Checked before reading: a missing file or URL still has a URL, and only fails once the
        // loader opens it - as a MappingException indistinguishable from an invalid resource.
        if (!resource.exists()) {
            notFound(resource, null);
            return;
        }
        try {
            load(resource.getURL(), format != null ? format : mappingFormat);
            resources.add(resource);
        } catch (IOException e) {
            notFound(resource, e);
        }
    }

    private void notFound(Resource resource, IOException cause) {
        if (!ignoreResourceNotFound) {
            throw new IllegalArgumentException(resource.getDescription() + " could not be read", cause);
        }
        log.debug("Ignoring unreadable mapping resource {}", resource.getDescription());
    }

    /**
     * Registers the functions that a mapping's computed fallback refers to by name, i.e. what
     * {@code <unmatched mode="function" ref="..."/>} resolves against. A function receives the
     * key that had no entry and returns the value for it, so a mapping can derive its answer from
     * its input rather than list it.
     * <p>
     * A mapping source naming a function that is not registered is rejected as it is read, so
     * register them first. As a property of this bean that happens before any configurer adds a
     * mapping resource - but a resource set on this bean itself is read as soon as it is set, so in
     * a bean definition declare this property before {@code mappingResources}.
     *
     * @param functions functions by name, replacing any registered under the same name before
     */
    public synchronized void setMappingFunctions(Map<String, Function<String, String>> functions) {
        functions.forEach(this::registerMappingFunction);
    }

    /**
     * @see #setMappingFunctions(Map)
     */
    public synchronized void registerMappingFunction(String name, Function<String, String> function) {
        registerFunction(name, function);
    }

    /**
     * Forgets every mapping and the resources read, so that they can be read again. The functions
     * this bean was configured with stay registered.
     */
    @Override
    public synchronized void clear() {
        super.clear();
        resources.clear();
    }

}
