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
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.handler.logging.LogLevel;
import org.openehealth.ipf.commons.audit.NettyUtils;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.DisposableChannel;
import reactor.netty.tcp.TcpServer;
import reactor.util.Metrics;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

/**
 * TLS syslog server following RFC 5425.
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class TlsSyslogServer extends SyslogServer<DisposableChannel> {

    private static final Logger LOG = LoggerFactory.getLogger(TlsSyslogServer.class);
    protected final TlsParameters tlsParameters;

    public TlsSyslogServer(Consumer<? super Map<String, Object>> consumer,
                           Consumer<Throwable> errorConsumer) {
        this(consumer, errorConsumer, TlsParameters.getDefault());
    }

    public TlsSyslogServer(Consumer<? super Map<String, Object>> consumer,
                           Consumer<Throwable> errorConsumer,
                           TlsParameters tlsParameters) {
        super(consumer, errorConsumer);
        this.tlsParameters = tlsParameters;
    }

    @Override
    public TlsSyslogServer doStart(String host, int port) {
        var sslContext = NettyUtils.initSslContext(tlsParameters, true);
        channel = TcpServer.create()
                .host(host)
                .port(port)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                .wiretap(getClass().getName(), LogLevel.TRACE)
                .metrics(Metrics.isInstrumentationAvailable())
                .secure(spec -> spec.sslContext(sslContext))
                .doOnBind(serverBootstrap -> LOG.info("TLS Syslog Server is about to be started"))
                .doOnBound(disposableServer -> LOG.info("TLS Syslog Server bound on {}", disposableServer.address()))
                .doOnUnbound(disposableServer -> LOG.info("TLS Syslog Server unbound from {}", disposableServer.address()))
                .doOnConnection(connection -> {
                    LOG.debug("Received connection from {}", connection.channel().localAddress());
                    connection
                            .addHandler(new Rfc5425Decoder())   // extract frame
                            .addHandler(new Rfc5424Decoder());  // parse frame, fast enough for receiver thread
                })
                .handle((nettyInbound, nettyOutbound) -> nettyInbound.receiveObject()
                        .cast(Map.class)
                        .flatMap(this::handleMap)
                        .doOnError(errorConsumer)
                        .then())
                .bindNow(Duration.ofSeconds(TIMEOUT));
        return this;
    }



}
