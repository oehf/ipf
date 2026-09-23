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
import org.openehealth.ipf.commons.map.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;

/**
 * The configurer collects {@code CustomMappings} into a {@link SpringMappings} exactly as it does
 * into the older service, and an untyped {@link SpringBidiMappingService} over the same instance
 * sees everything the typed API sees.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/context-mappings-configurer.xml", "/context-custom-mappings.xml"})
@SuppressWarnings("removal")
public class SpringMappingsConfigurerTest {

    @Autowired
    private SpringMappings mappings;

    @Autowired
    private SpringBidiMappingService mappingService;

    @Test
    public void customMappingsEndUpInTheTypedApi() {
        assertThat(mappings, translates("m1", "a1").to("b1"));
        assertThat(mappings, translates("m2", "a2").to("b2"));
        assertThat(mappings, translates("m3", "a3").to("b3"));
        assertThat(mappings, translates("m4", "d1").to("c1"));
        assertThat(mappings, translates("m5", "d2").to("c2"));
        assertThat(mappings, translates("m6", "d3").to("c3"));
    }

    /**
     * One set of mappings, two front doors: the untyped service delegates to the same instance
     * rather than loading anything of its own, which is what makes a gradual migration possible.
     */
    @Test
    public void theUntypedServiceSeesTheSameMappings() {
        assertThat(mappingService.getMappings(), is(sameInstance(mappings)));
        assertThat(mappingService.get("m1", "a1"), is("b1"));
        assertThat(mappingService.getKey("m1", "b1"), is("a1"));
    }

    @Test
    public void mappingsIsInjectableByItsInterface(@Autowired Mappings injected) {
        assertThat(injected, is(sameInstance(mappings)));
    }
}
