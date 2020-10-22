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
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.tcp.TcpServer;
import reactor.util.Metrics;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
    public TlsSyslogServer start(String host, int port) {
        var sslContext = initSslContext();
        //noinspection unchecked
        channel = TcpServer.create()
                .host(host)
                .port(port)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .wiretap(true)
                .metrics(Metrics.isInstrumentationAvailable())
                .secure(spec -> spec.sslContext(sslContext))
                .doOnBind(serverBootstrap -> LOG.debug("Server is about to be started"))
                .doOnBound(disposableServer -> LOG.debug("Server bound on {}:{}", disposableServer.host(), disposableServer.port()))
                .doOnUnbound(disposableServer -> LOG.debug("Server unbound from {}:{}", disposableServer.host(), disposableServer.port()))
                .doOnConnection(connection -> {
                    LOG.debug("Received connection from {}", connection.channel().localAddress());
                    connection
                            .addHandler(new Rfc5425Decoder())   // extract frame
                            .addHandler(new Rfc5424Decoder());  // parse frame
                })
                // parse frame
                .handle((nettyInbound, nettyOutbound) -> nettyInbound.receiveObject()
                        .cast(Map.class)
                        .doOnNext((Consumer<? super Map>) consumer)
                        .doOnError(errorConsumer)
                        .then(Mono.empty()))
                .bindNow(Duration.ofSeconds(TIMEOUT));
        return this;
    }

    private SslContext initSslContext() {
        var allowedProtocols = System.getProperty("jdk.tls.client.protocols", "TLSv1.2");
        var protocols = Stream.of(allowedProtocols.split("\\s*,\\s*")).toArray(String[]::new);
        return new JdkSslContext(
                tlsParameters.getSSLContext(),
                false,
                null, // use default
                SupportedCipherSuiteFilter.INSTANCE,
                ApplicationProtocolConfig.DISABLED,
                ClientAuth.REQUIRE, // require mutual authentication
                protocols,
                false
        );
    }

}
