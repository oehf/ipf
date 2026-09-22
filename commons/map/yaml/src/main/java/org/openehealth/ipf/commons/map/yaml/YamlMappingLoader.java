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
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.MappingFunctionRegistry;
import org.openehealth.ipf.commons.map.MappingLoader;
import org.openehealth.ipf.commons.map.Unmatched;
import org.snakeyaml.engine.v2.api.LoadSettings;
import tools.jackson.core.JacksonException;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.dataformat.yaml.YAMLFactory;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@link MappingLoader} for {@code *.mapping.yaml} files: the same model as the XML format, about
 * 40% fewer keystrokes, and comments that survive - which matters for mapping files that record
 * the code system each code came from next to it.
 * <p>
 * Property names are bound strictly, so {@code keysystem} for {@code keySystem} is an error at
 * startup with a line and a column rather than a null found in production later. The JSON Schema
 * shipped at {@code META-INF/ipf/mapping-schema.json} covers the same rules for an editor; refer
 * to it from a mapping file with
 * <pre>
 * # yaml-language-server: $schema=http://openehealth.org/schema/ipf-commons-map.json
 * </pre>
 * and IntelliJ and VS Code validate while the file is being typed. It is not used at runtime,
 * which is why the loader repeats its constraints in code.
 * <p>
 * Values are strings. A YAML scalar that parses as a number or a boolean is rejected rather than
 * coerced: {@code AGN: 1004} is how an entire mapping came to return {@code Integer}s where every
 * other one returned {@code String}s, and {@code AGN: "1004"} is unambiguous.
 *
 * @since 6.0
 */
public class YamlMappingLoader implements MappingLoader {

    /**
     * Primary file extension this loader claims.
     */
    public static final String EXTENSION = ".mapping.yaml";

    /**
     * Also accepted, for people whose editors expect it.
     */
    public static final String SHORT_EXTENSION = ".mapping.yml";

    /**
     * Format id under which this loader can be selected explicitly.
     */
    public static final String FORMAT = "yaml";

    /**
     * How much YAML one mapping source may be. A limit has to exist - the document is read into
     * memory, and a loader may be pointed at a file or a URL that an application does not control -
     * and this one is deliberate rather than inherited: the parser's own default of 3 MiB is below
     * what a large terminology table legitimately needs.
     * <p>
     * The number is a ceiling on <em>work</em>, not just on memory, and that is what keeps it from
     * being raised casually: scanning one enormous scalar costs quadratic time in its length. A
     * mapping table's shape is cheap - 8 MiB of entries parses in well under a second, some
     * 400.000 of them, far past any real code system - while 8 MiB written as a single scalar,
     * which only an attacker would send, costs about twelve seconds. At 32 MiB that same document
     * costs over three minutes.
     */
    static final int MAX_DOCUMENT_SIZE = 8 * 1024 * 1024;

    /**
     * Aliases repeating an anchored <em>collection</em>, which is what a "billion laughs" document
     * is built from. The binding has no use for them - a mapping entry is a string - so the ceiling
     * is low enough that nothing can be amplified with them.
     */
    private static final int MAX_ALIASES = 16;

    private static final YAMLMapper MAPPER = YAMLMapper.builder(YAMLFactory.builder()
                    .loadSettings(LoadSettings.builder()
                            .setLabel("mapping file")
                            .setCodePointLimit(MAX_DOCUMENT_SIZE)
                            .setMaxAliasesForCollections(MAX_ALIASES)
                            .setAllowRecursiveKeys(false)
                            .build())
                    .build())
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            // a key written twice is a mistake worth an error: silently keeping the last one is how
            // a mapping ends up translating a code to something nobody declared
            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
            .build();

    private static final Set<String> ENTRY_PROPERTIES =
            Set.of("value", "equivalence", "keyDisplay", "valueDisplay");

    @Override
    public String format() {
        return FORMAT;
    }

    @Override
    public boolean supports(URI source) {
        return MappingLoader.hasExtension(source, EXTENSION)
                || MappingLoader.hasExtension(source, SHORT_EXTENSION);
    }

    @Override
    public List<Mapping> load(InputStream in, URI source, MappingFunctionRegistry functions) {
        MappingYaml.Document document;
        try {
            document = MAPPER.readValue(in, MappingYaml.Document.class);
        } catch (JacksonException e) {
            throw new MappingException(source, "Invalid mapping file: " + e.getOriginalMessage() + at(e), e);
        }
        if (document == null || document.mappings == null || document.mappings.isEmpty()) {
            throw new MappingException(source, "Mapping file declares no mappings");
        }

        var mappings = new ArrayList<Mapping>(document.mappings.size());
        document.mappings.forEach((name, declared) ->
                mappings.add(toMapping(name, declared, source, functions)));
        return mappings;
    }

    // ------------------------------------------------------------------ binding to model

    private static Mapping toMapping(String name, MappingYaml.YamlMapping yaml, URI source,
                                     MappingFunctionRegistry functions) {
        if (yaml == null) {
            throw new MappingException(source, "Mapping '" + name + "' has no body");
        }
        var builder = Mapping.builder(name)
                .keySystem(yaml.keySystem)
                .valueSystem(yaml.valueSystem)
                .reversible(yaml.reversible == null || yaml.reversible)
                .override(Boolean.TRUE.equals(yaml.override));

        if (yaml.entries != null) {
            yaml.entries.forEach((key, node) -> builder.entry(entry(name, key, node, source)));
        }
        builder.unmatched(unmatched(yaml.unmatched, name, "unmatched", source, functions));
        if (yaml.reverse != null) {
            builder.reverseUnmatched(unmatched(yaml.reverse.unmatched, name, "reverse.unmatched",
                    source, functions));
        }
        return builder.build();
    }

    private static org.openehealth.ipf.commons.map.Entry entry(String mapping, String key,
                                                               JsonNode node, URI source) {
        if (node == null || node.isNull()) {
            throw new MappingException(source, "Mapping '" + mapping + "': entry '" + key
                    + "' has no value. Write \"\" for the empty string");
        }
        if (node.isObject()) {
            node.propertyNames().forEach(property -> {
                if (!ENTRY_PROPERTIES.contains(property)) {
                    throw new MappingException(source, "Mapping '" + mapping + "': entry '" + key
                            + "' has the unknown property '" + property + "', expected one of "
                            + ENTRY_PROPERTIES);
                }
            });
            var value = node.get("value");
            if (value == null) {
                throw new MappingException(source, "Mapping '" + mapping + "': entry '" + key
                        + "' is missing the 'value' property");
            }
            var equivalence = node.get("equivalence");
            return new org.openehealth.ipf.commons.map.Entry(key,
                    text(mapping, key, value, source),
                    equivalence == null
                            ? Equivalence.EQUAL
                            : equivalence(mapping, text(mapping, key, equivalence, source), source),
                    display(mapping, key, node.get("keyDisplay"), source),
                    display(mapping, key, node.get("valueDisplay"), source));
        }
        return new org.openehealth.ipf.commons.map.Entry(key, text(mapping, key, node, source));
    }

    private static String display(String mapping, String key, JsonNode node, URI source) {
        return node == null || node.isNull() ? null : text(mapping, key, node, source);
    }

    private static String text(String mapping, String key, JsonNode node, URI source) {
        if (!node.isString()) {
            throw new MappingException(source, "Mapping '" + mapping + "': entry '" + key
                    + "' has the non-string value " + node + ". Mapping keys and values are"
                    + " strings; quote it");
        }
        return node.stringValue();
    }

    private static Equivalence equivalence(String mapping, String declared, URI source) {
        try {
            return Equivalence.valueOf(declared.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MappingException(source, "Mapping '" + mapping + "': unknown equivalence '"
                    + declared + "'", e);
        }
    }

    private static Unmatched unmatched(JsonNode node, String mapping, String property, URI source,
                                       MappingFunctionRegistry functions) {
        if (node == null || node.isNull()) {
            return Unmatched.ABSENT;
        }
        if (node.isString()) {
            return switch (node.stringValue()) {
                case "absent" -> Unmatched.ABSENT;
                // "provided" is what the XML format and FHIR ConceptMap call it
                case "identity", "provided" -> Unmatched.IDENTITY;
                case "fail" -> Unmatched.FAIL;
                default -> throw new MappingException(source, "Mapping '" + mapping + "': unknown "
                        + property + " mode '" + node.stringValue() + "', expected absent, identity"
                        + " or fail, or an object with a 'fixed', 'function' or 'delegate' property");
            };
        }
        if (node.isObject() && node.size() == 1) {
            var property_ = node.propertyNames().iterator().next();
            var argument = node.get(property_);
            if ("fixed".equals(property_) && argument.isString()) {
                return Unmatched.fixed(argument.stringValue());
            }
            if ("delegate".equals(property_) && argument.isString()) {
                return Unmatched.delegate(argument.stringValue());
            }
            if ("function".equals(property_) && argument.isString()) {
                var ref = argument.stringValue();
                if (!functions.contains(ref)) {
                    throw new MappingException(source, "Mapping '" + mapping + "' refers to the mapping"
                            + " function '" + ref + "', which is not registered. Register it with"
                            + " Mappings.builder().function(..) before loading this file");
                }
                return Unmatched.computed(ref);
            }
        }
        throw new MappingException(source, "Mapping '" + mapping + "': " + property + " must be"
                + " absent, identity or fail, or an object with exactly one string-valued 'fixed',"
                + " 'function' or 'delegate' property, but is " + node);
    }

    private static String at(JacksonException e) {
        var location = e.getLocation();
        return location == null || location.getLineNr() < 0
                ? ""
                : " at line " + location.getLineNr() + ", column " + location.getColumnNr();
    }
}
