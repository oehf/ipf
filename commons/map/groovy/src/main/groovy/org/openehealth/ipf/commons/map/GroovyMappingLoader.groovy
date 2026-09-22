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
package org.openehealth.ipf.commons.map

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.charset.StandardCharsets
import java.util.function.Function

/**
 * {@link MappingLoader} for the legacy Groovy mapping DSL, i.e. for {@code .map} files that are
 * evaluated as a script rather than parsed. See {@link MappingsBuilder} for the syntax.
 * <p>
 * The DSL's open-ended {@code (ELSE)} clause is folded onto the closed {@link Unmatched}
 * vocabulary: a constant becomes {@link Unmatched.Fixed}, and a closure is registered with the
 * {@link MappingFunctionRegistry} under {@code <mappingName>#unmatched} and becomes
 * {@link Unmatched.Computed}. The {@code ({'X'}) : (ELSE)} idiom &mdash; a closure used as a map
 * key to declare the fallback of the reverse direction &mdash; becomes
 * {@link Mapping#reverseUnmatched()}.
 * <p>
 * The DSL cannot state an {@link Equivalence}, and it let a later file replace a mapping of the
 * same name silently. Both are declared here rather than left to the container, so that mappings
 * read from a script satisfy the model's constraints like any other: every mapping is marked
 * {@link Mapping#override()}, and where several entries share a value all but the last are marked
 * {@link Equivalence#NARROWER}, which is the inverse the Groovy mapping service happened to pick.
 * <p>
 * Because the file is a script rather than data, nothing about it is checked before it runs. It
 * is kept for backwards compatibility and is scheduled for removal in IPF 7.0.
 *
 * @since 6.0
 * @deprecated the Groovy mapping DSL is deprecated as of IPF 6.0 and will be removed in 7.0.
 * Convert mapping files to a declarative format with {@link MappingConverter}; the XML format is
 * the recommended default.
 */
@Deprecated(since = '6.0', forRemoval = true)
class GroovyMappingLoader implements MappingLoader {

    static final String EXTENSION = '.map'

    /**
     * Format id under which this loader can be selected explicitly.
     */
    static final String FORMAT = 'groovy'

    private static final Logger log = LoggerFactory.getLogger(GroovyMappingLoader)

    private static final String KEYSYSTEM = '_%KEYSYSTEM%_'
    private static final String VALUESYSTEM = '_%VALUESYSTEM%_'
    private static final String ELSE = '_%ELSE%_'

    @Override
    String format() {
        FORMAT
    }

    @Override
    boolean supports(URI source) {
        MappingLoader.hasExtension(source, EXTENSION)
    }

    @Override
    List<Mapping> load(InputStream input, URI source, MappingFunctionRegistry functions) {
        def binding = new Binding()
        def shell = new GroovyShell(binding)
        try {
            shell.evaluate(new InputStreamReader(input, StandardCharsets.UTF_8), scriptName(source))
        } catch (Exception e) {
            throw new MappingException(source, 'Could not evaluate mapping script', e)
        }
        def declaration = binding.hasVariable('mappings') ? binding.getVariable('mappings') : null
        if (!(declaration instanceof Closure)) {
            throw new MappingException(source, "Mapping script does not declare a 'mappings' closure")
        }
        Map<Object, Map> declared
        try {
            declared = new MappingsBuilder().mappings((Closure) declaration)
        } catch (Exception e) {
            throw new MappingException(source, 'Could not build mappings from script', e)
        }
        declared.collect { name, entries ->
            toMapping(String.valueOf(name), entries ?: [:], functions)
        }
    }

    private static Mapping toMapping(String name, Map entries, MappingFunctionRegistry functions) {
        def builder = Mapping.builder(name)
        def unmatched = Unmatched.ABSENT
        def reverseUnmatched = Unmatched.ABSENT
        def declared = []

        for (def entry : entries.entrySet()) {
            def key = entry.key
            def value = entry.value
            if (KEYSYSTEM == key) {
                builder.keySystem(asString(value))
            } else if (VALUESYSTEM == key) {
                builder.valueSystem(asString(value))
            } else if (ELSE == key) {
                unmatched = toUnmatched(value, functions, "${name}#unmatched")
            } else if (ELSE == value) {
                // the ({'X'}) : (ELSE) idiom: the key declares the reverse direction's fallback
                reverseUnmatched = toUnmatched(key, functions, "${name}#reverseUnmatched")
            } else {
                declared << new Entry(asString(key), asString(value))
            }
        }

        resolveReverseCollisions(name, declared).each { builder.entry(it) }
        builder.unmatched(unmatched)
                .reverseUnmatched(reverseUnmatched)
                .reversible(true)
                // the DSL has no way to say otherwise, and last-one-wins is what it always did
                .override(true)
                .build()
    }

    /**
     * The DSL cannot state which of several keys sharing a value is that value's inverse, and the
     * Groovy mapping service resolved it by overwriting the reverse index as it went, so the last
     * declaration won. That is stated here as an {@link Equivalence}, which both preserves the
     * behavior and lets the mapping satisfy the model's constraint that a value has one inverse.
     */
    private static List<Entry> resolveReverseCollisions(String name, List<Entry> declared) {
        def lastIndexByValue = [:]
        declared.eachWithIndex { entry, index -> lastIndexByValue[entry.value()] = index }
        declared.withIndex().collect { entry, index ->
            if (lastIndexByValue[entry.value()] == index) {
                return entry
            }
            log.debug("Mapping '{}': key '{}' shares the value '{}' with a later entry, so it is" +
                    " marked narrower and '{}' stays the inverse", name, entry.key(), entry.value(),
                    declared[lastIndexByValue[entry.value()] as int].key())
            new Entry(entry.key(), entry.value(), Equivalence.NARROWER)
        }
    }

    private static Unmatched toUnmatched(Object declaration, MappingFunctionRegistry functions, String ref) {
        if (declaration instanceof Closure) {
            functions.register(ref, asFunction((Closure) declaration))
            return Unmatched.computed(ref)
        }
        declaration == null ? Unmatched.ABSENT : Unmatched.fixed(asString(declaration))
    }

    private static Function<String, String> asFunction(Closure closure) {
        return { String key ->
            def result = closure.maximumNumberOfParameters == 0 ? closure.call() : closure.call(key)
            asString(result)
        } as Function<String, String>
    }

    private static String asString(Object value) {
        value == null || value instanceof String ? (String) value : String.valueOf(value)
    }

    private static String scriptName(URI source) {
        source == null ? 'mappings.map' : source.toString()
    }
}
