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

import org.junit.Test;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;

import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 *
 */
public class AsynchronousAuditMessageQueueTest {

    @Test
    public void sendMessageWithoutExecutor() throws Exception {
        final AuditTransmissionProtocol messageSender = mock(AuditTransmissionProtocol.class);
        final DefaultAuditContext context = new DefaultAuditContext();
        context.setAuditEnabled(true);
        AsynchronousAuditMessageQueue queue = new AsynchronousAuditMessageQueue();
        context.setAuditMessageQueue(queue);
        context.setAuditTransmissionProtocol(messageSender);

        AuditMessage auditMessage = someAuditEventMessage();
        context.audit(auditMessage);

        verify(messageSender).send(context, Current.toString(auditMessage, false));
        verifyNoMoreInteractions(messageSender);
    }

    @Test
    public void sendMessageWithExecutor() throws Exception {
        final AuditTransmissionProtocol messageSender = mock(AuditTransmissionProtocol.class);
        final DefaultAuditContext context = new DefaultAuditContext();
        context.setAuditEnabled(true);
        AsynchronousAuditMessageQueue queue = new AsynchronousAuditMessageQueue();
        context.setAuditMessageQueue(queue);
        context.setAuditTransmissionProtocol(messageSender);
        try {
            queue.setExecutorService(Executors.newSingleThreadExecutor());
            AuditMessage auditMessage = someAuditEventMessage();
            context.audit(auditMessage);

            Thread.sleep(500);
            verify(messageSender).send(context, Current.toString(auditMessage, false));
            verifyNoMoreInteractions(messageSender);
        } finally {
            queue.shutdown();
        }
    }

    private AuditMessage someAuditEventMessage() {
        return mock(AuditMessage.class);
    }

}
