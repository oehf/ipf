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
package org.openehealth.ipf.commons.spring.map.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.commons.spring.map.SpringBidiMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
		"/context-custom-mappings.xml" })
@SuppressWarnings("removal")
public class CustomMappingsConfigurerTest {

    @Autowired
    private CustomMappingsConfigurer<?> configurer;

    @Autowired
    private SpringBidiMappingService mappingService;

    @Test
    public void testMappings() {
        assertThat(configurer.getMappingService(), is(mappingService));

        assertThat(mappingService.get("m1", "a1"), is("b1"));
        assertThat(mappingService.get("m2", "a2"), is("b2"));
        assertThat(mappingService.get("m3", "a3"), is("b3"));

        assertThat(mappingService.get("m4", "d1"), is("c1"));
        assertThat(mappingService.get("m5", "d2"), is("c2"));
        assertThat(mappingService.get("m6", "d3"), is("c3"));
    }
	
}
