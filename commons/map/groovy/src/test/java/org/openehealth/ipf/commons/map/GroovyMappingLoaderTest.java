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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslate;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

/**
 * How the legacy Groovy DSL is folded onto the format-independent model.
 */
public class GroovyMappingLoaderTest {

    private static final String ENCOUNTER_TYPE = "encounterType";
    private static final String MARITAL_STATUS = "maritalStatus";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String REVERSE_ELSE = "reverseElseTest";
    private static final String VIP = "vip";

    @Test
    public void loaderIsDiscoveredForItsExtension() {
        assertThat(MappingLoaders.forSource(URI.create("file:/x/example2.map")),
                hasValue(instanceOf(GroovyMappingLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("jar:file:/x/y.jar!/example2.map")),
                hasValue(instanceOf(GroovyMappingLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("file:/x/example2.mapping.xml")), hasNoValue());
    }

    @Test
    public void readsEntriesAndCodeSystems() {
        var mappings = Mappings.builder().load("classpath:/example2.map").build();

        assertThat(mappings, translates(ENCOUNTER_TYPE, "I").to("IMP"));
        assertThat(mappings, translatesBack(ENCOUNTER_TYPE, "IMP").to("I"));
        assertThat(mappings.keySystem(ENCOUNTER_TYPE), hasValue("2.16.840.1.113883.12.4"));
        assertThat(mappings.keySystem(MESSAGE_TYPE), hasNoValue());
    }

    /**
     * An {@code (ELSE)} closure has no declarative equivalent, so it becomes a function the
     * loader registers under a name derived from the mapping.
     */
    @Test
    public void elseClosureBecomesARegisteredFunction() {
        var mappings = Mappings.builder().load("classpath:/example2.map").build();

        assertThat(mappings.mapping(VIP).orElseThrow().unmatched(),
                is(new Unmatched.Computed("vip#unmatched")));
        assertThat(mappings.functions().contains("vip#unmatched"), is(true));
        assertThat(mappings, translates(VIP, "X").to("X"));
        assertThat(mappings, translates(VIP, "Y").to("VIP"));
    }

    /**
     * The {@code ({'a'}) : (ELSE)} idiom - a closure used as a map key - is what the DSL had
     * instead of a named reverse fallback.
     */
    @Test
    public void closureUsedAsAKeyBecomesTheReverseFallback() {
        var mappings = Mappings.builder().load("classpath:/example2.map").build();

        assertThat(mappings.mapping(REVERSE_ELSE).orElseThrow().reverseUnmatched(),
                is(new Unmatched.Computed("reverseElseTest#reverseUnmatched")));
        assertThat(mappings, translatesBack(REVERSE_ELSE, "b").to("a"));
        assertThat(mappings, translatesBack(REVERSE_ELSE, "c").to("a"));
        assertThat(mappings, translates(REVERSE_ELSE, "d").to("c"));
    }

    /**
     * Regression test for the defect described in "Mappings without Groovy" section 04: loading a
     * second mapping source used to destroy the reverse fallback declared by the first, because
     * the whole reverse index was rebuilt from a forward map that the previous rebuild had
     * mutated. Indices are now built once per mapping, when it is registered.
     */
    @Test
    public void reverseFallbackSurvivesLoadingASecondSource() {
        var one = Mappings.builder()
                .load("classpath:/example2.map")
                .build();
        var two = Mappings.builder()
                .load("classpath:/example2.map")
                .load("classpath:/example3.map")
                .build();

        assertThat(one, translatesBack(REVERSE_ELSE, "c").to("a"));
        assertThat(two, translatesBack(REVERSE_ELSE, "c").to("a"));
    }

    /**
     * The DSL has no way to say that a mapping replaces one of the same name loaded earlier, and
     * it always did so silently - which example3.map relies on. The loader therefore declares the
     * override on every mapping it reads, so the file keeps working under a model that otherwise
     * treats a duplicate name as an error.
     */
    @Test
    public void everyMappingDeclaresAnOverride() {
        var mappings = Mappings.builder()
                .load("classpath:/example2.map")
                .load("classpath:/example3.map")
                .build();

        assertThat(mappings.mapping(MESSAGE_TYPE).orElseThrow().override(), is(true));
        assertThat(mappings, translates(MESSAGE_TYPE, "ADT^A04").to("PRPA_IN401001"));
        assertThat(mappings, doesNotTranslate(MESSAGE_TYPE, "ADT^A01"));
    }

    /**
     * The DSL cannot state which of several keys sharing a value is that value's inverse either;
     * the Groovy service resolved it by letting the last declaration overwrite the reverse index.
     * The loader states that as an equivalence, which preserves the answer and satisfies the
     * model's constraint that a value has one inverse.
     */
    @Test
    public void collidingEntriesAreResolvedAsTheGroovyServiceResolvedThem() {
        var mappings = Mappings.builder().load("classpath:/collisions.map").build();
        var mapping = mappings.mapping(MARITAL_STATUS).orElseThrow();

        assertThat(mapping.entries().stream().map(Entry::equivalence).toList(),
                contains(Equivalence.NARROWER, Equivalence.NARROWER, Equivalence.EQUAL));
        assertThat(mappings, translates(MARITAL_STATUS, "O").to("UNK"));
        assertThat(mappings, translates(MARITAL_STATUS, "U").to("UNK"));
        assertThat(mappings, translatesBack(MARITAL_STATUS, "UNK").to("U"));
    }

    /**
     * A composite value is one string to the model; only the MappingService adapter splits it.
     */
    @Test
    public void compositeValuesAreNotInterpreted() {
        var mappings = Mappings.builder().load("classpath:/example2.map").build();
        assertThat(mappings, translates("listTest", "a~b").to("c~d"));
    }

    @Test
    public void aScriptWithoutAMappingsClosureIsRejected() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/not-a-mapping.map"));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("does not declare a 'mappings' closure"));
    }
}
