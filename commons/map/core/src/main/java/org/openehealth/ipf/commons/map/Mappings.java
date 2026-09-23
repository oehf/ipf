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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Typed front door to a set of {@link Mapping}s, whatever format they were loaded from.
 * <p>
 * Compared to the older {@link MappingService} this interface is {@link String}-typed and
 * returns {@link Optional} rather than {@code null}, so call sites no longer cast. An unknown
 * <em>mapping name</em> is a programming error and raises {@link IllegalArgumentException};
 * an unknown <em>key</em> within a known mapping yields an empty {@code Optional}.
 * Mappings instances can be built programmatically like this:
 * <pre>
 * var mappings = Mappings.builder()
 *     .function("oidUri", s -&gt; !s.isEmpty() &amp;&amp; Character.isDigit(s.charAt(0)) ? "urn:oid:" + s : s)
 *     .load("classpath:/META-INF/map/atna2fhir.mapping.xml")
 *     .build();
 *
 * String uri = mappings.map("atnaCodingSystem", "DCM").orElseThrow();
 * </pre>
 *
 * @since 6.0
 */
public interface Mappings {

    /**
     * @param mapping mapping name
     * @return the mapping itself, or empty if no mapping of that name is registered
     */
    Optional<Mapping> mapping(String mapping);

    /**
     * @return the names of all registered mappings, in registration order
     */
    Set<String> mappingNames();

    /**
     * Finds mappings by the code systems they translate between, rather than by name.
     * <p>
     * A caller holding a code to translate knows which code system it is in and which one it wants,
     * not what the mapping happens to be called - which is the shape of FHIR's
     * {@code ConceptMap/$translate}, and the only way to reach a mapping read from a source that
     * names itself, such as a ConceptMap resource. Either argument may be {@code null} for "any",
     * so passing only a key system finds every mapping that translates out of it.
     * <p>
     * This is a scan over the registered mappings, not an index: resolve the mapping once and keep
     * it, rather than calling this per message.
     *
     * @param keySystem   identifier of the key code system, or {@code null} for any
     * @param valueSystem identifier of the value code system, or {@code null} for any
     * @return the matching mappings, in registration order
     */
    List<Mapping> mappingsFor(String keySystem, String valueSystem);

    /**
     * Where a {@link CompositeMapping} matches together with some of its parts, the composite
     * stands for them: it answers for all of them, so asking the whole of a ConceptMap by its
     * source system finds the ConceptMap rather than failing on its groups.
     *
     * @param keySystem   identifier of the key code system, or {@code null} for any
     * @param valueSystem identifier of the value code system, or {@code null} for any
     * @return the single mapping translating between the two systems, empty if there is none
     * @throws IllegalArgumentException if several mappings translate between them, since picking
     *                                  one of them would be arbitrary - use
     *                                  {@link #mappingsFor(String, String)} to see them all
     * @see #mappingsFor(String, String)
     */
    default Optional<Mapping> mappingFor(String keySystem, String valueSystem) {
        var matching = mappingsFor(keySystem, valueSystem);
        var covered = matching.stream()
                .flatMap(mapping -> mapping.parts().stream())
                .collect(Collectors.toSet());
        var found = matching.stream().filter(mapping -> !covered.contains(mapping.name())).toList();
        if (found.size() > 1) {
            throw new IllegalArgumentException(found.size() + " mappings translate from '"
                    + keySystem + "' to '" + valueSystem + "': "
                    + found.stream().map(Mapping::name).toList()
                    + ". Ask for one of them by name, or use mappingsFor(..)");
        }
        return found.stream().findFirst();
    }

    /**
     * Looks up a key without applying the mapping's {@link SimpleMapping#unmatched() unmatched}
     * behavior.
     *
     * @param mapping mapping name
     * @param key     left side of the mapping
     * @return the value declared for that key, empty if the mapping declares no entry for it
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> lookup(String mapping, String key);

    /**
     * Looks up a value without applying the mapping's
     * {@link SimpleMapping#reverseUnmatched() reverse unmatched} behavior.
     *
     * @param mapping mapping name
     * @param value   right side of the mapping
     * @return the key declared for that value, empty if the mapping declares no invertible
     * entry for it
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> lookupReverse(String mapping, String value);

    /**
     * Applies a mapping's unmatched behavior to a key, without looking the key up first.
     *
     * @param mapping mapping name
     * @param key     left side of the mapping
     * @return the fallback value, empty if the mapping declares no fallback
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> unmatched(String mapping, String key);

    /**
     * Applies a mapping's reverse unmatched behavior to a value, without looking it up first.
     *
     * @param mapping mapping name
     * @param value   right side of the mapping
     * @return the fallback key, empty if the mapping declares no reverse fallback
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> reverseUnmatched(String mapping, String value);

    /**
     * @param mapping mapping name
     * @param key     left side of the mapping
     * @return the mapped value, falling back to the mapping's unmatched behavior
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default Optional<String> map(String mapping, String key) {
        var value = lookup(mapping, key);
        return value.isPresent() ? value : unmatched(mapping, key);
    }

    /**
     * @param mapping      mapping name
     * @param key          left side of the mapping
     * @param defaultValue value to return if neither an entry nor a fallback yields one
     * @return the mapped value or {@code defaultValue}
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default String map(String mapping, String key, String defaultValue) {
        return map(mapping, key).orElse(defaultValue);
    }

    /**
     * @param mapping mapping name
     * @param value   right side of the mapping
     * @return the key mapping to that value, falling back to the mapping's reverse unmatched
     * behavior
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default Optional<String> mapReverse(String mapping, String value) {
        var key = lookupReverse(mapping, value);
        return key.isPresent() ? key : reverseUnmatched(mapping, value);
    }

    /**
     * @param mapping    mapping name
     * @param value      right side of the mapping
     * @param defaultKey key to return if neither an entry nor a fallback yields one
     * @return the reverse-mapped key or {@code defaultKey}
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default String mapReverse(String mapping, String value, String defaultKey) {
        return mapReverse(mapping, value).orElse(defaultKey);
    }

    /**
     * Maps a key like {@link #map(String, String)}, and says which code system the answer belongs
     * to and how it is displayed - what a caller needs to build a coded value from it.
     * <p>
     * The system is that of the mapping which actually answered: a
     * {@link CompositeMapping composite} answers with the system of the part that had the
     * answer, a {@link Unmatched.Delegate delegating} fallback with that of the mapping delegated
     * to. An {@link Unmatched.Identity identity} fallback answers with the key itself, and so with
     * the key system.
     * <p>
     * The default implementation knows none of that and answers with the mapping's value system
     * and no display; {@link DefaultMappings} overrides it.
     *
     * @param mapping mapping name
     * @param key     left side of the mapping
     * @return the mapped value with its system and display, falling back to the mapping's
     * unmatched behavior
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default Optional<Translation> translate(String mapping, String key) {
        return map(mapping, key)
            .map(value -> new Translation(value, valueSystem(mapping).orElse(null), null));
    }

    /**
     * Maps a value back like {@link #mapReverse(String, String)}, and says which code system the
     * answer belongs to and how it is displayed.
     *
     * @param mapping mapping name
     * @param value   right side of the mapping
     * @return the key mapping to that value with its system and display, falling back to the
     * mapping's reverse unmatched behavior
     * @throws IllegalArgumentException if the mapping name is not registered
     * @see #translate(String, String)
     */
    default Optional<Translation> translateReverse(String mapping, String value) {
        return mapReverse(mapping, value).map(key -> new Translation(key, keySystem(mapping).orElse(null), null));
    }

    /**
     * @param mapping mapping name
     * @return formal identifier of the key code system, empty if the mapping declares none
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> keySystem(String mapping);

    /**
     * @param mapping mapping name
     * @return formal identifier of the value code system, empty if the mapping declares none
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Optional<String> valueSystem(String mapping);

    /**
     * @param mapping mapping name
     * @return the keys this mapping translates, in declaration order. A key declared only by a
     * {@link Equivalence#DISJOINT} entry is not among them, because it does not translate, and
     * neither are the keys of a mapping this one delegates to - these are the keys this mapping
     * declares. For a {@link CompositeMapping composite} they are the keys of all its parts.
     * Changes to the set do not change the mapping
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    Set<String> keys(String mapping);

    /**
     * @param mapping mapping name
     * @return the values this mapping translates to, in declaration order. Changes to the
     * collection do not change the mapping
     * @throws IllegalArgumentException if the mapping name is not registered
     * @see #keys(String)
     */
    Collection<String> values(String mapping);

    /**
     * The entries a mapping declares, whatever its kind: those of a {@link SimpleMapping}, and for
     * a {@link CompositeMapping} those of its parts, in part order. Unlike {@link #keys(String)},
     * this is the declaration - {@link Equivalence#DISJOINT disjoint} entries included, and a key
     * declared by several parts listed once per part.
     *
     * @param mapping mapping name
     * @return the declared entries, in declaration order
     * @throws IllegalArgumentException if the mapping name is not registered
     */
    default List<Entry> entries(String mapping) {
        var declared = mapping(mapping)
                .orElseThrow(() -> new IllegalArgumentException("Unknown key " + mapping));
        if (declared instanceof CompositeMapping composite) {
            return composite.parts().stream().flatMap(part -> entries(part).stream()).toList();
        }
        return declared.entries();
    }

    /**
     * @return read access to the functions available to {@link Unmatched.Computed} fallbacks.
     * Functions are registered while building, with {@link Builder#function}; a built instance
     * cannot be changed through what this returns
     */
    MappingFunctions functions();

    static Builder builder() {
        return new Builder();
    }

    /**
     * Assembles a {@link Mappings} instance from mapping sources and named functions. Sources
     * are dispatched to a {@link MappingLoader} by file extension, or by a
     * {@link MappingLoader#format() format id} where the caller names one, so formats may be
     * mixed freely; load order is the order in which they are declared here. A builder is spent
     * once {@link #build()} has been called, so what it returns cannot be changed afterwards.
     */
    class Builder {

        private DefaultMappings mappings = new DefaultMappings();

        Builder() {
        }

        /**
         * Registers a function that an {@link Unmatched.Computed} fallback can refer to.
         * Functions must be registered before the source that refers to them is loaded.
         */
        public Builder function(String name, Function<String, String> function) {
            pending().registerFunction(name, function);
            return this;
        }

        /**
         * Allows a later mapping source to silently replace a mapping of the same name loaded
         * earlier. Off by default: a duplicate mapping name is a load-time error.
         */
        public Builder allowOverride(boolean allowOverride) {
            pending().setAllowOverride(allowOverride);
            return this;
        }

        /**
         * @see DefaultMappings#setAllowReverseCollisions(boolean)
         */
        public Builder allowReverseCollisions(boolean allowReverseCollisions) {
            pending().setAllowReverseCollisions(allowReverseCollisions);
            return this;
        }

        /**
         * Loads a mapping source, in whichever format its file extension identifies.
         *
         * @param location a URL, or a {@code classpath:} location such as
         *                 {@code classpath:/META-INF/map/atna2fhir.mapping.xml}
         */
        public Builder load(String location) {
            return load(location, null);
        }

        /**
         * Loads a mapping source in a format named explicitly, for a source whose name does not
         * identify one:
         * <pre>
         * Mappings.builder()
         *     .load("https://tx.example.org/ConceptMap/gender", "conceptmap-r4-json")
         *     .build();
         * </pre>
         *
         * @param location a URL, or a {@code classpath:} location
         * @param format   a {@link MappingLoader#format() format id} such as {@code xml},
         *                 {@code yaml} or {@code conceptmap-r4-json}, or {@code null} to dispatch by
         *                 file extension
         */
        public Builder load(String location, String format) {
            return load(toUrl(location), format);
        }

        public Builder load(URL url) {
            return load(url, (String) null);
        }

        /**
         * @see #load(String, String)
         */
        public Builder load(URL url, String format) {
            pending().load(url, format);
            return this;
        }

        /**
         * Loads a mapping source with a loader the caller provides, which need not be one the
         * {@link java.util.ServiceLoader} can see.
         */
        public Builder load(URL url, MappingLoader loader) {
            pending().load(url, loader);
            return this;
        }

        public Builder load(URI uri) {
            return load(uri, null);
        }

        /**
         * @see #load(String, String)
         */
        public Builder load(URI uri, String format) {
            try {
                return load(uri.toURL(), format);
            } catch (MalformedURLException | IllegalArgumentException e) {
                throw new MappingException(uri, "Not a readable mapping location", e);
            }
        }

        /**
         * Registers an already assembled mapping, for programmatic use and for tests.
         */
        public Builder mapping(Mapping mapping) {
            pending().register(mapping, null);
            return this;
        }

        public Mappings build() {
            var built = pending();
            mappings = null;
            return built;
        }

        private DefaultMappings pending() {
            if (mappings == null) {
                throw new IllegalStateException("This builder has already been built");
            }
            return mappings;
        }

        private static URL toUrl(String location) {
            if (location == null) {
                throw new IllegalArgumentException("Mapping location required");
            }
            if (location.startsWith("classpath:")) {
                var path = location.substring("classpath:".length());
                var resource = path.startsWith("/")
                        ? Mappings.class.getResource(path)
                        : Mappings.class.getClassLoader().getResource(path);
                if (resource == null) {
                    throw new MappingException(safeUri(location), "Mapping resource not found");
                }
                return resource;
            }
            try {
                return URI.create(location).toURL();
            } catch (MalformedURLException | IllegalArgumentException e) {
                var resource = Mappings.class.getClassLoader().getResource(location);
                if (resource == null) {
                    throw new MappingException(safeUri(location), "Mapping resource not found", e);
                }
                return resource;
            }
        }

        private static URI safeUri(String location) {
            try {
                return new URI(null, null, location, null);
            } catch (URISyntaxException e) {
                return null;
            }
        }
    }
}
