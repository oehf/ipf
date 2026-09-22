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

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Discovery of {@link MappingLoader} implementations through {@link ServiceLoader}.
 *
 * @since 6.0
 */
public final class MappingLoaders {

    private MappingLoaders() {
    }

    /**
     * @return all loaders visible to the current thread's context class loader, falling back
     * to the class loader of this class
     */
    public static List<MappingLoader> discover() {
        var loaders = new ArrayList<MappingLoader>();
        collect(loaders, Thread.currentThread().getContextClassLoader());
        if (loaders.isEmpty()) {
            collect(loaders, MappingLoaders.class.getClassLoader());
        }
        return loaders;
    }

    /**
     * @param source a mapping source location
     * @return the first discovered loader that supports the given source
     */
    public static Optional<MappingLoader> forSource(URI source) {
        return discover().stream()
                .filter(loader -> loader.supports(source))
                .findFirst();
    }

    /**
     * @param format a {@link MappingLoader#format() format id}, compared case-insensitively
     * @return the loader reading that format, empty if none on the classpath does
     * @throws IllegalStateException if several loaders claim the same format id, since picking
     *                               one of them would be arbitrary
     */
    public static Optional<MappingLoader> forFormat(String format) {
        if (format == null || format.isBlank()) {
            return Optional.empty();
        }
        var wanted = format.trim();
        var matches = discover().stream()
                .filter(loader -> wanted.equalsIgnoreCase(loader.format()))
                .toList();
        if (matches.size() > 1) {
            throw new IllegalStateException("Several mapping loaders claim the format '" + wanted
                    + "': " + matches.stream().map(loader -> loader.getClass().getName()).toList()
                    + ". A format id selects one loader, so two of them cannot share it");
        }
        return matches.stream().findFirst();
    }

    /**
     * @return the format ids that can currently be read, for diagnostics
     */
    public static Set<String> formats() {
        var formats = new LinkedHashSet<String>();
        discover().forEach(loader -> formats.add(loader.format()));
        return formats;
    }

    /**
     * Picks the loader for a source: the one named by {@code format} if there is one, and
     * otherwise the one claiming the source's file extension. This is the single place where
     * that precedence is decided - an explicitly named format always wins, which is what lets a
     * source whose name says nothing about its format be read at all.
     *
     * @param source a mapping source location
     * @param format a {@link MappingLoader#format() format id}, or {@code null} to dispatch by
     *               file extension
     * @return the loader to read the source with
     * @throws MappingException if no loader on the classpath can read it
     */
    public static MappingLoader resolve(URI source, String format) {
        if (format != null && !format.isBlank()) {
            return forFormat(format).orElseThrow(() -> new MappingException(source,
                    "No mapping loader implements the format '" + format.trim() + "'. Add the module"
                            + " for it to the classpath; formats that can be read here: " + formats()));
        }
        return forSource(source).orElseThrow(() -> new MappingException(source,
                "No mapping loader supports this source. Add the module for its format to the"
                        + " classpath: ipf-commons-map-xml for *.mapping.xml, ipf-commons-map-yaml for"
                        + " *.mapping.yaml, ipf-commons-map-groovy for the legacy *.map script format."
                        + " If the source name does not identify its format, name the format when"
                        + " loading it; formats that can be read here: " + formats()));
    }

    private static void collect(List<MappingLoader> loaders, ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        try {
            ServiceLoader.load(MappingLoader.class, classLoader).forEach(loaders::add);
        } catch (ServiceConfigurationError e) {
            throw new IllegalStateException("Could not discover mapping loaders", e);
        }
    }
}
