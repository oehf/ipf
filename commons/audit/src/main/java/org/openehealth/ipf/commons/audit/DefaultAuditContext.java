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
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.protocol.TLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.UDPSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.protocol.VertxTLSSyslogSenderImpl;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Christian Ohr
 */
public class DefaultAuditContext implements AuditContext {

    static final AuditContext NO_AUDIT = new DefaultAuditContext();

    @Getter
    private String auditRepositoryHostName = "localhost";

    @Getter
    private InetAddress auditRepositoryAddress = AuditUtils.inetAddress().orElse(null);

    @Getter @Setter
    private int auditRepositoryPort = 514;

    @Getter @Setter
    private boolean auditEnabled = false;

    @Getter @Setter
    private AuditTransmissionProtocol auditTransmissionProtocol;

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

    public void setAuditRepositoryHost(String auditRepositoryHost) throws UnknownHostException {
        this.auditRepositoryHostName = auditRepositoryHost;
        this.auditRepositoryAddress = InetAddress.getByName(auditRepositoryHost);
    }

    public String getAuditRepositoryTransport() {
        return auditTransmissionProtocol.getTransport();
    }

    public void setAuditRepositoryTransport(String transport) {
        switch (transport) {
            case "UDP": setAuditTransmissionProtocol(new UDPSyslogSenderImpl()); break;
            case "TLS": setAuditTransmissionProtocol(new TLSSyslogSenderImpl()); break;
            case "NIO-TLS": setAuditTransmissionProtocol(new VertxTLSSyslogSenderImpl()); break;
            default: throw new IllegalArgumentException("Unknown transport :" + transport);
        }
    }

}
