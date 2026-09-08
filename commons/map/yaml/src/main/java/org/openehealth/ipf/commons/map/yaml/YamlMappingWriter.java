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
package org.openehealth.ipf.commons.map.yaml;

import org.openehealth.ipf.commons.map.Equivalence;
import org.openehealth.ipf.commons.map.Mapping;
import org.openehealth.ipf.commons.map.MappingWriter;
import org.openehealth.ipf.commons.map.Unmatched;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes {@link Mapping}s as a {@code *.mapping.yaml} document.
 * <p>
 * The output opens with the {@code yaml-language-server} comment that points editors at the JSON
 * Schema, keeps numeric-looking values quoted so they stay strings, and leaves out anything that
 * only restates a default.
 *
 * @since 6.0
 */
public class YamlMappingWriter implements MappingWriter {

    private static final YAMLMapper MAPPER = YAMLMapper.builder()
            .disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
            .enable(YAMLWriteFeature.MINIMIZE_QUOTES)
            // without this, a code like "1004" would come back as the number 1004
            .enable(YAMLWriteFeature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
            .build();

    private static final String HEADER =
            "# yaml-language-server: $schema=" + MappingYaml.SCHEMA_LOCATION + "\n";

    @Override
    public String format() {
        return YamlMappingLoader.FORMAT;
    }

    @Override
    public String extension() {
        return YamlMappingLoader.EXTENSION;
    }

    @Override
    public void write(List<Mapping> mappings, Writer out) {
        var document = new LinkedHashMap<String, Object>();
        var declared = new LinkedHashMap<String, Object>();
        mappings.forEach(mapping -> declared.put(mapping.name(), toYaml(mapping)));
        document.put("mappings", declared);
        try {
            out.write(HEADER);
            MAPPER.writeValue(out, document);
            out.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write mappings as YAML", e);
        }
    }

    public String toYaml(List<Mapping> mappings) {
        var out = new java.io.StringWriter();
        write(mappings, out);
        return out.toString();
    }

    private static Map<String, Object> toYaml(Mapping mapping) {
        var yaml = new LinkedHashMap<String, Object>();
        if (mapping.keySystem() != null) {
            yaml.put("keySystem", mapping.keySystem());
        }
        if (mapping.valueSystem() != null) {
            yaml.put("valueSystem", mapping.valueSystem());
        }
        if (!mapping.reversible()) {
            yaml.put("reversible", false);
        }
        if (mapping.override()) {
            yaml.put("override", true);
        }
        if (!mapping.entries().isEmpty()) {
            var entries = new LinkedHashMap<String, Object>();
            mapping.entries().forEach(entry -> entries.put(entry.key(), toYaml(entry)));
            yaml.put("entries", entries);
        }
        var unmatched = toYaml(mapping.unmatched());
        if (unmatched != null) {
            yaml.put("unmatched", unmatched);
        }
        var reverseUnmatched = toYaml(mapping.reverseUnmatched());
        if (reverseUnmatched != null) {
            yaml.put("reverse", Map.of("unmatched", reverseUnmatched));
        }
        return yaml;
    }

    /**
     * @return the scalar shorthand for an entry that needs nothing but its value, the object form
     * otherwise
     */
    private static Object toYaml(org.openehealth.ipf.commons.map.Entry entry) {
        if (entry.equivalence() == Equivalence.EQUAL
                && entry.keyDisplay() == null && entry.valueDisplay() == null) {
            return entry.value();
        }
        var object = new LinkedHashMap<String, Object>();
        object.put("value", entry.value());
        if (entry.equivalence() != Equivalence.EQUAL) {
            object.put("equivalence", entry.equivalence().name().toLowerCase());
        }
        if (entry.keyDisplay() != null) {
            object.put("keyDisplay", entry.keyDisplay());
        }
        if (entry.valueDisplay() != null) {
            object.put("valueDisplay", entry.valueDisplay());
        }
        return object;
    }

    /**
     * @return the {@code unmatched} declaration, or {@code null} for {@link Unmatched.Absent},
     * which is what an omitted property means
     */
    private static Object toYaml(Unmatched unmatched) {
        if (unmatched instanceof Unmatched.Absent) {
            return null;
        }
        if (unmatched instanceof Unmatched.Identity) {
            return "identity";
        }
        if (unmatched instanceof Unmatched.Fail) {
            return "fail";
        }
        if (unmatched instanceof Unmatched.Fixed fixed) {
            return Map.of("fixed", fixed.value());
        }
        if (unmatched instanceof Unmatched.Computed computed) {
            return Map.of("function", computed.ref());
        }
        if (unmatched instanceof Unmatched.Delegate delegate) {
            return Map.of("delegate", delegate.mapping());
        }
        throw new IllegalStateException("Unsupported unmatched behavior " + unmatched);
    }
}
