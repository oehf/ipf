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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.spring.map.config.CustomMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslate;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

/**
 * The Spring wiring - {@link SpringMappings} and the {@link CustomMappings} beans it collects -
 * is format-agnostic: a resource is dispatched to a loader by its file extension, so an
 * application can add mappings in whichever format suits each file, including several formats in
 * one list.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/context-mixed-mappings.xml")
public class MixedFormatMappingsTest {

    // The mapping each fixture contributes, named after the format it is written in rather than
    // after the fixture file, since which format a mapping came from is what these tests are about.
    private static final String FROM_GROOVY = "m1";
    private static final String FROM_XML = "m7";
    private static final String FROM_YAML = "m8";
    private static final String FROM_NAMED_FORMAT = "m9";

    private static final String DEVICE_TYPE = "deviceType";

    /**
     * A mapping function as a Spring bean, for {@code <unmatched mode="function" ref="first4"/>}.
     */
    public static class First4 implements Function<String, String> {

        @Override
        public String apply(String key) {
            return key == null || key.length() <= 4 ? key : key.substring(0, 4);
        }
    }

    @Autowired
    private SpringMappings mappings;

    @Test
    public void everyFormatEndsUpInTheSameService() {
        // legacy Groovy script
        assertThat(mappings, translates(FROM_GROOVY, "a1").to("b1"));
        assertThat(mappings, translates("m2", "a2").to("b2"));
        // IPF XML
        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
        assertThat(mappings.keySystem(FROM_XML), hasValue("2.16.840.1.113883.12.7"));
        // IPF YAML
        assertThat(mappings, translates(FROM_YAML, "d8").to("c8"));
    }

    /**
     * Where a resource name does not identify a format - here a file that is IPF XML without
     * being called {@code .mapping.xml} - the holder names the format its resources are read in.
     */
    @Test
    public void aHolderMayNameTheFormatOfItsResources() {
        assertThat(mappings, translates(FROM_NAMED_FORMAT, "d9").to("c9"));
        assertThat(mappings, translates(FROM_NAMED_FORMAT, "unknown").to("c9-default"));
    }

    @Test
    public void fallbacksWorkWhicheverFormatDeclaredThem() {
        assertThat(mappings, translates(FROM_XML, "unknown").to("c7-default"));  // XML, fixed
        assertThat(mappings, translates(FROM_YAML, "unknown").to("unknown"));     // YAML, identity
        assertThat(mappings, doesNotTranslate(FROM_GROOVY, "unknown"));           // .map, none
    }

    /**
     * A computed fallback declared in a mapping file resolves against a function registered as a
     * bean property, which is applied before any holder contributes a resource.
     */
    @Test
    public void computedFallbackResolvesAgainstABeanProperty() {
        assertThat(mappings, translates(DEVICE_TYPE, "LEGACY").to("legacy-device"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
    }

    @Test
    public void reverseLookupWorksAcrossFormats() {
        assertThat(mappings, translatesBack(FROM_GROOVY, "b1").to("a1"));
        assertThat(mappings, translatesBack(FROM_XML, "c7").to("d7"));
        assertThat(mappings, translatesBack(FROM_YAML, "c8").to("d8"));
    }

    /**
     * The resources a holder was given are the ones the service ended up with, whatever format
     * they were in.
     */
    @Test
    public void resourcesAreRecorded() {
        var filenames = mappings.getMappingResources().stream()
                .map(Resource::getFilename)
                .collect(Collectors.toSet());
        assertThat(filenames, hasItems("configurer1.map", "configurer2.map",
                "configurer7.mapping.xml", "configurer8.mapping.yaml", "functions.mapping.xml"));
    }

    /**
     * Adding a resource later, the way an application extends a running service.
     */
    @Test
    public void aResourceCanBeAddedAfterwards() {
        var added = new CustomMappings();
        added.setMappingResource(new ClassPathResource("configurer3.map"));
        mappings.setMappingResources(added.getMappingResources());

        assertThat(mappings, translates("m3", "a3").to("b3"));
        // and the mappings loaded before it are untouched
        assertThat(mappings, translates(FROM_XML, "d7").to("c7"));
        assertThat(mappings, translates(FROM_YAML, "d8").to("c8"));
    }
}
