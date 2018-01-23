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

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.util.*;

/**
 * For testing only: an implementation that records the audit messages in memory.
 * After some time, this will cause OutOfMemoryErrors.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class RecordingAuditMessageQueue implements AbstractMockedAuditMessageQueue {

    private List<AuditMessage> messages = new ArrayList<>();

    @Override
    public void audit(AuditContext auditContext, AuditMessage... auditMessages) {
        messages.addAll(Arrays.asList(auditMessages));
    }


    public List<AuditMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public Optional<AuditMessage> getFirstMessage() {
        return getMessages().stream().findFirst();
    }

    /**
     * Clears the message list
     */
    public void clear() {
        messages.clear();
    }

}
