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

package org.openehealth.ipf.commons.audit.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.NettyUtils;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.Connection;
import reactor.netty.internal.util.Metrics;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Reactor Netty client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class ReactorNettyTLSSyslogSenderImpl extends NioTLSSyslogSenderImpl<Connection, ReactorNettyTLSSyslogSenderImpl.ReactorNettyDestination> {

    private static final Logger log = LoggerFactory.getLogger(ReactorNettyTLSSyslogSenderImpl.class);

    private int workerThreads = 1;
    private long connectTimeoutMillis = 5000;
    private long sendTimeoutMillis = 5000;

    public ReactorNettyTLSSyslogSenderImpl(TlsParameters tlsParameters) {
        super(tlsParameters);
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.REACTOR_NETTY_TLS.getProtocolName();
    }

    @Override
    protected ReactorNettyDestination makeDestination(TlsParameters tlsParameters, String host, int port, boolean logging) {
        return new ReactorNettyDestination(tlsParameters, host, port, workerThreads, connectTimeoutMillis, sendTimeoutMillis);
    }

    /**
     * Sets the connect timeout
     *
     * @param value    time value
     * @param timeUnit time unit
     */
    public void setConnectTimeout(int value, TimeUnit timeUnit) {
        this.connectTimeoutMillis = timeUnit.toMillis(value);
    }

    /**
     * Sets the send timeout
     *
     * @param value    time value
     * @param timeUnit time unit
     */
    public void setSendTimeout(int value, TimeUnit timeUnit) {
        this.sendTimeoutMillis = timeUnit.toMillis(value);
    }

    /**
     * Set the number of working threads. This corresponds with the number of connections
     * being opened. Defaults to 1.
     *
     * @param workerThreads number of worker threads.
     */
    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    /**
     * Destination abstraction for Netty
     */
    public static final class ReactorNettyDestination implements NioTLSSyslogSenderImpl.Destination<Connection> {
        private final long sendTimeout;
        private final TcpClient tcpClient;
        private Connection connection;
        private final String host;
        private final int port;

        ReactorNettyDestination(TlsParameters tlsParameters, String host, int port, int workerThreads,
                                long connectTimeout, long sendTimeout) {

            this.sendTimeout = sendTimeout;
            this.host = host;
            this.port = port;

            // Configure the client.
            var loop = LoopResources.create("event-loop", 1, workerThreads, true);
            var sslContext = NettyUtils.initSslContext(tlsParameters, false);
            this.tcpClient = TcpClient.create()
                    .host(host)
                    .port(port)
                    .runOn(loop)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .wiretap(getClass().getName(), LogLevel.TRACE)
                    .metrics(Metrics.isMicrometerAvailable())
                    .secure(spec -> spec.sslContext(sslContext))
                    .doOnConnect(config -> log.info("TLS Syslog Client is about to be started"))
                    .doOnConnected(connection -> log.info("TLS Syslog Client connected to {}", connection.address()))
                    .doOnDisconnected(connection -> log.info("TLS Syslog Client disconnected from {}", connection.address()));

        }

        @Override
        public void shutdown() {
            if (connection != null) {
                connection.disposeNow(Duration.ofSeconds(10));
            }
        }

        @Override
        public Connection getHandle() {
            if (connection == null || !connection.channel().isActive()) {
                try {
                    connection = tcpClient.connectNow(Duration.ofSeconds(10));
                } catch (Exception e) {
                    throw new AuditException("Interrupted while establishing TLS connection to " + host + ":" + port, e);
                }
            }
            return connection;
        }

        @Override
        public void write(byte[] bytes) {
            // The write operation is asynchronous.
            var channel = getHandle().channel();
            log.trace("Writing {} bytes using session: {}", bytes.length, channel);
            try {
                if (!channel.writeAndFlush(Unpooled.wrappedBuffer(bytes)).await(sendTimeout)) {
                    throw new AuditException("Could not send audit message to " + host + ":" + port);
                }
            } catch (InterruptedException e) {
                throw new AuditException("Interrupted during sending audit message to " + host + ":" + port, e);
            }
        }

    }


}

