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
import io.vertx.core.net.*;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * NIO implemention of a TLS Syslog sender by using an embedded Vert.x instance.
 *
 * @author Christian Ohr
 */
public class VertxTLSSyslogSenderImpl extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(VertxTLSSyslogSenderImpl.class);

    private volatile AtomicReference<String> writeHandlerId = new AtomicReference<>();
    private final Vertx vertx;

    @Setter
    private boolean trustAll;


    public VertxTLSSyslogSenderImpl() {
        this(Vertx.vertx());
    }

    public VertxTLSSyslogSenderImpl(Vertx vertx) {
        super(AuditUtils.getLocalHostName(), AuditUtils.getProcessId());
        this.vertx = vertx;
    }

    @Override
    public void send(AuditContext auditContext, String... auditMessages) {
        if (auditMessages != null) {
            for (String auditMessage : auditMessages) {

                // Could use a Vertx codec for this
                byte[] msgBytes = getTransportPayload(auditContext.getSendingApplication(), auditMessage);
                byte[] syslogFrame = String.format("%d ", msgBytes.length).getBytes();
                LOG.debug("Auditing to {}:{}",
                        auditContext.getAuditRepositoryAddress().getHostAddress(),
                        auditContext.getAuditRepositoryPort());
                LOG.trace("{}", new String(msgBytes, StandardCharsets.UTF_8));
                Buffer buffer = new BufferImpl()
                        .appendBytes(syslogFrame)
                        .appendBytes(msgBytes);

                // The net socket has registered itself on the Vertx EventBus
                vertx.eventBus().send(ensureEstablishedConnection(auditContext), buffer);
            }
        }
    }

    @Override
    public String getTransport() {
        return "NIO-TLS";
    }

    @Override
    public void shutdown() {
        vertx.close();
    }

    private String ensureEstablishedConnection(AuditContext auditContext) {
        if (writeHandlerId.get() == null) {
            CountDownLatch latch = new CountDownLatch(1);
            NetClientOptions options = new NetClientOptions()
                    .setConnectTimeout(1000)
                    .setReconnectAttempts(5)
                    .setReconnectInterval(1000)
                    .setSsl(true);

            if (trustAll) {
                options.setTrustAll(true);
            } else {
                initializeTLSParameters(options);
            }

            NetClient client = vertx.createNetClient(options);
            client.connect(
                    auditContext.getAuditRepositoryPort(),
                    auditContext.getAuditRepositoryAddress().getHostAddress(),
                    event -> {
                        LOG.info("Attempt to connect to {}:{} : {}",
                                auditContext.getAuditRepositoryAddress().getHostAddress(),
                                auditContext.getAuditRepositoryPort(),
                                event.succeeded());
                        if (event.succeeded()) {
                            NetSocket socket = event.result();
                            socket
                                    .exceptionHandler(exceptionEvent -> {
                                        LOG.info("Audit Connection caught exception", exceptionEvent);
                                        writeHandlerId.set(null);
                                        socket.close();
                                    })
                                    .closeHandler(closeEvent -> {
                                        LOG.info("Audit Connection closed");
                                        writeHandlerId.set(null);
                                    });
                            writeHandlerId.compareAndSet(null, socket.writeHandlerID());
                            latch.countDown();
                        }
                    });

            // Ensure that connection is established before returning
            try {
                latch.await(10000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new AuditException(String.format("Could not establish TLS connection to %s:%d",
                        auditContext.getAuditRepositoryAddress().getHostAddress(),
                        auditContext.getAuditRepositoryPort()));
            }
        }
        return writeHandlerId.get();
    }

    private void initializeTLSParameters(NetClientOptions options) {
        String keyStoreType = System.getProperty(JAVAX_NET_SSL_KEYSTORE_TYPE, KeyStore.getDefaultType());
        if ("JKS".equalsIgnoreCase(keyStoreType)) {
            options.setKeyStoreOptions(new JksOptions()
                    .setPath(System.getProperty(JAVAX_NET_SSL_KEYSTORE))
                    .setPassword(System.getProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD)));
        } else {
            options.setPfxKeyCertOptions(new PfxOptions()
                    .setPath(System.getProperty(JAVAX_NET_SSL_KEYSTORE))
                    .setPassword(System.getProperty(JAVAX_NET_SSL_KEYSTORE_PASSWORD)));
        }
        String trustStoreType = System.getProperty(JAVAX_NET_SSL_TRUSTSTORE_TYPE, KeyStore.getDefaultType());
        if ("JKS".equalsIgnoreCase(trustStoreType)) {
            options.setTrustStoreOptions(new JksOptions()
                    .setPath(System.getProperty(JAVAX_NET_SSL_TRUSTSTORE))
                    .setPassword(System.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD)));
        } else {
            options.setPfxTrustOptions(new PfxOptions()
                    .setPath(System.getProperty(JAVAX_NET_SSL_TRUSTSTORE))
                    .setPassword(System.getProperty(JAVAX_NET_SSL_TRUSTSTORE_PASSWORD)));
        }
        String allowedProtocols = System.getProperty(JDK_TLS_CLIENT_PROTOCOLS, "TLSv1.2");
        Stream.of(allowedProtocols.split("\\s*,\\s*"))
                .forEach(options::addEnabledSecureTransportProtocol);

        String allowedCiphers = System.getProperty(HTTPS_CIPHERSUITES);
        if (allowedCiphers != null) {
            Stream.of(allowedCiphers.split("\\s*,\\s*"))
                    .forEach(options::addEnabledCipherSuite);
        }
    }

}
