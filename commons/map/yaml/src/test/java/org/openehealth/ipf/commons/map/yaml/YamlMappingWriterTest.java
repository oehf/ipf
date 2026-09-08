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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.MappingConverter;
import org.openehealth.ipf.commons.map.Mappings;
import org.openehealth.ipf.commons.map.Unmatched;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.hasUnmatched;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translatesBack;

public class YamlMappingWriterTest {

    private static final String BIDI_GENDER = "hl7v2v3-bidi-administrativeGender-administrativeGender";
    private static final String MARITAL_STATUS = "hl7v2v3-patient-maritalStatus";
    private static final String RELIGION = "hl7v2fhir-patient-religion";

    private static final Function<String, String> OID_URI = key -> key != null && !key.isEmpty()
            && Character.isDigit(key.charAt(0)) ? "urn:oid:" + key : key;

    /**
     * Everything the format can express survives a round trip, and the result is still valid
     * YAML that the loader accepts.
     */
    @Test
    public void roundTripsEveryPartOfTheModel(@TempDir Path directory) throws IOException {
        var original = Mappings.builder()
                .function("oidUri", OID_URI)
                .load("classpath:/example.mapping.yaml")
                .build();

        var mappings = original.mappingNames().stream()
                .map(name -> original.mapping(name).orElseThrow())
                .toList();
        var yaml = new YamlMappingWriter().toYaml(mappings);

        var target = directory.resolve("round-trip.mapping.yaml");
        Files.writeString(target, yaml);
        var reloaded = Mappings.builder()
                .function("oidUri", OID_URI)
                .load(target.toUri())
                .build();

        assertThat(reloaded.mappingNames(), is(original.mappingNames()));
        for (var name : original.mappingNames()) {
            assertThat("mapping " + name + " did not survive the round trip:\n" + yaml,
                    reloaded.mapping(name).orElseThrow(),
                    is(original.mapping(name).orElseThrow()));
        }
    }

    /**
     * A numeric-looking code must come back out quoted, or reloading it would produce a number
     * where the model wants a string.
     */
    @Test
    public void keepsNumericCodesQuotedAndPointsAtTheSchema() {
        var mappings = Mappings.builder()
                .function("oidUri", OID_URI)
                .load("classpath:/example.mapping.yaml")
                .build();
        var yaml = new YamlMappingWriter().toYaml(
                List.of(mappings.mapping(RELIGION).orElseThrow()));

        assertThat(yaml, startsWith("# yaml-language-server: $schema="));
        assertThat(yaml, containsString("AGN: \"1004\""));
    }

    /**
     * The converter targets whichever format a writer on the classpath produces, so migrating a
     * legacy script to YAML is the same one-liner as migrating it to XML.
     */
    @Test
    public void converterCanTargetYaml(@TempDir Path directory) throws IOException {
        var converter = new MappingConverter();
        var source = Path.of(Objects.requireNonNull(getClass().getResource("/legacy.map")).getPath());
        var conversion = converter.convert(source, directory, YamlMappingLoader.EXTENSION);

        var target = directory.resolve("legacy.mapping.yaml");
        assertThat(Files.exists(target), is(true));
        var yaml = Files.readString(target);
        assertThat(yaml, containsString("unmatched: identity"));
        assertThat(yaml, containsString("fixed: UN"));
        assertThat(yaml, containsString("\"1004\""));
        assertThat(conversion.warnings().toString(),
                conversion.warnings().stream().anyMatch(w -> w.startsWith("atnaCodingSystem:")),
                is(true));

        var mappings = Mappings.builder()
                .function("TODO-atnaCodingSystem", OID_URI)
                .load(target.toUri())
                .build();
        assertThat(mappings, translates(BIDI_GENDER, "UN").to("O"));
        assertThat(mappings, translatesBack(BIDI_GENDER, "X").to("UN"));
        assertThat(mappings, translates(RELIGION, "AGN").to("1004"));
        assertThat(mappings, translates(MARITAL_STATUS, "L").to("A"));
        assertThat(mappings, translates("atnaCodingSystem", "1.2.3").to("urn:oid:1.2.3"));
        assertThat(mappings.mapping(MARITAL_STATUS).orElseThrow(), hasUnmatched(Unmatched.IDENTITY));
    }
}
