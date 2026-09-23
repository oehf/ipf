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
 * What a {@link Mapping} answers with when the requested key has no entry.
 * <p>
 * Every mode but {@link Delegate} answers on its own; {@link Delegate} hands the question to
 * another mapping, whose own fallback then decides.
 * <p>
 * This replaces the {@code (ELSE)} clause of the Groovy mapping DSL, whose value could be
 * any Groovy expression. The vocabulary here is closed and therefore exhaustively checkable:
 * a mapping file can only declare one of the modes below, and {@link Computed} can only name
 * a function that a {@link MappingFunctionRegistry} knows about.
 *
 * @since 6.0
 */
public sealed interface Unmatched {

    /** No fallback &mdash; an unmatched key has no value. */
    record Absent() implements Unmatched {
    }

    /** The key itself is the value. Corresponds to {@code (ELSE) : { it }}. */
    record Identity() implements Unmatched {
    }

    /** A constant value. Corresponds to {@code (ELSE) : 'UNK'}. */
    record Fixed(String value) implements Unmatched {
        public Fixed {
            if (value == null) {
                throw new IllegalArgumentException("A fixed fallback requires a value; for none, use ABSENT");
            }
        }
    }

    /**
     * The value is computed by a named function resolved against a
     * {@link MappingFunctionRegistry}.
     */
    record Computed(String ref) implements Unmatched {
        public Computed {
            if (ref == null || ref.isEmpty()) {
                throw new IllegalArgumentException("Computed fallback requires a function reference");
            }
        }
    }

    /** An unmatched key is an error. */
    record Fail() implements Unmatched {
    }

    /**
     * Another mapping answers instead. The named mapping is asked <em>in full</em> - its entries
     * first and then its own unmatched behaviour - so it is that mapping which finally decides the
     * outcome, and a chain of delegations ends at the first mapping whose fallback is not itself a
     * delegation.
     * <p>
     * This is how a mapping specialises another without restating it: declare the codes that
     * differ and delegate the rest. Corresponds to {@code unmapped.mode = other-map} in FHIR
     * ConceptMap.
     * <p>
     * The named mapping must already be registered, which is what makes a cycle impossible to
     * declare. A name without a version also finds the only mapping registered as
     * {@code name|version}, the way a versionless FHIR canonical refers to the one version at
     * hand; with several such versions it is ambiguous and rejected.
     */
    record Delegate(String mapping) implements Unmatched {
        public Delegate {
            if (mapping == null || mapping.isEmpty()) {
                throw new IllegalArgumentException("Delegating fallback requires a mapping name");
            }
        }
    }

    Unmatched ABSENT = new Absent();
    Unmatched IDENTITY = new Identity();
    Unmatched FAIL = new Fail();

    static Unmatched fixed(String value) {
        return new Fixed(value);
    }

    static Unmatched computed(String ref) {
        return new Computed(ref);
    }

    static Unmatched delegate(String mapping) {
        return new Delegate(mapping);
    }
}
