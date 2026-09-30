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

import java.util.List;

/**
 * A named translation from one code system into another, as {@link Mappings} serves it and a
 * {@link MappingLoader} produces it. A lookup by name works the same for either kind:
 * <ul>
 *     <li>a {@link SimpleMapping} is a translation table - entries of its own and a fallback for
 *     either direction. Every mapping format reads into one and writes one;</li>
 *     <li>a {@link CompositeMapping} holds no entries but the names of simple mappings, its
 *     parts, and answers through them. Loaders create one where a source spreads a single
 *     translation over several tables, such as the groups of a FHIR ConceptMap.</li>
 * </ul>
 * Both kinds answer every accessor declared here, so a caller inspecting a mapping need not know
 * its kind in advance. What only a table has - {@link #entries()}, the fallbacks and
 * {@link #reversible()} - a composite cannot report, since it holds nothing but the names of its
 * parts; it throws {@link UnsupportedOperationException} for these. To inspect what a mapping of
 * either kind declares, ask {@link Mappings#entries(String)} by name, which resolves the parts.
 *
 * @since 6.0
 */
public sealed interface Mapping permits SimpleMapping, CompositeMapping {

    /**
     * @return mapping name, unique within a {@link Mappings} instance
     */
    String name();

    /**
     * @return formal identifier (usually an OID or a URI) of the key code system, may be
     * {@code null}
     */
    String keySystem();

    /**
     * @return formal identifier of the value code system, may be {@code null}
     */
    String valueSystem();

    /**
     * @return whether this declaration is meant to replace a mapping of the same name loaded
     * earlier. Without it, a duplicate name is a load-time error
     */
    boolean override();

    /**
     * @return the key/value pairs, in declaration order
     * @throws UnsupportedOperationException for a {@link CompositeMapping}, whose entries are
     *                                       those of its parts; see {@link Mappings#entries(String)}
     */
    List<Entry> entries();

    /**
     * @return what to answer when a key has no entry
     * @throws UnsupportedOperationException for a {@link CompositeMapping}, which falls back
     *                                       through its parts
     */
    Unmatched unmatched();

    /**
     * @return what to answer when a value has no entry
     * @throws UnsupportedOperationException for a {@link CompositeMapping}, which falls back
     *                                       through its parts
     */
    Unmatched reverseUnmatched();

    /**
     * @return whether a reverse index is built at all
     * @throws UnsupportedOperationException for a {@link CompositeMapping}, whose parts decide
     *                                       this each for themselves
     */
    boolean reversible();

    /**
     * @return the names of the mappings a {@link CompositeMapping} asks, in order; empty for a
     * {@link SimpleMapping}
     */
    List<String> parts();
}
