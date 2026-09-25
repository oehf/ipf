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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * In-memory {@link Mappings} implementation, backed by one forward and one reverse index per
 * mapping. Both indices are built once, when the mapping is registered, and are never rebuilt
 * afterwards &mdash; which is what keeps loading a second mapping source from disturbing the
 * mappings loaded from the first.
 * <p>
 * Reads are lock-free against an immutable snapshot; registration is serialized, and validates
 * each mapping against what is already registered - which is what lets a lookup follow
 * delegations and composites without checking them again.
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
     * A mapping together with its two lookup indices and the source it was read from. The indices
     * hold the entries rather than just their other side, so that an answer comes with its display.
     * A composite mapping has empty indices; it answers through its parts. {@code superseded} is the
     * definition this one overrides, kept only where a fallback delegates to it.
     */
    private record Indexed(Mapping mapping, Map<String, Entry> forward, Map<String, Entry> reverse, URI source,
                           Map<Direction, String> delegates, Indexed superseded) {

        /**
         * @return the mappings a lookup in this direction is handed on to
         */
        List<String> onward(Direction direction) {
            return DefaultMappings.onward(mapping, delegates, direction);
        }

        /**
         * @return whether a lookup handed on to this name goes to the definition this one overrides
         */
        boolean supersedes(String name) {
            return superseded != null && mapping.name().equals(name);
        }
    }

    /**
     * @return read access to the registered functions; register them with
     * {@link #registerFunction(String, java.util.function.Function)}
     */
    @Override
    public MappingFunctions functions() {
        return new MappingFunctions() {
            @Override
            public Optional<java.util.function.Function<String, String>> lookup(String name) {
                return functions.lookup(name);
            }

            @Override
            public boolean contains(String name) {
                return functions.contains(name);
            }
        };
    }

    /**
     * Registers a function that {@link Unmatched.Computed} fallbacks can name, replacing any
     * registered before under that name. Register it before loading a source that names it: a
     * source naming an unknown function is rejected as it is read.
     *
     * @param name     function name
     * @param function the function; it must tolerate a {@code null} argument
     */
    public synchronized void registerFunction(String name, java.util.function.Function<String, String> function) {
        functions.register(name, function);
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
        // A loader may register functions as it reads - the Groovy one does for its closures. They
        // go into a copy, kept only if every mapping of the source registers, so that a source
        // failing halfway cannot have replaced the function of a mapping registered before.
        var scratch = functions.copy();
        List<Mapping> loaded;
        try (var in = open(url)) {
            loaded = loader.load(in, source, scratch);
        } catch (IOException e) {
            throw new MappingException(source, "Could not read mapping source", e);
        } catch (IllegalArgumentException e) {
            // e.g. a mapping without a name, which the model rejects wherever it is built
            throw new MappingException(source, e.getMessage(), e);
        }
        registerAll(loaded, source, scratch);
    }

    /**
     * Opens a source with the limits that apply to reading one at all. A mapping file is data
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
     * Registers a single mapping, validating it against the model's constraints.
     *
     * @param mapping the mapping to register
     * @param source  where it was read from, for diagnostics; may be {@code null}
     */
    public void register(Mapping mapping, URI source) {
        registerAll(List.of(mapping), source, functions);
    }

    /**
     * Registers the mappings of one source all or nothing: each is validated against the
     * mappings registered before plus those of the same source ahead of it, and only once all of
     * them pass are they published - together with the functions the source's loader registered.
     * A source failing halfway leaves nothing behind, so reading it again reports its actual
     * problem rather than a duplicate of its own first mappings, and no reader ever sees the parts
     * of a composite without the composite.
     */
    private synchronized void registerAll(List<Mapping> mappings, URI source, MappingFunctionRegistry scratch) {
        var next = new LinkedHashMap<>(state);
        for (var mapping : mappings) {
            var existing = next.get(mapping.name());
            if (existing != null && !allowOverride && !mapping.override()) {
                throw new MappingException(source, "Duplicate mapping '" + mapping.name()
                        + "', already defined in " + Optional.ofNullable(existing.source())
                        .map(URI::toString).orElse("this application")
                        + ". Declare override=\"true\" on the later one if this is intended");
            }
            if (existing != null) {
                log.debug("Mapping '{}' from {} replaces the one from {}", mapping.name(), source, existing.source());
            }
            next.put(mapping.name(), index(mapping, source, next, scratch));
        }
        state = Collections.unmodifiableMap(next);
        if (scratch != functions) {
            functions.registerAll(scratch);
        }
    }

    /**
     * Forgets every mapping. The {@link #functions() functions} stay registered: they are
     * configuration, which mapping sources loaded afterwards refer to just as before.
     */
    public synchronized void clear() {
        state = Collections.emptyMap();
    }

    /**
     * @param registered the mappings registered so far, which the new one is validated against
     * @param functions  the functions it may name
     */
    private Indexed index(Mapping declared, URI source, Map<String, Indexed> registered,
                          MappingFunctionRegistry functions) {
        var delegates = resolveDelegates(declared, source, registered);
        var superseded = superseded(declared, source, registered, delegates);
        validate(declared, source, registered, functions, delegates, superseded);
        if (!(declared instanceof SimpleMapping mapping)) {
            return new Indexed(declared, Map.of(), Map.of(), source, delegates, null);
        }

        // LinkedHashMap, not Map.copyOf: a mapping may declare a null key, and declaration
        // order is part of the contract of keys() and values().
        var forward = new LinkedHashMap<String, Entry>();
        var reverse = new LinkedHashMap<String, Entry>();
        for (var entry : mapping.entries()) {
            // A disjoint entry says the two concepts are explicitly not equivalent, so its value
            // is not an answer to its key. It stays in mapping.entries(), which is the declaration,
            // but not in either index, which is what answers a lookup.
            if (!entry.isTranslation()) {
                continue;
            }
            var duplicate = forward.put(entry.key(), entry);
            if (duplicate != null) {
                throw new MappingException(source, "Mapping '" + mapping.name() + "' declares the key '"
                        + entry.key() + "' twice, translating it to both '" + duplicate.value() + "' and '"
                        + entry.value() + "'. Keep one, or mark the other as disjoint");
            }
            if (mapping.reversible() && entry.isInvertible()) {
                var collision = reverse.put(entry.value(), entry);
                if (collision != null && !allowReverseCollisions) {
                    throw new MappingException(source, "Mapping '" + mapping.name() + "' maps both '"
                            + collision.key() + "' and '" + entry.key() + "' onto '" + entry.value()
                            + "'. Mark all but the canonical one as narrower or wider, or declare"
                            + " the mapping as not reversible");
                }
            }
        }
        return new Indexed(mapping, Collections.unmodifiableMap(forward), Collections.unmodifiableMap(reverse), source,
                delegates, superseded);
    }

    /**
     * A mapping overriding another may delegate to the definition it replaces by naming its own
     * mapping, and so declare only the codes that differ under the name its callers already use.
     * That definition is kept for this mapping only; under the name, the new one is registered.
     *
     * @return the definition the mapping overrides if a fallback delegates to it, else null
     */
    private static Indexed superseded(Mapping mapping, URI source, Map<String, Indexed> registered,
                                      Map<Direction, String> delegates) {
        if (!delegates.containsValue(mapping.name())) {
            return null;
        }
        var existing = registered.get(mapping.name());
        if (existing == null) {
            throw new MappingException(source, "Mapping '" + mapping.name() + "' delegates to itself, but"
                    + " overrides no mapping registered before. Only an overriding mapping can delegate to"
                    + " the definition it replaces");
        }
        return existing;
    }

    // ------------------------------------------------------------------ validation

    private static void validate(Mapping mapping, URI source, Map<String, Indexed> registered,
                                 MappingFunctionRegistry functions, Map<Direction, String> delegates,
                                 Indexed superseded) {
        validateParts(mapping, source, registered);
        for (var direction : Direction.values()) {
            if (mapping instanceof SimpleMapping
                    && direction.fallback(mapping) instanceof Unmatched.Computed computed
                    && !functions.contains(computed.ref())) {
                throw new MappingException(source, "Mapping '" + mapping.name() + "' refers to the unknown"
                        + " mapping function '" + computed.ref() + "'");
            }
            var path = new ArrayDeque<String>();
            path.add(mapping.name());
            for (var next : onward(mapping, delegates, direction)) {
                if (superseded != null && next.equals(mapping.name())) {
                    path.addLast(next);
                    validateOnward(mapping, superseded, direction, path, source, registered);
                    path.removeLast();
                } else {
                    validateReachable(mapping, next, direction, path, source, registered);
                }
            }
        }
    }

    /**
     * The parts of a composite must be ordinary mappings that are already registered, and a
     * mapping serving as a part cannot be replaced by a composite, which keeps composites one
     * level deep.
     */
    private static void validateParts(Mapping mapping, URI source, Map<String, Indexed> registered) {
        if (!(mapping instanceof CompositeMapping composite)) {
            return;
        }
        for (var part : composite.parts()) {
            var target = registered.get(part);
            if (target == null) {
                throw new MappingException(source, "Composite mapping '" + mapping.name() + "' has the"
                        + " part '" + part + "', which is not registered. Load its parts first");
            }
            if (target.mapping() instanceof CompositeMapping) {
                throw new MappingException(source, "Composite mapping '" + mapping.name() + "' has the"
                        + " part '" + part + "', which is itself composite. Name its parts instead");
            }
        }
        registered.values().stream()
                .filter(indexed -> indexed.mapping() instanceof CompositeMapping other
                        && other.parts().contains(mapping.name()))
                .findFirst()
                .ifPresent(indexed -> {
                    throw new MappingException(source, "Mapping '" + mapping.name() + "' is a part of"
                            + " the composite '" + indexed.mapping().name() + "', so it cannot become"
                            + " a composite itself");
                });
    }

    /**
     * Everything a lookup can be handed on to from a mapping being registered must already be
     * registered, and must not lead back to it - through delegating fallbacks and into the parts
     * of composites alike. A cycle can only close when a mapping is registered, and it then runs
     * through that mapping, so checking from there on every registration keeps the whole graph
     * acyclic and lets a lookup follow it without guarding against one.
     *
     * @param path the mappings from the one being registered to here
     */
    private static void validateReachable(Mapping mapping, String next, Direction direction,
                                          Deque<String> path, URI source, Map<String, Indexed> registered) {
        if (path.contains(next)) {
            throw new MappingException(source, "Mapping '" + mapping.name() + "' delegates in a cycle: "
                    + String.join(" -> ", path) + " -> " + next);
        }
        var target = registered.get(next);
        if (target == null) {
            // parts are checked to be registered before, so only a delegation gets here
            throw new MappingException(source, "Mapping '" + path.getLast() + "' delegates to '" + next
                    + "', which is not registered. Load the mapping it delegates to first - which is also"
                    + " what keeps a chain of delegations acyclic");
        }
        path.addLast(next);
        validateOnward(mapping, target, direction, path, source, registered);
        path.removeLast();
    }

    /**
     * Follows everything a lookup can be handed on to from a registered mapping - including the
     * definition it overrides, which a later override elsewhere may lead back from.
     */
    private static void validateOnward(Mapping mapping, Indexed from, Direction direction, Deque<String> path,
                                       URI source, Map<String, Indexed> registered) {
        for (var further : from.onward(direction)) {
            if (from.supersedes(further)) {
                path.addLast(further);
                validateOnward(mapping, from.superseded(), direction, path, source, registered);
                path.removeLast();
            } else {
                validateReachable(mapping, further, direction, path, source, registered);
            }
        }
    }

    /**
     * @param delegates the mappings the fallbacks delegate to, resolved at registration
     * @return the mappings a lookup in this direction may be handed on to: the parts of a
     * composite, or the mapping a fallback delegates to
     */
    private static List<String> onward(Mapping mapping, Map<Direction, String> delegates, Direction direction) {
        if (mapping instanceof CompositeMapping composite) {
            return composite.parts();
        }
        var delegate = delegates.get(direction);
        return delegate == null ? List.of() : List.of(delegate);
    }

    /**
     * Resolves the mapping each delegating fallback names, once, when the mapping is registered.
     * A name is used as it is if it is registered. A name without a version - the canonical of a
     * FHIR ConceptMap, as an {@code other-map} fallback often gives it - also finds a mapping
     * registered under that name with a version, {@code name|version}, if there is exactly one;
     * with several it is ambiguous and rejected. Pinning the result means that registering a
     * further version later cannot change where an existing mapping delegates to.
     */
    private static Map<Direction, String> resolveDelegates(Mapping mapping, URI source,
                                                           Map<String, Indexed> registered) {
        var delegates = new EnumMap<Direction, String>(Direction.class);
        if (mapping instanceof SimpleMapping) {
            for (var direction : Direction.values()) {
                if (direction.fallback(mapping) instanceof Unmatched.Delegate delegate) {
                    delegates.put(direction, resolveDelegate(delegate.mapping(), mapping, source, registered));
                }
            }
        }
        return Collections.unmodifiableMap(delegates);
    }

    private static String resolveDelegate(String name, Mapping mapping, URI source, Map<String, Indexed> registered) {
        if (registered.containsKey(name) || name.contains("|")) {
            return name;
        }
        var prefix = name + "|";
        var versions = registered.keySet().stream()
                .filter(candidate -> candidate.startsWith(prefix) && candidate.indexOf('#', prefix.length()) < 0)
                .toList();
        if (versions.size() > 1) {
            throw new MappingException(source, "Mapping '" + mapping.name() + "' delegates to '" + name
                    + "', which is registered in several versions " + versions + ". Name the version, as in '"
                    + versions.get(0) + "'");
        }
        // not registered at all: the reachability check reports it
        return versions.isEmpty() ? name : versions.get(0);
    }

    // ------------------------------------------------------------------ lookup

    @Override
    public Optional<Mapping> mapping(String mapping) {
        return Optional.ofNullable(state.get(mapping)).map(Indexed::mapping);
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
        return declared(required(mapping), key, Direction.FORWARD).map(Translation::code);
    }

    @Override
    public Optional<String> lookupReverse(String mapping, String value) {
        return declared(required(mapping), value, Direction.REVERSE).map(Translation::code);
    }

    @Override
    public Optional<String> unmatched(String mapping, String key) {
        return fallback(required(mapping), key, Direction.FORWARD).map(Translation::code);
    }

    @Override
    public Optional<String> reverseUnmatched(String mapping, String value) {
        return fallback(required(mapping), value, Direction.REVERSE).map(Translation::code);
    }

    @Override
    public Optional<Translation> translate(String mapping, String key) {
        return answer(required(mapping), key, Direction.FORWARD);
    }

    @Override
    public Optional<Translation> translateReverse(String mapping, String value) {
        return answer(required(mapping), value, Direction.REVERSE);
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
        return new LinkedHashSet<>(forward(required(mapping)).keySet());
    }

    @Override
    public Collection<String> values(String mapping) {
        return forward(required(mapping)).values().stream().map(Entry::value)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @return the entries a lookup finds before a fallback answers on its own: the mapping's own,
     * for a composite those of its parts with the first part declaring a key winning, and then
     * those of the mappings a fallback delegates to
     */
    private Map<String, Entry> forward(Indexed indexed) {
        if (!(indexed.mapping() instanceof CompositeMapping) && !indexed.delegates().containsKey(Direction.FORWARD)) {
            return indexed.forward();
        }
        var merged = new LinkedHashMap<String, Entry>();
        collectForward(indexed, merged);
        return merged;
    }

    /**
     * Adds what a lookup finds in this mapping to the entries found before it, which win.
     *
     * @return whether a code without an entry may still go unanswered, so that a lookup asks on
     */
    private boolean collectForward(Indexed indexed, Map<String, Entry> merged) {
        if (!(indexed.mapping() instanceof CompositeMapping composite)) {
            indexed.forward().forEach(merged::putIfAbsent);
            return collectFallback(indexed, merged);
        }
        var parts = composite.parts().stream().map(this::required).toList();
        parts.forEach(part -> part.forward().forEach(merged::putIfAbsent));
        for (var part : parts) {
            if (!collectFallback(part, merged)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return whether a lookup asks on after this fallback: it does after an absent one, and a
     * delegating one is followed. Any other fallback is taken to answer every code itself
     */
    private boolean collectFallback(Indexed indexed, Map<String, Entry> merged) {
        var fallback = indexed.mapping().unmatched();
        if (fallback instanceof Unmatched.Delegate) {
            return collectForward(delegate(indexed, Direction.FORWARD), merged);
        }
        return fallback instanceof Unmatched.Absent;
    }

    /**
     * @return the declared answer, else the fallback's
     */
    private Optional<Translation> answer(Indexed indexed, String input, Direction direction) {
        return declared(indexed, input, direction).or(() -> fallback(indexed, input, direction));
    }

    /**
     * @return the entry declared for the input, from the first part declaring one for a composite
     */
    private Optional<Translation> declared(Indexed indexed, String input, Direction direction) {
        if (indexed.mapping() instanceof CompositeMapping composite) {
            return firstOfParts(composite, part -> declared(part, input, direction));
        }
        return Optional.ofNullable(direction.index(indexed).get(input))
                .flatMap(entry -> direction.translation(entry, indexed.mapping()));
    }

    /**
     * @return what the fallback answers, from the first part whose fallback answers for a composite
     */
    private Optional<Translation> fallback(Indexed indexed, String input, Direction direction) {
        if (indexed.mapping() instanceof CompositeMapping composite) {
            return firstOfParts(composite, part -> fallback(part, input, direction));
        }
        return resolve(direction.fallback(indexed.mapping()), input, indexed, direction);
    }

    private Optional<Translation> firstOfParts(CompositeMapping composite,
                                               Function<Indexed, Optional<Translation>> ask) {
        return composite.parts().stream()
                .map(this::required)
                .map(ask)
                .flatMap(Optional::stream)
                .findFirst();
    }

    // Once the build moves past Java 17 this is a pattern switch over the sealed interface,
    // which the compiler can then check for exhaustiveness.
    private Optional<Translation> resolve(Unmatched unmatched, String input, Indexed indexed,
                                          Direction direction) {
        var mapping = indexed.mapping();
        if (unmatched instanceof Unmatched.Absent) {
            return Optional.empty();
        }
        if (unmatched instanceof Unmatched.Identity) {
            // the input comes back unchanged, so it stays in the system it was given in
            return translation(input, direction.inputSystem(mapping), null);
        }
        if (unmatched instanceof Unmatched.Fixed fixed) {
            return translation(fixed.value(), direction.outputSystem(mapping), null);
        }
        if (unmatched instanceof Unmatched.Computed computed) {
            var function = functions.lookup(computed.ref())
                    .orElseThrow(() -> new IllegalStateException("Mapping '" + mapping.name()
                            + "' refers to the unknown mapping function '" + computed.ref() + "'"));
            return translation(function.apply(input), direction.outputSystem(mapping), null);
        }
        if (unmatched instanceof Unmatched.Fail) {
            throw new IllegalArgumentException("Mapping '" + mapping.name() + "' has no entry for '" + input + "'");
        }
        if (unmatched instanceof Unmatched.Delegate) {
            // Ask the delegate in full - its entries, then its own fallback - so the chain ends
            // wherever a fallback finally answers on its own. Registration keeps it acyclic.
            return answer(delegate(indexed, direction), input, direction);
        }
        throw new IllegalStateException("Unsupported unmatched behavior " + unmatched);
    }

    /**
     * @return the mapping a fallback in this direction delegates to: the definition this one
     * overrides if it names its own mapping, else the one registered under the name
     */
    private Indexed delegate(Indexed indexed, Direction direction) {
        var name = indexed.delegates().get(direction);
        return indexed.supersedes(name) ? indexed.superseded() : required(name);
    }

    private static Optional<Translation> translation(String code, String system, String display) {
        return code == null ? Optional.empty() : Optional.of(new Translation(code, system, display));
    }

    /**
     * Which way a lookup reads a mapping: from key to value, or back. Everything that differs
     * between the two is here, so the lookup itself is written once.
     */
    private enum Direction {
        FORWARD, REVERSE;

        Map<String, Entry> index(Indexed indexed) {
            return this == FORWARD ? indexed.forward() : indexed.reverse();
        }

        Unmatched fallback(Mapping mapping) {
            return this == FORWARD ? mapping.unmatched() : mapping.reverseUnmatched();
        }

        String inputSystem(Mapping mapping) {
            return this == FORWARD ? mapping.keySystem() : mapping.valueSystem();
        }

        String outputSystem(Mapping mapping) {
            return this == FORWARD ? mapping.valueSystem() : mapping.keySystem();
        }

        Optional<Translation> translation(Entry entry, Mapping mapping) {
            return this == FORWARD
                    ? DefaultMappings.translation(entry.value(), outputSystem(mapping), entry.valueDisplay())
                    : DefaultMappings.translation(entry.key(), outputSystem(mapping), entry.keyDisplay());
        }
    }

    private Indexed required(String mapping) {
        var indexed = state.get(mapping);
        if (indexed == null) {
            // Message kept verbatim from BidiMappingService, which callers assert on.
            throw new IllegalArgumentException("Unknown key " + mapping);
        }
        return indexed;
    }

    /**
     * @return the location as a URI, for diagnostics and to select a loader. A URL may hold
     * characters a URI must quote - a space in a file name - which are quoted rather than fail
     */
    static URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            var external = url.toExternalForm();
            var ssp = external.substring(url.getProtocol().length() + 1);
            if (url.getRef() != null) {
                ssp = ssp.substring(0, ssp.length() - url.getRef().length() - 1);
            }
            try {
                return new URI(url.getProtocol(), ssp, url.getRef());
            } catch (URISyntaxException again) {
                return null;
            }
        }
    }
}
