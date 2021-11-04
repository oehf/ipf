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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * Simple UDP sender that opens a new DatagramSocket for every batch of AuditMessages
 * being sent.
 * <p>
 * Note that this implementation disobeys the ATNA specification saying,
 * that the Secure Application, Secure Node, or Audit Record Forwarder is unable to send the
 * message to the Audit Record Repository, then the actor shall store the audit record
 * locally and send it when it is able.
 * </p>
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class UDPSyslogSenderImpl extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(UDPSyslogSenderImpl.class);
    private static final int MAX_DATAGRAM_PACKET_SIZE = 65479;

    public UDPSyslogSenderImpl() {
        super();
    }

    @SuppressWarnings("unused")
    public UDPSyslogSenderImpl(TlsParameters tlsParameters) {
        super();
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.UDP.getProtocolName();
    }

    @Override
    public void send(AuditContext auditContext, AuditMetadataProvider auditMetadataProvider, String auditMessage) throws Exception {
        if (auditMessage != null) {
            try (var socket = new DatagramSocket()) {
                var msgBytes = getTransportPayload(auditMetadataProvider, auditMessage);
                var inetAddress = auditContext.getAuditRepositoryAddress();
                LOG.debug("Auditing {} bytes to {}:{} ({})",
                        msgBytes.length,
                        auditContext.getAuditRepositoryHostName(),
                        auditContext.getAuditRepositoryPort(),
                        inetAddress.getHostAddress());
                var packet = new DatagramPacket(
                        msgBytes,
                        Math.min(MAX_DATAGRAM_PACKET_SIZE, msgBytes.length),
                        inetAddress,
                        auditContext.getAuditRepositoryPort());
                socket.send(packet);
                if (LOG.isTraceEnabled()) {
                    LOG.trace(new String(msgBytes, StandardCharsets.UTF_8));
                }
            }
        }
    }

    @Override
    public void shutdown() {
    }
}
