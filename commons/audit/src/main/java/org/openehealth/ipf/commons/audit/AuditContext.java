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

import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.queue.AuditMessageQueue;
import org.openehealth.ipf.commons.audit.types.AuditSource;

import java.net.InetAddress;
import java.util.stream.Stream;

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
 * @since 3.5
 */
public interface AuditContext {

    /**
     * @return true if auditing is enabled, false otherwise
     */
    boolean isAuditEnabled();

    /**
     * @param auditEnabled enable or disable auditing
     * @deprecated to be removed from the interface in order to prevent accidental change
     */
    void setAuditEnabled(boolean auditEnabled);

    /**
     * @return hostname of the audit repository
     */
    String getAuditRepositoryHostName();

    /**
     * @return address of the audit repository
     */
    InetAddress getAuditRepositoryAddress();

    /**
     * @return port of the audit repository
     */
    int getAuditRepositoryPort();

    /**
     * @return sending application
     */
    String getSendingApplication();


    /**
     * @return the wire protocol to be used
     */
    AuditTransmissionProtocol getAuditTransmissionProtocol();

    /**
     * @return the queue implementation to be used
     */
    AuditMessageQueue getAuditMessageQueue();

    /**
     * @return a post-processor for audit messages (defaults to a NO-OP implementation
     */
    default AuditMessagePostProcessor getAuditMessagePostProcessor() {
        return AuditMessagePostProcessor.noOp();
    }

    /**
     * @return the serialization strategy (defaults to the latest relevant DICOM version)
     */
    default SerializationStrategy getSerializationStrategy() {
        return Current.INSTANCE;
    }

    /**
     * Sends out the (potentially post-processed) audit messages as configured in this audit context
     *
     * @param messages audit messages to be sent
     */
    default void audit(AuditMessage... messages) {
        if (isAuditEnabled() && messages != null) {
            getAuditMessageQueue().audit(this, Stream.of(messages)
                    .map(getAuditMessagePostProcessor())
                    .toArray(AuditMessage[]::new));
        }
    }

    /**
     * Returns a value that is used when an otherwise mandatory attribute for an audit
     * record in missing (e.g. a participant object ID). In this case, we can still write an audit
     * record (e.g. documenting a failed request due to the missing attribute).
     * <p>
     * This can also be set to null at the risk that building the audit record might throw an
     * exception.
     *
     * @return a value that is used when an otherwise mandatory attribute for an audit record in missing
     */
    default String getAuditValueIfMissing() {
        return "UNKNOWN";
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

    /**
     * @return exception handler
     */
    AuditExceptionHandler getAuditExceptionHandler();

    /**
     * Determines whether participant object records shall be added to the audit message
     * that are derived from the response of a request. This specifically applies to
     * query results. The DICOM audit specification states that this should not be the case,
     * however, project and legal requirements sometimes mandate that e.g. patient identifiers
     * being retrieved shall be audited.
     *
     * @return true if participant object records shall be added, otherwise false
     */
    boolean isIncludeParticipantsFromResponse();

    static AuditContext noAudit() {
        return DefaultAuditContext.NO_AUDIT;
    }
}
