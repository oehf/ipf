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

package org.openehealth.ipf.commons.audit.protocol;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferImpl;
import io.vertx.core.net.NetClientOptions;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.openehealth.ipf.commons.audit.VertxTlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NIO implementation of a TLS Syslog sender by using an embedded Vert.x instance.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class VertxTLSSyslogSenderImpl extends NioTLSSyslogSenderImpl<String, VertxTLSSyslogSenderImpl.VertxDestination> {

    private static final Logger LOG = LoggerFactory.getLogger(VertxTLSSyslogSenderImpl.class);

    private final Vertx vertx;

    public VertxTLSSyslogSenderImpl() {
        this(VertxTlsParameters.getDefault());
    }

    public VertxTLSSyslogSenderImpl(TlsParameters tlsParameters) {
        this(Vertx.vertx(), tlsParameters);
    }

    public VertxTLSSyslogSenderImpl(Vertx vertx) {
        this(vertx, VertxTlsParameters.getDefault());
    }

    public VertxTLSSyslogSenderImpl(Vertx vertx, TlsParameters tlsParameters) {
        super(tlsParameters);
        this.vertx = vertx;
    }

    @Override
    protected VertxDestination makeDestination(TlsParameters tlsParameters, String host, int port, boolean logging) {
        return new VertxTLSSyslogSenderImpl.VertxDestination(vertx, (VertxTlsParameters) tlsParameters, host, port);
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.VERTX_TLS.getProtocolName();
    }

    @Override
    protected void customizeDestination(VertxDestination destination) {
    }

    public static class VertxDestination implements NioTLSSyslogSenderImpl.Destination<String> {

        private final Vertx vertx;
        private final VertxTlsParameters tlsParameters;
        private final String host;
        private final int port;
        private final AtomicReference<String> writeHandlerId = new AtomicReference<>();

        public VertxDestination(Vertx vertx, VertxTlsParameters tlsParameters, String host, int port) {
            this.vertx = vertx;
            this.tlsParameters = tlsParameters;
            this.host = host;
            this.port = port;
        }

        @Override
        public void write(byte[]... bytes) {
            Buffer buffer = new BufferImpl();
            for (var b : bytes) buffer.appendBytes(b);
            vertx.eventBus().send(getHandle(), buffer);
        }

        @Override
        public void shutdown() {
            vertx.close();
        }

        @Override
        public String getHandle() {
            if (writeHandlerId.get() == null) {
                var latch = new CountDownLatch(1);
                var options = new NetClientOptions()
                        .setConnectTimeout(1000)
                        .setReconnectAttempts(5)
                        .setReconnectInterval(1000)
                        .setSsl(true);

                tlsParameters.initNetClientOptions(options);

                var client = vertx.createNetClient(options);
                client.connect(
                        port,
                        host,
                        event -> {
                            LOG.info("Attempt to connect to {}:{}, : {}",
                                    host,
                                    port,
                                    event.succeeded());
                            if (event.succeeded()) {
                                var socket = event.result();
                                socket
                                        .exceptionHandler(exceptionEvent -> {
                                            LOG.info("Audit Connection caught exception", exceptionEvent);
                                            writeHandlerId.set(null);
                                            client.close();
                                        })
                                        .closeHandler(closeEvent -> {
                                            LOG.info("Audit Connection closed");
                                            writeHandlerId.set(null);
                                            client.close();
                                        });
                                writeHandlerId.compareAndSet(null, socket.writeHandlerID());
                                latch.countDown();
                            }
                        });

                // Ensure that connection is established before returning
                try {
                    latch.await(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new AuditException(String.format("Could not establish TLS connection to %s:%d",
                            host,
                            port));
                }
            }
            return writeHandlerId.get();
        }

    }
}
