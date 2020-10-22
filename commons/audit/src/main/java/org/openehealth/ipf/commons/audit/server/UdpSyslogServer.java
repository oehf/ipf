/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.commons.audit.server;

import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.udp.UdpServer;
import reactor.util.Metrics;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

/**
 * TLS syslog server following RFC 5426.
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class UdpSyslogServer extends SyslogServer<Connection> {

    private static final Logger LOG = LoggerFactory.getLogger(UdpSyslogServer.class);

    public UdpSyslogServer(Consumer<? super Map<String, Object>> consumer,
                           Consumer<Throwable> errorConsumer) {
        super(consumer, errorConsumer);
    }

    @Override
    public UdpSyslogServer start(String host, int port) {
        channel = UdpServer.create()
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .host(host)
                .port(port)
                .wiretap(true)
                .metrics(Metrics.isInstrumentationAvailable())

                // This does not work!
                //.doOnBound(connection -> connection
                //        .addHandler(new Rfc5426Decoder())
                //        .addHandler(new Rfc5424Decoder()))
                .doOnBind(serverBootstrap -> LOG.debug("Server is about to be started"))
                .doOnBound(disposableServer -> LOG.debug("Server bound on {}", disposableServer.channel().localAddress()))
                .doOnUnbound(disposableServer -> LOG.debug("Server unbound from {}", disposableServer.channel().localAddress()))
                .handle((udpInbound, udpOutbound) -> udpInbound.receiveObject()
                        // Because the handlers don't seem to step in, we handle it here
                        .map(o -> ((DatagramPacket) o).content())
                        .map(Rfc5424Decoder::parseByteBuf)
                        .cast(Map.class)
                        .doOnNext((Consumer<? super Map>) consumer)
                        .doOnError(errorConsumer)
                        .then(Mono.empty()))
                .bindNow(Duration.ofSeconds(TIMEOUT));
        return this;
    }

}
