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
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple client implementation of RFC 5425 TLS syslog transport for sending
 * audit messages to an Audit Record Repository that implements TLS syslog.
 * Multiple messages may be sent over the same socket.
 * <p>
 * Designed to run in a standalone mode and is not dependent on any context or
 * configuration.
 * <p>
 * <p>
 * Note that this implementation disobeys the ATNA specification saying, that
 * the Secure Application, Secure Node, or Audit Record Forwarder is unable to
 * send the message to the Audit Record Repository, then the actor shall store
 * the audit record locally and send it when it is able.
 * </p>
 *
 * @author Lawrence Tarbox, Derived from code written by Matthew Davis of IBM.
 * @author Christian Ohr
 * @since 3.5
 */
public class TLSSyslogSenderImpl extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(TLSSyslogSenderImpl.class);
    private static final int MIN_SO_TIMEOUT = 1;
    private static final Boolean DEFAULT_SOCKET_KEEPALIVE = Boolean.TRUE;

    private final AtomicReference<Socket> socket = new AtomicReference<>();
    private final SocketFactory socketFactory;
    private final SocketTestPolicy socketTestPolicy;

    /**
     * Constructor which uses default values for all parameters.
     */
    public TLSSyslogSenderImpl() {
        this(SocketTestPolicy.TEST_BEFORE_WRITE);
    }

    public TLSSyslogSenderImpl(SocketTestPolicy socketTestPolicy) {
        super();
        this.socketFactory = SSLSocketFactory.getDefault();
        this.socketTestPolicy = socketTestPolicy;
    }

    public TLSSyslogSenderImpl(TlsParameters tlsParameters) {
        this(tlsParameters.getSSLSocketFactory(), SocketTestPolicy.TEST_BEFORE_WRITE);
    }

    /**
     * @param socketFactory    SSL socket factory to be used for creating the TCP
     *                         socket.
     * @param socketTestPolicy Determining if and when to test the socket for a
     *                         connection close/reset
     */
    public TLSSyslogSenderImpl(SSLSocketFactory socketFactory, SocketTestPolicy socketTestPolicy) {
        super();
        this.socketFactory = socketFactory;
        this.socketTestPolicy = socketTestPolicy;
    }

    /**
     * @param tlsParameters    TlsParameters to be used for creating the TCP
     *                         socket.
     * @param socketTestPolicy Determining if and when to test the socket for a
     *                         connection close/reset
     */
    public TLSSyslogSenderImpl(TlsParameters tlsParameters, SocketTestPolicy socketTestPolicy) {
        this(tlsParameters.getSSLSocketFactory(), socketTestPolicy);
    }


    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.TLS.getProtocolName();
    }

    private Socket getSocket(AuditContext auditContext) {
        if (socket.get() == null)
            socket.compareAndSet(null, getTLSSocket(auditContext));
        return socket.get();
    }

    @Override
    public void send(AuditContext auditContext, AuditMetadataProvider auditMetadataProvider, String auditMessage) throws Exception {
        if (auditMessage != null) {
            var msgBytes = getTransportPayload(auditMetadataProvider, auditMessage);
            var syslogFrame = String.format("%d ", msgBytes.length).getBytes();
            LOG.debug("Auditing to {}:{}", auditContext.getAuditRepositoryHostName(), auditContext.getAuditRepositoryPort());
            if (LOG.isTraceEnabled()) {
                LOG.trace(new String(msgBytes, StandardCharsets.UTF_8));
            }
            try {
                doSend(auditContext, syslogFrame, msgBytes);
            } catch (SocketException | SocketTimeoutException e) {
                try {
                    LOG.info("Failed to use existing TLS socket. Will create a new connection and retry.");
                    closeSocket(socket.get());
                    socket.set(null);
                    doSend(auditContext, syslogFrame, msgBytes);
                } catch (Exception exception) {
                    LOG.error("Failed to audit using new TLS socket, giving up - this audit message will be lost.");
                    closeSocket(socket.get());
                    socket.set(null);
                    // re-throw the exception so caller knows what happened
                    throw exception;
                }
            }
        }
    }

    @Override
    public void shutdown() {
        if (socket.get() != null) {
            // TODO could wait until everything is sent
            closeSocket(socket.get());
        }
    }

    private synchronized void doSend(AuditContext auditContext, byte[] syslogFrame, byte[] msgBytes)
            throws IOException {
        final var socket = getSocket(auditContext);

        if (socketTestPolicy.isBeforeWrite()) {
            LOG.trace("Testing whether socket connection is alive and well before attempting to write");
            if (!isSocketConnectionAlive(socket)) {
                closeSocket(socket);
                throw new FastSocketException(
                        "Read-test before write operation determined that the socket connection is dead");
            }
            LOG.debug("Socket connection is confirmed to be alive.");
        }

        LOG.trace("Now writing out ATNA record");

        var out = socket.getOutputStream();
        out.write(syslogFrame);
        out.write(msgBytes);
        out.flush();

        LOG.debug("ATNA record has been written ({} bytes)", syslogFrame.length + msgBytes.length);

        if (socketTestPolicy.isAfterWrite()) {
            LOG.trace(
                    "Testing whether socket connection is alive and well after write to confirm the write operation");
            if (!isSocketConnectionAlive(socket)) {
                closeSocket(socket);
                throw new FastSocketException(
                        "Read-test after write operation determined that the socket connection is dead");
            }
            LOG.debug("Socket connection is confirmed alive. Assuming write operation has succeeded");
        }
    }

    private Socket getTLSSocket(AuditContext auditContext) {
        final SSLSocket socket;
        var auditRepositoryAddress = auditContext.getAuditRepositoryAddress();
        try {
            socket = (SSLSocket) socketFactory.createSocket(
                    auditRepositoryAddress,
                    auditContext.getAuditRepositoryPort());
            setSocketOptions(socket);
            if (socketTestPolicy != SocketTestPolicy.DONT_TEST_POLICY) {
                // Need to perform the SSL handshake before we set the aggressive SO_TIMEOUT,
                // otherwise the handshake will fail with a read-timeout.
                // Javadoc: "This method is synchronous for the initial handshake on a
                // connection [..]."
                socket.startHandshake();
                socket.setSoTimeout(MIN_SO_TIMEOUT);
            }
        } catch (IOException e) {
            throw new AuditException(String.format("Could not establish TLS connection to %s:%d (%s)",
                    auditContext.getAuditRepositoryHostName(),
                    auditContext.getAuditRepositoryPort(),
                    auditRepositoryAddress.getHostAddress()),
                    e);
        }
        return socket;
    }

    /**
     * Override this method to set any socket option. The default implementation
     * sets {@code SO_KEEPALIVE} to {@code true}. The method is called once for
     * every new socket instance that is created before the first ATNA record is
     * sent over that socket connection.
     * <p>
     * BEWARE: If your implementation specify any socket test policy other than
     * {@link SocketTestPolicy#DONT_TEST_POLICY}, then {@code SO_TIMEOUT} will be
     * set to 1 ms regardless of the value your implementation might set.
     *
     * @param socket Socket to configure
     * @throws SocketException if setting keep alive failed
     */
    protected void setSocketOptions(final Socket socket) throws SocketException {
        Objects.requireNonNull(socket);
        socket.setKeepAlive(DEFAULT_SOCKET_KEEPALIVE);
    }

    /**
     * Tries to determine whether the socket connection is alive and well by reading
     * from the socket's {@link InputStream input stream}. Since syslog is a simplex
     * protocol the results of the read can either be
     * <dl>
     * <dt>SocketTimeoutException</dt>
     * <dd>We assume the connection is alive</dd>
     * <dt>Read {@code -1}</dt>
     * <dd>Server closed connection</dd>
     * <dt>IOException</dt>
     * <dd>We assume the connection is dead</dd>
     * </dl>
     *
     * @param socket The socket (connection) under test
     * @return {@code true} if the connection is alive, {@code false} otherwise
     */
    private boolean isSocketConnectionAlive(final Socket socket) {
        boolean isAlive;
        try {
            if (socket.getSoTimeout() > 0) {
                final var nextByte = socket.getInputStream().read();
                if (nextByte > -1) {
                    LOG.warn(
                            "Socket test was able to read a byte from the socket other than the 'stream closed' value of -1. "
                                    + "This should never happen since SYSLOG is a simplex (write only) protocol! Byte value read from stream: {}",
                            nextByte);
                    isAlive = true;
                } else {
                    LOG.debug("Socket test read '-1' -> connection closed by server.");
                    isAlive = false;
                }
            } else {
                throw new IllegalStateException("Test requires an SO_TIMEOUT greater than zero set on the socket.");
            }
        } catch (SocketTimeoutException e) {
            LOG.debug("Socket read timed out; assuming the connection is still alive.");
            isAlive = true;
        } catch (IOException e) {
            LOG.warn("Socket read failed for non-timeout reason; assuming the connection is dead.");
            isAlive = false;
        }

        return isAlive;
    }

    /**
     * Closes socket if it is not null and has not been closed yet.
     *
     * @param socket Socket to close.
     */
    private void closeSocket(final Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                LOG.debug(
                        "Failed to close pre-existing socket. As we are either shutting down or are in the process of replacing the socket this is not really a worry... Message: {}",
                        e.getMessage());
            }
        }
    }

    /**
     * Enum to control the level of paranoia when it comes to trusting the socket
     * connection. The more paranoid the more overhead is incurred.
     *
     * @author taastrad
     */
    public enum SocketTestPolicy {

        DONT_TEST_POLICY(false, false),
        TEST_BEFORE_WRITE(true, false),
        TEST_AFTER_WRITE(false, true),
        TEST_BEFORE_AND_AFTER_WRITE(true, true);

        private final boolean beforeWrite;
        private final boolean afterWrite;

        SocketTestPolicy(boolean beforeWrite, boolean afterWrite) {
            this.beforeWrite = beforeWrite;
            this.afterWrite = afterWrite;
        }

        public boolean isBeforeWrite() {
            return beforeWrite;
        }

        public boolean isAfterWrite() {
            return afterWrite;
        }

    }

    /**
     * We use exceptions for control flow. Which is an anti pattern. In places where
     * we raise the exception we can at least make sure the overhead of creating the
     * exception is minimal by not populating the stacktrace.
     *
     * @author taastrad
     */
    private static class FastSocketException extends SocketException {
        private static final long serialVersionUID = 1L;

        public FastSocketException(final String msg) {
            super(msg);
        }

        @Override
        public Throwable fillInStackTrace() {
            // Intentional NOOP
            return null;
        }
    }

}
