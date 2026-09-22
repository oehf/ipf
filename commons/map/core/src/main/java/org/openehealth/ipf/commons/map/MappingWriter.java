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

import java.io.Writer;
import java.util.List;

/**
 * Writes {@link Mapping} records as a mapping source document, the counterpart of
 * {@link MappingLoader}.
 * <p>
 * Implementations are discovered with {@link java.util.ServiceLoader} and selected either by
 * their {@link #format() format id} or by the file extension they produce, which is what lets
 * {@link MappingConverter} target whichever formats happen to be on the classpath. Register an
 * implementation by listing it in
 * {@code META-INF/services/org.openehealth.ipf.commons.map.MappingWriter}.
 * <p>
 * A writer is not required to round-trip everything a loader can read: a format that cannot
 * express part of the model rejects it rather than dropping it silently.
 *
 * @since 6.0
 */
public interface MappingWriter {

    /**
     * Short, stable identifier of the format this writer produces, matching the
     * {@link MappingLoader#format() format id} of the loader that reads it back, e.g.
     * {@code xml}, {@code yaml} or {@code conceptmap-r4-json}. Compared case-insensitively, and
     * unique among the writers on one classpath.
     *
     * @return the format id, never {@code null} or blank
     */
    String format();

    /**
     * @return the file extension of the format this writer produces, including the leading dot
     * and lower case, e.g. {@code .mapping.xml}
     */
    String extension();

    /**
     * @param mappings the mappings to write
     * @param out      the destination; the caller closes it
     */
    void write(List<Mapping> mappings, Writer out);
}
