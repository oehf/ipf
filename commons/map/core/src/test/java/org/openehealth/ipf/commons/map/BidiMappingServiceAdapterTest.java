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
package org.openehealth.ipf.commons.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The untyped {@link MappingService} contract as {@link BidiMappingService} still honors it, on
 * top of the typed {@link Mappings} it delegates to. It answers what the typed API answers; the
 * empty-value and tilde conventions of IPF 5.x are gone.
 */
@SuppressWarnings("removal")
public class BidiMappingServiceAdapterTest {

    private static final String COMPOSITE = "composite";
    private static final String ENCOUNTER_TYPE = "encounterType";

    private BidiMappingService mappingService;

    @BeforeEach
    public void setUp() {
        mappingService = new BidiMappingService();
        mappingService.setMappingScript(getClass().getResource("/example.testmap"));
    }

    @Test
    public void forwardAndReverseLookup() {
        assertThat(mappingService.mappingKeys().contains(ENCOUNTER_TYPE), is(true));
        assertThat(mappingService.get(ENCOUNTER_TYPE, "I"), is("IMP"));
        assertThat(mappingService.get(ENCOUNTER_TYPE, "X"), is(nullValue()));
        assertThat(mappingService.get(ENCOUNTER_TYPE, "X", "WRONG"), is("WRONG"));
        assertThat(mappingService.getKey(ENCOUNTER_TYPE, "IMP"), is("I"));
        assertThat(mappingService.getKey(ENCOUNTER_TYPE, "X", "WRONG"), is("WRONG"));
        assertThat(mappingService.getKeySystem(ENCOUNTER_TYPE), is("2.16.840.1.113883.12.4"));
        assertThat(mappingService.getValueSystem(ENCOUNTER_TYPE), is("2.16.840.1.113883.5.4"));
        assertThat(mappingService.keys(ENCOUNTER_TYPE), containsInAnyOrder("E", "I", "O"));
        assertThat(mappingService.values(ENCOUNTER_TYPE), contains("EMER", "IMP", "AMB"));
    }

    @Test
    public void unknownMappingKeepsItsHistoricalMessage() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> mappingService.get("O", ENCOUNTER_TYPE));
        assertThat(e.getMessage(), is("Unknown key O"));
    }

    /**
     * A value containing a tilde is one string, as it is to the model, rather than a List.
     */
    @Test
    public void compositeValueIsReturnedAsItIs() {
        assertThat(mappingService.get(COMPOSITE, "PRPA_IN201301UV02"), is("A01~ADT_A01"));
        assertThat(mappingService.getKey(COMPOSITE, "A01~ADT_A01"), is("PRPA_IN201301UV02"));
    }

    /**
     * IPF 5.x joined a Collection with the tilde before lookup. Rather than look up its
     * {@code toString()} form and silently find nothing, the adapter rejects it.
     */
    @Test
    public void collectionKeyIsRejected() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> mappingService.getKey(COMPOSITE, List.of("A01", "ADT_A01")));
        assertThat(e.getMessage(), containsString("Composite keys are no longer supported"));
        assertThrows(IllegalArgumentException.class,
                () -> mappingService.get(COMPOSITE, List.of("PRPA_IN201301UV02")));
    }

    /**
     * The Groovy implementation reached the fallback through the {@code ?:} operator, which treats
     * an empty string as no value. An empty value is a value now: it is returned as it is, and
     * neither the fallback nor a default applies to it.
     */
    @Test
    public void emptyValueIsReturnedAsItIs() {
        assertThat(mappingService.get("emptyValue", "X"), is(""));
        assertThat(mappingService.get("emptyValue", ""), is(""));
        assertThat(mappingService.get("emptyValue", "X", "DEFAULT"), is(""));
        assertThat(mappingService.getKey("emptyValue", ""), is(""));
        assertThat(mappingService.get("emptyValue", "unknown"), is("FALLBACK"));
    }

    @Test
    public void clearingRemovesEverything() {
        mappingService.clearMappings();
        assertThat(mappingService.mappingKeys(), is(empty()));
        assertThat(mappingService.getScripts(), is(empty()));
        assertThrows(IllegalArgumentException.class, () -> mappingService.get(ENCOUNTER_TYPE, "I"));
    }

    /**
     * The same computed fallback through the untyped service, which is how a Spring-wired
     * application reaches it. Functions must be registered before the mapping source that names
     * them is read, and they survive clearMappings().
     */
    @Test
    public void computedFallbackThroughTheUntypedService() {
        var service = new BidiMappingService();
        service.setMappingFunctions(Map.of(
                "first4", key -> key == null || key.length() <= 4 ? key : key.substring(0, 4)));
        service.setMappingScript(getClass().getResource("/dynamic.testmap"));

        assertThat(service.get("deviceType", "LEGACY"), is("legacy-device"));
        assertThat(service.get("deviceType", "ABCDEFGH"), is("ABCD"));

        service.clearMappings();
        service.setMappingScript(getClass().getResource("/dynamic.testmap"));
        assertThat(service.get("deviceType", "ABCDEFGH"), is("ABCD"));
    }

    @Test
    public void unregisteredFunctionIsRejectedWhenTheSourceIsRead() {
        var service = new BidiMappingService();
        var e = assertThrows(MappingException.class,
                () -> service.setMappingScript(getClass().getResource("/dynamic.testmap")));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("unknown mapping function 'first4'"));
    }
}
