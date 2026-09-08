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
package org.openehealth.ipf.commons.map.yaml;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingLoaders;
import org.openehealth.ipf.commons.map.MappingWriters;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.Unmatched;

import java.net.URI;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.MappingLoaders.forSource;
import static org.openehealth.ipf.commons.map.MappingWriters.extensions;
import static org.openehealth.ipf.commons.map.MappingWriters.forExtension;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasEquivalence;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasReverseUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.mapsBetween;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class YamlMappingLoaderTest {

    private static final String ADDRESS_USE = "hl7v2fhir-address-use";
    private static final String ATNA_CODING_SYSTEM = "atnaCodingSystem";
    private static final String BIDI_GENDER = "hl7v2v3-bidi-administrativeGender";
    private static final String GENDER = "hl7v2fhir-patient-administrativeGender";
    private static final String MARITAL_STATUS = "hl7v2v3-patient-maritalStatus";
    private static final String STRICT = "strictMapping";

    private static Mappings.Builder builder() {
        return Mappings.builder().function("oidUri", key -> key != null && !key.isEmpty()
                && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key);
    }

    private static Mappings example() {
        return builder().load("classpath:/example.mapping.yaml").build();
    }

    @Test
    public void loaderIsDiscoveredForItsExtension() {
        assertThat(forSource(URI.create("file:/x/example.mapping.yaml")),
                hasValue(instanceOf(YamlMappingLoader.class)));
        assertThat(forSource(URI.create("file:/x/example.mapping.yml")), hasValue(instanceOf(YamlMappingLoader.class)));
        assertThat(forSource(URI.create("jar:file:/x/y.jar!/example.mapping.yaml")),
                hasValue(instanceOf(YamlMappingLoader.class)));
        // plain .yaml says nothing about which of the many YAML formats a file holds
        assertThat(forSource(URI.create("file:/x/example.yaml")), hasNoValue());
    }

    @Test
    public void writerIsDiscoveredForItsExtension() {
        assertThat(forExtension(".mapping.yaml"), hasValue(instanceOf(YamlMappingWriter.class)));
        assertThat(extensions(), hasItem(".mapping.yaml"));
    }

    @Test
    public void readsEntriesAndCodeSystems() {
        var mappings = example();

        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, mapsBetween(GENDER, "2.16.840.1.113883.12.1",
                "http://hl7.org/fhir/administrative-gender"));
        assertThat(mappings.keys(GENDER),
                contains("M", "F", "A", "O", "U"));
    }

    /**
     * The scalar shorthand and the object form of an entry, side by side in one mapping.
     */
    @Test
    public void equivalenceDecidesTheInverse() {
        var mappings = example();
        var mapping = mappings.mapping(GENDER).orElseThrow();

        assertThat(mapping.entries().get(2), hasEquivalence(Equivalence.NARROWER));
        assertThat(mapping.entries().get(3), hasEquivalence(Equivalence.EQUAL));
        assertThat(mappings, translatesBack(GENDER, "other").to("O"));
    }

    @Test
    public void readsEmptyStringKeysAndValues() {
        var mappings = example();
        assertThat(mappings.lookup(BIDI_GENDER, ""), hasValue(""));
        assertThat(mappings.lookupReverse(BIDI_GENDER, ""), hasValue(""));
        assertThat(mappings, translates(ADDRESS_USE, "X").to(""));
    }

    @Test
    public void readsAllUnmatchedModes() {
        var mappings = example();

        assertThat(mappings.mapping(BIDI_GENDER).orElseThrow(), hasUnmatched(Unmatched.fixed("O")));
        assertThat(mappings.mapping(BIDI_GENDER).orElseThrow(), hasReverseUnmatched(Unmatched.fixed("UN")));
        assertThat(mappings.mapping(MARITAL_STATUS).orElseThrow(), hasUnmatched(Unmatched.IDENTITY));
        assertThat(mappings.mapping(ATNA_CODING_SYSTEM).orElseThrow(), hasUnmatched(Unmatched.computed("oidUri")));
        assertThat(mappings.mapping(STRICT).orElseThrow(), hasUnmatched(Unmatched.FAIL));
        assertThat(mappings.mapping(ADDRESS_USE).orElseThrow(), hasReverseUnmatched(Unmatched.ABSENT));

        assertThat(mappings, translates(BIDI_GENDER, "X").to("O"));
        assertThat(mappings, translatesBack(BIDI_GENDER, "X").to("UN"));
        assertThat(mappings, translates(MARITAL_STATUS, "X").to("X"));
        assertThat(mappings, translates(ATNA_CODING_SYSTEM, "1.2.3").to("urn:oid:1.2.3"));
        assertThat(mappings, translates(ATNA_CODING_SYSTEM, "IHE").to("IHE"));
        assertThrows(IllegalArgumentException.class, () -> mappings.map(STRICT, "X"));
    }

    @Test
    public void aDelegatingFallbackAsksTheNamedMapping() {
        var mappings = example();
        var custom = "hl7v2fhir-patient-administrativeGender-custom";

        assertThat(mappings.mapping(custom).orElseThrow(), hasUnmatched(Unmatched.delegate(GENDER)));
        assertThat(mappings, translates(custom, "CUSTOM").to("other"));
        assertThat(mappings, translates(custom, "M").to("male"));
        assertThat(mappings, translates(custom, "anything").to("other"));
    }

    @Test
    public void oneDirectionalMappingBuildsNoReverseIndex() {
        var mappings = example();
        assertThat(mappings, translates(ADDRESS_USE, "L").to("home"));
        assertThat(mappings, doesNotTranslateBack(ADDRESS_USE, "home"));
    }

    /**
     * The point of binding strictly: a misspelled property is an error naming the file, the line
     * and the column.
     */
    @Test
    public void misspelledPropertyIsRejectedWithALocation() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/typo.mapping.yaml"));
        assertThat(e.getMessage(), containsString("keysystem"));
        assertThat(e.getMessage(), containsString("at line"));
        assertThat(e.getMessage(), containsString("typo.mapping.yaml"));
    }

    /**
     * An unquoted numeric code is how hl7v2fhir-patient-religion came to return Integers where
     * every other mapping returned Strings. YAML makes the same mistake available, so the loader
     * refuses it instead of coercing.
     */
    @Test
    public void unquotedNumberIsRejected() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/unquoted-number.mapping.yaml"));
        assertThat(e.getMessage(), containsString("non-string value 1004"));
        assertThat(e.getMessage(), containsString("quote it"));
    }

    @Test
    public void functionModeRequiresARegisteredFunction() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/example.mapping.yaml"));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("'oidUri', which is not registered"));
    }

    @Test
    public void overrideIsDeclaredInTheFile() {
        var mappings = builder()
                .load("classpath:/example.mapping.yaml")
                .load("classpath:/override.mapping.yaml")
                .build();
        assertThat(mappings, translates(MARITAL_STATUS, "D").to("DIVORCED"));

        var builder = builder().load("classpath:/example.mapping.yaml");
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/example.mapping.yaml"));
        assertThat(e.getMessage(), containsString("Duplicate mapping"));
    }

    /**
     * Formats are chosen per file, not per project: one Mappings instance holds YAML and the
     * legacy script side by side.
     */
    @Test
    public void formatsMixInOneMappingsInstance() {
        var mappings = builder()
                .allowOverride(true)
                .load("classpath:/example.mapping.yaml")
                .load("classpath:/legacy.map")
                .build();
        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, translates("hl7v2fhir-patient-religion", "AGN").to("1004"));
    }
}
