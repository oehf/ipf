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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslate;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasEquivalence;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.mapsBetween;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class MappingsTest {

    private static final String ADDRESS_USE = "addressUse";
    private static final String CHAINED_GENDER = "chainedGender";
    private static final String COMPUTED = "computed";
    private static final String CUSTOM = "custom";
    private static final String CUSTOM_GENDER = "customGender";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String DISJOINTNESS = "disjointness";
    private static final String EMPTY_VALUE = "emptyValue";
    private static final String ENCOUNTER_TYPE = "encounterType";
    private static final String GENDER = "gender";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String RELIGION = "religion";
    private static final String VIP = "vip";

    private static Mappings example() {
        return Mappings.builder().load("classpath:/example.testmap").build();
    }

    @Test
    public void loadsThroughTheLoaderRegisteredForTheExtension() {
        var mappings = example();

        assertThat(mappings.mappingNames(), hasItem(ENCOUNTER_TYPE));
        assertThat(mappings, translates(ENCOUNTER_TYPE, "I").to("IMP"));
        assertThat(mappings, doesNotTranslate(ENCOUNTER_TYPE, "X"));
        assertThat(mappings.map(ENCOUNTER_TYPE, "X", "WRONG"), is("WRONG"));
        assertThat(mappings, translatesBack(ENCOUNTER_TYPE, "IMP").to("I"));
        assertThat(mappings, mapsBetween(ENCOUNTER_TYPE, "2.16.840.1.113883.12.4",
                "2.16.840.1.113883.5.4"));
        // keys and values come back in declaration order, which is part of the contract
        assertThat(mappings.keys(ENCOUNTER_TYPE), contains("E", "I", "O"));
        assertThat(mappings.values(ENCOUNTER_TYPE), contains("EMER", "IMP", "AMB"));
    }

    @Test
    public void keySystemsAreOptionalNotNull() {
        assertThat(example().keySystem(MESSAGE_TYPE), hasNoValue());
    }

    /**
     * An unknown mapping name is a programming error, an unknown key is not.
     */
    @Test
    public void unknownMappingNameIsRejected() {
        var mappings = example();
        var e = assertThrows(IllegalArgumentException.class, () -> mappings.map("BLABLA", "X"));
        assertThat(e.getMessage(), is("Unknown key BLABLA"));
    }

    @Test
    public void unmatchedModes() {
        var mappings = Mappings.builder()
                .function("oidUri", key -> key != null && !key.isEmpty() && Character.isDigit(key.charAt(0))
                        ? "urn:oid:" + key : key)
                .mapping(Mapping.builder("absent").entry("a", "b").build())
                .mapping(Mapping.builder("identity").unmatched(Unmatched.IDENTITY).build())
                .mapping(Mapping.builder("fixed").unmatched(Unmatched.fixed("UNK")).build())
                .mapping(Mapping.builder(COMPUTED).unmatched(Unmatched.computed("oidUri")).build())
                .mapping(Mapping.builder("failing").unmatched(Unmatched.FAIL).build())
                .build();

        assertThat(mappings, doesNotTranslate("absent", "x"));
        assertThat(mappings, translates("identity", "x").to("x"));
        assertThat(mappings, translates("fixed", "x").to("UNK"));
        assertThat(mappings, translates(COMPUTED, "1.2.3").to("urn:oid:1.2.3"));
        assertThat(mappings, translates(COMPUTED, "DCM").to("DCM"));
        assertThrows(IllegalArgumentException.class, () -> mappings.map("failing", "x"));
    }

    /**
     * A fallback can derive its answer from the key instead of listing it - the one thing the
     * Groovy DSL's arbitrary (ELSE) closures were genuinely needed for. The function is named in
     * the mapping file and written in Java, so it is statically typed, unit-testable on its own,
     * and visible to a debugger.
     */
    @Test
    public void computedFallbackDerivesTheValueFromTheKey() {
        var mappings = Mappings.builder()
                .function("first4", key -> key == null || key.length() <= 4 ? key : key.substring(0, 4))
                .load("classpath:/dynamic.testmap")
                .build();

        assertThat(mappings, translates(DEVICE_TYPE, "LEGACY").to("legacy-device"));
        assertThat(mappings, translates(DEVICE_TYPE, "ABCDEFGH").to("ABCD"));
        assertThat(mappings, translates(DEVICE_TYPE, "AB").to("AB"));
        assertThat(mappings, doesNotTranslate(DEVICE_TYPE, null));
    }

    @Test
    public void unmatchedAppliesToBothDirectionsIndependently() {
        var mappings = example();
        assertThat(mappings, translates(GENDER, "X").to("other"));
        assertThat(mappings, translatesBack(GENDER, "X").to("U"));
        assertThat(mappings, translates(VIP, "X").to("X"));
        assertThat(mappings, doesNotTranslateBack(VIP, "X"));
    }

    /**
     * The empty string is a code, not a missing value: it has an entry of its own and does not
     * reach the fallback.
     */
    @Test
    public void emptyStringIsACode() {
        var mappings = example();
        assertThat(mappings, translates(EMPTY_VALUE, "").to(""));
        assertThat(mappings, translates(EMPTY_VALUE, "X").to(""));
        assertThat(mappings, translates(EMPTY_VALUE, "unknown").to("FALLBACK"));
    }

    @Test
    public void duplicateMappingNameIsRejected() {
        var builder = Mappings.builder().load("classpath:/example.testmap");
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/duplicate.testmap"));
        assertThat(e.getMessage(), allOf(
                containsString("Duplicate mapping 'messageType'"),
                containsString("duplicate.testmap")));
    }

    /**
     * A replacement is either declared by the later mapping itself or allowed for the whole
     * builder, never silent.
     */
    @Test
    public void overrideIsDeclaredEitherInTheFileOrOnTheBuilder() {
        var byDeclaration = Mappings.builder()
                .load("classpath:/example.testmap")
                .load("classpath:/override.testmap")
                .build();
        assertThat(byDeclaration, translates(MESSAGE_TYPE, "ADT^A04").to("PRPA_IN401001"));
        assertThat(byDeclaration, doesNotTranslate(MESSAGE_TYPE, "ADT^A01"));

        var byBuilder = Mappings.builder()
                .allowOverride(true)
                .load("classpath:/example.testmap")
                .load("classpath:/duplicate.testmap")
                .build();
        assertThat(byBuilder, translates(MESSAGE_TYPE, "ADT^A04").to("PRPA_IN401001"));
    }

    /**
     * Two entries that both claim to be equal to the same value cannot both be its inverse.
     * Declaring the equivalence is what resolves it.
     */
    @Test
    public void reverseCollisionIsRejected() {
        var colliding = Mapping.builder("maritalStatus")
                .entry("P", "T")
                .entry("R", "T")
                .build();
        var e = assertThrows(MappingException.class, () -> Mappings.builder().mapping(colliding).build());
        assertThat(e.getMessage(), containsString("maps both 'P' and 'R' onto 'T'"));

        // the same shape, resolved by an equivalence, is what example.testmap declares
        var mappings = example();
        assertThat(mappings, translates(GENDER, "A").to("other"));
        assertThat(mappings, translatesBack(GENDER, "other").to("O"));
    }

    @Test
    public void oneDirectionalMappingMayShareValues() {
        var mappings = example();
        assertThat(mappings, translates(ADDRESS_USE, "L").to("home"));
        assertThat(mappings, doesNotTranslateBack(ADDRESS_USE, "home"));
    }

    /**
     * A mapping specialises another by declaring only what differs and delegating the rest. The
     * delegate is asked in full - its entries and then its own fallback - so it is the last
     * mapping in the chain that finally decides.
     */
    @Test
    public void aDelegatingFallbackAsksAnotherMappingInFull() {
        var mappings = example();

        // its own entry wins
        assertThat(mappings, translates(CUSTOM_GENDER, "X").to("non-binary"));
        // then the delegate's entries
        assertThat(mappings, translates(CUSTOM_GENDER, "M").to("male"));
        // and finally the delegate's own fallback, which is what decides the outcome
        assertThat(mappings, translates(CUSTOM_GENDER, "anything").to("UNK"));

        // lookup() is the declaration, so it does not follow the delegation
        assertThat(mappings.lookup(CUSTOM_GENDER, "M"), hasNoValue());
        // nor do keys(): those are the keys this mapping declares
        assertThat(mappings.keys(CUSTOM_GENDER), contains("X"));
    }

    @Test
    public void delegationChains() {
        var mappings = example();

        assertThat(mappings, translates(CHAINED_GENDER, "Z").to("other"));
        assertThat(mappings, translates(CHAINED_GENDER, "X").to("non-binary"));
        assertThat(mappings, translates(CHAINED_GENDER, "F").to("female"));
        assertThat(mappings, translates(CHAINED_GENDER, "anything").to("UNK"));
    }

    /**
     * The delegate must already be registered. That is what makes a cycle impossible to declare -
     * reaching one would need a forward reference - and it turns a typo into a load-time error.
     */
    @Test
    public void aDelegateMustAlreadyBeRegistered() {
        var absent = Mapping.builder("m").unmatched(Unmatched.delegate("nowhere")).build();
        var e = assertThrows(MappingException.class, () -> Mappings.builder().mapping(absent).build());
        assertThat(e.getMessage(), containsString("delegates to 'nowhere', which is not registered"));

        // ... and overriding a link in an existing chain is checked again from its own end
        var builder = Mappings.builder()
                .allowOverride(true)
                .mapping(Mapping.builder("a").entry("k", "v").build())
                .mapping(Mapping.builder("b").unmatched(Unmatched.delegate("a")).build());
        var cycle = Mapping.builder("a").unmatched(Unmatched.delegate("b")).build();
        var cyclic = assertThrows(MappingException.class, () -> builder.mapping(cycle));
        assertThat(cyclic.getMessage(), containsString("delegates in a cycle"));
    }

    /**
     * Delegation is per direction: a mapping may hand the reverse direction to another mapping
     * without handing over the forward one.
     */
    @Test
    public void theReverseDirectionDelegatesSeparately() {
        var mappings = Mappings.builder()
                .mapping(Mapping.builder("base").entry("M", "male").entry("F", "female").build())
                .mapping(Mapping.builder(CUSTOM)
                        .entry("X", "non-binary")
                        .reverseUnmatched(Unmatched.delegate("base"))
                        .build())
                .build();

        assertThat(mappings, translatesBack(CUSTOM, "non-binary").to("X"));
        assertThat(mappings, translatesBack(CUSTOM, "male").to("M"));
        assertThat(mappings, doesNotTranslateBack(CUSTOM, "nothing"));
        // the forward direction was not delegated
        assertThat(mappings, doesNotTranslate(CUSTOM, "M"));
    }

    @Test
    public void unknownFunctionIsRejectedAtLoadTime() {
        var mapping = Mapping.builder("m").unmatched(Unmatched.computed("nope")).build();
        var e = assertThrows(MappingException.class, () -> Mappings.builder().mapping(mapping).build());
        assertThat(e.getMessage(), containsString("unknown mapping function 'nope'"));
    }

    /**
     * A disjoint entry asserts that its key and value are explicitly <em>not</em> equivalent, so
     * answering a lookup with its value would state the opposite of what the mapping says. FHIR
     * draws the same line: the result of a $translate cannot be true for a disjoint match.
     */
    @Test
    public void aDisjointEntryDoesNotTranslate() {
        var mappings = example();

        assertThat(mappings, translates(DISJOINTNESS, "a").to("b"));
        assertThat(mappings.lookup(DISJOINTNESS, "x"), hasNoValue());
        assertThat(mappings, doesNotTranslateBack(DISJOINTNESS, "y"));
        // it is not a translation, so it does not appear among the keys the mapping translates
        assertThat(mappings.keys(DISJOINTNESS), contains("a"));
        assertThat(mappings.values(DISJOINTNESS), not(hasItem("y")));
        // the declaration itself survives, so it round-trips through a writer
        var mapping = mappings.mapping(DISJOINTNESS).orElseThrow();
        assertThat(mapping.entries(), hasSize(2));
        assertThat(mapping.entries().get(1), hasEquivalence(Equivalence.DISJOINT));
        assertThat(mapping.entries().get(1).isTranslation(), is(false));
    }

    /**
     * A caller holding a code to translate knows its code system and the one it wants, not what
     * the mapping is called - which is how FHIR's $translate is addressed, and the only way to
     * reach a mapping read from a source that names itself.
     */
    @Test
    public void mappingsCanBeFoundByTheCodeSystemsTheyTranslateBetween() {
        var mappings = example();

        assertThat(namesOf(mappings.mappingsFor("2.16.840.1.113883.12.4", "2.16.840.1.113883.5.4")),
                contains(ENCOUNTER_TYPE));
        assertThat(mappings.mappingFor("2.16.840.1.113883.12.4", "2.16.840.1.113883.5.4")
                .orElseThrow().name(), is(ENCOUNTER_TYPE));

        // either side may be left open
        assertThat(namesOf(mappings.mappingsFor("2.16.840.1.113883.12.4", null)),
                contains(ENCOUNTER_TYPE));
        assertThat(namesOf(mappings.mappingsFor(null, "2.16.840.1.113883.5.5")), contains(GENDER));

        assertThat(namesOf(mappings.mappingsFor("urn:oid:9.9.9", null)), is(empty()));
        assertThat(mappings.mappingFor("urn:oid:9.9.9", null), hasNoValue());
    }

    /**
     * Two mappings between the same pair of systems is not an error - only picking one of them
     * silently would be.
     */
    @Test
    public void anAmbiguousCodeSystemPairIsReported() {
        var mappings = example();

        assertThat(namesOf(mappings.mappingsFor("urn:oid:1.2.3", "urn:oid:4.5.6")),
                contains(DISJOINTNESS, "sameSystems"));
        var e = assertThrows(IllegalArgumentException.class,
                () -> mappings.mappingFor("urn:oid:1.2.3", "urn:oid:4.5.6"));
        assertThat(e.getMessage(), containsString("[disjointness, sameSystems]"));
    }

    private static List<String> namesOf(List<Mapping> mappings) {
        return mappings.stream().map(Mapping::name).toList();
    }

    @Test
    public void entriesDefaultToEqualEquivalence() {
        assertThat(new Entry("a", "b", null), hasEquivalence(Equivalence.EQUAL));
        assertThat(new Entry("a", "b").isInvertible(), is(true));
        assertThat(new Entry("a", "b", Equivalence.NARROWER).isInvertible(), is(false));
        assertThat(new Entry("a", "b", Equivalence.NARROWER).isTranslation(), is(true));
        assertThat(new Entry("a", "b", Equivalence.DISJOINT).isTranslation(), is(false));
    }

    /**
     * Displays are informative - nothing is looked up by one - and optional in every format.
     */
    @Test
    public void entriesMayCarryDisplays() {
        var entry = new Entry("M", "male").withDisplays("Male", "Male gender");
        assertThat(entry.keyDisplay(), is("Male"));
        assertThat(entry.valueDisplay(), is("Male gender"));
        assertThat(new Entry("M", "male", Equivalence.EQUAL, null, null), is(new Entry("M", "male")));
    }

    @Test
    public void loaderIsSelectedByFileExtension() {
        assertThat(MappingLoaders.forSource(URI.create("file:/x/example.testmap")),
                hasValue(instanceOf(TestMappingLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("jar:file:/x/y.jar!/example.testmap")),
                hasValue(instanceOf(TestMappingLoader.class)));
        assertThat(MappingLoaders.forSource(URI.create("file:/x/example.map")), hasNoValue());
    }

    /**
     * A format whose module is not on the classpath fails by name, saying which module to add.
     */
    @Test
    public void unsupportedFormatNamesTheModuleToAdd() {
        var builder = Mappings.builder();
        var e = assertThrows(MappingException.class, () -> builder.load("classpath:/log4j2.xml"));
        assertThat(e.getMessage(), allOf(
                containsString("No mapping loader supports this source"),
                containsString("ipf-commons-map-groovy"),
                // and it points at the way out for a source not recognizable by its name
                containsString("name the format")));
    }

    /**
     * A source whose name says nothing about its format - a location without an extension, a
     * ConceptMap fetched from a terminology server - is readable by naming the format.
     */
    @Test
    public void loaderCanBeSelectedByFormatId() {
        var mappings = Mappings.builder()
                .load("classpath:/extensionless", TestMappingLoader.FORMAT)
                .build();

        assertThat(mappings, translates(RELIGION, "LUT").to("Lutheran"));
        assertThat(MappingLoaders.formats(), hasItem(TestMappingLoader.FORMAT));
    }

    /**
     * The format id is what selects the loader, so it is compared case-insensitively - a value
     * read from a configuration file is not required to match the declaration's spelling.
     */
    @Test
    public void formatIdsAreCaseInsensitive() {
        assertThat(MappingLoaders.forFormat("TestMap"), hasValue(instanceOf(TestMappingLoader.class)));
        assertThat(MappingLoaders.forFormat(" testmap "), hasValue(instanceOf(TestMappingLoader.class)));
        assertThat(MappingLoaders.forFormat("  "), hasNoValue());
        assertThat(MappingLoaders.forFormat(null), hasNoValue());
    }

    /**
     * A named format that nothing implements fails by name and lists what could be read instead,
     * rather than falling back to the extension and reading the file in the wrong format.
     */
    @Test
    public void unknownFormatIsRejectedByName() {
        var builder = Mappings.builder();

        var e = assertThrows(MappingException.class,
                () -> builder.load("classpath:/example.testmap", "toml"));

        assertThat(e.getMessage(), allOf(
                containsString("No mapping loader implements the format 'toml'"),
                containsString(TestMappingLoader.FORMAT)));
    }

    /**
     * A loader may also be handed over directly, which is what a caller with a loader that is not
     * registered as a service does.
     */
    @Test
    public void loaderCanBePassedDirectly() {
        var mappings = Mappings.builder()
                .load(Mappings.class.getResource("/extensionless"), new TestMappingLoader())
                .build();

        assertThat(mappings, translates(RELIGION, "LUT").to("Lutheran"));
    }

    @Test
    public void aBuilderIsSpentOnceItIsBuilt() {
        var builder = Mappings.builder();
        builder.build();
        assertThrows(IllegalStateException.class, () -> builder.load("classpath:/example.testmap"));
    }
}
