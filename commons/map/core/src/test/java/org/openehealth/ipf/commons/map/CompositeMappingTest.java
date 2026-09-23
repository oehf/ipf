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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasNoValue;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

/**
 * Composite mappings, which ask several mappings in turn - the shape of a FHIR ConceptMap whose
 * groups translate one source system into different target systems - and {@link Translation}s,
 * which say which system an answer belongs to.
 */
public class CompositeMappingTest {

    private static final String KDL = "urn:kdl";
    private static final String TYPE_CODE = "urn:ihe-typecode";
    private static final String NULL_FLAVOR = "urn:null-flavor";

    private static final String TYPE_CODES = "kdl#typecode";
    private static final String NULL_FLAVORS = "kdl#null-flavor";
    private static final String WHOLE = "kdl";

    private static Mapping typeCodes() {
        return SimpleMapping.builder(TYPE_CODES)
                .keySystem(KDL)
                .valueSystem(TYPE_CODE)
                .entry(new Entry("AD010101", "BERI", Equivalence.EQUAL, "Ärztliche Stellungnahme", "Arztberichte"))
                .unmatched(Unmatched.fixed("OTH"))
                .build();
    }

    private static Mapping nullFlavors() {
        return SimpleMapping.builder(NULL_FLAVORS)
                .keySystem(KDL)
                .valueSystem(NULL_FLAVOR)
                .entry(new Entry("UB140101", "UNK", Equivalence.EQUAL, "Sonstiges", "unknown"))
                .unmatched(Unmatched.fixed("NI"))
                .build();
    }

    private static CompositeMapping whole() {
        return CompositeMapping.builder(WHOLE).keySystem(KDL).part(TYPE_CODES).part(NULL_FLAVORS).build();
    }

    private static Mappings mappings() {
        return Mappings.builder().mapping(typeCodes()).mapping(nullFlavors()).mapping(whole()).build();
    }

    /**
     * A code declared by any part is found, even where an earlier part has a fallback that would
     * have answered - declarations are asked first, fallbacks only if no part declares the code.
     */
    @Test
    public void declarationsOfAllPartsComeBeforeAnyFallback() {
        var mappings = mappings();

        assertThat(mappings, translates(WHOLE, "AD010101").to("BERI"));
        assertThat(mappings, translates(WHOLE, "UB140101").to("UNK"));
        assertThat(mappings, translates(WHOLE, "XX999999").to("OTH"));
        assertThat(mappings, translatesBack(WHOLE, "UNK").to("UB140101"));
        assertThat(mappings.lookup(WHOLE, "XX999999"), hasNoValue());
    }

    /**
     * The answer comes with the system of the part that gave it, so {@code UNK} is a null flavor
     * rather than an IHE type code - which a merged table of strings could not tell.
     */
    @Test
    public void aTranslationCarriesTheSystemOfThePartThatAnswered() {
        var mappings = mappings();

        assertThat(mappings.translate(WHOLE, "AD010101"),
                hasValue(new Translation("BERI", TYPE_CODE, "Arztberichte")));
        assertThat(mappings.translate(WHOLE, "UB140101"),
                hasValue(new Translation("UNK", NULL_FLAVOR, "unknown")));
        assertThat(mappings.translate(WHOLE, "XX999999"), hasValue(new Translation("OTH", TYPE_CODE, null)));
        assertThat(mappings.translateReverse(WHOLE, "UNK"),
                hasValue(new Translation("UB140101", KDL, "Sonstiges")));
    }

    @Test
    public void aPartIsStillAskedOnItsOwn() {
        var mappings = mappings();

        assertThat(mappings, translates(NULL_FLAVORS, "AD010101").to("NI"));
        assertThat(mappings.translate(NULL_FLAVORS, "UB140101"),
                hasValue(new Translation("UNK", NULL_FLAVOR, "unknown")));
    }

    /**
     * Asked by its source system alone, the composite matches together with its parts, and stands
     * for them; asked for one target system, only the part translating into it matches.
     */
    @Test
    public void aCompositeStandsForItsPartsWhenFoundBySystem() {
        var mappings = mappings();

        assertThat(mappings.mappingsFor(KDL, null).stream().map(Mapping::name).toList(),
                contains(TYPE_CODES, NULL_FLAVORS, WHOLE));
        assertThat(mappings.mappingFor(KDL, null), hasValue(whole()));
        assertThat(mappings.mappingFor(KDL, NULL_FLAVOR), hasValue(nullFlavors()));
    }

    @Test
    public void theKeysAndValuesAreThoseOfAllParts() {
        var mappings = mappings();

        assertThat(mappings.keys(WHOLE), contains("AD010101", "UB140101"));
        assertThat(mappings.values(WHOLE), contains("BERI", "UNK"));
        assertThat(mappings.mapping(WHOLE).orElseThrow(), is(whole()));
        assertThat(mappings.keySystem(WHOLE), hasValue(KDL));
        assertThat(mappings.valueSystem(WHOLE), hasNoValue());
    }

    /**
     * What a mapping declares can be asked by name whatever its kind: for a composite, the
     * entries of its parts in part order.
     */
    @Test
    public void theEntriesOfACompositeAreThoseOfItsParts() {
        var mappings = mappings();

        assertThat(mappings.entries(WHOLE), is(Stream.concat(typeCodes().entries().stream(),
                nullFlavors().entries().stream()).toList()));
        assertThat(mappings.entries(TYPE_CODES), is(typeCodes().entries()));
        assertThrows(IllegalArgumentException.class, () -> mappings.entries("unknown"));
    }

    /**
     * Both kinds answer the same accessors. A composite holds only the names of its parts, so what
     * only a table has it cannot report, and says where to look instead.
     */
    @Test
    public void aCompositeCannotReportWhatOnlyATableHas() {
        var e = assertThrows(UnsupportedOperationException.class, () -> whole().entries());
        assertThat(e.getMessage(), containsString("Mappings.entries(\"" + WHOLE + "\")"));
        assertThrows(UnsupportedOperationException.class, () -> whole().unmatched());
        assertThrows(UnsupportedOperationException.class, () -> whole().reverseUnmatched());
        assertThrows(UnsupportedOperationException.class, () -> whole().reversible());
        assertThat(typeCodes().parts(), is(empty()));
    }

    /**
     * An identity fallback hands the key back unchanged, so it stays in the key system; a
     * delegating fallback answers with the system of the mapping delegated to.
     */
    @Test
    public void fallbacksAnswerInTheSystemTheirCodeBelongsTo() {
        var mappings = Mappings.builder()
                .mapping(typeCodes())
                .mapping(SimpleMapping.builder("provided").keySystem(KDL).valueSystem(TYPE_CODE)
                        .unmatched(Unmatched.IDENTITY).build())
                .mapping(SimpleMapping.builder("delegating").keySystem(KDL).valueSystem("urn:other")
                        .unmatched(Unmatched.delegate(TYPE_CODES)).build())
                .build();

        assertThat(mappings.translate("provided", "AD010101"), hasValue(new Translation("AD010101", KDL, null)));
        assertThat(mappings.translate("delegating", "AD010101"),
                hasValue(new Translation("BERI", TYPE_CODE, "Arztberichte")));
    }

    /**
     * An implementation that does not override translate() still answers, with the mapping's
     * own systems and no display.
     */
    @Test
    public void theDefaultTranslationUsesTheMappingsSystems() {
        var delegate = mappings();
        Mappings minimal = new DelegatingMappings(delegate);

        assertThat(minimal.translate(TYPE_CODES, "AD010101"), hasValue(new Translation("BERI", TYPE_CODE, null)));
        assertThat(minimal.translateReverse(TYPE_CODES, "BERI"), hasValue(new Translation("AD010101", KDL, null)));
    }

    @Test
    public void partsMustBeRegisteredFirst() {
        var builder = Mappings.builder().mapping(typeCodes());
        var e = assertThrows(MappingException.class, () -> builder.mapping(whole()));
        assertThat(e.getMessage(), containsString("'" + NULL_FLAVORS + "', which is not registered"));
    }

    @Test
    public void compositesDoNotNest() {
        var builder = Mappings.builder().mapping(typeCodes()).mapping(nullFlavors()).mapping(whole());
        var e = assertThrows(MappingException.class,
                () -> builder.mapping(CompositeMapping.builder("outer").part(WHOLE).build()));
        assertThat(e.getMessage(), containsString("itself composite"));
    }

    /**
     * Replacing a part by a composite would let a composite reach itself; it is rejected as the
     * replacement is registered.
     */
    @Test
    public void aPartCannotBecomeAComposite() {
        var builder = Mappings.builder().allowOverride(true)
                .mapping(typeCodes()).mapping(nullFlavors()).mapping(whole());
        var e = assertThrows(MappingException.class,
                () -> builder.mapping(CompositeMapping.builder(TYPE_CODES).part(NULL_FLAVORS).build()));
        assertThat(e.getMessage(), containsString("is a part of the composite '" + WHOLE + "'"));
    }

    /**
     * A part delegating to the composite it belongs to would hand an unknown code round in a
     * circle. The cycle runs through a composite rather than a chain of delegations, and is still
     * rejected when it is registered rather than on every lookup.
     */
    @Test
    public void aCycleThroughACompositeIsRejectedOnRegistration() {
        var builder = Mappings.builder().allowOverride(true)
                .mapping(typeCodes()).mapping(nullFlavors()).mapping(whole());
        var e = assertThrows(MappingException.class, () -> builder.mapping(SimpleMapping.builder(TYPE_CODES)
                .entry("AD010101", "BERI").unmatched(Unmatched.delegate(WHOLE)).build()));
        assertThat(e.getMessage(), containsString(
                "delegates in a cycle: " + TYPE_CODES + " -> " + WHOLE + " -> " + TYPE_CODES));
    }

    @Test
    public void aCompositeHasAtLeastOnePartAndNotItself() {
        assertThrows(IllegalArgumentException.class, () -> CompositeMapping.builder(WHOLE).build());
        assertThrows(IllegalArgumentException.class, () -> CompositeMapping.builder(WHOLE).part(WHOLE).build());
    }

    /**
     * Implements only what {@link Mappings} requires, so that its default methods are what is
     * tested.
     */
    private record DelegatingMappings(Mappings delegate) implements Mappings {

        @Override
        public Optional<Mapping> mapping(String mapping) {
            return delegate.mapping(mapping);
        }

        @Override
        public Set<String> mappingNames() {
            return delegate.mappingNames();
        }

        @Override
        public List<Mapping> mappingsFor(String keySystem, String valueSystem) {
            return delegate.mappingsFor(keySystem, valueSystem);
        }

        @Override
        public Optional<String> lookup(String mapping, String key) {
            return delegate.lookup(mapping, key);
        }

        @Override
        public Optional<String> lookupReverse(String mapping, String value) {
            return delegate.lookupReverse(mapping, value);
        }

        @Override
        public Optional<String> unmatched(String mapping, String key) {
            return delegate.unmatched(mapping, key);
        }

        @Override
        public Optional<String> reverseUnmatched(String mapping, String value) {
            return delegate.reverseUnmatched(mapping, value);
        }

        @Override
        public Optional<String> keySystem(String mapping) {
            return delegate.keySystem(mapping);
        }

        @Override
        public Optional<String> valueSystem(String mapping) {
            return delegate.valueSystem(mapping);
        }

        @Override
        public Set<String> keys(String mapping) {
            return delegate.keys(mapping);
        }

        @Override
        public Collection<String> values(String mapping) {
            return delegate.values(mapping);
        }

        @Override
        public MappingFunctions functions() {
            return delegate.functions();
        }
    }
}
