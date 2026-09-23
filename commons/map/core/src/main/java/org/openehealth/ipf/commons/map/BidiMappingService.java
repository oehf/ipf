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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * {@link MappingService} implementation backed by {@link Mappings}, which is where the actual
 * model and the loading of mapping sources live as of IPF 6.0.
 * <p>
 * This class exists to keep the untyped, {@code null}-returning {@link MappingService} contract
 * working for existing applications. It answers exactly what {@link Mappings} answers, with
 * {@code null} where that returns an empty {@link java.util.Optional}.
 * <p>
 * Two conventions of the Groovy implementation it replaces are gone:
 * <ul>
 *     <li>an empty value is a value: an entry mapping to {@code ""} yields {@code ""}, rather than
 *     the mapping's fallback, and a default passed in applies only where there is no value at
 *     all;</li>
 *     <li>there is no composite convention: a value containing {@code ~} is returned as the string
 *     it is rather than as a {@link List}, and a {@link Collection} passed as a key or value is
 *     rejected rather than joined. A mapping whose keys or values consist of several parts must
 *     either be split into separate mappings, or the calling code joins and splits the parts
 *     itself.</li>
 * </ul>
 * <p>
 * New code should use {@link Mappings} directly &mdash; it is {@link String}-typed and returns
 * {@link java.util.Optional}. {@link #getMappings()} hands out the instance this service delegates
 * to.
 * <p>
 * Mapping sources are dispatched to a {@link MappingLoader} by file extension, so the format of
 * a mapping file is a decision per file. Reading the legacy {@code .map} script format needs
 * {@code ipf-commons-map-groovy} on the classpath.
 * <p>
 * As of version 3.1, the add/setMappingScript(Resource) methods have been out-factored
 * into the class SpringBidiMappingService located in the ipf-commons-spring module.
 *
 * @see Mappings
 * @deprecated as of 6.0, use {@link Mappings} - {@link DefaultMappings} is the implementation to
 * build on, and {@code SpringMappings} in {@code ipf-commons-spring} the bean to wire. This class
 * remains as an adapter for applications still holding a {@link MappingService}, and is scheduled
 * for removal in IPF 7.0.
 */
@Deprecated(since = "6.0", forRemoval = true)
@SuppressWarnings("removal")
public class BidiMappingService implements MappingService {

    protected static final String KEYSYSTEM = "_%KEYSYSTEM%_";
    protected static final String VALUESYSTEM = "_%VALUESYSTEM%_";
    protected static final String ELSE = "_%ELSE%_";

    private final DefaultMappings mappings;
    private final List<URL> scripts = new ArrayList<>();
    private final Map<String, Function<String, String>> functions = new LinkedHashMap<>();

    @Setter
    private boolean ignoreResourceNotFound = false;

    public BidiMappingService() {
        this(new DefaultMappings());
    }

    /**
     * Wraps mappings somebody else owns, so that the same content is reachable through both this
     * service and the typed {@link Mappings} API. Whoever created the instance configures it; this
     * constructor changes nothing about it.
     *
     * @param mappings the mappings to delegate to
     */
    public BidiMappingService(DefaultMappings mappings) {
        this.mappings = mappings;
    }

    /**
     * @return the mappings this service delegates to, for call sites moving to the typed API
     */
    public Mappings getMappings() {
        return mappings;
    }

    // ------------------------------------------------------------------ configuration

    public synchronized void setMappingScript(URL script) {
        scripts.add(script);
        load(script);
    }

    public synchronized void setMappingScripts(URL[] scripts) {
        for (var script : scripts) {
            setMappingScript(script);
        }
    }

    public synchronized void clearMappings() {
        scripts.clear();
        mappings.clear();
        functions.forEach(mappings.functions()::register);
    }

    /**
     * Registers the functions that a mapping's computed fallback refers to by name, i.e. what
     * {@code <unmatched mode="function" ref="..."/>} resolves against. A function receives the
     * key that had no entry and returns the value for it, so a mapping can derive its answer from
     * its input rather than list it:
     * <pre>
     * mappingService.setMappingFunctions(Map.of(
     *         "first4", key -&gt; key == null || key.length() &lt;= 4 ? key : key.substring(0, 4)));
     * </pre>
     * A mapping source naming a function that is not registered is rejected as it is read, so
     * register them first. As a Spring bean property that happens before any configurer adds
     * mapping resources; a module that ships both a mapping file and the function it names should
     * contribute a {@link MappingFunctionProvider} instead.
     *
     * @param functions functions by name, replacing any registered under the same name before
     */
    public synchronized void setMappingFunctions(Map<String, Function<String, String>> functions) {
        functions.forEach(this::registerMappingFunction);
    }

    /**
     * @see #setMappingFunctions(Map)
     */
    public synchronized void registerMappingFunction(String name, Function<String, String> function) {
        functions.put(name, function);
        mappings.functions().register(name, function);
    }

    public List<URL> getScripts() {
        return Collections.unmodifiableList(scripts);
    }

    public boolean getIgnoreResourceNotFound() {
        return ignoreResourceNotFound;
    }

    private void load(URL script) {
        mappings.load(script);
    }

    // ------------------------------------------------------------------ MappingService

    @Override
    public Object get(Object mappingKey, Object key) {
        return forward(mappingKey, key);
    }

    @Override
    public Object get(Object mappingKey, Object key, Object defaultValue) {
        var value = forward(mappingKey, key);
        return value == null ? defaultValue : value;
    }

    @Override
    public Object getKey(Object mappingKey, Object value) {
        return reverse(mappingKey, value);
    }

    @Override
    public Object getKey(Object mappingKey, Object value, Object defaultKey) {
        var key = reverse(mappingKey, value);
        return key == null ? defaultKey : key;
    }

    @Override
    public Object getKeySystem(Object mappingKey) {
        return mappings.keySystem(name(mappingKey)).orElse(null);
    }

    @Override
    public Object getValueSystem(Object mappingKey) {
        return mappings.valueSystem(name(mappingKey)).orElse(null);
    }

    @Override
    public Set<?> mappingKeys() {
        return mappings.mappingNames();
    }

    @Override
    public Set<?> keys(Object mappingKey) {
        return mappings.keys(name(mappingKey));
    }

    @Override
    public Collection<?> values(Object mappingKey) {
        return mappings.values(name(mappingKey));
    }

    // ------------------------------------------------------------------ internals

    private String forward(Object mappingKey, Object key) {
        return mappings.map(name(mappingKey), toString(key)).orElse(null);
    }

    private String reverse(Object mappingKey, Object value) {
        return mappings.mapReverse(name(mappingKey), toString(value)).orElse(null);
    }

    private static String name(Object mappingKey) {
        return mappingKey == null ? null : String.valueOf(mappingKey);
    }

    /**
     * @param x a key or value
     * @return the argument as a string
     * @throws IllegalArgumentException for a {@link Collection}, which IPF 5.x joined with {@code ~}
     */
    private static String toString(Object x) {
        if (x instanceof Collection<?>) {
            throw new IllegalArgumentException("Composite keys are no longer supported; join them in"
                    + " the calling code or split the mapping into separate ones: " + x);
        }
        return x == null ? null : String.valueOf(x);
    }
}
