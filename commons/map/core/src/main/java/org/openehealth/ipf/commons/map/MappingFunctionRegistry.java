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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Named functions that an {@link Unmatched.Computed} fallback can refer to.
 * <p>
 * A mapping file names a function; the function itself is written, and unit-tested, in Java.
 * That is the whole of what the Groovy DSL's arbitrary {@code (ELSE)} closures are needed for
 * in practice &mdash; across all mapping files shipped with IPF exactly one fallback computes
 * anything.
 * <p>
 * Functions get here either explicitly, through {@link Mappings.Builder#function}, or from a
 * {@link MappingFunctionProvider} that a module ships alongside a mapping file of its own.
 *
 * @since 6.0
 */
public class MappingFunctionRegistry {

    private final Map<String, Function<String, String>> functions = new ConcurrentHashMap<>();

    /**
     * Registers a function under the given name, replacing any function registered before
     * under that name.
     *
     * @param name     function name, as referred to by {@link Unmatched.Computed#ref()}
     * @param function the function; it must tolerate a {@code null} argument
     * @return this registry
     */
    public MappingFunctionRegistry register(String name, Function<String, String> function) {
        functions.put(
                Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("Function name required")),
                Optional.ofNullable(function).orElseThrow(() -> new IllegalArgumentException("Function required")));
        return this;
    }

    public Optional<Function<String, String>> lookup(String name) {
        return name == null ? Optional.empty() : Optional.ofNullable(functions.get(name));
    }

    public boolean contains(String name) {
        return name != null && functions.containsKey(name);
    }

    public void clear() {
        functions.clear();
    }
}
