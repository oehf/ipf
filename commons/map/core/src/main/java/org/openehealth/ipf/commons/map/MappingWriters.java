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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Discovery of {@link MappingWriter} implementations through {@link ServiceLoader}.
 *
 * @since 6.0
 */
public final class MappingWriters {

    private MappingWriters() {
    }

    /**
     * @return all writers visible to the current thread's context class loader, falling back to
     * the class loader of this class
     */
    public static List<MappingWriter> discover() {
        var writers = new ArrayList<MappingWriter>();
        collect(writers, Thread.currentThread().getContextClassLoader());
        if (writers.isEmpty()) {
            collect(writers, MappingWriters.class.getClassLoader());
        }
        return writers;
    }

    /**
     * @param extension a file extension including the leading dot, compared case-insensitively
     * @return the first discovered writer producing that extension
     */
    public static Optional<MappingWriter> forExtension(String extension) {
        return extension == null ? Optional.empty() : discover().stream()
                .filter(writer -> writer.extension().equalsIgnoreCase(extension))
                .findFirst();
    }

    /**
     * @param format a {@link MappingWriter#format() format id}, compared case-insensitively
     * @return the writer producing that format, empty if none on the classpath does
     * @throws IllegalStateException if several writers claim the same format id
     */
    public static Optional<MappingWriter> forFormat(String format) {
        if (format == null || format.isBlank()) {
            return Optional.empty();
        }
        var wanted = format.trim();
        var matches = discover().stream()
                .filter(writer -> wanted.equalsIgnoreCase(writer.format()))
                .toList();
        if (matches.size() > 1) {
            throw new IllegalStateException("Several mapping writers claim the format '" + wanted
                    + "': " + matches.stream().map(writer -> writer.getClass().getName()).toList()
                    + ". A format id selects one writer, so two of them cannot share it");
        }
        return matches.stream().findFirst();
    }

    /**
     * Resolves a target format named either way, so that a caller - a command line above all -
     * can say {@code xml} or {@code .mapping.xml} and mean the same writer.
     *
     * @param target a {@link MappingWriter#format() format id} or a file extension
     * @return the writer producing it, empty if none on the classpath does
     */
    public static Optional<MappingWriter> forTarget(String target) {
        var byFormat = forFormat(target);
        return byFormat.isPresent() ? byFormat : forExtension(target);
    }

    /**
     * @return the format ids that can currently be written, for diagnostics
     */
    public static Set<String> formats() {
        var formats = new LinkedHashSet<String>();
        discover().forEach(writer -> formats.add(writer.format()));
        return formats;
    }

    /**
     * @return the extensions that can currently be written, for diagnostics
     */
    public static Set<String> extensions() {
        var extensions = new LinkedHashSet<String>();
        discover().forEach(writer -> extensions.add(writer.extension()));
        return extensions;
    }

    private static void collect(List<MappingWriter> writers, ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        try {
            ServiceLoader.load(MappingWriter.class, classLoader).forEach(writers::add);
        } catch (ServiceConfigurationError e) {
            throw new IllegalStateException("Could not discover mapping writers", e);
        }
    }

    /**
     * Rejects entries without a key or a value, which the model tolerates - a Groovy script may
     * declare {@code (null) : 'x'} - but no file format can state. Writers call this before
     * writing anything, so that a mapping file is never produced that its own loader rejects.
     *
     * @param mappings the mappings to be written
     * @throws IllegalArgumentException naming the first mapping with such an entry
     */
    public static void requireKeysAndValues(List<? extends Mapping> mappings) {
        for (var mapping : mappings) {
            for (var entry : mapping.entries()) {
                if (entry.key() == null || entry.value() == null) {
                    throw new IllegalArgumentException("Mapping '" + mapping.name() + "' has an entry without a "
                            + (entry.key() == null ? "key" : "value") + ", which no mapping format can write");
                }
            }
        }
    }
}
