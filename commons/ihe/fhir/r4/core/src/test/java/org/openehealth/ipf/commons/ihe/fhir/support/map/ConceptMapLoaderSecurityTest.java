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
package org.openehealth.ipf.commons.ihe.fhir.support.map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openehealth.ipf.commons.map.MappingException;
import org.openehealth.ipf.commons.map.Mappings;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A ConceptMap is the one mapping format an application is likely to accept from outside itself -
 * from a terminology server, or out of an Implementation Guide - so what HAPI's parsers do with a
 * hostile document is worth pinning down rather than assuming.
 */
public class ConceptMapLoaderSecurityTest {

    private static final String SECRET = "TOP-SECRET-VALUE";

    private static Path write(Path directory, String name, String content) throws IOException {
        var file = directory.resolve(name);
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }

    private static Mappings load(Path source) {
        return Mappings.builder().load(source.toUri()).build();
    }

    @Test
    public void externalEntityIsNotResolved(@TempDir Path directory) throws IOException {
        var secret = write(directory, "secret.txt", SECRET);
        var source = write(directory, "xxe.conceptmap.r4.xml", """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE ConceptMap [<!ENTITY xxe SYSTEM "%s">]>
                <ConceptMap xmlns="http://hl7.org/fhir">
                  <name value="&xxe;"/>
                  <group><source value="s"/><target value="t"/>
                    <element><code value="a"/>
                      <target><code value="b"/><equivalence value="equal"/></target>
                    </element>
                  </group>
                </ConceptMap>
                """.formatted(secret.toUri()));

        var e = assertThrows(MappingException.class, () -> load(source));

        assertThat(e.getMessage(), not(containsString(SECRET)));
        assertThat(e.getMessage(), containsString("Undeclared general entity"));
    }

    /**
     * The document's DOCTYPE is ignored rather than fetched, so a resource cannot make the
     * application issue a request from inside its own network.
     */
    @Test
    public void externalDtdIsNotFetched(@TempDir Path directory) throws Exception {
        try (var listener = new Listener()) {
            var source = write(directory, "dtd.conceptmap.r4.xml", """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE ConceptMap SYSTEM "http://127.0.0.1:%d/evil.dtd">
                    <ConceptMap xmlns="http://hl7.org/fhir">
                      <name value="m"/>
                      <group><source value="s"/><target value="t"/>
                        <element><code value="a"/>
                          <target><code value="b"/><equivalence value="equal"/></target>
                        </element>
                      </group>
                    </ConceptMap>
                    """.formatted(listener.port()));

            assertThat(load(source).map("m", "a"), is(java.util.Optional.of("b")));
            assertThat(listener.wasCalled(), is(false));
        }
    }

    @Test
    public void deeplyNestedJsonIsRejectedRatherThanCrashing(@TempDir Path directory) throws IOException {
        var depth = 50_000;
        var source = write(directory, "deep.conceptmap.r4.json",
                "{\"resourceType\":\"ConceptMap\",\"name\":\"m\",\"group\":"
                        + "[".repeat(depth) + "]".repeat(depth) + "}");

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("nesting depth"));
    }

    @Test
    public void deeplyNestedXmlIsRejectedRatherThanCrashing(@TempDir Path directory) throws IOException {
        var depth = 20_000;
        var source = write(directory, "deep.conceptmap.r4.xml",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ConceptMap xmlns=\"http://hl7.org/fhir\">"
                        + "<group>".repeat(depth) + "</group>".repeat(depth) + "</ConceptMap>");

        var e = assertThrows(MappingException.class, () -> load(source));
        assertThat(e.getMessage(), containsString("Depth limit"));
    }

    /**
     * A socket that answers nothing and remembers whether anybody knocked.
     */
    private static final class Listener implements AutoCloseable {

        private final ServerSocket socket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress());
        private final CountDownLatch called = new CountDownLatch(1);

        private Listener() throws IOException {
            socket.setSoTimeout(2000);
            var thread = new Thread(() -> {
                try (var accepted = socket.accept()) {
                    called.countDown();
                } catch (IOException e) {
                    // closed, or nobody called - which is the outcome the tests want
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
