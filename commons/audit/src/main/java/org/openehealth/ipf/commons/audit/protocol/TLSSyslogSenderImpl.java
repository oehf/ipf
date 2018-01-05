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
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple client implementation of RFC 5425 TLS syslog transport
 * for sending audit messages to an Audit Record Repository
 * that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 * <p>
 * Designed to run in a standalone mode from the standard IHE Auditor
 * and is not dependent on any context or configuration.
 * <p>
 * TODO provide a NIO-based sender using Netty/Mina
 *
 * @author <a href="mailto:tarboxl@mir.wustl.edu">Lawrence Tarbox</a>
 * @author Christian Ohr
 * Derived from code written by Matthew Davis of IBM.
 */
public class TLSSyslogSenderImpl extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(TLSSyslogSenderImpl.class);

    private AtomicReference<Socket> socket = new AtomicReference<>();

    public TLSSyslogSenderImpl() {
        this(AuditUtils.getLocalHostName(), AuditUtils.getProcessId());
    }

    public TLSSyslogSenderImpl(String sendingHost, String sendingProcess) {
        super(sendingHost, sendingProcess);
    }

    @Override
    public String getTransport() {
        return "TLS";
    }

    private Socket getSocket(AuditContext auditContext) {
        if (socket.get() == null) socket.compareAndSet(null, getTLSSocket(auditContext));
        return socket.get();
    }

    @Override
    public void send(AuditContext auditContext, String... auditMessages) throws Exception {
        if (auditMessages != null) {
            for (String auditMessage : auditMessages) {
                byte[] msgBytes = getTransportPayload(auditContext.getSendingApplication(), auditMessage);
                byte[] syslogFrame = String.format("%d ", msgBytes.length).getBytes();
                LOG.debug("Auditing to {}:{}",
                        auditContext.getAuditRepositoryAddress().getHostAddress(),
                        auditContext.getAuditRepositoryPort());
                LOG.trace("{}", new String(msgBytes, StandardCharsets.UTF_8));
                try {
                    doSend(auditContext, syslogFrame, msgBytes);
                } catch (SocketException e) {
                    try {
                        LOG.info("Failed to use existing TLS socket. Will create a new connection and retry.");
                        socket.set(null);
                        doSend(auditContext, syslogFrame, msgBytes);
                    } catch (Exception exception) {
                        LOG.error("Still exception trying to audit to TLS socket. Cannot audit to repository - this audit message will be lost.");
                        socket.set(null);
                        // rethrow the exception so caller knows what happened
                        throw exception;
                    }
                }
            }
        }
    }

    private synchronized void doSend(AuditContext auditContext, byte[] syslogFrame, byte[] msgBytes) throws IOException {
        Socket socket = getSocket(auditContext);
        OutputStream out = socket.getOutputStream();
        out.write(syslogFrame);
        out.write(msgBytes);
        out.flush();
    }

    private Socket getTLSSocket(AuditContext auditContext) {
        try {
            return SSLSocketFactory.getDefault()
                    .createSocket(auditContext.getAuditRepositoryAddress(), auditContext.getAuditRepositoryPort());
        } catch (IOException e) {
            throw new AuditException(String.format("Could not establish TLS connection to %s:%d",
                    auditContext.getAuditRepositoryAddress().getHostAddress(),
                    auditContext.getAuditRepositoryPort()), e);
        }
    }

}
