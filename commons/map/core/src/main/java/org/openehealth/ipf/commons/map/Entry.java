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

/**
 * A single key/value pair of a {@link Mapping}, qualified by the {@link Equivalence} that
 * holds between the two, and optionally by the human-readable name of either side.
 * <p>
 * The two displays are informative: nothing looks a code up by its display, and no format
 * requires them. They exist so that a mapping read from a source that carries them - a FHIR
 * ConceptMap does, for both the source and the target code - keeps them, and so that a caller
 * building a {@code Coding} from a translation has one to put in it.
 * <p>
 * Both key and value are {@link String}s.
 *
 * @param key          left side of the mapping, may be {@code null} for a mapping that
 *                     answers an absent input
 * @param value        right side of the mapping
 * @param equivalence  correspondence between key and value, never {@code null} &mdash;
 *                     defaults to {@link Equivalence#EQUAL}
 * @param keyDisplay   human-readable name of the key in its code system, or {@code null}
 * @param valueDisplay human-readable name of the value in its code system, or {@code null}
 * @since 6.0
 */
public record Entry(String key, String value, Equivalence equivalence,
                    String keyDisplay, String valueDisplay) {

    public Entry {
        if (equivalence == null) {
            equivalence = Equivalence.EQUAL;
        }
    }

    public Entry(String key, String value) {
        this(key, value, Equivalence.EQUAL);
    }

    public Entry(String key, String value, Equivalence equivalence) {
        this(key, value, equivalence, null, null);
    }

    /**
     * @return whether this entry may contribute to the reverse index of its mapping
     */
    public boolean isInvertible() {
        return equivalence.isInvertible();
    }

    /**
     * @return whether looking this entry's key up answers with its value
     * @see Equivalence#isTranslation()
     */
    public boolean isTranslation() {
        return equivalence.isTranslation();
    }

    /**
     * @return a copy of this entry carrying the given displays
     */
    public Entry withDisplays(String keyDisplay, String valueDisplay) {
        return new Entry(key, value, equivalence, keyDisplay, valueDisplay);
    }
}
