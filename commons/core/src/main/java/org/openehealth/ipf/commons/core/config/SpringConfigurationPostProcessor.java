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
package org.openehealth.ipf.commons.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Spring Listener which holds the instances of all {@link OrderedConfigurer}. These
 * instances are collected from the Spring context on a {@link ContextRefreshedEvent}.
 *
 * @author Boris Stanojevic
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringConfigurationPostProcessor implements
        ApplicationListener<ContextRefreshedEvent> {

    private static Logger LOG = LoggerFactory.getLogger(SpringConfigurationPostProcessor.class);

    private boolean refreshed;
    private boolean restartOnce = true;

    private List<OrderedConfigurer> springConfigurers;

    protected void configure(Registry registry) {
        for (OrderedConfigurer sc : springConfigurers) {
            Collection configurations = sc.lookup(registry);
            if (configurations != null && configurations.size() > 0) {
                for (Object configuration : configurations) {
                    LOG.debug("Configuring extension {}", configuration);
                    sc.configure(configuration);
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
            SpringRegistry registry = new SpringRegistry();
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
