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

import io.micrometer.context.ThreadLocalAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.audit.protocol.RecordingAuditMessageTransmission;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

/**
 *
 */
public class AsynchronousAuditMessageQueueTest {

    @Test
    public void sendMessageWithoutExecutor() {
        final var context = new DefaultAuditContext();
        context.setAuditEnabled(true);
        final var auditMessageTransmission = new RecordingAuditMessageTransmission();
        var queue = new AsynchronousAuditMessageQueue();
        context.setAuditMessageQueue(queue);
        context.setAuditTransmissionProtocol(auditMessageTransmission);

        var auditMessage = someAuditEventMessage();
        context.audit(auditMessage);
        assertThat(auditMessageTransmission.getMessages(), hasSize(1));
        assertThat(auditMessageTransmission.getFirstMessage().orElseThrow(), equalTo(Current.toString(auditMessage, false)));
    }

    @Test
    public void sendMessageWithExecutorAndThreadLocal() throws Exception {
        final var context = new DefaultAuditContext();
        context.getContextRegistry().registerThreadLocalAccessor(new CustomThreadLocalAccessor());
        context.setAuditEnabled(true);

        final var auditMessageTransmission = new RecordingAuditMessageTransmission();
        final var value = "someValue";
        CustomThreadLocalHolder.set(value);
        // assert that value has been transferred to audit thread
        auditMessageTransmission.setConsumer(s -> assertThat(CustomThreadLocalHolder.get(), equalTo(value)));

        var queue = new AsynchronousAuditMessageQueue();
        context.setAuditMessageQueue(queue);
        context.setAuditTransmissionProtocol(auditMessageTransmission);

        try {
            queue.setExecutorService(Executors.newSingleThreadExecutor());
            var auditMessage = someAuditEventMessage();
            context.audit(auditMessage);

            Thread.sleep(500);
            assertThat(auditMessageTransmission.getMessages(), hasSize(1));
            assertThat(auditMessageTransmission.getFirstMessage().orElseThrow(), equalTo(Current.toString(auditMessage, false)));
        } finally {
            queue.shutdown();
        }
    }

    private AuditMessage someAuditEventMessage() {
        return mock(AuditMessage.class);
    }

    private static class CustomThreadLocalHolder {
        private static final ThreadLocal<String> value = new ThreadLocal<>();

        public static void set(String newValue) {
            value.set(newValue);
        }

        public static String get() {
            return value.get();
        }

        public static void remove() {
            value.remove();
        }
    }

    private static class CustomThreadLocalAccessor implements ThreadLocalAccessor<String> {

        public static final String KEY = "value";

        @Override
        public String getValue() {
            return CustomThreadLocalHolder.get();
        }

        @Override
        public Object key() {
            return KEY;
        }

        @Override
        public void setValue(String value) {
            CustomThreadLocalHolder.set(value);
        }

        @Override
        public void setValue() {
            CustomThreadLocalHolder.remove();
        }
    }

}
