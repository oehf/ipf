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

package org.openehealth.ipf.commons.audit.queue;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Christian Ohr
 * @since 3.5
 */
public class CompositeAuditMessageQueue implements AuditMessageQueue {

    @Getter
    private List<AuditMessageQueue> queues;

    public CompositeAuditMessageQueue(List<AuditMessageQueue> queues) {
        this.queues = requireNonNull(queues);
    }

    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        queues.forEach(q -> q.audit(auditContext, auditMessages));
    }

    @Override
    public void flush() {
        queues.forEach(AuditMessageQueue::flush);
    }

    @Override
    public void shutdown() {
        queues.forEach(AuditMessageQueue::shutdown);
    }
}
