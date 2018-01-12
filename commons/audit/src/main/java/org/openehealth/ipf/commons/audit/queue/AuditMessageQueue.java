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
 * Determine the timing and priority in which audit messages are delivered to the
 * transmission protocol sender. Examples for implementations are synchronous,
 * asynchronous or JMS-based.
 */
public interface AuditMessageQueue {

    void audit(AuditContext auditContext, AuditMessage... auditMessages) throws Exception;

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
