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
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * NIO implemention of a UDP Syslog sender by using an embedded Vert.x instance.
 * There is no obvious performance improvement compared to {@link UDPSyslogSenderImpl}.
 *
 * @author Christian Ohr
 */
public class VertxUDPSyslogSenderImpl extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(VertxUDPSyslogSenderImpl.class);

    private final Vertx vertx;

    public VertxUDPSyslogSenderImpl() {
        this(Vertx.vertx());
    }

    public VertxUDPSyslogSenderImpl(Vertx vertx) {
        super(AuditUtils.getLocalHostName(), AuditUtils.getProcessId());
        this.vertx = vertx;
    }

    @Override
    public void send(AuditContext auditContext, String... auditMessages) {
        DatagramSocketOptions options = new DatagramSocketOptions()
                .setSendBufferSize(16384);
        DatagramSocket socket = vertx.createDatagramSocket(options);
        if (auditMessages != null) {
            for (String auditMessage : auditMessages) {

                // Could use a Vertx codec for this
                byte[] msgBytes = getTransportPayload(auditContext.getSendingApplication(), auditMessage);
                LOG.debug("Auditing to {}:{}",
                        auditContext.getAuditRepositoryAddress().getHostAddress(),
                        auditContext.getAuditRepositoryPort());
                LOG.trace("{}", new String(msgBytes, StandardCharsets.UTF_8));
                Buffer buffer = new BufferImpl().appendBytes(msgBytes);

                // The net socket has registered itself on the Vertx EventBus
                socket.send(buffer,
                        auditContext.getAuditRepositoryPort(),
                        auditContext.getAuditRepositoryAddress().getHostAddress(),
                        event -> {
                            if (event.failed()) {
                                LOG.warn("Sending Audit Event via UDP failed");
                            }
                        });
            }
        }
    }

    @Override
    public String getTransport() {
        return "NIO-UDP";
    }

    @Override
    public void shutdown() {
        vertx.close();
    }


}
