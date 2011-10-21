/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender

/**
 * Mocked sender implementation for ATNA messages. 
 * Records the messages to allow verification in tests.
 * @author Jens Riemschneider
 */
class MockedSender implements AuditMessageSender {
    List<AuditEventMessage> messages = Collections.synchronizedList(new ArrayList<AuditEventMessage>())

    void sendAuditEvent(AuditEventMessage[] msg) {
        messages.addAll(Arrays.asList(msg))
    }
    
    void sendAuditEvent(AuditEventMessage[] msg, InetAddress destination, int port) {
        sendAuditEvent(msg)
    }
}
