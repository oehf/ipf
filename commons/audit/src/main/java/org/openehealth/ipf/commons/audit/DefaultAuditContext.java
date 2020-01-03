/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.AuditSourceType;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.handler.LoggingAuditExceptionHandler;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.protocol.TLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.UDPSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.VertxTLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class DefaultAuditContext implements AuditContext {

    static final AuditContext NO_AUDIT = new DefaultAuditContext();

    @Getter @Setter
    private String auditRepositoryHostName = "localhost";

    @Getter @Setter
    private int auditRepositoryPort = 514;

    @Getter @Setter
    private boolean auditEnabled = false;

    @Getter @Setter
    private AuditTransmissionProtocol auditTransmissionProtocol = new UDPSyslogSenderImpl();

    @Getter @Setter
    private AuditMessageQueue auditMessageQueue = new SynchronousAuditMessageQueue();

    @Getter @Setter
    private String sendingApplication = "IPF";

    @Getter @Setter
    private String auditSourceId = "IPF";

    @Getter @Setter
    private String auditEnterpriseSiteId = "IPF";

    @Getter @Setter
    private AuditSource auditSource = AuditSourceType.Other;

    @Getter @Setter
    private SerializationStrategy serializationStrategy = new Current();

    @Getter @Setter
    private AuditMessagePostProcessor auditMessagePostProcessor = AuditMessagePostProcessor.noOp();

    @Getter @Setter
    private AuditExceptionHandler auditExceptionHandler = new LoggingAuditExceptionHandler();

    @Getter @Setter
    private boolean includeParticipantsFromResponse = false;

    @Getter @Setter
    private String auditValueIfMissing = "UNKNOWN";

    public String getAuditRepositoryTransport() {
        return auditTransmissionProtocol.getTransportName();
    }

    public void setAuditRepositoryHost(String host) {
        setAuditRepositoryHostName(host);
    }

    public void setAuditRepositoryTransport(String transport) {
        switch (transport) {
            case "UDP": setAuditTransmissionProtocol(new UDPSyslogSenderImpl()); break;
            case "TLS": setAuditTransmissionProtocol(new TLSSyslogSenderImpl()); break;
            case "NIO-TLS": setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl()); break;
            default: throw new IllegalArgumentException("Unknown transport :" + transport);
        }
    }

    @Override
    public InetAddress getAuditRepositoryAddress() {
        try {
            return InetAddress.getByName(auditRepositoryHostName);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
