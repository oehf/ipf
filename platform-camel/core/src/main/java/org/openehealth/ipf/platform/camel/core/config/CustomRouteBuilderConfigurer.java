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
package org.openehealth.ipf.platform.camel.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.SmartLifecycle;

/**
 * 
 * Configurer used to autowire all classes extending
 * the {@link CustomRouteBuilder} abstract class.
 * <p>
 * The configurer collects the {@link CustomRouteBuilder} beans of the application context
 * itself, so a single bean declaration is enough:
 *
 * <pre class="code">
 *     &lt;bean class="org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer"&gt;
 *         &lt;property name="camelContext" ref="camelContext"/&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * The {@code camelContext} property may be omitted if the application context contains exactly
 * one {@link CamelContext} bean.
 * <p>
 * Collecting happens when Spring starts its {@code Lifecycle} beans, i.e. after all singletons
 * have been initialized -- so Groovy DSL extensions contributed by a
 * {@code SpringDynamicExtensionRegistrar} are available -- but before the
 * {@code ContextRefreshedEvent} that starts the Camel context.
 * <p>
 * Note that this class still extends the deprecated {@link OrderedConfigurer} so that it can be
 * listed in the {@code springConfigurers} property of a deprecated
 * {@code SpringConfigurationPostProcessor}. That base class will be dropped in the next major
 * release. Both mechanisms may be active at the same time; a route builder that has already been
 * configured is skipped.
 * 
 * @author Boris Stanojevic
 * 
 */
@SuppressWarnings("removal")
public class CustomRouteBuilderConfigurer<R extends Registry> extends OrderedConfigurer<CustomRouteBuilder, R> 
       implements Comparator<CustomRouteBuilder>, BeanFactoryAware, SmartLifecycle {

    @Setter
    @Getter
    private CamelContext camelContext;

    private BeanFactory beanFactory;

    private final Set<CustomRouteBuilder> configured = Collections.newSetFromMap(new IdentityHashMap<>());

    private volatile boolean running;

    private static final Logger log = LoggerFactory.getLogger(CustomRouteBuilderConfigurer.class);

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Run early among the {@code Lifecycle} beans, so that the routes are known before e.g. a
     * web server starts accepting requests.
     */
    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public void start() {
        if (beanFactory != null) {
            for (var customRouteBuilder : lookup()) {
                try {
                    configure(customRouteBuilder);
                } catch (Exception e) {
                    throw new BeanInitializationException("Cannot initialize " + customRouteBuilder, e);
                }
            }
        }
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Collection<CustomRouteBuilder> lookup(R registry) {        
        var list = new ArrayList<>(registry.beans(CustomRouteBuilder.class).values());
        Collections.sort(list);
        return list;
    }

    private Collection<CustomRouteBuilder> lookup() {
        var list = new ArrayList<>(beanFactory.getBeanProvider(CustomRouteBuilder.class)
                .orderedStream().toList());
        // route builders intercepting another one must be configured first; the sort is stable,
        // so the @Order of the route builder beans decides within each of the two groups
        Collections.sort(list);
        log.debug("Number of custom route builder beans: {}", list.size());
        return list;
    }

    
    @Override
    public int compare(CustomRouteBuilder rb1, CustomRouteBuilder rb2) {
        return rb1.compareTo(rb2);
    }
    
    @Override
    public void configure(CustomRouteBuilder customRouteBuilder) throws Exception{
        if (!configured.add(customRouteBuilder)) {
            log.debug("Custom route builder {} has already been configured, skipping", customRouteBuilder);
            return;
        }
        if (customRouteBuilder.getIntercepted() != null) {
            var intercepted = customRouteBuilder.getIntercepted();
            customRouteBuilder.setCamelContext(getRequiredCamelContext());
            customRouteBuilder.setTemplatedRouteCollection(intercepted.getTemplatedRouteCollection());
            customRouteBuilder.setRouteTemplateCollection(intercepted.getRouteTemplateCollection());
            customRouteBuilder.setRestCollection(intercepted.getRestCollection());
            customRouteBuilder.setErrorHandlerFactory(intercepted.getErrorHandlerFactory());

            var f1 = RouteBuilder.class.getDeclaredField("routeCollection");
            f1.setAccessible(true);
            f1.set(customRouteBuilder, intercepted.getRouteCollection());

            // must invoke configure on the original builder, so it adds its configuration to me
            customRouteBuilder.configure();

        } else {
            getRequiredCamelContext().addRoutes(customRouteBuilder);
        }
        log.debug("Custom route builder configured: {}", customRouteBuilder);
    }

    private CamelContext getRequiredCamelContext() {
        if (camelContext == null && beanFactory != null) {
            camelContext = beanFactory.getBeanProvider(CamelContext.class).getIfUnique();
        }
        if (camelContext == null) {
            throw new IllegalStateException("No CamelContext set on " + this +
                    " and none (or more than one) found in the application context");
        }
        return camelContext;
    }

}
