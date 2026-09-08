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

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * In-memory {@link Mappings} implementation, backed by one forward and one reverse index per
 * mapping. Both indices are built once, when the mapping is registered, and are never rebuilt
 * afterwards &mdash; which is what keeps loading a second mapping source from disturbing the
 * mappings loaded from the first.
 * <p>
 * Reads are lock-free against an immutable snapshot; registration is serialized.
 * <p>
 * This is the extension point for wiring the mapping service into a container: a subclass may add
 * whatever way of naming a source that container prefers and calls {@link #load(URL)}. See
 * {@code SpringMappings} in {@code ipf-commons-spring}, which takes Spring {@code Resource}s.
 *
 * @since 6.0
 */
public class DefaultMappings implements Mappings {

    private static final Logger log = LoggerFactory.getLogger(DefaultMappings.class);

    /**
     * How long a mapping source fetched over the network may take to answer. A mapping is loaded
     * while the application starts, so a source that never answers would otherwise hold the start
     * up for as long as the peer keeps the socket open. Ignored by {@code file:}, {@code jar:} and
     * classpath locations, which is every source IPF itself ships.
     */
    private static final int CONNECT_TIMEOUT_MS = 10_000;

    private static final int READ_TIMEOUT_MS = 30_000;

    /**
     * How much a mapping source may be. Every format is read into memory, so an endless response
     * from a location the application does not control would otherwise end as an
     * {@link OutOfMemoryError} that takes the whole process with it rather than the one mapping
     * file. Far above any real mapping file: the largest IPF ships is a few dozen KiB, and a
     * terminology table with a hundred thousand codes is a few MiB.
     */
    private static final long MAX_SOURCE_SIZE = 64L * 1024 * 1024;

    private final MappingFunctionRegistry functions = new MappingFunctionRegistry();

    private volatile Map<String, Indexed> state = Collections.emptyMap();

    /**
     * Whether an existing mapping may be overridden to a mapping definition registered later
     */
    @Setter
    private volatile boolean allowOverride;

    /**
     *  Whether a mapping may register two invertible entries sharing a value, in which case the
     *  last one declared wins the reverse index. Off by default: a value with two inverses is a
     *  load-time error, since the alternative is a reverse lookup that silently answers whichever
     *  key happened to be written last.
     */
    @Setter
    private volatile boolean allowReverseCollisions;

    /**
     * Applies every discovered {@link MappingFunctionProvider}, so that a mapping file shipped
     * with the functions it names works without any configuration.
     */
    public DefaultMappings() {
        MappingFunctionProvider.registerAll(functions);
    }

    /**
     * A mapping together with its two lookup indices and the source it was read from.
     */
    private record Indexed(Mapping mapping, Map<String, String> forward, Map<String, String> reverse, URI source) {
    }

    @Override
    public MappingFunctionRegistry functions() {
        return functions;
    }

    // ------------------------------------------------------------------ loading

    /**
     * Reads a mapping source and registers everything it contains.
     *
     * @param url location of the source; the {@link MappingLoader} is selected by its file
     *            extension
     */
    public void load(URL url) {
        load(url, (String) null);
    }

    /**
     * Reads a mapping source in a format named explicitly, for a source whose name does not
     * identify one - a ConceptMap fetched over HTTP, a file called {@code gender.xml} - or where
     * the file extension is not the loader the caller wants.
     *
     * @param url    location of the source
     * @param format a {@link MappingLoader#format() format id} such as {@code xml} or
     *               {@code conceptmap-r4-json}, or {@code null} to select the loader by file
     *               extension
     */
    public void load(URL url, String format) {
        var source = toUri(url);
        load(url, MappingLoaders.resolve(source, format));
    }

    /**
     * Reads a mapping source with an explicitly provided loader, which need not be one the
     * {@link java.util.ServiceLoader} can see.
     */
    public void load(URL url, MappingLoader loader) {
        var source = toUri(url);
        List<Mapping> loaded;
        try (var in = open(url)) {
            loaded = loader.load(in, source, functions);
        } catch (IOException e) {
            throw new MappingException(source, "Could not read mapping source", e);
        }
        loaded.forEach(mapping -> register(mapping, source));
    }

    /**
     * Opens a source with the limits that apply to reading one at all - a mapping file is data
     * from wherever the application points the loader, and the ones that matter are how long it
     * may take and how much of it there may be.
     */
    private static InputStream open(URL url) throws IOException {
        var connection = url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        return new BoundedInputStream(connection.getInputStream(), MAX_SOURCE_SIZE);
    }

    /**
     * Fails rather than reads on past the limit, so that the parser downstream never sees more
     * than {@link #MAX_SOURCE_SIZE} bytes however much the other end is willing to send.
     */
    private static final class BoundedInputStream extends FilterInputStream {

        private final long limit;
        private long read;

        private BoundedInputStream(InputStream in, long limit) {
            super(in);
            this.limit = limit;
        }

        @Override
        public int read() throws IOException {
            var b = super.read();
            if (b >= 0) {
                count(1);
            }
            return b;
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            var n = super.read(buffer, offset, length);
            if (n > 0) {
                count(n);
            }
            return n;
        }

        private void count(int n) throws IOException {
            read += n;
            if (read > limit) {
                throw new IOException("mapping source is larger than the " + limit
                        + " bytes a mapping file may be");
            }
        }
    }

    /**
     * Registers a single mapping, validating it against the model's constraints.
     *
     * @param mapping the mapping to register
     * @param source  where it was read from, for diagnostics; may be {@code null}
     */
    public synchronized void register(Mapping mapping, URI source) {
        var current = state;
        var existing = current.get(mapping.name());
        if (existing != null && !allowOverride && !mapping.override()) {
            throw new MappingException(source, "Duplicate mapping '" + mapping.name()
                    + "', already defined in " + Optional.ofNullable(existing.source())
                    .map(URI::toString).orElse("this application")
                    + ". Declare override=\"true\" on the later one if this is intended");
        }
        if (existing != null) {
            log.debug("Mapping '{}' from {} replaces the one from {}", mapping.name(), source, existing.source());
        }
        var next = new LinkedHashMap<>(current);
        next.put(mapping.name(), index(mapping, source));
        state = Collections.unmodifiableMap(next);
    }

    public synchronized void clear() {
        state = Collections.emptyMap();
        functions.clear();
        MappingFunctionProvider.registerAll(functions);
    }

    private Indexed index(Mapping mapping, URI source) {
        validateFunction(mapping.unmatched(), mapping, source);
        validateFunction(mapping.reverseUnmatched(), mapping, source);
        validateDelegate(mapping.unmatched(), mapping, source, false);
        validateDelegate(mapping.reverseUnmatched(), mapping, source, true);

        // LinkedHashMap, not Map.copyOf: a mapping may declare a null key, and declaration
        // order is part of the contract of keys() and values().
        var forward = new LinkedHashMap<String, String>();
        var reverse = new LinkedHashMap<String, String>();
        for (var entry : mapping.entries()) {
            // A disjoint entry says the two concepts are explicitly not equivalent, so its value
            // is not an answer to its key. It stays in mapping.entries(), which is the declaration,
            // but not in either index, which is what answers a lookup.
            if (!entry.isTranslation()) {
                continue;
            }
            forward.put(entry.key(), entry.value());
            if (mapping.reversible() && entry.isInvertible()) {
                var collision = reverse.put(entry.value(), entry.key());
                if (collision != null && !allowReverseCollisions) {
                    throw new MappingException(source, "Mapping '" + mapping.name() + "' maps both '"
                            + collision + "' and '" + entry.key() + "' onto '" + entry.value()
                            + "'. Mark all but the canonical one as narrower or wider, or declare"
                            + " the mapping as not reversible");
                }
            }
        }
        return new Indexed(mapping, Collections.unmodifiableMap(forward), Collections.unmodifiableMap(reverse), source);
    }

    /**
     * A delegating fallback must name a mapping that is already registered. That is what makes a
     * cycle undeclarable: reaching one would need a forward reference, and a later mapping that
     * overrides a link in the chain is checked here again from its own end.
     */
    private void validateDelegate(Unmatched unmatched, Mapping mapping, URI source, boolean reverse) {
        if (!(unmatched instanceof Unmatched.Delegate delegate)) {
            return;
        }
        var visited = new LinkedHashSet<String>();
        visited.add(mapping.name());
        var next = delegate.mapping();
        while (next != null) {
            var target = state.get(next);
            if (target == null) {
                throw new MappingException(source, "Mapping '" + mapping.name() + "' delegates to '"
                        + next + "', which is not registered. Load the mapping it delegates to"
                        + " first - which is also what keeps a chain of delegations acyclic");
            }
            if (!visited.add(next)) {
                throw new MappingException(source, "Mapping '" + mapping.name() + "' delegates in a"
                        + " cycle: " + visited + " -> " + next);
            }
            var fallback = reverse ? target.mapping().reverseUnmatched() : target.mapping().unmatched();
            next = fallback instanceof Unmatched.Delegate further ? further.mapping() : null;
        }
    }

    private void validateFunction(Unmatched unmatched, Mapping mapping, URI source) {
        if (unmatched instanceof Unmatched.Computed computed && !functions.contains(computed.ref())) {
            throw new MappingException(source, "Mapping '" + mapping.name() + "' refers to the unknown"
                    + " mapping function '" + computed.ref() + "'");
        }
    }

    // ------------------------------------------------------------------ lookup

    @Override
    public Optional<Mapping> mapping(String mapping) {
        var indexed = state.get(mapping);
        return indexed == null ? Optional.empty() : Optional.of(indexed.mapping());
    }

    @Override
    public Set<String> mappingNames() {
        return new LinkedHashSet<>(state.keySet());
    }

    @Override
    public List<Mapping> mappingsFor(String keySystem, String valueSystem) {
        return state.values().stream()
                .map(Indexed::mapping)
                .filter(mapping -> matches(keySystem, mapping.keySystem()))
                .filter(mapping -> matches(valueSystem, mapping.valueSystem()))
                .toList();
    }

    private static boolean matches(String wanted, String declared) {
        return wanted == null || wanted.equals(declared);
    }

    @Override
    public Optional<String> lookup(String mapping, String key) {
        return Optional.ofNullable(required(mapping).forward().get(key));
    }

    @Override
    public Optional<String> lookupReverse(String mapping, String value) {
        return Optional.ofNullable(required(mapping).reverse().get(value));
    }

    @Override
    public Optional<String> unmatched(String mapping, String key) {
        return resolve(required(mapping).mapping().unmatched(), key, mapping, false, new LinkedHashSet<>());
    }

    @Override
    public Optional<String> reverseUnmatched(String mapping, String value) {
        return resolve(required(mapping).mapping().reverseUnmatched(), value, mapping, true, new LinkedHashSet<>());
    }

    @Override
    public Optional<String> keySystem(String mapping) {
        return Optional.ofNullable(required(mapping).mapping().keySystem());
    }

    @Override
    public Optional<String> valueSystem(String mapping) {
        return Optional.ofNullable(required(mapping).mapping().valueSystem());
    }

    @Override
    public Set<String> keys(String mapping) {
        return new LinkedHashSet<>(required(mapping).forward().keySet());
    }

    @Override
    public Collection<String> values(String mapping) {
        return new ArrayList<>(required(mapping).forward().values());
    }

    // Once the build moves past Java 17 this is a pattern switch over the sealed interface,
    // which the compiler can then check for exhaustiveness.
    private Optional<String> resolve(Unmatched unmatched, String input, String mapping,
                                     boolean reverse, Set<String> visited) {
        if (unmatched instanceof Unmatched.Absent) {
            return Optional.empty();
        }
        if (unmatched instanceof Unmatched.Identity) {
            return Optional.ofNullable(input);
        }
        if (unmatched instanceof Unmatched.Fixed fixed) {
            return Optional.ofNullable(fixed.value());
        }
        if (unmatched instanceof Unmatched.Computed computed) {
            var function = functions.lookup(computed.ref())
                    .orElseThrow(() -> new IllegalStateException("Mapping '" + mapping
                            + "' refers to the unknown mapping function '" + computed.ref() + "'"));
            return Optional.ofNullable(function.apply(input));
        }
        if (unmatched instanceof Unmatched.Fail) {
            throw new IllegalArgumentException("Mapping '" + mapping + "' has no entry for '" + input + "'");
        }
        if (unmatched instanceof Unmatched.Delegate delegate) {
            // Ask the delegate in full - its entries, then its own fallback - so the chain ends
            // wherever a fallback finally answers on its own. Registration rejects a cycle; the
            // visited set is what makes that guarantee visible here.
            if (!visited.add(mapping)) {
                throw new IllegalStateException("Mapping '" + mapping + "' delegates in a cycle: "
                        + visited);
            }
            var target = required(delegate.mapping());
            var declared = reverse
                    ? Optional.ofNullable(target.reverse().get(input))
                    : Optional.ofNullable(target.forward().get(input));
            return declared.isPresent()
                    ? declared
                    : resolve(reverse ? target.mapping().reverseUnmatched() : target.mapping().unmatched(),
                    input, delegate.mapping(), reverse, visited);
        }
        throw new IllegalStateException("Unsupported unmatched behavior " + unmatched);
    }

    private Indexed required(String mapping) {
        var indexed = state.get(mapping);
        if (indexed == null) {
            // Message kept verbatim from BidiMappingService, which callers assert on.
            throw new IllegalArgumentException("Unknown key " + mapping);
        }
        return indexed;
    }

    private static URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            return URI.create(url.toString().replace(" ", "%20"));
        }
    }
}
