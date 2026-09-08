/*
 * Copyright 2026 the original author or authors.
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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.spring.map.SpringBidiMappingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Asserts how {@link SpringBidiMappingService} orders the {@link CustomMappings} contributions it
 * collects. Mapping resources are evaluated in collection order, so a later contribution wins for
 * keys that an earlier one already defined.
 */
public class CustomMappingsOrderTest {

    /**
     * {@code @Order} on a {@code @Bean} factory method must be honoured, not just {@code @Order}
     * on the contribution's class or an {@code Ordered} implementation.
     */
    @Test
    public void testOrderOnBeanMethodIsHonoured() {
        try (var context = new AnnotationConfigApplicationContext(OrderedConfig.class)) {
            assertThat(mappingResourcesOf(context),
                    contains("configurer1.map", "configurer2.map", "configurer3.map"));
        }
    }

    /**
     * Contributions of ancestor contexts are collected as well. Without an explicit
     * {@code @Order} the beans of the local context come first, which is the sequence Spring's
     * bean provider produces -- see {@link #testOrderWinsOverTheContextHierarchy()} for taking
     * control of it.
     */
    @Test
    public void testContributionsOfParentContextsAreCollected() {
        try (var context = childContextOf(ParentConfig.class, ChildConfig.class)) {
            assertThat(mappingResourcesOf(context),
                    containsInAnyOrder("configurer1.map", "configurer2.map"));
        }
    }

    @Test
    public void testOrderWinsOverTheContextHierarchy() {
        try (var context = childContextOf(OrderedParentConfig.class, OrderedChildConfig.class)) {
            assertThat(mappingResourcesOf(context),
                    contains("configurer1.map", "configurer2.map"));
        }
    }

    private static List<String> mappingResourcesOf(AnnotationConfigApplicationContext context) {
        return filenames(context.getBean(SpringBidiMappingService.class).getMappingResources());
    }

    private static List<String> filenames(Collection<? extends Resource> resources) {
        return resources.stream().map(Resource::getFilename).toList();
    }

    private static AnnotationConfigApplicationContext childContextOf(Class<?> parentConfig,
                                                                     Class<?> childConfig) {
        var parent = new AnnotationConfigApplicationContext(parentConfig);
        var child = new AnnotationConfigApplicationContext();
        child.setParent(parent);
        child.register(childConfig);
        child.refresh();
        return child;
    }

    private static CustomMappings mappings(String name) {
        var customMappings = new CustomMappings();
        customMappings.setMappingResource(new ClassPathResource(name));
        return customMappings;
    }

    /**
     * The bean methods are declared in an order that does not match their {@code @Order}, so that
     * a comparator ignoring the annotation would produce a different result.
     */
    @Configuration
    static class OrderedConfig {

        @Bean
        SpringBidiMappingService mappingService() {
            return new SpringBidiMappingService();
        }

        @Bean
        @Order(3)
        CustomMappings third() {
            return mappings("configurer3.map");
        }

        @Bean
        @Order(1)
        CustomMappings first() {
            return mappings("configurer1.map");
        }

        @Bean
        @Order(2)
        CustomMappings second() {
            return mappings("configurer2.map");
        }
    }

    @Configuration
    static class ParentConfig {

        @Bean
        CustomMappings inheritedMappings() {
            return mappings("configurer1.map");
        }
    }

    @Configuration
    static class ChildConfig {

        @Bean
        SpringBidiMappingService mappingService() {
            return new SpringBidiMappingService();
        }

        @Bean
        CustomMappings localMappings() {
            return mappings("configurer2.map");
        }
    }

    @Configuration
    static class OrderedParentConfig {

        @Bean
        @Order(1)
        CustomMappings inheritedMappings() {
            return mappings("configurer1.map");
        }
    }

    @Configuration
    static class OrderedChildConfig {

        @Bean
        SpringBidiMappingService mappingService() {
            return new SpringBidiMappingService();
        }

        @Bean
        @Order(2)
        CustomMappings localMappings() {
            return mappings("configurer2.map");
        }
    }

}
