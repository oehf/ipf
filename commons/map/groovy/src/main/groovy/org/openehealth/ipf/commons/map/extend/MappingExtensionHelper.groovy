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
 * mapping name is part of the method name.
 * <p>
 * Keys and values are single strings, exactly as the mapping declares them. There is no composite
 * convention: a mapping whose keys or values consist of several parts must either be split into
 * separate mappings, or the calling code joins and splits the parts itself.
 *
 * @author Martin Krasser
 */
class MappingExtensionHelper {

    static def methodMissingLogic = { Mappings mappings, def normalizer, String name, args ->
        simpleMethodMissingLogic(mappings, normalizer(delegate), name, args)
    }

    static def simpleMethodMissingLogic = { Mappings mappings, Object src, String name, args ->
        def key = src?.toString()
        if (name.startsWith('mapReverse')) {
            def mapping = name.substring('mapReverse'.length()).uncapitalize()
            return args
                    ? mappings.mapReverse(mapping, key, args[0]?.toString())
                    : mappings.mapReverse(mapping, key).orElse(null)
        }
        if (name.startsWith('map')) {
            def mapping = name.substring('map'.length()).uncapitalize()
            return args
                    ? mappings.map(mapping, key, args[0]?.toString())
                    : mappings.map(mapping, key).orElse(null)
        }
        throw new RuntimeException(new MissingMethodException(name, delegate.class, args))
    }

}
