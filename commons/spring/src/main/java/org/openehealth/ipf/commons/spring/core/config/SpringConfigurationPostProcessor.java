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
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringConfigurationPostProcessor implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SpringConfigurationPostProcessor.class);

    private boolean refreshed;
    private boolean restartOnce = true;

    private List<OrderedConfigurer> springConfigurers;

    protected void configure(Registry registry) {
        for (var sc : springConfigurers) {
            var configurations = sc.lookup(registry);
            if (configurations != null && configurations.size() > 0) {
                for (var configuration : configurations) {
                    LOG.debug("Configuring extension {}", configuration);
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
     * @param restartOnce
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
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!refreshed || !restartOnce) {
            var registry = new SpringRegistry();
            registry.setBeanFactory(event.getApplicationContext());
            // If there are no configurers set, we look them up
            if (getSpringConfigurers() == null) {
                LOG.info("No extension beans configured, will look up registry for extension beans");
                springConfigurers = new ArrayList(registry.beans(
                        OrderedConfigurer.class).values());
                Collections.sort(springConfigurers);
            }
            LOG.info("Number of extension beans: " + springConfigurers.size());
            configure(registry);
            refreshed = true;

        } else {
            LOG.info("Spring context has already been initialized before");
        }
    }
}
