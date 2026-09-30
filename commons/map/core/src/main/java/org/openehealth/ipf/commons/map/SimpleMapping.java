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

import java.util.ArrayList;
import java.util.List;

/**
 * A translation table: entries of its own, the identifiers of the two code systems involved, and
 * the fallback behavior for either direction. This is what every {@link MappingLoader} reads a
 * table in its format into, and what every {@link MappingWriter} writes.
 *
 * @param name             mapping name, unique within a {@link Mappings} instance
 * @param keySystem        formal identifier (usually an OID or a URI) of the key code system,
 *                         may be {@code null}
 * @param valueSystem      formal identifier of the value code system, may be {@code null}
 * @param entries          the key/value pairs, in declaration order
 * @param unmatched        what to answer when a key has no entry, never {@code null}
 * @param reverseUnmatched what to answer when a value has no entry, never {@code null}
 * @param reversible       whether a reverse index is built at all. A one-directional mapping
 *                         may legally map several keys onto the same value
 * @param override         whether this declaration is meant to replace a mapping of the same name
 *                         loaded earlier. Without it, a duplicate name is a load-time error
 * @since 6.0
 */
public record SimpleMapping(
        String name,
        String keySystem,
        String valueSystem,
        List<Entry> entries,
        Unmatched unmatched,
        Unmatched reverseUnmatched,
        boolean reversible,
        boolean override) implements Mapping {

    public SimpleMapping {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A mapping requires a name");
        }
        entries = entries == null ? List.of() : List.copyOf(entries);
        unmatched = unmatched == null ? Unmatched.ABSENT : unmatched;
        reverseUnmatched = reverseUnmatched == null ? Unmatched.ABSENT : reverseUnmatched;
    }

    /**
     * @return no parts: a simple mapping answers from its own entries
     */
    @Override
    public List<String> parts() {
        return List.of();
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Mutable helper for {@link MappingLoader} implementations, which assemble a mapping
     * incrementally while walking their source document.
     */
    public static final class Builder {

        private final String name;
        private final List<Entry> entries = new ArrayList<>();
        private String keySystem;
        private String valueSystem;
        private Unmatched unmatched = Unmatched.ABSENT;
        private Unmatched reverseUnmatched = Unmatched.ABSENT;
        private boolean reversible = true;
        private boolean override;

        private Builder(String name) {
            this.name = name;
        }

        public Builder keySystem(String keySystem) {
            this.keySystem = keySystem;
            return this;
        }

        public Builder valueSystem(String valueSystem) {
            this.valueSystem = valueSystem;
            return this;
        }

        public Builder entry(String key, String value) {
            return entry(new Entry(key, value));
        }

        public Builder entry(String key, String value, Equivalence equivalence) {
            return entry(new Entry(key, value, equivalence));
        }

        public Builder entry(Entry entry) {
            entries.add(entry);
            return this;
        }

        public Builder unmatched(Unmatched unmatched) {
            this.unmatched = unmatched;
            return this;
        }

        public Builder reverseUnmatched(Unmatched reverseUnmatched) {
            this.reverseUnmatched = reverseUnmatched;
            return this;
        }

        public Builder reversible(boolean reversible) {
            this.reversible = reversible;
            return this;
        }

        public Builder override(boolean override) {
            this.override = override;
            return this;
        }

        public SimpleMapping build() {
            return new SimpleMapping(name, keySystem, valueSystem, entries, unmatched, reverseUnmatched,
                    reversible, override);
        }
    }
}
