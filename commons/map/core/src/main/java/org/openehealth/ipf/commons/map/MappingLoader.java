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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

/**
 * Turns a mapping source document into {@link Mapping} records.
 * <p>
 * Implementations are discovered with {@link java.util.ServiceLoader}, so a single
 * {@link Mappings} instance can hold mappings written in different formats. The format is a
 * decision per mapping file, and it is reached in either of two ways:
 * <ul>
 *     <li>by <b>file extension</b>, through {@link #supports(URI)}. This is the default and
 *     needs no configuration - a {@code *.mapping.xml} file finds its loader on its own.</li>
 *     <li>by <b>{@link #format() format id}</b>, when the caller names one. Extensions cannot
 *     always decide: a mapping fetched from {@code https://tx.example.org/ConceptMap/gender}
 *     has no extension at all, a file may be called {@code gender.xml} for reasons outside
 *     IPF's control, and two loaders may be able to read one and the same extension. Naming
 *     the format bypasses {@link #supports(URI)} entirely.</li>
 * </ul>
 * Register an implementation by listing it in
 * {@code META-INF/services/org.openehealth.ipf.commons.map.MappingLoader}.
 *
 * @since 6.0
 */
public interface MappingLoader {

    /**
     * Short, stable identifier of the format this loader reads, e.g. {@code xml}, {@code yaml}
     * or {@code conceptmap-r4-json}. It is what a caller names to select this loader regardless of
     * the source's file extension, and it is compared case-insensitively.
     * <p>
     * Two loaders on one classpath must not share a format id, since the id is what picks one of
     * them; qualify it the way {@code conceptmap-r4-json} does when a format has versions or
     * encodings that a second loader might read.
     *
     * @return the format id, never {@code null} or blank
     */
    String format();

    /**
     * Whether this loader claims a source that nobody named a format for. Implementations decide
     * by file extension: a source it cannot recognize from its name alone is not its own, even
     * if it could read the content, because that is what keeps dispatch predictable.
     *
     * @param source location of the mapping source, used for its file extension
     * @return whether this loader can read the given source
     */
    boolean supports(URI source);

    /**
     * Reads all mappings contained in one source document.
     *
     * @param in        the source content; the caller closes the stream
     * @param source    location of the source, for diagnostics
     * @param functions registry a loader may add functions to, and against which it should
     *                  validate any {@link Unmatched.Computed} reference it produces
     * @return the mappings read, in declaration order
     * @throws IOException      if the source cannot be read
     * @throws MappingException if the source is not valid for this format
     */
    List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions) throws IOException;

    /**
     * Reads all mappings contained in one source document, reporting what the model cannot hold.
     * <p>
     * A format that can state more than the model - the legacy Groovy DSL binds arbitrary objects
     * where the model holds strings - reports each such loss to {@code warnings}, one message per
     * mapping, starting with the mapping's name. {@link MappingConverter} collects them. The
     * default implementation has nothing to report.
     *
     * @param in        the source content; the caller closes the stream
     * @param source    location of the source, for diagnostics
     * @param functions registry a loader may add functions to
     * @param warnings  receives a human-readable message for every loss the loader has to make
     * @return the mappings read, in declaration order
     * @throws IOException      if the source cannot be read
     * @throws MappingException if the source is not valid for this format
     */
    default List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions,
                               Consumer<String> warnings) throws IOException {
        return load(in, source, functions);
    }

    /**
     * @param source a source location
     * @return the path portion of the location, also for opaque URIs such as {@code jar:} URLs
     */
    static String path(URI source) {
        if (source == null) {
            return "";
        }
        var path = source.isOpaque() ? source.getSchemeSpecificPart() : source.getPath();
        return path == null ? source.toString() : path;
    }

    /**
     * @param source    a source location
     * @param extension file extension including the leading dot, compared case-insensitively
     * @return whether the source location ends with the given extension
     */
    static boolean hasExtension(URI source, String extension) {
        return path(source).toLowerCase().endsWith(extension.toLowerCase());
    }
}
