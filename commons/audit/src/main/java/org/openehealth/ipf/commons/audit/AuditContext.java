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

import org.openehealth.ipf.commons.audit.codes.AuditSourceType;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;

import java.net.InetAddress;

/**
 * AuditContext is the central location where all aspects of serializing and sending out
 * Audit messages are defined. This includes
 * <ul>
 * <li>whether auditing is enabled at all</li>
 * <li>the transmission protocol (UDP, TLS, ...)</li>
 * <li>the queue implementation (synchronous, asynchronous, ...</li>
 * <li>the serialization strategy (e.g. which DICOM audit version shall be used)</li>
 * <li>global parameters like source ID, enterprise ID</li>
 * </ul>
 *
 * @author Christian Ohr
 */
public interface AuditContext {

    /**
     * @return true if auditing is enabled, false otherwise
     */
    boolean isAuditEnabled();

    void setAuditEnabled(boolean auditEnabled);

    String getAuditRepositoryHostName();

    InetAddress getAuditRepositoryAddress();

    String getSendingApplication();

    int getAuditRepositoryPort();

    /**
     * @return the wire protocol to be used
     */
    AuditTransmissionProtocol getAuditTransmissionProtocol();

    /**
     * @return the queue implementation to be used
     */
    AuditMessageQueue getAuditMessageQueue();

    /**
     * @return the serialization strategy (defaults to the latest relevant DICOM version)
     */
    default SerializationStrategy getSerializationStrategy() {
        return Current.INSTANCE;
    }

    /**
     * Sends out the audit messages as configured in this audit context
     *
     * @param messages audit messages to be sent
     */
    default void audit(AuditMessage... messages) throws Exception {
        getAuditMessageQueue().audit(this, messages);
    }

    /**
     * @return Source ID attribute of the audit event
     */
    String getAuditSourceId();

    /**
     * @return Enterprise site ID attribute of the audit event
     */
    String getAuditEnterpriseSiteId();

    /**
     * @return type of audit source
     */
    AuditSource getAuditSource();

    static AuditContext noAudit() {
        return DefaultAuditContext.NO_AUDIT;
    }
}
