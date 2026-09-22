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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.map.MappingException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class SpringMappingsTest {

    private static final String DEVICE_TYPE = "deviceType";
    private static final String FROM_XML = "m7";

    private SpringMappings mappings;

    @BeforeEach
    public void setUp() {
        mappings = new SpringMappings();
    }

    @Test
    public void readsResourcesInEveryFormat() {
        mappings.setMappingResources(List.of(
                new ClassPathResource("configurer1.map"),
                new ClassPathResource("configurer7.mapping.xml"),
                new ClassPathResource("configurer8.mapping.yaml")));

        assertThat(mappings, translates("m1", "a1").to("b1"));
        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
        assertThat(mappings, translates("m8", "d8").to("c8"));
        assertThat(mappings.keySystem(FROM_XML), hasValue("2.16.840.1.113883.12.7"));
        assertThat(mappings, translatesBack(FROM_XML, "c7").to("d7"));
        assertThat(mappings, translates(FROM_XML, "unknown").to("c7-default"));
    }

    @Test
    public void recordsTheResourcesItRead() {
        mappings.setMappingResource(new ClassPathResource("configurer7.mapping.xml"));
        assertThat(mappings.getMappingResources().stream().map(Resource::getFilename).toList(),
                contains("configurer7.mapping.xml"));
    }

    /**
     * A function as a bean property, registered before the resource that names it is read.
     */
    @Test
    public void resolvesComputedFallbacks() {
        mappings.setMappingFunctions(Map.of(
                "first4", key -> key == null || key.length() <= 4 ? key : key.substring(0, 4)));
        mappings.setMappingResource(new ClassPathResource("functions.mapping.xml"));

        assertThat(mappings, translates(DEVICE_TYPE, "LEGACY").to("legacy-device"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
    }

    @Test
    public void functionsSurviveClearing() {
        mappings.setMappingFunctions(Map.of("first4", key -> key.substring(0, 4)));
        mappings.setMappingResource(new ClassPathResource("functions.mapping.xml"));
        mappings.clear();

        assertThat(mappings.mappingNames(), is(empty()));
        assertThat(mappings.getMappingResources(), is(empty()));
        mappings.setMappingResource(new ClassPathResource("functions.mapping.xml"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
    }

    /**
     * Unlike the untyped service, this holds the model's constraints. A declarative format can
     * say that it means to replace a mapping; the legacy script format says so for every mapping
     * it reads, because that is what it always did.
     */
    @Test
    public void aDuplicateNameNeedsAnOverride() {
        mappings.setMappingResource(new ClassPathResource("configurer7.mapping.xml"));
        var duplicate = new ClassPathResource("configurer7-again.mapping.xml");
        var e = assertThrows(MappingException.class, () -> mappings.setMappingResource(duplicate));
        assertThat(e.getMessage(), containsString("Duplicate mapping 'm7'"));

        mappings.setAllowOverride(true);
        mappings.setMappingResource(duplicate);
        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
    }

    /**
     * Reading the very same resource again is not a duplicate declaration but the same one
     * arriving twice, which is what happens when a contribution reaches the mappings both
     * directly and through a deprecated {@code CustomMappingsConfigurer}. It is skipped, so
     * neither mechanism has to know about the other.
     */
    @Test
    public void theSameResourceIsReadOnce() {
        var resource = new ClassPathResource("configurer7.mapping.xml");
        mappings.setMappingResource(resource);
        mappings.setMappingResource(new ClassPathResource("configurer7.mapping.xml"));

        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
        assertThat(mappings.getMappingResources(), contains(resource));
    }

    @Test
    public void unreadableResourceFailsUnlessIgnored() {
        var missing = new ClassPathResource("nowhere.mapping.xml");
        assertThrows(IllegalArgumentException.class, () -> mappings.setMappingResource(missing));

        mappings.setIgnoreResourceNotFound(true);
        mappings.setMappingResources(List.of(missing, new ClassPathResource("configurer7.mapping.xml")));
        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
        assertThat(mappings.getMappingResources().stream().map(Resource::getFilename).toList(),
                contains("configurer7.mapping.xml"));
    }
}
