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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasKey;

/**
 * Asserts that {@link CustomModelClassesRegistrar} collects the {@link CustomModelClasses}
 * beans of the application context by itself, i.e. without a configurer and a post processor.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
        "/context-custom-classes.xml" })
public class CustomModelClassesRegistrarTest {

    @Autowired
    private CustomModelClassFactory groovyCustomModelClassFactory;

    @Autowired
    private CustomModelClassFactory javaCustomModelClassFactory;

    @Test
    public void testModelClassesAdded() {
        var map = groovyCustomModelClassFactory.getCustomModelClasses();
        assertThat(map, hasKey("2.5"));
        assertThat(map.get("2.5"),
                arrayContaining("org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25"));
    }

    @Test
    public void testDelegateConfiguredRecursively() {
        var map = javaCustomModelClassFactory.getCustomModelClasses();
        assertThat(map, hasKey("2.5"));
        assertThat(map.get("2.5"),
                arrayContaining("org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25"));
    }

}
