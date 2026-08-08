/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.core.atna;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.audit.handler.AuditExceptionHandler;
import org.openehealth.ipf.commons.audit.model.AuditMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Auditing runs in the {@code finally} of the interceptors, so anything thrown while assembling an audit
 * record would replace the outcome of the transaction being audited. An encrypted access token used to do
 * exactly that: the claims extractor dereferenced a null claim set and the served request came back as a
 * server error.
 *
 * @author Christian Ohr
 */
public class AuditFailureIsolationTest {

    @Test
    public void testAFailureWhileBuildingTheRecordDoesNotReachTheCaller() {
        var handled = new AtomicReference<Throwable>();
        var auditContext = auditContextWith(handled);
        var boom = new NullPointerException("cannot read the claims of an encrypted token");

        assertDoesNotThrow(() -> failingStrategy(boom).doAudit(auditContext, auditDataset()));

        assertSame(boom, handled.get(), "the failure did not reach the audit exception handler");
    }

    @Test
    public void testAFailureWhileDeliveringTheRecordDoesNotReachTheCallerEither() {
        var handled = new AtomicReference<Throwable>();
        var auditContext = auditContextWith(handled);
        auditContext.setAuditMessageQueue((context, messages) -> {
            throw new IllegalStateException("audit repository unreachable");
        });

        assertDoesNotThrow(() -> succeedingStrategy().doAudit(auditContext, auditDataset()));

        assertNotNull(handled.get());
        assertInstanceOf(IllegalStateException.class, handled.get());
    }

    @Test
    public void testTheRecordIsStillAuditedWhenNothingFails() {
        var audited = new AtomicReference<AuditMessage[]>();
        var auditContext = auditContextWith(new AtomicReference<>());
        auditContext.setAuditMessageQueue((context, messages) -> audited.set(messages));

        succeedingStrategy().doAudit(auditContext, auditDataset());

        assertNotNull(audited.get());
        assertEquals(1, audited.get().length);
    }

    private DefaultAuditContext auditContextWith(AtomicReference<Throwable> handled) {
        var auditContext = new DefaultAuditContext();
        auditContext.setAuditEnabled(true);
        auditContext.setAuditExceptionHandler(rememberingHandler(handled));
        return auditContext;
    }

    private AuditExceptionHandler rememberingHandler(AtomicReference<Throwable> handled) {
        return (auditContext, throwable, auditRecord) -> handled.set(throwable);
    }

    private AuditStrategySupport<AuditDataset> failingStrategy(RuntimeException failure) {
        return new AuditStrategySupport<>(true) {
            @Override
            public AuditMessage[] makeAuditMessage(AuditContext auditContext, AuditDataset auditDataset) {
                throw failure;
            }

            @Override
            public AuditDataset createAuditDataset() {
                return auditDataset();
            }
        };
    }

    private AuditStrategySupport<AuditDataset> succeedingStrategy() {
        return new AuditStrategySupport<>(true) {
            @Override
            public AuditMessage[] makeAuditMessage(AuditContext auditContext, AuditDataset auditDataset) {
                return new AuditMessage[]{new AuditMessage()};
            }

            @Override
            public AuditDataset createAuditDataset() {
                return auditDataset();
            }
        };
    }

    private AuditDataset auditDataset() {
        return new AuditDataset(true) {
            @Override public String getSourceUserId() { return null; }
            @Override public String getDestinationUserId() { return null; }
            @Override public String getLocalAddress() { return null; }
            @Override public String getRemoteAddress() { return null; }
            @Override public List<HumanUser> getHumanUsers() { return List.of(); }
        };
    }

}
