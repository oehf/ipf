/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.map.extend

import org.openehealth.ipf.commons.map.Mappings

/**
 * Backs the dynamic {@code mapXxx()} / {@code mapReverseXxx()} form of the mapping DSL, where the
 * mapping name is part of the method name, and holds the composite-value convention that the DSL
 * has and the mapping model does not: a {@link Collection} used as a key is joined with
 * {@value #SEPARATOR} before lookup, and a value containing it comes back as a {@link List}.
 *
 * @author Martin Krasser
 */
class MappingExtensionHelper {

    /**
     * Separates the components of a composite key or value. A DSL affordance only - to
     * {@link Mappings} a composite value is one string.
     */
    static final String SEPARATOR = '~'

    static def methodMissingLogic = { Mappings mappings, def normalizer, String name, args ->
        simpleMethodMissingLogic(mappings, normalizer(delegate), name, args)
    }

    static def simpleMethodMissingLogic = { Mappings mappings, Object src, String name, args ->
        if (name.startsWith('mapReverse')) {
            def mapping = name.substring('mapReverse'.length()).uncapitalize()
            return splitValue(args
                    ? mappings.mapReverse(mapping, joinKey(src), joinKey(args[0]))
                    : mappings.mapReverse(mapping, joinKey(src)).orElse(null))
        }
        if (name.startsWith('map')) {
            def mapping = name.substring('map'.length()).uncapitalize()
            return splitValue(args
                    ? mappings.map(mapping, joinKey(src), joinKey(args[0]))
                    : mappings.map(mapping, joinKey(src)).orElse(null))
        }
        throw new RuntimeException(new MissingMethodException(name, delegate.class, args))
    }

    /**
     * @param x a key, a value, or a collection of their components
     * @return the components joined with {@link #SEPARATOR}, or the argument as a string
     */
    static String joinKey(Object x) {
        x instanceof Collection ? x.collect { String.valueOf(it) }.join(SEPARATOR) : (String) x?.toString()
    }

    /**
     * @param x a mapped value
     * @return a {@link List} of its components if it is composite, the value itself otherwise
     */
    static Object splitValue(Object x) {
        if (!(x instanceof String)) {
            return x
        }
        def components = ((String) x).split(SEPARATOR).toList()
        components.size() == 1 ? components[0] : components
    }

}
