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

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.NettyUtils;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.Connection;
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
        return new ReactorNettyDestination(tlsParameters, host, port, workerThreads, connectTimeoutMillis, sendTimeoutMillis, logging);
    }

    /**
     * Sets the timeout for establishing the connection, including the TLS handshake. Defaults to 5 seconds.
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
     * Set the number of worker threads of the event loop. As there is only one connection per Audit
     * Record Repository, which is always served by one thread, more threads only make a difference when
     * auditing to several repositories. Defaults to 1.
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
        private final long connectTimeoutMillis;
        private final long sendTimeoutMillis;
        private final LoopResources loop;
        private final TcpClient tcpClient;
        // only ever holds an established connection
        private Connection connection;
        private final String host;
        private final int port;

        ReactorNettyDestination(TlsParameters tlsParameters, String host, int port, int workerThreads,
                                long connectTimeoutMillis, long sendTimeoutMillis, boolean withLogging) {

            this.connectTimeoutMillis = connectTimeoutMillis;
            this.sendTimeoutMillis = sendTimeoutMillis;
            this.host = host;
            this.port = port;

            // Configure the client.
            this.loop = LoopResources.create("event-loop", 1, workerThreads, true);
            var sslContext = NettyUtils.initSslContext(tlsParameters, false);
            var client = TcpClient.create()
                    .host(host)
                    .port(port)
                    .runOn(loop)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeoutMillis)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .metrics(NettyUtils.isMicrometerAvailable())
                    .secure(spec -> spec.sslContext(sslContext))
                    .doOnConnect(config -> log.info("TLS Syslog Client is about to be started"))
                    .doOnConnected(connection -> log.info("TLS Syslog Client connected to {}", connection.address()))
                    .doOnDisconnected(connection -> log.info("TLS Syslog Client disconnected from {}", connection.address()));
            this.tcpClient = withLogging ? client.wiretap(getClass().getName(), LogLevel.DEBUG) : client;

        }

        @Override
        public synchronized void shutdown() {
            if (connection != null) {
                connection.disposeNow(Duration.ofSeconds(10));
            }
            loop.disposeLater(Duration.ZERO, Duration.ofSeconds(10)).block(Duration.ofSeconds(10));
        }

        /**
         * Returns the connection to the Audit Record Repository, and establishes it if there is none or
         * the previous one has been closed. {@link TcpClient#connectNow(Duration)} only returns an
         * established connection, including the TLS handshake, within the connect timeout. Synchronized,
         * so that concurrent senders neither open several connections nor see one that is still being set up.
         *
         * @return the established connection
         * @throws AuditException if the connection cannot be established
         */
        @Override
        public synchronized Connection getHandle() {
            if (connection == null || !connection.channel().isActive()) {
                if (connection != null) {
                    connection.dispose();
                }
                try {
                    connection = tcpClient.connectNow(Duration.ofMillis(connectTimeoutMillis));
                } catch (Exception e) {
                    throw new AuditException("Could not establish TLS connection to " + host + ":" + port, e);
                }
            }
            return connection;
        }

        @Override
        public void write(byte[] bytes) {
            var channel = getHandle().channel();
            log.trace("Writing {} bytes using session: {}", bytes.length, channel);
            NettyUtils.writeAndAwait(channel, bytes, sendTimeoutMillis, host, port);
        }

    }


}

