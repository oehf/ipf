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

package org.openehealth.ipf.commons.spring.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 */
public class SpringBidiMappingServiceTest {

    SpringBidiMappingService mappingService;

    @BeforeEach
    public void setUp() {
        mappingService = new SpringBidiMappingService();
    }

    /**
     * Tests whether the "ignoreResourceNotFound" option works.
     */
    @Test
    public void testIgnoreResourceNotFound() {
        List<? extends Resource> resources = Arrays.asList(
                new ClassPathResource("example2.map.NONEXISTENT"),
                new ClassPathResource("example3.map")
        );

        mappingService.setIgnoreResourceNotFound(true);
        mappingService.setMappingResources(resources);
        assertEquals("PRPA_IN401001", mappingService.get("messageType", "ADT^A04"));

        try {
            mappingService.get("O", "encounterType");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown key O", e.getMessage());
        }
    }

    @Test
    public void testFailResourceNotFound() {
        List<? extends Resource> resources = Arrays.asList(
                new ClassPathResource("example2.map.NONEXISTENT"),
                new ClassPathResource("example3.map")
        );
        mappingService.setIgnoreResourceNotFound(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            mappingService.setMappingResources(resources);
        });
    }
}
