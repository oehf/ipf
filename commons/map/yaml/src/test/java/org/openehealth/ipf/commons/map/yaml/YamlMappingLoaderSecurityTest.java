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
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.Mappings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.core.hamcrest.OptionalMatchers.hasValue;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;

/**
 * YAML's dangerous features are the ones that make it more than a data format: type tags that name
 * a class to instantiate, and aliases that let a small document expand into a huge one.
 */
public class YamlMappingLoaderSecurityTest {

    private static Path write(Path directory, String name, String content) throws IOException {
        var file = directory.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    private static Mappings load(Path source) {
        return Mappings.builder().load(source.toUri()).build();
    }

    /**
     * The SnakeYAML deserialization gadget: a tag naming a class the parser would instantiate.
     * {@code ScriptEngineManager} loads a URL through the context class loader, which is remote
     * code execution where a parser honors it.
     */
    @Test
    public void typeTagsDoNotInstantiateClasses(@TempDir Path directory) throws IOException {
        var source = write(directory, "gadget.mapping.yaml", """
                mappings:
                  m: !!javax.script.ScriptEngineManager [!!java.net.URL ["http://127.0.0.1:1/"]]
                """);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), not(containsString("ScriptEngineManager cannot be cast")));
    }

    /**
     * A tag on a value the loader does expect is inert as well: the value binds as the text it is,
     * and no class named in the document is touched.
     */
    @Test
    public void typeTagsOnKnownFieldsAreInert(@TempDir Path directory) throws IOException {
        var source = write(directory, "tagged.mapping.yaml", """
                mappings:
                  m:
                    entries:
                      a: !!java.net.URL "http://127.0.0.1:1/"
                """);

        assertThat(load(source).map("m", "a"), hasValue("http://127.0.0.1:1/"));
    }

    /**
     * "Billion laughs" through aliases: without a limit, this expands to gigabytes before the
     * binding ever sees it. Aliases are rejected outright, before anything is expanded.
     */
    @Test
    public void aliasExpansionIsBounded(@TempDir Path directory) throws IOException {
        var yaml = new StringBuilder("mappings:\n  m:\n    entries:\n");
        yaml.append("      a: &a \"").append("x".repeat(1000)).append("\"\n");
        var previous = "a";
        for (var level = 0; level < 10; level++) {
            var name = "l" + level;
            yaml.append("      ").append(name).append(": &").append(name).append(" [");
            yaml.append(("*" + previous + ",").repeat(9)).append('*').append(previous).append("]\n");
            previous = name;
        }

        var source = write(directory, "aliases.mapping.yaml", yaml.toString());
        var before = System.nanoTime();

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("aliases are not supported"));

        var seconds = (System.nanoTime() - before) / 1_000_000_000.0;
        assertThat(seconds, lessThan(10.0));
    }

    /**
     * A document that is nothing but nesting comes back as a rejected mapping file rather than as
     * a StackOverflowError.
     */
    @Test
    public void deepNestingIsRejectedRatherThanCrashing(@TempDir Path directory) throws IOException {
        var depth = 100_000;
        var source = write(directory, "deep.mapping.yaml",
                "mappings:\n  m:\n    entries:\n      a: " + "[".repeat(depth) + "]".repeat(depth) + "\n");

        assertThrows(MappingException.class, () -> load(source));
    }

    /**
     * A recursive alias has no finite expansion, so the one thing that must not happen is an
     * attempt to build it. It is rejected like any other alias.
     */
    @Test
    public void recursiveAliasesAreRejected(@TempDir Path directory) throws IOException {
        var source = write(directory, "recursive.mapping.yaml", """
                mappings: &loop
                  m:
                    entries:
                      a: *loop
                """);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("aliases are not supported at line 4"));
    }

    /**
     * The parser does not resolve an alias but hands over the anchor's name, so an alias used to
     * translate a code to that name - {@code b} to {@code x} here - without any error.
     */
    @Test
    public void plainAliasesAreRejectedRatherThanReadAsTheAnchorsName(@TempDir Path directory) throws IOException {
        var source = write(directory, "alias.mapping.yaml", """
                mappings:
                  m:
                    entries:
                      a: &x male
                      b: *x
                """);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("aliases are not supported at line 5"));
    }

    /**
     * A key written twice used to be the last one silently winning, which is how a mapping ends up
     * translating a code to something nobody declared.
     */
    @Test
    public void duplicateKeysAreRejected(@TempDir Path directory) throws IOException {
        var source = write(directory, "duplicate.mapping.yaml", """
                mappings:
                  m:
                    entries:
                      M: male
                      M: something-else
                """);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("Duplicate"));
    }

    /**
     * The document limit is deliberate, and set above what a real terminology table needs - the
     * parser's own default of 3 MiB is not.
     */
    @Test
    public void aLargeButLegitimateTableStillLoads(@TempDir Path directory) throws IOException {
        var yaml = new StringBuilder("mappings:\n  m:\n    entries:\n");
        for (var i = 0; i < 200_000; i++) {
            yaml.append("      k").append(i).append(": v").append(i).append('\n');
        }
        assertThat(yaml.length(), greaterThan(3 * 1024 * 1024));

        var mappings = load(write(directory, "large.mapping.yaml", yaml.toString()));

        assertThat(mappings.keys("m"), hasSize(200_000));
        assertThat(mappings, translates("m", "k199999").to("v199999"));
    }

    /**
     * ... and a document beyond it is refused rather than read into memory. Built out of entries
     * rather than one enormous scalar, because scanning a scalar that size is exactly the quadratic
     * work the limit exists to bound - the test would pay it too.
     */
    @Test
    public void anOversizedDocumentIsRefused(@TempDir Path directory) {
        var oversized = new StringBuilder("mappings:\n  m:\n    entries:\n");
        for (var i = 0; oversized.length() <= YamlMappingLoader.MAX_DOCUMENT_SIZE; i++) {
            oversized.append("      k").append(i).append(": some-value-that-takes-up-room\n");
        }

        var e = assertThrows(MappingException.class,
                () -> load(write(directory, "oversized.mapping.yaml", oversized.toString())));
        assertThat(e.getMessage(), containsString("exceeds the limit"));
    }
}
