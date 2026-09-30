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

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openehealth.ipf.commons.map.hamcrest.MappingMatchers.translates;

/**
 * What holds for a mapping source whatever format it is in, and whoever is at the other end of the
 * location it was given. A source is read while the application starts, so a location that answers
 * endlessly must end as one failed mapping file rather than as an {@link OutOfMemoryError}.
 */
public class MappingSourceLimitsTest {

    private static final String ONE_MAPPING = """
            @name = religion
            LUT = Lutheran
            """;

    @Test
    public void aSourceThatNeverEndsIsCutOff() throws IOException {
        try (var server = new Endless(Integer.MAX_VALUE)) {
            var e = assertThrows(MappingException.class,
                    () -> Mappings.builder().load(server.uri(), TestMappingLoader.FORMAT).build());

            assertThat(e.getMessage(), containsString("Could not read mapping source"));
            assertThat(rootCause(e).getMessage(), containsString("larger than"));
        }
    }

    /**
     * The bound is on the source, not on the protocol: a location that answers normally is read
     * exactly as before.
     */
    @Test
    public void aSourceOverHttpIsReadNormally() throws IOException {
        try (var server = new Endless(0)) {
            var mappings = Mappings.builder().load(server.uri(), TestMappingLoader.FORMAT).build();

            assertThat(mappings, translates("religion", "LUT").to("Lutheran"));
        }
    }

    private static Throwable rootCause(Throwable e) {
        var cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    /**
     * Answers one valid mapping and then however many more bytes it was told to send.
     */
    private static final class Endless implements AutoCloseable {

        private final HttpServer server;

        private Endless(int extraLines) throws IOException {
            server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
            server.createContext("/mappings", exchange -> {
                exchange.sendResponseHeaders(200, 0);
                try (var out = exchange.getResponseBody()) {
                    out.write(ONE_MAPPING.getBytes(StandardCharsets.UTF_8));
                    var filler = "# padding padding padding padding padding padding padding\n"
                            .getBytes(StandardCharsets.UTF_8);
                    for (var i = 0; i < extraLines; i++) {
                        out.write(filler);
                    }
                } catch (IOException e) {
                    // the loader hung up once it had read enough, which is the point
                }
            });
            server.start();
        }

        URI uri() {
            return URI.create("http://" + server.getAddress().getHostString()
                    + ":" + server.getAddress().getPort() + "/mappings");
        }

        @Override
        public void close() {
            server.stop(0);
        }
    }
}
