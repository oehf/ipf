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
package org.openehealth.ipf.commons.ihe.fhir.support.map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.CompositeMapping;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingConverter;
import org.openehealth.ipf.commons.map.MappingWriters;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.SimpleMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class ConceptMapWriterTest {

    private static final String ADDRESS_USE = "hl7v2fhir-address-use";
    private static final String BASE = "hl7v2fhir_base";
    private static final String CUSTOM_GENDER = "hl7v2fhir_custom";
    private static final String GENDER = "hl7v2fhir-patient-administrativeGender";

    private static final Function<String, String> OID_URI = key -> key != null && !key.isEmpty()
            && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key;

    private static Mappings.Builder builder() {
        return Mappings.builder().function("oidUri", OID_URI);
    }

    @Test
    public void writerIsDiscoveredForItsExtension() {
        assertThat(MappingWriters.forExtension(".conceptmap.r4.json"), hasValue(instanceOf(ConceptMapWriter.class)));
        assertThat(MappingWriters.extensions(), hasItem(".conceptmap.r4.json"));
    }

    /**
     * Everything the model holds, including the four things ConceptMap has no field for, survives
     * being written and read back.
     */
    @Test
    public void roundTripsEveryPartOfTheModel(@TempDir Path directory) throws IOException {
        var original = builder()
                .load("classpath:/example.conceptmap.r4.json")
                .load("classpath:/multi-group.conceptmap.r4.xml")
                .build();
        // a composite is no table of its own, so no format writes it; its parts are written
        var mappings = original.mappingNames().stream()
                .map(name -> original.mapping(name).orElseThrow())
                .filter(mapping -> !(mapping instanceof CompositeMapping))
                .toList();

        var target = directory.resolve("round-trip.conceptmap.r4.json");
        Files.writeString(target, new ConceptMapWriter().toConceptMap(mappings));
        var reloaded = builder().load(target.toUri()).build();

        assertThat(List.copyOf(reloaded.mappingNames()), is(mappings.stream().map(Mapping::name).toList()));
        for (var mapping : mappings) {
            assertThat("mapping " + mapping.name() + " did not survive the round trip",
                    reloaded.mapping(mapping.name()).orElseThrow(), is(mapping));
        }
    }

    /**
     * A mapping maintained as an IPF XML table can be published as the standard resource, which
     * is the point of having a writer at all.
     */
    @Test
    public void convertsTheXmlFormatToAPublishableConceptMap(@TempDir Path directory) throws IOException {
        var source = copy("/publishable.mapping.xml", directory.resolve("publishable.mapping.xml"));
        new MappingConverter().convert(source, directory, ".conceptmap.r4.json");

        var target = directory.resolve("publishable.conceptmap.r4.json");
        assertThat(Files.exists(target), is(true));
        var json = Files.readString(target);
        assertThat(json, containsString("\"resourceType\": \"ConceptMap\""));
        assertThat(json, containsString("mapping-name"));
        assertThat(json, containsString("reverse-unmapped"));
        assertThat(json, containsString("reversible"));

        var mappings = Mappings.builder().load(target.toUri()).build();
        assertThat(mappings, translates(GENDER, "M").to("male"));
        assertThat(mappings, translatesBack(GENDER, "other").to("O"));
        assertThat(mappings, translatesBack(GENDER, "ZZZ").to("O"));
        assertThat(mappings, translates(ADDRESS_USE, "L").to("home"));
        assertThat(mappings, doesNotTranslateBack(ADDRESS_USE, "home"));
    }

    /**
     * A FHIR code is never the empty string, and two of the mappings IPF ships use it as one. The
     * writer says so rather than dropping the entry, which is what HAPI would otherwise do
     * silently on serialization.
     */
    @Test
    public void emptyCodesCannotBePublishedAsAConceptMap(@TempDir Path directory) throws IOException {
        var source = copy("/META-INF/map/hl7-v2-v3-translation.mapping.xml",
                directory.resolve("hl7-v2-v3-translation.mapping.xml"));
        var converter = new MappingConverter();
        var conversion = converter.read(source.toUri().toURL());

        var e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> converter.render(conversion.mappings(), ".conceptmap.r4.json"));
        assertThat(e.getMessage(), containsString("cannot express the missing or empty key"));
        assertThat(e.getMessage(),
                e.getMessage(),
                containsString("hl7v2v3-bidi-administrativeGender"));
    }

    private Path copy(String resource, Path target) throws IOException {
        // the mapping may ship inside another module's jar, so copy it out before converting
        try (var in = getClass().getResourceAsStream(resource)) {
            Files.copy(java.util.Objects.requireNonNull(in), target);
        }
        return target;
    }

    /**
     * Displays go back into element.display and target.display, so a ConceptMap read and written
     * again keeps the names it came with.
     */
    @Test
    public void displaysSurviveTheRoundTrip(@TempDir Path directory) throws IOException {
        var original = builder().load("classpath:/kdl-ihe-typecode.conceptmap.r4.json").build();
        // a composite is no table of its own, so no format writes it; its parts are written
        var mappings = original.mappingNames().stream()
                .map(name -> original.mapping(name).orElseThrow())
                .filter(mapping -> !(mapping instanceof CompositeMapping))
                .toList();

        var json = new ConceptMapWriter().toConceptMap(mappings);
        assertThat(json, containsString("Ärztliche Stellungnahme"));
        assertThat(json, containsString("Arztberichte"));

        var target = directory.resolve("kdl.conceptmap.r4.json");
        Files.writeString(target, json);
        var reloaded = builder().load(target.toUri()).build();
        for (var mapping : mappings) {
            assertThat(reloaded.mapping(mapping.name()).orElseThrow(), is(mapping));
        }
    }

    /**
     * A delegating fallback is native in ConceptMap - {@code unmapped.mode = other-map} - and the
     * mapping name has to survive the url verbatim for the round trip to hold.
     */
    @Test
    public void delegationRoundTripsThroughOtherMap(@TempDir Path directory) throws IOException {
        var base = SimpleMapping.builder(BASE)
                .entry("M", "male")
                .unmatched(org.openehealth.ipf.commons.map.Unmatched.fixed("unknown"))
                .build();
        var custom = SimpleMapping.builder(CUSTOM_GENDER)
                .entry("CUSTOM", "other")
                .unmatched(org.openehealth.ipf.commons.map.Unmatched.delegate(BASE))
                .build();

        var json = new ConceptMapWriter().toConceptMap(List.of(base, custom));
        assertThat(json, containsString("other-map"));
        assertThat(json, containsString("ConceptMap/hl7v2fhir_base"));

        var target = directory.resolve("chain.conceptmap.r4.json");
        Files.writeString(target, json);
        var reloaded = Mappings.builder().load(target.toUri()).build();

        assertThat(reloaded.mapping(BASE).orElseThrow(), is(base));
        assertThat(reloaded.mapping(CUSTOM_GENDER).orElseThrow(), is(custom));
        assertThat(reloaded, translates(CUSTOM_GENDER, "anything").to("unknown"));
    }

    @Test
    public void unwritableBehaviorIsRejectedRatherThanDropped() {
        var padded = SimpleMapping.builder("padded").entry(" a ", "b").build();
        var e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ConceptMapWriter().toConceptMap(List.of(padded)));
        assertThat(e.getMessage(), containsString("FHIR trims leading and trailing whitespace"));

        var nested = SimpleMapping.builder("nested")
                .entry("a", "b")
                .unmatched(org.openehealth.ipf.commons.map.Unmatched.delegate("dir/base"))
                .build();
        e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ConceptMapWriter().toConceptMap(List.of(nested)));
        assertThat(e.getMessage(), containsString("cannot contain '/'"));
    }

    /**
     * A forward fallback that fails and the override flag have no field in ConceptMap; IPF
     * extensions carry them, so they survive being written and read back.
     */
    @Test
    public void failingFallbackAndOverrideRoundTrip(@TempDir Path directory) throws IOException {
        var mapping = SimpleMapping.builder("strict")
                .entry("a", "b")
                .unmatched(org.openehealth.ipf.commons.map.Unmatched.FAIL)
                .override(true)
                .build();

        var target = directory.resolve("strict.conceptmap.r4.json");
        Files.writeString(target, new ConceptMapWriter().toConceptMap(List.of(mapping)));

        assertThat(builder().load(target.toUri()).build().mapping("strict").orElseThrow(), is(mapping));
    }

    /**
     * The model says how the key relates to the value, FHIR R4 how the target relates to the
     * source: a key narrower than its value is published as "wider", and read back as narrower.
     */
    @Test
    public void equivalenceIsPublishedFromTheTargetsSide(@TempDir Path directory) throws IOException {
        var mapping = SimpleMapping.builder("relations")
                .entry("adoption", "other", Equivalence.NARROWER)
                .entry("any", "adoption", Equivalence.WIDER)
                .build();

        var json = new ConceptMapWriter().toConceptMap(List.of(mapping));
        assertThat(Pattern.compile("\"code\":\\s*\"other\",\\s*\"equivalence\":\\s*\"wider\"")
                .matcher(json).find(), is(true));
        assertThat(Pattern.compile("\"code\":\\s*\"adoption\",\\s*\"equivalence\":\\s*\"narrower\"")
                .matcher(json).find(), is(true));

        var target = directory.resolve("relations.conceptmap.r4.json");
        Files.writeString(target, json);
        assertThat(builder().load(target.toUri()).build().mapping("relations").orElseThrow(), is(mapping));
    }

    /**
     * The writer names the resource after its first mapping, cleaned up to a FHIR id. Read back,
     * the resource used to become a composite under that cleaned-up name as well; since every
     * group carries its mapping's name, it now reads back as exactly the mappings written.
     */
    @Test
    public void writtenMappingsReadBackWithoutAnInventedComposite(@TempDir Path directory) throws IOException {
        var written = List.of(
                SimpleMapping.builder("gender_v2").entry("M", "male").build(),
                SimpleMapping.builder("kdl|2025#other").entry("A", "B").build());

        var target = directory.resolve("named.conceptmap.r4.json");
        Files.writeString(target, new ConceptMapWriter().toConceptMap(written));
        var reloaded = builder().load(target.toUri()).build();

        assertThat(List.copyOf(reloaded.mappingNames()), is(List.of("gender_v2", "kdl|2025#other")));
    }
}
