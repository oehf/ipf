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
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * For testing only: an implementation that records the audit message strings in memory instead of sending them to
 * some destination. After some time, this will cause OutOfMemoryErrors.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class RecordingAuditMessageTransmission implements AuditTransmissionProtocol {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void send(AuditContext auditContext, AuditMetadataProvider auditMetadataProvider, String auditMessage) {
        if (auditMessage != null) {
            messages.add(auditMessage);
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getTransportName() {
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
