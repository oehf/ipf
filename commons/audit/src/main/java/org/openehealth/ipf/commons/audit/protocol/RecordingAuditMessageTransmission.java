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

package org.openehealth.ipf.commons.audit.protocol;

import org.openehealth.ipf.commons.audit.AuditContext;

import java.util.*;

/**
 * For testing only: an implementation that records the audit message strings in memory instead of sending them to
 * some destination. After some time, this will cause OutOfMemoryErrors.
 *
 * @author Christian Ohr
 */
public class RecordingAuditMessageTransmission implements AuditTransmissionProtocol {

    private List<String> messages = new ArrayList<>();

    @Override
    public void send(AuditContext auditContext, String... auditMessages) {
        if (auditMessages != null) {
            messages.addAll(Arrays.asList(auditMessages));
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getTransport() {
        return "RECORDER";
    }


    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public Optional<String> getFirstMessage() {
        return getMessages().stream().findFirst();
    }

    /**
     * Clears the message list
     */
    public void clear() {
        messages.clear();
    }

}
