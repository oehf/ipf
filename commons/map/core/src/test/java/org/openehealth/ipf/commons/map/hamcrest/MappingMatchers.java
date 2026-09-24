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
package org.openehealth.ipf.commons.map.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openehealth.ipf.commons.map.Entry;
import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.SimpleMapping;
import org.openehealth.ipf.commons.map.Unmatched;

import java.util.Optional;

/**
 * Matchers for the mapping model, so that a test says what a mapping does rather than which method
 * it called to find out.
 * <pre>
 * assertThat(mappings, translates("gender", "M").to("male"));
 * assertThat(mappings, translatesBack("gender", "male").to("M"));
 * assertThat(mappings, doesNotTranslate("gender", "X"));
 * assertThat(mappings, mapsBetween("gender", "2.16.840.1.113883.12.1", "2.16.840.1.113883.5.5"));
 * assertThat(mapping, hasUnmatched(Unmatched.fixed("UNK")));
 * assertThat(entry, hasEquivalence(Equivalence.WIDER));
 * </pre>
 * The mismatch is the point: a translation that fails says whether the mapping is not registered at
 * all, whether it has no entry for the key, or what it answered instead - which the same assertion
 * written as {@code is(Optional.of("male"))} cannot tell apart.
 * <p>
 * Ships in this module's test jar, for every module that reads mappings in a format of its own.
 * Depend on it with
 * {@code <artifactId>ipf-commons-map-core</artifactId><type>test-jar</type><scope>test</scope>}.
 *
 * @since 6.0
 */
public final class MappingMatchers {

    private MappingMatchers() {
    }

    // ------------------------------------------------------------------ translating

    /**
     * @param mapping name of the mapping that should do the translating
     * @param key     the code to translate
     * @return half a matcher - name what it should translate to with {@link Translation#to(String)}
     */
    public static Translation translates(String mapping, String key) {
        return new Translation(mapping, key, false);
    }

    /**
     * The reverse direction: the value is given, the key is what is expected.
     *
     * @see #translates(String, String)
     */
    public static Translation translatesBack(String mapping, String value) {
        return new Translation(mapping, value, true);
    }

    /**
     * That a code has no translation is a statement in its own right - it is what an absent
     * fallback means - so it reads as one.
     */
    public static Matcher<Mappings> doesNotTranslate(String mapping, String key) {
        return new Translation(mapping, key, false).toNothing();
    }

    /**
     * @see #doesNotTranslate(String, String)
     */
    public static Matcher<Mappings> doesNotTranslateBack(String mapping, String value) {
        return new Translation(mapping, value, true).toNothing();
    }

    /**
     * A translation asserted from one side, waiting for the other.
     */
    public static final class Translation {

        private final String mapping;
        private final String input;
        private final boolean reverse;

        private Translation(String mapping, String input, boolean reverse) {
            this.mapping = mapping;
            this.input = input;
            this.reverse = reverse;
        }

        /**
         * @param expected what the mapping should answer, fallback included
         */
        public Matcher<Mappings> to(String expected) {
            return matcher(Optional.of(expected));
        }

        private Matcher<Mappings> toNothing() {
            return matcher(Optional.empty());
        }

        private Matcher<Mappings> matcher(Optional<String> expected) {
            var direction = reverse ? "back " : "";
            return new TypeSafeDiagnosingMatcher<>() {

                @Override
                protected boolean matchesSafely(Mappings mappings, Description mismatch) {
                    if (mappings.mapping(mapping).isEmpty()) {
                        mismatch.appendText("no mapping ").appendValue(mapping)
                                .appendText(" is registered, only ").appendValue(mappings.mappingNames());
                        return false;
                    }
                    var actual = reverse ? mappings.mapReverse(mapping, input) : mappings.map(mapping, input);
                    if (!actual.equals(expected)) {
                        mismatch.appendText(mapping.isEmpty() ? "it" : mapping)
                                .appendText(" translated ").appendText(direction).appendValue(input)
                                .appendText(actual.map(value -> " to " + quoted(value))
                                        .orElse(" to nothing"));
                        return false;
                    }
                    return true;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("mappings where ").appendValue(mapping)
                            .appendText(" translates ").appendText(direction).appendValue(input)
                            .appendText(expected.map(value -> " to " + quoted(value))
                                    .orElse(" to nothing"));
                }
            };
        }

        private static String quoted(String value) {
            return "\"" + value + "\"";
        }
    }

    // ------------------------------------------------------------------ what a mapping declares

    /**
     * The code systems a mapping translates between, which is how a caller holding a code finds it
     * when the mapping's own name means nothing to them.
     *
     * @param keySystem   identifier of the key code system, {@code null} for "declares none"
     * @param valueSystem identifier of the value code system, {@code null} for "declares none"
     */
    public static Matcher<Mappings> mapsBetween(String mapping, String keySystem, String valueSystem) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Mappings mappings, Description mismatch) {
                if (mappings.mapping(mapping).isEmpty()) {
                    mismatch.appendText("no mapping ").appendValue(mapping).appendText(" is registered");
                    return false;
                }
                var actualKey = mappings.keySystem(mapping).orElse(null);
                var actualValue = mappings.valueSystem(mapping).orElse(null);
                if (!java.util.Objects.equals(keySystem, actualKey)
                        || !java.util.Objects.equals(valueSystem, actualValue)) {
                    mismatch.appendText(mapping).appendText(" translates from ").appendValue(actualKey)
                            .appendText(" to ").appendValue(actualValue);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("mappings where ").appendValue(mapping)
                        .appendText(" translates from ").appendValue(keySystem)
                        .appendText(" to ").appendValue(valueSystem);
            }
        };
    }

    public static Matcher<Mapping> hasUnmatched(Unmatched unmatched) {
        return unmatched("unmatched", unmatched, SimpleMapping::unmatched);
    }

    public static Matcher<Mapping> hasReverseUnmatched(Unmatched unmatched) {
        return unmatched("reverse unmatched", unmatched, SimpleMapping::reverseUnmatched);
    }

    private static Matcher<Mapping> unmatched(String what, Unmatched expected,
                                              java.util.function.Function<SimpleMapping, Unmatched> of) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Mapping mapping, Description mismatch) {
                if (!(mapping instanceof SimpleMapping simple)) {
                    mismatch.appendText(mapping.name()).appendText(" is composite");
                    return false;
                }
                var actual = of.apply(simple);
                if (!expected.equals(actual)) {
                    mismatch.appendText(mapping.name()).appendText(" has ").appendText(what)
                            .appendText(" ").appendValue(actual);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a mapping whose ").appendText(what)
                        .appendText(" is ").appendValue(expected);
            }
        };
    }

    /**
     * Whether the mapping answers in the reverse direction at all.
     */
    public static Matcher<Mapping> isReversible(boolean reversible) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Mapping mapping, Description mismatch) {
                if (!(mapping instanceof SimpleMapping simple)) {
                    mismatch.appendText(mapping.name()).appendText(" is composite");
                    return false;
                }
                if (simple.reversible() != reversible) {
                    mismatch.appendText(mapping.name()).appendText(reversible
                            ? " is not reversible" : " is reversible");
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a mapping that is ")
                        .appendText(reversible ? "reversible" : "not reversible");
            }
        };
    }

    // ------------------------------------------------------------------ entries

    /**
     * The equivalence is what decides whether an entry translates and whether it has an inverse,
     * so an entry is rarely interesting without it.
     */
    public static Matcher<Entry> hasEquivalence(Equivalence equivalence) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Entry entry, Description mismatch) {
                if (entry.equivalence() != equivalence) {
                    mismatch.appendText("entry ").appendValue(entry.key()).appendText(" -> ")
                            .appendValue(entry.value()).appendText(" is ").appendValue(entry.equivalence());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an entry declared ").appendValue(equivalence);
            }
        };
    }

    /**
     * @param key   left side of the entry
     * @param value right side of the entry
     */
    public static Matcher<Entry> isEntry(String key, String value) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Entry entry, Description mismatch) {
                if (!java.util.Objects.equals(key, entry.key())
                        || !java.util.Objects.equals(value, entry.value())) {
                    mismatch.appendText("was ").appendValue(entry.key())
                            .appendText(" -> ").appendValue(entry.value());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("the entry ").appendValue(key)
                        .appendText(" -> ").appendValue(value);
            }
        };
    }

    /**
     * The displays a format carried over, which are informative but must survive a round trip.
     */
    public static Matcher<Entry> hasDisplays(String keyDisplay, String valueDisplay) {
        return new TypeSafeDiagnosingMatcher<>() {

            @Override
            protected boolean matchesSafely(Entry entry, Description mismatch) {
                if (!java.util.Objects.equals(keyDisplay, entry.keyDisplay())
                        || !java.util.Objects.equals(valueDisplay, entry.valueDisplay())) {
                    mismatch.appendText("entry ").appendValue(entry.key()).appendText(" displays ")
                            .appendValue(entry.keyDisplay()).appendText(" -> ")
                            .appendValue(entry.valueDisplay());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an entry displaying ").appendValue(keyDisplay)
                        .appendText(" -> ").appendValue(valueDisplay);
            }
        };
    }
}
