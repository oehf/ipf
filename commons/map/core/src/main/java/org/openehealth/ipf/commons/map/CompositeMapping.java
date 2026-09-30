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
 * A mapping that holds no entries of its own but the names of other mappings - its parts - and
 * answers through them. It exists for sources that spread one translation over several tables,
 * the common case being a FHIR ConceptMap whose groups translate one source code system into
 * several target systems. Each group is a {@link SimpleMapping}; the composite over them lets a
 * caller ask the ConceptMap as a whole without knowing which groups it has or which one holds a
 * code.
 * <p>
 * A lookup asks the parts in declaration order, in two rounds:
 * <ol>
 *     <li>the first part that <em>declares</em> an entry for the key answers;</li>
 *     <li>only if no part does, the first part whose <em>fallback</em> answers.</li>
 * </ol>
 * So a code declared in the second part is found even if the first part has a fallback that
 * would answer anything - which is how FHIR's {@code $translate} reads all groups of a
 * ConceptMap when no target system is given. Reverse lookups work the same way.
 * <p>
 * Since the parts may translate into different code systems, a plain lookup cannot say which
 * system an answer belongs to; {@link Mappings#translate(String, String)} returns it along with
 * the code, taken from the part that answered. The composite's own {@link #keySystem()} and
 * {@link #valueSystem()} are whatever its creator declares; the ConceptMap loader sets those that its
 * parts share, and {@code null} where they differ.
 * <p>
 * The parts must be simple mappings registered before the composite, so composites do not nest.
 * No file format declares one - loaders create them - and no {@link MappingWriter} writes one:
 * its parts are written instead.
 *
 * @param name        mapping name, unique within a {@link Mappings} instance
 * @param keySystem   formal identifier of the key code system, may be {@code null}
 * @param valueSystem formal identifier of the value code system, may be {@code null}
 * @param parts       the names of the mappings asked, in order; never empty
 * @param override    whether this declaration is meant to replace a mapping of the same name
 *                    loaded earlier
 * @since 6.0
 */
public record CompositeMapping(
        String name,
        String keySystem,
        String valueSystem,
        List<String> parts,
        boolean override) implements Mapping {

    public CompositeMapping {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A mapping requires a name");
        }
        parts = parts == null ? List.of() : List.copyOf(parts);
        if (parts.isEmpty()) {
            throw new IllegalArgumentException("Composite mapping '" + name + "' requires at least one part");
        }
        if (parts.contains(name)) {
            throw new IllegalArgumentException("Composite mapping '" + name + "' cannot be a part of itself");
        }
    }

    @Override
    public List<Entry> entries() {
        throw unsupported("entries of its own; ask Mappings.entries(\"" + name + "\") for those of its parts");
    }

    @Override
    public Unmatched unmatched() {
        throw unsupported("fallback of its own; it falls back through its parts");
    }

    @Override
    public Unmatched reverseUnmatched() {
        throw unsupported("reverse fallback of its own; it falls back through its parts");
    }

    @Override
    public boolean reversible() {
        throw unsupported("reversibility of its own; each of its parts decides it");
    }

    private UnsupportedOperationException unsupported(String what) {
        return new UnsupportedOperationException("Composite mapping '" + name + "' has no " + what);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Mutable helper for {@link MappingLoader} implementations, which assemble a composite
     * while walking their source document.
     */
    public static final class Builder {

        private final String name;
        private final List<String> parts = new ArrayList<>();
        private String keySystem;
        private String valueSystem;
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

        /**
         * Adds a mapping this one asks, after the parts added before.
         */
        public Builder part(String mapping) {
            parts.add(mapping);
            return this;
        }

        public Builder override(boolean override) {
            this.override = override;
            return this;
        }

        public CompositeMapping build() {
            return new CompositeMapping(name, keySystem, valueSystem, parts, override);
        }
    }
}
