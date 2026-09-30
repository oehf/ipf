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

import java.util.Optional;
import java.util.function.Function;

/**
 * Read access to the functions that {@link Unmatched.Computed} fallbacks name. What
 * {@link Mappings#functions()} hands out: a built {@link Mappings} cannot be changed through it,
 * since a function replaced or removed after the mappings naming it were checked would change or
 * break their answers. Functions are registered through {@link Mappings.Builder#function} or
 * {@link DefaultMappings#registerFunction}.
 *
 * @since 6.0
 */
public interface MappingFunctions {

    /**
     * @param name function name, as referred to by {@link Unmatched.Computed#ref()}
     * @return the function registered under that name
     */
    Optional<Function<String, String>> lookup(String name);

    /**
     * @param name function name
     * @return whether a function is registered under that name
     */
    boolean contains(String name);
}
