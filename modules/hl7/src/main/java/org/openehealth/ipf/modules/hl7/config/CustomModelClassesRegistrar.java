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
package org.openehealth.ipf.modules.hl7.config;

import ca.uhn.hl7v2.parser.ModelClassFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects the {@link CustomModelClasses} beans of the application context and adds their
 * package definitions to the {@link CustomModelClassFactory} instances in use. A single bean
 * declaration is enough:
 *
 * <pre class="code">
 *     &lt;bean class="org.openehealth.ipf.modules.hl7.config.CustomModelClassesRegistrar"/&gt;
 * </pre>
 *
 * If {@link #setCustomModelClassFactory(CustomModelClassFactory)} is not set, all
 * {@link CustomModelClassFactory} beans of the application context are configured -- which is
 * what makes this work out of the box in Spring Boot, where the factory bean is contributed by
 * auto-configuration and frequently replaced by the application.
 * <p>
 * Collecting happens in {@link #afterSingletonsInstantiated()}, i.e. before the
 * {@code ContextRefreshedEvent} and thus well before any HL7v2 message is parsed.
 * <p>
 * This replaces the deprecated {@link CustomModelClassFactoryConfigurer}.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public class CustomModelClassesRegistrar implements BeanFactoryAware, SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(CustomModelClassesRegistrar.class);

    private BeanFactory beanFactory;

    /**
     * The factory to be configured. If null, all {@link CustomModelClassFactory} beans of the
     * application context are configured.
     */
    @Getter @Setter
    private CustomModelClassFactory customModelClassFactory;

    /**
     * Whether the delegates of a {@link CustomModelClassFactory} shall be configured as well.
     */
    @Getter @Setter
    private boolean configureRecursively = true;

    /**
     * @see BeanFactoryAware#setBeanFactory(BeanFactory)
     */
    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (beanFactory == null) {
            return;
        }
        var contributions = orderedBeans(CustomModelClasses.class);
        if (contributions.isEmpty()) {
            return;
        }
        var factories = customModelClassFactory != null ?
                List.of(customModelClassFactory) :
                orderedBeans(CustomModelClassFactory.class);
        log.debug("Adding {} custom model class contributions to {} model class factories",
                contributions.size(), factories.size());
        for (var factory : factories) {
            for (var contribution : contributions) {
                addModels(factory, contribution, configureRecursively);
            }
        }
    }

    private <T> List<T> orderedBeans(Class<T> type) {
        return beanFactory.getBeanProvider(type).orderedStream().toList();
    }

    /**
     * Adds the package definitions of the given contribution to the factory and, if requested,
     * to its chain of {@link CustomModelClassFactory} delegates. Package definitions that the
     * factory already knows about are skipped, so that a contribution reaching a factory both
     * directly and through a deprecated {@link CustomModelClassFactoryConfigurer} is only added
     * once.
     *
     * @param factory       model class factory to configure
     * @param contribution  custom model classes to add
     * @param recursively   whether to configure the delegates as well
     */
    public static void addModels(CustomModelClassFactory factory, CustomModelClasses contribution,
                                 boolean recursively) {
        if (factory == null || contribution.getModelClasses() == null) {
            return;
        }
        ModelClassFactory current = factory;
        while (current instanceof CustomModelClassFactory customFactory) {
            addMissingModels(customFactory, contribution.getModelClasses());
            if (!recursively) {
                break;
            }
            current = customFactory.getDelegate();
        }
        log.debug("Custom model classes configured: {}", contribution);
    }

    private static void addMissingModels(CustomModelClassFactory factory, Map<String, String[]> models) {
        var known = factory.getCustomModelClasses();
        var missing = new HashMap<String, String[]>();
        models.forEach((version, packageNames) -> {
            var knownPackageNames = known != null ? known.get(version) : null;
            var missingPackageNames = knownPackageNames == null ? packageNames :
                    Arrays.stream(packageNames)
                            .filter(packageName -> !Arrays.asList(knownPackageNames).contains(packageName))
                            .toArray(String[]::new);
            if (missingPackageNames.length > 0) {
                missing.put(version, missingPackageNames);
            }
        });
        if (!missing.isEmpty()) {
            try {
                factory.addModels(missing);
            } catch (UnsupportedOperationException e) {
                // HAPI's addModels() writes straight into the map the factory was constructed with
                throw new IllegalStateException("Cannot add custom model classes to " + factory +
                        ": its custom model class map is immutable. Construct the " +
                        "CustomModelClassFactory with a mutable map, e.g. new HashMap<>(Map.of(...))", e);
            }
        }
    }

}
