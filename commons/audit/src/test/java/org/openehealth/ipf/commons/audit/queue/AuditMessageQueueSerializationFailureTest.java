/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.audit.queue;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.marshal.SerializationStrategy;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.model.EventIdentificationType;
import org.openehealth.ipf.commons.audit.protocol.RecordingAuditMessageTransmission;
import org.openehealth.ipf.commons.audit.types.EventId;

import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * A serialization strategy that fails -- as a profiled AuditEvent does when the audit message lacks
 * something it dereferences -- must not tear down the transaction being audited. The failure belongs
 * to the configured {@link AuditExceptionHandler}, which is where a failed delivery ends up too.
 *
 * @author Christian Ohr
 * @since 5.3
 */
public class AuditMessageQueueSerializationFailureTest {

    private static class FailingSerializationStrategy implements SerializationStrategy {
        @Override
        public void marshal(AuditMessage auditMessage, Writer writer, boolean pretty) {
            throw new NullPointerException("cannot serialize this audit message");
        }
    }

    private static class RecordingAuditExceptionHandler implements AuditExceptionHandler {
        private final List<Throwable> handled = new ArrayList<>();

        @Override
        public void handleException(AuditContext auditContext, Throwable t, String auditMessage) {
            handled.add(t);
        }
    }

    @Test
    public void testSerializationFailureIsHandledInsteadOfPropagated() {
        var exceptionHandler = new RecordingAuditExceptionHandler();
        var context = auditContext(exceptionHandler);

        assertDoesNotThrow(() -> context.audit(someAuditMessage()));

        assertThat(exceptionHandler.handled, hasSize(1));
        assertThat(exceptionHandler.handled.get(0), instanceOf(NullPointerException.class));
    }

    @Test
    public void testTheOtherMessagesAreStillAudited() {
        var exceptionHandler = new RecordingAuditExceptionHandler();
        var context = auditContext(exceptionHandler);
        var transmission = new RecordingAuditMessageTransmission();
        context.setAuditTransmissionProtocol(transmission);

        context.audit(someAuditMessage(), someAuditMessage());

        // both failed to serialize, so nothing was sent, but both were reported
        assertThat(transmission.getMessages(), empty());
        assertThat(exceptionHandler.handled, hasSize(2));
    }

    private DefaultAuditContext auditContext(AuditExceptionHandler exceptionHandler) {
        var context = new DefaultAuditContext();
        context.setAuditEnabled(true);
        context.setAuditMessageQueue(new SynchronousAuditMessageQueue());
        context.setSerializationStrategy(new FailingSerializationStrategy());
        context.setAuditExceptionHandler(exceptionHandler);
        return context;
    }

    private AuditMessage someAuditMessage() {
        var eventIdentification = new EventIdentificationType(
                EventId.of("110100", "DCM", "Application Activity"),
                Instant.now(),
                EventOutcomeIndicator.Success);
        eventIdentification.setEventActionCode(EventActionCode.Execute);
        var auditMessage = new AuditMessage();
        auditMessage.setEventIdentification(eventIdentification);
        return auditMessage;
    }
}
