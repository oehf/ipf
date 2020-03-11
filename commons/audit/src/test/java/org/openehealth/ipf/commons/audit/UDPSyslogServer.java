/*
 * Copyright 2018 the original author or authors.
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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.ext.unit.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class UDPSyslogServer extends AbstractVerticle {

    private final int udpPort;

    private final String host;

    private final Async async;

    private final DatagramSocketOptions dsOptions = new DatagramSocketOptions()
            .setIpV6(false)
            .setReuseAddress(true);

    private static final Logger log = LoggerFactory.getLogger(UDPSyslogServer.class);

    public UDPSyslogServer(String host, int udpPort, Async async) {
        this.udpPort = udpPort;
        this.host = host;
        this.async = async;
    }

    @Override
    public void start() {
        final DatagramSocket socket = vertx.createDatagramSocket(dsOptions);
        socket.listen(udpPort, host, datagramSocketAsyncResult -> {
            if (datagramSocketAsyncResult.succeeded()){
                log.info("Listening on UDP port {}", socket.localAddress());
                async.countDown();
                socket.handler(packet -> {
                    String decoded = packet.data().getString(0, packet.data().length());
                    log.debug("=============== Received content on {} ================= \n{}",
                            socket.localAddress(), decoded);
                    async.countDown();
                });
            } else {
                log.warn("Listen failed on port {}", udpPort, datagramSocketAsyncResult.cause());
            }
        });
    }
}