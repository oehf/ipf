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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A deliberately trivial {@code .testmap} format, so that this module can test loading, SPI
 * dispatch and the {@link BidiMappingService} adapter without depending on a real mapping format
 * - every one of those lives in a module downstream of this one.
 * <pre>
 * &#64;name        = encounterType
 * &#64;keySystem   = 2.16.840.1.113883.12.4
 * &#64;unmatched   = fixed:UNK          # absent | provided | fixed:X | function:ref | delegate:name | fail
 * &#64;reverse     = provided
 * &#64;reversible  = false
 * &#64;override    = true
 * E = EMER
 * A = other | narrower                 # the optional third field is the equivalence
 * ---                                  # separates mappings within one file
 * </pre>
 */
public class TestMappingLoader implements MappingLoader {

    public static final String EXTENSION = ".testmap";

    public static final String FORMAT = "testmap";

    @Override
    public String format() {
        return FORMAT;
    }

    @Override
    public boolean supports(URI source) {
        return MappingLoader.hasExtension(source, EXTENSION);
    }

    @Override
    public List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions) throws IOException {
        var mappings = new ArrayList<Mapping>();
        var builders = new ArrayList<Mapping.Builder>();
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            Mapping.Builder current = null;
            String line;
            while ((line = reader.readLine()) != null) {
                var content = line.indexOf('#') < 0 ? line : line.substring(0, line.indexOf('#'));
                content = content.trim();
                if (content.isEmpty()) {
                    continue;
                }
                if ("---".equals(content)) {
                    current = null;
                    continue;
                }
                var equals = content.indexOf('=');
                if (equals < 0) {
                    throw new MappingException(source, "Not a key/value line: " + line);
                }
                var key = content.substring(0, equals).trim();
                var value = content.substring(equals + 1).trim();
                if ("@name".equals(key)) {
                    current = Mapping.builder(value);
                    builders.add(current);
                    continue;
                }
                if (current == null) {
                    throw new MappingException(source, "@name must come first");
                }
                switch (key) {
                    case "@keySystem" -> current.keySystem(value);
                    case "@valueSystem" -> current.valueSystem(value);
                    case "@reversible" -> current.reversible(Boolean.parseBoolean(value));
                    case "@override" -> current.override(Boolean.parseBoolean(value));
                    case "@unmatched" -> current.unmatched(unmatched(value));
                    case "@reverse" -> current.reverseUnmatched(unmatched(value));
                    default -> {
                        var bar = value.lastIndexOf('|');
                        current.entry(bar < 0
                                ? new Entry(key, value)
                                : new Entry(key, value.substring(0, bar).trim(),
                                Equivalence.valueOf(value.substring(bar + 1).trim().toUpperCase())));
                    }
                }
            }
        }
        builders.forEach(builder -> mappings.add(builder.build()));
        return mappings;
    }

    private static Unmatched unmatched(String declaration) {
        var colon = declaration.indexOf(':');
        var mode = colon < 0 ? declaration : declaration.substring(0, colon);
        var argument = colon < 0 ? null : declaration.substring(colon + 1);
        return switch (mode) {
            case "absent" -> Unmatched.ABSENT;
            case "provided" -> Unmatched.IDENTITY;
            case "fail" -> Unmatched.FAIL;
            case "fixed" -> Unmatched.fixed(argument);
            case "function" -> Unmatched.computed(argument);
            case "delegate" -> Unmatched.delegate(argument);
            default -> throw new IllegalArgumentException("Unknown unmatched mode " + mode);
        };
    }
}
