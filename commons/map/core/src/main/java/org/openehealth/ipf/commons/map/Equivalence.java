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
 * Degree of correspondence between the key and the value of a mapping {@link Entry}.
 * <p>
 * The vocabulary is taken from FHIR ConceptMap (R4 {@code element.target.equivalence},
 * renamed to {@code relationship} in R5). Its purpose here is to state which entries may
 * be used to build the reverse index of a {@link Mapping}: only entries that assert an
 * {@link #EQUAL} or {@link #EQUIVALENT} correspondence are invertible. A key that is
 * {@link #NARROWER} than its value does not become the canonical inverse of that value,
 * which is what makes reverse-direction collisions detectable instead of silent.
 *
 * @since 6.0
 */
public enum Equivalence {

    /** The key and the value are the same concept. */
    EQUAL,

    /** The key and the value are different concepts with the same meaning. */
    EQUIVALENT,

    /** The key is a broader concept than the value. */
    WIDER,

    /** The key is a narrower concept than the value. */
    NARROWER,

    /** The key and the value overlap, but neither subsumes the other. */
    INEXACT,

    /** The key and the value are explicitly not equivalent. */
    DISJOINT;

    /**
     * @return whether an entry with this equivalence may contribute to the reverse index
     */
    public boolean isInvertible() {
        return this == EQUAL || this == EQUIVALENT;
    }

    /**
     * Whether an entry with this equivalence is a translation at all, i.e. whether looking up its key
     * should answer with its value. Everything but {@link #DISJOINT} is: a key that is wider,
     * narrower or only inexactly related to its value still translates to it, imprecisely.
     * {@link #DISJOINT} asserts the opposite - that the two are explicitly <em>not</em>
     * equivalent - so answering with its value would state the reverse of what the mapping says.
     * <p>
     * FHIR draws the same line: the {@code result} of a {@code $translate} "can only be true if at
     * least one returned match has an equivalence which is not unmatched or disjoint".
     *
     * @return whether a lookup of this entry's key answers with its value
     */
    public boolean isTranslation() {
        return this != DISJOINT;
    }
}
