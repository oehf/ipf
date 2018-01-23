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
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
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
        this(AuditUtils.getLocalHostName(), AuditUtils.getProcessId());
    }

    public UDPSyslogSenderImpl(String sendingHost, String sendingProcess) {
        super(sendingHost, sendingProcess);
    }

    @Override
    public String getTransportName() {
        return "UDP";
    }

    @Override
    public void send(AuditContext auditContext, String... auditMessages) throws Exception {
        if (auditMessages != null) {
            try (DatagramSocket socket = new DatagramSocket()) {
                for (String auditMessage : auditMessages) {
                    byte[] msgBytes = getTransportPayload(auditContext.getSendingApplication(), auditMessage);
                    LOG.debug("Auditing to {}:{}",
                            auditContext.getAuditRepositoryAddress().getHostAddress(),
                            auditContext.getAuditRepositoryPort());
                    LOG.trace("{}", new String(msgBytes, StandardCharsets.UTF_8));
                    DatagramPacket packet = new DatagramPacket(
                            msgBytes,
                            Math.min(MAX_DATAGRAM_PACKET_SIZE, msgBytes.length),
                            auditContext.getAuditRepositoryAddress(),
                            auditContext.getAuditRepositoryPort());
                    socket.send(packet);
                }
            }
        }
    }

    @Override
    public void shutdown() {

    }
}
