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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import lombok.Getter;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.NettyUtils;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.openehealth.ipf.commons.audit.protocol.NettyTLSSyslogSenderImpl.NettyDestination;

/**
 * Simple Netty client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 *
 * @author Christian Ohr
 * @since 3.7
 */
public class NettyTLSSyslogSenderImpl extends NioTLSSyslogSenderImpl<ChannelFuture, NettyDestination> {

    private static final Logger log = LoggerFactory.getLogger(NettyTLSSyslogSenderImpl.class);

    private int workerThreads = 1;
    private long connectTimeoutMillis = 5000;
    private long sendTimeoutMillis = 5000;

    public NettyTLSSyslogSenderImpl(TlsParameters tlsParameters) {
        super(tlsParameters);
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.NETTY_TLS.getProtocolName();
    }

    @Override
    protected NettyDestination makeDestination(TlsParameters tlsParameters, String host, int port, boolean logging) {
        return new NettyDestination(tlsParameters, host, port, workerThreads, connectTimeoutMillis, sendTimeoutMillis, logging);
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
    public static final class NettyDestination implements NioTLSSyslogSenderImpl.Destination<ChannelFuture> {
        private final long connectTimeout;
        private final long sendTimeout;
        @Getter
        private final Bootstrap bootstrap;
        private final EventLoopGroup workerGroup;
        // only ever holds a connection that has completed the TLS handshake
        private ChannelFuture channelFuture;
        private final String host;
        private final int port;

        NettyDestination(TlsParameters tlsParameters, String host, int port, int workerThreads,
                         long connectTimeout, long sendTimeout, boolean withLogging) {

            this.workerGroup = new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());
            this.connectTimeout = connectTimeout;
            this.sendTimeout = sendTimeout;
            this.host = host;
            this.port = port;

            // Configure the client.
            this.bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .remoteAddress(host, port)
                    .handler(new InitializerHandler(NettyUtils.initSslContext(tlsParameters, false), host, port, withLogging));
        }

        @Override
        public void shutdown() {
            if (workerGroup != null) {
                log.info("TLS Channel to Audit Repository at {}:{} is closed", host, port);
                workerGroup.shutdownGracefully(0, 10, TimeUnit.SECONDS).awaitUninterruptibly(10, TimeUnit.SECONDS);
            }
        }

        /**
         * Returns the connection to the Audit Record Repository, and establishes it if there is none or
         * the previous one has been closed. A connection is only handed out once it is fully established,
         * i.e. including the TLS handshake. Synchronized, so that concurrent senders neither open several
         * connections nor see one that is still being set up.
         *
         * @return the future of the established connection
         * @throws AuditException if the connection cannot be established
         */
        @Override
        public synchronized ChannelFuture getHandle() {
            if (channelFuture == null || !channelFuture.channel().isActive()) {
                if (channelFuture != null) {
                    channelFuture.channel().close();
                }
                channelFuture = connect();
            }
            return channelFuture;
        }

        /**
         * Connects and waits for the TLS handshake to complete, both within the connect timeout.
         */
        private ChannelFuture connect() {
            var deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(connectTimeout);
            var future = bootstrap.connect();
            try {
                if (!future.await(remainingMillis(deadline))) {
                    future.channel().close();
                    throw new AuditException("Timed out while establishing TLS connection to " + host + ":" + port);
                }
                if (!future.isSuccess()) {
                    throw new AuditException("Could not establish TLS connection to " + host + ":" + port, future.cause());
                }
                var handshake = future.channel().pipeline().get(SslHandler.class).handshakeFuture();
                if (!handshake.await(remainingMillis(deadline))) {
                    future.channel().close();
                    throw new AuditException("Timed out during TLS handshake with " + host + ":" + port);
                }
                if (!handshake.isSuccess()) {
                    future.channel().close();
                    throw new AuditException("TLS handshake with " + host + ":" + port + " failed", handshake.cause());
                }
                return future;
            } catch (InterruptedException e) {
                future.channel().close();
                Thread.currentThread().interrupt();
                throw new AuditException("Interrupted while establishing TLS connection to " + host + ":" + port, e);
            }
        }

        private static long remainingMillis(long deadline) {
            return Math.max(0, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
        }

        @Override
        public void write(byte[] bytes) {
            var channel = getHandle().channel();
            log.trace("Writing {} bytes using session: {}", bytes.length, channel);
            NettyUtils.writeAndAwait(channel, bytes, sendTimeout, host, port);
        }

        /**
         * Handler called upon channel events
         */
        private static class InboundHandler extends ChannelInboundHandlerAdapter {

            private final String host;
            private final int port;

            public InboundHandler(String host, int port) {
                this.host = host;
                this.port = port;
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                log.info("TLS Channel to Audit Repository at {}:{} is now active", host, port);
                super.channelActive(ctx);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                log.info("Exception on TLS channel to Audit Repository at {}:{}, closing it", host, port, cause);
                if (ctx != null) {
                    ctx.close();
                }
            }
        }

        /**
         * Handler called upon setup
         */
        private static class InitializerHandler extends ChannelInitializer<SocketChannel> {
            private final SslContext sslContext;
            private final String host;
            private final int port;
            private final boolean withLogging;

            public InitializerHandler(SslContext sslContext, String host, int port, boolean withLogging) {
                this.sslContext = sslContext;
                this.host = host;
                this.port = port;
                this.withLogging = withLogging;
            }

            @Override
            protected void initChannel(SocketChannel channel) {
                var pipeline = channel.pipeline();
                pipeline.addLast(sslContext.newHandler(channel.alloc(), host, port));
                pipeline.addLast(new InboundHandler(host, port));
                if (withLogging) {
                    channel.pipeline().addLast(new LoggingHandler(getClass(), LogLevel.DEBUG));
                }
            }

        }
    }


}

