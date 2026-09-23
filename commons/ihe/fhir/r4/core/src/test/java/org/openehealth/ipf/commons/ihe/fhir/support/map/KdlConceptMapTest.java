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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.Translation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslate;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.doesNotTranslateBack;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.mapsBetween;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;

/**
 * The loader against a ConceptMap nobody here wrote: an excerpt of the DVMD's KDL to
 * IHE-XDS DocumentEntry.typeCode map, published at
 * <a href="https://simplifier.net/kdl/kdl-ihe-typecode">simplifier.net/kdl/kdl-ihe-typecode</a>
 * (canonical {@code http://dvmd.de/fhir/ConceptMap/kdl-ihe-typecode}, version 2025,
 * &#169; 2025 DVMD e.V.). Structure and content are as published; only the number of elements is
 * reduced.
 * <p>
 * It is a good test because it is shaped the way real terminology is and IPF's own mappings are
 * not: two groups mapping one source system onto two different targets, hundreds of source codes
 * collapsing onto a few dozen target codes, and every single target marked {@code wider}. That
 * last part is what makes it loadable at all - under the mapping model, forty codes claiming to
 * be <em>equal</em> to {@code FUNK} would be a load-time error, and forty codes that are
 * <em>narrower</em> than it are simply forty codes with no inverse.
 */
public class KdlConceptMapTest {

    private static final String KDL = "http://dvmd.de/fhir/CodeSystem/kdl";
    private static final String NULL_FLAVOR = "http://terminology.hl7.org/CodeSystem/v3-NullFlavor";
    private static final String TYPE_CODE = "http://ihe-d.de/CodeSystems/IHEXDStypeCode";

    /*
     * Two groups without a mapping-name extension, so they are named after the canonical and
     * version of the resource, qualified by their target; the canonical and version alone name
     * the ConceptMap as a whole.
     */
    private static final String NULL_FLAVORS = "kdl-ihe-typecode|2025#v3-NullFlavor";
    private static final String TYPE_CODES = "kdl-ihe-typecode|2025#IHEXDStypeCode";
    private static final String WHOLE = "kdl-ihe-typecode|2025";

    private Mappings mappings;

    @BeforeEach
    public void setUp() {
        mappings = Mappings.builder()
                .load("classpath:/kdl-ihe-typecode.conceptmap.r4.json")
                .build();
    }

    /**
     * Translating a document class code the way an application would.
     */
    @Test
    public void translatesKdlCodesToIheTypeCodes() {
        assertThat(mappings, translates(TYPE_CODES, "AD010101").to("BERI"));  // Ärztliche Stellungnahme
        assertThat(mappings, translates(TYPE_CODES, "AD020202").to("BEFU"));  // Befundbogen
        assertThat(mappings, translates(TYPE_CODES, "ED110108").to("MEDI"));  // eRezept
        assertThat(mappings, translates(NULL_FLAVORS, "UB140101").to("UNK"));

        assertThat(mappings, mapsBetween(TYPE_CODES, KDL,
                TYPE_CODE));
    }

    /**
     * The resource declares no {@code unmapped}, so an unknown code has no answer rather than a
     * wrong one. A caller that wants a default asks for one.
     */
    @Test
    public void unknownCodeHasNoAnswer() {
        assertThat(mappings, doesNotTranslate(TYPE_CODES, "XX999999"));
        assertThat(mappings.map(TYPE_CODES, "XX999999", "DEFAULT"), is("DEFAULT"));
    }

    /**
     * Fifteen KDL codes, five IHE type codes: the source concepts are all narrower than their
     * targets, so no target has an inverse and the reverse direction answers nothing. Before the
     * equivalence was part of the model, the reverse index would have silently held whichever
     * code happened to be declared last.
     */
    @Test
    public void widerTargetsHaveNoInverse() {
        assertThat(mappings, doesNotTranslateBack(TYPE_CODES, "BERI"));
        assertThat(mappings, doesNotTranslateBack(NULL_FLAVORS, "UNK"));
    }

    @Test
    public void equivalencesAreReadFromTheResource() {
        var mapping = mappings.mapping(TYPE_CODES).orElseThrow();

        assertThat(mapping.entries(), hasSize(15));
        // R4 "wider": the IHE type code is the broader concept, so each KDL key is the narrower one
        assertThat(mapping.entries().stream().allMatch(entry -> entry.equivalence() == Equivalence.NARROWER),
                is(true));
        assertThat(mapping.entries().stream().anyMatch(Entry::isInvertible), is(false));
        assertThat(mappings.mappingNames(), contains(TYPE_CODES, NULL_FLAVORS, WHOLE));
    }

    /**
     * A consumer of a third-party ConceptMap knows neither its groups nor which one holds a code.
     * It asks the ConceptMap as a whole, the way FHIR's {@code $translate} does without a target
     * system, and learns from the answer which system the code belongs to - an IHE type code, or
     * the null flavor that KDL maps codes without an IHE equivalent to.
     */
    @Test
    public void theWholeConceptMapAnswersWithTheSystemOfTheGroupThatHadTheCode() {
        assertThat(mappings, translates(WHOLE, "AD010101").to("BERI"));
        assertThat(mappings, translates(WHOLE, "UB140101").to("UNK"));
        assertThat(mappings, doesNotTranslate(WHOLE, "XX999999"));

        assertThat(mappings.translate(WHOLE, "AD010101"),
                hasValue(new Translation("BERI", TYPE_CODE, "Arztberichte")));
        assertThat(mappings.translate(WHOLE, "UB140101"),
                hasValue(new Translation("UNK", NULL_FLAVOR, "Unbekannt")));
        assertThat(mappings.mapping(WHOLE).orElseThrow().parts(), contains(TYPE_CODES, NULL_FLAVORS));
        assertThat(mappings.entries(WHOLE),
                hasSize(mappings.entries(TYPE_CODES).size() + mappings.entries(NULL_FLAVORS).size()));
    }

    /**
     * A group can also be reached by the code systems it translates between, the shape of FHIR's
     * $translate with a target system. Asked only for the source system, both groups and the
     * ConceptMap as a whole answer - they all translate out of KDL.
     */
    @Test
    public void theMappingIsReachableByItsCodeSystems() {
        var mapping = mappings.mappingFor(KDL, TYPE_CODE).orElseThrow();
        assertThat(mapping.name(), is(TYPE_CODES));
        assertThat(mappings, translates(mapping.name(), "AD010101").to("BERI"));

        // both groups translate out of KDL, into different systems
        assertThat(mappings.mappingsFor(KDL, null).stream().map(Mapping::name).toList(),
                contains(TYPE_CODES, NULL_FLAVORS, WHOLE));
        assertThat(mappings.mappingsFor("http://example.org/nothing", null), is(empty()));
    }

    /**
     * The resource names every code on both sides. Those names are informative, but throwing them
     * away would make a round trip through the ConceptMap writer lossy.
     */
    @Test
    public void displaysAreKeptOnBothSides() {
        var entry = mappings.mapping(TYPE_CODES).orElseThrow().entries().get(0);

        assertThat(entry.key(), is("AD010101"));
        assertThat(entry.keyDisplay(), is("Ärztliche Stellungnahme"));
        assertThat(entry.value(), is("BERI"));
        assertThat(entry.valueDisplay(), is("Arztberichte"));
    }

    /**
     * The same resource alongside a mapping in IPF's own XML format, in one service - which is
     * the case the ConceptMap loader exists for: terminology governed in ConceptMap next to plain
     * tables that are better off as plain tables.
     */
    @Test
    public void worksAlongsideOtherFormats() {
        var mixed = Mappings.builder()
                .load("classpath:/kdl-ihe-typecode.conceptmap.r4.json")
                .load("classpath:/publishable.mapping.xml")
                .build();

        assertThat(mixed, translates(TYPE_CODES, "AD010101").to("BERI"));
        assertThat(mixed, translates("hl7v2fhir-patient-administrativeGender", "M").to("male"));
    }

    /**
     * KDL is published once a year under the same canonical and id, and documents coded with
     * different releases have to be translated side by side. The version is part of the mapping
     * name, so the releases do not collide - even though DVMD gave both the same {@code name}.
     */
    @Test
    public void severalVersionsLoadSideBySide(@TempDir Path directory) throws IOException {
        var release2024 = release(directory, "2024", "\"version\": \"2024\"");

        var both = Mappings.builder()
                .load("classpath:/kdl-ihe-typecode.conceptmap.r4.json")
                .load(release2024.toUri())
                .build();

        assertThat(both.mappingNames(), containsInAnyOrder(TYPE_CODES, NULL_FLAVORS, WHOLE,
                "kdl-ihe-typecode|2024#IHEXDStypeCode", "kdl-ihe-typecode|2024#v3-NullFlavor",
                "kdl-ihe-typecode|2024"));
        assertThat(both, translates(WHOLE, "AD010101").to("BERI"));
        assertThat(both, translates("kdl-ihe-typecode|2024", "AD010101").to("BERI"));
    }

    /**
     * Without a version the name is the canonical's last segment alone, with no trailing bar.
     */
    @Test
    public void aResourceWithoutVersionIsNamedByItsCanonicalAlone(@TempDir Path directory) throws IOException {
        var unversioned = release(directory, "unversioned", null);

        var loaded = Mappings.builder().load(unversioned.toUri()).build();

        assertThat(loaded.mappingNames(), containsInAnyOrder("kdl-ihe-typecode",
                "kdl-ihe-typecode#IHEXDStypeCode", "kdl-ihe-typecode#v3-NullFlavor"));
    }

    /**
     * @param version replaces the 2025 version of the published resource, or removes it if
     *                {@code null}
     */
    private static Path release(Path directory, String fileName, String version) throws IOException {
        try (var in = KdlConceptMapTest.class.getResourceAsStream("/kdl-ihe-typecode.conceptmap.r4.json")) {
            var content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            var original = "\"version\": \"2025\"";
            if (!content.contains(original)) {
                throw new IllegalStateException("fixture no longer declares " + original);
            }
            content = version == null
                    ? content.replace(original + ",", "")
                    : content.replace(original, version);
            var file = directory.resolve(fileName + ".conceptmap.r4.json");
            Files.writeString(file, content, StandardCharsets.UTF_8);
            return file;
        }
    }
}
