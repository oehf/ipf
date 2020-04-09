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
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.TlsParameters;
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
public abstract class NioTLSSyslogSenderImpl<H, D extends NioTLSSyslogSenderImpl.Destination<H>> extends RFC5424Protocol implements AuditTransmissionProtocol {

    private static final Logger LOG = LoggerFactory.getLogger(NioTLSSyslogSenderImpl.class);
    private boolean loggingEnabled = false;
    private final TlsParameters tlsParameters;

    private final Map<String, D> destinations = new ConcurrentHashMap<>();

    public NioTLSSyslogSenderImpl(TlsParameters tlsParameters) {
        super();
        this.tlsParameters = tlsParameters;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    /**
     * Allows to customize the destination, e.g. to set network-specific parameters
     * @param destination destination used for the connection
     */
    protected abstract void customizeDestination(D destination);

    @Override
    public void send(AuditContext auditContext, AuditMetadataProvider auditMetadataProvider, String auditMessage) throws Exception {
        if (auditMessage != null) {
            Destination<H> destination = getDestination(auditContext.getAuditRepositoryHostName(), auditContext.getAuditRepositoryPort());
            byte[] msgBytes = getTransportPayload(auditMetadataProvider, auditMessage);
            byte[] length = String.format("%d ", msgBytes.length).getBytes();
            LOG.debug("Auditing to {}:{}", auditContext.getAuditRepositoryHostName(),
                    auditContext.getAuditRepositoryPort());
            destination.write(length, msgBytes);
        }
    }

    private D getDestination(String host, int port) throws Exception {
        D destination = destinations.get(host + port);
        if (destination == null) {
            synchronized (this) {
                destination = makeDestination(tlsParameters, host, port, loggingEnabled);
                customizeDestination(destination);
                Destination<H> existing = destinations.put(host + port, destination);
                // shutdown replaced connection
                if (existing != null) existing.shutdown();
            }
        }
        return destination;
    }

    protected abstract D makeDestination(TlsParameters tlsParameters, String host, int port, boolean logging);

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
