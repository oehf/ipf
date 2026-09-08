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

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Contributes named functions that an {@link Unmatched.Computed} fallback can refer to.
 * <p>
 * A module that ships a mapping file whose fallback is computed also ships the function it names,
 * so that the file works wherever it is loaded from without the application having to know about
 * it. Providers are discovered with {@link ServiceLoader} and applied before any mapping source
 * is read; register one by listing it in
 * {@code META-INF/services/org.openehealth.ipf.commons.map.MappingFunctionProvider}.
 * <p>
 * Function names share one namespace per {@link Mappings} instance. Names contributed by a
 * provider should therefore be specific enough not to collide - qualify them if in doubt. A name
 * registered explicitly through {@link Mappings.Builder#function} wins over a provider's.
 *
 * @since 6.0
 */
public interface MappingFunctionProvider {

    /**
     * @param registry the registry to add functions to
     */
    void register(MappingFunctionRegistry registry);

    /**
     * Applies every discovered provider to the given registry.
     */
    static void registerAll(MappingFunctionRegistry registry) {
        var classLoader = Thread.currentThread().getContextClassLoader();
        try {
            ServiceLoader.load(MappingFunctionProvider.class,
                            classLoader != null ? classLoader : MappingFunctionProvider.class.getClassLoader())
                    .forEach(provider -> provider.register(registry));
        } catch (ServiceConfigurationError e) {
            throw new IllegalStateException("Could not discover mapping function providers", e);
        }
    }
}
