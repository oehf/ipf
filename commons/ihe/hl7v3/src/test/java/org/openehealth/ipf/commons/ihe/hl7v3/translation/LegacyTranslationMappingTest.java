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
package org.openehealth.ipf.commons.ihe.hl7v3.translation;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.map.Mappings;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * The legacy {@code .map} file is still shipped for applications that list it, so it has to hold
 * what the translators ask - including the two mappings that replaced the composite
 * {@code hl7v2v3-interactionId-eventStructure} - with the same answers as the XML.
 */
public class LegacyTranslationMappingTest {

    @Test
    public void legacyMapHoldsTheMappingsTheTranslatorsAsk() {
        var legacy = Mappings.builder().load("classpath:/META-INF/map/hl7-v2-v3-translation.map").build();
        var current = Mappings.builder().load("classpath:/META-INF/map/hl7-v2-v3-translation.mapping.xml").build();

        for (var mappings : new Mappings[] { legacy, current }) {
            assertThat(mappings.map("hl7v2v3-interactionId-triggerEvent", "PRPA_IN201302UV02"),
                    is(Optional.of("A08")));
            assertThat(mappings.map("hl7v2v3-interactionId-messageStructure", "PRPA_IN201302UV02"),
                    is(Optional.of("ADT_A01")));
            assertThat(mappings.mapReverse("hl7v2v3-interactionId-messageStructure", "ADT_A01"),
                    is(Optional.of("PRPA_IN201302UV02")));
        }
    }
}
