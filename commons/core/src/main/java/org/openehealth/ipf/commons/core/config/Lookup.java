/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.core.config;

import java.util.Collection;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Lookup implementation of an interface type using {@link ServiceLoader}.
 */
public class Lookup {

    /**
     * Returns the first implementation of T
     *
     * @param clazz interface
     * @param <T>   interface type
     * @return first implementation of T, if any
     */
    public static <T> Optional<T> lookup(Class<T> clazz) {
        return load(clazz).findFirst();
    }

    /**
     * Returns all implementations of T
     *
     * @param clazz interface
     * @param <T>   interface type
     * @return implementations of T or an empty collection
     */
    public static <T> Collection<? extends T> lookupAll(Class<T> clazz) {
        return load(clazz).collect(Collectors.toList());
    }

    private static <T> Stream<T> load(Class<T> clazz) {
        return StreamSupport.stream(ServiceLoader.load(clazz).spliterator(), false);
    }

}
