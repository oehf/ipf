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
package org.openehealth.ipf.commons.map.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.SimpleMapping;
import org.openehealth.ipf.commons.map.Unmatched;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.MappingLoaders.forSource;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslate;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasEquivalence;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasReverseUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.mapsBetween;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class XmlMappingLoaderTest {

    private static final String ADDRESS_USE = "hl7v2fhir-address-use";
    private static final String ATNA_CODING_SYSTEM = "atnaCodingSystem";
    private static final String BIDI_GENDER = "hl7v2v3-bidi-administrativeGender";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String ENCOUNTER_CLASS = "encounterClass";
    private static final String ENCOUNTER_TYPE = "encounterType";
    private static final String GENDER = "hl7v2fhir-patient-administrativeGender";
    private static final String MARITAL_STATUS = "hl7v2v3-patient-maritalStatus";
    private static final String STRICT = "strictMapping";

    private static Mappings.Builder builder() {
        return Mappings.builder().function("oidUri", key -> key != null && !key.isEmpty()
                && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key);
    }

    private static Mappings example() {
        return builder().load("classpath:/example.mapping.xml").build();
    }

    @Test
    public void loaderIsDiscoveredForItsExtension() {
        assertThat(forSource(URI.create("file:/x/example.mapping.xml")), hasValue(instanceOf(XmlMappingLoader.class)));
        assertThat(forSource(URI.create("jar:file:/x/y.jar!/example.mapping.xml")),
                hasValue(instanceOf(XmlMappingLoader.class)));
        // .map still goes to the legacy loader, .xml on its own to neither
        assertThat(forSource(URI.create("file:/x/example.xml")), hasNoValue());
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
     * The empty string is a code, not a missing value.
     */
    @Test
    public void readsEmptyStringKeysAndValues() {
        var mappings = example();
        assertThat(mappings.lookup(BIDI_GENDER, ""), hasValue(""));
        assertThat(mappings.lookupReverse(BIDI_GENDER, ""), hasValue(""));
    }

    /**
     * The equivalence is what lets two keys share a value in a reversible mapping and still have
     * one unambiguous inverse.
     */
    @Test
    public void equivalenceDecidesTheInverse() {
        var mappings = example();
        var mapping = mappings.mapping(GENDER).orElseThrow();

        assertThat(mapping.entries().get(2), hasEquivalence(Equivalence.WIDER));
        assertThat(mapping.entries().get(3), hasEquivalence(Equivalence.EQUAL));
        assertThat(mappings, translates(GENDER, "A").to("other"));
        assertThat(mappings, translatesBack(GENDER, "other").to("O"));
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

    /**
     * A mapping specialises another by declaring what differs and delegating the rest.
     */
    @Test
    public void aDelegatingFallbackAsksTheNamedMapping() {
        var mappings = example();
        var custom = "hl7v2fhir-patient-administrativeGender-custom";

        assertThat(mappings.mapping(custom).orElseThrow(), hasUnmatched(Unmatched.delegate(GENDER)));
        assertThat(mappings, translates(custom, "CUSTOM").to("other"));
        assertThat(mappings, translates(custom, "M").to("male"));
        // the delegate's own fallback finally decides
        assertThat(mappings, translates(custom, "anything").to("other"));
    }

    @Test
    public void oneDirectionalMappingBuildsNoReverseIndex() {
        var mappings = example();
        assertThat(mappings, translates(ADDRESS_USE, "L").to("home"));
        assertThat(mappings, doesNotTranslateBack(ADDRESS_USE, "home"));
    }

    @Test
    public void readsASingleMappingAsTheDocumentRoot() {
        var mappings = Mappings.builder().load("classpath:/single.mapping.xml").build();
        assertThat(mappings, translates(ENCOUNTER_TYPE, "I").to("IMP"));
    }

    /**
     * The point of the schema: a misspelt attribute is an error naming the file, the line and the
     * column, not a silently ignored declaration.
     */
    @Test
    public void misspeltAttributeIsRejectedWithALocation() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/unknown-attribute.mapping.xml"));
        assertThat(e.getMessage(), containsString("keysystem"));
        assertThat(e.getMessage(), containsString("at line 2"));
        assertThat(e.getMessage(), containsString("unknown-attribute.mapping.xml"));
    }

    @Test
    public void functionModeRequiresARegisteredFunction() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/example.mapping.xml"));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("'oidUri', which is not registered"));
    }

    /**
     * A mapping file can be declared as replacing one loaded earlier, which is what the Groovy
     * DSL did silently for every duplicate name.
     */
    @Test
    public void overrideIsDeclaredInTheFile() {
        var mappings = Mappings.builder()
                .load("classpath:/single.mapping.xml")
                .load("classpath:/overriding.mapping.xml")
                .build();
        assertThat(mappings, translates(ENCOUNTER_TYPE, "E").to("EMERGENCY"));
        assertThat(mappings, doesNotTranslate(ENCOUNTER_TYPE, "I"));

        var builder = Mappings.builder().load("classpath:/single.mapping.xml");
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/single.mapping.xml"));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("Duplicate mapping 'encounterType'"));
    }

    @Test
    public void readsDisplays() {
        var entries = example().mapping(MARITAL_STATUS).orElseThrow().entries();

        assertThat(entries.get(0).keyDisplay(), is("Divorced"));
        assertThat(entries.get(0).valueDisplay(), is("Divorced"));
        assertThat(entries.get(1).keyDisplay(), is(nullValue()));
    }

    /**
     * A disjoint entry is a declaration that the two concepts are not equivalent, so it is read
     * and kept, but it does not answer a lookup.
     */
    @Test
    public void readsButDoesNotApplyADisjointEntry() {
        var mappings = example();

        assertThat(mappings.mapping(MARITAL_STATUS).orElseThrow().entries().get(2).equivalence(),
                is(Equivalence.DISJOINT));
        assertThat(mappings.lookup(MARITAL_STATUS, "X"), hasNoValue());
        // the mapping's own fallback still applies, as it does to any key it does not translate
        assertThat(mappings, translates(MARITAL_STATUS, "X").to("X"));
    }

    /**
     * A fallback that derives its answer from the key, which is what an arbitrary (ELSE) closure
     * in the Groovy DSL was for. The XML names a function; the function is Java.
     */
    @Test
    public void computedFallbackDerivesTheValueFromTheKey() {
        var mappings = Mappings.builder()
                .function("first4", key -> key == null || key.length() <= 4 ? key : key.substring(0, 4))
                .load("classpath:/dynamic.mapping.xml")
                .build();

        assertThat(mappings, translates(DEVICE_TYPE, "LEGACY").to("legacy-device"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
    }

    /**
     * Formats are chosen per file, not per project: one Mappings instance holds both.
     */
    @Test
    public void formatsMixInOneMappingsInstance() {
        var mappings = builder()
                .load("classpath:/example.mapping.xml")
                .load("classpath:/mixed.map")
                .build();
        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, translates(ATNA_CODING_SYSTEM, "1.2.3").to("urn:oid:1.2.3"));
        assertThat(mappings, translates(ENCOUNTER_CLASS, "I").to("IMP"));
        assertThat(mappings, translates(ENCOUNTER_CLASS, "X").to("X"));
    }

    /**
     * Every value is an attribute, and a parser turns a literal tab, line feed or carriage return
     * in an attribute into a space; written as character references they come back as they were.
     * A character XML cannot represent at all is rejected rather than written unreadably.
     */
    @Test
    public void whitespaceSurvivesAndIllegalCharactersAreRejected(@TempDir Path directory) throws IOException {
        var mapping = SimpleMapping.builder("whitespace")
                .entry(new Entry("a\tb", "c\nd", Equivalence.EQUAL, "tab\tkey", "line\r\nvalue"))
                .build();

        var target = directory.resolve("whitespace.mapping.xml");
        Files.writeString(target, new XmlMappingWriter().toXml(List.of(mapping)));
        assertThat(Mappings.builder().load(target.toUri()).build().mapping("whitespace").orElseThrow(), is(mapping));

        var control = SimpleMapping.builder("control").entry("a\u0001", "b").build();
        var e = assertThrows(IllegalArgumentException.class, () -> new XmlMappingWriter().toXml(List.of(control)));
        assertThat(e.getMessage(), containsString("a character XML cannot represent"));
    }
}
