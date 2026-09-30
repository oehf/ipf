/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.server.TlsSyslogServer;
import org.openehealth.ipf.commons.audit.server.UdpSyslogServer;
import org.openehealth.ipf.commons.audit.server.support.SyslogEventCollector;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A message the syslog server cannot parse at all (here: a blank one) is passed to the error consumer,
 * and must not keep the server from handling the messages that follow.
 */
public class SyslogServerErrorHandlingTest extends AbstractAuditorIntegrationTest {

    private final List<Throwable> errors = new CopyOnWriteArrayList<>();

    @Test
    public void testTlsServerKeepsConnectionAfterUnparsableMessage() throws Exception {
        var tls = setupDefaultTlsParameter();
        var consumer = SyslogEventCollector.newInstance().withExpectation(1);
        try (var ignored = new TlsSyslogServer(consumer, errors::add, tls).start(LOCALHOST, port);
             var socket = tls.getSSLContext(false).getSocketFactory().createSocket(LOCALHOST, port)) {
            var out = socket.getOutputStream();
            out.write("1  ".getBytes(StandardCharsets.US_ASCII));           // blank message
            out.write("7 garbage".getBytes(StandardCharsets.US_ASCII));     // parsed into a failure map
            out.flush();
            assertTrue(consumer.await(5, TimeUnit.SECONDS), "the message after the unparsable one was not handled");
        }
        assertEquals(1, errors.size(), errors.toString());
    }

    /**
     * A frame within the maximum length is handled, a longer one gets the client disconnected
     */
    @Test
    public void testTlsServerDisconnectsOnTooLongFrame() throws Exception {
        var tls = setupDefaultTlsParameter();
        var consumer = SyslogEventCollector.newInstance().withExpectation(1);
        var server = new TlsSyslogServer(consumer, errors::add, tls);
        server.setMaxFrameLength(100);
        try (var ignored = server.start(LOCALHOST, port);
             var socket = tls.getSSLContext(false).getSocketFactory().createSocket(LOCALHOST, port)) {
            socket.setSoTimeout(5000);
            var out = socket.getOutputStream();
            out.write(("100 " + "x".repeat(100)).getBytes(StandardCharsets.US_ASCII));
            out.flush();
            assertTrue(consumer.await(5, TimeUnit.SECONDS), "the frame within the maximum length was not handled");

            out.write(("101 " + "x".repeat(101)).getBytes(StandardCharsets.US_ASCII));
            out.flush();
            assertEquals(-1, socket.getInputStream().read(), "the client has not been disconnected");
        }
        assertEquals(1, consumer.getSyslogEvents().size());
    }

    @Test
    public void testInvalidMaxFrameLength() {
        var server = new TlsSyslogServer(map -> { }, errors::add, setupDefaultTlsParameter());
        assertThrows(IllegalArgumentException.class, () -> server.setMaxFrameLength(0));
    }

    @Test
    public void testUdpServerContinuesAfterUnparsableDatagram() throws Exception {
        var consumer = SyslogEventCollector.newInstance().withExpectation(1);
        try (var ignored = new UdpSyslogServer(consumer, errors::add).start(LOCALHOST, port);
             var socket = new DatagramSocket()) {
            var address = InetAddress.getByName(LOCALHOST);
            for (var datagram : List.of(" ", "garbage")) {
                var bytes = datagram.getBytes(StandardCharsets.US_ASCII);
                socket.send(new DatagramPacket(bytes, bytes.length, address, port));
            }
            assertTrue(consumer.await(5, TimeUnit.SECONDS), "the datagram after the unparsable one was not handled");
        }
        assertEquals(1, errors.size(), errors.toString());
    }
}
