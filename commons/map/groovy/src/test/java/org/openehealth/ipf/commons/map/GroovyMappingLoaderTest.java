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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
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
                contains(Equivalence.WIDER, Equivalence.WIDER, Equivalence.EQUAL));
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

    /**
     * The model holds strings, so a script mapping objects loads as their string form. That is a
     * silent change for callers of the deprecated MappingService, which used to get the objects
     * back, so the loader names every typed mapping.
     */
    @Test
    public void typedMappingsAreReported() throws Exception {
        var warnings = new ArrayList<String>();
        var source = GroovyMappingLoaderTest.class.getResource("/typed.map");
        List<Mapping> loaded;
        try (var in = source.openStream()) {
            loaded = new GroovyMappingLoader().load(in, source.toURI(), new MappingFunctionRegistry(), warnings::add);
        }

        assertThat(loaded, hasSize(4));
        assertThat(warnings, hasItem(allOf(startsWith("timeUnit:"),
                containsString("keys of type java.util.concurrent.TimeUnit"),
                containsString("values of type java.time.temporal.ChronoUnit"))));
        assertThat(warnings, hasItem(allOf(startsWith("religion:"),
                containsString("values of type java.lang.Integer"))));
        assertThat(warnings.stream().anyMatch(warning -> warning.startsWith("religion:") && warning.contains("keys")),
                is(false));
        assertThat(warnings.stream().anyMatch(warning -> warning.startsWith("plain:")), is(false));
    }

    /**
     * The string form is {@code toString()}, not the constant name: {@code ChronoUnit} overrides
     * it, which is one reason a typed mapping cannot simply be converted back to its enum.
     */
    @Test
    public void typedMappingsLoadAsTheirStringForm() {
        var mappings = Mappings.builder().load("classpath:/typed.map").build();

        assertThat(mappings, translates("timeUnit", "SECONDS").to("Seconds"));
        assertThat(mappings, translates("timeUnit", "DAYS").to("Forever"));
        assertThat(mappings, translates("religion", "EOT").to("1068"));
    }

    /**
     * A fallback closure's result only shows when it is called, so its type is reported then, and
     * only once.
     */
    @Test
    public void typedFallbackResultsAreReportedOnce() throws Exception {
        var warnings = new ArrayList<String>();
        var functions = new MappingFunctionRegistry();
        var source = GroovyMappingLoaderTest.class.getResource("/typed.map");
        try (var in = source.openStream()) {
            new GroovyMappingLoader().load(in, source.toURI(), functions, warnings::add);
        }
        warnings.clear();

        var fallback = functions.lookup("unitName#unmatched").orElseThrow();
        assertThat(fallback.apply("HOURS"), is("Hours"));
        assertThat(fallback.apply("DAYS"), is("Days"));

        assertThat(warnings, contains(allOf(startsWith("unitName:"),
                containsString("forward fallback computes values of type java.time.temporal.ChronoUnit"))));
    }

    /**
     * The DSL's former composite convention is gone, so a key or value containing the separator
     * is one string. Every mapping relying on the convention is named, because its callers used to
     * get Lists back.
     */
    @Test
    public void compositeMappingsAreReported() throws Exception {
        var warnings = new ArrayList<String>();
        var functions = new MappingFunctionRegistry();
        var source = GroovyMappingLoaderTest.class.getResource("/composite.map");
        List<Mapping> loaded;
        try (var in = source.openStream()) {
            loaded = new GroovyMappingLoader().load(in, source.toURI(), functions, warnings::add);
        }

        assertThat(loaded, hasSize(3));
        assertThat(loaded.get(0).entries().get(0), is(new Entry("SAP-ISH~HZL", "1.2.3~HZL")));
        assertThat(warnings, contains(allOf(startsWith("directory:"),
                containsString("key 'SAP-ISH~HZL'"),
                containsString("value '1.2.3~HZL'"),
                containsString("fallback '1.2.3.999~UNKNOWN'"),
                containsString("split the mapping"))));
    }

    /**
     * A fallback closure returning a List was a composite value; its result only shows when it is
     * called, so it is reported then, and not as a typed mapping.
     */
    @Test
    public void compositeFallbackResultsAreReported() throws Exception {
        var warnings = new ArrayList<String>();
        var functions = new MappingFunctionRegistry();
        var source = GroovyMappingLoaderTest.class.getResource("/composite.map");
        try (var in = source.openStream()) {
            new GroovyMappingLoader().load(in, source.toURI(), functions, warnings::add);
        }
        warnings.clear();

        var fallback = functions.lookup("namespace#unmatched").orElseThrow();
        assertThat(fallback.apply("X"), is("[, ]"));
        assertThat(fallback.apply("Y"), is("[, ]"));

        assertThat(warnings, contains(allOf(startsWith("namespace:"),
                containsString("forward fallback computes composite values such as ['', '']"),
                containsString("split the mapping"))));
    }
}
