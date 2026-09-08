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
package org.openehealth.ipf.commons.spring.core.config;

import lombok.NonNull;
import org.openehealth.ipf.commons.core.config.OrderedConfigurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring Listener which holds the instances of all {@link OrderedConfigurer}. These
 * instances are collected from the Spring context on a {@link ContextRefreshedEvent}.
 *
 * @author Boris Stanojevic
 *
 * @deprecated the holders and registrars that used to be filled by this listener now collect
 * their own contributions, so this bean can simply be removed from the application context.
 * Running on a {@link ContextRefreshedEvent} was also too late: every singleton has been created
 * by then, so anything resolving e.g. a mapping during bean initialization saw an empty
 * {@link org.openehealth.ipf.commons.map.MappingService}. See
 * {@link org.openehealth.ipf.commons.spring.map.SpringBidiMappingService},
 * {@link org.openehealth.ipf.commons.spring.core.extend.SpringDynamicExtensionRegistrar},
 * {@code org.openehealth.ipf.modules.hl7.config.CustomModelClassesRegistrar} and
 * {@code org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer}.
 * <p>
 * Note that this listener also created a {@link SpringRegistry} as a side effect, and thereby
 * initialized the {@link org.openehealth.ipf.commons.core.config.ContextFacade} that the
 * stateful Groovy and Kotlin extensions look up beans in. Contexts that relied on that side
 * effect have to declare the registry explicitly when dropping this bean:
 * <pre class="code">
 *     &lt;ipf:globalContext/&gt;
 * </pre>
 */
@Deprecated(since = "6.0.0", forRemoval = true)
@SuppressWarnings({"rawtypes", "unchecked", "removal"})
public class SpringConfigurationPostProcessor implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SpringConfigurationPostProcessor.class);

    private boolean refreshed;
    private boolean restartOnce = true;

    private List<OrderedConfigurer> springConfigurers;

    protected void configure(Registry registry) {
        for (var sc : springConfigurers) {
            var configurations = sc.lookup(registry);
            if (configurations != null && !configurations.isEmpty()) {
                for (var configuration : configurations) {
                    log.debug("Configuring extension {}", configuration);
                    try {
                        sc.configure(configuration);
                    } catch (Exception e) {
                        throw new BeanInitializationException("Cannot initialize " + configuration, e);
                    }
                }
            }
        }
    }

    /**
     * @param restartOnce if routes shall be initialized only once
     */
    public void setRestartOnce(boolean restartOnce) {
        this.restartOnce = restartOnce;
    }

    public List<OrderedConfigurer> getSpringConfigurers() {
        return springConfigurers;
    }

    public void setSpringConfigurers(List<OrderedConfigurer> springConfigurers) {
        this.springConfigurers = springConfigurers;
        Collections.sort(springConfigurers);
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (!refreshed || !restartOnce) {
            var registry = new SpringRegistry();
            registry.setBeanFactory(event.getApplicationContext());
            // If there are no configurers set, we look them up
            if (getSpringConfigurers() == null) {
                log.info("No extension beans configured, will look up registry for extension beans");
                springConfigurers = new ArrayList(registry.beans(
                        OrderedConfigurer.class).values());
                Collections.sort(springConfigurers);
            }
            log.info("Number of extension beans: {}", springConfigurers.size());
            configure(registry);
            refreshed = true;

        } else {
            log.info("Spring context has already been initialized before");
        }
    }
}
