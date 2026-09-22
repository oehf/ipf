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
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mappings;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
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
    private static final String NULL_FLAVORS = "ConceptMapKdlIheTypecode#1";
    private static final String TYPE_CODE = "http://ihe-d.de/CodeSystems/IHEXDStypeCode";
    private static final String TYPE_CODES = "ConceptMapKdlIheTypecode#0";

    /** Two groups without a mapping-name extension, so they are named after the resource. */

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
        assertThat(mapping.entries().stream().allMatch(entry -> entry.equivalence() == Equivalence.WIDER),
                is(true));
        assertThat(mapping.entries().stream().anyMatch(Entry::isInvertible), is(false));
        assertThat(mappings.mappingNames(), contains(TYPE_CODES, NULL_FLAVORS));
    }

    /**
     * The groups carry no mapping-name extension, so they are named after the resource - which is
     * exactly the case where asking by name is useless. Asking by the code systems the mapping
     * translates between is how a caller holding a KDL code would actually reach it, and it is the
     * shape of FHIR's $translate.
     */
    @Test
    public void theMappingIsReachableByItsCodeSystems() {
        var mapping = mappings.mappingFor(KDL, TYPE_CODE).orElseThrow();
        assertThat(mapping.name(), is(TYPE_CODES));
        assertThat(mappings, translates(mapping.name(), "AD010101").to("BERI"));

        // both groups translate out of KDL, into different systems
        assertThat(mappings.mappingsFor(KDL, null).stream().map(m -> m.name()).toList(),
                contains(TYPE_CODES, NULL_FLAVORS));
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
}
