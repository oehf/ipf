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
package org.openehealth.ipf.commons.audit.queue;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

/**
 * Determine the timing and priority in which audit messages are delivered.
 * Examples for implementations are synchronous, asynchronous or JMS-based.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface AuditMessageQueue {

    String X_IPF_ATNA_TIMESTAMP = "X_IPF_ATNA_Timestamp";
    String X_IPF_ATNA_HOSTNAME = "X_IPF_ATNA_Hostname";
    String X_IPF_ATNA_PROCESSID = "X_IPF_ATNA_ProcessID";
    String X_IPF_ATNA_APPLICATION = "X_IPF_ATNA_Application";

    /**
     *
     * @param auditContext IPF audit context
     * @param auditMessages one or more audit message instances
     */
    void audit(AuditContext auditContext, AuditMessage... auditMessages);

    /**
     * Forces all unsent messages in the queue to be sent
     */
    default void flush() {}

    /**
     * Flushes the queue and shutdown any associated runtime daemons that
     * may be handling queue inflow/outflow
     */
    default void shutdown() {}
}
