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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for non-blocking TLS sender implementations.
 *
 * @param <H> handle representation of the concrete NIO framework used by subclasses
 * @author Christian Ohr
 * @since 3.7
 */
public abstract class NioTLSSyslogSenderImpl<H> extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(NioTLSSyslogSenderImpl.class);
    private boolean loggingEnabled = false;

    private Map<String, Destination<H>> destinations = new ConcurrentHashMap<>();

    public NioTLSSyslogSenderImpl() {
        super(AuditUtils.getLocalHostName(), AuditUtils.getProcessId());
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    @Override
    public void send(AuditContext auditContext, String... auditMessages) throws Exception {
        if (auditMessages != null) {
            Destination<H> destination = getDestination(auditContext.getAuditRepositoryHostName(), auditContext.getAuditRepositoryPort());
            for (String auditMessage : auditMessages) {
                byte[] msgBytes = getTransportPayload(auditContext.getSendingApplication(), auditMessage);
                byte[] length = String.format("%d ", msgBytes.length).getBytes();
                LOG.debug("Auditing to {}:{}", auditContext.getAuditRepositoryHostName(),
                        auditContext.getAuditRepositoryPort());
                destination.write(length, msgBytes);
            }
        }
    }

    private Destination<H> getDestination(String host, int port) throws Exception {
        Destination<H> destination = destinations.get(host + port);
        if (destination == null) {
            synchronized (this) {
                destination = makeDestination(host, port, loggingEnabled);
                Destination<H> existing = destinations.put(host + port, destination);
                // shutdown replaced connection
                if (existing != null) existing.shutdown();
            }
        }
        return destination;
    }

    protected abstract Destination<H> makeDestination(String host, int port, boolean logging) throws Exception;

    @Override
    public void shutdown() {
        destinations.values().forEach(Destination::shutdown);
    }

    /**
     * @param <H>
     */
    public interface Destination<H> {

        /**
         * Write the audit record to the destination
         *
         * @param bytes audit record content
         */
        void write(byte[]... bytes);

        /**
         * Shut down the destination
         */
        void shutdown();

        /**
         * @return a handle of the underlying NIO framework (e.g. a session)
         * @throws RuntimeException (or a subclass thereof) if no handle could be obtained
         */
        H getHandle();


    }

}
