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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
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
 * The DSL binds arbitrary objects, the model holds strings. A mapping whose keys, values or
 * fallback results are not strings - typically enum constants - is converted to their string
 * form and reported as a warning, because code that expects the objects back no longer gets them.
 * Such a typed mapping cannot be expressed in any mapping format and needs rewriting in Java.
 * <p>
 * The DSL used to have a composite convention: a key or value containing {@code ~} stood for a
 * {@link List} of its parts. That convention is gone, so such a key or value is one string, and a
 * {@link Collection} is held as its {@code toString()} form. Every composite mapping is reported as
 * a warning, because it must be split into separate mappings, or its callers join and split the
 * parts themselves.
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

    private static final String TYPED_CONSEQUENCE = 'Mappings and every mapping format hold strings,' +
            ' so a typed mapping cannot be expressed in them, and the deprecated MappingService now' +
            ' returns these strings instead of the objects the script declares - a caller casting the' +
            ' result fails with a ClassCastException. Rewrite the mapping in Java.'

    private static final String COMPOSITE_SEPARATOR = '~'

    private static final String COMPOSITE_CONSEQUENCE = 'IPF no longer interprets the composite separator' +
            " '${COMPOSITE_SEPARATOR}': such a key or value is one string rather than a List, and a" +
            ' Collection is held as its toString() form; split the mapping into one per component, or' +
            ' join and split the parts in the calling code.'

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
        load(input, source, functions, { String warning -> log.warn('{}: {}', scriptName(source), warning) })
    }

    @Override
    List<Mapping> load(InputStream input, URI source, MappingFunctionRegistry functions,
                       Consumer<String> warnings) {
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
            toMapping(String.valueOf(name), entries ?: [:], functions, warnings)
        }
    }

    private static Mapping toMapping(String name, Map entries, MappingFunctionRegistry functions,
                                     Consumer<String> warnings) {
        def builder = Mapping.builder(name)
        def unmatched = Unmatched.ABSENT
        def reverseUnmatched = Unmatched.ABSENT
        def declared = []
        Set<String> keyTypes = new LinkedHashSet<>()
        Set<String> valueTypes = new LinkedHashSet<>()
        Set<String> composite = new LinkedHashSet<>()

        for (def entry : entries.entrySet()) {
            def key = entry.key
            def value = entry.value
            if (KEYSYSTEM == key) {
                builder.keySystem(asString(value))
            } else if (VALUESYSTEM == key) {
                builder.valueSystem(asString(value))
            } else if (ELSE == key) {
                collectComposite('fallback', value, composite)
                unmatched = toUnmatched(value, functions, "${name}#unmatched", valueTypes,
                        { Object result -> warnings.accept(fallbackWarning(name, 'forward', result)) })
            } else if (ELSE == value) {
                // the ({'X'}) : (ELSE) idiom: the key declares the reverse direction's fallback
                collectComposite('reverse fallback', key, composite)
                reverseUnmatched = toUnmatched(key, functions, "${name}#reverseUnmatched", keyTypes,
                        { Object result -> warnings.accept(fallbackWarning(name, 'reverse', result)) })
            } else {
                collectType(key, keyTypes)
                collectType(value, valueTypes)
                collectComposite('key', key, composite)
                collectComposite('value', value, composite)
                declared << new Entry(asString(key), asString(value))
            }
        }
        if (keyTypes || valueTypes) {
            warnings.accept(typedMappingWarning(name, keyTypes, valueTypes))
        }
        if (composite) {
            warnings.accept(compositeMappingWarning(name, composite))
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

    /**
     * @param types            collects the type of a constant fallback that is not a string
     * @param reportedResults  told, once, the first result a fallback closure computes that the
     *                         model cannot hold as it is - typed or composite - which only shows
     *                         when the closure is called
     */
    private static Unmatched toUnmatched(Object declaration, MappingFunctionRegistry functions, String ref,
                                         Set<String> types, Consumer<Object> reportedResults) {
        if (declaration instanceof Closure) {
            functions.register(ref, asFunction((Closure) declaration, reportedResults))
            return Unmatched.computed(ref)
        }
        collectType(declaration, types)
        declaration == null ? Unmatched.ABSENT : Unmatched.fixed(asString(declaration))
    }

    private static Function<String, String> asFunction(Closure closure, Consumer<Object> reportedResults) {
        def reported = new AtomicBoolean()
        return { String key ->
            def result = closure.maximumNumberOfParameters == 0 ? closure.call() : closure.call(key)
            if ((typeOf(result) || isComposite(result)) && reported.compareAndSet(false, true)) {
                reportedResults.accept(result)
            }
            asString(result)
        } as Function<String, String>
    }

    private static void collectComposite(String role, Object value, Set<String> composite) {
        if (isComposite(value)) {
            composite << "${role} ${value.inspect()}".toString()
        }
    }

    /**
     * @return whether a value was a composite under the DSL's former {@code ~} convention: a
     * collection of parts, or a string joining them
     */
    private static boolean isComposite(Object value) {
        value instanceof Collection ||
                (value instanceof CharSequence && value.toString().contains(COMPOSITE_SEPARATOR))
    }

    private static void collectType(Object value, Set<String> types) {
        def type = typeOf(value)
        if (type) {
            types << type
        }
    }

    /**
     * @return the name of the type of a value the model cannot hold as it is, or {@code null} for
     * a string or a collection, which is reported as composite instead. An enum constant with a
     * body is an anonymous subclass, so its enum is named instead.
     */
    private static String typeOf(Object value) {
        if (value == null || value instanceof CharSequence || value instanceof Collection) {
            return null
        }
        value instanceof Enum ? ((Enum) value).declaringClass.name : value.class.name
    }

    private static String typedMappingWarning(String name, Set<String> keyTypes, Set<String> valueTypes) {
        def typed = []
        if (keyTypes) {
            typed << "keys of type ${keyTypes.join(', ')}"
        }
        if (valueTypes) {
            typed << "values of type ${valueTypes.join(', ')}"
        }
        "${name}: ${typed.join(' and ')} are held as their toString() form only. ${TYPED_CONSEQUENCE}"
    }

    private static String compositeMappingWarning(String name, Set<String> composite) {
        "${name}: ${composite.join(', ')} ${composite.size() == 1 ? 'is' : 'are'} composite." +
                " ${COMPOSITE_CONSEQUENCE}"
    }

    private static String fallbackWarning(String name, String direction, Object result) {
        def type = typeOf(result)
        type
                ? "${name}: the ${direction} fallback computes values of type ${type}, which are held as" +
                " their toString() form only. ${TYPED_CONSEQUENCE}"
                : "${name}: the ${direction} fallback computes composite values such as" +
                " ${result.inspect()}. ${COMPOSITE_CONSEQUENCE}"
    }

    private static String asString(Object value) {
        value == null || value instanceof String ? (String) value : String.valueOf(value)
    }

    private static String scriptName(URI source) {
        source == null ? 'mappings.map' : source.toString()
    }
}
