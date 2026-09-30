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
package org.openehealth.ipf.commons.map.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.Mappings;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A mapping file is data, and it arrives from wherever the application points the loader: a jar on
 * the classpath, but also a file someone dropped in a configuration directory or a document fetched
 * over HTTP. These are the attacks an XML parser is expected to shrug off.
 */
public class XmlMappingLoaderSecurityTest {

    private static final String SECRET = "the-secret-contents";

    private static Mappings load(Path source) {
        return Mappings.builder().load(source.toUri()).build();
    }

    private static Path write(Path directory, String name, String content) throws IOException {
        var file = directory.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    /**
     * The classic: an external entity that reads a local file into the document.
     */
    @Test
    public void externalEntityIsNotResolved(@TempDir Path directory) throws IOException {
        var secret = write(directory, "secret.txt", SECRET);
        var source = write(directory, "xxe.mapping.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE mapping [<!ENTITY xxe SYSTEM "%s">]>
                <mapping name="m"><entry key="a" value="&xxe;"/></mapping>
                """.formatted(secret.toUri()));

        var e = assertThrows(MappingException.class, () -> load(source));

        assertThat(e.getMessage(), not(containsString(SECRET)));
        assertThat(e.getMessage().toLowerCase(), containsString("doctype"));
    }

    /**
     * Entity expansion, the "billion laughs" denial of service. A rejected DOCTYPE is what makes
     * the expansion limits moot.
     */
    @Test
    public void entityExpansionIsNotEvenAttempted(@TempDir Path directory) throws IOException {
        var source = write(directory, "laughs.mapping.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE mapping [
                  <!ENTITY a "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa">
                  <!ENTITY b "&a;&a;&a;&a;&a;&a;&a;&a;&a;&a;">
                  <!ENTITY c "&b;&b;&b;&b;&b;&b;&b;&b;&b;&b;">
                  <!ENTITY d "&c;&c;&c;&c;&c;&c;&c;&c;&c;&c;">
                  <!ENTITY e "&d;&d;&d;&d;&d;&d;&d;&d;&d;&d;">
                  <!ENTITY f "&e;&e;&e;&e;&e;&e;&e;&e;&e;&e;">
                ]>
                <mapping name="m"><entry key="a" value="&f;"/></mapping>
                """);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage().toLowerCase(), containsString("doctype"));
    }

    /**
     * An external DTD subset, which is how an XXE payload exfiltrates over the network.
     */
    @Test
    public void externalDtdIsNotFetched(@TempDir Path directory) throws Exception {
        try (var listener = new Listener()) {
            var source = write(directory, "dtd.mapping.xml", """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE mapping SYSTEM "http://127.0.0.1:%d/evil.dtd">
                    <mapping name="m"><entry key="a" value="b"/></mapping>
                    """.formatted(listener.port()));

            assertThrows(MappingException.class, () -> load(source));
            assertThat(listener.wasCalled(), is(false));
        }
    }

    /**
     * The schema is the one this module ships, so a document naming another one is not a way to
     * make the loader fetch a URL. This would be a request the application never asked for, from
     * inside its own network.
     */
    @Test
    public void schemaLocationHintIsNotFetched(@TempDir Path directory) throws Exception {
        try (var listener = new Listener()) {
            var source = write(directory, "hint.mapping.xml", """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <mapping name="m"
                             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xsi:noNamespaceSchemaLocation="http://127.0.0.1:%d/evil.xsd">
                        <entry key="a" value="b"/>
                    </mapping>
                    """.formatted(listener.port()));

            var mappings = load(source);

            assertThat(mappings.map("m", "a"), is(java.util.Optional.of("b")));
            assertThat(listener.wasCalled(), is(false));
        }
    }

    /**
     * A document that is nothing but nesting must come back as a rejected mapping file, not as a
     * StackOverflowError that no caller can be expected to handle.
     */
    @Test
    public void deepNestingIsRejectedAsInvalidRatherThanCrashing(@TempDir Path directory) throws IOException {
        var depth = 50_000;
        var nested = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<mappings>".repeat(depth) +
            "</mappings>".repeat(depth);
        var source = write(directory, "deep.mapping.xml", nested);

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), anyOf(containsString("Invalid mapping file"),
                containsString("Could not parse mapping file")));
    }

    /**
     * A socket that answers nothing and remembers whether anybody knocked.
     */
    private static final class Listener implements AutoCloseable {

        private final ServerSocket socket = new ServerSocket(0, 1, java.net.InetAddress.getLoopbackAddress());
        private final CountDownLatch called = new CountDownLatch(1);

        private Listener() throws IOException {
            socket.setSoTimeout(2000);
            // closed or nobody called, which is the outcome the tests want
            Thread thread = new Thread(() -> {
                try (var accepted = socket.accept()) {
                    called.countDown();
                } catch (IOException e) {
                    // closed or nobody called, which is the outcome the tests want
                }
            });
            thread.setDaemon(true);
            thread.start();
        }

        int port() {
            return socket.getLocalPort();
        }

        boolean wasCalled() throws InterruptedException {
            return called.await(500, TimeUnit.MILLISECONDS);
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}
